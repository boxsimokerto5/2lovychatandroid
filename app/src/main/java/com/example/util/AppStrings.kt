package com.example.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable

object AppStrings {
    // Motto (New friends, fun friends)
    fun motto(lang: AppLanguage): String = "New friends, fun friends"

    // Tabs
    fun tabLive(lang: AppLanguage): String = if (lang == AppLanguage.INDONESIAN) "Siaran" else "Live"
    fun tabChat(lang: AppLanguage): String = if (lang == AppLanguage.INDONESIAN) "Obrolan" else "Chats"
    fun tabDiscover(lang: AppLanguage): String = if (lang == AppLanguage.INDONESIAN) "Temukan" else "Discover"
    fun tabMoments(lang: AppLanguage): String = if (lang == AppLanguage.INDONESIAN) "Momen" else "Moments"
    fun tabProfile(lang: AppLanguage): String = if (lang == AppLanguage.INDONESIAN) "Saya" else "Profile"

    // Splash
    fun splashSub(lang: AppLanguage): String = if (lang == AppLanguage.INDONESIAN) 
        "Teman Sekitar • Pesan Botol • Momen" 
    else 
        "Nearby Friends • Bottle Messages • Moments"

    // Login Screen
    fun welcomeBack(lang: AppLanguage): String = if (lang == AppLanguage.INDONESIAN) "Selamat Datang Kembali" else "Welcome Back"
    fun createAccount(lang: AppLanguage): String = if (lang == AppLanguage.INDONESIAN) "Buat Akun Baru" else "Create New Account"
    fun loginSubtitle(lang: AppLanguage): String = if (lang == AppLanguage.INDONESIAN) 
        "Masuk untuk mulai mengobrol & berbagi momen" 
    else 
        "Sign in to start chatting & sharing moments"
    fun signUpSubtitle(lang: AppLanguage): String = if (lang == AppLanguage.INDONESIAN) 
        "Daftar dan temukan teman baru di sekitarmu" 
    else 
        "Sign up and discover new friends around you"
    fun usernameLabel(lang: AppLanguage, isSignUp: Boolean): String = if (lang == AppLanguage.INDONESIAN) {
        if (isSignUp) "Nama Panggilan / Username" else "Username atau ID Lovy"
    } else {
        if (isSignUp) "Nickname / Username" else "Username or Lovy ID"
    }
    fun usernamePlaceholder(lang: AppLanguage): String = if (lang == AppLanguage.INDONESIAN) 
        "Contoh: Rania, Dimas, atau lovy_user" 
    else 
        "e.g. Alex, Sarah, or lovy_user"
    fun passwordLabel(lang: AppLanguage): String = if (lang == AppLanguage.INDONESIAN) "Kata Sandi" else "Password"
    fun passwordPlaceholder(lang: AppLanguage): String = if (lang == AppLanguage.INDONESIAN) "Minimal 4 karakter" else "At least 4 characters"
    fun btnLogin(lang: AppLanguage, isSignUp: Boolean): String = if (lang == AppLanguage.INDONESIAN) {
        if (isSignUp) "Daftar Sekarang" else "Masuk ke Lovy Chat"
    } else {
        if (isSignUp) "Register Now" else "Sign In to Lovy Chat"
    }
    fun btnGuest(lang: AppLanguage): String = if (lang == AppLanguage.INDONESIAN) 
        "Masuk Sebagai Tamu (Eksplorasi)" 
    else 
        "Continue as Guest (Explore)"
    fun alreadyHaveAccount(lang: AppLanguage): String = if (lang == AppLanguage.INDONESIAN) "Sudah punya akun?" else "Already have an account?"
    fun dontHaveAccount(lang: AppLanguage): String = if (lang == AppLanguage.INDONESIAN) "Belum punya akun?" else "Don't have an account?"
    fun signInAction(lang: AppLanguage): String = if (lang == AppLanguage.INDONESIAN) "Masuk" else "Sign In"
    fun signUpAction(lang: AppLanguage): String = if (lang == AppLanguage.INDONESIAN) "Daftar Baru" else "Sign Up"

