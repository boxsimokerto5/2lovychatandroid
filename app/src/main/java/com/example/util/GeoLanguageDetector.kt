package com.example.util

import android.content.Context
import android.telephony.TelephonyManager
import java.util.Locale
import java.util.TimeZone

enum class AppLanguage(val code: String, val displayName: String) {
    INDONESIAN("in", "Bahasa Indonesia"),
    ENGLISH("en", "English")
}

object GeoLanguageDetector {

    /**
     * Mendeteksi otomatis bahasa berdasarkan area geografis pengguna.
     * Jika terdeteksi di Indonesia (ID) atau Malaysia (MY), otomatis Bahasa Indonesia.
     * Selain itu, otomatis Bahasa Inggris (EN).
     */
    fun detectLanguage(context: Context?): AppLanguage {
        if (context == null) return AppLanguage.INDONESIAN
        val detectedCountry = getCountryCode(context).uppercase(Locale.ROOT)
        
        // Cek kode negara: ID (Indonesia), MY (Malaysia)
        if (detectedCountry == "ID" || detectedCountry == "MY") {
            return AppLanguage.INDONESIAN
        }

        // Cek TimeZone sebagai deteksi geografis pendukung yang sangat akurat
        val tzId = TimeZone.getDefault().id.lowercase(Locale.ROOT)
        if (isIndonesianOrMalaysianTimeZone(tzId)) {
            return AppLanguage.INDONESIAN
        }

        // Cek locale bawaan perangkat
        val defaultLocale = Locale.getDefault().country.uppercase(Locale.ROOT)
        val defaultLang = Locale.getDefault().language.lowercase(Locale.ROOT)
        if (defaultLocale == "ID" || defaultLocale == "MY" || defaultLang == "in" || defaultLang == "id" || defaultLang == "ms") {
            return AppLanguage.INDONESIAN
        }

        // Selain Indonesia dan Malaysia -> Bahasa Inggris
        return AppLanguage.ENGLISH
    }

    private fun getCountryCode(context: Context?): String {
        if (context == null) {
            return Locale.getDefault().country.orEmpty().trim()
        }
        try {
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
            if (tm != null) {
                // 1. Cek SIM Country ISO (dari kartu seluler)
                val simCountry = tm.simCountryIso
                if (!simCountry.isNullOrBlank()) {
                    return simCountry.trim()
                }

                // 2. Cek Network Country ISO (dari operator jaringan aktif)
                val networkCountry = tm.networkCountryIso
                if (!networkCountry.isNullOrBlank()) {
                    return networkCountry.trim()
                }
            }
        } catch (_: Throwable) {
            // Abaikan jika izin tidak ada atau di lingkungan tablet/emulator
        }

        // 3. Fallback ke Locale pengguna
        val localeCountry = Locale.getDefault().country
        if (!localeCountry.isNullOrBlank()) {
            return localeCountry.trim()
        }

        return ""
    }

    private fun isIndonesianOrMalaysianTimeZone(tzId: String): Boolean {
        return tzId.contains("jakarta") ||
                tzId.contains("pontianak") ||
                tzId.contains("makassar") ||
                tzId.contains("jayapura") ||
                tzId.contains("kuala_lumpur") ||
                tzId.contains("kuching") ||
                tzId.startsWith("asia/jakarta") ||
                tzId.startsWith("asia/kuala_lumpur")
    }
}
