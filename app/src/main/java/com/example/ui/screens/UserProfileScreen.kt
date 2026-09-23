package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Wc
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Place
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.material.icons.filled.NoPhotography
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.example.R
import coil.compose.AsyncImage
import com.example.model.UserProfile
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.LovyChatTheme
import com.example.ui.theme.NeutralBorder
import com.example.ui.theme.NeutralDark
import com.example.ui.theme.NeutralLight
import com.example.ui.theme.NeutralMedium
import com.example.ui.theme.ScreenBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    userProfile: UserProfile,
    onBack: () -> Unit,
    onSaveProfile: (UserProfile) -> Unit,
    isUploadingPhoto: Boolean = false,
    uploadProgressText: String? = null,
    onUploadPhoto: ((android.net.Uri) -> Unit)? = null,
    onClearPhoto: (() -> Unit)? = null,
    currentGpsLocation: com.example.util.UserGpsLocation? = null,
    hasLocationPermission: Boolean = false,
    isGpsEnabled: Boolean = true,
    onRefreshLocation: (() -> Unit)? = null,
    onPermissionResult: ((Boolean) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var showEditDialog by remember { mutableStateOf(false) }
    var showPhotoOptionsDialog by remember { mutableStateOf(false) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: android.net.Uri? ->
        if (uri != null) {
            onUploadPhoto?.invoke(uri)
        }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseGranted = permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
        val granted = fineGranted || coarseGranted
        onPermissionResult?.invoke(granted)
        if (granted) {
            onRefreshLocation?.invoke()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Detail Profil Pengguna",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("btn_back_user_profile")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showEditDialog = true },
                        modifier = Modifier.testTag("btn_edit_profile_top")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Profil",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = EmeraldGreen
                )
            )
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ScreenBackground)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Card with Avatar, Display Name, and Lovy ID
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("card_profile_header")
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp, horizontal = 16.dp)
                ) {
                    // Profile Picture with circular styling & online badge
                    Box(
                        contentAlignment = Alignment.BottomEnd,
                        modifier = Modifier
                            .size(104.dp)
                            .testTag("profile_picture_container")
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(104.dp)
                                .clip(CircleShape)
                                .border(3.dp, EmeraldGreen, CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        listOf(EmeraldGreen, Color(0xFF00796B))
                                    )
                                )
                                .clickable {
                                    showPhotoOptionsDialog = true
                                }
                        ) {
                            if (!userProfile.profilePicture.isNullOrBlank()) {
                                AsyncImage(
                                    model = userProfile.profilePicture,
                                    contentDescription = "Foto Profil ${userProfile.displayName}",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape)
                                )
                            } else {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_lovy_logo),
                                    contentDescription = "Logo Lovy Chat",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape)
                                )
                            }

                            if (isUploadingPhoto) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Black.copy(alpha = 0.6f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        color = Color.White,
                                        modifier = Modifier.size(36.dp),
                                        strokeWidth = 3.dp
                                    )
                                }
                            }
                        }

                        // Edit overlay circle button
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(EmeraldGreen)
                                .border(2.dp, Color.White, CircleShape)
                                .clickable {
                                    showPhotoOptionsDialog = true
                                }
                                .testTag("btn_avatar_edit")
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "Ubah Foto",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    if (isUploadingPhoto && !uploadProgressText.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = uploadProgressText,
                            fontSize = 12.sp,
                            color = EmeraldGreen,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Display Name
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = userProfile.displayName,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeutralDark,
                            modifier = Modifier.testTag("text_display_name")
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Terverifikasi",
                            tint = EmeraldGreen,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Lovy ID Tag
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = NeutralLight,
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        Text(
                            text = "ID: ${userProfile.lovyId}",
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = NeutralMedium,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bio Section Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("card_profile_bio")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.FormatQuote,
                            contentDescription = null,
                            tint = EmeraldGreen,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Tentang Saya (Bio)",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = NeutralDark
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = userProfile.bio.ifBlank { "Belum ada bio yang ditulis. Ketuk tombol edit untuk menambahkan bio." },
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        fontStyle = if (userProfile.bio.isBlank()) FontStyle.Italic else FontStyle.Normal,
                        color = if (userProfile.bio.isBlank()) NeutralMedium else NeutralDark,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("text_profile_bio")
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // User Details Information Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("card_profile_details")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Informasi Akun",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NeutralDark,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("row_profile_city_location")
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(EmeraldGreen.copy(alpha = 0.12f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = EmeraldGreen,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "Kota Domisili",
                                    fontSize = 11.5.sp,
                                    color = NeutralMedium
                                )
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = EmeraldGreen.copy(alpha = 0.12f)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(5.dp)
                                                .clip(CircleShape)
                                                .background(EmeraldGreen)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Otomatis Peta",
                                            fontSize = 9.5.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = EmeraldGreen
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = userProfile.city.ifBlank { "Mendeteksi posisi GPS..." },
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeutralDark,
                                modifier = Modifier.testTag("text_profile_city")
                            )
                            Text(
                                text = "Dilihat oleh pengguna lain di radar & obrolan",
                                fontSize = 11.sp,
                                color = NeutralMedium
                            )
                        }

                        IconButton(
                            onClick = {
                                if (!hasLocationPermission) {
                                    locationPermissionLauncher.launch(
                                        arrayOf(
                                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                                            android.Manifest.permission.ACCESS_COARSE_LOCATION
                                        )
                                    )
                                } else {
                                    onRefreshLocation?.invoke()
                                }
                            },
                            modifier = Modifier.testTag("btn_refresh_profile_city")
                        ) {
                            Icon(
                                imageVector = Icons.Default.MyLocation,
                                contentDescription = "Sinkronkan Lokasi Peta",
                                tint = EmeraldGreen,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    HorizontalDivider(color = NeutralBorder, thickness = 0.6.dp, modifier = Modifier.padding(vertical = 8.dp))

                    val genderLabel = if (userProfile.gender.equals("MALE", ignoreCase = true) || userProfile.gender.equals("Laki-laki", ignoreCase = true)) "Laki-laki" else "Perempuan"
                    ProfileDetailRow(
                        icon = Icons.Default.Wc,
                        label = "Jenis Kelamin & Usia",
                        value = "$genderLabel • ${userProfile.age} tahun"
                    )

                    HorizontalDivider(color = NeutralBorder, thickness = 0.6.dp, modifier = Modifier.padding(vertical = 8.dp))

                    ProfileDetailRow(
                        icon = Icons.Default.Email,
                        label = "Email Akun",
                        value = userProfile.email ?: "Belum terhubung"
                    )

                    HorizontalDivider(color = NeutralBorder, thickness = 0.6.dp, modifier = Modifier.padding(vertical = 8.dp))

                    ProfileDetailRow(
                        icon = Icons.Default.Badge,
                        label = "Lovy ID Unik",
                        value = userProfile.lovyId
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Primary Action: Edit Profile Button
            Button(
                onClick = { showEditDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("btn_edit_profile_action")
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Edit Detail Profil",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Photo Options Dialog (Pilih Galeri atau Kosongkan Foto)
    if (showPhotoOptionsDialog) {
        val context = LocalContext.current
        AlertDialog(
            onDismissRequest = { showPhotoOptionsDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = null,
                    tint = EmeraldGreen,
                    modifier = Modifier.size(30.dp)
                )
            },
            title = {
                Text(
                    text = "Foto Profil",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Kelola foto profil Anda. Anda dapat mengunggah foto baru atau mengosongkan foto profil untuk menggunakan logo resmi Lovy Chat sebagai profil default.",
                        fontSize = 13.sp,
                        color = NeutralMedium,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Opsi 1: Pilih dari Galeri
                    Button(
                        onClick = {
                            showPhotoOptionsDialog = false
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("btn_option_pick_gallery")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddPhotoAlternate,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Pilih Foto Baru dari Galeri",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    // Opsi 2: Kosongkan Foto Profil (Gunakan Logo Lovy Chat)
                    OutlinedButton(
                        onClick = {
                            showPhotoOptionsDialog = false
                            onClearPhoto?.invoke()
                            Toast.makeText(
                                context,
                                "Foto profil dikosongkan. Logo Lovy Chat aktif sebagai foto profil Anda.",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = EmeraldGreen),
                        border = BorderStroke(1.dp, EmeraldGreen.copy(alpha = 0.6f)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("btn_option_clear_photo")
                    ) {
                        Icon(
                            imageVector = Icons.Default.NoPhotography,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Kosongkan (Gunakan Logo Lovy Chat)",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.5.sp
                        )
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showPhotoOptionsDialog = false }) {
                    Text("Tutup", color = NeutralDark)
                }
            }
        )
    }

    // Edit Profile Dialog
    if (showEditDialog) {
        EditProfileDialog(
            currentProfile = userProfile,
            onDismiss = { showEditDialog = false },
            onPickPhotoFromGallery = {
                photoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
            isUploadingPhoto = isUploadingPhoto,
            uploadProgressText = uploadProgressText,
            currentGpsCity = currentGpsLocation?.cityName,
            onSave = { updatedProfile ->
                onSaveProfile(updatedProfile)
                showEditDialog = false
            }
        )
    }
}

@Composable
private fun ProfileDetailRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = EmeraldGreen,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 11.5.sp,
                color = NeutralMedium
            )
            Text(
                text = value,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Medium,
                color = NeutralDark
            )
        }
    }
}

@Composable
fun EditProfileDialog(
    currentProfile: UserProfile,
    onDismiss: () -> Unit,
    onPickPhotoFromGallery: () -> Unit,
    isUploadingPhoto: Boolean = false,
    uploadProgressText: String? = null,
    currentGpsCity: String? = null,
    onSave: (UserProfile) -> Unit
) {
    var displayName by remember { mutableStateOf(currentProfile.displayName) }
    var bio by remember { mutableStateOf(currentProfile.bio) }
    var profilePictureUrl by remember { mutableStateOf(currentProfile.profilePicture ?: "") }
    var city by remember { mutableStateOf(currentProfile.city) }
    var gender by remember {
        mutableStateOf(
            if (currentProfile.gender.equals("MALE", ignoreCase = true) || currentProfile.gender.equals("Laki-laki", ignoreCase = true)) "MALE" else "FEMALE"
        )
    }
    var ageText by remember { mutableStateOf(currentProfile.age.toString()) }

    androidx.compose.runtime.LaunchedEffect(currentProfile.profilePicture) {
        if (!currentProfile.profilePicture.isNullOrBlank()) {
            profilePictureUrl = currentProfile.profilePicture
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    tint = EmeraldGreen
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Edit Profil",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = NeutralDark
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Avatar Preview with edit badge
                Box(
                    contentAlignment = Alignment.BottomEnd,
                    modifier = Modifier
                        .padding(top = 4.dp, bottom = 2.dp)
                        .clickable(enabled = !isUploadingPhoto, onClick = onPickPhotoFromGallery)
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE0E0E0))
                            .border(2.dp, EmeraldGreen.copy(alpha = 0.5f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (profilePictureUrl.isNotBlank()) {
                            AsyncImage(
                                model = profilePictureUrl,
                                contentDescription = "Foto Profil",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.ic_lovy_logo),
                                contentDescription = "Logo Lovy Chat",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        if (isUploadingPhoto) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Black.copy(alpha = 0.5f)),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.size(28.dp),
                                    strokeWidth = 2.5.dp
                                )
                            }
                        }
                    }

                    // Camera Icon Badge
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(EmeraldGreen)
                            .border(2.dp, Color.White, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Ganti Foto",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                // Upload Photo from Gallery to Cloudflare R2
                Button(
                    onClick = onPickPhotoFromGallery,
                    enabled = !isUploadingPhoto,
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("btn_pick_photo_r2")
                ) {
                    if (isUploadingPhoto) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(uploadProgressText ?: "Mengunggah...", fontSize = 13.sp)
                    } else {
                        Icon(
                            imageVector = Icons.Default.AddPhotoAlternate,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Pilih Foto Galeri (Cloudflare R2)", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }

                if (profilePictureUrl.isNotBlank()) {
                    OutlinedButton(
                        onClick = { profilePictureUrl = "" },
                        enabled = !isUploadingPhoto,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD32F2F)),
                        border = BorderStroke(1.dp, Color(0xFFFFCDD2)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("btn_clear_photo_edit_dialog")
                    ) {
                        Icon(
                            imageVector = Icons.Default.NoPhotography,
                            contentDescription = null,
                            tint = Color(0xFFD32F2F),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Kosongkan Foto Profil (Gunakan Logo Lovy Chat)",
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFD32F2F)
                        )
                    }
                } else {
                    Surface(
                        color = Color(0xFFE8F5E9),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, Color(0xFFC8E6C9)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = EmeraldGreen,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Logo Resmi Lovy Chat aktif sebagai foto profil Anda",
                                fontSize = 12.sp,
                                color = EmeraldGreen,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = displayName,
                    onValueChange = { displayName = it },
                    label = { Text("Nama Tampilan (Display Name)") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_edit_display_name")
                )

                // Gender Selection (Jenis Kelamin)
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Jenis Kelamin",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NeutralDark
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        val isMale = gender == "MALE"
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isMale) EmeraldGreen.copy(alpha = 0.15f) else Color(0xFFF5F5F5),
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isMale) EmeraldGreen else Color(0xFFE0E0E0)),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { gender = "MALE" }
                                .testTag("btn_select_gender_male")
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Male,
                                    contentDescription = "Laki-laki",
                                    tint = if (isMale) EmeraldGreen else NeutralMedium,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Laki-laki",
                                    fontSize = 13.sp,
                                    fontWeight = if (isMale) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isMale) EmeraldGreen else NeutralDark
                                )
                            }
                        }

                        val isFemale = gender == "FEMALE"
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isFemale) EmeraldGreen.copy(alpha = 0.15f) else Color(0xFFF5F5F5),
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isFemale) EmeraldGreen else Color(0xFFE0E0E0)),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { gender = "FEMALE" }
                                .testTag("btn_select_gender_female")
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Female,
                                    contentDescription = "Perempuan",
                                    tint = if (isFemale) EmeraldGreen else NeutralMedium,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Perempuan",
                                    fontSize = 13.sp,
                                    fontWeight = if (isFemale) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isFemale) EmeraldGreen else NeutralDark
                                )
                            }
                        }
                    }
                }

                // Age Input Field (Usia)
                OutlinedTextField(
                    value = ageText,
                    onValueChange = { input ->
                        if (input.all { it.isDigit() } && input.length <= 3) {
                            ageText = input
                        }
                    },
                    label = { Text("Usia (Tahun)") },
                    singleLine = true,
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = null,
                            tint = EmeraldGreen
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_edit_age")
                )

                OutlinedTextField(
                    value = bio,
                    onValueChange = { bio = it },
                    label = { Text("Bio / Status Singkat") },
                    maxLines = 3,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_edit_bio")
                )

                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text("Kota / Lokasi Domisili") },
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = EmeraldGreen
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_edit_city")
                )

                if (!currentGpsCity.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = EmeraldGreen.copy(alpha = 0.08f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldGreen.copy(alpha = 0.3f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                city = currentGpsCity
                            }
                            .testTag("btn_use_gps_city")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MyLocation,
                                contentDescription = null,
                                tint = EmeraldGreen,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Gunakan Lokasi GPS Terdeteksi",
                                    fontSize = 11.sp,
                                    color = NeutralMedium
                                )
                                Text(
                                    text = currentGpsCity,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldGreen
                                )
                            }
                            Text(
                                text = "Terapkan",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = EmeraldGreen
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Lokasi domisili Anda terdeteksi otomatis dari GPS peta hingga tingkat kecamatan & kota (misal Kec. Depok, Sleman atau Kec. Gondomanan, Yogyakarta).",
                    fontSize = 11.sp,
                    lineHeight = 15.sp,
                    color = NeutralMedium
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val parsedAge = ageText.trim().toIntOrNull()?.coerceIn(12, 120) ?: currentProfile.age
                    val updated = currentProfile.copy(
                        displayName = displayName.trim().ifBlank { currentProfile.displayName },
                        bio = bio.trim(),
                        profilePicture = profilePictureUrl.trim().ifBlank { null },
                        city = city.trim().ifBlank { currentProfile.city },
                        gender = if (gender.equals("MALE", ignoreCase = true)) "MALE" else "FEMALE",
                        age = parsedAge,
                        updatedAt = System.currentTimeMillis()
                    )
                    onSave(updated)
                },
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                modifier = Modifier.testTag("btn_save_edit_profile")
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Simpan")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("btn_cancel_edit_profile")
            ) {
                Text("Batal", color = NeutralMedium)
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun UserProfileScreenPreview() {
    LovyChatTheme {
        UserProfileScreen(
            userProfile = UserProfile(
                displayName = "Aisyah Putri",
                bio = "Suka fotografi langit senja dan menjelajahi tempat baru bersama Lovy Chat ✨",
                lovyId = "lovy_778129",
                city = "Bandung, Jawa Barat",
                profilePicture = null
            ),
            onBack = {},
            onSaveProfile = {}
        )
    }
}
