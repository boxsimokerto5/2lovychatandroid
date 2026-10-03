package com.example

import android.app.Application
import android.util.Log
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.intercept.Interceptor
import com.example.data.storage.R2StorageClient

class LovyApplication : Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()
        instance = this
        try {
            com.example.data.supabase.SupabaseClient.init(this)
            com.example.data.pocketbase.PocketBaseClient.init(this)
            R2StorageClient.init(this)
        } catch (t: Throwable) {
            Log.w(TAG, "Failed initializing client services in onCreate", t)
        }
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .components {
                add(Interceptor { chain ->
                    val request = chain.request
                    val data = request.data
                    if (data is String) {
                        val resolved = R2StorageClient.getOrRefreshPresignedUrl(data)
                        if (resolved.isNotBlank() && resolved != data) {
                            return@Interceptor chain.proceed(
                                request.newBuilder()
                                    .data(resolved)
                                    .build()
                            )
                        }
                    }
                    chain.proceed(request)
                })
            }
            .crossfade(true)
            .build()
    }

    companion object {
        private const val TAG = "LovyApplication"
        lateinit var instance: LovyApplication
            private set
        val appContext: android.content.Context get() = instance.applicationContext
    }
}
