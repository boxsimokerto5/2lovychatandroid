package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ui.LovyChatViewModel
import com.example.ui.screens.DiscoverTabScreen
import com.example.ui.screens.MainAppScreen
import com.example.ui.theme.LovyChatTheme
import com.example.util.AdManager
import com.ironsource.mediationsdk.IronSource

class MainActivity : ComponentActivity() {
  private val viewModel: LovyChatViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

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

  override fun onResume() {
    super.onResume()
    AdManager.updateCurrentActivity(this)
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

