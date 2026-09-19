package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Phishing
import androidx.compose.material.icons.filled.Water
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BottleMessage
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.NeutralDark
import com.example.ui.theme.NeutralMedium
import com.example.ui.theme.ScreenBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottleScreen(
    oceanBottles: List<BottleMessage>,
    myBottles: List<BottleMessage>,
    fishedBottle: BottleMessage?,
    isFishing: Boolean,
    onBack: () -> Unit,
    onThrowBottle: (String) -> Unit,
    onFishBottle: () -> Unit,
    onDismissFishedBottle: () -> Unit,
    onReplyBottle: (BottleMessage) -> Unit,
    modifier: Modifier = Modifier
) {
    var showThrowDialog by remember { mutableStateOf(false) }
    var throwMessageText by remember { mutableStateOf("") }
    var selectedTab by remember { mutableIntStateOf(0) }

    // Wave floating animation
    val infiniteTransition = rememberInfiniteTransition(label = "wave_anim")
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "wave_y"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Pesan dalam Botol",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("bottle_btn_back")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF00838F) // Deep ocean cyan
                )
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
            // Ocean Hero Canvas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF00838F),
                                Color(0xFF006064),
                                Color(0xFF004D40)
                            )
                        )
                    )
                    .padding(16.dp)
            ) {
                // Background Waves decoration
                Icon(
                    imageVector = Icons.Default.Waves,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.15f),
                    modifier = Modifier
                        .size(160.dp)
                        .align(Alignment.BottomEnd)
                        .offset(y = 20.dp)
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Lautan Misteri Lovy Chat",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Lempar kata hatimu atau pancing pesan dari sahabat baru",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Floating Bottle Graphic
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .offset(y = waveOffset.dp)
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f))
                            .border(2.dp, Color.White.copy(alpha = 0.4f), CircleShape)
                    ) {
                        Text(
                            text = "🍾",
                            fontSize = 32.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Action Buttons Row: Lempar & Pancing
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        // Throw button
                        Button(
                            onClick = { showThrowDialog = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = EmeraldGreen
                            ),
                            shape = RoundedCornerShape(25.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_throw_bottle")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Lempar", fontSize = 13.5.sp, fontWeight = FontWeight.Bold)
                        }

                        // Fish button
                        Button(
                            onClick = onFishBottle,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF00BCD4)
                            ),
                            shape = RoundedCornerShape(25.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_fish_bottle")
                        ) {
                            if (isFishing) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Phishing,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (isFishing) "Mancing..." else "Pancing", fontSize = 13.5.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Tabs: Botol Lautan vs Botol Saya
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = EmeraldGreen
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Lautan Pesan (${oceanBottles.size})", fontWeight = FontWeight.SemiBold) },
                    modifier = Modifier.testTag("tab_ocean_bottles")
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Botol Saya (${myBottles.size})", fontWeight = FontWeight.SemiBold) },
                    modifier = Modifier.testTag("tab_my_bottles")
                )
            }

            // Content List
            val displayList = if (selectedTab == 0) oceanBottles else myBottles

            if (displayList.isEmpty()) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "🍾", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (selectedTab == 0) "Belum ada botol di sekitar" else "Kamu belum pernah melempar botol",
                            fontSize = 14.sp,
                            color = NeutralMedium,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Tulis kata-kata manismu dan lempar ke lautan!",
                            fontSize = 12.sp,
                            color = NeutralMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(displayList, key = { it.id }) { bottle ->
                        BottleCardItem(
                            bottle = bottle,
                            onReply = { onReplyBottle(bottle) }
                        )
                    }
                }
            }
        }
    }

    // Dialog: Throw bottle
    if (showThrowDialog) {
        AlertDialog(
            onDismissRequest = { showThrowDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🍾", fontSize = 22.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Lempar Pesan Botol",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            text = {
                Column {
                    Text(
                        text = "Tulis pesan, curhatan, atau salam yang ingin kamu hanyutkan ke lautan:",
                        fontSize = 12.5.sp,
                        color = NeutralMedium
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = throwMessageText,
                        onValueChange = { throwMessageText = it },
                        placeholder = { Text("Contoh: Semangat buat kamu yang lagi berjuang hari ini!") },
                        minLines = 3,
                        maxLines = 5,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_throw_bottle")
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (throwMessageText.isNotBlank()) {
                            onThrowBottle(throwMessageText)
                            throwMessageText = ""
                            showThrowDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                    modifier = Modifier.testTag("btn_confirm_throw")
                ) {
                    Text("Hanyutkan")
                }
            },
            dismissButton = {
                TextButton(onClick = { showThrowDialog = false }) {
                    Text("Batal", color = NeutralMedium)
                }
            }
        )
    }

    // Dialog: Fished Bottle Reveal
    fishedBottle?.let { bottle ->
        AlertDialog(
            onDismissRequest = onDismissFishedBottle,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🎣", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Botol Berhasil Dipancing!",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00838F)
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFE0F7FA))
                        .padding(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(bottle.avatarHex))
                        ) {
                            Text(
                                text = bottle.senderName.take(1),
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = bottle.senderName,
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeutralDark
                            )
                            Text(
                                text = "Ditemukan di ${bottle.locationHint}",
                                fontSize = 11.sp,
                                color = NeutralMedium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "\"${bottle.content}\"",
                        fontSize = 14.sp,
                        color = NeutralDark,
                        fontStyle = FontStyle.Italic,
                        lineHeight = 20.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onReplyBottle(bottle)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                    modifier = Modifier.testTag("btn_reply_fished_bottle")
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Balas di Obrolan")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismissFishedBottle,
                    modifier = Modifier.testTag("btn_dismiss_fished_bottle")
                ) {
                    Text("Lempar Kembali", color = NeutralMedium)
                }
            }
        )
    }
}

@Composable
fun BottleCardItem(
    bottle: BottleMessage,
    onReply: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("bottle_card_${bottle.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(bottle.avatarHex))
                ) {
                    Text(
                        text = bottle.senderName.take(1),
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = bottle.senderName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeutralDark
                    )
                    Text(
                        text = "${bottle.locationHint} • Lautan Lovy",
                        fontSize = 11.5.sp,
                        color = NeutralMedium
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFE0F7FA))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "🌊 Botol",
                        fontSize = 11.sp,
                        color = Color(0xFF00838F),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = bottle.content,
                fontSize = 13.5.sp,
                color = NeutralDark,
                lineHeight = 18.sp
            )

            if (!bottle.isFromMe) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(
                        onClick = onReply,
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        modifier = Modifier.testTag("btn_reply_bottle_${bottle.id}")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Chat,
                            contentDescription = null,
                            tint = EmeraldGreen,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Balas Pesan",
                            fontSize = 12.5.sp,
                            color = EmeraldGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
