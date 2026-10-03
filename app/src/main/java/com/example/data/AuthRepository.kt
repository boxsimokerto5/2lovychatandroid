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
    val email: String? = null,
    val lovyId: String? = null,
    val gender: Gender = Gender.FEMALE,
    val bio: String? = null,
    val avatarUrl: String? = null,
    val isGoogleUser: Boolean = false,
    val city: String? = null,
    val age: Int? = null
)

data class SavedSession(
    val isLoggedIn: Boolean,
    val isGuest: Boolean,
    val lovyId: String,
    val username: String,
    val displayName: String,
    val email: String? = null,
    val gender: Gender = Gender.FEMALE,
    val bio: String = "",
    val avatarUrl: String? = null,
    val isGoogleUser: Boolean = false,
    val city: String? = null,
    val age: Int? = null
)

class AuthRepository(
    private val context: Context,
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

    /**
     * Mengambil ID Lovy permanen untuk pengguna atau email.
     * Jika akun sudah pernah dibuat atau memiliki ID sebelumnya, ID tersebut dipakai kembali.
     * Jika akun baru, dibuatkan ID deterministik permanen dari email/username (Bukan random acak).
     * Dengan demikian, ID tidak akan pernah berganti-ganti saat keluar masuk aplikasi.
     */
    fun getOrGenerateLovyId(identifier: String, preferredId: String? = null): String {
        val cleanKey = identifier.trim().lowercase()
        if (cleanKey.isEmpty()) return "lovy_100001"

        val lovyIdKey = "user_lovy_id_$cleanKey"

        // 1. Periksa apakah sudah ada ID tersimpan permanen di SharedPreferences
        val stored = prefs.getString(lovyIdKey, null)?.takeIf { it.isNotBlank() && it != "lovy_889214" }
        if (stored != null) return stored

        // 2. Jika ada preferredId yang valid dari sesi atau server
        if (!preferredId.isNullOrBlank() && preferredId != "lovy_889214" && preferredId.startsWith("lovy_")) {
            prefs.edit().putString(lovyIdKey, preferredId).apply()
            return preferredId
        }

        // 3. Buat ID permanen deterministik berdasarkan email/identifier (konsisten & stabil seumur hidup)
        val permanentId = com.example.data.pocketbase.PocketBaseClient.toLovyId(cleanKey)
        prefs.edit().putString(lovyIdKey, permanentId).apply()
        return permanentId
    }

    fun getSavedSession(): SavedSession? {
        val jsonStr = prefs.getString(KEY_SAVED_SESSION, null) ?: return null
        return try {
            val json = JSONObject(jsonStr)
            val isLoggedIn = json.optBoolean("is_logged_in", false)
            if (!isLoggedIn) return null

            val rawUsername = json.optString("username", "")
            val rawDisplayName = json.optString("display_name", "")
            val fallbackName = if (rawUsername.contains("@")) {
                rawUsername.substringBefore("@").replaceFirstChar { it.uppercase() }
            } else {
                rawUsername
            }
            val finalDisplayName = when {
                rawDisplayName.isNotBlank() && !rawDisplayName.equals("Pengguna Lovy", ignoreCase = true) -> rawDisplayName
                fallbackName.isNotBlank() -> fallbackName
                else -> "Pengguna"
            }

            val storedEmail = json.optString("email").takeIf { it.isNotBlank() }
                ?: if (rawUsername.contains("@")) rawUsername else null

            val storedLovyId = json.optString("lovy_id", "")
            val finalLovyId = getOrGenerateLovyId(storedEmail ?: rawUsername, storedLovyId)

            val storedBio = json.optString("bio", "")
            val cleanBio = if (storedBio == "Menjelajahi dunia dan mencari teman baru di Lovy Chat ✨") "" else storedBio

            SavedSession(
                isLoggedIn = true,
                isGuest = json.optBoolean("is_guest", false),
                lovyId = finalLovyId,
                username = rawUsername,
                displayName = finalDisplayName,
                email = storedEmail,
                gender = if (json.optString("gender", "FEMALE").equals("MALE", ignoreCase = true)) Gender.MALE else Gender.FEMALE,
                bio = cleanBio,
                avatarUrl = json.optString("avatar_url").takeIf { it.isNotBlank() },
                isGoogleUser = json.optBoolean("is_google_user", false),
                city = json.optString("city").takeIf { it.isNotBlank() },
                age = if (json.has("age")) json.optInt("age") else null
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
                put("email", session.email ?: "")
                put("gender", session.gender.name)
                put("bio", session.bio)
                put("avatar_url", session.avatarUrl ?: "")
                put("is_google_user", session.isGoogleUser)
                put("city", session.city ?: "")
                if (session.age != null) {
                    put("age", session.age)
                }
            }
            prefs.edit().putString(KEY_SAVED_SESSION, json.toString()).apply()
        } catch (e: Exception) {
            Log.w(TAG, "Gagal menyimpan session lokal", e)
        }
    }

    fun clearSession() {
        prefs.edit().remove(KEY_SAVED_SESSION).apply()
        com.example.data.pocketbase.PocketBaseClient.clearAuthToken(context)
    }

    fun deleteAccount(username: String) {
        clearSession()
        try {
            val normalizedUser = username.lowercase().trim()
            prefs.edit().remove("user_lovy_id_$normalizedUser").apply()
            val rawUsers = prefs.getString(KEY_REGISTERED_USERS, null)
            if (!rawUsers.isNullOrBlank()) {
                val json = JSONObject(rawUsers)
                json.remove(normalizedUser)
                prefs.edit().putString(KEY_REGISTERED_USERS, json.toString()).apply()
            }
            val rawGoogle = prefs.getString(KEY_GOOGLE_USERS, null)
            if (!rawGoogle.isNullOrBlank()) {
                val json = JSONObject(rawGoogle)
                val keysToRemove = mutableListOf<String>()
                val iter = json.keys()
                while (iter.hasNext()) {
                    val key = iter.next()
                    val userObj = json.optJSONObject(key)
                    if (userObj?.optString("username")?.equals(username, ignoreCase = true) == true ||
                        userObj?.optString("lovy_id")?.equals(username, ignoreCase = true) == true ||
                        userObj?.optString("email")?.equals(username, ignoreCase = true) == true ||
                        key.equals(username, ignoreCase = true)
                    ) {
                        keysToRemove.add(key)
                        prefs.edit().remove("user_lovy_id_${key.lowercase()}").apply()
                    }
                }
                keysToRemove.forEach { json.remove(it) }
                prefs.edit().putString(KEY_GOOGLE_USERS, json.toString()).apply()
            }
        } catch (e: Exception) {
            Log.w(TAG, "Gagal menghapus akun lokal: ${e.message}")
        }
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
        if (password.length < 6) {
            return@withContext AuthResult(false, "Kata sandi minimal 6 karakter sesuai standar keamanan")
        }

        val normalizedKey = trimmed.lowercase()
        val finalDisplayName = if (displayName.isNotBlank()) displayName.trim() else trimmed
        val lovyId = getOrGenerateLovyId(normalizedKey)
        val hashedPassword = hashPassword(password)
        val defaultBio = "Halo, saya pengguna baru Lovy Chat! ✨"

        // 1. Cek & Simpan di PocketBase / Backend Cloud
        if (supabaseRepo != null) {
            try {
                if (SupabaseClient.isPocketBase()) {
                    val pbResult = supabaseRepo.registerWithPassword(
                        username = normalizedKey,
                        password = password,
                        displayName = finalDisplayName,
                        gender = gender
                    )
                    if (!pbResult.success) {
                        return@withContext pbResult
                    }
                    val effectiveLovyId = pbResult.lovyId ?: lovyId
                    val userEmail = pbResult.email ?: (if (normalizedKey.contains("@")) normalizedKey else null)

                    // Simpan cadangan di storage lokal agar tetap bisa login secara offline
                    val map = getUsersMap()
                    map[normalizedKey] = hashedPassword
                    saveUsersMap(map)
                    prefs.edit().putString("user_lovy_id_${normalizedKey}", effectiveLovyId).apply()

                    val session = SavedSession(
                        isLoggedIn = true,
                        isGuest = false,
                        lovyId = effectiveLovyId,
                        username = pbResult.username ?: normalizedKey,
                        displayName = pbResult.displayName ?: finalDisplayName,
                        email = userEmail,
                        gender = pbResult.gender,
                        bio = pbResult.bio ?: defaultBio,
                        avatarUrl = pbResult.avatarUrl,
                        isGoogleUser = false
                    )
                    saveSession(session)
                    return@withContext pbResult
                } else if (SupabaseClient.isConfigured()) {
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

                    supabaseRepo.registerOrUpdateAccount(accountDto)
                    supabaseRepo.registerOrUpdateUser(
                        id = lovyId,
                        name = finalDisplayName,
                        gender = gender,
                        bio = defaultBio
                    )
                }
            } catch (e: Exception) {
                Log.w(TAG, "Peringatan saat mendaftar ke server, fallback lokal tetap berjalan: ${e.message}")
            }
        }

        // 2. Simpan cadangan di storage lokal agar tetap bisa login secara offline
        val map = getUsersMap()
        map[normalizedKey] = hashedPassword
        saveUsersMap(map)
        prefs.edit().putString("user_lovy_id_${normalizedKey}", lovyId).apply()

        // 3. Simpan sesi aktif
        val userEmail = if (normalizedKey.contains("@")) normalizedKey else null
        val session = SavedSession(
            isLoggedIn = true,
            isGuest = false,
            lovyId = lovyId,
            username = normalizedKey,
            displayName = finalDisplayName,
            email = userEmail,
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
            email = userEmail,
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

        // 1. Coba pencocokan melalui PocketBase jika aktif
        if (supabaseRepo != null) {
            try {
                if (SupabaseClient.isPocketBase()) {
                    val pbResult = supabaseRepo.loginWithPassword(trimmed, password)
                    if (pbResult.success) {
                        val effectiveLovyId = pbResult.lovyId ?: getOrGenerateLovyId(normalizedKey)
                        val userEmail = pbResult.email ?: (if (normalizedKey.contains("@")) normalizedKey else null)
                        val hashedPassword = hashPassword(password)

                        // Simpan cadangan lokal
                        val map = getUsersMap()
                        map[normalizedKey] = hashedPassword
                        saveUsersMap(map)
                        prefs.edit().putString("user_lovy_id_${normalizedKey}", effectiveLovyId).apply()

                        val session = SavedSession(
                            isLoggedIn = true,
                            isGuest = false,
                            lovyId = effectiveLovyId,
                            username = pbResult.username ?: normalizedKey,
                            displayName = pbResult.displayName ?: trimmed,
                            email = userEmail,
                            gender = pbResult.gender,
                            bio = pbResult.bio ?: "",
                            avatarUrl = pbResult.avatarUrl,
                            city = pbResult.city,
                            isGoogleUser = false
                        )
                        saveSession(session)
                        return@withContext pbResult
                    } else {
                        // Jika server merespon dengan kegagalan kredensial, jangan fallback ke lokal kecuali masalah koneksi
                        val msg = pbResult.message.lowercase()
                        val isNetworkIssue = msg.contains("terputus") || msg.contains("koneksi") || msg.contains("timeout") || msg.contains("unable to resolve")
                        if (!isNetworkIssue) {
                            return@withContext pbResult
                        }
                    }
                } else if (SupabaseClient.isConfigured()) {
                    val cloudAccount = supabaseRepo.findAccountByUsername(normalizedKey)
                    if (cloudAccount != null) {
                        if (isPasswordMatching(cloudAccount.passwordHash, password)) {
                            supabaseRepo.updateAccountLoginTime(cloudAccount.id)
                            supabaseRepo.updateUserLastActive(cloudAccount.id)

                            val userGender = if (cloudAccount.gender?.equals("MALE", ignoreCase = true) == true) Gender.MALE else Gender.FEMALE
                            val dispName = cloudAccount.displayName ?: cloudAccount.username
                            val bioText = cloudAccount.bio ?: ""
                            val cloudEmail = cloudAccount.googleEmail ?: if (normalizedKey.contains("@")) normalizedKey else null

                            val map = getUsersMap()
                            cloudAccount.passwordHash?.let { map[normalizedKey] = it }
                            saveUsersMap(map)

                            val session = SavedSession(
                                isLoggedIn = true,
                                isGuest = false,
                                lovyId = cloudAccount.id,
                                username = cloudAccount.username,
                                displayName = dispName,
                                email = cloudEmail,
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
                                email = cloudEmail,
                                lovyId = cloudAccount.id,
                                gender = userGender,
                                bio = bioText,
                                avatarUrl = cloudAccount.avatarUrl,
                                isGoogleUser = false
                            )
                        } else {
                            return@withContext AuthResult(
                                success = false,
                                message = "Kata sandi yang Anda masukkan salah. Silakan coba lagi."
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                Log.w(TAG, "Gagal mencocokkan akun ke server, mencoba pencocokan lokal: ${e.message}")
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

        val lovyIdKey = "user_lovy_id_${normalizedKey}"
        val existingLovyId = prefs.getString(lovyIdKey, null)
        val lovyId = getOrGenerateLovyId(normalizedKey, existingLovyId)
        val userEmail = if (normalizedKey.contains("@")) normalizedKey else null
        val session = SavedSession(
            isLoggedIn = true,
            isGuest = false,
            lovyId = lovyId,
            username = normalizedKey,
            displayName = trimmed,
            email = userEmail,
            gender = Gender.FEMALE,
            bio = "",
            isGoogleUser = false
        )
        saveSession(session)

        return@withContext AuthResult(
            success = true,
            message = "Login berhasil!",
            username = normalizedKey,
            displayName = trimmed,
            email = userEmail,
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
                    // Akun Google sudah melekat di server! Muat data permanennya
                    supabaseRepo.updateAccountLoginTime(existingCloudAccount.id)
                    supabaseRepo.updateUserLastActive(existingCloudAccount.id)

                    val userGender = if (existingCloudAccount.gender?.equals("MALE", ignoreCase = true) == true) Gender.MALE else Gender.FEMALE
                    val dispName = existingCloudAccount.displayName ?: existingCloudAccount.username
                    val bioText = existingCloudAccount.bio ?: ""
                    val permanentLovyId = getOrGenerateLovyId(googleEmail, existingCloudAccount.id)

                    val session = SavedSession(
                        isLoggedIn = true,
                        isGuest = false,
                        lovyId = permanentLovyId,
                        username = existingCloudAccount.username,
                        displayName = dispName,
                        email = googleEmail,
                        gender = userGender,
                        bio = bioText,
                        avatarUrl = existingCloudAccount.avatarUrl ?: avatarUrl,
                        isGoogleUser = true
                    )
                    saveSession(session)
                    com.example.data.pocketbase.PocketBaseClient.saveAuthToken(context, com.example.data.pocketbase.PocketBaseClient.authToken)

                    return@withContext AuthResult(
                        success = true,
                        message = "Selamat datang kembali, $dispName!",
                        username = existingCloudAccount.username,
                        displayName = dispName,
                        email = googleEmail,
                        lovyId = permanentLovyId,
                        gender = userGender,
                        bio = bioText,
                        avatarUrl = session.avatarUrl,
                        isGoogleUser = true
                    )
                } else {
                    // Pengguna pertama kali login Google: Cek apakah ada akun lokal atau akun username sebelumnya agar ID lama tetap dipakai (Account Linker)
                    val saved = getSavedSession()
                    val baseUsername = googleEmail.substringBefore("@").replace(".", "_")
                    val existingByUsername = supabaseRepo.findAccountByUsername(baseUsername)
                        ?: if (displayName.isNotBlank() && !displayName.equals("Pengguna Lovy", ignoreCase = true)) {
                            supabaseRepo.findAccountByUsername(displayName)
                        } else null

                    val candidateId = existingByUsername?.id
                        ?: saved?.lovyId?.takeIf { it.isNotBlank() && it.startsWith("lovy_") }

                    val newLovyId = getOrGenerateLovyId(googleEmail, candidateId)

                    val newAccount = SupabaseAccountDto(
                        id = newLovyId,
                        username = newLovyId,
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
                        email = googleEmail,
                        gender = Gender.FEMALE,
                        bio = newAccount.bio ?: "",
                        avatarUrl = avatarUrl,
                        isGoogleUser = true
                    )
                    saveSession(session)
                    com.example.data.pocketbase.PocketBaseClient.saveAuthToken(context, com.example.data.pocketbase.PocketBaseClient.authToken)

                    return@withContext AuthResult(
                        success = true,
                        message = "Selamat datang, $displayName!",
                        username = baseUsername,
                        displayName = displayName,
                        email = googleEmail,
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

        // 2. Fallback Lokal jika server belum terhubung
        val localLovyId = getOrGenerateLovyId(googleEmail)
        val baseUsername = googleEmail.substringBefore("@").replace(".", "_")
        val session = SavedSession(
            isLoggedIn = true,
            isGuest = false,
            lovyId = localLovyId,
            username = baseUsername,
            displayName = displayName,
            email = googleEmail,
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
            email = googleEmail,
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

