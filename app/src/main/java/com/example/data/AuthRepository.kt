package com.example.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.data.supabase.SupabaseAccountDto
import com.example.data.supabase.SupabaseClient
import com.example.data.supabase.SupabaseRepository
import com.example.model.Gender
import com.example.util.GoogleAuthHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.security.MessageDigest

data class AuthResult(
    val success: Boolean,
    val message: String,
    val username: String? = null,
    val displayName: String? = null,
    val lovyId: String? = null,
    val gender: Gender = Gender.FEMALE,
    val bio: String? = null,
    val avatarUrl: String? = null,
    val isGoogleUser: Boolean = false
)

data class SavedSession(
    val isLoggedIn: Boolean,
    val isGuest: Boolean,
    val lovyId: String,
    val username: String,
    val displayName: String,
    val gender: Gender = Gender.FEMALE,
    val bio: String = "",
    val avatarUrl: String? = null,
    val isGoogleUser: Boolean = false
)

class AuthRepository(
    context: Context,
    private val supabaseRepo: SupabaseRepository? = null
) {
    private val prefs: SharedPreferences = context.getSharedPreferences("lovy_auth_store", Context.MODE_PRIVATE)
    private val TAG = "AuthRepository"

    companion object {
        private const val KEY_REGISTERED_USERS = "registered_users_map"
        private const val KEY_GOOGLE_USERS = "registered_google_users"
        private const val KEY_SAVED_SESSION = "current_active_session"

        fun hashPassword(password: String): String {
            return try {
                val md = MessageDigest.getInstance("SHA-256")
                val digest = md.digest(password.toByteArray(Charsets.UTF_8))
                digest.fold("") { str, it -> str + "%02x".format(it) }
            } catch (_: Exception) {
                password
            }
        }

        fun isPasswordMatching(stored: String?, input: String): Boolean {
            if (stored.isNullOrEmpty()) return false
            val hashedInput = hashPassword(input)
            return stored == hashedInput || stored == input
        }
    }

    // ==================== Local Fallback Storage ====================

    private fun getUsersMap(): MutableMap<String, String> {
        val jsonStr = prefs.getString(KEY_REGISTERED_USERS, null) ?: return mutableMapOf()
        val result = mutableMapOf<String, String>()
        try {
            val json = JSONObject(jsonStr)
            val keys = json.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                result[key.lowercase()] = json.getString(key)
            }
        } catch (_: Exception) {
        }
        return result
    }

    private fun saveUsersMap(map: Map<String, String>) {
        val json = JSONObject()
        for ((k, v) in map) {
            json.put(k.lowercase(), v)
        }
        prefs.edit().putString(KEY_REGISTERED_USERS, json.toString()).apply()
    }

    // ==================== Session Management ====================

    fun getSavedSession(): SavedSession? {
        val jsonStr = prefs.getString(KEY_SAVED_SESSION, null) ?: return null
        return try {
            val json = JSONObject(jsonStr)
            val isLoggedIn = json.optBoolean("is_logged_in", false)
            if (!isLoggedIn) return null
            SavedSession(
                isLoggedIn = true,
                isGuest = json.optBoolean("is_guest", false),
                lovyId = json.optString("lovy_id", "lovy_${(100000..999999).random()}"),
                username = json.optString("username", "user"),
                displayName = json.optString("display_name", "Pengguna Lovy"),
                gender = if (json.optString("gender", "FEMALE").equals("MALE", ignoreCase = true)) Gender.MALE else Gender.FEMALE,
                bio = json.optString("bio", ""),
                avatarUrl = json.optString("avatar_url").takeIf { it.isNotBlank() },
                isGoogleUser = json.optBoolean("is_google_user", false)
            )
        } catch (_: Exception) {
            null
        }
    }

    fun saveSession(session: SavedSession) {
        try {
            val json = JSONObject().apply {
                put("is_logged_in", session.isLoggedIn)
                put("is_guest", session.isGuest)
                put("lovy_id", session.lovyId)
                put("username", session.username)
                put("display_name", session.displayName)
                put("gender", session.gender.name)
                put("bio", session.bio)
                put("avatar_url", session.avatarUrl ?: "")
                put("is_google_user", session.isGoogleUser)
            }
            prefs.edit().putString(KEY_SAVED_SESSION, json.toString()).apply()
        } catch (e: Exception) {
            Log.w(TAG, "Gagal menyimpan session lokal", e)
        }
    }

    fun clearSession() {
        prefs.edit().remove(KEY_SAVED_SESSION).apply()
    }

    fun updateAvatarUrl(newAvatarUrl: String?) {
        val current = getSavedSession() ?: return
        saveSession(current.copy(avatarUrl = newAvatarUrl))
    }

    // ==================== Register (Daftar) ====================

    suspend fun register(
        username: String,
        password: String,
        displayName: String = "",
        gender: Gender = Gender.FEMALE
    ): AuthResult = withContext(Dispatchers.IO) {
        val trimmed = username.trim()
        if (trimmed.isEmpty()) {
            return@withContext AuthResult(false, "Username tidak boleh kosong")
        }
        if (password.length < 4) {
            return@withContext AuthResult(false, "Kata sandi minimal 4 karakter")
        }

        val normalizedKey = trimmed.lowercase()
        val finalDisplayName = if (displayName.isNotBlank()) displayName.trim() else trimmed
        val lovyId = "lovy_${(100000..999999).random()}"
        val hashedPassword = hashPassword(password)
        val defaultBio = "Halo, saya pengguna baru Lovy Chat! ✨"

        // 1. Cek & Simpan di Supabase jika terkonfigurasi
        if (SupabaseClient.isConfigured() && supabaseRepo != null) {
            try {
                val existingAccount = supabaseRepo.findAccountByUsername(normalizedKey)
                if (existingAccount != null) {
                    return@withContext AuthResult(
                        success = false,
                        message = "Username \"$trimmed\" sudah terdaftar. Silakan gunakan username lain atau pilih Masuk."
                    )
                }

                val accountDto = SupabaseAccountDto(
                    id = lovyId,
                    username = normalizedKey,
                    passwordHash = hashedPassword,
                    displayName = finalDisplayName,
                    gender = gender.name,
                    bio = defaultBio,
                    avatarUrl = null,
                    googleId = null,
                    googleEmail = null,
                    createdAt = System.currentTimeMillis(),
                    lastLoginAt = System.currentTimeMillis()
                )

                // Simpan ke Supabase app_accounts
                supabaseRepo.registerOrUpdateAccount(accountDto)

                // Sinkronkan juga ke nearby_users agar akun langsung tampil di radar
                supabaseRepo.registerOrUpdateUser(
                    id = lovyId,
                    name = finalDisplayName,
                    gender = gender,
                    bio = defaultBio
                )
            } catch (e: Exception) {
                Log.w(TAG, "Peringatan saat mendaftar ke Supabase, fallback lokal tetap berjalan: ${e.message}")
            }
        }

        // 2. Simpan cadangan di storage lokal agar tetap bisa login secara offline
        val map = getUsersMap()
        map[normalizedKey] = hashedPassword
        saveUsersMap(map)

        // 3. Simpan sesi aktif
        val session = SavedSession(
            isLoggedIn = true,
            isGuest = false,
            lovyId = lovyId,
            username = normalizedKey,
            displayName = finalDisplayName,
            gender = gender,
            bio = defaultBio,
            isGoogleUser = false
        )
        saveSession(session)

        return@withContext AuthResult(
            success = true,
            message = "Registrasi berhasil! Selamat datang di Lovy Chat.",
            username = normalizedKey,
            displayName = finalDisplayName,
            lovyId = lovyId,
            gender = gender,
            bio = defaultBio,
            isGoogleUser = false
        )
    }

    // ==================== Login (Masuk) ====================

    suspend fun login(username: String, password: String): AuthResult = withContext(Dispatchers.IO) {
        val trimmed = username.trim()
        if (trimmed.isEmpty()) {
            return@withContext AuthResult(false, "Username tidak boleh kosong")
        }
        if (password.isEmpty()) {
            return@withContext AuthResult(false, "Silakan masukkan kata sandi Anda")
        }

        val normalizedKey = trimmed.lowercase()

        // 1. Coba pencocokan melalui Supabase jika terkonfigurasi
        if (SupabaseClient.isConfigured() && supabaseRepo != null) {
            try {
                val cloudAccount = supabaseRepo.findAccountByUsername(normalizedKey)
                if (cloudAccount != null) {
                    if (isPasswordMatching(cloudAccount.passwordHash, password)) {
                        // Password cocok! Perbarui waktu login terakhir di Supabase
                        supabaseRepo.updateAccountLoginTime(cloudAccount.id)
                        supabaseRepo.updateUserLastActive(cloudAccount.id)

                        val userGender = if (cloudAccount.gender?.equals("MALE", ignoreCase = true) == true) Gender.MALE else Gender.FEMALE
                        val dispName = cloudAccount.displayName ?: cloudAccount.username
                        val bioText = cloudAccount.bio ?: ""

                        // Simpan cadangan lokal
                        val map = getUsersMap()
                        cloudAccount.passwordHash?.let { map[normalizedKey] = it }
                        saveUsersMap(map)

                        val session = SavedSession(
                            isLoggedIn = true,
                            isGuest = false,
                            lovyId = cloudAccount.id,
                            username = cloudAccount.username,
                            displayName = dispName,
                            gender = userGender,
                            bio = bioText,
                            avatarUrl = cloudAccount.avatarUrl,
                            isGoogleUser = false
                        )
                        saveSession(session)

                        return@withContext AuthResult(
                            success = true,
                            message = "Login berhasil! Selamat datang kembali.",
                            username = cloudAccount.username,
                            displayName = dispName,
                            lovyId = cloudAccount.id,
                            gender = userGender,
                            bio = bioText,
                            avatarUrl = cloudAccount.avatarUrl,
                            isGoogleUser = false
                        )
                    } else {
                        // Username ada tapi kata sandi salah
                        return@withContext AuthResult(
                            success = false,
                            message = "Kata sandi yang Anda masukkan salah. Silakan coba lagi."
                        )
                    }
                }
            } catch (e: Exception) {
                Log.w(TAG, "Gagal mencocokkan akun ke Supabase, mencoba pencocokan lokal: ${e.message}")
            }
        }

        // 2. Pencocokan melalui storage lokal (Offline fallback)
        val localMap = getUsersMap()
        if (!localMap.containsKey(normalizedKey)) {
            return@withContext AuthResult(
                success = false,
                message = "Username \"$trimmed\" belum terdaftar. Silakan pilih menu Daftar Akun terlebih dahulu."
            )
        }

        val storedPassword = localMap[normalizedKey]
        if (!isPasswordMatching(storedPassword, password)) {
            return@withContext AuthResult(
                success = false,
                message = "Kata sandi yang Anda masukkan salah. Silakan periksa kembali."
            )
        }

        val lovyId = "lovy_${(100000..999999).random()}"
        val session = SavedSession(
            isLoggedIn = true,
            isGuest = false,
            lovyId = lovyId,
            username = normalizedKey,
            displayName = trimmed,
            gender = Gender.FEMALE,
            bio = "Menjelajahi dunia dengan Lovy Chat ✨",
            isGoogleUser = false
        )
        saveSession(session)

        return@withContext AuthResult(
            success = true,
            message = "Login berhasil!",
            username = normalizedKey,
            displayName = trimmed,
            lovyId = lovyId,
            gender = Gender.FEMALE,
            bio = session.bio,
            isGoogleUser = false
        )
    }

    // ==================== Google Sign-In (Melekat Permanen) ====================

    suspend fun loginWithGoogle(googleUser: GoogleAuthHelper.GoogleUserResult): AuthResult = withContext(Dispatchers.IO) {
        val googleEmail = googleUser.email.trim().lowercase()
        val displayName = googleUser.displayName.ifBlank { googleEmail.substringBefore("@") }
        val avatarUrl = googleUser.profilePictureUri

        // 1. Periksa akun melekat di Supabase jika terkonfigurasi
        if (SupabaseClient.isConfigured() && supabaseRepo != null) {
            try {
                val existingCloudAccount = supabaseRepo.findAccountByGoogle(googleEmail)
                if (existingCloudAccount != null) {
                    // Akun Google sudah melekat di Supabase! Muat data permanennya
                    supabaseRepo.updateAccountLoginTime(existingCloudAccount.id)
                    supabaseRepo.updateUserLastActive(existingCloudAccount.id)

                    val userGender = if (existingCloudAccount.gender?.equals("MALE", ignoreCase = true) == true) Gender.MALE else Gender.FEMALE
                    val dispName = existingCloudAccount.displayName ?: existingCloudAccount.username
                    val bioText = existingCloudAccount.bio ?: ""

                    val session = SavedSession(
                        isLoggedIn = true,
                        isGuest = false,
                        lovyId = existingCloudAccount.id,
                        username = existingCloudAccount.username,
                        displayName = dispName,
                        gender = userGender,
                        bio = bioText,
                        avatarUrl = existingCloudAccount.avatarUrl ?: avatarUrl,
                        isGoogleUser = true
                    )
                    saveSession(session)

                    return@withContext AuthResult(
                        success = true,
                        message = "Selamat datang kembali, $dispName!",
                        username = existingCloudAccount.username,
                        displayName = dispName,
                        lovyId = existingCloudAccount.id,
                        gender = userGender,
                        bio = bioText,
                        avatarUrl = session.avatarUrl,
                        isGoogleUser = true
                    )
                } else {
                    // Pengguna baru pertama kali login Google! Buat dan lekatkan akun secara permanen di Supabase
                    val newLovyId = "lovy_${(100000..999999).random()}"
                    val baseUsername = googleEmail.substringBefore("@").replace(".", "_")

                    val newAccount = SupabaseAccountDto(
                        id = newLovyId,
                        username = baseUsername,
                        passwordHash = null,
                        displayName = displayName,
                        gender = "FEMALE",
                        bio = "Pengguna terverifikasi Google di Lovy Chat ✨",
                        avatarUrl = avatarUrl,
                        googleId = googleEmail,
                        googleEmail = googleEmail,
                        createdAt = System.currentTimeMillis(),
                        lastLoginAt = System.currentTimeMillis()
                    )

                    supabaseRepo.registerOrUpdateAccount(newAccount)
                    supabaseRepo.registerOrUpdateUser(
                        id = newLovyId,
                        name = displayName,
                        gender = Gender.FEMALE,
                        bio = newAccount.bio ?: "",
                        avatarUrl = avatarUrl
                    )

                    val session = SavedSession(
                        isLoggedIn = true,
                        isGuest = false,
                        lovyId = newLovyId,
                        username = baseUsername,
                        displayName = displayName,
                        gender = Gender.FEMALE,
                        bio = newAccount.bio ?: "",
                        avatarUrl = avatarUrl,
                        isGoogleUser = true
                    )
                    saveSession(session)

                    return@withContext AuthResult(
                        success = true,
                        message = "Selamat datang, $displayName!",
                        username = baseUsername,
                        displayName = displayName,
                        lovyId = newLovyId,
                        gender = Gender.FEMALE,
                        bio = newAccount.bio,
                        avatarUrl = avatarUrl,
                        isGoogleUser = true
                    )
                }
            } catch (e: Exception) {
                Log.w(TAG, "Gagal menyambungkan Google Sign-In ke Supabase: ${e.message}")
            }
        }

        // 2. Fallback Lokal jika Supabase belum terhubung
        val localLovyId = "lovy_${(100000..999999).random()}"
        val baseUsername = googleEmail.substringBefore("@").replace(".", "_")
        val session = SavedSession(
            isLoggedIn = true,
            isGuest = false,
            lovyId = localLovyId,
            username = baseUsername,
            displayName = displayName,
            gender = Gender.FEMALE,
            bio = "Pengguna Google di Lovy Chat ✨",
            avatarUrl = avatarUrl,
            isGoogleUser = true
        )
        saveSession(session)

        return@withContext AuthResult(
            success = true,
            message = "Selamat datang, $displayName!",
            username = baseUsername,
            displayName = displayName,
            lovyId = localLovyId,
            gender = Gender.FEMALE,
            bio = session.bio,
            avatarUrl = avatarUrl,
            isGoogleUser = true
        )
    }

    fun isUsernameTaken(username: String): Boolean {
        val normalizedKey = username.trim().lowercase()
        return getUsersMap().containsKey(normalizedKey)
    }
}

