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
     * Mengubah koordinat Latitude/Longitude menjadi nama kota dan kecamatan nyata menggunakan Geocoder dan Fallback
     */
    fun getCityName(context: Context?, latitude: Double, longitude: Double): String {
        if (context != null) {
            try {
                if (android.location.Geocoder.isPresent()) {
                    val geocoder = android.location.Geocoder(context, Locale("id", "ID"))
                    @Suppress("DEPRECATION")
                    val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                    val address = addresses?.firstOrNull()
                    if (address != null) {
                        val subAdmin = address.subAdminArea // e.g. "Kabupaten Sleman", "Kota Yogyakarta", "Kota Kediri"
                        val locality = address.locality     // e.g. "Kecamatan Depok", "Depok", "Mojoroto"
                        val subLocality = address.subLocality // e.g. "Depok", "Condongcatur", "Gubeng"
                        val admin = address.adminArea       // e.g. "Daerah Istimewa Yogyakarta", "Jawa Timur"

                        // 1. Ekstrak dan bersihkan nama Kota / Kabupaten
                        val cleanCity = when {
                            !subAdmin.isNullOrBlank() -> cleanAdminName(subAdmin)
                            !admin.isNullOrBlank() -> cleanAdminName(admin)
                            else -> ""
                        }

                        // 2. Ekstrak dan bersihkan nama Kecamatan (District)
                        val rawDistrict = when {
                            !subLocality.isNullOrBlank() && !subLocality.equals(subAdmin, ignoreCase = true) -> subLocality
                            !locality.isNullOrBlank() && !locality.equals(subAdmin, ignoreCase = true) -> locality
                            else -> null
                        }

                        val cleanDistrict = rawDistrict?.let { cleanDistrictName(it) }?.takeIf {
                            it.isNotBlank() && !it.equals(cleanCity, ignoreCase = true)
                        }

                        // 3. Susun format komprehensif: "Kec. [Kecamatan], [Kota]"
                        if (!cleanDistrict.isNullOrBlank() && cleanCity.isNotBlank()) {
                            return "Kec. $cleanDistrict, $cleanCity"
                        } else if (cleanCity.isNotBlank()) {
                            return cleanCity
                        } else if (!cleanDistrict.isNullOrBlank()) {
                            return "Kec. $cleanDistrict"
                        }
                    }
                }
            } catch (_: Throwable) {
                // Geocoder service may be offline or rate-limited
            }
        }

        // Fallback cerdas berbasis jarak terdekat ke pusat kecamatan / kota di Indonesia
        return resolveClosestIndonesianCity(latitude, longitude)
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

    private fun resolveClosestIndonesianCity(lat: Double, lon: Double): String {
        data class CityCoordinate(val name: String, val lat: Double, val lon: Double)
        val cities = listOf(
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

            // Jawa Timur - Kediri & Sekitarnya (Kecamatan detail)
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

        var closestCity = "Surabaya"
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
