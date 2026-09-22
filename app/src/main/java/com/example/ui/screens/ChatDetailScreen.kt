package com.example.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.PhotoLibrary
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
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
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
    onToggleLikeMoment: ((String) -> Unit)? = null,
    onPartnerProfileClick: (() -> Unit)? = null,
    onDeleteMessageForSender: ((String) -> Unit)? = null,
    onSendPhotoMessage: ((android.net.Uri, String) -> Unit)? = null,
    isUploadingPhoto: Boolean = false,
    uploadProgressText: String? = null,
    onPollMessages: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var inputText by remember { mutableStateOf("") }
    var showPartnerProfileSheet by remember { mutableStateOf(false) }
    var messageToDelete by remember { mutableStateOf<ChatMessage?>(null) }
    var pendingPhotoUri by remember { mutableStateOf<android.net.Uri?>(null) }
    var viewingPhotoUrl by remember { mutableStateOf<String?>(null) }

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
                        LovyAvatar(
                            name = partnerName,
                            avatarColorHex = partnerAvatarHex,
                            avatarUrl = partnerAvatarUrl,
                            size = 38.dp,
                            fontSize = 16.sp,
                            isOnline = true
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
                                            text = "Diblokir",
                                            color = Color.White,
                                            fontSize = 9.5.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Profil",
                                    tint = Color.White.copy(alpha = 0.85f),
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                            Text(
                                text = if (isPartnerBlocked) "Kontak Diblokir • Ketuk lihat profil" else "Online • Ketuk lihat profil",
                                fontSize = 11.sp,
                                color = if (isPartnerBlocked) Color(0xFFFFCDD2) else Color.White.copy(alpha = 0.88f)
                            )
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
                            contentDescription = "Kembali",
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
                            contentDescription = "Lihat Profil Lawan Bicara",
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
            // Messages List
            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(messages.filterNot { it.deletedForSender && it.isFromMe }, key = { it.id }) { msg ->
                    ChatBubble(
                        message = msg,
                        onClick = {
                            if (msg.isFromMe) {
                                messageToDelete = msg
                            }
                        },
                        onPhotoClick = { url ->
                            viewingPhotoUrl = url
                        }
                    )
                }
            }

            // Dialog Hapus Pesan untuk Saya (deleted_for_sender)
            if (messageToDelete != null) {
                val msg = messageToDelete!!
                AlertDialog(
                    onDismissRequest = { messageToDelete = null },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = null,
                            tint = Color(0xFFD32F2F)
                        )
                    },
                    title = {
                        Text(
                            text = "Hapus Pesan untuk Saya?",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                    },
                    text = {
                        Column {
                            Text(
                                text = "\"${msg.text}\"",
                                fontSize = 13.5.sp,
                                color = NeutralDark,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Pesan ini akan dihapus dari obrolan Anda (deleted_for_sender) dan tidak akan terlihat lagi oleh Anda.",
                                fontSize = 12.5.sp,
                                color = NeutralMedium
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                onDeleteMessageForSender?.invoke(msg.id)
                                messageToDelete = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                        ) {
                            Text("Hapus untuk Saya", color = Color.White)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { messageToDelete = null }) {
                            Text("Batal", color = NeutralMedium)
                        }
                    }
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
                        val suggestions = listOf("Halo! 👋", "Lagi di mana?", "Kenalan dong 😊", "Asik nih!")
                        suggestions.forEach { suggestion ->
                            SuggestionChip(
                                onClick = {
                                    onSendMessage(suggestion)
                                },
                                label = { Text(suggestion, fontSize = 11.5.sp) },
                                colors = SuggestionChipDefaults.suggestionChipColors(
                                    containerColor = ScreenBackground
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
                                        text = "Pengguna diblokir. Tidak dapat mengirim pesan.",
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
                                        text = "Buka Blokir",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFD32F2F)
                                    )
                                }
                            }
                        }
                    } else {
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
                                            text = "Foto siap dikirim",
                                            fontSize = 12.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = EmeraldGreen
                                        )
                                        Text(
                                            text = if (isUploadingPhoto) (uploadProgressText ?: "Mengunggah ke Cloudflare R2...") else "Ketik keterangan atau tekan tombol kirim",
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
                                                contentDescription = "Batal",
                                                tint = NeutralMedium,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Bottom Input Bar
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
                                    contentDescription = "Kirim Foto",
                                    tint = EmeraldGreen,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(4.dp))

                            OutlinedTextField(
                                value = inputText,
                                onValueChange = { inputText = it },
                                placeholder = {
                                    Text(
                                        text = if (pendingPhotoUri != null) "Tambah keterangan foto..." else "Ketik pesan...",
                                        fontSize = 14.sp
                                    )
                                },
                                maxLines = 4,
                                shape = RoundedCornerShape(24.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = EmeraldGreen,
                                    unfocusedBorderColor = Color(0xFFE0E0E0),
                                    focusedContainerColor = ScreenBackground,
                                    unfocusedContainerColor = ScreenBackground
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("chat_input_field")
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            IconButton(
                                onClick = {
                                    lastActivityTime = System.currentTimeMillis()
                                    if (pendingPhotoUri != null && onSendPhotoMessage != null) {
                                        val uriToSend = pendingPhotoUri!!
                                        val caption = inputText
                                        pendingPhotoUri = null
                                        inputText = ""
                                        onSendPhotoMessage(uriToSend, caption)
                                    } else if (inputText.isNotBlank()) {
                                        onSendMessage(inputText)
                                        inputText = ""
                                    }
                                },
                                enabled = !isUploadingPhoto && (pendingPhotoUri != null || inputText.isNotBlank()),
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
                                        contentDescription = "Kirim",
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Fullscreen Chat Photo Dialog
    viewingPhotoUrl?.let { photoUrl ->
        Dialog(
            onDismissRequest = { viewingPhotoUrl = null },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.95f))
                    .clickable { viewingPhotoUrl = null }
            ) {
                AsyncImage(
                    model = photoUrl,
                    contentDescription = "Foto Obrolan Penuh",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                )
                IconButton(
                    onClick = { viewingPhotoUrl = null },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .size(40.dp)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Tutup",
                        tint = Color.White
                    )
                }
            }
        }
    }

    // Modal Bottom Sheet displaying Partner Profile details
    if (showPartnerProfileSheet) {
        PartnerProfileBottomSheet(
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
            onToggleLikeMoment = onToggleLikeMoment,
            onDismiss = { showPartnerProfileSheet = false },
            onSendGreeting = { greeting ->
                onSendMessage(greeting)
                showPartnerProfileSheet = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartnerProfileBottomSheet(
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
    onToggleLikeMoment: ((String) -> Unit)? = null,
    onDismiss: () -> Unit,
    onSendGreeting: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var previewMoment by remember { mutableStateOf<MomentItem?>(null) }
    var showBlockConfirmDialog by remember { mutableStateOf(false) }

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
                    text = "Profil Teman Obrolan",
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
                        contentDescription = "Tutup",
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
                            text = "Pengguna ini berada dalam daftar blokir Anda. Tidak dapat mengirim pesan dan tidak muncul di Orang di Sekitar.",
                            fontSize = 11.5.sp,
                            color = Color(0xFFC62828),
                            lineHeight = 15.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Partner Avatar (Large, with online badge)
            LovyAvatar(
                name = partnerName,
                avatarColorHex = partnerAvatarHex,
                avatarUrl = partnerAvatarUrl,
                size = 80.dp,
                fontSize = 32.sp,
                isOnline = true
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
                    contentDescription = "Terverifikasi",
                    tint = EmeraldGreen,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Gender and Age pill
            val genderText = if (partnerGender == Gender.FEMALE) "♀ Perempuan" else "♂ Laki-laki"
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (partnerGender == Gender.FEMALE) Color(0xFFFCE4EC) else Color(0xFFE3F2FD),
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                Text(
                    text = "$genderText • $partnerAge thn",
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
                    text = "$partnerCity • Jarak $partnerDistance",
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
                            text = "Bio & Status",
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
                        text = "Momen & Foto Terbaru",
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
                        text = "${activeMoments.size} Momen",
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
                            text = "Belum Ada Momen",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeutralDark
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$partnerName belum membagikan foto atau momen yang aktif.",
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
                        PartnerMomentItemCard(
                            moment = moment,
                            onPhotoClick = { previewMoment = moment },
                            onToggleLike = {
                                onToggleLikeMoment?.invoke(moment.id)
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
                    Text("Lanjutkan Chat", fontSize = 13.5.sp, color = NeutralDark)
                }

                Button(
                    onClick = {
                        onSendGreeting("Halo $partnerName! Senang bisa menyapamu 👋✨")
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("btn_send_greeting_partner")
                ) {
                    Text("Sapa Balik 👋", fontSize = 13.5.sp, fontWeight = FontWeight.Bold)
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
                        text = "Buka Blokir Pengguna",
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
                        text = "Blokir Pengguna Ini",
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFD32F2F)
                    )
                }
            }
        }
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
                    text = "Blokir $partnerName?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Text(
                    text = "Pengguna ini tidak akan dapat mengirim pesan lagi kepadamu dan tidak akan muncul di daftar Orang di Sekitar.",
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
                    Text("Blokir", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showBlockConfirmDialog = false },
                    modifier = Modifier.testTag("btn_cancel_block_user")
                ) {
                    Text("Batal", color = NeutralDark)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(18.dp)
        )
    }

    // Photo Preview Lightbox Dialog
    previewMoment?.let { moment ->
        PartnerPhotoPreviewDialog(
            moment = moment,
            partnerAvatarHex = partnerAvatarHex,
            onDismiss = { previewMoment = null },
            onToggleLike = {
                onToggleLikeMoment?.invoke(moment.id)
            }
        )
    }
}

@Composable
fun PartnerMomentItemCard(
    moment: MomentItem,
    onPhotoClick: () -> Unit,
    onToggleLike: () -> Unit,
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
                                contentDescription = "Perbesar Foto",
                                tint = Color.White,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "Foto",
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
                                contentDescription = "Suka",
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

                        // Comments count indicator
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.ChatBubbleOutline,
                                contentDescription = "Komentar",
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
    moment: MomentItem,
    partnerAvatarHex: Long,
    onDismiss: () -> Unit,
    onToggleLike: () -> Unit
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
                            contentDescription = "Tutup",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Center: High-res photo
                if (!moment.imageUrl.isNullOrBlank()) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(vertical = 12.dp)
                    ) {
                        AsyncImage(
                            model = moment.imageUrl,
                            contentDescription = "Foto momen penuh",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
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
                                    text = "${moment.likesCount} Suka",
                                    fontSize = 12.5.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Text(
                                text = "${moment.commentsCount} Komentar",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChatBubble(
    message: ChatMessage,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    onPhotoClick: ((String) -> Unit)? = null
) {
    val timeFormat = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
    val formattedTime = remember(message.timestamp) {
        timeFormat.format(Date(message.timestamp))
    }

    Row(
        horizontalArrangement = if (message.isFromMe) Arrangement.End else Arrangement.Start,
        modifier = modifier.fillMaxWidth()
    ) {
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
                    if (onClick != null) Modifier.clickable { onClick() }
                    else Modifier
                )
                .padding(horizontal = 10.dp, vertical = 8.dp)
        ) {
            Column(horizontalAlignment = if (message.isFromMe) Alignment.End else Alignment.Start) {
                // If message has photo attachment
                if (!message.imageUrl.isNullOrBlank()) {
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
                            contentDescription = "Foto Obrolan",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    if (message.text.isNotBlank() && message.text != "📷 Foto") {
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
                        Icon(
                            imageVector = Icons.Default.DoneAll,
                            contentDescription = null,
                            tint = EmeraldGreen,
                            modifier = Modifier.size(13.dp)
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
