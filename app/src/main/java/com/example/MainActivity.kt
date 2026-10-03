package com.example

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.example.ui.LovyChatViewModel
import com.example.ui.screens.DiscoverTabScreen
import com.example.ui.screens.MainAppScreen
import com.example.ui.theme.LovyChatTheme
import com.example.util.AdManager
import com.ironsource.mediationsdk.IronSource

class MainActivity : ComponentActivity() {
  private val viewModel: LovyChatViewModel by viewModels()

  private val requestNotificationPermissionLauncher =
    registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
      if (isGranted) {
        android.util.Log.d("MainActivity", "Notification permission granted")
      } else {
        android.util.Log.w("MainActivity", "Notification permission denied")
      }
    }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    // Inisialisasi channel notifikasi lengkap dengan getar & suara
    com.example.util.LovyNotificationHelper.createNotificationChannel(this)

    // Cek jika intent berasal dari notifikasi push chat
    handleNotificationIntent(intent)

    // Initialize ironSource SDK with Lovy Chat App Key
    AdManager.init(this)

    setContent {
      LovyChatTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
          MainAppScreen(viewModel = viewModel)
        }
      }
    }
  }

  override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)
    setIntent(intent)
    handleNotificationIntent(intent)
  }

  private fun handleNotificationIntent(intent: Intent?) {
    val convId = intent?.getStringExtra("extra_conversation_id")
    val sender = intent?.getStringExtra("extra_sender_name") ?: "Teman"
    if (!convId.isNullOrBlank()) {
      viewModel.openChat(convId, sender, 0xFF4CAF50)
    }
  }

  private fun checkAndRequestNotificationPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      if (ContextCompat.checkSelfPermission(
          this,
          Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
      ) {
        requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
      }
    }
  }

  override fun onResume() {
    super.onResume()
    AdManager.updateCurrentActivity(this)
    viewModel.checkForAppUpdate()
    try {
      IronSource.onResume(this)
    } catch (e: Throwable) {
      android.util.Log.w("MainActivity", "IronSource.onResume failed", e)
    }
  }

  override fun onPause() {
    super.onPause()
    try {
      IronSource.onPause(this)
    } catch (e: Throwable) {
      android.util.Log.w("MainActivity", "IronSource.onPause failed", e)
    }
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DiscoverScreenPreview() {
  LovyChatTheme {
    DiscoverTabScreen(
      onNavigateToNearby = {},
      onNavigateToBottle = {},
      onNavigateToMoments = {}
    )
  }
}

