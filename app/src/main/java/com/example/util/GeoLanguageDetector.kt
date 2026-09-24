package com.example.util

import android.content.Context
import android.telephony.TelephonyManager
import java.util.Locale
import java.util.TimeZone

enum class AppLanguage(
    val code: String,
    val displayName: String,
    val nativeName: String,
    val isLocalAuto: Boolean = false
) {
    LOCAL("lo", "Local (Auto)", "Lokal", true),
    INDONESIAN("id", "Bahasa Indonesia", "Indonesia"),
    ENGLISH("en", "English", "English"),
    CHINESE("zh", "Chinese", "中文"),
    JAPANESE("ja", "Japanese", "日本語"),
    KOREAN("ko", "Korean", "한국어"),
    ARABIC("ar", "Arabic", "العربية"),
    SPANISH("es", "Spanish", "Español"),
    FRENCH("fr", "French", "Français"),
    GERMAN("de", "German", "Deutsch"),
    RUSSIAN("ru", "Russian", "Русский"),
    PORTUGUESE("pt", "Portuguese", "Português");

    companion object {
        fun fromCode(code: String): AppLanguage {
            return entries.find { 
                it.code.equals(code, ignoreCase = true) || 
                (code.equals("in", ignoreCase = true) && it == INDONESIAN) 
            } ?: ENGLISH
        }
    }
}

object GeoLanguageDetector {

    /**
     * Mendeteksi otomatis bahasa lokal berdasarkan perangkat dan negara pengguna.
     * Mendukung deteksi bahasa dari seluruh dunia: Cina (zh), Jepang (ja), Arab (ar),
     * Korea (ko), Spanyol (es), Prancis (fr), Jerman (de), Rusia (ru), Portugal/Brasil (pt),
     * Indonesia/Malaysia (id/in/ms), dll.
     */
    fun detectLocalLanguage(context: Context? = null): AppLanguage {
        val defaultLocale = Locale.getDefault()
        val langCode = defaultLocale.language.lowercase(Locale.ROOT)
        val countryCode = (getCountryCode(context).ifBlank { defaultLocale.country }).uppercase(Locale.ROOT)
        val tzId = TimeZone.getDefault().id.lowercase(Locale.ROOT)

        // 1. Deteksi Cina / Mandarin (zh, CN, TW, HK, MO)
        if (langCode.startsWith("zh") || countryCode in listOf("CN", "TW", "HK", "MO") ||
            tzId.contains("shanghai") || tzId.contains("beijing") || tzId.contains("taipei") || tzId.contains("hong_kong")
        ) {
            return AppLanguage.CHINESE
        }

        // 2. Deteksi Jepang (ja, JP)
        if (langCode.startsWith("ja") || countryCode == "JP" || tzId.contains("tokyo")) {
            return AppLanguage.JAPANESE
        }

        // 3. Deteksi Korea (ko, KR)
        if (langCode.startsWith("ko") || countryCode == "KR" || tzId.contains("seoul")) {
            return AppLanguage.KOREAN
        }

        // 4. Deteksi Arab (ar, SA, AE, EG, QA, KW, OM, BH, JO, LB, IQ, dll)
        if (langCode.startsWith("ar") || countryCode in listOf("SA", "AE", "EG", "QA", "KW", "OM", "BH", "JO", "LB", "IQ", "MA", "DZ", "TN", "LY", "YE", "SY") ||
            tzId.contains("riyadh") || tzId.contains("dubai") || tzId.contains("cairo") || tzId.contains("doha") || tzId.contains("kuwait")
        ) {
            return AppLanguage.ARABIC
        }

        // 5. Deteksi Spanyol (es, ES, MX, AR, CO, CL, PE, VE, EC, GT, dll)
        if (langCode.startsWith("es") || countryCode in listOf("ES", "MX", "AR", "CO", "CL", "PE", "VE", "EC", "GT", "CU", "BO", "DO", "HN", "PY", "SV", "NI", "CR", "PA", "UY")) {
            return AppLanguage.SPANISH
        }

        // 6. Deteksi Prancis (fr, FR, BE, CA, MC, dll)
        if (langCode.startsWith("fr") || countryCode in listOf("FR", "BE", "MC", "SN", "CI") || tzId.contains("paris") || tzId.contains("brussels")) {
            return AppLanguage.FRENCH
        }

        // 7. Deteksi Jerman (de, DE, AT, CH, LI)
        if (langCode.startsWith("de") || countryCode in listOf("DE", "AT", "LI") || tzId.contains("berlin") || tzId.contains("vienna") || tzId.contains("zurich")) {
            return AppLanguage.GERMAN
        }

        // 8. Deteksi Rusia (ru, RU, BY, KZ, KG)
        if (langCode.startsWith("ru") || countryCode in listOf("RU", "BY", "KZ", "KG") || tzId.contains("moscow") || tzId.contains("minsk") || tzId.contains("almaty")) {
            return AppLanguage.RUSSIAN
        }

        // 9. Deteksi Portugis (pt, BR, PT, AO, MZ)
        if (langCode.startsWith("pt") || countryCode in listOf("BR", "PT", "AO", "MZ") || tzId.contains("sao_paulo") || tzId.contains("lisbon") || tzId.contains("rio")) {
            return AppLanguage.PORTUGUESE
        }

        // 10. Deteksi Indonesia / Melayu (id, in, ms, ID, MY, BN)
        if (langCode in listOf("id", "in", "ms") || countryCode in listOf("ID", "MY", "BN") || isIndonesianOrMalaysianTimeZone(tzId)) {
            return AppLanguage.INDONESIAN
        }

        // 11. Bahasa Inggris jika perangkat berbahasa Inggris atau zona negara global
        if (langCode.startsWith("en") || countryCode in listOf("US", "GB", "AU", "CA", "NZ", "SG", "IE", "IN", "PH", "ZA", "NG", "KE", "GH") ||
            tzId.contains("london") || tzId.startsWith("australia/") || tzId.startsWith("pacific/") || tzId.contains("new_york") || tzId.contains("chicago") || tzId.contains("los_angeles")
        ) {
            return AppLanguage.ENGLISH
        }

        // Fallback berdasarkan kode bahasa ISO atau wilayah
        return when (langCode) {
            "zh" -> AppLanguage.CHINESE
            "ja" -> AppLanguage.JAPANESE
            "ko" -> AppLanguage.KOREAN
            "ar" -> AppLanguage.ARABIC
            "es" -> AppLanguage.SPANISH
            "fr" -> AppLanguage.FRENCH
            "de" -> AppLanguage.GERMAN
            "ru" -> AppLanguage.RUSSIAN
            "pt" -> AppLanguage.PORTUGUESE
            "id", "in", "ms" -> AppLanguage.INDONESIAN
            else -> if (countryCode in listOf("ID", "MY") || isIndonesianOrMalaysianTimeZone(tzId)) AppLanguage.INDONESIAN else AppLanguage.ENGLISH
        }
    }

