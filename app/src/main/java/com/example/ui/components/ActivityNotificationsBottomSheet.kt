package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ActivityNotification
import com.example.model.NotificationCategory
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.NeutralDark
import com.example.ui.theme.NeutralMedium

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityNotificationsBottomSheet(
    notifications: List<ActivityNotification>,
    onDismiss: () -> Unit,
    onMarkAllAsRead: () -> Unit,
    onClearAll: () -> Unit,
    onNotificationClick: (ActivityNotification) -> Unit,
    language: com.example.util.AppLanguage = com.example.util.AppLanguage.INDONESIAN,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedCategory by remember { mutableStateOf<NotificationCategory?>(null) }

    val filteredList = remember(notifications, selectedCategory) {
        if (selectedCategory == null) notifications
        else notifications.filter { it.category == selectedCategory }
    }

    val unreadCount = remember(notifications) {
        notifications.count { !it.isRead }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp),
        containerColor = Color.White,
        modifier = modifier.testTag("activity_notifications_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .navigationBarsPadding()
        ) {
            // Header: Judul, Badge Jumlah Belum Dibaca, dan Tombol Tutup
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(EmeraldGreen.copy(alpha = 0.12f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = null,
                            tint = EmeraldGreen,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = com.example.util.AppStrings.notificationsTitle(language),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeutralDark
                            )
                            if (unreadCount > 0) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = EmeraldGreen,
                                    modifier = Modifier.padding(horizontal = 2.dp)
                                ) {
                                    Text(
                                        text = com.example.util.AppStrings.notificationsNewBadge(language, unreadCount),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                        Text(
                            text = com.example.util.AppStrings.notificationsSubtitle(language),
                            fontSize = 12.sp,
                            color = NeutralMedium
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = com.example.util.AppStrings.commonClose(language),
                        tint = NeutralMedium,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Bar Aksi Cepat: Tandai Semua Dibaca & Bersihkan
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(
                    onClick = onMarkAllAsRead,
                    enabled = unreadCount > 0,
                    modifier = Modifier.testTag("btn_mark_all_notifications_read")
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = if (unreadCount > 0) EmeraldGreen else NeutralMedium.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = com.example.util.AppStrings.notificationsMarkAllRead(language),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (unreadCount > 0) EmeraldGreen else NeutralMedium.copy(alpha = 0.5f)
                    )
                }

                TextButton(
                    onClick = onClearAll,
                    enabled = notifications.isNotEmpty(),
                    modifier = Modifier.testTag("btn_clear_all_notifications")
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteSweep,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = if (notifications.isNotEmpty()) Color(0xFFE53935) else NeutralMedium.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = com.example.util.AppStrings.notificationsClearAll(language),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (notifications.isNotEmpty()) Color(0xFFE53935) else NeutralMedium.copy(alpha = 0.5f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Kategori Filter Chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    CategoryChip(
                        title = com.example.util.AppStrings.notificationsCategoryAll(language),
                        isSelected = selectedCategory == null,
                        onClick = { selectedCategory = null }
                    )
                }
                item {
                    CategoryChip(
                        title = com.example.util.AppStrings.notificationsCategoryFriends(language),
                        isSelected = selectedCategory == NotificationCategory.FRIEND,
                        onClick = { selectedCategory = NotificationCategory.FRIEND }
                    )
                }
                item {
                    CategoryChip(
                        title = com.example.util.AppStrings.notificationsCategoryRadar(language),
                        isSelected = selectedCategory == NotificationCategory.NEARBY,
                        onClick = { selectedCategory = NotificationCategory.NEARBY }
                    )
                }
                item {
                    CategoryChip(
                        title = com.example.util.AppStrings.bottleTitle(language),
                        isSelected = selectedCategory == NotificationCategory.BOTTLE,
                        onClick = { selectedCategory = NotificationCategory.BOTTLE }
                    )
                }
                item {
                    CategoryChip(
                        title = com.example.util.AppStrings.notificationsCategorySystem(language),
                        isSelected = selectedCategory == NotificationCategory.SYSTEM,
                        onClick = { selectedCategory = NotificationCategory.SYSTEM }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Daftar Notifikasi / Tampilan Kosong
            if (filteredList.isEmpty()) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                        .padding(16.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(EmeraldGreen.copy(alpha = 0.08f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.NotificationsNone,
                                contentDescription = null,
                                tint = EmeraldGreen,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = com.example.util.AppStrings.notificationsEmptyTitle(language),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeutralDark
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = com.example.util.AppStrings.notificationsEmptyDesc(language),
                            fontSize = 12.sp,
                            color = NeutralMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(340.dp)
                ) {
                    items(filteredList, key = { it.id }) { notification ->
                        NotificationCard(
                            notification = notification,
                            language = language,
                            onClick = { onNotificationClick(notification) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tombol Tutup
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .testTag("btn_close_notifications")
            ) {
                Text(
                    text = com.example.util.AppStrings.commonClose(language),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun CategoryChip(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) EmeraldGreen else Color(0xFFF5F5F5),
        border = if (isSelected) null else BorderStroke(1.dp, Color(0xFFE8E8E8)),
        modifier = Modifier.height(32.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 14.dp)
        ) {
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color.White else NeutralDark
            )
        }
    }
}

@Composable
private fun NotificationCard(
    notification: ActivityNotification,
    language: com.example.util.AppLanguage = com.example.util.AppLanguage.INDONESIAN,
    onClick: () -> Unit
) {
    val (categoryColor, categoryIcon, categoryLabel) = when (notification.category) {
        NotificationCategory.FRIEND -> Triple(Color(0xFF00A86B), Icons.Default.PersonAdd, com.example.util.AppStrings.notificationsCategoryFriends(language))
        NotificationCategory.NEARBY -> Triple(Color(0xFF2E7D32), Icons.Default.NearMe, com.example.util.AppStrings.notificationsCategoryRadar(language))
        NotificationCategory.BOTTLE -> Triple(Color(0xFF0288D1), Icons.Default.Waves, com.example.util.AppStrings.bottleTitle(language))
        NotificationCategory.SYSTEM -> Triple(Color(0xFFF57C00), Icons.Default.Info, com.example.util.AppStrings.notificationsCategorySystem(language))
    }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (notification.isRead) Color(0xFFFBFBFB) else Color.White
        ),
        border = BorderStroke(
            1.dp,
            if (notification.isRead) Color(0xFFEEEEEE) else EmeraldGreen.copy(alpha = 0.35f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (notification.isRead) 0.dp else 1.5.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            // Icon Kategori dalam lingkaran warna lembut
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(categoryColor.copy(alpha = 0.12f))
            ) {
                Icon(
                    imageVector = categoryIcon,
                    contentDescription = null,
                    tint = categoryColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = notification.getDisplayTitle(language),
                        fontSize = 13.sp,
                        fontWeight = if (notification.isRead) FontWeight.SemiBold else FontWeight.Bold,
                        color = NeutralDark,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = formatTimeAgo(notification.timestamp, language),
                        fontSize = 10.sp,
                        color = NeutralMedium
                    )
                }

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = notification.getDisplayMessage(language),
                    fontSize = 12.sp,
                    color = if (notification.isRead) NeutralMedium else Color(0xFF424242),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Dot belum dibaca
            if (!notification.isRead) {
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(EmeraldGreen)
                )
            }
        }
    }
}

private fun formatTimeAgo(timestamp: Long, language: com.example.util.AppLanguage): String {
    val diffMs = System.currentTimeMillis() - timestamp
    val seconds = diffMs / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24

    return when {
        minutes < 1 -> com.example.util.AppStrings.timeAgoJustNow(language)
        minutes < 60 -> com.example.util.AppStrings.timeAgoMinutes(language, minutes)
        hours < 24 -> com.example.util.AppStrings.timeAgoHours(language, hours)
        days < 7 -> com.example.util.AppStrings.timeAgoDays(language, days)
        else -> {
            val sdf = java.text.SimpleDateFormat("dd MMM", java.util.Locale.getDefault())
            sdf.format(java.util.Date(timestamp))
        }
    }
}
