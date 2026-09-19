package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Wc
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ChatMessage
import com.example.model.Gender
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
    onPartnerProfileClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var inputText by remember { mutableStateOf("") }
    var showPartnerProfileSheet by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    // Scroll to bottom when new messages arrive
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
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
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Profil",
                                    tint = Color.White.copy(alpha = 0.85f),
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                            Text(
                                text = "Online • Ketuk lihat profil",
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.88f)
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
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(messages, key = { it.id }) { msg ->
                    ChatBubble(message = msg)
                }
            }

            // Quick Greeting Chips
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
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
                        )
                    )
                }
            }

            // Bottom Input Bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = { Text("Ketik pesan...", fontSize = 14.sp) },
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
                        if (inputText.isNotBlank()) {
                            onSendMessage(inputText)
                            inputText = ""
                        }
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = EmeraldGreen,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .testTag("chat_send_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Kirim",
                        modifier = Modifier.size(20.dp)
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
    onDismiss: () -> Unit,
    onSendGreeting: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

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

            Spacer(modifier = Modifier.height(20.dp))

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
        }
    }
}

@Composable
fun ChatBubble(
    message: ChatMessage,
    modifier: Modifier = Modifier
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
                .padding(horizontal = 14.dp, vertical = 9.dp)
        ) {
            Column(horizontalAlignment = if (message.isFromMe) Alignment.End else Alignment.Start) {
                Text(
                    text = message.text,
                    fontSize = 14.sp,
                    color = NeutralDark,
                    lineHeight = 19.sp
                )

                Spacer(modifier = Modifier.height(3.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
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
