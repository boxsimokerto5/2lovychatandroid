package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.ui.LovyChatViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Lovy Chat", appName)
  }

  @Test
  fun `verify initial discover tab and throw bottle`() {
    val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    val viewModel = LovyChatViewModel(app)
    val state = viewModel.uiState.value

    // Discover is tab 2 by default as in user screenshot
    assertEquals(2, state.currentTab)
    assertTrue(state.nearbyUsers.isNotEmpty())
    assertTrue(state.oceanBottles.isNotEmpty())

    // Test throwing bottle
    val success = viewModel.throwBottle("Halo teman Lovy!")
    assertTrue(success)
    assertEquals(1, viewModel.uiState.value.myBottles.size)
    assertEquals("Halo teman Lovy!", viewModel.uiState.value.myBottles.first().content)
  }

  @Test
  fun `test supabase credentials update in viewModel`() {
    val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    val viewModel = LovyChatViewModel(app)
    val testUrl = "https://exampletest.supabase.co"
    val testKey = "test-anon-key-123"

    viewModel.saveSupabaseCredentials(testUrl, testKey)
    val state = viewModel.uiState.value
    assertEquals("https://exampletest.supabase.co/", state.supabaseUrl)
    assertEquals(testKey, state.supabaseAnonKey)
    assertTrue(state.isSupabaseConnected)

    viewModel.clearSupabaseCredentials()
    val clearedState = viewModel.uiState.value
    assertTrue(clearedState.supabaseUrl.isEmpty() || clearedState.supabaseUrl.contains("your-project-id") || clearedState.supabaseUrl != "https://exampletest.supabase.co/")
  }

  @Test
  fun `test splash to login and login to main flow`() {
    val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    val viewModel = LovyChatViewModel(app)

    // Initial state is Splash
    assertEquals(com.example.ui.CurrentScreen.Splash, viewModel.uiState.value.currentScreen)

    // Splash finished -> Login
    viewModel.onSplashFinished()
    assertEquals(com.example.ui.CurrentScreen.Login, viewModel.uiState.value.currentScreen)

    // Login with name -> Main
    viewModel.loginUser("Andi")
    assertEquals(com.example.ui.CurrentScreen.Main, viewModel.uiState.value.currentScreen)
    assertEquals("Andi", viewModel.uiState.value.myName)
    assertTrue(viewModel.uiState.value.isLoggedIn)

    // Logout -> Login
    viewModel.logout()
    assertEquals(com.example.ui.CurrentScreen.Login, viewModel.uiState.value.currentScreen)
  }

  @Test
  fun `test geo language detection and manual switch`() {
    val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    val viewModel = LovyChatViewModel(app)

    // Language should be initialized (either ID or EN)
    val initialLang = viewModel.uiState.value.language
    assertNotNull(initialLang)

    // Switch to English
    viewModel.setLanguage(com.example.util.AppLanguage.ENGLISH)
    assertEquals(com.example.util.AppLanguage.ENGLISH, viewModel.uiState.value.language)
    assertEquals("Discover", com.example.util.AppStrings.tabDiscover(viewModel.uiState.value.language))
    assertEquals("New friends, fun friends", com.example.util.AppStrings.motto(viewModel.uiState.value.language))

    // Switch to Indonesian
    viewModel.setLanguage(com.example.util.AppLanguage.INDONESIAN)
    assertEquals(com.example.util.AppLanguage.INDONESIAN, viewModel.uiState.value.language)
    assertEquals("Temukan", com.example.util.AppStrings.tabDiscover(viewModel.uiState.value.language))
  }

  @Test
  fun `test native gps integration and distance calculation`() {
    val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    val viewModel = LovyChatViewModel(app)

    // Test distance calculation utility
    val distance = com.example.util.AndroidGpsTracker.calculateDistanceMeters(
        -6.2088, 106.8456, // Jakarta
        -6.2188, 106.8556  // Dekat Jakarta
    )
    assertTrue(distance > 0)

    // Update GPS location state
    val mockLoc = com.example.util.UserGpsLocation(
        latitude = -6.2088,
        longitude = 106.8456,
        accuracy = 12f,
        provider = "GPS",
        readableLocation = "GPS: -6.2088, 106.8456"
    )
    viewModel.updateGpsLocation(mockLoc)
    assertEquals(mockLoc, viewModel.uiState.value.currentGpsLocation)

    // Update location permission
    viewModel.updateLocationPermission(true)
    assertTrue(viewModel.uiState.value.hasLocationPermission)
  }

  @Test
  fun `test google client id configuration and login user`() {
    val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    val viewModel = LovyChatViewModel(app)

    // Verify Google Client ID is configured correctly
    assertEquals(
        "347302027962-arib0vs4tq697ole1ua23kojq1uqbs3i.apps.googleusercontent.com",
        com.example.util.GoogleAuthHelper.SERVER_CLIENT_ID
    )

    // Verify login with Google account name
    viewModel.loginUser("Fauzan Google")
    assertTrue(viewModel.uiState.value.isLoggedIn)
    assertEquals("Fauzan Google", viewModel.uiState.value.myName)
  }

  @Test
  fun `test nearby users list and expansion on rewarded ad trigger`() {
    val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    val viewModel = LovyChatViewModel(app)

    val state = viewModel.uiState.value
    // Default initial state: isNearbyExpanded should be false
    org.junit.Assert.assertFalse(state.isNearbyExpanded)
    // Nearby users list has plenty of users (more than 6)
    assertTrue(state.nearbyUsers.size > 6)

    // Expand nearby users
    viewModel.expandNearbyUsers()
    assertTrue(viewModel.uiState.value.isNearbyExpanded)

    // Reset nearby expansion
    viewModel.resetNearbyExpansion()
    org.junit.Assert.assertFalse(viewModel.uiState.value.isNearbyExpanded)
  }

  @Test
  fun `test interstitial ad triggers after 20 feature clicks and excludes chat`() {
    val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    val viewModel = LovyChatViewModel(app)

    com.example.util.AdManager.resetFeatureClickCount()
    assertEquals(0, com.example.util.AdManager.featureClickCount)

    // Simulate 19 feature clicks (e.g. switching tabs, navigating, filtering)
    repeat(19) {
      viewModel.recordFeatureClick()
    }
    assertEquals(19, com.example.util.AdManager.featureClickCount)

    // Sending chat message should NOT increment the click count
    viewModel.sendMessage("conv_test", "Halo ini pesan obrolan", "User Test")
    assertEquals(19, com.example.util.AdManager.featureClickCount)

    // 20th feature click should trigger the interstitial threshold and reset count to 0
    viewModel.recordFeatureClick()
    assertEquals(0, com.example.util.AdManager.featureClickCount)
  }

  @Test
  fun `test UserProfile data model and Room database persistence`() = kotlinx.coroutines.test.runTest {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val inMemoryDb = androidx.room.Room.inMemoryDatabaseBuilder(
        context,
        com.example.data.local.AppDatabase::class.java
    ).allowMainThreadQueries().build()

    val dao = inMemoryDb.userProfileDao()
    val repository = com.example.data.local.UserProfileRepository(dao)

    val testProfile = com.example.model.UserProfile(
        id = "test_user_1",
        displayName = "Aisyah Lovy",
        bio = "Halo dari Room Database! ✨",
        profilePicture = "https://example.com/avatar.jpg",
        lovyId = "lovy_123456",
        city = "Surabaya",
        gender = "FEMALE",
        age = 23
    )

    // Save to Room
    repository.saveProfile(testProfile)

    // Retrieve from Room
    val retrieved = repository.getProfile("test_user_1")
    assertNotNull(retrieved)
    assertEquals("Aisyah Lovy", retrieved?.displayName)
    assertEquals("Halo dari Room Database! ✨", retrieved?.bio)
    assertEquals("https://example.com/avatar.jpg", retrieved?.profilePicture)
    assertEquals("lovy_123456", retrieved?.lovyId)
    assertEquals("Surabaya", retrieved?.city)

    // Update bio and profile picture in Room
    val updatedProfile = testProfile.copy(
        bio = "Bio terupdate di Room SQLite!",
        profilePicture = "https://example.com/new_avatar.jpg"
    )
    repository.saveProfile(updatedProfile)

    val updatedRetrieved = repository.getProfile("test_user_1")
    assertNotNull(updatedRetrieved)
    assertEquals("Bio terupdate di Room SQLite!", updatedRetrieved?.bio)
    assertEquals("https://example.com/new_avatar.jpg", updatedRetrieved?.profilePicture)

    inMemoryDb.close()
  }

  @Test
  fun `test viewModel UserProfile navigation and update`() {
    val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    val viewModel = LovyChatViewModel(app)

    // Navigate to UserProfile screen
    viewModel.navigateTo(com.example.ui.CurrentScreen.UserProfile)
    assertEquals(com.example.ui.CurrentScreen.UserProfile, viewModel.uiState.value.currentScreen)

    // Update profile via ViewModel
    val newProfile = viewModel.uiState.value.userProfile.copy(
        displayName = "Budi Santoso",
        bio = "Pecinta kopi dan petualangan di Lovy Chat",
        profilePicture = "https://example.com/budi.jpg"
    )
    viewModel.saveUserProfile(newProfile)
    org.robolectric.shadows.ShadowLooper.idleMainLooper()

    assertEquals("Budi Santoso", viewModel.uiState.value.myName)
    assertEquals("Pecinta kopi dan petualangan di Lovy Chat", viewModel.uiState.value.myBio)
    assertEquals("https://example.com/budi.jpg", viewModel.uiState.value.userProfile.profilePicture)

    // Navigate back to Main
    viewModel.navigateBack()
    assertEquals(com.example.ui.CurrentScreen.Main, viewModel.uiState.value.currentScreen)
  }

  @Test
  fun `test open chat detail and partner info`() {
    val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    val viewModel = LovyChatViewModel(app)

    val conv = viewModel.uiState.value.conversations.first()
    viewModel.openChat(conv.id, conv.partnerName, conv.partnerAvatarHex)

    val currentScreen = viewModel.uiState.value.currentScreen
    assertTrue(currentScreen is com.example.ui.CurrentScreen.ChatDetail)
    val chatDetail = currentScreen as com.example.ui.CurrentScreen.ChatDetail
    assertEquals(conv.id, chatDetail.conversationId)
    assertEquals(conv.partnerName, chatDetail.partnerName)
  }
}

