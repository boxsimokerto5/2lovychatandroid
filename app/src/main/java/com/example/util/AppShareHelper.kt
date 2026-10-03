package com.example.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

object AppShareHelper {
    const val PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=com.lovychat.gecckocreator"
    const val PACKAGE_NAME = "com.lovychat.gecckocreator"

    /**
     * Membuka halaman Play Store untuk memberikan rating & ulasan bintang 5.
     * Menggunakan protokol market:// terlebih dahulu agar langsung membuka aplikasi Google Play Store di HP pengguna.
     * Jika Google Play Store tidak tersedia, akan fallback membuka browser ke URL web Play Store.
     */
    fun openPlayStoreRating(context: Context) {
        val marketUri = Uri.parse("market://details?id=$PACKAGE_NAME")
        val marketIntent = Intent(Intent.ACTION_VIEW, marketUri).apply {
            addFlags(
                Intent.FLAG_ACTIVITY_NO_HISTORY or
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK
            )
        }
        try {
            context.startActivity(marketIntent)
        } catch (e: ActivityNotFoundException) {
            val webUri = Uri.parse(PLAY_STORE_URL)
            context.startActivity(Intent(Intent.ACTION_VIEW, webUri))
        } catch (e: Exception) {
            Toast.makeText(context, "Tidak dapat membuka Play Store: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Membuka sheet Bagikan bawaan Android (Android Share Sheet)
     * Membagikan link resmi Play Store aplikasi Lovy Chat ke WhatsApp, Telegram, Instagram, SMS, dll.
     */
    fun shareApp(context: Context, language: AppLanguage = AppLanguage.INDONESIAN) {
        try {
            val shareMessage = AppStrings.shareAppMessage(language)
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, AppStrings.shareAppSubject(language))
                putExtra(Intent.EXTRA_TEXT, shareMessage)
            }
            val chooser = Intent.createChooser(shareIntent, AppStrings.menuShareApp(language))
            context.startActivity(chooser)
        } catch (e: Exception) {
            Toast.makeText(context, "Gagal membuka menu bagikan: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }
}
