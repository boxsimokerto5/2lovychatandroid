package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddComment
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ChatConversation
import com.example.model.Gender
import com.example.ui.components.LovyAvatar
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.NeutralBorder
import com.example.ui.theme.NeutralDark
import com.example.ui.theme.NeutralMedium
import com.example.ui.theme.ScreenBackground
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatsTabScreen(
    conversations: List<ChatConversation>,
    onOpenChat: (ChatConversation) -> Unit,
    onStartNewChat: () -> Unit,
    onDeleteConversations: ((Set<String>) -> Unit)? = null,
    onRefresh: (() -> Unit)? = null,
    language: com.example.util.AppLanguage = com.example.util.AppLanguage.INDONESIAN,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedConversationIds by remember { mutableStateOf(emptySet<String>()) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var viewingAvatarPhoto by remember { mutableStateOf<Pair<String, String>?>(null) } // Pair(name, url)

    val isSelectionMode = selectedConversationIds.isNotEmpty()

    val filtered = remember(conversations, searchQuery) {
        if (searchQuery.isBlank()) conversations
        else conversations.filter {
            it.partnerName.contains(searchQuery, ignoreCase = true) ||
            it.lastMessage.contains(searchQuery, ignoreCase = true)
        }
    }

    Scaffold(
        topBar = {
            if (isSelectionMode) {
                // Top Action Bar saat mode seleksi aktif (Mirip WhatsApp)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(EmeraldGreen)
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                ) {
                    // Tombol Batal Seleksi (X)
                    IconButton(
                        onClick = { selectedConversationIds = emptySet() },
                        modifier = Modifier.size(38.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = com.example.util.AppStrings.btnCancel(language),
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Jumlah obrolan yang dipilih
                    Text(
                        text = "${selectedConversationIds.size}",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )

                    // Tombol Pilih Semua / Hapus Centang Semua
                    IconButton(
                        onClick = {
                            selectedConversationIds = if (selectedConversationIds.size == filtered.size) {
                                emptySet()
                            } else {
                                filtered.map { it.id }.toSet()
                            }
                        },
                        modifier = Modifier.size(38.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.SelectAll,
                            contentDescription = com.example.util.AppStrings.chatsSelectAll(language),
                            tint = Color.White,
                            modifier = Modifier.size(21.dp)
                        )
                    }

                    // Tombol Tong Sampah untuk Hapus
                    IconButton(
                        onClick = { showDeleteConfirmDialog = true },
                        modifier = Modifier.size(38.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = com.example.util.AppStrings.chatsBtnDelete(language),
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            } else {
                // Header normal dengan Search Bar
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(EmeraldGreen)
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = com.example.util.AppStrings.chatsTitle(language),
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        if (onRefresh != null) {
                            IconButton(
                                onClick = onRefresh,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = com.example.util.AppStrings.btnRefreshGps(language),
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Search Bar (Compact & Sleek)
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text(com.example.util.AppStrings.chatsSearchPlaceholder(language), fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = Color.White.copy(alpha = 0.8f),
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White.copy(alpha = 0.2f),
                            unfocusedContainerColor = Color.White.copy(alpha = 0.15f),
                            focusedBorderColor = Color.White.copy(alpha = 0.4f),
                            unfocusedBorderColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(42.dp)
                            .testTag("chats_search_input")
                    )
                }
            }
        },
        floatingActionButton = {
            if (!isSelectionMode) {
                FloatingActionButton(
                    onClick = onStartNewChat,
                    containerColor = EmeraldGreen,
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier.testTag("fab_new_chat")
                ) {
                    Icon(imageVector = Icons.Default.AddComment, contentDescription = com.example.util.AppStrings.chatsStartChat(language))
                }
            }
        },
        containerColor = Color.White,
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        if (filtered.isEmpty()) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(paddingValues)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "💬", fontSize = 42.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = com.example.util.AppStrings.chatsEmptyTitle(language),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeutralDark
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = com.example.util.AppStrings.chatsEmptyDesc(language),
                        fontSize = 12.5.sp,
                        color = NeutralMedium
                    )
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(top = 4.dp, bottom = 0.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(paddingValues)
            ) {
                items(filtered, key = { it.id }) { conv ->
                    val isSelected = selectedConversationIds.contains(conv.id)
                    ChatConversationItem(
                        conversation = conv,
                        isSelected = isSelected,
                        isSelectionMode = isSelectionMode,
                        onAvatarClick = {
                            if (!isSelectionMode && !conv.partnerAvatarUrl.isNullOrBlank()) {
                                viewingAvatarPhoto = Pair(conv.partnerName, conv.partnerAvatarUrl)
                            } else if (isSelectionMode) {
                                selectedConversationIds = if (isSelected) {
                                    selectedConversationIds - conv.id
                                } else {
                                    selectedConversationIds + conv.id
                                }
                            } else {
                                onOpenChat(conv)
                            }
                        },
                        onClick = {
                            if (isSelectionMode) {
                                selectedConversationIds = if (isSelected) {
                                    selectedConversationIds - conv.id
                                } else {
                                    selectedConversationIds + conv.id
                                }
                            } else {
                                onOpenChat(conv)
                            }
                        },
                        onLongClick = {
                            if (!isSelectionMode) {
                                selectedConversationIds = setOf(conv.id)
                            } else {
                                selectedConversationIds = if (isSelected) {
                                    selectedConversationIds - conv.id
                                } else {
                                    selectedConversationIds + conv.id
                                }
                            }
                        }
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 68.dp, end = 14.dp),
                        thickness = 0.5.dp,
                        color = NeutralBorder
                    )
                }
            }
        }
    }

    // Dialog Konfirmasi Hapus Obrolan yang Dipilih
    if (showDeleteConfirmDialog) {
        val count = selectedConversationIds.size
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = null,
                    tint = Color(0xFFE53935),
                    modifier = Modifier.size(28.dp)
                )
            },
            title = {
                Text(
                    text = com.example.util.AppStrings.chatsDeleteConfirmTitle(language, count),
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.5.sp,
                    color = NeutralDark
                )
            },
            text = {
                Text(
                    text = com.example.util.AppStrings.chatsDeleteConfirmDesc(language),
                    fontSize = 13.5.sp,
                    color = NeutralMedium,
                    lineHeight = 18.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteConversations?.invoke(selectedConversationIds)
                        selectedConversationIds = emptySet()
                        showDeleteConfirmDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = com.example.util.AppStrings.chatsBtnDelete(language),
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.5.sp
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteConfirmDialog = false }
                ) {
                    Text(
                        text = com.example.util.AppStrings.btnCancel(language),
                        color = NeutralMedium,
                        fontSize = 13.5.sp
                    )
                }
            }
        )
    }

    // Zoomable Fullscreen Photo Viewer saat foto avatar teman di list obrolan diklik
    viewingAvatarPhoto?.let { (partnerName, photoUrl) ->
        com.example.ui.components.ZoomablePhotoViewerDialog(
            photoUrl = photoUrl,
            title = "${com.example.util.AppStrings.profileTitle(language)} $partnerName",
            onDismiss = { viewingAvatarPhoto = null }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatConversationItem(
    conversation: ChatConversation,
    isSelected: Boolean = false,
    isSelectionMode: Boolean = false,
    language: com.example.util.AppLanguage = com.example.util.AppLanguage.INDONESIAN,
    onAvatarClick: (() -> Unit)? = null,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val timeFormat = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
    val formattedTime = remember(conversation.lastTimestamp) {
        timeFormat.format(Date(conversation.lastTimestamp))
    }

    val itemBackgroundColor = if (isSelected) {
        EmeraldGreen.copy(alpha = 0.12f)
    } else {
        Color.White
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onLongClick?.invoke()
                }
            )
            .background(itemBackgroundColor)
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .testTag("conversation_${conversation.id}")
    ) {
        // Avatar (Compact 44dp) - Klik untuk zoom foto jika ada foto profil
        val hasPhoto = !conversation.partnerAvatarUrl.isNullOrBlank()
        LovyAvatar(
            name = conversation.partnerName,
            avatarColorHex = conversation.partnerAvatarHex,
            avatarUrl = conversation.partnerAvatarUrl,
            size = 44.dp,
            fontSize = 18.sp,
            isOnline = conversation.isOnline,
            modifier = if (hasPhoto && onAvatarClick != null) {
                Modifier
                    .clip(CircleShape)
                    .clickable { onAvatarClick() }
            } else Modifier
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Content
        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    Text(
                        text = conversation.partnerName,
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeutralDark,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )

                    // Badge Gender & Usia (misal: ♀ 22 atau ♂ 25)
                    val badgeColor = if (conversation.partnerGender == Gender.FEMALE) Color(0xFFFF4081) else Color(0xFF1976D2)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(badgeColor)
                            .padding(horizontal = 4.5.dp, vertical = 1.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(1.5.dp)
                        ) {
                            Icon(
                                imageVector = if (conversation.partnerGender == Gender.FEMALE) Icons.Default.Female else Icons.Default.Male,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(10.dp)
                            )
                            Text(
                                text = "${conversation.partnerAge}",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }

                    // Badge Jarak (misal: 📍 95m / 📍 1.2 km)
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = EmeraldGreen.copy(alpha = 0.10f),
                        border = BorderStroke(0.6.dp, EmeraldGreen.copy(alpha = 0.25f))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 4.5.dp, vertical = 1.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = EmeraldGreen,
                                modifier = Modifier.size(10.dp)
                            )
                            Spacer(modifier = Modifier.width(1.5.dp))
                            Text(
                                text = conversation.formattedDistance,
                                color = EmeraldGreen,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = formattedTime,
                    fontSize = 11.sp,
                    color = NeutralMedium
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    if (conversation.lastMessageIsFromMe) {
                        val tickColor = if (conversation.lastMessageIsRead) Color(0xFF34B7F1) else NeutralMedium
                        Icon(
                            imageVector = Icons.Default.DoneAll,
                            contentDescription = if (conversation.lastMessageIsRead) "Dibaca" else "Terkirim",
                            tint = tickColor,
                            modifier = Modifier
                                .size(15.dp)
                                .padding(end = 4.dp)
                        )
                    }
                    Text(
                        text = conversation.lastMessage,
                        fontSize = 13.sp,
                        color = if (conversation.unreadCount > 0) NeutralDark else NeutralMedium,
                        fontWeight = if (conversation.unreadCount > 0) FontWeight.Medium else FontWeight.Normal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (!isSelectionMode && conversation.unreadCount > 0) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(EmeraldGreen)
                    ) {
                        Text(
                            text = "${conversation.unreadCount}",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Kolom centang kecil di sebelah kanan (Muncul saat Mode Seleksi)
        AnimatedVisibility(
            visible = isSelectionMode,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) EmeraldGreen else Color.Transparent)
                    .border(
                        width = 1.5.dp,
                        color = if (isSelected) EmeraldGreen else NeutralBorder,
                        shape = CircleShape
                    )
            ) {
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Terpilih",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

