package com.example.util

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.Locale

data class UserGpsLocation(
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float = 0f,
    val provider: String = "GPS",
    val readableLocation: String = ""
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

            bestLocation?.let { toUserGpsLocation(it) }
        } catch (_: Throwable) {
            null
        }
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
                trySend(toUserGpsLocation(location))
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

    private fun toUserGpsLocation(location: Location): UserGpsLocation {
        val latStr = String.format(Locale.US, "%.4f", location.latitude)
        val lonStr = String.format(Locale.US, "%.4f", location.longitude)
        return UserGpsLocation(
            latitude = location.latitude,
            longitude = location.longitude,
            accuracy = location.accuracy,
            provider = location.provider ?: "GPS Native",
            readableLocation = "GPS: $latStr, $lonStr"
        )
    }
}
