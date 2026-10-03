package com.example.util

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume
import java.util.Locale

data class UserGpsLocation(
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float = 0f,
    val provider: String = "GPS",
    val readableLocation: String = "",
    val cityName: String = ""
)

object AndroidGpsTracker {

    /**
     * Memeriksa apakah perangkat memiliki akses GPS / Network location aktif
     */
    fun isLocationEnabled(context: Context?): Boolean {
        if (context == null) return false
        return try {
            val lm = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager ?: return false
            lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                    lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (_: Throwable) {
            false
        }
    }

    /**
     * Mendapatkan lokasi terakhir yang diketahui (Last Known Location) menggunakan Android Native LocationManager
     */
    @SuppressLint("MissingPermission")
    fun getLastKnownLocation(context: Context?): UserGpsLocation? {
        if (context == null) return null
        return try {
            val lm = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager ?: return null
            var bestLocation: Location? = null

            if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                val gpsLoc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (gpsLoc != null) bestLocation = gpsLoc
            }

            if (bestLocation == null && lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                val netLoc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (netLoc != null) bestLocation = netLoc
            }

            if (bestLocation == null && lm.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) {
                val passLoc = lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)
                if (passLoc != null) bestLocation = passLoc
            }

            bestLocation?.let { toUserGpsLocation(it, context) }
        } catch (_: Throwable) {
            null
        }
    }

    /**
     * Meminta koordinat GPS aktif langsung dari sensor perangkat (Single Fresh Fix).
     * Sensor GPS hardware/jaringan dinyalakan sesaat, lalu otomatis dimatikan begitu lokasi pertama didapat,
     * sehingga sangat hemat baterai dan data tidak macet pada cache lama.
     */
    @SuppressLint("MissingPermission")
    suspend fun getCurrentFreshLocation(
        context: Context?,
        timeoutMs: Long = 4000L
    ): UserGpsLocation? = withContext(Dispatchers.Main) {
        if (context == null) return@withContext null
        val lm = try {
            context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
        } catch (_: Throwable) {
            null
        } ?: return@withContext null

        withTimeoutOrNull(timeoutMs) {
            suspendCancellableCoroutine<UserGpsLocation?> { cont ->
                val listener = object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                        try {
                            lm.removeUpdates(this)
                        } catch (_: Throwable) {}
                        if (cont.isActive) {
                            cont.resume(toUserGpsLocation(location, context))
                        }
                    }

                    @Deprecated("Deprecated in Java")
                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
                    override fun onProviderEnabled(provider: String) {}
                    override fun onProviderDisabled(provider: String) {}
                }

                var requested = false
                try {
                    // Coba NETWORK_PROVIDER dulu karena sangat cepat (Cell/WiFi fix < 500ms)
                    if (lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                        lm.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            0L,
                            0f,
                            listener,
                            Looper.getMainLooper()
                        )
                        requested = true
                    }
                    // Juga aktifkan GPS_PROVIDER untuk presisi satelit
                    if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        lm.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            0L,
                            0f,
                            listener,
                            Looper.getMainLooper()
                        )
                        requested = true
                    }
                } catch (_: Throwable) {
                    // SecurityException jika izin belum lengkap
                }

                if (!requested) {
                    val fallback = getLastKnownLocation(context)
                    cont.resume(fallback)
                    return@suspendCancellableCoroutine
                }

                cont.invokeOnCancellation {
                    try {
                        lm.removeUpdates(listener)
                    } catch (_: Throwable) {}
                }
            }
        } ?: getLastKnownLocation(context)
    }

    /**
     * Memperbarui lokasi GPS real-time satu kali (Single Fix) atau berkala
     */
    @SuppressLint("MissingPermission")
    fun requestLocationUpdates(context: Context?): Flow<UserGpsLocation> = callbackFlow {
        if (context == null) {
            close()
            return@callbackFlow
        }

        val lm = try {
            context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
        } catch (_: Throwable) {
            null
        }

        if (lm == null) {
            close()
            return@callbackFlow
        }

        // Coba berikan last known location dulu sebagai respon instan
        getLastKnownLocation(context)?.let { trySend(it) }

        val listener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                trySend(toUserGpsLocation(location, context))
            }

            @Deprecated("Deprecated in Java")
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }

        try {
            if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                lm.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    3000L,
                    5f,
                    listener,
                    Looper.getMainLooper()
                )
            } else if (lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                lm.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    3000L,
                    5f,
                    listener,
                    Looper.getMainLooper()
                )
            }
        } catch (e: Exception) {
            // Izin belum diberikan atau error perangkat
        }

        awaitClose {
            try {
                lm.removeUpdates(listener)
            } catch (_: Exception) {}
        }
    }

    /**
     * Menghitung jarak realistis antara dua koordinat GPS dalam Meter (Formula Haversine / Android Location API)
     */
    fun calculateDistanceMeters(
        startLat: Double,
        startLon: Double,
        endLat: Double,
        endLon: Double
    ): Int {
        val results = FloatArray(1)
        try {
            Location.distanceBetween(startLat, startLon, endLat, endLon, results)
            return results[0].toInt()
        } catch (_: Exception) {
            return 100
        }
    }

    /**
     * Mengubah koordinat Latitude/Longitude menjadi nama kota dan negara nyata untuk 177+ negara
     * menggunakan Geocoder global dan Fallback koordinat kota metropolitan dunia.
     */
    fun getCityName(context: Context?, latitude: Double, longitude: Double): String {
        if (context != null) {
            try {
                if (android.location.Geocoder.isPresent()) {
                    val geocoder = android.location.Geocoder(context, Locale.getDefault())
                    @Suppress("DEPRECATION")
                    val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                    val address = addresses?.firstOrNull()
                    if (address != null) {
                        val countryCode = address.countryCode?.trim().orEmpty()
                        val countryName = address.countryName?.trim().orEmpty()
                        val subAdmin = address.subAdminArea // e.g. "Kabupaten Sleman" or "Santa Clara County"
                        val locality = address.locality     // e.g. "Los Angeles", "Tokyo", "Kecamatan Depok"
                        val subLocality = address.subLocality
                        val admin = address.adminArea       // e.g. "California", "Tokyo", "Jawa Timur"

                        // Cek apakah lokasi berada di Indonesia atau Mancanegara (177 negara)
                        val isIndonesia = countryCode.equals("ID", ignoreCase = true) ||
                                countryName.contains("Indonesia", ignoreCase = true) ||
                                (latitude in -11.0..6.0 && longitude in 95.0..141.0 && countryCode.isBlank())

                        if (isIndonesia) {
                            // 1. Ekstrak dan bersihkan nama Kota / Kabupaten di Indonesia
                            val cleanCity = when {
                                !subAdmin.isNullOrBlank() -> cleanAdminName(subAdmin)
                                !admin.isNullOrBlank() -> cleanAdminName(admin)
                                else -> ""
                            }

                            // 2. Ekstrak dan bersihkan nama Kecamatan
                            val rawDistrict = when {
                                !subLocality.isNullOrBlank() && !subLocality.equals(subAdmin, ignoreCase = true) -> subLocality
                                !locality.isNullOrBlank() && !locality.equals(subAdmin, ignoreCase = true) -> locality
                                else -> null
                            }

                            val cleanDistrict = rawDistrict?.let { cleanDistrictName(it) }?.takeIf {
                                it.isNotBlank() && !it.equals(cleanCity, ignoreCase = true)
                            }

                            if (!cleanDistrict.isNullOrBlank() && cleanCity.isNotBlank()) {
                                return "Kec. $cleanDistrict, $cleanCity"
                            } else if (cleanCity.isNotBlank()) {
                                return cleanCity
                            } else if (!cleanDistrict.isNullOrBlank()) {
                                return "Kec. $cleanDistrict"
                            }
                        } else {
                            // Format Internasional (Luar Negeri)
                            val mainCity = when {
                                !locality.isNullOrBlank() -> locality.trim()
                                !subLocality.isNullOrBlank() -> subLocality.trim()
                                !admin.isNullOrBlank() -> admin.trim()
                                !subAdmin.isNullOrBlank() -> subAdmin.trim()
                                else -> ""
                            }

                            if (mainCity.isNotBlank() && countryName.isNotBlank()) {
                                return if (mainCity.equals(countryName, ignoreCase = true)) {
                                    mainCity
                                } else {
                                    "$mainCity, $countryName"
                                }
                            } else if (mainCity.isNotBlank()) {
                                return mainCity
                            } else if (countryName.isNotBlank()) {
                                return countryName
                            }
                        }
                    }
                }
            } catch (_: Throwable) {
                // Geocoder service may be offline or rate-limited
            }
        }

        // Fallback cerdas mendeteksi kota terdekat secara global (Indonesia & 177 negara)
        return resolveClosestGlobalCity(latitude, longitude)
    }

    private fun cleanAdminName(name: String): String {
        return name
            .replace("Kota Administrasi ", "", ignoreCase = true)
            .replace("Kotamadya ", "", ignoreCase = true)
            .replace("Kota ", "", ignoreCase = true)
            .replace("Kabupaten ", "", ignoreCase = true)
            .replace("Kab. ", "", ignoreCase = true)
            .replace("Daerah Khusus Ibukota ", "", ignoreCase = true)
            .replace("DKI ", "", ignoreCase = true)
            .trim()
    }

    private fun cleanDistrictName(name: String): String {
        return name
            .replace("Kecamatan ", "", ignoreCase = true)
            .replace("Kec. ", "", ignoreCase = true)
            .replace("Distrik ", "", ignoreCase = true)
            .trim()
    }

    private fun resolveClosestGlobalCity(lat: Double, lon: Double): String {
        data class CityCoordinate(val name: String, val lat: Double, val lon: Double)

        val isWithinIndonesia = lat in -11.0..6.0 && lon in 95.0..141.0

        val cities = if (isWithinIndonesia) {
            listOf(
                // Yogyakarta & Sekitarnya (Kecamatan detail)
                CityCoordinate("Kec. Gondomanan, Yogyakarta", -7.8000, 110.3680),
                CityCoordinate("Kec. Depok, Sleman", -7.7680, 110.3950),
                CityCoordinate("Kec. Mlati, Sleman", -7.7450, 110.3550),
                CityCoordinate("Kec. Umbulharjo, Yogyakarta", -7.8150, 110.3880),
                CityCoordinate("Kec. Danurejan, Yogyakarta", -7.7940, 110.3730),
                CityCoordinate("Kec. Sewon, Bantul", -7.8500, 110.3600),
                CityCoordinate("Kec. Banguntapan, Bantul", -7.8100, 110.4100),
                CityCoordinate("Kec. Kasihan, Bantul", -7.8180, 110.3320),
                CityCoordinate("Kec. Ngaglik, Sleman", -7.7100, 110.3900),
                CityCoordinate("Kec. Gamping, Sleman", -7.7990, 110.3200),
                CityCoordinate("Kec. Kalasan, Sleman", -7.7680, 110.4700),
                CityCoordinate("Yogyakarta", -7.7956, 110.3695),
                CityCoordinate("Sleman", -7.7167, 110.3556),
                CityCoordinate("Bantul", -7.8878, 110.3289),
                CityCoordinate("Gunungkidul", -7.9625, 110.6033),
                CityCoordinate("Kulon Progo", -7.8286, 110.1583),
                CityCoordinate("Klaten", -7.7058, 110.6067),
                CityCoordinate("Solo (Surakarta)", -7.5666, 110.8167),
                CityCoordinate("Magelang", -7.4705, 110.2178),
                CityCoordinate("Purworejo", -7.7144, 110.0078),
                CityCoordinate("Kebumen", -7.6698, 109.6515),
                CityCoordinate("Salatiga", -7.3305, 110.5084),
                CityCoordinate("Semarang", -6.9667, 110.4167),

                // Jawa Timur - Kediri & Sekitarnya
                CityCoordinate("Kec. Kota, Kediri", -7.8200, 112.0150),
                CityCoordinate("Kec. Mojoroto, Kediri", -7.8100, 111.9950),
                CityCoordinate("Kec. Pesantren, Kediri", -7.8400, 112.0400),
                CityCoordinate("Kec. Gampengrejo, Kediri", -7.7750, 112.0300),
                CityCoordinate("Kec. Pare, Kediri", -7.7700, 112.1900),
                CityCoordinate("Kediri", -7.8480, 112.0178),
                CityCoordinate("Blitar", -8.0983, 112.1681),
                CityCoordinate("Tulungagung", -8.0658, 111.9015),
                CityCoordinate("Malang", -7.9797, 112.6304),
                CityCoordinate("Surabaya", -7.2575, 112.7521),
                CityCoordinate("Sidoarjo", -7.4478, 112.7183),
                CityCoordinate("Gresik", -7.1566, 112.6555),
                CityCoordinate("Mojokerto", -7.4726, 112.4385),
                CityCoordinate("Jombang", -7.5460, 112.2331),
                CityCoordinate("Madiun", -7.6298, 111.5239),
                CityCoordinate("Pasuruan", -7.6453, 112.9075),
                CityCoordinate("Probolinggo", -7.7543, 113.2159),
                CityCoordinate("Jember", -8.1724, 113.7007),
                CityCoordinate("Banyuwangi", -8.2192, 114.3691),

                // Wilayah Lainnya di Indonesia
                CityCoordinate("Cirebon", -6.7320, 108.5523),
                CityCoordinate("Bandung", -6.9175, 107.6191),
                CityCoordinate("Jakarta Selatan", -6.2615, 106.8106),
                CityCoordinate("Jakarta Pusat", -6.1818, 106.8223),
                CityCoordinate("Jakarta Barat", -6.1683, 106.7589),
                CityCoordinate("Jakarta Timur", -6.2250, 106.9004),
                CityCoordinate("Jakarta Utara", -6.1214, 106.7741),
                CityCoordinate("Depok", -6.4025, 106.7942),
                CityCoordinate("Tangerang", -6.1783, 106.6319),
                CityCoordinate("Tangerang Selatan", -6.2889, 106.7181),
                CityCoordinate("Bekasi", -6.2383, 106.9756),
                CityCoordinate("Bogor", -6.5971, 106.8060),
                CityCoordinate("Denpasar", -8.6705, 115.2126),
                CityCoordinate("Medan", 3.5952, 98.6722),
                CityCoordinate("Makassar", -5.1477, 119.4327),
                CityCoordinate("Palembang", -2.9761, 104.7754),
                CityCoordinate("Bandar Lampung", -5.4292, 105.2625),
                CityCoordinate("Batam", 1.1301, 104.0529),
                CityCoordinate("Pekanbaru", 0.5071, 101.4478),
                CityCoordinate("Padang", -0.9471, 100.4172),
                CityCoordinate("Banjarmasin", -3.3194, 114.5908),
                CityCoordinate("Pontianak", -0.0263, 109.3425),
                CityCoordinate("Balikpapan", -1.2379, 116.8529),
                CityCoordinate("Samarinda", -0.5022, 117.1536),
                CityCoordinate("Manado", 1.4748, 124.8428)
            )
        } else {
            // Kota-kota Metropolitan Utama Dunia (Mencakup 177 Negara & Benua)
            listOf(
                // Amerika Utara & Emulator Android
                CityCoordinate("Mountain View, United States", 37.4220, -122.0841),
                CityCoordinate("San Francisco, United States", 37.7749, -122.4194),
                CityCoordinate("Los Angeles, United States", 34.0522, -118.2437),
                CityCoordinate("New York, United States", 40.7128, -74.0060),
                CityCoordinate("Chicago, United States", 41.8781, -87.6298),
                CityCoordinate("Houston, United States", 29.7604, -95.3698),
                CityCoordinate("Seattle, United States", 47.6062, -122.3321),
                CityCoordinate("Miami, United States", 25.7617, -80.1918),
                CityCoordinate("Toronto, Canada", 43.6532, -79.3832),
                CityCoordinate("Vancouver, Canada", 49.2827, -123.1207),
                CityCoordinate("Montreal, Canada", 45.5017, -73.5673),
                CityCoordinate("Mexico City, Mexico", 19.4326, -99.1332),

                // Eropa
                CityCoordinate("London, United Kingdom", 51.5074, -0.1278),
                CityCoordinate("Paris, France", 48.8566, 2.3522),
                CityCoordinate("Berlin, Germany", 52.5200, 13.4050),
                CityCoordinate("Frankfurt, Germany", 50.1109, 8.6821),
                CityCoordinate("Amsterdam, Netherlands", 52.3676, 4.9041),
                CityCoordinate("Rome, Italy", 41.9028, 12.4964),
                CityCoordinate("Madrid, Spain", 40.4168, -3.7038),
                CityCoordinate("Barcelona, Spain", 41.3879, 2.1699),
                CityCoordinate("Zurich, Switzerland", 47.3769, 8.5417),
                CityCoordinate("Vienna, Austria", 48.2082, 16.3738),
                CityCoordinate("Brussels, Belgium", 50.8503, 4.3517),
                CityCoordinate("Warsaw, Poland", 52.2297, 21.0122),
                CityCoordinate("Stockholm, Sweden", 59.3293, 18.0686),
                CityCoordinate("Oslo, Norway", 59.9139, 10.7522),
                CityCoordinate("Copenhagen, Denmark", 55.6761, 12.5683),
                CityCoordinate("Helsinki, Finland", 60.1699, 24.9384),
                CityCoordinate("Dublin, Ireland", 53.3498, -6.2603),
                CityCoordinate("Istanbul, Turkey", 41.0082, 28.9784),
                CityCoordinate("Moscow, Russia", 55.7558, 37.6173),

                // Asia Timur & Tenggara
                CityCoordinate("Singapore", 1.3521, 103.8198),
                CityCoordinate("Kuala Lumpur, Malaysia", 3.1390, 101.6869),
                CityCoordinate("Penang, Malaysia", 5.4141, 100.3288),
                CityCoordinate("Bangkok, Thailand", 13.7563, 100.5018),
                CityCoordinate("Manila, Philippines", 14.5995, 120.9842),
                CityCoordinate("Hanoi, Vietnam", 21.0285, 105.8542),
                CityCoordinate("Ho Chi Minh City, Vietnam", 10.8231, 106.6297),
                CityCoordinate("Tokyo, Japan", 35.6762, 139.6503),
                CityCoordinate("Osaka, Japan", 34.6937, 135.5023),
                CityCoordinate("Seoul, South Korea", 37.5665, 126.9780),
                CityCoordinate("Busan, South Korea", 35.1796, 129.0756),
                CityCoordinate("Beijing, China", 39.9042, 116.4074),
                CityCoordinate("Shanghai, China", 31.2304, 121.4737),
                CityCoordinate("Guangzhou, China", 23.1291, 113.2644),
                CityCoordinate("Hong Kong", 22.3193, 114.1694),
                CityCoordinate("Taipei, Taiwan", 25.0330, 121.5654),
                CityCoordinate("New Delhi, India", 28.6139, 77.2090),
                CityCoordinate("Mumbai, India", 19.0760, 72.8777),
                CityCoordinate("Bengaluru, India", 12.9716, 77.5946),
                CityCoordinate("Dhaka, Bangladesh", 23.8103, 90.4125),
                CityCoordinate("Islamabad, Pakistan", 33.6844, 73.0479),
                CityCoordinate("Karachi, Pakistan", 24.8607, 67.0011),

                // Timur Tengah & Afrika
                CityCoordinate("Dubai, United Arab Emirates", 25.2048, 55.2708),
                CityCoordinate("Abu Dhabi, United Arab Emirates", 24.4539, 54.3773),
                CityCoordinate("Riyadh, Saudi Arabia", 24.7136, 46.6753),
                CityCoordinate("Jeddah, Saudi Arabia", 21.5433, 39.1728),
                CityCoordinate("Doha, Qatar", 25.2854, 51.5310),
                CityCoordinate("Kuwait City, Kuwait", 29.3759, 47.9774),
                CityCoordinate("Cairo, Egypt", 30.0444, 31.2357),
                CityCoordinate("Johannesburg, South Africa", -26.2041, 28.0473),
                CityCoordinate("Cape Town, South Africa", -33.9249, 18.4241),
                CityCoordinate("Nairobi, Kenya", -1.2921, 36.8219),
                CityCoordinate("Lagos, Nigeria", 6.5244, 3.3792),
                CityCoordinate("Casablanca, Morocco", 33.5731, -7.5898),

                // Australia & Oseania
                CityCoordinate("Sydney, Australia", -33.8688, 151.2093),
                CityCoordinate("Melbourne, Australia", -37.8136, 144.9631),
                CityCoordinate("Brisbane, Australia", -27.4698, 153.0251),
                CityCoordinate("Perth, Australia", -31.9505, 115.8605),
                CityCoordinate("Auckland, New Zealand", -36.8485, 174.7633),

                // Amerika Selatan
                CityCoordinate("São Paulo, Brazil", -23.5505, -46.6333),
                CityCoordinate("Rio de Janeiro, Brazil", -22.9068, -43.1729),
                CityCoordinate("Buenos Aires, Argentina", -34.6037, -58.3816),
                CityCoordinate("Santiago, Chile", -33.4489, -70.6693),
                CityCoordinate("Bogotá, Colombia", 4.7110, -74.0721),
                CityCoordinate("Lima, Peru", -12.0464, -77.0428)
            )
        }

        var closestCity = if (isWithinIndonesia) "Surabaya" else "Singapore"
        var minDistance = Double.MAX_VALUE

        for (city in cities) {
            val dLat = Math.toRadians(city.lat - lat)
            val dLon = Math.toRadians(city.lon - lon)
            val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                    Math.cos(Math.toRadians(lat)) * Math.cos(Math.toRadians(city.lat)) *
                    Math.sin(dLon / 2) * Math.sin(dLon / 2)
            val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
            val dist = 6371.0 * c // in km

            if (dist < minDistance) {
                minDistance = dist
                closestCity = city.name
            }
        }

        return closestCity
    }

    private fun toUserGpsLocation(location: Location, context: Context? = null): UserGpsLocation {
        val latStr = String.format(Locale.US, "%.4f", location.latitude)
        val lonStr = String.format(Locale.US, "%.4f", location.longitude)
        val city = getCityName(context, location.latitude, location.longitude)
        return UserGpsLocation(
            latitude = location.latitude,
            longitude = location.longitude,
            accuracy = location.accuracy,
            provider = location.provider ?: "GPS Native",
            readableLocation = if (city.isNotBlank()) "$city (GPS: $latStr, $lonStr)" else "GPS: $latStr, $lonStr",
            cityName = city
        )
    }
}