    /**
     * Backward compatibility untuk kode lama
     */
    fun detectLanguage(context: Context?): AppLanguage {
        return detectLocalLanguage(context)
    }

    fun getCountryOrRegionName(context: Context? = null): String {
        val defaultLocale = Locale.getDefault()
        val lang = detectLocalLanguage(context)
        val country = (getCountryCode(context).ifBlank { defaultLocale.country }).uppercase(Locale.ROOT)
        return when (lang) {
            AppLanguage.CHINESE -> "Cina / China (CN)"
            AppLanguage.JAPANESE -> "Jepang / Japan (JP)"
            AppLanguage.KOREAN -> "Korea Selatan (KR)"
            AppLanguage.ARABIC -> "Timur Tengah / Arab (${country.ifBlank { "AR" }})"
            AppLanguage.SPANISH -> "Spanyol / Amerika Latin (${country.ifBlank { "ES" }})"
            AppLanguage.FRENCH -> "Prancis / France (FR)"
            AppLanguage.GERMAN -> "Jerman / Germany (DE)"
            AppLanguage.RUSSIAN -> "Rusia / Russia (RU)"
            AppLanguage.PORTUGUESE -> "Brasil / Portugal (${country.ifBlank { "BR" }})"
            AppLanguage.INDONESIAN -> "Indonesia / Malaysia (ID/MY)"
            AppLanguage.ENGLISH -> "Internasional / Global (EN)"
            AppLanguage.LOCAL -> "Otomatis Lokal (LO)"
        }
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
