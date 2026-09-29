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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddComment
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MarkChatUnread
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import android.widget.Toast
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Gender
import com.example.model.NewFriendRequest
import com.example.model.User
import com.example.ui.components.LovyAvatar
import com.example.ui.components.QrCodeScannerDialog
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.NeutralBorder
import com.example.ui.theme.NeutralDark
import com.example.ui.theme.NeutralMedium

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewFriendsScreen(
    requests: List<NewFriendRequest>,
    onBack: () -> Unit,
    onAcceptFriend: (User) -> Unit,
    onIgnoreFriend: (String) -> Unit,
    onOpenChat: (User) -> Unit,
    onNavigateToNearby: () -> Unit,
    searchUserByCode: (String, (User?) -> Unit) -> Unit = { _, callback -> callback(null) },
    myLovyId: String = "",
    myName: String = "",
    myAvatarUrl: String? = null,
    myAvatarColorHex: String? = null,
    language: com.example.util.AppLanguage = com.example.util.AppLanguage.INDONESIAN,
    modifier: Modifier = Modifier
) {
    var viewingAvatarPhoto by remember { mutableStateOf<Pair<String, String>?>(null) }
    var showQrScanner by remember { mutableStateOf(false) }

    val context = LocalContext.current

    if (showQrScanner) {
        QrCodeScannerDialog(
            onDismiss = { showQrScanner = false },
            onUserFound = { user ->
                onAcceptFriend(user)
            },
            searchUserByCode = searchUserByCode,
            isAlreadyFriend = { false },
            myLovyId = myLovyId,
            myName = myName,
            myAvatarUrl = myAvatarUrl,
            myAvatarColorHex = myAvatarColorHex,
            onOpenChatWithUser = { user ->
                onOpenChat(user)
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = com.example.util.AppStrings.newFriendsTitle(language),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color.White
                            )
                            if (requests.isNotEmpty()) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.25f))
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "${requests.size}",
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                        Text(
                            text = com.example.util.AppStrings.newFriendsSubtitle(language),
                            fontSize = 11.5.sp,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("btn_back_new_friends")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = com.example.util.AppStrings.btnBack(language),
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showQrScanner = true },
                        modifier = Modifier.testTag("btn_qr_scanner_new_friends")
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCodeScanner,
                            contentDescription = com.example.util.AppStrings.qrScanPrompt(language),
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = EmeraldGreen)
            )
        },
        containerColor = Color(0xFFF7F9FA),
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header Info Banner
            item {
                Surface(
                    color = Color(0xFFE8F5E9),
                    border = BorderStroke(1.dp, Color(0xFFC8E6C9)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.MarkChatUnread,
                            contentDescription = null,
                            tint = EmeraldGreen,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = com.example.util.AppStrings.newFriendsInfoBanner(language),
                            fontSize = 12.sp,
                            color = NeutralDark,
                            lineHeight = 16.5.sp
                        )
                    }
                }
            }

            if (requests.isEmpty()) {
                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 48.dp, horizontal = 20.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE8F5E9))
                        ) {
                            Icon(
                                imageVector = Icons.Default.PersonSearch,
                                contentDescription = null,
                                tint = EmeraldGreen,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = com.example.util.AppStrings.newFriendsEmptyTitle(language),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeutralDark
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = com.example.util.AppStrings.newFriendsEmptyDesc(language),
                            fontSize = 13.sp,
                            color = NeutralMedium,
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = onNavigateToNearby,
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(com.example.util.AppStrings.newFriendsFindNearbyBtn(language), fontSize = 13.sp)
                            }
                        }
                    }
                }
            } else {
                items(requests, key = { it.id }) { request ->
                    NewFriendRequestCard(
                        request = request,
                        language = language,
                        onAvatarClick = {
                            if (!request.user.avatarUrl.isNullOrBlank()) {
                                viewingAvatarPhoto = Pair(request.user.name, request.user.avatarUrl)
                            }
                        },
                        onAccept = { 
                            onAcceptFriend(request.user) 
                            Toast.makeText(
                                context,
                                com.example.util.AppStrings.friendAcceptedToast(language, request.user.name),
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        onIgnore = { 
                            onIgnoreFriend(request.user.id) 
                            Toast.makeText(
                                context,
                                com.example.util.AppStrings.friendIgnoredToast(language, request.user.name),
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        onOpenChat = { onOpenChat(request.user) }
                    )
                }
            }
        }
    }

    // Zoomable Photo Viewer saat avatar teman baru diklik
    viewingAvatarPhoto?.let { (userName, photoUrl) ->
        com.example.ui.components.ZoomablePhotoViewerDialog(
            photoUrl = photoUrl,
            title = "Foto Profil $userName",
            onDismiss = { viewingAvatarPhoto = null }
        )
    }
}

@Composable
private fun NewFriendRequestCard(
    request: NewFriendRequest,
    language: com.example.util.AppLanguage = com.example.util.AppLanguage.INDONESIAN,
    onAvatarClick: (() -> Unit)? = null,
    onAccept: () -> Unit,
    onIgnore: () -> Unit,
    onOpenChat: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, NeutralBorder.copy(alpha = 0.7f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // User Avatar - Klik untuk zoom jika ada foto
                val hasAvatarPhoto = !request.user.avatarUrl.isNullOrBlank()
                LovyAvatar(
                    avatarUrl = request.user.avatarUrl,
                    avatarColorHex = request.user.avatarColorHex,
                    name = request.user.name,
                    size = 52.dp,
                    isOnline = request.user.isOnline,
                    modifier = if (hasAvatarPhoto && onAvatarClick != null) {
                        Modifier
                            .clip(CircleShape)
                            .clickable { onAvatarClick() }
                    } else Modifier
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = request.user.name.ifBlank { "Pengguna Lovy" },
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = NeutralDark,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.width(6.dp))

                        // Gender & Age pill
                        val isFemale = request.user.gender == Gender.FEMALE
                        Surface(
                            color = if (isFemale) Color(0xFFFCE4EC) else Color(0xFFE3F2FD),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = if (isFemale) "♀ ${request.user.age}" else "♂ ${request.user.age}",
                                color = if (isFemale) Color(0xFFC2185B) else Color(0xFF1976D2),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = "${request.user.formattedDistance} • ${request.user.city}",
                        fontSize = 12.sp,
                        color = NeutralMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Greeting/Message Box
            Surface(
                color = Color(0xFFF5F7F8),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "“${request.greetingMessage.ifBlank { "Halo! Salam kenal ya 👋" }}”",
                        fontSize = 12.5.sp,
                        color = NeutralDark,
                        lineHeight = 17.sp,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onIgnore,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, NeutralBorder),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = NeutralMedium),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("btn_ignore_friend_${request.user.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp),
                        tint = NeutralMedium
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = com.example.util.AppStrings.newFriendsIgnore(language), fontSize = 12.5.sp)
                }

                Spacer(modifier = Modifier.width(8.dp))

                OutlinedButton(
                    onClick = onOpenChat,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, EmeraldGreen.copy(alpha = 0.6f)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = EmeraldGreen),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("btn_open_chat_${request.user.id}")
                ) {
                    Text(text = com.example.util.AppStrings.newFriendsReplyChat(language), fontSize = 12.5.sp, fontWeight = FontWeight.SemiBold)
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = onAccept,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("btn_accept_friend_${request.user.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = com.example.util.AppStrings.newFriendsAccept(language), fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
