package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PersonSearch
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Gender
import com.example.model.User
import com.example.ui.components.LovyAvatar
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
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var friendToDelete by remember { mutableStateOf<User?>(null) }
    var showClearAllDialog by remember { mutableStateOf(false) }
    var viewingAvatarPhoto by remember { mutableStateOf<Pair<String, String>?>(null) }

    // Urutkan daftar teman:
    // 1. Teman Favorit (⭐) SELALU berada di paling atas (bahkan di atas teman online)!
    // 2. Teman yang sedang ONLINE
    // 3. Nama alfabetis
    val sortedAndFiltered = remember(friends, searchQuery) {
        val baseList = if (searchQuery.isBlank()) friends
        else friends.filter { it.name.contains(searchQuery, ignoreCase = true) }

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
                    text = "Teman",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Cari teman...", fontSize = 13.sp, color = Color.White.copy(alpha = 0.7f)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
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
                        .height(48.dp)
                        .testTag("friends_search_input")
                )
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
                        title = "Teman Baru",
                        badge = if (newFriendsCount > 0) newFriendsCount.toString() else null,
                        onClick = onNavigateToNewFriends
                    )
                    HorizontalDivider(modifier = Modifier.padding(start = 72.dp), color = NeutralBorder, thickness = 0.6.dp)

                    FriendShortcutItem(
                        icon = Icons.Default.LocationOn,
                        iconBgColor = Color(0xFFE0F7FA),
                        iconTint = Color(0xFF00ACC1),
                        title = "Cari Teman Sekitar",
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
                        text = "Kontak Saya (${sortedAndFiltered.size})",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeutralMedium
                    )

                    if (sortedAndFiltered.isNotEmpty()) {
                        Text(
                            text = "Hapus Semua",
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
                            text = "Belum Ada Kontak Teman",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeutralDark
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Mulai percakapan dengan menyapa pengguna di sekitar melalui fitur radar untuk menambahkan teman ke kontak.",
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
                            Text("Cari Teman Sekitar", fontSize = 13.sp)
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
                            .padding(start = 16.dp, end = 6.dp, top = 10.dp, bottom = 10.dp)
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

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = user.name,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NeutralDark,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.weight(1f, fill = false)
                                )

                                // Badge Gender & Usia
                                val badgeColor = if (user.gender == Gender.FEMALE) Color(0xFFFF4081) else Color(0xFF1976D2)
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
                                            imageVector = if (user.gender == Gender.FEMALE) Icons.Default.Female else Icons.Default.Male,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(10.dp)
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
                                            text = user.formattedDistance,
                                            color = EmeraldGreen,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }

                                if (user.isFavorite) {
                                    Surface(
                                        color = Color(0xFFFFF8E1),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.Star,
                                                contentDescription = null,
                                                tint = Color(0xFFFFB300),
                                                modifier = Modifier.size(11.dp)
                                            )
                                            Spacer(modifier = Modifier.width(2.dp))
                                            Text(
                                                text = "Favorit",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFFE65100)
                                            )
                                        }
                                    }
                                }
                                if (user.isOnline) {
                                    Surface(
                                        color = Color(0xFFE8F5E9),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = "Online",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF2E7D32),
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = user.bio,
                                fontSize = 12.sp,
                                color = NeutralMedium,
                                maxLines = 1
                            )
                        }

                        // Tombol Bintang Favorit (Pin ke paling atas)
                        IconButton(
                            onClick = { onToggleFavorite(user) },
                            modifier = Modifier
                                .size(40.dp)
                                .testTag("favorite_button_${user.id}")
                        ) {
                            Icon(
                                imageVector = if (user.isFavorite) Icons.Filled.Star else Icons.Outlined.StarBorder,
                                contentDescription = if (user.isFavorite) "Hapus dari Favorit" else "Jadikan Favorit",
                                tint = if (user.isFavorite) Color(0xFFFFB300) else NeutralMedium.copy(alpha = 0.45f),
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        // Tombol Hapus Kontak
                        IconButton(
                            onClick = { friendToDelete = user },
                            modifier = Modifier
                                .size(40.dp)
                                .testTag("delete_friend_${user.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = "Hapus Kontak Teman",
                                tint = NeutralMedium.copy(alpha = 0.55f),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    HorizontalDivider(modifier = Modifier.padding(start = 76.dp), color = NeutralBorder, thickness = 0.6.dp)
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
                Text(text = "Hapus Kontak Teman", fontWeight = FontWeight.Bold, fontSize = 17.sp)
            },
            text = {
                Text(
                    text = "Apakah Anda yakin ingin menghapus \"${target.name}\" dari daftar kontak teman? Kontak tidak akan menyampah di halaman ini lagi.",
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
                    Text("Hapus", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { friendToDelete = null }) {
                    Text("Batal", color = NeutralMedium)
                }
            }
        )
    }

    // Dialog konfirmasi bersihkan semua teman
    if (showClearAllDialog) {
        AlertDialog(
            onDismissRequest = { showClearAllDialog = false },
            title = {
                Text(text = "Bersihkan Semua Teman", fontWeight = FontWeight.Bold, fontSize = 17.sp)
            },
            text = {
                Text(
                    text = "Hapus semua kontak teman dari daftar ini? Anda tetap dapat menyapa dan mencari teman baru kapan saja melalui radar.",
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
                    Text("Hapus Semua", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearAllDialog = false }) {
                    Text("Batal", color = NeutralMedium)
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
