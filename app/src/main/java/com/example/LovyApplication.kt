package com.example

import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log

class LovyApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        exemptHiddenApi()
    }

    private fun exemptHiddenApi() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            try {
                val forName = Class::class.java.getDeclaredMethod("forName", String::class.java)
                val getDeclaredMethod = Class::class.java.getDeclaredMethod(
                    "getDeclaredMethod",
                    String::class.java,
                    arrayOf<Class<*>>()::class.java
                )
                val vmRuntimeClass = forName.invoke(null, "dalvik.system.VMRuntime") as? Class<*>
                if (vmRuntimeClass != null) {
                    val getRuntime = getDeclaredMethod.invoke(vmRuntimeClass, "getRuntime", null) as? java.lang.reflect.Method
                    val setHiddenApiExemptions = getDeclaredMethod.invoke(
                        vmRuntimeClass,
                        "setHiddenApiExemptions",
                        arrayOf(Array<String>::class.java)
                    ) as? java.lang.reflect.Method
                    val sVmRuntime = getRuntime?.invoke(null)
                    if (sVmRuntime != null && setHiddenApiExemptions != null) {
                        setHiddenApiExemptions.invoke(sVmRuntime, arrayOf("L"))
                        Log.d(TAG, "Hidden API exemptions applied successfully")
                    }
                }
            } catch (t: Throwable) {
                Log.w(TAG, "Failed to apply hidden API exemptions", t)
            }
        }
    }

    companion object {
        private const val TAG = "LovyApplication"
    }
}
