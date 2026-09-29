package com.example.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import android.content.ClipboardManager
import android.content.ClipData
import android.content.Context
import android.widget.Toast
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import com.example.data.pocketbase.PocketBaseClient
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import com.example.ui.components.ReportDialog
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Pause
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.util.VoiceRecorder
import com.example.util.VoicePlayer
import com.example.ui.components.ReportType
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.SentimentSatisfiedAlt
import androidx.compose.material.icons.filled.KeyboardVoice
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.speech.RecognizerIntent
import androidx.core.content.ContextCompat
import androidx.compose.material.icons.filled.Wc
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt
import kotlinx.coroutines.launch
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.foundation.text.ClickableText
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.model.ChatMessage
import com.example.model.Gender
import com.example.model.MomentItem
import com.example.ui.components.LovyAvatar
import com.example.util.AppLanguage
import com.example.util.AppStrings
import com.example.ui.theme.ChatBubbleOther
import com.example.ui.theme.ChatBubbleSelf
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.NeutralBorder
import com.example.ui.theme.NeutralDark
import com.example.ui.theme.NeutralLight
import com.example.ui.theme.NeutralMedium
import com.example.ui.theme.ScreenBackground
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDetailScreen(
    conversationId: String,
    partnerName: String,
    partnerAvatarHex: Long,
    messages: List<ChatMessage>,
    onBack: () -> Unit,
    onSendMessage: (String) -> Unit,
    onSendMessageWithReply: ((String, ChatMessage?) -> Unit)? = null,
    partnerAvatarUrl: String? = null,
    partnerBio: String = "Senang berteman dan mencari cerita seru di Lovy Chat ✨",
    partnerCity: String = "Jakarta Selatan",
    partnerDistance: String = "500m",
    partnerGender: Gender? = Gender.FEMALE,
    partnerAge: Int = 22,
    partnerMoments: List<MomentItem> = emptyList(),
    isPartnerBlocked: Boolean = false,
    onBlockPartner: (() -> Unit)? = null,
    onUnblockPartner: (() -> Unit)? = null,
    onReportPartner: ((reason: String, notes: String, alsoBlock: Boolean) -> Unit)? = null,
    onToggleLikeMoment: ((String) -> Unit)? = null,
    onPartnerProfileClick: (() -> Unit)? = null,
    onDeleteMessageForSender: ((String) -> Unit)? = null,
    onDeleteMessageForMe: ((String) -> Unit)? = null,
    onDeleteMessageForEveryone: ((String) -> Unit)? = null,
    onReactToMessage: ((messageId: String, emoji: String?) -> Unit)? = null,
    onSendPhotoMessage: ((android.net.Uri, String) -> Unit)? = null,
    onSendPhotoMessageWithReply: ((android.net.Uri, String, ChatMessage?) -> Unit)? = null,
    onSendVoiceNote: ((java.io.File, Int, ChatMessage?) -> Unit)? = null,
    isUploadingPhoto: Boolean = false,
    uploadProgressText: String? = null,
    onPollMessages: (() -> Unit)? = null,
    isPartnerTyping: Boolean = false,
    onUserTyping: ((Boolean) -> Unit)? = null,
    isFriend: Boolean = true,
    onAddFriend: (() -> Unit)? = null,
    momentComments: Map<String, List<com.example.model.MomentComment>> = emptyMap(),
    onAddComment: ((momentId: String, text: String) -> Unit)? = null,
    language: AppLanguage = AppLanguage.INDONESIAN,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }
    var inputText by remember { mutableStateOf("") }
    var replyingToMessage by remember { mutableStateOf<ChatMessage?>(null) }
    var lastSendClickTime by remember { mutableStateOf(0L) }
    val voiceRecorder = remember { VoiceRecorder(context) }
    val isRecordingVoice by voiceRecorder.isRecording.collectAsState()
    val recordingDurationSec by voiceRecorder.recordingDurationSeconds.collectAsState()
    val recordingAmp by voiceRecorder.amplitudeFlow.collectAsState()

    val audioPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            voiceRecorder.startRecording()
        } else {
            Toast.makeText(context, "Izin mikrofon diperlukan untuk merekam pesan suara", Toast.LENGTH_SHORT).show()
        }
    }

    val speechToTextLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val spokenMatches = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val spokenText = spokenMatches?.firstOrNull()?.trim()
            if (!spokenText.isNullOrEmpty()) {
                val current = inputText.trimEnd()
                inputText = if (current.isEmpty()) spokenText else "$current $spokenText"
                onUserTyping?.invoke(true)
            }
        }
    }

    val speechAudioPermissionLauncher = rememberLauncherForActivityResult(
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

    val onTriggerSpeechToText: () -> Unit = {
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
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
            speechAudioPermissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            VoicePlayer.stop()
            if (voiceRecorder.isRecording.value) {
                voiceRecorder.cancelRecording()
            }
        }
    }
    var showPartnerProfileSheet by remember { mutableStateOf(false) }
    var activeMomentForComments by remember { mutableStateOf<MomentItem?>(null) }
    var messageToDelete by remember { mutableStateOf<ChatMessage?>(null) }
    var messageForActionMenu by remember { mutableStateOf<ChatMessage?>(null) }
    var showExtendedEmojiPicker by remember { mutableStateOf(false) }
    var targetMessageForExtendedEmoji by remember { mutableStateOf<ChatMessage?>(null) }
    var showInputEmojiPicker by remember { mutableStateOf(false) }
    var pendingPhotoUri by remember { mutableStateOf<android.net.Uri?>(null) }
    var viewingPhotoUrl by remember { mutableStateOf<String?>(null) }

    // Otomatis fokuskan keyboard ke kolom input saat pesan digeser untuk dibalas (Swipe to Reply)
    LaunchedEffect(replyingToMessage) {
        if (replyingToMessage != null) {
            try {
                focusRequester.requestFocus()
            } catch (_: Throwable) {}
        }
    }

    // Debounce status mengetik pengguna saat mengetik di kolom input pesan
    LaunchedEffect(inputText) {
        if (inputText.isNotBlank()) {
            onUserTyping?.invoke(true)
            kotlinx.coroutines.delay(2500)
            onUserTyping?.invoke(false)
        } else {
            onUserTyping?.invoke(false)
        }
    }

    androidx.compose.runtime.DisposableEffect(Unit) {
        onDispose {
            onUserTyping?.invoke(false)
        }
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: android.net.Uri? ->
        if (uri != null) {
            pendingPhotoUri = uri
        }
    }

    val listState = rememberLazyListState()

    // Lacak waktu aktivitas dan status lifecycle layar untuk Smart Adaptive Polling (Fase 1)
    var lastActivityTime by remember { mutableStateOf(System.currentTimeMillis()) }
    var isScreenVisible by remember { mutableStateOf(true) }

    val lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
    androidx.compose.runtime.DisposableEffect(lifecycleOwner) {
        val observer = androidx.lifecycle.LifecycleEventObserver { _, event ->
            if (event == androidx.lifecycle.Lifecycle.Event.ON_RESUME) {
                isScreenVisible = true
                lastActivityTime = System.currentTimeMillis()
                onPollMessages?.invoke()
            } else if (event == androidx.lifecycle.Lifecycle.Event.ON_PAUSE || event == androidx.lifecycle.Lifecycle.Event.ON_STOP) {
                isScreenVisible = false
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Scroll to bottom when new messages arrive dan perbarui waktu aktivitas
    LaunchedEffect(messages.size) {
        lastActivityTime = System.currentTimeMillis()
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    // Scroll to bottom saat lawan bicara sedang mengetik agar animasi indikator terlihat
    LaunchedEffect(isPartnerTyping) {
        if (isPartnerTyping && messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size)
        }
    }

    // Smart Adaptive Polling (Menghemat kuota koneksi & CPU database gratisan untuk 50k users)
    LaunchedEffect(conversationId) {
        while (true) {
            val idleSeconds = (System.currentTimeMillis() - lastActivityTime) / 1000
            val pollDelayMs = when {
                idleSeconds < 25 -> 4000L   // Obrolan aktif: polling setiap 4 detik
                idleSeconds < 90 -> 8000L   // Percakapan melambat: 8 detik
                else -> 15000L              // Percakapan diam/ditinggal: 15 detik
            }
            kotlinx.coroutines.delay(pollDelayMs)
            if (isScreenVisible) {
                onPollMessages?.invoke()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                showPartnerProfileSheet = true
                                onPartnerProfileClick?.invoke()
                            }
                            .padding(horizontal = 4.dp, vertical = 4.dp)
                            .testTag("chat_header_partner_profile")
                    ) {
                        val hasPartnerPhoto = !partnerAvatarUrl.isNullOrBlank()
                        LovyAvatar(
                            name = partnerName,
                            avatarColorHex = partnerAvatarHex,
                            avatarUrl = partnerAvatarUrl,
                            size = 38.dp,
                            fontSize = 16.sp,
                            isOnline = true,
                            modifier = if (hasPartnerPhoto) {
                                Modifier
                                    .clip(CircleShape)
                                    .clickable { viewingPhotoUrl = partnerAvatarUrl }
                            } else Modifier
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = partnerName,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                if (isPartnerBlocked) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(Color(0xFFD32F2F))
                                            .padding(horizontal = 5.dp, vertical = 1.dp)
                                    ) {
                                        Text(
                                            text = AppStrings.tagBlocked(language),
                                            color = Color.White,
                                            fontSize = 9.5.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = AppStrings.chatProfileDetail(language),
                                    tint = Color.White.copy(alpha = 0.85f),
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                            if (isPartnerBlocked) {
                                Text(
                                    text = AppStrings.chatBlockedSubtitle(language),
                                    fontSize = 11.sp,
                                    color = Color(0xFFFFCDD2)
                                )
                            } else if (isPartnerTyping) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.testTag("chat_detail_typing_status")
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(7.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF69F0AE))
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text(
                                        text = AppStrings.chatsTyping(language),
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFFE8F5E9)
                                    )
                                }
                            } else {
                                Text(
                                    text = AppStrings.chatOnlineSubtitle(language),
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.88f)
                                )
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("chat_detail_btn_back")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = AppStrings.btnBack(language),
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            showPartnerProfileSheet = true
                            onPartnerProfileClick?.invoke()
                        },
                        modifier = Modifier.testTag("chat_header_btn_view_profile")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = AppStrings.chatProfileDetail(language),
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = EmeraldGreen)
            )
        },
        containerColor = ScreenBackground,
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Banner jika lawan bicara belum ditambahkan ke Kontak Saya
            if (!isFriend && onAddFriend != null) {
                Surface(
                    color = Color(0xFFE8F5E9),
                    border = BorderStroke(1.dp, Color(0xFFC8E6C9)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                        .testTag("banner_add_contact_unadded_user")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Icon(
                                imageVector = Icons.Default.PersonAdd,
                                contentDescription = null,
                                tint = EmeraldGreen,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = AppStrings.chatNotFriendNotice(language),
                                fontSize = 12.sp,
                                color = NeutralDark
                            )
                        }
                        Button(
                            onClick = onAddFriend,
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text(text = AppStrings.chatAddFriendBtn(language), fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Messages List
            val displayedMessages = remember(messages) {
                deduplicateMessagesForUi(messages)
            }
            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(
                    displayedMessages.filterNot { 
                        (it.deletedForSender && it.isFromMe) || 
                        (it.deletedForReceiver && !it.isFromMe) ||
                        it.text.contains("Salam kenal dari fitur Teman Sekitar")
                    }, 
                    key = { it.id }
                ) { msg ->
                    SwipeableChatBubble(
                        language = language,
                        message = msg,
                        onReply = { targetMsg ->
                            replyingToMessage = targetMsg
                        },
                        onClick = {
                            // Klik biasa pada bubble
                        },
                        onLongClick = {
                            // Tahan lama (long press) untuk opsi aksi WhatsApp style & reaksi emoji
                            messageForActionMenu = msg
                        },
                        onReactionClick = {
                            onReactToMessage?.invoke(msg.id, msg.reaction)
                        },
                        onPhotoClick = { url ->
                            viewingPhotoUrl = url
                        },
                        onQuotedMessageClick = { quotedId ->
                            val targetIndex = messages.indexOfFirst { it.id == quotedId }
                            if (targetIndex >= 0) {
                                coroutineScope.launch {
                                    listState.animateScrollToItem(targetIndex)
                                }
                            }
                        }
                    )
                }

                if (isPartnerTyping) {
                    item(key = "typing_indicator_bubble") {
                        TypingIndicatorBubble(
                            partnerName = partnerName,
                            partnerAvatarHex = partnerAvatarHex,
                            partnerAvatarUrl = partnerAvatarUrl
                        )
                    }
                }
            }

            // Dialog Hapus Pesan (WhatsApp Style: Hapus untuk Saya vs Hapus untuk Semua Orang)
            if (messageToDelete != null) {
                val msg = messageToDelete!!
                AlertDialog(
                    onDismissRequest = { messageToDelete = null },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = null,
                            tint = Color(0xFFE53935)
                        )
                    },
                    title = {
                        Text(
                            text = AppStrings.chatDeleteDialogTitle(language),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = NeutralDark
                        )
                    },
                    text = {
                        Column {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = NeutralLight.copy(alpha = 0.5f),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Text(
                                    text = if (msg.text.isNotBlank()) "\"${msg.text}\"" else "📷 ${AppStrings.commonPhoto(language)}",
                                    fontSize = 13.5.sp,
                                    color = NeutralDark,
                                    fontWeight = FontWeight.Medium,
                                    maxLines = 3,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = if (msg.isFromMe) {
                                    AppStrings.chatDeleteDialogDescEveryone(language)
                                } else {
                                    AppStrings.chatDeleteDialogDescMe(language)
                                },
                                fontSize = 12.5.sp,
                                color = NeutralMedium,
                                lineHeight = 17.sp
                            )
                        }
                    },
                    confirmButton = {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Opsi 1: Hapus untuk Semua Orang (Hanya jika pesan dikirim oleh kita sendiri)
                            if (msg.isFromMe) {
                                Button(
                                    onClick = {
                                        onDeleteMessageForEveryone?.invoke(msg.id)
                                            ?: onDeleteMessageForSender?.invoke(msg.id)
                                        messageToDelete = null
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Group,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(17.dp)
                                        )
                                        Text(
                                            text = AppStrings.chatDeleteForEveryone(language),
                                            color = Color.White,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 13.5.sp
                                        )
                                    }
                                }
                            }

                            // Opsi 2: Hapus untuk Saya (Bisa untuk pesan kita maupun pesan teman)
                            OutlinedButton(
                                onClick = {
                                    onDeleteMessageForMe?.invoke(msg.id)
                                        ?: onDeleteMessageForSender?.invoke(msg.id)
                                    messageToDelete = null
                                },
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, Color(0xFFD32F2F)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        tint = Color(0xFFD32F2F),
                                        modifier = Modifier.size(17.dp)
                                    )
                                    Text(
                                        text = AppStrings.chatDeleteForMe(language),
                                        color = Color(0xFFD32F2F),
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 13.5.sp
                                    )
                                }
                            }

                            // Opsi 3: Batal
                            TextButton(
                                onClick = { messageToDelete = null },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = AppStrings.btnCancel(language),
                                    color = NeutralMedium,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    },
                    dismissButton = null
                )
            }

            // Bottom Input & Quick Greeting Section (Compact frame attached together)
            Surface(
                color = Color.White,
                shadowElevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    // Quick Greeting Chips (compact & scrollable horizontally, directly above input)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(bottom = 4.dp)
                    ) {
                        val suggestions = AppStrings.chatQuickSuggestions(language)
                        suggestions.forEach { suggestion ->
                            SuggestionChip(
                                onClick = {
                                    val replyTarget = replyingToMessage
                                    replyingToMessage = null
                                    if (onSendMessageWithReply != null) {
                                        onSendMessageWithReply(suggestion, replyTarget)
                                    } else {
                                        onSendMessage(suggestion)
                                    }
                                },
                                label = { Text(suggestion, fontSize = 11.5.sp, color = NeutralDark) },
                                colors = SuggestionChipDefaults.suggestionChipColors(
                                    containerColor = ScreenBackground,
                                    labelColor = NeutralDark
                                ),
                                border = BorderStroke(0.8.dp, NeutralBorder.copy(alpha = 0.7f)),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier.height(30.dp)
                            )
                        }
                    }

                    // Bottom Chat Area
                    if (isPartnerBlocked) {
                        // Blocked User Info Banner instead of input
                        Surface(
                            color = Color(0xFFFFEBEE),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, Color(0xFFFFCDD2)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .testTag("banner_chat_blocked")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                    Icon(
                                        imageVector = Icons.Default.Block,
                                        contentDescription = null,
                                        tint = Color(0xFFD32F2F),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = AppStrings.chatBlockedNotice(language),
                                        fontSize = 12.sp,
                                        color = Color(0xFFC62828)
                                    )
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                TextButton(
                                    onClick = { onUnblockPartner?.invoke() },
                                    modifier = Modifier.testTag("btn_unblock_partner_chat")
                                ) {
                                    Text(
                                        text = AppStrings.chatUnblockBtn(language),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFD32F2F)
                                    )
                                }
                            }
                        }
                    } else {
                        // Replying To Preview Bar (WhatsApp Style)
                        AnimatedVisibility(
                            visible = replyingToMessage != null,
                            enter = expandVertically() + fadeIn(),
                            exit = shrinkVertically() + fadeOut()
                        ) {
                            if (replyingToMessage != null) {
                                val target = replyingToMessage!!
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color(0xFFF1F8E9),
                                    border = BorderStroke(1.dp, Color(0xFFC8E6C9)),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 6.dp)
                                        .testTag("chat_replying_to_preview_bar")
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                                    ) {
                                        // Left vertical accent stripe
                                        Box(
                                            modifier = Modifier
                                                .width(4.dp)
                                                .height(36.dp)
                                                .clip(RoundedCornerShape(2.dp))
                                                .background(EmeraldGreen)
                                        )

                                        Spacer(modifier = Modifier.width(8.dp))

                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.Reply,
                                            contentDescription = null,
                                            tint = EmeraldGreen,
                                            modifier = Modifier.size(18.dp)
                                        )

                                        Spacer(modifier = Modifier.width(6.dp))

                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = AppStrings.chatReplyingTo(language, if (target.isFromMe) AppStrings.chatYou(language) else partnerName),
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = EmeraldGreen
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = if (!target.imageUrl.isNullOrBlank() && target.text.isBlank()) "📷 ${AppStrings.commonPhoto(language)}" else target.text,
                                                fontSize = 11.5.sp,
                                                color = NeutralDark.copy(alpha = 0.85f),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }

                                        if (!target.imageUrl.isNullOrBlank()) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Box(
                                                modifier = Modifier
                                                    .size(36.dp)
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(Color.LightGray)
                                            ) {
                                                AsyncImage(
                                                    model = target.imageUrl,
                                                    contentDescription = "Thumbnail pesan dibalas",
                                                    contentScale = ContentScale.Crop,
                                                    modifier = Modifier.fillMaxSize()
                                                )
                                            }
                                        }

                                        IconButton(
                                            onClick = { replyingToMessage = null },
                                            modifier = Modifier
                                                .size(28.dp)
                                                .testTag("btn_cancel_reply")
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = AppStrings.chatCancelReply(language),
                                                tint = NeutralMedium,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Pending Photo Preview Bar
                        if (pendingPhotoUri != null) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFF1F8E9),
                                border = BorderStroke(1.dp, Color(0xFFC8E6C9)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 6.dp)
                                    .testTag("chat_photo_preview_bar")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(54.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color.LightGray)
                                        ) {
                                        AsyncImage(
                                            model = pendingPhotoUri,
                                            contentDescription = "Foto yang akan dikirim",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = AppStrings.chatPhotoReady(language),
                                            fontSize = 12.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = EmeraldGreen
                                        )
                                        Text(
                                            text = if (isUploadingPhoto) (uploadProgressText ?: AppStrings.momentsUploading(language)) else AppStrings.chatPhotoInputHint(language),
                                            fontSize = 11.sp,
                                            color = NeutralMedium
                                        )
                                    }
                                    if (isUploadingPhoto) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(20.dp),
                                            color = EmeraldGreen,
                                            strokeWidth = 2.dp
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                    } else {
                                        IconButton(
                                            onClick = { pendingPhotoUri = null },
                                            modifier = Modifier.size(28.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = AppStrings.btnCancel(language),
                                                tint = NeutralMedium,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Bottom Input Bar
                        if (isRecordingVoice) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp, horizontal = 4.dp)
                            ) {
                                val infiniteTransition = rememberInfiniteTransition(label = "pulse")
                                val pulseAlpha by infiniteTransition.animateFloat(
                                    initialValue = 0.3f,
                                    targetValue = 1.0f,
                                    animationSpec = infiniteRepeatable(
                                        animation = tween(600, easing = FastOutSlowInEasing),
                                        repeatMode = RepeatMode.Reverse
                                    ),
                                    label = "pulseAlpha"
                                )
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFE53935).copy(alpha = pulseAlpha))
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                val min = recordingDurationSec / 60
                                val sec = recordingDurationSec % 60
                                Text(
                                    text = String.format(Locale.getDefault(), "%02d:%02d", min, sec),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = Color(0xFFE53935)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Merekam suara...",
                                    fontSize = 13.5.sp,
                                    color = NeutralMedium,
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(
                                    onClick = {
                                        voiceRecorder.cancelRecording()
                                        Toast.makeText(context, "Rekaman dibatalkan", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.size(42.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DeleteOutline,
                                        contentDescription = "Batal",
                                        tint = Color(0xFFE53935),
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                                IconButton(
                                    onClick = {
                                        val recordResult = voiceRecorder.stopRecording()
                                        if (recordResult != null) {
                                            val (file, duration) = recordResult
                                            val replyTarget = replyingToMessage
                                            replyingToMessage = null
                                            onSendVoiceNote?.invoke(file, duration, replyTarget)
                                        } else {
                                            Toast.makeText(context, "Rekaman suara terlalu singkat", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    colors = IconButtonDefaults.iconButtonColors(
                                        containerColor = EmeraldGreen,
                                        contentColor = Color.White
                                    ),
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.Send,
                                        contentDescription = "Kirim",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 2.dp, bottom = 2.dp)
                            ) {
                            // Attach Photo Button (Gallery -> Cloudflare R2)
                            IconButton(
                                onClick = {
                                    photoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                },
                                enabled = !isUploadingPhoto,
                                modifier = Modifier
                                    .size(42.dp)
                                    .testTag("chat_btn_attach_photo")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AddPhotoAlternate,
                                    contentDescription = AppStrings.chatSendPhotoTooltip(language),
                                    tint = EmeraldGreen,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            // Emoji Picker Toggle Button
                            IconButton(
                                onClick = {
                                    showInputEmojiPicker = !showInputEmojiPicker
                                },
                                modifier = Modifier
                                    .size(42.dp)
                                    .testTag("chat_btn_emoji_toggle")
                            ) {
                                Icon(
                                    imageVector = if (showInputEmojiPicker) Icons.Default.Keyboard else Icons.Default.SentimentSatisfiedAlt,
                                    contentDescription = "Emoji",
                                    tint = if (showInputEmojiPicker) EmeraldGreen else NeutralMedium,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(4.dp))

                            OutlinedTextField(
                                value = inputText,
                                onValueChange = { inputText = it },
                                textStyle = TextStyle(
                                    color = Color(0xFF111827),
                                    fontSize = 15.sp
                                ),
                                placeholder = {
                                    Text(
                                        text = AppStrings.chatTypePlaceholder(language, pendingPhotoUri != null),
                                        fontSize = 14.sp,
                                        color = Color(0xFF94A3B8)
                                    )
                                },
                                trailingIcon = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(end = 4.dp)
                                    ) {
                                        if (inputText.isNotEmpty()) {
                                            IconButton(
                                                onClick = {
                                                    inputText = ""
                                                    onUserTyping?.invoke(false)
                                                },
                                                modifier = Modifier.size(28.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Close,
                                                    contentDescription = "Hapus teks",
                                                    tint = NeutralMedium,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(2.dp))
                                        }
                                        IconButton(
                                            onClick = onTriggerSpeechToText,
                                            modifier = Modifier
                                                .size(34.dp)
                                                .testTag("chat_btn_speech_to_text")
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.KeyboardVoice,
                                                contentDescription = AppStrings.speechToTextTooltip(language),
                                                tint = EmeraldGreen,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                },
                                maxLines = 4,
                                shape = RoundedCornerShape(24.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color(0xFF111827),
                                    unfocusedTextColor = Color(0xFF111827),
                                    focusedPlaceholderColor = Color(0xFF94A3B8),
                                    unfocusedPlaceholderColor = Color(0xFF94A3B8),
                                    cursorColor = EmeraldGreen,
                                    focusedBorderColor = EmeraldGreen,
                                    unfocusedBorderColor = Color(0xFFD1D5DB),
                                    focusedContainerColor = Color(0xFFF9FAFB),
                                    unfocusedContainerColor = Color(0xFFF9FAFB)
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .focusRequester(focusRequester)
                                    .testTag("chat_input_field")
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            if (inputText.isBlank() && pendingPhotoUri == null) {
                                IconButton(
                                    onClick = {
                                        val hasPermission = androidx.core.content.ContextCompat.checkSelfPermission(
                                            context,
                                            android.Manifest.permission.RECORD_AUDIO
                                        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
                                        if (hasPermission) {
                                            voiceRecorder.startRecording()
                                        } else {
                                            audioPermissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
                                        }
                                    },
                                    colors = IconButtonDefaults.iconButtonColors(
                                        containerColor = EmeraldGreen,
                                        contentColor = Color.White
                                    ),
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(CircleShape)
                                        .testTag("chat_btn_mic")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Mic,
                                        contentDescription = "Rekam Suara",
                                        tint = Color.White,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            } else {
                                IconButton(
                                    onClick = {
                                        val now = System.currentTimeMillis()
                                        if (now - lastSendClickTime < 500L) return@IconButton
                                        lastSendClickTime = now
                                        lastActivityTime = now
                                        val replyTarget = replyingToMessage
                                        replyingToMessage = null
                                        if (pendingPhotoUri != null) {
                                            val uriToSend = pendingPhotoUri!!
                                            val caption = inputText
                                            pendingPhotoUri = null
                                            inputText = ""
                                            onUserTyping?.invoke(false)
                                            if (onSendPhotoMessageWithReply != null) {
                                                onSendPhotoMessageWithReply(uriToSend, caption, replyTarget)
                                            } else {
                                                onSendPhotoMessage?.invoke(uriToSend, caption)
                                            }
                                        } else if (inputText.isNotBlank()) {
                                            val textToSend = inputText
                                            inputText = ""
                                            onUserTyping?.invoke(false)
                                            if (onSendMessageWithReply != null) {
                                                onSendMessageWithReply(textToSend, replyTarget)
                                            } else {
                                                onSendMessage(textToSend)
                                            }
                                        }
                                    },
                                    enabled = !isUploadingPhoto,
                                    colors = IconButtonDefaults.iconButtonColors(
                                        containerColor = EmeraldGreen,
                                        contentColor = Color.White,
                                        disabledContainerColor = EmeraldGreen.copy(alpha = 0.4f),
                                        disabledContentColor = Color.White.copy(alpha = 0.6f)
                                    ),
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(CircleShape)
                                        .testTag("chat_send_button")
                                ) {
                                    if (isUploadingPhoto) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(18.dp),
                                            color = Color.White,
                                            strokeWidth = 2.dp
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.Send,
                                            contentDescription = AppStrings.chatSendBtn(language),
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                        // Emoji Keyboard Panel (collapsible below input bar)
                        AnimatedVisibility(
                            visible = showInputEmojiPicker,
                            enter = expandVertically() + fadeIn(),
                            exit = shrinkVertically() + fadeOut()
                        ) {
                            EmojiKeyboardPanel(
                                language = language,
                                onEmojiClick = { emoji ->
                                    inputText += emoji
                                },
                                onBackspaceClick = {
                                    if (inputText.isNotEmpty()) {
                                        val codePoints = inputText.codePoints().toArray()
                                        if (codePoints.isNotEmpty()) {
                                            inputText = String(codePoints, 0, codePoints.size - 1)
                                        }
                                    }
                                },
                                onClose = {
                                    showInputEmojiPicker = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // Contextual Message Action Bottom Sheet (Reaksi Emoji WhatsApp Style, Balas, Salin, Hapus)
    if (messageForActionMenu != null) {
        val targetMsg = messageForActionMenu!!
        MessageActionMenuBottomSheet(
            language = language,
            message = targetMsg,
            onDismiss = { messageForActionMenu = null },
            onReact = { emoji ->
                onReactToMessage?.invoke(targetMsg.id, emoji)
                messageForActionMenu = null
            },
            onMoreEmojis = {
                targetMessageForExtendedEmoji = targetMsg
                messageForActionMenu = null
                showExtendedEmojiPicker = true
            },
            onReply = {
                replyingToMessage = targetMsg
                messageForActionMenu = null
            },
            onCopy = {
                if (targetMsg.text.isNotBlank()) {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("Chat message", targetMsg.text)
                    clipboard.setPrimaryClip(clip)
                    Toast.makeText(context, AppStrings.chatActionCopy(language) + " ✓", Toast.LENGTH_SHORT).show()
                }
                messageForActionMenu = null
            },
            onRemoveReaction = {
                onReactToMessage?.invoke(targetMsg.id, null)
                messageForActionMenu = null
            },
            onDelete = {
                messageToDelete = targetMsg
                messageForActionMenu = null
            }
        )
    }

    // Extended Emoji Picker Modal Bottom Sheet (Grid Lengkap Emoji untuk Reaksi Pesan)
    if (showExtendedEmojiPicker && targetMessageForExtendedEmoji != null) {
        val targetMsg = targetMessageForExtendedEmoji!!
        ExtendedEmojiPickerBottomSheet(
            language = language,
            currentReaction = targetMsg.reaction,
            onDismiss = {
                showExtendedEmojiPicker = false
                targetMessageForExtendedEmoji = null
            },
            onEmojiSelected = { emoji ->
                onReactToMessage?.invoke(targetMsg.id, emoji)
                showExtendedEmojiPicker = false
                targetMessageForExtendedEmoji = null
            }
        )
    }

    // Fullscreen Chat Photo Dialog dengan dukungan Zoom 2 Jari (Pinch-to-zoom)
    viewingPhotoUrl?.let { photoUrl ->
        com.example.ui.components.ZoomablePhotoViewerDialog(
            photoUrl = photoUrl,
            title = AppStrings.chatPhotoTitle(language),
            onDismiss = { viewingPhotoUrl = null }
        )
    }

    // Modal Bottom Sheet displaying Partner Profile details
    if (showPartnerProfileSheet) {
        PartnerProfileBottomSheet(
            language = language,
            partnerName = partnerName,
            partnerAvatarHex = partnerAvatarHex,
            partnerAvatarUrl = partnerAvatarUrl,
            partnerBio = partnerBio,
            partnerCity = partnerCity,
            partnerDistance = partnerDistance,
            partnerGender = partnerGender,
            partnerAge = partnerAge,
            partnerMoments = partnerMoments,
            isBlocked = isPartnerBlocked,
            onBlockUser = onBlockPartner,
            onUnblockUser = onUnblockPartner,
            onReportUser = onReportPartner,
            onToggleLikeMoment = onToggleLikeMoment,
            onCommentClick = { moment -> activeMomentForComments = moment },
            momentComments = momentComments,
            onDismiss = { showPartnerProfileSheet = false },
            onSendGreeting = { greeting ->
                onSendMessage(greeting)
                showPartnerProfileSheet = false
            }
        )
    }

    // Modal Bottom Sheet untuk Melihat & Mengirim Komentar Momen Teman
    activeMomentForComments?.let { activeMoment ->
        val comments = momentComments[activeMoment.id] ?: emptyList()
        com.example.ui.components.MomentCommentsBottomSheet(
            moment = activeMoment,
            comments = comments,
            language = language,
            onDismiss = { activeMomentForComments = null },
            onAddComment = { text ->
                onAddComment?.invoke(activeMoment.id, text)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartnerProfileBottomSheet(
    language: AppLanguage = AppLanguage.INDONESIAN,
    partnerName: String,
    partnerAvatarHex: Long,
    partnerAvatarUrl: String?,
    partnerBio: String,
    partnerCity: String,
    partnerDistance: String,
    partnerGender: Gender?,
    partnerAge: Int,
    partnerMoments: List<MomentItem> = emptyList(),
    isBlocked: Boolean = false,
    onBlockUser: (() -> Unit)? = null,
    onUnblockUser: (() -> Unit)? = null,
    onReportUser: ((reason: String, notes: String, alsoBlock: Boolean) -> Unit)? = null,
    onToggleLikeMoment: ((String) -> Unit)? = null,
    onCommentClick: ((MomentItem) -> Unit)? = null,
    momentComments: Map<String, List<com.example.model.MomentComment>> = emptyMap(),
    onDismiss: () -> Unit,
    onSendGreeting: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var previewMoment by remember { mutableStateOf<MomentItem?>(null) }
    var viewingAvatarUrl by remember { mutableStateOf<String?>(null) }
    var showBlockConfirmDialog by remember { mutableStateOf(false) }
    var showReportDialog by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = Color.White,
        modifier = Modifier.testTag("sheet_partner_profile")
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header with title and close button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = AppStrings.partnerProfileSheetTitle(language),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeutralDark
                )
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = AppStrings.commonClose(language),
                        tint = NeutralMedium,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            if (isBlocked) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    color = Color(0xFFFFEBEE),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, Color(0xFFFFCDD2)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("banner_sheet_blocked")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Block,
                            contentDescription = null,
                            tint = Color(0xFFD32F2F),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = AppStrings.partnerProfileBlockedBanner(language),
                            fontSize = 11.5.sp,
                            color = Color(0xFFC62828),
                            lineHeight = 15.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Partner Avatar (Large, with online badge - Clickable to open zoomable viewer if has photo)
            val hasPhoto = !partnerAvatarUrl.isNullOrBlank()
            LovyAvatar(
                name = partnerName,
                avatarColorHex = partnerAvatarHex,
                avatarUrl = partnerAvatarUrl,
                size = 80.dp,
                fontSize = 32.sp,
                isOnline = true,
                modifier = if (hasPhoto) {
                    Modifier
                        .clip(CircleShape)
                        .clickable { viewingAvatarUrl = partnerAvatarUrl }
                } else Modifier
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Partner Name & Verified Check
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = partnerName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeutralDark
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = AppStrings.commonVerified(language),
                    tint = EmeraldGreen,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Gender and Age pill
            val genderText = AppStrings.genderLabel(language, partnerGender)
            val ageText = AppStrings.ageYears(language, partnerAge)
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (partnerGender == Gender.FEMALE) Color(0xFFFCE4EC) else Color(0xFFE3F2FD),
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                Text(
                    text = "$genderText • $ageText",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (partnerGender == Gender.FEMALE) Color(0xFFC2185B) else Color(0xFF1976D2),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Location & Distance Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = EmeraldGreen,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = AppStrings.locationDistance(language, partnerCity, partnerDistance),
                    fontSize = 12.5.sp,
                    color = NeutralMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bio Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ScreenBackground),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.FormatQuote,
                            contentDescription = null,
                            tint = EmeraldGreen,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = AppStrings.partnerProfileBio(language),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = NeutralDark
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = partnerBio,
                        fontSize = 13.5.sp,
                        lineHeight = 19.sp,
                        color = NeutralDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Section: Momen & Foto yang diunggah dan belum dihapus
            val activeMoments = remember(partnerMoments) {
                partnerMoments.filter { !it.isDeleted }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.PhotoLibrary,
                        contentDescription = null,
                        tint = EmeraldGreen,
                        modifier = Modifier.size(19.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = AppStrings.partnerProfileRecentMoments(language),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeutralDark
                    )
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = EmeraldGreen.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = AppStrings.partnerProfileMomentsCount(language, activeMoments.size),
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = EmeraldGreen,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (activeMoments.isEmpty()) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ScreenBackground),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp, horizontal = 16.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = null,
                                tint = NeutralMedium,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = AppStrings.partnerProfileNoMoments(language),
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeutralDark
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = AppStrings.partnerProfileNoMomentsDesc(language, partnerName),
                            fontSize = 12.sp,
                            color = NeutralMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    activeMoments.forEach { moment ->
                        val commentsForThis = momentComments[moment.id]
                        val accurateCommentsCount = maxOf(moment.commentsCount, commentsForThis?.size ?: 0)
                        val displayMoment = if (moment.commentsCount != accurateCommentsCount) {
                            moment.copy(commentsCount = accurateCommentsCount)
                        } else moment

                        PartnerMomentItemCard(
                            language = language,
                            moment = displayMoment,
                            onPhotoClick = { previewMoment = displayMoment },
                            onToggleLike = {
                                onToggleLikeMoment?.invoke(displayMoment.id)
                            },
                            onCommentClick = {
                                onCommentClick?.invoke(displayMoment)
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // Quick Actions: Sapa Balik & Lanjutkan Chat
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("btn_close_partner_profile")
                ) {
                    Text(AppStrings.partnerProfileContinueChat(language), fontSize = 13.5.sp, color = NeutralDark)
                }

                Button(
                    onClick = {
                        onSendGreeting(AppStrings.partnerProfileGreetingText(language, partnerName))
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("btn_send_greeting_partner")
                ) {
                    Text(AppStrings.partnerProfileSendGreeting(language), fontSize = 13.5.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Block / Unblock User Action
            if (isBlocked) {
                OutlinedButton(
                    onClick = {
                        onUnblockUser?.invoke()
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = EmeraldGreen
                    ),
                    border = BorderStroke(1.dp, EmeraldGreen),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("btn_unblock_user_action")
                ) {
                    Icon(
                        imageVector = Icons.Default.LockOpen,
                        contentDescription = null,
                        tint = EmeraldGreen,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = AppStrings.partnerProfileUnblockUser(language),
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldGreen
                    )
                }
            } else {
                OutlinedButton(
                    onClick = {
                        showBlockConfirmDialog = true
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFD32F2F)
                    ),
                    border = BorderStroke(1.dp, Color(0xFFFFCDD2)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("btn_block_user_action")
                ) {
                    Icon(
                        imageVector = Icons.Default.Block,
                        contentDescription = null,
                        tint = Color(0xFFD32F2F),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = AppStrings.partnerProfileBlockUser(language),
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFD32F2F)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                OutlinedButton(
                    onClick = { showReportDialog = true },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFC62828)
                    ),
                    border = BorderStroke(1.dp, Color(0xFFFFCDD2)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("btn_report_partner_action")
                ) {
                    Icon(
                        imageVector = Icons.Default.Flag,
                        contentDescription = null,
                        tint = Color(0xFFC62828),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = AppStrings.partnerProfileReportUser(language),
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFC62828)
                    )
                }
            }
        }
    }

    if (showReportDialog) {
        ReportDialog(
            targetName = partnerName,
            reportType = ReportType.USER,
            language = language,
            onDismiss = { showReportDialog = false },
            onSubmitReport = { reason, notes, alsoBlock ->
                onReportUser?.invoke(reason, notes, alsoBlock)
                onDismiss()
            }
        )
    }

    // Block User Confirmation Dialog
    if (showBlockConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showBlockConfirmDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Block,
                    contentDescription = null,
                    tint = Color(0xFFD32F2F),
                    modifier = Modifier.size(32.dp)
                )
            },
            title = {
                Text(
                    text = AppStrings.partnerProfileBlockConfirmTitle(language, partnerName),
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Text(
                    text = AppStrings.partnerProfileBlockConfirmDesc(language),
                    fontSize = 13.5.sp,
                    color = NeutralMedium,
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showBlockConfirmDialog = false
                        onBlockUser?.invoke()
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("btn_confirm_block_user")
                ) {
                    Text(AppStrings.btnBlock(language), color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showBlockConfirmDialog = false },
                    modifier = Modifier.testTag("btn_cancel_block_user")
                ) {
                    Text(AppStrings.btnCancel(language), color = NeutralDark)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(18.dp)
        )
    }

    // Photo Preview Lightbox Dialog
    previewMoment?.let { moment ->
        val commentsForThis = momentComments[moment.id]
        val accurateCommentsCount = maxOf(moment.commentsCount, commentsForThis?.size ?: 0)
        val displayMoment = if (moment.commentsCount != accurateCommentsCount) {
            moment.copy(commentsCount = accurateCommentsCount)
        } else moment

        PartnerPhotoPreviewDialog(
            language = language,
            moment = displayMoment,
            partnerAvatarHex = partnerAvatarHex,
            onDismiss = { previewMoment = null },
            onToggleLike = {
                onToggleLikeMoment?.invoke(displayMoment.id)
            },
            onCommentClick = {
                previewMoment = null
                onCommentClick?.invoke(displayMoment)
            }
        )
    }

    // Zoomable Fullscreen Dialog saat foto profil teman diklik
    viewingAvatarUrl?.let { avatarUrl ->
        com.example.ui.components.ZoomablePhotoViewerDialog(
            photoUrl = avatarUrl,
            title = AppStrings.partnerProfilePhotoTitle(language, partnerName),
            onDismiss = { viewingAvatarUrl = null }
        )
    }
}

@Composable
fun PartnerMomentItemCard(
    language: AppLanguage = AppLanguage.INDONESIAN,
    moment: MomentItem,
    onPhotoClick: () -> Unit,
    onToggleLike: () -> Unit,
    onCommentClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFEEEEEE)),
        modifier = modifier
            .fillMaxWidth()
            .testTag("partner_moment_${moment.id}")
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Photo if available
            if (!moment.imageUrl.isNullOrBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                        .clickable { onPhotoClick() }
                ) {
                    AsyncImage(
                        model = moment.imageUrl,
                        contentDescription = "Foto momen dari ${moment.authorName}",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Zoom / Fullscreen overlay indicator
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.Black.copy(alpha = 0.55f),
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Fullscreen,
                                contentDescription = AppStrings.commonZoomPhoto(language),
                                tint = Color.White,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = AppStrings.commonPhoto(language),
                                fontSize = 11.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    // Bottom location tag over image if present
                    if (!moment.locationTag.isNullOrBlank()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .background(
                                    Brush.verticalGradient(
                                        listOf(Color.Transparent, Color.Black.copy(alpha = 0.65f))
                                    )
                                )
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = moment.locationTag,
                                    fontSize = 11.sp,
                                    color = Color.White,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }

            // Caption & Metadata
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                // Time ago and location if no image
                if (moment.imageUrl.isNullOrBlank() && !moment.locationTag.isNullOrBlank()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = EmeraldGreen,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = moment.locationTag,
                            fontSize = 11.5.sp,
                            color = NeutralMedium
                        )
                    }
                }

                Text(
                    text = moment.content,
                    fontSize = 13.5.sp,
                    lineHeight = 19.sp,
                    color = NeutralDark,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Footer Row: Time Ago & Social Interactions
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = moment.timeAgo,
                        fontSize = 11.5.sp,
                        color = NeutralMedium
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Like Button with heart
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onToggleLike() }
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Icon(
                                imageVector = if (moment.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = AppStrings.momentsLike(language),
                                tint = if (moment.isLiked) Color(0xFFE91E63) else NeutralMedium,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${moment.likesCount}",
                                fontSize = 12.sp,
                                fontWeight = if (moment.isLiked) FontWeight.Bold else FontWeight.Normal,
                                color = if (moment.isLiked) Color(0xFFE91E63) else NeutralMedium
                            )
                        }

                        // Comments count indicator and action
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onCommentClick() }
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ChatBubbleOutline,
                                contentDescription = AppStrings.momentsComment(language),
                                tint = NeutralMedium,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${moment.commentsCount}",
                                fontSize = 12.sp,
                                color = NeutralMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PartnerPhotoPreviewDialog(
    language: AppLanguage = AppLanguage.INDONESIAN,
    moment: MomentItem,
    partnerAvatarHex: Long,
    onDismiss: () -> Unit,
    onToggleLike: () -> Unit,
    onCommentClick: (() -> Unit)? = null
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.92f))
                .clickable { onDismiss() }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Header Bar
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        LovyAvatar(
                            name = moment.authorName,
                            avatarColorHex = partnerAvatarHex,
                            avatarUrl = moment.authorAvatarUrl,
                            size = 36.dp,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = moment.authorName,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = moment.timeAgo,
                                fontSize = 11.5.sp,
                                color = Color.White.copy(alpha = 0.75f)
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = AppStrings.commonClose(language),
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Center: High-res photo with 2-finger zoom and double-tap zoom
                if (!moment.imageUrl.isNullOrBlank()) {
                    var scale by remember { mutableFloatStateOf(1f) }
                    var offset by remember { mutableStateOf(Offset.Zero) }

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(vertical = 12.dp)
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onDoubleTap = { tapOffset ->
                                        if (scale > 1.2f) {
                                            scale = 1f
                                            offset = Offset.Zero
                                        } else {
                                            scale = 2.5f
                                            offset = Offset(
                                                x = (size.width / 2f - tapOffset.x) * 1.5f,
                                                y = (size.height / 2f - tapOffset.y) * 1.5f
                                            )
                                        }
                                    }
                                )
                            }
                            .pointerInput(Unit) {
                                detectTransformGestures { _, pan, zoom, _ ->
                                    val newScale = (scale * zoom).coerceIn(0.85f, 5f)
                                    scale = newScale
                                    if (newScale > 1f) {
                                        val maxOffsetX = (size.width * (newScale - 1f)) / 1.8f
                                        val maxOffsetY = (size.height * (newScale - 1f)) / 1.8f
                                        offset = Offset(
                                            x = (offset.x + pan.x).coerceIn(-maxOffsetX, maxOffsetX),
                                            y = (offset.y + pan.y).coerceIn(-maxOffsetY, maxOffsetY)
                                        )
                                    } else {
                                        offset = Offset.Zero
                                    }
                                }
                            }
                    ) {
                        AsyncImage(
                            model = moment.imageUrl,
                            contentDescription = "Foto momen penuh",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .graphicsLayer(
                                    scaleX = scale,
                                    scaleY = scale,
                                    translationX = offset.x,
                                    translationY = offset.y
                                )
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }

                // Bottom: Caption, Location, and Likes Card
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFF1E1E1E),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .clickable(enabled = false) {}
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        if (!moment.locationTag.isNullOrBlank()) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(bottom = 6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = EmeraldGreen,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = moment.locationTag,
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            }
                        }

                        Text(
                            text = moment.content,
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Like toggle in dialog
                            Button(
                                onClick = onToggleLike,
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (moment.isLiked) Color(0xFFE91E63) else Color.White.copy(alpha = 0.15f)
                                ),
                                modifier = Modifier.height(36.dp)
                            ) {
                                Icon(
                                    imageVector = if (moment.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = AppStrings.partnerMomentLikesCount(language, moment.likesCount),
                                    fontSize = 12.5.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .then(
                                        if (onCommentClick != null) Modifier.clickable { onCommentClick() }
                                        else Modifier
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ChatBubbleOutline,
                                    contentDescription = null,
                                    tint = Color.White.copy(alpha = 0.85f),
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = AppStrings.partnerMomentCommentsCount(language, moment.commentsCount),
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.85f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * WhatsApp-style Swipe to Reply wrapper component.
 * Allows swiping any message bubble horizontally to the right to trigger a reply.
 * Displays an animated reply icon on the left with haptic feedback when reaching threshold.
 */
@Composable
fun SwipeableChatBubble(
    language: AppLanguage = AppLanguage.INDONESIAN,
    message: ChatMessage,
    onReply: (ChatMessage) -> Unit,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
    onReactionClick: (() -> Unit)? = null,
    onPhotoClick: ((String) -> Unit)? = null,
    onQuotedMessageClick: ((String) -> Unit)? = null
) {
    val haptic = LocalHapticFeedback.current
    val density = LocalDensity.current
    val swipeThresholdDp = 52.dp
    val swipeThresholdPx = with(density) { swipeThresholdDp.toPx() }
    val maxDragPx = with(density) { 88.dp.toPx() }

    var dragOffset by remember { mutableFloatStateOf(0f) }
    var hasHapticTriggered by remember { mutableStateOf(false) }

    val animatedOffset by animateFloatAsState(
        targetValue = dragOffset,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "chat_bubble_swipe"
    )

    val swipeProgress = (animatedOffset / swipeThresholdPx).coerceIn(0f, 1f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(message.id) {
                detectHorizontalDragGestures(
                    onHorizontalDrag = { change, dragAmount ->
                        // Swipe to the right (WhatsApp standard)
                        val newOffset = (dragOffset + dragAmount).coerceIn(0f, maxDragPx)
                        if (newOffset != dragOffset) {
                            dragOffset = newOffset
                            change.consume()
                        }
                        if (dragOffset >= swipeThresholdPx && !hasHapticTriggered) {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            hasHapticTriggered = true
                        } else if (dragOffset < swipeThresholdPx) {
                            hasHapticTriggered = false
                        }
                    },
                    onDragEnd = {
                        if (dragOffset >= swipeThresholdPx) {
                            onReply(message)
                        }
                        dragOffset = 0f
                        hasHapticTriggered = false
                    },
                    onDragCancel = {
                        dragOffset = 0f
                        hasHapticTriggered = false
                    }
                )
            }
    ) {
        // WhatsApp Reply Indicator Icon appearing from left
        if (animatedOffset > 4f) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .offset(x = with(density) { (animatedOffset * 0.35f).coerceAtLeast(6f).toDp() })
                    .size(34.dp)
                    .graphicsLayer {
                        alpha = swipeProgress
                        scaleX = 0.6f + (0.4f * swipeProgress)
                        scaleY = 0.6f + (0.4f * swipeProgress)
                    }
                    .clip(CircleShape)
                    .background(
                        if (swipeProgress >= 0.95f) EmeraldGreen else EmeraldGreen.copy(alpha = 0.25f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Reply,
                    contentDescription = AppStrings.chatReplyingTo(language),
                    tint = if (swipeProgress >= 0.95f) Color.White else EmeraldGreen,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        // The Sliding Chat Bubble
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(animatedOffset.roundToInt(), 0) }
                .padding(bottom = if (!message.reaction.isNullOrBlank()) 10.dp else 2.dp)
        ) {
            ChatBubble(
                language = language,
                message = message,
                onClick = onClick,
                onLongClick = onLongClick,
                onReactionClick = onReactionClick,
                onPhotoClick = onPhotoClick,
                onQuotedMessageClick = onQuotedMessageClick
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatBubble(
    language: AppLanguage = AppLanguage.INDONESIAN,
    message: ChatMessage,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
    onReactionClick: (() -> Unit)? = null,
    onPhotoClick: ((String) -> Unit)? = null,
    onQuotedMessageClick: ((String) -> Unit)? = null
) {
    val haptic = LocalHapticFeedback.current
    val timeFormat = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
    val formattedTime = remember(message.timestamp) {
        timeFormat.format(Date(message.timestamp))
    }

    Row(
        horizontalArrangement = if (message.isFromMe) Arrangement.End else Arrangement.Start,
        modifier = modifier.fillMaxWidth()
    ) {
        Box {
            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (message.isFromMe) 16.dp else 4.dp,
                            bottomEnd = if (message.isFromMe) 4.dp else 16.dp
                        )
                    )
                    .background(if (message.isFromMe) ChatBubbleSelf else ChatBubbleOther)
                    .then(
                        if (onClick != null || onLongClick != null) {
                            Modifier.combinedClickable(
                                onClick = { onClick?.invoke() },
                                onLongClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onLongClick?.invoke()
                                }
                            )
                        } else Modifier
                    )
                    .padding(horizontal = 10.dp, vertical = 8.dp)
            ) {
            Column(horizontalAlignment = if (message.isFromMe) Alignment.End else Alignment.Start) {
                // Quoted Reply Preview inside bubble (WhatsApp Style)
                if (!message.replyToText.isNullOrBlank() || !message.replyToSender.isNullOrBlank()) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (message.isFromMe) Color(0xFFC8E6C9).copy(alpha = 0.55f) else Color(0xFFE8ECEF),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .then(
                                if (onQuotedMessageClick != null && !message.replyToId.isNullOrBlank()) {
                                    Modifier.clickable { onQuotedMessageClick(message.replyToId) }
                                } else Modifier
                            )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min)
                        ) {
                            // Left vertical accent stripe
                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .fillMaxHeight()
                                    .background(if (message.isFromMe) EmeraldGreen else Color(0xFF00897B))
                            )

                            Column(
                                modifier = Modifier
                                    .padding(horizontal = 8.dp, vertical = 5.dp)
                                    .weight(1f)
                            ) {
                                Text(
                                    text = message.replyToSender ?: AppStrings.chatMessageFallback(language),
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (message.isFromMe) Color(0xFF1B5E20) else Color(0xFF00695C),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = message.replyToText ?: "",
                                    fontSize = 11.5.sp,
                                    color = NeutralDark.copy(alpha = 0.85f),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }

                // If message has voice note attachment
                if (!message.audioUrl.isNullOrBlank()) {
                    VoiceNoteBubbleContent(
                        audioUrl = message.audioUrl,
                        durationSeconds = message.audioDurationSeconds,
                        messageId = message.id,
                        isFromMe = message.isFromMe
                    )
                    if (message.text.isNotBlank() && !message.text.startsWith("🎙️")) {
                        Spacer(modifier = Modifier.height(4.dp))
                        MessageTextWithLinks(
                            text = message.text,
                            isFromMe = message.isFromMe,
                            fontSize = 14.sp,
                            textColor = NeutralDark,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }
                } else if (!message.imageUrl.isNullOrBlank()) {
                    Box(
                        modifier = Modifier
                            .widthIn(min = 140.dp, max = 220.dp)
                            .heightIn(min = 140.dp, max = 240.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable {
                                onPhotoClick?.invoke(message.imageUrl)
                            }
                    ) {
                        AsyncImage(
                            model = message.imageUrl,
                            contentDescription = AppStrings.chatPhotoPreviewTitle(language),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    if (message.text.isNotBlank() && !message.text.startsWith("📷")) {
                        Spacer(modifier = Modifier.height(6.dp))
                        MessageTextWithLinks(
                            text = message.text,
                            isFromMe = message.isFromMe,
                            fontSize = 14.sp,
                            textColor = NeutralDark,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }
                } else {
                    MessageTextWithLinks(
                        text = message.text,
                        isFromMe = message.isFromMe,
                        fontSize = 14.sp,
                        textColor = NeutralDark,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(3.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(horizontal = 4.dp)
                ) {
                    Text(
                        text = formattedTime,
                        fontSize = 10.5.sp,
                        color = NeutralMedium
                    )

                    if (message.isFromMe) {
                        val tickColor by animateColorAsState(
                            targetValue = if (message.isRead) Color(0xFF34B7F1) else NeutralMedium.copy(alpha = 0.7f),
                            animationSpec = tween(durationMillis = 350),
                            label = "tickColorAnim"
                        )
                        Icon(
                            imageVector = Icons.Default.DoneAll,
                            contentDescription = if (message.isRead) AppStrings.chatStatusRead(language) else AppStrings.chatStatusSent(language),
                            tint = tickColor,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }

        // Floating Emoji Reaction Badge (WhatsApp Style)
        if (!message.reaction.isNullOrBlank()) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                shadowElevation = 2.dp,
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier
                    .align(if (message.isFromMe) Alignment.BottomEnd else Alignment.BottomStart)
                    .offset(
                        x = if (message.isFromMe) (-8).dp else 8.dp,
                        y = 10.dp
                    )
                    .clip(RoundedCornerShape(12.dp))
                    .clickable {
                        onReactionClick?.invoke()
                    }
                    .testTag("chat_msg_reaction_${message.id}")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = message.reaction,
                        fontSize = 13.5.sp
                    )
                }
            }
        }
    }
}
}

private val URL_PATTERN = Regex("""(https?://[^\s]+|www\.[^\s]+)""", RegexOption.IGNORE_CASE)

/**
 * Komponen teks dengan deteksi URL otomatis (Clickable Links).
 * Memungkinkan pengguna membuka tautan langsung ke browser dengan sekali ketuk.
 */
@Composable
fun MessageTextWithLinks(
    text: String,
    modifier: Modifier = Modifier,
    isFromMe: Boolean = false,
    fontSize: TextUnit = 14.sp,
    textColor: Color = NeutralDark
) {
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current
    val matches = remember(text) { URL_PATTERN.findAll(text).toList() }

    if (matches.isEmpty()) {
        Text(
            text = text,
            fontSize = fontSize,
            color = textColor,
            lineHeight = 19.sp,
            modifier = modifier
        )
        return
    }

    val linkColor = if (isFromMe) Color(0xFF0D47A1) else Color(0xFF1565C0)

    val annotatedString = remember(text, matches, linkColor) {
        buildAnnotatedString {
            var lastIndex = 0
            for (match in matches) {
                val start = match.range.first
                val end = match.range.last + 1

                if (start > lastIndex) {
                    append(text.substring(lastIndex, start))
                }

                val urlMatch = match.value
                val tag = "URL"
                pushStringAnnotation(tag = tag, annotation = urlMatch)
                pushStyle(
                    SpanStyle(
                        color = linkColor,
                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                append(urlMatch)
                pop()
                pop()

                lastIndex = end
            }
            if (lastIndex < text.length) {
                append(text.substring(lastIndex))
            }
        }
    }

    ClickableText(
        text = annotatedString,
        style = androidx.compose.ui.text.TextStyle(
            fontSize = fontSize,
            color = textColor,
            lineHeight = 19.sp
        ),
        modifier = modifier,
        onClick = { offset ->
            annotatedString.getStringAnnotations(tag = "URL", start = offset, end = offset)
                .firstOrNull()?.let { annotation ->
                    var rawUrl = annotation.item.trim()
                    while (rawUrl.isNotEmpty() && (rawUrl.endsWith(".") || rawUrl.endsWith(",") || rawUrl.endsWith(")") || rawUrl.endsWith("!"))) {
                        rawUrl = rawUrl.dropLast(1)
                    }
                    val finalUrl = if (!rawUrl.startsWith("http://", ignoreCase = true) &&
                        !rawUrl.startsWith("https://", ignoreCase = true)
                    ) {
                        "https://$rawUrl"
                    } else {
                        rawUrl
                    }
                    try {
                        uriHandler.openUri(finalUrl)
                    } catch (_: Exception) {
                        try {
                            val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(finalUrl)).apply {
                                addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
                            }
                            context.startActivity(intent)
                        } catch (t: Throwable) {
                            android.widget.Toast.makeText(context, "Tidak dapat membuka tautan: $finalUrl", android.widget.Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        }
    )
}

@Composable
fun TypingIndicatorBubble(
    partnerName: String,
    partnerAvatarHex: Long,
    partnerAvatarUrl: String? = null,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "typing_dots_transition")

    val dot1Offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -5.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 420, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot1_offset"
    )
    val dot2Offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -5.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 420, delayMillis = 140, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot2_offset"
    )
    val dot3Offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -5.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 420, delayMillis = 280, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot3_offset"
    )

    Row(
        verticalAlignment = Alignment.Bottom,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .testTag("typing_indicator_bubble")
    ) {
        LovyAvatar(
            name = partnerName,
            avatarColorHex = partnerAvatarHex,
            avatarUrl = partnerAvatarUrl,
            size = 32.dp,
            fontSize = 13.sp,
            isOnline = true
        )

        Spacer(modifier = Modifier.width(8.dp))

        Surface(
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 4.dp, bottomEnd = 16.dp),
            color = Color.White,
            shadowElevation = 1.dp,
            border = BorderStroke(0.6.dp, Color(0xFFE0E0E0)),
            modifier = Modifier.heightIn(min = 36.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .offset(y = dot1Offset.dp)
                        .size(7.dp)
                        .clip(CircleShape)
                        .background(EmeraldGreen)
                )
                Box(
                    modifier = Modifier
                        .offset(y = dot2Offset.dp)
                        .size(7.dp)
                        .clip(CircleShape)
                        .background(EmeraldGreen.copy(alpha = 0.8f))
                )
                Box(
                    modifier = Modifier
                        .offset(y = dot3Offset.dp)
                        .size(7.dp)
                        .clip(CircleShape)
                        .background(EmeraldGreen.copy(alpha = 0.6f))
                )
            }
        }
    }
}

/**
 * WhatsApp-style Contextual Message Action Bottom Sheet.
 * Displays quick emoji reactions bar (❤️ 👍 😂 😮 😢 🙏 🔥 🎉 +) and standard message actions.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageActionMenuBottomSheet(
    language: AppLanguage,
    message: ChatMessage,
    onDismiss: () -> Unit,
    onReact: (String) -> Unit,
    onMoreEmojis: () -> Unit,
    onReply: () -> Unit,
    onCopy: () -> Unit,
    onRemoveReaction: () -> Unit,
    onDelete: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val haptic = LocalHapticFeedback.current
    val quickEmojis = remember { listOf("❤️", "👍", "😂", "😮", "😢", "🙏", "🔥", "🎉") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = Color.White,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
        ) {
            // WhatsApp style Floating Quick Reaction Bar
            Surface(
                shape = RoundedCornerShape(32.dp),
                color = Color(0xFFF1F5F9),
                shadowElevation = 0.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    quickEmojis.forEach { emoji ->
                        val isSelected = message.reaction == emoji
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) EmeraldGreen.copy(alpha = 0.2f) else Color.Transparent
                                )
                                .clickable {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onReact(emoji)
                                }
                        ) {
                            Text(
                                text = emoji,
                                fontSize = 22.sp
                            )
                        }
                    }

                    // More Emojis button (+)
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onMoreEmojis()
                            }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = AppStrings.chatReactionMore(language),
                            tint = EmeraldGreen,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Quoted message preview snippet
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = NeutralLight.copy(alpha = 0.45f),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                Text(
                    text = if (message.text.isNotBlank()) "\"${message.text}\"" else "📷 Foto",
                    fontSize = 13.sp,
                    color = NeutralDark,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                )
            }

            // Action Items List
            // 1. Reply
            ActionMenuItemRow(
                icon = Icons.AutoMirrored.Filled.Reply,
                title = AppStrings.chatActionReply(language),
                tint = NeutralDark,
                onClick = onReply
            )

            // 2. Copy (if has text)
            if (message.text.isNotBlank()) {
                ActionMenuItemRow(
                    icon = Icons.Default.ContentCopy,
                    title = AppStrings.chatActionCopy(language),
                    tint = NeutralDark,
                    onClick = onCopy
                )
            }

            // 3. Remove Reaction (if has reaction)
            if (!message.reaction.isNullOrBlank()) {
                ActionMenuItemRow(
                    icon = Icons.Default.Close,
                    title = AppStrings.chatActionRemoveReaction(language),
                    tint = Color(0xFFE53935),
                    onClick = onRemoveReaction
                )
            }

            HorizontalDivider(
                color = NeutralBorder.copy(alpha = 0.5f),
                modifier = Modifier.padding(vertical = 4.dp)
            )

            // 4. Delete Message
            ActionMenuItemRow(
                icon = Icons.Default.DeleteOutline,
                title = AppStrings.chatActionDelete(language),
                tint = Color(0xFFD32F2F),
                onClick = onDelete
            )
        }
    }
}

@Composable
fun ActionMenuItemRow(
    icon: ImageVector,
    title: String,
    tint: Color = NeutralDark,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            text = title,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = tint
        )
    }
}

private val EMOJI_CATEGORY_SMILEYS: List<String> = listOf(
    "😀", "😃", "😄", "😁", "😆", "😅", "😂", "🤣", "🥲", "🥹", "☺️", "😊", "😇", "🙂", "🙃", "😉", "😌", "😍", "🥰", "😘", "😗", "😙", "😚", "😋", "😛", "😝", "😜", "🤪", "🤨", "🧐", "🤓", "😎", "🤩", "🥳", "😏", "😒", "😞", "😔", "😟", "😕", "🙁", "☹️", "😣", "😖", "😫", "😩", "🥺", "😢", "😭", "😮‍💨", "😤", "😠", "😡", "🤬", "🤯", "😳", "🥵", "🥶", "😱", "😨", "😰", "😥", "😓", "🤗", "🤔", "🫣", "🤭", "🫢", "🫡", "🤫", "🫠", "🤥", "😶", "😐", "😑", "😬", "🫨", "😯", "😦", "😧", "😮", "😲", "🥱", "😴", "🤤", "😪", "😵", "🤐", "🥴", "🤢", "🤮", "🤧", "😷", "🤒", "🤕", "🤑", "🤠"
)

private val EMOJI_CATEGORY_HEARTS: List<String> = listOf(
    "❤️", "🩷", "🧡", "💛", "💚", "💙", "🩵", "💜", "🖤", "🩶", "🤍", "🤎", "💔", "❤️‍🔥", "❤️‍🩹", "❣️", "💕", "💞", "💓", "💗", "💖", "💘", "💝", "💟", "💌", "💋", "🫰", "🫶", "😍", "🥰", "😘", "💐", "🌹", "🌷", "🌸"
)

private val EMOJI_CATEGORY_GESTURES: List<String> = listOf(
    "👍", "👎", "👊", "✊", "🤛", "🤜", "🫷", "🫸", "🤞", "✌️", "🫰", "🤟", "🤘", "👌", "🤌", "🤏", "👈", "👉", "👆", "👇", "☝️", "✋", "🤚", "🖐️", "🖖", "👋", "🤙", "👏", "🙌", "🫶", "👐", "🤲", "🤝", "🙏", "✍️", "💅", "🤳", "💪"
)

private val EMOJI_CATEGORY_PARTY: List<String> = listOf(
    "🎉", "🎊", "🥳", "🎈", "🎁", "🎂", "✨", "🌟", "⭐", "💫", "🔥", "💥", "💯", "🏆", "🥇", "🎯", "🚀", "🏖️", "🌸", "🍕", "🍔", "☕", "🍦", "🍻", "🍹", "🍿", "🎧", "🎵", "🎶", "🎸", "🎮", "🕹️", "🎲", "💎", "💡"
)

/**
 * Extended Emoji Picker Modal Bottom Sheet for Message Reactions.
 * Categorized emoji grid allowing users to react to messages with any emoji.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExtendedEmojiPickerBottomSheet(
    language: AppLanguage,
    currentReaction: String?,
    onDismiss: () -> Unit,
    onEmojiSelected: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedTab by remember { mutableStateOf(0) }
    val categories: List<Pair<String, List<String>>> = remember {
        listOf(
            "😀" to EMOJI_CATEGORY_SMILEYS,
            "❤️" to EMOJI_CATEGORY_HEARTS,
            "👍" to EMOJI_CATEGORY_GESTURES,
            "🎉" to EMOJI_CATEGORY_PARTY
        )
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = Color.White,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 28.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                Text(
                    text = AppStrings.chatReactionTitle(language),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeutralDark,
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = AppStrings.commonClose(language),
                        tint = NeutralMedium,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color(0xFFF8FAFC),
                contentColor = EmeraldGreen,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = EmeraldGreen
                    )
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .padding(bottom = 12.dp)
            ) {
                categories.forEachIndexed { index, cat ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = cat.first,
                                fontSize = 18.sp
                            )
                        }
                    )
                }
            }

            val currentEmojis: List<String> = categories[selectedTab].second
            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                contentPadding = PaddingValues(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            ) {
                items(items = currentEmojis) { emoji ->
                    val isSelected = currentReaction == emoji
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) EmeraldGreen.copy(alpha = 0.2f) else Color.Transparent
                            )
                            .clickable {
                                onEmojiSelected(emoji)
                            }
                    ) {
                        Text(
                            text = emoji,
                            fontSize = 24.sp
                        )
                    }
                }
            }
        }
    }
}

/**
 * Collapsible Emoji Keyboard Panel for Chat Input Bar.
 * Allows quick selection and insertion of emojis directly into text input.
 */
@Composable
fun EmojiKeyboardPanel(
    language: AppLanguage,
    onEmojiClick: (String) -> Unit,
    onBackspaceClick: () -> Unit,
    onClose: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val categories: List<Pair<String, List<String>>> = remember {
        listOf(
            "😀" to EMOJI_CATEGORY_SMILEYS,
            "❤️" to EMOJI_CATEGORY_HEARTS,
            "👍" to EMOJI_CATEGORY_GESTURES,
            "🎉" to EMOJI_CATEGORY_PARTY
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .background(Color(0xFFF9FAFB))
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        // Tab Row with categories and Close/Backspace
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp)
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.Transparent,
                contentColor = EmeraldGreen,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = EmeraldGreen
                    )
                },
                modifier = Modifier.weight(1f)
            ) {
                categories.forEachIndexed { index, cat ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = cat.first,
                                fontSize = 18.sp
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.width(6.dp))

            // Backspace icon button
            IconButton(
                onClick = onBackspaceClick,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Backspace,
                    contentDescription = "Hapus",
                    tint = NeutralMedium,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Close emoji panel button
            IconButton(
                onClick = onClose,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = AppStrings.commonClose(language),
                    tint = NeutralMedium,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Emoji Grid
        val currentEmojis: List<String> = categories[selectedTab].second
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            contentPadding = PaddingValues(vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(items = currentEmojis) { emoji ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .clickable {
                            onEmojiClick(emoji)
                        }
                ) {
                    Text(
                        text = emoji,
                        fontSize = 22.sp
                    )
                }
            }
        }
    }
}



/**
 * Komponen Pemutar Pesan Suara Modern (Voice Note Bubble) ala WhatsApp / Telegram.
 * Dilengkapi tombol Putar/Jeda melingkar, gelombang audio interaktif (bisa di-tap untuk seek),
 * durasi audio, dan ikon mikrofon.
 */
@Composable
fun VoiceNoteBubbleContent(
    audioUrl: String,
    durationSeconds: Int,
    messageId: String,
    isFromMe: Boolean
) {
    val currentPlayingId by VoicePlayer.currentPlayingId.collectAsState()
    val isPlaying by VoicePlayer.isPlaying.collectAsState()
    val progress by VoicePlayer.playbackProgress.collectAsState()
    val currentPosSec by VoicePlayer.currentPositionSeconds.collectAsState()

    val isThisPlaying = currentPlayingId == messageId && isPlaying
    val currentProgress = if (currentPlayingId == messageId) progress else 0f

    val accentColor = if (isFromMe) EmeraldGreen else Color(0xFF00897B)
    val trackBgColor = if (isFromMe) EmeraldGreen.copy(alpha = 0.28f) else Color(0xFF90A4AE).copy(alpha = 0.45f)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .widthIn(min = 200.dp, max = 260.dp)
            .padding(vertical = 4.dp, horizontal = 2.dp)
    ) {
        // Tombol Putar / Jeda Melingkar
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(accentColor)
                .clickable {
                    VoicePlayer.play(messageId, audioUrl)
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isThisPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = if (isThisPlaying) "Jeda" else "Putar",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
            // Visualisasi Gelombang Audio Interaktif
            VoiceNoteWaveformTrack(
                progress = currentProgress,
                activeColor = accentColor,
                inactiveColor = trackBgColor,
                audioKey = messageId,
                onSeek = { seekPct ->
                    if (currentPlayingId == messageId) {
                        VoicePlayer.seekTo(seekPct)
                    } else {
                        VoicePlayer.play(messageId, audioUrl)
                        VoicePlayer.seekTo(seekPct)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(28.dp)
            )

            Spacer(modifier = Modifier.height(2.dp))

            // Timer & Durasi
            val displaySec = if (currentPlayingId == messageId && currentPosSec > 0) currentPosSec else durationSeconds
            val min = displaySec / 60
            val sec = displaySec % 60
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = String.format(Locale.getDefault(), "%d:%02d", min, sec),
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = NeutralMedium
                )
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = null,
                    tint = accentColor.copy(alpha = 0.7f),
                    modifier = Modifier.size(13.dp)
                )
            }
        }
    }
}

/**
 * Trek Gelombang Suara (Waveform Bars) dengan ketinggian bervariasi.
 * Mendukung interaksi ketukan (tap to seek).
 */
@Composable
fun VoiceNoteWaveformTrack(
    progress: Float,
    activeColor: Color,
    inactiveColor: Color,
    audioKey: String,
    onSeek: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val barCount = 26
    val barHeights = remember(audioKey) {
        val rand = java.util.Random(audioKey.hashCode().toLong())
        List(barCount) {
            0.25f + rand.nextFloat() * 0.75f
        }
    }

    Box(
        modifier = modifier
            .pointerInput(audioKey) {
                detectTapGestures { offset ->
                    val pct = (offset.x / size.width).coerceIn(0f, 1f)
                    onSeek(pct)
                }
            }
    ) {
        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
            val totalWidth = size.width
            val totalHeight = size.height
            val spacing = 2.dp.toPx()
            val totalSpacing = spacing * (barCount - 1)
            val barWidth = ((totalWidth - totalSpacing) / barCount).coerceAtLeast(2.dp.toPx())

            for (i in 0 until barCount) {
                val barFraction = i.toFloat() / barCount.toFloat()
                val isPlayed = barFraction <= progress
                val paintColor = if (isPlayed) activeColor else inactiveColor
                val barHeight = totalHeight * barHeights[i]
                val left = i * (barWidth + spacing)
                val top = (totalHeight - barHeight) / 2f

                drawRoundRect(
                    color = paintColor,
                    topLeft = androidx.compose.ui.geometry.Offset(left, top),
                    size = androidx.compose.ui.geometry.Size(barWidth, barHeight),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(barWidth / 2f, barWidth / 2f)
                )
            }
        }
    }
}

/**
 * Filter anti-duplikasi UI untuk menjamin tidak ada balon chat ganda
 * pada tampilan ChatDetailScreen.
 */
private fun deduplicateMessagesForUi(messages: List<ChatMessage>): List<ChatMessage> {
    if (messages.size <= 1) return messages
    val result = ArrayList<ChatMessage>(messages.size)
    for (m in messages) {
        val isDuplicate = result.any { existing ->
            if (existing.id == m.id) return@any true
            val pb1 = PocketBaseClient.toPbId(existing.id)
            val pb2 = PocketBaseClient.toPbId(m.id)
            if (pb1 == pb2 && pb1.isNotBlank()) return@any true
            val sameConv = existing.conversationId == m.conversationId
            val sameSender = existing.isFromMe == m.isFromMe
            val sameText = existing.text == m.text
            val sameImg = existing.imageUrl == m.imageUrl
            val sameAudio = existing.audioUrl == m.audioUrl
            val closeTime = kotlin.math.abs(existing.timestamp - m.timestamp) <= 15000L
            sameConv && sameSender && sameText && sameImg && sameAudio && closeTime
        }
        if (!isDuplicate) {
            result.add(m)
        }
    }
    return result
}
