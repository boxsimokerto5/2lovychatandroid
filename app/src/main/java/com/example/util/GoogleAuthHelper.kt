package com.example.util

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import java.security.MessageDigest
import java.util.UUID

object GoogleAuthHelper {
    // Web Application Client ID dari Google Cloud Console proyek pengguna (wajib tipe Web untuk serverClientId)
    val SERVER_CLIENT_ID: String
        get() {
            val build = try {
                val field = com.example.BuildConfig::class.java.getField("GOOGLE_SERVER_CLIENT_ID")
                field.get(null) as? String ?: ""
            } catch (_: Throwable) { "" }
            return if (build.isNotBlank() && !build.startsWith("your_")) build
            else "347302027962-9g1rvg326b9hvtgamckkqcn7mr00i2gp.apps.googleusercontent.com"
        }


    data class GoogleUserResult(
        val idToken: String,
        val displayName: String,
        val email: String,
        val profilePictureUri: String? = null
    )

    suspend fun signInWithGoogle(context: Context): Result<GoogleUserResult> {
        return try {
            val credentialManager = CredentialManager.create(context)

            // Buat raw nonce unik untuk keamanan token
            val rawNonce = UUID.randomUUID().toString()
            val md = MessageDigest.getInstance("SHA-256")
            val digest = md.digest(rawNonce.toByteArray())
            val hashedNonce = digest.fold("") { str, it -> str + "%02x".format(it) }

            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(SERVER_CLIENT_ID)
                .setAutoSelectEnabled(false)
                .setNonce(hashedNonce)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val response: GetCredentialResponse = credentialManager.getCredential(
                request = request,
                context = context
            )

            val credential = response.credential
            if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                Result.success(
                    GoogleUserResult(
                        idToken = googleIdTokenCredential.idToken,
                        displayName = googleIdTokenCredential.displayName ?: googleIdTokenCredential.id.substringBefore("@"),
                        email = googleIdTokenCredential.id,
                        profilePictureUri = googleIdTokenCredential.profilePictureUri?.toString()
                    )
                )
            } else {
                Result.failure(Exception("Format kredensial tidak dikenali"))
            }
        } catch (e: GetCredentialCancellationException) {
            Log.d("GoogleAuthHelper", "User cancelled Google Sign-In: ${e.message}")
            Result.failure(Exception("Login Google dibatalkan oleh pengguna"))
        } catch (e: GetCredentialException) {
            Log.e("GoogleAuthHelper", "Google Sign-In failed: ${e.type} -> ${e.message}", e)
            Result.failure(Exception(e.localizedMessage ?: "Gagal terhubung dengan akun Google"))
        } catch (e: Exception) {
            Log.e("GoogleAuthHelper", "Unexpected error: ${e.message}", e)
            Result.failure(e)
        }
    }
}
