package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardVoice
import android.speech.RecognizerIntent
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.model.MomentItem
import com.example.model.MomentComment
import com.example.model.User
import com.example.model.Gender
import com.example.model.UserProfile
import com.example.ui.components.UserProfileBottomSheet
import com.example.util.AppLanguage
import com.example.util.AppStrings
import com.example.ui.components.IronSourceBannerView
import com.example.ui.components.LevelPlayNativeAdCard
import com.example.ui.components.LovyAvatar
import com.example.ui.components.ReportDialog
import com.example.ui.components.ReportType
import com.example.ui.theme.EmeraldGreen
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.MyLocation
import com.example.ui.theme.NeutralBorder
import com.example.ui.theme.NeutralDark
import com.example.ui.theme.NeutralMedium
import com.example.ui.theme.ScreenBackground

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MomentsScreen(
    moments: List<MomentItem>,
    myMomentIds: Set<String> = emptySet(),
    momentComments: Map<String, List<MomentComment>> = emptyMap(),
    currentUserId: String = "",
    currentUserName: String = "",
    currentUserAvatarUrl: String? = null,
    currentGpsLocation: com.example.util.UserGpsLocation? = null,
    nearbyUsers: List<User> = emptyList(),
    friends: List<User> = emptyList(),
    currentUserProfile: UserProfile = UserProfile(),
    blockedUserIds: Set<String> = emptySet(),
    language: AppLanguage = AppLanguage.INDONESIAN,
    initialOnlyMyMoments: Boolean = false,
    onBack: () -> Unit,
    onToggleLike: (String) -> Unit,
    onAddComment: ((momentId: String, text: String) -> Unit)? = null,
    onSayHi: ((User) -> Unit)? = null,
    onBlockUser: ((User) -> Unit)? = null,
    onPostMoment: (String) -> Unit,
    onPostMomentWithDetails: ((content: String, imageUrl: String?, locationTag: String?) -> Unit)? = null,
    onPostMomentWithPhotoUri: ((content: String, uri: android.net.Uri?, locationTag: String?, onResult: (Boolean) -> Unit) -> Unit)? = null,
    onDeleteMoment: ((String) -> Unit)? = null,
    onReportMoment: ((momentId: String, authorName: String, reason: String, notes: String) -> Unit)? = null,
    onRefresh: (() -> Unit)? = null,
    isRefreshing: Boolean = false,
    isUploadingPhoto: Boolean = false,
    uploadProgressText: String? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var filterOnlyMyMoments by remember(initialOnlyMyMoments) { mutableStateOf(initialOnlyMyMoments) }
    var showPostDialog by remember { mutableStateOf(false) }
    var reportingMoment by remember { mutableStateOf<MomentItem?>(null) }
    var selectedUserForProfile by remember { mutableStateOf<User?>(null) }
    var postText by remember { mutableStateOf("") }
    
    // Otomatis deteksi nama kota dari GPS map
    val resolvedGpsCity = remember(currentGpsLocation) {
        currentGpsLocation?.cityName?.ifBlank { null }
            ?: if (currentGpsLocation != null) com.example.util.AndroidGpsTracker.getCityName(context, currentGpsLocation.latitude, currentGpsLocation.longitude) else null
    }

    var postLocation by remember { 
        mutableStateOf(resolvedGpsCity ?: "Surabaya") 
    }

    // Setiap kali dialog "Bagikan Momen Baru" dibuka, isi nama kota sesuai GPS map
    LaunchedEffect(showPostDialog, resolvedGpsCity) {
        if (showPostDialog) {
            val detected = resolvedGpsCity 
                ?: com.example.util.AndroidGpsTracker.getLastKnownLocation(context)?.let {
                    it.cityName.ifBlank {
                        com.example.util.AndroidGpsTracker.getCityName(context, it.latitude, it.longitude)
                    }
                }
            if (!detected.isNullOrBlank()) {
                postLocation = detected
            }
        }
    }

    var selectedPhotoUri by remember { mutableStateOf<android.net.Uri?>(null) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: android.net.Uri? ->
        if (uri != null) {
            selectedPhotoUri = uri
        }
    }
    
    // State for viewing photos fullscreen
    var fullscreenPhotoUrl by remember { mutableStateOf<String?>(null) }
    var previewMomentForLightbox by remember { mutableStateOf<MomentItem?>(null) }

    // State for viewing & adding comments
    var activeMomentIdForComments by remember { mutableStateOf<String?>(null) }
    var commentInputText by remember { mutableStateOf("") }
    val commentSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var activeSpeechTarget by remember { mutableStateOf<String?>(null) }

    val speechToTextLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val spokenMatches = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val spokenText = spokenMatches?.firstOrNull()?.trim()
            if (!spokenText.isNullOrEmpty()) {
                if (activeSpeechTarget == "post") {
                    val current = postText.trimEnd()
                    postText = if (current.isEmpty()) spokenText else "$current $spokenText"
                } else if (activeSpeechTarget == "comment") {
                    val current = commentInputText.trimEnd()
                    commentInputText = if (current.isEmpty()) spokenText else "$current $spokenText"
                }
            }
        }
    }

    val speechPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                val langTag = when (language) {
                    AppLanguage.INDONESIAN -> "id-ID"
                    AppLanguage.ENGLISH -> "en-US"
                    AppLanguage.CHINESE -> "zh-CN"
                    AppLanguage.JAPANESE -> "ja-JP"
                    AppLanguage.KOREAN -> "ko-KR"
                    AppLanguage.ARABIC -> "ar-SA"
                    AppLanguage.SPANISH -> "es-ES"
                    AppLanguage.FRENCH -> "fr-FR"
                    AppLanguage.GERMAN -> "de-DE"
                    AppLanguage.RUSSIAN -> "ru-RU"
                    AppLanguage.PORTUGUESE -> "pt-BR"
                    else -> "id-ID"
                }
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, langTag)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, langTag)
                putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, false)
                putExtra(RecognizerIntent.EXTRA_PROMPT, AppStrings.speechToTextPrompt(language))
            }
            try {
                speechToTextLauncher.launch(intent)
            } catch (e: Exception) {
                Toast.makeText(context, AppStrings.speechNotAvailable(language), Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, AppStrings.micPermissionRequiredForSpeech(language), Toast.LENGTH_SHORT).show()
        }
    }

    val startMomentsSpeechToText: (String) -> Unit = { target ->
        activeSpeechTarget = target
        val hasPermission = androidx.core.content.ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.RECORD_AUDIO
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        if (hasPermission) {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                val langTag = when (language) {
                    AppLanguage.INDONESIAN -> "id-ID"
                    AppLanguage.ENGLISH -> "en-US"
                    AppLanguage.CHINESE -> "zh-CN"
                    AppLanguage.JAPANESE -> "ja-JP"
                    AppLanguage.KOREAN -> "ko-KR"
                    AppLanguage.ARABIC -> "ar-SA"
                    AppLanguage.SPANISH -> "es-ES"
                    AppLanguage.FRENCH -> "fr-FR"
                    AppLanguage.GERMAN -> "de-DE"
                    AppLanguage.RUSSIAN -> "ru-RU"
                    AppLanguage.PORTUGUESE -> "pt-BR"
                    else -> "id-ID"
                }
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, langTag)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, langTag)
                putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, false)
                putExtra(RecognizerIntent.EXTRA_PROMPT, AppStrings.speechToTextPrompt(language))
            }
            try {
                speechToTextLauncher.launch(intent)
            } catch (e: Exception) {
                Toast.makeText(context, AppStrings.speechNotAvailable(language), Toast.LENGTH_SHORT).show()
            }
        } else {
            speechPermissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
        }
    }

    // Otomatis segarkan dan sinkronisasikan momen dari server saat membuka layar Momen
    LaunchedEffect(Unit) {
        onRefresh?.invoke()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = if (filterOnlyMyMoments) AppStrings.menuMyMoments(language) else AppStrings.momentsTitle(language),
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = if (filterOnlyMyMoments) AppStrings.menuMyMomentsSub(language) else AppStrings.momentsSubtitle(language),
                            fontSize = 11.5.sp,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("moments_btn_back")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = AppStrings.btnBack(language),
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    if (onRefresh != null) {
                        val infiniteTransition = rememberInfiniteTransition(label = "moment_refresh_anim")
                        val rotation by infiniteTransition.animateFloat(
                            initialValue = 0f,
                            targetValue = 360f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(700, easing = LinearEasing),
                                repeatMode = RepeatMode.Restart
                            ),
                            label = "moment_refresh_rot"
                        )
                        IconButton(
                            onClick = {
                                Toast.makeText(context, AppStrings.momentsRefreshToast(language), Toast.LENGTH_SHORT).show()
                                onRefresh()
                            },
                            enabled = !isRefreshing,
                            modifier = Modifier.testTag("moments_btn_refresh")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = AppStrings.btnRefreshGps(language),
                                tint = Color.White,
                                modifier = if (isRefreshing) Modifier.rotate(rotation) else Modifier
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = EmeraldGreen)
            )
        },
        bottomBar = {
            IronSourceBannerView(applyNavigationBarsPadding = true)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showPostDialog = true },
                containerColor = Color(0xFFFB8C00),
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.testTag("fab_post_moment")
            ) {
                Icon(imageVector = Icons.Default.CameraAlt, contentDescription = AppStrings.momentsCreateTitle(language))
            }
        },
        containerColor = ScreenBackground,
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        val displayMoments = remember(moments, myMomentIds, currentUserId, currentUserName, filterOnlyMyMoments) {
            val base = moments.filterNot { 
                it.authorName.contains("test", ignoreCase = true) ||
                it.authorName.contains("tester", ignoreCase = true) ||
                it.authorName.contains("dummy", ignoreCase = true) ||
                it.authorId.contains("test", ignoreCase = true) ||
                it.authorId.startsWith("test_")
            }
            if (filterOnlyMyMoments) {
                base.filter { moment ->
                    (currentUserId.isNotBlank() && (moment.authorId == currentUserId || moment.authorId.equals(currentUserId, ignoreCase = true))) ||
                    myMomentIds.contains(moment.id) ||
                    (currentUserName.isNotBlank() && moment.authorName.equals(currentUserName, ignoreCase = true))
                }
            } else {
                base
            }
        }
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Tab Switcher: [Semua Momen (Publik)] vs [Momen Saya (Pribadi)]
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Tab 1: Semua Momen (Komunitas / Temuan)
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (!filterOnlyMyMoments) EmeraldGreen else Color.Transparent,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { filterOnlyMyMoments = false }
                                .testTag("tab_all_moments")
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Explore,
                                    contentDescription = null,
                                    tint = if (!filterOnlyMyMoments) Color.White else NeutralMedium,
                                    modifier = Modifier.size(17.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (language == AppLanguage.INDONESIAN) "Semua Momen" else "All Moments",
                                    fontSize = 13.5.sp,
                                    fontWeight = if (!filterOnlyMyMoments) FontWeight.Bold else FontWeight.Medium,
                                    color = if (!filterOnlyMyMoments) Color.White else NeutralDark
                                )
                            }
                        }

                        // Tab 2: Momen Saya (Pribadi)
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (filterOnlyMyMoments) EmeraldGreen else Color.Transparent,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { filterOnlyMyMoments = true }
                                .testTag("tab_my_moments")
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = if (filterOnlyMyMoments) Color.White else NeutralMedium,
                                    modifier = Modifier.size(17.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = AppStrings.menuMyMoments(language),
                                    fontSize = 13.5.sp,
                                    fontWeight = if (filterOnlyMyMoments) FontWeight.Bold else FontWeight.Medium,
                                    color = if (filterOnlyMyMoments) Color.White else NeutralDark
                                )
                            }
                        }
                    }
                }
            }

            // Header information strip
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (filterOnlyMyMoments) Color(0xFFFFF3E0) else Color(0xFFE8F5E9)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 11.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = if (filterOnlyMyMoments) "📸" else "✨", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (filterOnlyMyMoments) {
                                if (language == AppLanguage.INDONESIAN) {
                                    if (displayMoments.isEmpty()) "Anda belum memiliki momen yang dibagikan"
                                    else "Menampilkan ${displayMoments.size} momen yang Anda bagikan"
                                } else {
                                    if (displayMoments.isEmpty()) "You haven't shared any moments yet"
                                    else "Viewing ${displayMoments.size} moments shared by you"
                                }
                            } else {
                                AppStrings.momentsViewingCount(language, displayMoments.size)
                            },
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (filterOnlyMyMoments) Color(0xFFE65100) else Color(0xFF2E7D32)
                        )
                    }
                }
            }

            if (displayMoments.isEmpty()) {
                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 48.dp, horizontal = 24.dp)
                    ) {
                        Icon(
                            imageVector = if (filterOnlyMyMoments) Icons.Default.Person else Icons.Default.CameraAlt,
                            contentDescription = null,
                            tint = EmeraldGreen.copy(alpha = 0.6f),
                            modifier = Modifier.size(56.dp)
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = if (filterOnlyMyMoments) {
                                if (language == AppLanguage.INDONESIAN) "Belum Ada Momen Anda" else "No Moments Shared by You"
                            } else {
                                AppStrings.momentsEmptyTitle(language)
                            },
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeutralDark
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (filterOnlyMyMoments) {
                                if (language == AppLanguage.INDONESIAN) "Ketuk tombol kamera oranye di kanan bawah untuk membagikan foto atau cerita pertama Anda!" else "Tap the orange camera button below to share your first photo or story!"
                            } else {
                                AppStrings.momentsEmptyDesc(language)
                            },
                            fontSize = 13.sp,
                            color = NeutralMedium,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(18.dp))
                        Button(
                            onClick = { showPostDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(AppStrings.momentsShareBtn(language), fontSize = 13.sp)
                        }
                    }
                }
            } else {
                displayMoments.forEachIndexed { index, item ->
                    val cleanMyId = currentUserId.trim()
                    val cleanAuthorId = item.authorId.trim()
                    val isMyMoment = when {
                        // User belum login / ID kosong tidak memiliki hak hapus
                        cleanMyId.isBlank() -> false
                        // 1. Pemilik utama: authorId cocok persis dengan currentUserId
                        cleanAuthorId.isNotBlank() && !cleanAuthorId.equals("me", ignoreCase = true) -> {
                            cleanAuthorId.equals(cleanMyId, ignoreCase = true)
                        }
                        // 2. Cadangan: momen baru diinput di sesi ini pada perangkat ini
                        else -> {
                            item.id in myMomentIds && (cleanAuthorId.isBlank() || cleanAuthorId.equals("me", ignoreCase = true) || cleanAuthorId.equals(cleanMyId, ignoreCase = true))
                        }
                    }

                    // Pastikan jumlah komentar yang ditampilkan sinkron langsung dengan komentar yang tersimpan
                    val commentsForThisItem = momentComments[item.id]
                    val accurateCommentsCount = maxOf(item.commentsCount, commentsForThisItem?.size ?: 0)
                    val displayItem = if (item.commentsCount != accurateCommentsCount) {
                        item.copy(commentsCount = accurateCommentsCount)
                    } else item

                    item(key = item.id) {
                    MomentCard(
                        item = displayItem,
                        isMyMoment = isMyMoment,
                        currentUserAvatarUrl = currentUserAvatarUrl,
                        language = language,
                        onAuthorClick = {
                            selectedUserForProfile = resolveUserForAuthor(
                                authorId = item.authorId,
                                authorName = item.authorName,
                                authorAvatarHex = item.authorAvatarHex,
                                authorAvatarUrl = item.authorAvatarUrl,
                                locationTag = item.locationTag,
                                nearbyUsers = nearbyUsers,
                                friends = friends,
                                currentUserId = currentUserId,
                                currentUserName = currentUserName,
                                currentUserProfile = currentUserProfile
                            )
                        },
                        onToggleLike = { onToggleLike(item.id) },
                        onPhotoClick = { url -> fullscreenPhotoUrl = url },
                        onMomentPhotoClick = { moment -> previewMomentForLightbox = moment },
                        onCommentClick = { activeMomentIdForComments = item.id },
                        onShareClick = null,
                        onDeleteClick = if (isMyMoment && onDeleteMoment != null) {
                            { onDeleteMoment(item.id) }
                        } else null,
                        onReportClick = if (!isMyMoment && onReportMoment != null) {
                            { reportingMoment = item }
                        } else null
                    )
                }

                // Sisipkan Iklan Native langsung setelah postingan ke-1 (index == 0) dan berkala setiap 4 postingan
                if (index == 0 || (index > 0 && (index + 1) % 4 == 0)) {
                    item(key = "native_ad_moments_$index") {
                        LevelPlayNativeAdCard(
                            testTag = "native_ad_moments_$index"
                        )
                    }
                }
            }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    if (reportingMoment != null) {
        val target = reportingMoment!!
        ReportDialog(
            targetName = "${AppStrings.reportTypeMoment(language)} (${target.authorName})",
            reportType = ReportType.MOMENT,
            language = language,
            onDismiss = { reportingMoment = null },
            onSubmitReport = { reason, notes, _ ->
                onReportMoment?.invoke(target.id, target.authorName, reason, notes)
                reportingMoment = null
            }
        )
    }

    // Dialog posting moment dengan opsi foto & lokasi
    if (showPostDialog) {
        AlertDialog(
            onDismissRequest = { if (!isUploadingPhoto) showPostDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = null,
                        tint = EmeraldGreen,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = AppStrings.momentsCreateTitle(language),
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = NeutralDark
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Header Pembuat Momen (Avatar & Nama)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 2.dp)
                    ) {
                        LovyAvatar(
                            name = currentUserName.ifBlank { "Saya" },
                            avatarColorHex = 0xFF00A86B,
                            avatarUrl = currentUserAvatarUrl,
                            size = 44.dp,
                            fontSize = 17.sp
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = currentUserName.ifBlank { "Saya" },
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = NeutralDark
                            )
                            Text(
                                text = AppStrings.momentsTimelineNotice(language),
                                fontSize = 12.sp,
                                color = NeutralMedium
                            )
                        }
                    }

                    OutlinedTextField(
                        value = postText,
                        onValueChange = { postText = it },
                        placeholder = { Text(AppStrings.momentsInputPlaceholder(language), color = NeutralMedium) },
                        trailingIcon = {
                            IconButton(
                                onClick = { startMomentsSpeechToText("post") },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardVoice,
                                    contentDescription = AppStrings.speechToTextTooltip(language),
                                    tint = EmeraldGreen,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        },
                        minLines = 3,
                        maxLines = 5,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = NeutralDark,
                            unfocusedTextColor = NeutralDark,
                            cursorColor = EmeraldGreen,
                            focusedBorderColor = EmeraldGreen,
                            unfocusedBorderColor = NeutralBorder
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = postLocation,
                        onValueChange = { postLocation = it },
                        label = { Text(AppStrings.momentsLocationLabel(language)) },
                        leadingIcon = {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = EmeraldGreen, modifier = Modifier.size(18.dp))
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    val lastLoc = com.example.util.AndroidGpsTracker.getLastKnownLocation(context)
                                    val city = if (lastLoc != null) {
                                        lastLoc.cityName.ifBlank {
                                             com.example.util.AndroidGpsTracker.getCityName(context, lastLoc.latitude, lastLoc.longitude)
                                        }
                                    } else {
                                        resolvedGpsCity ?: "Surabaya"
                                    }
                                    if (city.isNotBlank()) {
                                        postLocation = city
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MyLocation,
                                    contentDescription = AppStrings.btnRefreshGps(language),
                                    tint = EmeraldGreen,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = NeutralDark,
                            unfocusedTextColor = NeutralDark,
                            cursorColor = EmeraldGreen,
                            focusedBorderColor = EmeraldGreen,
                            unfocusedBorderColor = NeutralBorder
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Tombol Pilih Foto dari Galeri
                    Button(
                        onClick = {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("btn_pick_moment_photo")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddPhotoAlternate,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(AppStrings.momentsPickPhoto(language), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }

                    // Pratinjau Foto Galeri yang dipilih
                    if (selectedPhotoUri != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFEEEEEE))
                        ) {
                            AsyncImage(
                                model = selectedPhotoUri,
                                contentDescription = AppStrings.momentsPickPhoto(language),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(6.dp)
                                    .clip(CircleShape)
                                    .background(Color.Black.copy(alpha = 0.55f))
                                    .clickable {
                                        selectedPhotoUri = null
                                    }
                                    .padding(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = AppStrings.chatsBtnDelete(language),
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }

                    if (isUploadingPhoto) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = EmeraldGreen,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = uploadProgressText ?: AppStrings.momentsUploading(language),
                                fontSize = 12.sp,
                                color = EmeraldGreen,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (postText.isNotBlank() || selectedPhotoUri != null) {
                            val contentToPost = postText.ifBlank { "📷 Foto momen" }
                            if (selectedPhotoUri != null && onPostMomentWithPhotoUri != null) {
                                onPostMomentWithPhotoUri(contentToPost, selectedPhotoUri, postLocation) { success ->
                                    if (success) {
                                        postText = ""
                                        selectedPhotoUri = null
                                        showPostDialog = false
                                    }
                                }
                            } else if (onPostMomentWithDetails != null) {
                                onPostMomentWithDetails(contentToPost, null, postLocation)
                                postText = ""
                                selectedPhotoUri = null
                                showPostDialog = false
                            } else {
                                onPostMoment(contentToPost)
                                postText = ""
                                selectedPhotoUri = null
                                showPostDialog = false
                            }
                        }
                    },
                    enabled = !isUploadingPhoto && (postText.isNotBlank() || selectedPhotoUri != null),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    if (isUploadingPhoto) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(uploadProgressText ?: "Mengunggah...", fontWeight = FontWeight.Bold)
                    } else {
                        Text(AppStrings.momentsShareBtn(language), fontWeight = FontWeight.Bold)
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showPostDialog = false },
                    enabled = !isUploadingPhoto
                ) {
                    Text(AppStrings.btnCancel(language), color = NeutralMedium)
                }
            }
        )
    }

    // Fullscreen Photo Modal dengan Carousel Geser Kiri-Kanan (Swipe antar-foto teman)
    previewMomentForLightbox?.let { moment ->
        val authorMoments = remember(moments, moment) {
            val list = moments.filter {
                it.authorName.equals(moment.authorName, ignoreCase = true) &&
                        !it.isDeleted &&
                        !it.imageUrl.isNullOrBlank()
            }
            if (list.any { it.id == moment.id }) list else (list + moment).distinctBy { it.id }
        }
        val commentsCounts = remember(momentComments) {
            momentComments.mapValues { it.value.size }
        }

        com.example.ui.components.PartnerPhotoPreviewDialog(
            language = language,
            moments = authorMoments,
            initialMomentId = moment.id,
            partnerAvatarHex = moment.authorAvatarHex,
            momentCommentsCount = commentsCounts,
            onDismiss = { previewMomentForLightbox = null },
            onToggleLike = { momentId ->
                onToggleLike(momentId)
            },
            onCommentClick = { targetMoment ->
                previewMomentForLightbox = null
                activeMomentIdForComments = targetMoment.id
            }
        )
    }

    // Fullscreen Photo Modal dengan Zoom 2 Jari
    fullscreenPhotoUrl?.let { photoUrl ->
        com.example.ui.components.ZoomablePhotoViewerDialog(
            photoUrl = photoUrl,
            title = "Foto Momen",
            onDismiss = { fullscreenPhotoUrl = null }
        )
    }

    // Modal Bottom Sheet untuk Melihat & Mengirim Komentar Momen
    val activeMoment = moments.find { it.id == activeMomentIdForComments }
    if (activeMoment != null) {
        val comments = momentComments[activeMoment.id] ?: emptyList()
        ModalBottomSheet(
            onDismissRequest = {
                activeMomentIdForComments = null
                commentInputText = ""
            },
            sheetState = commentSheetState,
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            dragHandle = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .width(36.dp)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color(0xFFE0E0E0))
                    )
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .imePadding()
            ) {
                // Header Sheet Komentar
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = AppStrings.momentsCommentsTitle(language),
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeutralDark
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = CircleShape,
                                color = EmeraldGreen.copy(alpha = 0.12f)
                            ) {
                                Text(
                                    text = "${comments.size}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldGreen,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "${AppStrings.reportTypeMoment(language)} (${activeMoment.authorName})",
                            fontSize = 12.sp,
                            color = NeutralMedium
                        )
                    }

                    IconButton(
                        onClick = {
                            activeMomentIdForComments = null
                            commentInputText = ""
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = AppStrings.btnCancel(language),
                            tint = NeutralDark
                        )
                    }
                }

                HorizontalDivider(color = NeutralBorder.copy(alpha = 0.6f), thickness = 0.6.dp)

                // List Komentar
                if (comments.isEmpty()) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .padding(16.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFFF1F8E9),
                                modifier = Modifier.size(52.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.ChatBubbleOutline,
                                        contentDescription = null,
                                        tint = EmeraldGreen,
                                        modifier = Modifier.size(26.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = AppStrings.momentsNoCommentsTitle(language),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = NeutralDark
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = AppStrings.momentsNoCommentsDesc(language),
                                fontSize = 12.sp,
                                color = NeutralMedium,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 120.dp, max = 380.dp)
                    ) {
                        items(comments, key = { it.id }) { comment ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.Top
                            ) {
                                LovyAvatar(
                                    name = comment.authorName,
                                    avatarColorHex = comment.authorAvatarHex,
                                    avatarUrl = comment.authorAvatarUrl,
                                    size = 38.dp,
                                    fontSize = 15.sp,
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .clickable {
                                            selectedUserForProfile = resolveUserForAuthor(
                                                authorId = comment.authorId,
                                                authorName = comment.authorName,
                                                authorAvatarHex = comment.authorAvatarHex,
                                                authorAvatarUrl = comment.authorAvatarUrl,
                                                locationTag = null,
                                                nearbyUsers = nearbyUsers,
                                                friends = friends,
                                                currentUserId = currentUserId,
                                                currentUserName = currentUserName,
                                                currentUserProfile = currentUserProfile
                                            )
                                        }
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Surface(
                                        shape = RoundedCornerShape(
                                            topStart = 4.dp,
                                            topEnd = 14.dp,
                                            bottomStart = 14.dp,
                                            bottomEnd = 14.dp
                                        ),
                                        color = Color(0xFFF5F6F8),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Text(
                                                    text = comment.authorName,
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = NeutralDark,
                                                    modifier = Modifier.clickable {
                                                        selectedUserForProfile = resolveUserForAuthor(
                                                            authorId = comment.authorId,
                                                            authorName = comment.authorName,
                                                            authorAvatarHex = comment.authorAvatarHex,
                                                            authorAvatarUrl = comment.authorAvatarUrl,
                                                            locationTag = null,
                                                            nearbyUsers = nearbyUsers,
                                                            friends = friends,
                                                            currentUserId = currentUserId,
                                                            currentUserName = currentUserName,
                                                            currentUserProfile = currentUserProfile
                                                        )
                                                    }
                                                )
                                                Text(
                                                    text = comment.timeAgo,
                                                    fontSize = 10.5.sp,
                                                    color = NeutralMedium
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(3.dp))
                                            Text(
                                                text = comment.text,
                                                fontSize = 13.sp,
                                                color = NeutralDark,
                                                lineHeight = 18.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                HorizontalDivider(color = NeutralBorder.copy(alpha = 0.5f), thickness = 0.6.dp)

                // Input Bar untuk Kirim Komentar
                Surface(
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        OutlinedTextField(
                            value = commentInputText,
                            onValueChange = { commentInputText = it },
                            placeholder = {
                                Text(
                                    AppStrings.momentsWriteComment(language),
                                    fontSize = 13.sp,
                                    color = NeutralMedium
                                )
                            },
                            trailingIcon = {
                                IconButton(
                                    onClick = { startMomentsSpeechToText("comment") },
                                    modifier = Modifier.size(34.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardVoice,
                                        contentDescription = AppStrings.speechToTextTooltip(language),
                                        tint = EmeraldGreen,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            },
                            singleLine = false,
                            maxLines = 3,
                            shape = RoundedCornerShape(22.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = NeutralDark,
                                unfocusedTextColor = NeutralDark,
                                cursorColor = EmeraldGreen,
                                focusedBorderColor = EmeraldGreen,
                                unfocusedBorderColor = NeutralBorder.copy(alpha = 0.8f),
                                focusedContainerColor = Color(0xFFFAFAFA),
                                unfocusedContainerColor = Color(0xFFFAFAFA)
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("input_moment_comment")
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = {
                                val textToSend = commentInputText.trim()
                                if (textToSend.isNotBlank()) {
                                    onAddComment?.invoke(activeMoment.id, textToSend)
                                    commentInputText = ""
                                    Toast.makeText(context, AppStrings.momentsCommentSent(language), Toast.LENGTH_SHORT).show()
                                }
                            },
                            enabled = commentInputText.isNotBlank(),
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(
                                    if (commentInputText.isNotBlank()) EmeraldGreen else Color(0xFFE0E0E0)
                                )
                                .testTag("btn_send_moment_comment")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = AppStrings.momentsWriteComment(language),
                                tint = if (commentInputText.isNotBlank()) Color.White else NeutralMedium,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    // Modal Bottom Sheet untuk Melihat Profil & Momen Teman saat Nama/Avatar Diklik
    selectedUserForProfile?.let { user ->
        UserProfileBottomSheet(
            user = user,
            existingMoments = moments,
            isBlocked = blockedUserIds.contains(user.id),
            momentComments = momentComments,
            onAddComment = onAddComment,
            onToggleLikeMoment = onToggleLike,
            language = language,
            onDismiss = { selectedUserForProfile = null },
            onSayHi = { targetUser ->
                selectedUserForProfile = null
                onSayHi?.invoke(targetUser)
            },
            onBlockUser = {
                selectedUserForProfile = null
                onBlockUser?.invoke(user)
            },
            onReportUser = { reason, notes, _ ->
                selectedUserForProfile = null
                onReportMoment?.invoke(user.id, user.name, reason, notes)
            }
        )
    }
}

private fun resolveUserForAuthor(
    authorId: String,
    authorName: String,
    authorAvatarHex: Long,
    authorAvatarUrl: String?,
    locationTag: String?,
    nearbyUsers: List<User>,
    friends: List<User>,
    currentUserId: String,
    currentUserName: String,
    currentUserProfile: UserProfile
): User {
    val cleanAuthorId = authorId.trim()
    val cleanMyId = currentUserId.trim()
    val isMe = (cleanMyId.isNotBlank() && cleanAuthorId.equals(cleanMyId, ignoreCase = true)) ||
            cleanAuthorId.equals("me", ignoreCase = true) ||
            (currentUserName.isNotBlank() && authorName.equals(currentUserName, ignoreCase = true))

    if (isMe) {
        val userGender = if (currentUserProfile.gender.equals("MALE", ignoreCase = true)) Gender.MALE else Gender.FEMALE
        return User(
            id = cleanMyId.ifBlank { "current_user" },
            name = currentUserName.ifBlank { currentUserProfile.displayName.ifBlank { "Saya" } },
            gender = userGender,
            age = currentUserProfile.age,
            distanceMeters = 0,
            bio = currentUserProfile.bio.ifBlank { "Profil saya di Lovy Chat ✨" },
            avatarColorHex = 0xFF00A86B,
            isOnline = true,
            city = currentUserProfile.city.ifBlank { locationTag ?: "Indonesia" },
            avatarUrl = currentUserProfile.profilePicture ?: authorAvatarUrl
        )
    }

    // 1. Cari dari daftar teman
    val friend = friends.find { f ->
        (cleanAuthorId.isNotBlank() && f.id.equals(cleanAuthorId, ignoreCase = true)) ||
                f.name.equals(authorName, ignoreCase = true)
    }
    if (friend != null) return friend

    // 2. Cari dari daftar pengguna terdekat
    val nearby = nearbyUsers.find { u ->
        (cleanAuthorId.isNotBlank() && u.id.equals(cleanAuthorId, ignoreCase = true)) ||
                u.name.equals(authorName, ignoreCase = true)
    }
    if (nearby != null) return nearby

    // 3. Fallback konsisten jika akun belum tercatat di teman/sekitar
    val seed = kotlin.math.abs(authorName.hashCode())
    val age = 19 + (seed % 12)
    val distance = 100 + (seed % 900)
    return User(
        id = cleanAuthorId.ifBlank { "user_$seed" },
        name = authorName.ifBlank { "Pengguna Lovy" },
        gender = Gender.FEMALE,
        age = age,
        distanceMeters = distance,
        bio = "Halo! Senang bisa berbagi momen dan cerita seru di Lovy Chat ✨",
        avatarColorHex = authorAvatarHex,
        isOnline = true,
        city = locationTag ?: "Indonesia",
        avatarUrl = authorAvatarUrl
    )
}

@Composable
fun MomentCard(
    item: MomentItem,
    isMyMoment: Boolean = false,
    currentUserAvatarUrl: String? = null,
    language: AppLanguage = AppLanguage.INDONESIAN,
    onAuthorClick: (() -> Unit)? = null,
    onToggleLike: () -> Unit,
    onPhotoClick: (String) -> Unit,
    onMomentPhotoClick: ((MomentItem) -> Unit)? = null,
    onShareClick: (() -> Unit)? = null,
    onCommentClick: () -> Unit = {},
    onDeleteClick: (() -> Unit)? = null,
    onReportClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var showDeleteConfirm by remember { mutableStateOf(false) }

    val heartColor by animateColorAsState(
        targetValue = if (item.isLiked) Color(0xFFE53935) else NeutralMedium,
        label = "heartColor"
    )

    if (showDeleteConfirm && onDeleteClick != null) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = {
                Text(
                    text = AppStrings.momentsDeleteTitle(language),
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = NeutralDark
                )
            },
            text = {
                Text(
                    text = AppStrings.momentsDeleteDesc(language),
                    fontSize = 14.sp,
                    color = NeutralMedium
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteConfirm = false
                        onDeleteClick()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))
                ) {
                    Text(AppStrings.chatsBtnDelete(language), color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text(AppStrings.btnCancel(language), color = NeutralMedium)
                }
            }
        )
    }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("moment_card_${item.id}")
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Header Row: Avatar, Name, Location, Time, Delete button (if my moment)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                val displayAvatarUrl = item.authorAvatarUrl?.takeIf { it.isNotBlank() }
                    ?: if (isMyMoment) currentUserAvatarUrl?.takeIf { it.isNotBlank() } else null

                val hasAvatarPhoto = !displayAvatarUrl.isNullOrBlank()
                LovyAvatar(
                    name = item.authorName,
                    avatarColorHex = item.authorAvatarHex,
                    avatarUrl = displayAvatarUrl,
                    size = 40.dp,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable {
                            if (onAuthorClick != null) {
                                onAuthorClick()
                            } else if (hasAvatarPhoto) {
                                onPhotoClick(displayAvatarUrl)
                            }
                        }
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(enabled = onAuthorClick != null) {
                            onAuthorClick?.invoke()
                        }
                ) {
                    Text(
                        text = item.authorName.ifBlank { com.example.util.AppStrings.defaultUserName(language) },
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeutralDark,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(
                            text = item.timeAgo,
                            fontSize = 11.sp,
                            color = NeutralMedium
                        )

                        if (!item.locationTag.isNullOrBlank()) {
                            Text(text = "•", fontSize = 10.sp, color = NeutralMedium)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = EmeraldGreen,
                                    modifier = Modifier.size(10.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = item.locationTag,
                                    fontSize = 10.5.sp,
                                    color = EmeraldGreen,
                                    fontWeight = FontWeight.Medium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }

                if (isMyMoment && onDeleteClick != null) {
                    Surface(
                        onClick = { showDeleteConfirm = true },
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFFFEBEE),
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("btn_delete_moment_${item.id}")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = AppStrings.chatsBtnDelete(language),
                                tint = Color(0xFFD32F2F),
                                modifier = Modifier.size(17.dp)
                            )
                        }
                    }
                } else if (!isMyMoment && onReportClick != null) {
                    Surface(
                        onClick = onReportClick,
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFF8FAFC),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("btn_report_moment_${item.id}")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Flag,
                                contentDescription = AppStrings.sheetReport(language),
                                tint = Color(0xFF94A3B8),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Moment Content Text
            Text(
                text = item.content,
                fontSize = 13.5.sp,
                color = NeutralDark,
                lineHeight = 19.sp
            )

            // Large Photo Display
            if (!item.imageUrl.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(210.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF1F3F4))
                        .clickable {
                            if (onMomentPhotoClick != null) onMomentPhotoClick(item)
                            else onPhotoClick(item.imageUrl ?: "")
                        }
                        .testTag("moment_image_${item.id}")
                ) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(com.example.util.ImageCompressor.resolveImageModel(item.imageUrl))
                            .crossfade(true)
                            .build(),
                        contentDescription = "Foto Momen ${item.authorName}",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                        loading = {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = EmeraldGreen,
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 2.dp
                                )
                            }
                        },
                        error = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color(0xFFF5F5F5)),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        imageVector = Icons.Default.BrokenImage,
                                        contentDescription = null,
                                        tint = NeutralMedium,
                                        modifier = Modifier.size(32.dp)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = com.example.util.AppStrings.imageLoadFailed(language),
                                        fontSize = 11.sp,
                                        color = NeutralMedium
                                    )
                                }
                            }
                        }
                    )

                    // Subtle zoom / fullscreen pill at bottom right
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(10.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.Black.copy(alpha = 0.55f))
                            .padding(horizontal = 9.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Fullscreen,
                                contentDescription = AppStrings.commonZoomPhoto(language),
                                tint = Color.White,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = AppStrings.commonZoomPhoto(language),
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            HorizontalDivider(color = NeutralBorder.copy(alpha = 0.6f), thickness = 0.6.dp)

            Spacer(modifier = Modifier.height(8.dp))

            // Action Row: Like, Comment, Share
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Like Button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(onClick = onToggleLike)
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                        .testTag("btn_like_${item.id}")
                ) {
                    Icon(
                        imageVector = if (item.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = AppStrings.sheetLike(language),
                        tint = heartColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${item.likesCount}",
                        fontSize = 13.sp,
                        color = heartColor,
                        fontWeight = if (item.isLiked) FontWeight.Bold else FontWeight.Medium
                    )
                }

                // Comment Button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(onClick = onCommentClick)
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                        .testTag("btn_comment_${item.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.ChatBubbleOutline,
                        contentDescription = AppStrings.sheetComments(language),
                        tint = NeutralMedium,
                        modifier = Modifier.size(19.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${item.commentsCount}",
                        fontSize = 13.sp,
                        color = NeutralMedium,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Share Button (hanya tampil jika onShareClick aktif)
                if (onShareClick != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable(onClick = onShareClick)
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = AppStrings.sheetShare(language),
                            tint = NeutralMedium,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = AppStrings.sheetShare(language),
                            fontSize = 12.5.sp,
                            color = NeutralMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
