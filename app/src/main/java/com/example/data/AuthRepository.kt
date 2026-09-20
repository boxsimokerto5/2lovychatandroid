package com.example.data

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONObject

data class AuthResult(
    val success: Boolean,
    val message: String,
    val username: String? = null
)

class AuthRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("lovy_auth_store", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_REGISTERED_USERS = "registered_users_map"
    }

    /**
     * Mengambil seluruh map username -> password (case-insensitive username)
     */
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

    /**
     * Mendaftarkan pengguna baru.
     * Jika username sudah terdaftar (case-insensitive), pendaftaran ditolak.
     */
    fun register(username: String, password: String): AuthResult {
        val trimmed = username.trim()
        if (trimmed.isEmpty()) {
            return AuthResult(false, "Username tidak boleh kosong")
        }
        if (password.length < 4) {
            return AuthResult(false, "Kata sandi minimal 4 karakter")
        }

        val map = getUsersMap()
        val normalizedKey = trimmed.lowercase()

        if (map.containsKey(normalizedKey)) {
            return AuthResult(
                success = false,
                message = "Username \"$trimmed\" sudah terdaftar. Silakan pilih username lain atau masuk di menu Login."
            )
        }

        map[normalizedKey] = password
        saveUsersMap(map)

        return AuthResult(
            success = true,
            message = "Registrasi berhasil! Selamat datang di Lovy Chat.",
            username = trimmed
        )
    }

    /**
     * Masuk dengan akun terdaftar.
     * Memeriksa keberadaan username dan mencocokkan kata sandi.
     */
    fun login(username: String, password: String): AuthResult {
        val trimmed = username.trim()
        if (trimmed.isEmpty()) {
            return AuthResult(false, "Username tidak boleh kosong")
        }

        val map = getUsersMap()
        val normalizedKey = trimmed.lowercase()

        if (!map.containsKey(normalizedKey)) {
            return AuthResult(
                success = false,
                message = "Username \"$trimmed\" belum terdaftar. Silakan lakukan registrasi akun terlebih dahulu."
            )
        }

        val storedPassword = map[normalizedKey]
        if (storedPassword != password) {
            return AuthResult(
                success = false,
                message = "Kata sandi yang Anda masukkan salah. Silakan coba lagi."
            )
        }

        return AuthResult(
            success = true,
            message = "Login berhasil!",
            username = trimmed
        )
    }

    /**
     * Cek apakah username sudah dipakai
     */
    fun isUsernameTaken(username: String): Boolean {
        val normalizedKey = username.trim().lowercase()
        return getUsersMap().containsKey(normalizedKey)
    }
}