    // Discover Screen
    fun discoverTitle(lang: AppLanguage): String = if (lang == AppLanguage.INDONESIAN) "Temukan" else "Discover"
    fun discoverSubtitle(lang: AppLanguage): String = if (lang == AppLanguage.INDONESIAN) "Jelajahi dan temukan koneksi baru" else "Explore and make new connections"
    fun menuNearby(lang: AppLanguage): String = if (lang == AppLanguage.INDONESIAN) "Orang di Sekitar" else "People Nearby"
    fun menuNearbySub(lang: AppLanguage): String = if (lang == AppLanguage.INDONESIAN) "Temukan teman baru di sekelilingmu" else "Find new friends around your location"
    fun menuBottle(lang: AppLanguage): String = if (lang == AppLanguage.INDONESIAN) "Pesan dalam Botol" else "Message in a Bottle"
    fun menuBottleSub(lang: AppLanguage): String = if (lang == AppLanguage.INDONESIAN) "Lempar & pancing cerita di lautan" else "Toss & fish random stories across the ocean"
    fun menuMoments(lang: AppLanguage): String = if (lang == AppLanguage.INDONESIAN) "Momen & Status" else "Moments & Stories"
    fun menuMomentsSub(lang: AppLanguage): String = if (lang == AppLanguage.INDONESIAN) "Lihat cerita terbaru dari teman" else "Check out updates from friends"

    // Profile Screen
    fun profileTitle(lang: AppLanguage): String = if (lang == AppLanguage.INDONESIAN) "Saya" else "Profile"
    fun menuSupabase(lang: AppLanguage): String = if (lang == AppLanguage.INDONESIAN) "Database Supabase" else "Supabase Database"
    fun menuSupabaseSub(lang: AppLanguage): String = if (lang == AppLanguage.INDONESIAN) "Konfigurasi backend & sinkronisasi cloud" else "Backend & cloud synchronization settings"
    fun menuAbout(lang: AppLanguage): String = if (lang == AppLanguage.INDONESIAN) "Tentang Lovy Chat" else "About Lovy Chat"
    fun menuLogout(lang: AppLanguage): String = if (lang == AppLanguage.INDONESIAN) "Keluar Akun" else "Sign Out"
    fun menuLogoutSub(lang: AppLanguage): String = if (lang == AppLanguage.INDONESIAN) "Ganti akun atau kembali ke layar masuk" else "Switch account or return to login screen"
    fun languageSetting(lang: AppLanguage): String = if (lang == AppLanguage.INDONESIAN) "Bahasa (Otomatis Geografis)" else "Language (Auto Geo-Detection)"
    fun currentGeoHint(lang: AppLanguage): String = if (lang == AppLanguage.INDONESIAN) 
        "Terdeteksi wilayah ID/MY (Bahasa Indonesia)" 
    else 
        "Detected outside ID/MY (English)"

    // Nearby Screen
    fun nearbyTitle(lang: AppLanguage): String = if (lang == AppLanguage.INDONESIAN) "Orang di Sekitar" else "People Nearby"
    fun nearbyScanning(lang: AppLanguage): String = if (lang == AppLanguage.INDONESIAN) "Memindai radar sekitarmu..." else "Scanning radar nearby..."
    fun nearbyEmpty(lang: AppLanguage): String = if (lang == AppLanguage.INDONESIAN) "Belum ada teman di radar ini" else "No users found on radar"
    fun filterAll(lang: AppLanguage): String = if (lang == AppLanguage.INDONESIAN) "Semua" else "All"
    fun filterMale(lang: AppLanguage): String = if (lang == AppLanguage.INDONESIAN) "Pria" else "Male"
    fun filterFemale(lang: AppLanguage): String = if (lang == AppLanguage.INDONESIAN) "Wanita" else "Female"

    // Bottle Screen
    fun bottleTitle(lang: AppLanguage): String = if (lang == AppLanguage.INDONESIAN) "Pesan dalam Botol" else "Message in a Bottle"
    fun btnTossBottle(lang: AppLanguage): String = if (lang == AppLanguage.INDONESIAN) "Lempar Botol" else "Toss a Bottle"
    fun btnFishBottle(lang: AppLanguage): String = if (lang == AppLanguage.INDONESIAN) "Pancing Botol" else "Fish a Bottle"
}
