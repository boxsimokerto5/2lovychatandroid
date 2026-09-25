package com.example

import android.app.Application
import android.util.Log

class LovyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        try {
            com.example.data.supabase.SupabaseClient.init(this)
            com.example.data.storage.R2StorageClient.init(this)
        } catch (t: Throwable) {
            Log.w(TAG, "Failed initializing client services in onCreate", t)
        }
    }

    companion object {
        private const val TAG = "LovyApplication"
    }
}
