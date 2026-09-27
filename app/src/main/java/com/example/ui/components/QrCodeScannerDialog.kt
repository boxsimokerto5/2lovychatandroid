package com.example.ui.components

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.WavingHand
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.model.Gender
import com.example.model.User
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.NeutralBorder
import com.example.ui.theme.NeutralDark
import com.example.ui.theme.NeutralMedium
import com.example.util.LovyNotificationHelper
import com.example.util.QrCodeDecoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors

@Composable
fun QrCodeScannerDialog(
    onDismiss: () -> Unit,
    onUserFound: (User) -> Unit,
    searchUserByCode: (String, (User?) -> Unit) -> Unit,
    isAlreadyFriend: (String) -> Boolean = { false },
    myLovyId: String = "",
    myName: String = "",
    myAvatarUrl: String? = null,
    myAvatarColorHex: String? = null,
    onOpenChatWithUser: (User) -> Unit = {},
    language: com.example.util.AppLanguage = com.example.util.AppLanguage.INDONESIAN
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
        if (!granted) {
            Toast.makeText(context, com.example.util.AppStrings.qrCameraPermissionDenied(language), Toast.LENGTH_LONG).show()
        }
    }

    var showCameraDisclosure by remember { mutableStateOf(!hasCameraPermission) }

    if (showCameraDisclosure && !hasCameraPermission) {
        PermissionDisclosureDialog(
            type = DisclosureType.CAMERA,
            language = language,
            onConfirm = {
                showCameraDisclosure = false
                permissionLauncher.launch(Manifest.permission.CAMERA)
            },
            onDismiss = {
                showCameraDisclosure = false
            }
        )
    }

    var camera by remember { mutableStateOf<Camera?>(null) }
    var isTorchOn by remember { mutableStateOf(false) }
    var isAnalyzingActive by remember { mutableStateOf(true) }
    var isDecodingGalleryImage by remember { mutableStateOf(false) }

    // Dialog profil pengguna yang berhasil dipindai
    var scannedUserResult by remember { mutableStateOf<User?>(null) }
    var notFoundCode by remember { mutableStateOf<String?>(null) }
    var isSearchingUser by remember { mutableStateOf(false) }
    var showMyQrDialog by remember { mutableStateOf(false) }

    // Photo picker dari galeri
    val galleryPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            isDecodingGalleryImage = true
            coroutineScope.launch {
                val decodedText = withContext(Dispatchers.IO) {
                    QrCodeDecoder.decodeFromUri(context, uri)
                }
                isDecodingGalleryImage = false
                if (!decodedText.isNullOrBlank()) {
                    LovyNotificationHelper.vibrateSubtle(context)
                    isAnalyzingActive = false
                    isSearchingUser = true
                    searchUserByCode(decodedText) { user ->
                        isSearchingUser = false
                        if (user != null) {
                            scannedUserResult = user
                        } else {
                            notFoundCode = decodedText
                            isAnalyzingActive = false
                        }
                    }
                } else {
                    Toast.makeText(context, com.example.util.AppStrings.qrNoCodeFoundInImage(language), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Tampilkan QR Code Saya jika tombol diklik
    if (showMyQrDialog) {
        MyQrCodeDialog(
            name = myName.ifBlank { "Pengguna Lovy" },
            lovyId = myLovyId.ifBlank { "lovy_user" },
            avatarUrl = myAvatarUrl,
            avatarColorHex = myAvatarColorHex,
            language = language,
            onDismiss = { showMyQrDialog = false }
        )
    }

    Dialog(
        onDismissRequest = {
            if (isTorchOn) {
                camera?.cameraControl?.enableTorch(false)
            }
            onDismiss()
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .testTag("dialog_qr_scanner")
        ) {
            if (hasCameraPermission) {
                // Kamera Live Viewfinder
                val cameraExecutor = remember { Executors.newSingleThreadExecutor() }

                DisposableEffect(Unit) {
                    onDispose {
                        cameraExecutor.shutdown()
                    }
                }

                AndroidView(
                    factory = { ctx ->
                        val previewView = PreviewView(ctx).apply {
                            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                        }

                        val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                        cameraProviderFuture.addListener({
                            val cameraProvider = cameraProviderFuture.get()

                            val preview = Preview.Builder().build().also {
                                it.setSurfaceProvider(previewView.surfaceProvider)
                            }

                            val imageAnalysis = ImageAnalysis.Builder()
                                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                .build()

                            imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy ->
                                if (isAnalyzingActive && !isSearchingUser && scannedUserResult == null && notFoundCode == null) {
                                    val code = QrCodeDecoder.decodeImageProxy(imageProxy)
                                    if (!code.isNullOrBlank()) {
                                        isAnalyzingActive = false
                                        coroutineScope.launch(Dispatchers.Main) {
                                            LovyNotificationHelper.vibrateSubtle(context)
                                            isSearchingUser = true
                                            searchUserByCode(code) { user ->
                                                isSearchingUser = false
                                                if (user != null) {
                                                    scannedUserResult = user
                                                } else {
                                                    notFoundCode = code
                                                    isAnalyzingActive = false
                                                }
                                            }
                                        }
                                    }
                                }
                                imageProxy.close()
                            }

                            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                            try {
                                cameraProvider.unbindAll()
                                camera = cameraProvider.bindToLifecycle(
                                    lifecycleOwner,
                                    cameraSelector,
                                    preview,
                                    imageAnalysis
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }, ContextCompat.getMainExecutor(ctx))

                        previewView
                    },
                    modifier = Modifier.fillMaxSize()
                )

                // Overlay Scanner Box + Laser Animasi
                ScannerOverlay(
                    language = language,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                // Tampilan jika izin kamera belum diberikan
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                ) {
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.15f)),
                        modifier = Modifier.fillMaxWidth(0.92f)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(24.dp)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .background(EmeraldGreen.copy(alpha = 0.2f))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Videocam,
                                    contentDescription = null,
                                    tint = EmeraldGreen,
                                    modifier = Modifier.size(32.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = com.example.util.AppStrings.qrAccessCameraRequired(language),
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = com.example.util.AppStrings.qrAccessCameraDesc(language),
                                color = Color.White.copy(alpha = 0.75f),
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center,
                                lineHeight = 19.sp
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Button(
                                onClick = { showCameraDisclosure = true },
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(46.dp)
                            ) {
                                Text(com.example.util.AppStrings.qrGrantCameraBtn(language), fontWeight = FontWeight.SemiBold, color = Color.White)
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedButton(
                                onClick = {
                                    galleryPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                },
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(46.dp)
                            ) {
                                Icon(Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(com.example.util.AppStrings.qrUploadFromGalleryBtn(language))
                            }
                        }
                    }
                }
            }

            // Top Bar: Tombol Kembali, Judul, dan Flashlight
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 14.dp)
                    .align(Alignment.TopCenter)
            ) {
                Surface(
                    onClick = {
                        if (isTorchOn) {
                            camera?.cameraControl?.enableTorch(false)
                        }
                        onDismiss()
                    },
                    shape = CircleShape,
                    color = Color.Black.copy(alpha = 0.5f),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = com.example.util.AppStrings.btnBack(language),
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = com.example.util.AppStrings.qrScannerTitle(language),
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = com.example.util.AppStrings.qrAddLovyFriendSubtitle(language),
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 11.sp
                    )
                }

                // Tombol Flashlight
                Surface(
                    onClick = {
                        val newTorch = !isTorchOn
                        isTorchOn = newTorch
                        camera?.cameraControl?.enableTorch(newTorch)
                    },
                    shape = CircleShape,
                    color = if (isTorchOn) EmeraldGreen else Color.Black.copy(alpha = 0.5f),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = if (isTorchOn) Icons.Default.FlashOn else Icons.Default.FlashOff,
                            contentDescription = com.example.util.AppStrings.qrFlashlightDesc(language),
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Bottom Actions: Tombol Unggah dari Galeri & Kode QR Saya
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 58.dp)
            ) {
                // Status loading jika sedang membaca gambar galeri atau mencari user
                AnimatedVisibility(
                    visible = isDecodingGalleryImage || isSearchingUser,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color.Black.copy(alpha = 0.75f),
                        border = BorderStroke(1.dp, EmeraldGreen.copy(alpha = 0.4f)),
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                        ) {
                            CircularProgressIndicator(
                                color = EmeraldGreen,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = if (isDecodingGalleryImage) com.example.util.AppStrings.qrProcessingGalleryImage(language) else com.example.util.AppStrings.qrSearchingUserData(language),
                                color = Color.White,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Tombol Upload dari Galeri
                    Button(
                        onClick = {
                            galleryPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("btn_upload_qr_gallery")
                    ) {
                        Icon(
                            imageVector = Icons.Default.PhotoLibrary,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(19.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = com.example.util.AppStrings.qrUploadPhotoBtn(language),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }

                    // Tombol Tampilkan QR Saya
                    Surface(
                        onClick = { showMyQrDialog = true },
                        shape = RoundedCornerShape(14.dp),
                        color = Color.White.copy(alpha = 0.18f),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)),
                        modifier = Modifier
                            .height(50.dp)
                            .testTag("btn_show_my_qr")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.QrCode,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(19.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = com.example.util.AppStrings.qrMyQrBtn(language),
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Dialog Hasil Temuan Pengguna (Scanned User Result Card)
            scannedUserResult?.let { foundUser ->
                val isSelf = (myLovyId.isNotBlank() && foundUser.id.equals(myLovyId, ignoreCase = true)) ||
                             (myName.isNotBlank() && foundUser.name.equals(myName, ignoreCase = true)) ||
                             foundUser.id.equals("me", ignoreCase = true) ||
                             foundUser.id.equals("current_user", ignoreCase = true)
                ScannedUserBottomSheet(
                    user = foundUser,
                    isAlreadyFriend = isAlreadyFriend(foundUser.id),
                    isSelf = isSelf,
                    language = language,
                    onDismiss = {
                        scannedUserResult = null
                        isAnalyzingActive = true
                    },
                    onAddFriend = {
                        if (!isSelf) {
                            onUserFound(foundUser)
                        }
                        scannedUserResult = null
                        onDismiss()
                    },
                    onStartChat = {
                        if (!isSelf) {
                            onOpenChatWithUser(foundUser)
                        }
                        scannedUserResult = null
                        onDismiss()
                    }
                )
            }

            // Dialog informasi jika Barcode / QR tidak terdaftar di database
            notFoundCode?.let { code ->
                NotFoundCodeDialog(
                    code = code,
                    language = language,
                    onDismiss = {
                        notFoundCode = null
                        isAnalyzingActive = true
                    }
                )
            }
        }
    }
}

/**
 * Overlay semi-transparan dengan jendela pindai transparan di tengah
 * dan laser scan animasi serta sudut siku khas pemindai barcode profesional.
 */
@Composable
private fun ScannerOverlay(
    modifier: Modifier = Modifier,
    language: com.example.util.AppLanguage = com.example.util.AppLanguage.INDONESIAN
) {
    val density = LocalDensity.current
    val infiniteTransition = rememberInfiniteTransition(label = "scanner_laser")
    val laserPositionRatio by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "laser_animation"
    )

    BoxWithConstraints(modifier = modifier) {
        val widthPx = constraints.maxWidth.toFloat()
        val heightPx = constraints.maxHeight.toFloat()

        // Kotak pemindai persegi di tengah layar
        val scanBoxSize = minOf(widthPx * 0.72f, with(density) { 260.dp.toPx() })
        val left = (widthPx - scanBoxSize) / 2f
        val top = (heightPx - scanBoxSize) / 2f - with(density) { 30.dp.toPx() }
        val right = left + scanBoxSize
        val bottom = top + scanBoxSize

        Canvas(modifier = Modifier.fillMaxSize()) {
            // Mask gelap di luar kotak pemindaian
            val backgroundPath = Path().apply {
                addRect(Rect(0f, 0f, widthPx, heightPx))
            }
            val cutoutPath = Path().apply {
                addRoundRect(
                    RoundRect(
                        rect = Rect(left, top, right, bottom),
                        cornerRadius = CornerRadius(20.dp.toPx(), 20.dp.toPx())
                    )
                )
            }

            drawPath(
                path = Path.combine(
                    operation = androidx.compose.ui.graphics.PathOperation.Difference,
                    path1 = backgroundPath,
                    path2 = cutoutPath
                ),
                color = Color.Black.copy(alpha = 0.65f)
            )

            // Garis pembatas halus kotak scanner
            drawRoundRect(
                color = Color.White.copy(alpha = 0.25f),
                topLeft = Offset(left, top),
                size = Size(scanBoxSize, scanBoxSize),
                cornerRadius = CornerRadius(20.dp.toPx(), 20.dp.toPx()),
                style = Stroke(width = 1.5.dp.toPx())
            )

            // Sudut siku hijau menyala (Glowing Emerald Corner Brackets)
            val cornerLength = 28.dp.toPx()
            val cornerStroke = 4.dp.toPx()
            val cornerRadius = 16.dp.toPx()
            val cornerColor = EmeraldGreen

            // Sudut Kiri-Atas
            val pathTopLeft = Path().apply {
                moveTo(left, top + cornerLength)
                lineTo(left, top + cornerRadius)
                quadraticTo(left, top, left + cornerRadius, top)
                lineTo(left + cornerLength, top)
            }
            drawPath(pathTopLeft, color = cornerColor, style = Stroke(width = cornerStroke, cap = StrokeCap.Round))

            // Sudut Kanan-Atas
            val pathTopRight = Path().apply {
                moveTo(right - cornerLength, top)
                lineTo(right - cornerRadius, top)
                quadraticTo(right, top, right, top + cornerRadius)
                lineTo(right, top + cornerLength)
            }
            drawPath(pathTopRight, color = cornerColor, style = Stroke(width = cornerStroke, cap = StrokeCap.Round))

            // Sudut Kiri-Bawah
            val pathBottomLeft = Path().apply {
                moveTo(left, bottom - cornerLength)
                lineTo(left, bottom - cornerRadius)
                quadraticTo(left, bottom, left + cornerRadius, bottom)
                lineTo(left + cornerLength, bottom)
            }
            drawPath(pathBottomLeft, color = cornerColor, style = Stroke(width = cornerStroke, cap = StrokeCap.Round))

            // Sudut Kanan-Bawah
            val pathBottomRight = Path().apply {
                moveTo(right - cornerLength, bottom)
                lineTo(right - cornerRadius, bottom)
                quadraticTo(right, bottom, right, bottom - cornerRadius)
                lineTo(right, bottom - cornerLength)
            }
            drawPath(pathBottomRight, color = cornerColor, style = Stroke(width = cornerStroke, cap = StrokeCap.Round))

            // Laser Pemindai (Laser Beam Animation)
            val laserY = top + (scanBoxSize * laserPositionRatio)
            val laserBrush = Brush.horizontalGradient(
                colors = listOf(
                    Color.Transparent,
                    EmeraldGreen.copy(alpha = 0.8f),
                    Color.White,
                    EmeraldGreen.copy(alpha = 0.8f),
                    Color.Transparent
                ),
                startX = left,
                endX = right
            )
            drawLine(
                brush = laserBrush,
                start = Offset(left + 10.dp.toPx(), laserY),
                end = Offset(right - 10.dp.toPx(), laserY),
                strokeWidth = 3.dp.toPx(),
                cap = StrokeCap.Round
            )
        }

        // Petunjuk teks di bawah kotak pemindai
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = 220.dp, start = 32.dp, end = 32.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.QrCodeScanner,
                    contentDescription = null,
                    tint = EmeraldGreen,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = com.example.util.AppStrings.qrScanSubtitlePrompt(language),
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

/**
 * Dialog hasil pemindaian kode QR pengguna dengan kartu profil elegan
 */
@Composable
private fun ScannedUserBottomSheet(
    user: User,
    isAlreadyFriend: Boolean,
    isSelf: Boolean = false,
    language: com.example.util.AppLanguage = com.example.util.AppLanguage.INDONESIAN,
    onDismiss: () -> Unit,
    onAddFriend: () -> Unit,
    onStartChat: () -> Unit
) {
    val context = LocalContext.current

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .padding(vertical = 24.dp)
                .testTag("dialog_scanned_user_result")
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // Header: Judul & Tombol Tutup
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(EmeraldGreen.copy(alpha = 0.12f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = EmeraldGreen,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = com.example.util.AppStrings.qrScannedSuccessTitle(language),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeutralDark
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = com.example.util.AppStrings.commonClose(language),
                            tint = NeutralMedium,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Avatar Pengguna
                Box(contentAlignment = Alignment.BottomEnd) {
                    LovyAvatar(
                        name = user.name,
                        avatarColorHex = user.avatarColorHex,
                        avatarUrl = user.avatarUrl,
                        size = 80.dp
                    )
                    // Status online badge
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .padding(2.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(if (user.isOnline) EmeraldGreen else Color(0xFFBDBDBD))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Nama & Badge Gender
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = user.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeutralDark
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (user.gender == Gender.FEMALE) Color(0xFFFCE4EC) else Color(0xFFE3F2FD)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Icon(
                                imageVector = if (user.gender == Gender.FEMALE) Icons.Default.Female else Icons.Default.Male,
                                contentDescription = null,
                                tint = if (user.gender == Gender.FEMALE) Color(0xFFE91E63) else Color(0xFF1976D2),
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "${user.age}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (user.gender == Gender.FEMALE) Color(0xFFE91E63) else Color(0xFF1976D2)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Lovy ID Tag dengan tombol Salin
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFF5F5F5),
                    modifier = Modifier.clickable {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                        val clip = android.content.ClipData.newPlainText(com.example.util.AppStrings.lovyIdLabel(language), user.id)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "${com.example.util.AppStrings.qrCopiedToast(language)}: ${user.id}", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${com.example.util.AppStrings.lovyIdLabel(language)}: ${user.id}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = NeutralMedium
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = com.example.util.AppStrings.qrCopyIdBtn(language),
                            tint = NeutralMedium,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }

                // Lokasi / Kota
                if (!user.city.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = EmeraldGreen,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = user.city ?: "",
                            fontSize = 12.sp,
                            color = NeutralMedium
                        )
                    }
                }

                // Bio
                if (user.bio.isNotBlank()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "\"${user.bio}\"",
                        fontSize = 12.sp,
                        color = NeutralMedium,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(22.dp))

                // Status pertemanan & Aksi
                if (isSelf) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFF5F5F5),
                        border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = EmeraldGreen,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = com.example.util.AppStrings.qrIsSelfNotice(language),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = NeutralDark
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text(com.example.util.AppStrings.commonClose(language), fontWeight = FontWeight.Bold, color = Color.White)
                    }
                } else if (isAlreadyFriend) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = EmeraldGreen.copy(alpha = 0.1f),
                        border = BorderStroke(1.dp, EmeraldGreen.copy(alpha = 0.3f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = EmeraldGreen,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = com.example.util.AppStrings.qrAlreadyFriendNotice(language),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = EmeraldGreen
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = onStartChat,
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("btn_scanned_open_chat")
                    ) {
                        Icon(Icons.Default.WavingHand, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(com.example.util.AppStrings.qrOpenChatBtn(language), fontWeight = FontWeight.Bold, color = Color.White)
                    }
                } else {
                    // Tombol Tambah Teman (Primary)
                    Button(
                        onClick = onAddFriend,
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("btn_scanned_add_friend")
                    ) {
                        Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(com.example.util.AppStrings.qrAddAsFriendBtn(language), fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Tombol Kirim Pesan Langsung
                    OutlinedButton(
                        onClick = onStartChat,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, EmeraldGreen),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = EmeraldGreen),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("btn_scanned_say_hi")
                    ) {
                        Icon(Icons.Default.WavingHand, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(com.example.util.AppStrings.qrSayHiDirectBtn(language), fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

/**
 * Dialog peringatan jika kode barcode / QR yang dipindai tidak terdaftar di database pengguna resmi Lovy Chat
 */
@Composable
private fun NotFoundCodeDialog(
    code: String,
    language: com.example.util.AppLanguage = com.example.util.AppLanguage.INDONESIAN,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .padding(vertical = 24.dp)
                .testTag("dialog_barcode_not_found")
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // Ikon bulat merah lembut
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFEBEE))
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = Color(0xFFD32F2F),
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Judul "Pengguna tidak ditemukan"
                Text(
                    text = com.example.util.AppStrings.qrUserNotFoundWithCode(language, code),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeutralDark,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Penjelasan bahwa barcode / QR tidak terdaftar di database pengguna Lovy Chat
                Text(
                    text = com.example.util.AppStrings.qrBarcodeNotRegisteredDesc(language, code),
                    fontSize = 13.sp,
                    color = NeutralMedium,
                    textAlign = TextAlign.Center,
                    lineHeight = 19.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Tombol "Pindai Lagi" (Aksi Utama)
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("btn_qr_scan_again")
                ) {
                    Icon(
                        imageVector = Icons.Default.QrCodeScanner,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = com.example.util.AppStrings.qrScanAgainBtn(language),
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Tombol Tutup Sekunder
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = com.example.util.AppStrings.commonClose(language),
                        color = NeutralMedium,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}
