package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ChatMessage
import com.example.ui.theme.ChatBubbleOther
import com.example.ui.theme.ChatBubbleSelf
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.NeutralDark
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
    modifier: Modifier = Modifier
) {
    var inputText by remember { mutableStateOf("") }
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color(partnerAvatarHex))
                        ) {
                            Text(
                                text = partnerName.take(1),
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = partnerName,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Online",
                                fontSize = 11.5.sp,
                                color = Color.White.copy(alpha = 0.85f)
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
