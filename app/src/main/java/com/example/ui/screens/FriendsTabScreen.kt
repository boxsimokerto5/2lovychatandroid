package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.material3.Surface
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Gender
import com.example.model.User
import com.example.ui.components.LovyAvatar
import com.example.ui.components.QrCodeScannerDialog
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.NeutralBorder
import com.example.ui.theme.NeutralDark
import com.example.ui.theme.NeutralMedium
import com.example.ui.theme.ScreenBackground

@Composable
fun FriendsTabScreen(
    friends: List<User>,
    newFriendsCount: Int = 0,
    onSelectFriend: (User) -> Unit,
    onNavigateToNearby: () -> Unit,
    onNavigateToNewFriends: () -> Unit = {},
    onDeleteFriend: (User) -> Unit = {},
    onClearAllFriends: () -> Unit = {},
    onToggleFavorite: (User) -> Unit = {},
    onAddFriend: (User) -> Unit = {},
    searchUserByCode: (String, (User?) -> Unit) -> Unit = { _, callback -> callback(null) },
    myLovyId: String = "",
    myName: String = "",
    myAvatarUrl: String? = null,
    myAvatarColorHex: String? = null,
    language: com.example.util.AppLanguage = com.example.util.AppLanguage.INDONESIAN,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var friendToDelete by remember { mutableStateOf<User?>(null) }
    var showClearAllDialog by remember { mutableStateOf(false) }
    var viewingAvatarPhoto by remember { mutableStateOf<Pair<String, String>?>(null) }
    var showQrScanner by remember { mutableStateOf(false) }

    if (showQrScanner) {
        QrCodeScannerDialog(
            onDismiss = { showQrScanner = false },
            onUserFound = { user ->
                onAddFriend(user)
            },
            searchUserByCode = searchUserByCode,
            isAlreadyFriend = { id -> friends.any { it.id.equals(id, ignoreCase = true) } },
            myLovyId = myLovyId,
            myName = myName,
            myAvatarUrl = myAvatarUrl,
            myAvatarColorHex = myAvatarColorHex,
            onOpenChatWithUser = { user ->
                onSelectFriend(user)
            }
        )
    }

    // Urutkan daftar teman:
    // 1. Teman Favorit (⭐) SELALU berada di paling atas (bahkan di atas teman online)!
    // 2. Teman yang sedang ONLINE
    // 3. Nama alfabetis
    val sortedAndFiltered = remember(friends, searchQuery, myLovyId, myName) {
        val nonSelf = friends.filterNot { 
            (myLovyId.isNotBlank() && it.id.equals(myLovyId, ignoreCase = true)) ||
            (myName.isNotBlank() && it.name.equals(myName, ignoreCase = true)) ||
            it.id.equals("me", ignoreCase = true) ||
            it.id.equals("current_user", ignoreCase = true)
        }
        val baseList = if (searchQuery.isBlank()) nonSelf
        else nonSelf.filter { it.name.contains(searchQuery, ignoreCase = true) }

        baseList.sortedWith(
            compareByDescending<User> { it.isFavorite }
                .thenByDescending { it.isOnline }
                .thenBy { it.name.lowercase() }
        )
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(EmeraldGreen)
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Text(
                    text = com.example.util.AppStrings.friendsTitle(language),
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Tombol Scanner Barcode / QR di sebelah kiri kolom pencarian
                    Surface(
                        onClick = { showQrScanner = true },
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White.copy(alpha = 0.22f),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.35f)),
                        modifier = Modifier
                            .size(48.dp)
                            .testTag("btn_friends_qr_scanner")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.QrCodeScanner,
                                contentDescription = com.example.util.AppStrings.qrScanPrompt(language),
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    // Search Bar (Desain Pill Putih Kontras Tinggi, Teks Terbaca Jelas & Tidak Tenggelam)
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("friends_search_bar_surface"),
                        shape = RoundedCornerShape(22.dp),
                        color = Color.White,
                        shadowElevation = 1.5.dp
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 14.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = EmeraldGreen,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            BasicTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                singleLine = true,
                                textStyle = TextStyle(
                                    color = NeutralDark,
                                    fontSize = 14.5.sp,
                                    fontWeight = FontWeight.Medium,
                                    platformStyle = PlatformTextStyle(includeFontPadding = false),
                                    lineHeight = 20.sp
                                ),
                                cursorBrush = SolidColor(EmeraldGreen),
                                decorationBox = { innerTextField ->
                                    Box(
                                        modifier = Modifier.fillMaxWidth(),
                                        contentAlignment = Alignment.CenterStart
                                    ) {
                                        if (searchQuery.isEmpty()) {
                                            Text(
                                                text = com.example.util.AppStrings.friendsSearchPlaceholder(language),
                                                fontSize = 13.5.sp,
                                                color = Color(0xFF94A3B8), // Slate 400
                                                maxLines = 1,
                                                style = TextStyle(
                                                    platformStyle = PlatformTextStyle(includeFontPadding = false),
                                                    lineHeight = 20.sp
                                                )
                                            )
                                        }
                                        innerTextField()
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("friends_search_input")
                            )
                            if (searchQuery.isNotBlank()) {
                                IconButton(
                                    onClick = { searchQuery = "" },
                                    modifier = Modifier.size(26.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = com.example.util.AppStrings.btnCancel(language),
                                        tint = Color(0xFF64748B),
                                        modifier = Modifier.size(17.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        containerColor = Color.White,
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {
            // Shortcut items
            item {
                Column(modifier = Modifier.background(Color.White)) {
                    FriendShortcutItem(
                        icon = Icons.Default.PersonAdd,
                        iconBgColor = Color(0xFFE8F5E9),
                        iconTint = EmeraldGreen,
                        title = com.example.util.AppStrings.friendsNewFriends(language),
                        badge = if (newFriendsCount > 0) newFriendsCount.toString() else null,
                        onClick = onNavigateToNewFriends
                    )
                    HorizontalDivider(modifier = Modifier.padding(start = 72.dp), color = NeutralBorder, thickness = 0.6.dp)

                    FriendShortcutItem(
                        icon = Icons.Default.LocationOn,
                        iconBgColor = Color(0xFFE0F7FA),
                        iconTint = Color(0xFF00ACC1),
                        title = com.example.util.AppStrings.friendsNearby(language),
                        onClick = onNavigateToNearby
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF7F9FA))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "${com.example.util.AppStrings.friendsMyContacts(language)} (${sortedAndFiltered.size})",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeutralMedium
                    )

                    if (sortedAndFiltered.isNotEmpty()) {
                        Text(
                            text = com.example.util.AppStrings.friendsClearAll(language),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFE53935),
                            modifier = Modifier
                                .clickable { showClearAllDialog = true }
                                .padding(vertical = 2.dp, horizontal = 4.dp)
                                .testTag("clear_all_friends_button")
                        )
                    }
                }
            }

            if (sortedAndFiltered.isEmpty()) {
                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp, horizontal = 24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PersonSearch,
                            contentDescription = null,
                            tint = EmeraldGreen.copy(alpha = 0.6f),
                            modifier = Modifier.size(56.dp)
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = com.example.util.AppStrings.friendsEmptyTitle(language),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeutralDark
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = com.example.util.AppStrings.friendsEmptyDesc(language),
                            fontSize = 13.sp,
                            color = NeutralMedium,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(18.dp))
                        Button(
                            onClick = onNavigateToNearby,
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(com.example.util.AppStrings.friendsFindNearbyBtn(language), fontSize = 13.sp)
                        }
                    }
                }
            } else {
                items(sortedAndFiltered, key = { it.id }) { user ->
                    val itemBg = if (user.isFavorite) Color(0xFFFFFDE7).copy(alpha = 0.45f) else Color.White
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectFriend(user) }
                            .background(itemBg)
                            .padding(start = 16.dp, end = 6.dp, top = 8.dp, bottom = 8.dp)
                            .testTag("friend_item_${user.id}")
                    ) {
                        val hasAvatarPhoto = !user.avatarUrl.isNullOrBlank()
                        LovyAvatar(
                            name = user.name,
                            avatarColorHex = user.avatarColorHex,
                            avatarUrl = user.avatarUrl,
                            size = 46.dp,
                            fontSize = 18.sp,
                            isOnline = user.isOnline,
                            modifier = if (hasAvatarPhoto) {
                                Modifier
                                    .clip(CircleShape)
                                    .clickable { viewingAvatarPhoto = Pair(user.name, user.avatarUrl) }
                            } else Modifier
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 4.dp)
                        ) {
                            // Baris 1: Nama Teman (Ruang luas, tidak terpotong)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = user.name.ifBlank { com.example.util.AppStrings.defaultUserName(language) },
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = NeutralDark,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.weight(1f, fill = false)
                                )

                                if (user.isFavorite) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        imageVector = Icons.Filled.Star,
                                        contentDescription = null,
                                        tint = Color(0xFFFFB300),
                                        modifier = Modifier.size(13.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(3.dp))

                            // Baris 2: Badge Usia & Gender, Badge Jarak, Status Online & Bio (Standar Slim)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.5.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                // Badge Gender & Usia
                                val badgeColor = if (user.gender == Gender.FEMALE) Color(0xFFFF4081) else Color(0xFF1976D2)
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(3.5.dp))
                                        .background(badgeColor)
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(1.5.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (user.gender == Gender.FEMALE) Icons.Default.Female else Icons.Default.Male,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(9.5.dp)
                                        )
                                        Text(
                                            text = "${user.age}",
                                            color = Color.White,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1,
                                            softWrap = false
                                        )
                                    }
                                }

                                // Badge Jarak
                                Surface(
                                    shape = RoundedCornerShape(3.5.dp),
                                    color = EmeraldGreen.copy(alpha = 0.10f),
                                    border = BorderStroke(0.5.dp, EmeraldGreen.copy(alpha = 0.25f))
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.LocationOn,
                                            contentDescription = null,
                                            tint = EmeraldGreen,
                                            modifier = Modifier.size(9.5.dp)
                                        )
                                        Spacer(modifier = Modifier.width(1.5.dp))
                                        Text(
                                            text = user.formattedDistance,
                                            color = EmeraldGreen,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }

                                // Badge Status Online
                                if (user.isOnline) {
                                    Surface(
                                        color = Color(0xFFE8F5E9),
                                        shape = RoundedCornerShape(3.5.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(5.dp)
                                                    .clip(CircleShape)
                                                    .background(Color(0xFF2E7D32))
                                            )
                                            Spacer(modifier = Modifier.width(2.5.dp))
                                            Text(
                                                text = com.example.util.AppStrings.friendsOnline(language),
                                                fontSize = 9.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF2E7D32)
                                            )
                                        }
                                    }
                                }

                                // Bio status singkat
                                if (user.bio.isNotBlank()) {
                                    Text(
                                        text = "•",
                                        fontSize = 10.sp,
                                        color = NeutralMedium.copy(alpha = 0.4f)
                                    )
                                    Text(
                                        text = user.bio,
                                        fontSize = 11.sp,
                                        color = NeutralMedium,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f, fill = false)
                                    )
                                }
                            }
                        }

                        // Tombol Bintang Favorit (Pin ke paling atas) - Slim
                        IconButton(
                            onClick = { onToggleFavorite(user) },
                            modifier = Modifier
                                .size(34.dp)
                                .testTag("favorite_button_${user.id}")
                        ) {
                            Icon(
                                imageVector = if (user.isFavorite) Icons.Filled.Star else Icons.Outlined.StarBorder,
                                contentDescription = if (user.isFavorite) com.example.util.AppStrings.friendsRemoveFavorite(language) else com.example.util.AppStrings.friendsAddFavorite(language),
                                tint = if (user.isFavorite) Color(0xFFFFB300) else NeutralMedium.copy(alpha = 0.40f),
                                modifier = Modifier.size(19.dp)
                            )
                        }

                        // Tombol Hapus Kontak - Slim
                        IconButton(
                            onClick = { friendToDelete = user },
                            modifier = Modifier
                                .size(34.dp)
                                .testTag("delete_friend_${user.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = com.example.util.AppStrings.friendsDeleteFriendDialogTitle(language),
                                tint = NeutralMedium.copy(alpha = 0.45f),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    HorizontalDivider(modifier = Modifier.padding(start = 74.dp), color = NeutralBorder, thickness = 0.6.dp)
                }
            }
        }
    }

    // Dialog konfirmasi hapus teman per individu
    if (friendToDelete != null) {
        val target = friendToDelete!!
        AlertDialog(
            onDismissRequest = { friendToDelete = null },
            title = {
                Text(text = com.example.util.AppStrings.friendsDeleteFriendDialogTitle(language), fontWeight = FontWeight.Bold, fontSize = 17.sp)
            },
            text = {
                Text(
                    text = com.example.util.AppStrings.friendsDeleteFriendDialogMessage(language, target.name),
                    fontSize = 14.sp,
                    color = NeutralDark,
                    lineHeight = 20.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteFriend(target)
                        friendToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(com.example.util.AppStrings.btnDelete(language), color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { friendToDelete = null }) {
                    Text(com.example.util.AppStrings.btnCancel(language), color = NeutralMedium)
                }
            }
        )
    }

    // Dialog konfirmasi bersihkan semua teman
    if (showClearAllDialog) {
        AlertDialog(
            onDismissRequest = { showClearAllDialog = false },
            title = {
                Text(text = com.example.util.AppStrings.friendsClearAllDialogTitle(language), fontWeight = FontWeight.Bold, fontSize = 17.sp)
            },
            text = {
                Text(
                    text = com.example.util.AppStrings.friendsClearAllDialogMessage(language),
                    fontSize = 14.sp,
                    color = NeutralDark,
                    lineHeight = 20.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onClearAllFriends()
                        showClearAllDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(com.example.util.AppStrings.friendsClearAll(language), color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearAllDialog = false }) {
                    Text(com.example.util.AppStrings.btnCancel(language), color = NeutralMedium)
                }
            }
        )
    }

    // Zoomable Photo Viewer saat foto avatar teman di daftar kontak diklik
    viewingAvatarPhoto?.let { (friendName, photoUrl) ->
        com.example.ui.components.ZoomablePhotoViewerDialog(
            photoUrl = photoUrl,
            title = "Foto Profil $friendName",
            onDismiss = { viewingAvatarPhoto = null }
        )
    }
}

@Composable
fun FriendShortcutItem(
    icon: ImageVector,
    iconBgColor: Color,
    iconTint: Color,
    title: String,
    badge: String? = null,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 13.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(iconBgColor)
        ) {
            Icon(imageVector = icon, contentDescription = title, tint = iconTint, modifier = Modifier.size(22.dp))
        }

        Spacer(modifier = Modifier.width(14.dp))

        Text(
            text = title,
            fontSize = 14.5.sp,
            fontWeight = FontWeight.SemiBold,
            color = NeutralDark,
            modifier = Modifier.weight(1f)
        )

        if (badge != null) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE53935))
            ) {
                Text(text = badge, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
