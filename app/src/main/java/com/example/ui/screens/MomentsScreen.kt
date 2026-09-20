package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.model.MomentItem
import com.example.ui.components.IronSourceBannerView
import com.example.ui.components.LevelPlayNativeAdCard
import com.example.ui.components.LovyAvatar
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.NeutralBorder
import com.example.ui.theme.NeutralDark
import com.example.ui.theme.NeutralMedium
import com.example.ui.theme.ScreenBackground

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MomentsScreen(
    moments: List<MomentItem>,
    onBack: () -> Unit,
    onToggleLike: (String) -> Unit,
    onPostMoment: (String) -> Unit,
    onPostMomentWithDetails: ((content: String, imageUrl: String?, locationTag: String?) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showPostDialog by remember { mutableStateOf(false) }
    var postText by remember { mutableStateOf("") }
    var postLocation by remember { mutableStateOf("Jakarta Selatan") }
    var selectedPhotoUrl by remember { mutableStateOf<String?>("https://images.unsplash.com/photo-1501339847302-ac426a4a7cbb?auto=format&fit=crop&w=1000&q=80") }
    
    // State for viewing photos fullscreen
    var fullscreenPhotoUrl by remember { mutableStateOf<String?>(null) }

    // Pre-made photo suggestions
    val photoSuggestions = listOf(
        Pair("☕ Kopi", "https://images.unsplash.com/photo-1501339847302-ac426a4a7cbb?auto=format&fit=crop&w=1000&q=80"),
        Pair("📸 Kota", "https://images.unsplash.com/photo-1519501025264-65ba15a82390?auto=format&fit=crop&w=1000&q=80"),
        Pair("🎨 Seni", "https://images.unsplash.com/photo-1513364776144-60967b0f800f?auto=format&fit=crop&w=1000&q=80"),
        Pair("🐱 Kucing", "https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?auto=format&fit=crop&w=1000&q=80"),
        Pair("🏖️ Pantai", "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=1000&q=80")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Momen",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Foto & cerita dari teman sekitarmu",
                            fontSize = 11.5.sp,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("moments_btn_back")
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
        bottomBar = {
            IronSourceBannerView(applyNavigationBarsPadding = true)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showPostDialog = true },
                containerColor = Color(0xFFFB8C00),
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.testTag("fab_post_moment")
            ) {
                Icon(imageVector = Icons.Default.CameraAlt, contentDescription = "Buat Momen")
            }
        },
        containerColor = ScreenBackground,
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Header information strip
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 11.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "✨", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Melihat ${moments.size} momen terbaru di sekitar",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF2E7D32)
                        )
                    }
                }
            }

            moments.forEachIndexed { index, item ->
                item(key = item.id) {
                    MomentCard(
                        item = item,
                        onToggleLike = { onToggleLike(item.id) },
                        onPhotoClick = { url -> fullscreenPhotoUrl = url },
                        onShareClick = {
                            Toast.makeText(context, "Tautan momen disalin!", Toast.LENGTH_SHORT).show()
                        }
                    )
                }

                // Sisipkan Iklan Native langsung setelah postingan ke-1 (index == 0) dan berkala setiap 4 postingan
                if (index == 0 || (index > 0 && (index + 1) % 4 == 0)) {
                    item(key = "native_ad_moments_$index") {
                        LevelPlayNativeAdCard(
                            testTag = "native_ad_moments_$index"
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // Dialog posting moment dengan opsi foto & lokasi
    if (showPostDialog) {
        AlertDialog(
            onDismissRequest = { showPostDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = null,
                        tint = EmeraldGreen,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Bagikan Momen Baru",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = NeutralDark
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = postText,
                        onValueChange = { postText = it },
                        placeholder = { Text("Apa ceritamu hari ini? Ceritakan aktivitasmu...") },
                        minLines = 3,
                        maxLines = 5,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EmeraldGreen,
                            unfocusedBorderColor = NeutralBorder
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = postLocation,
                        onValueChange = { postLocation = it },
                        label = { Text("Lokasi Momen") },
                        leadingIcon = {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = EmeraldGreen, modifier = Modifier.size(18.dp))
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EmeraldGreen,
                            unfocusedBorderColor = NeutralBorder
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = "Pilih Foto Menarik:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NeutralDark
                    )

                    // Photo chips selection
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        photoSuggestions.forEach { (label, url) ->
                            val isSelected = selectedPhotoUrl == url
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) EmeraldGreen else Color(0xFFF1F3F4))
                                    .clickable {
                                        selectedPhotoUrl = if (isSelected) null else url
                                    }
                                    .padding(horizontal = 8.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 11.5.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color.White else NeutralDark
                                )
                            }
                        }
                    }

                    // Photo preview
                    if (!selectedPhotoUrl.isNullOrBlank()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFEEEEEE))
                        ) {
                            AsyncImage(
                                model = selectedPhotoUrl,
                                contentDescription = "Pratinjau Foto",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(6.dp)
                                    .clip(CircleShape)
                                    .background(Color.Black.copy(alpha = 0.5f))
                                    .clickable { selectedPhotoUrl = null }
                                    .padding(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Hapus Foto",
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (postText.isNotBlank()) {
                            if (onPostMomentWithDetails != null) {
                                onPostMomentWithDetails(postText, selectedPhotoUrl, postLocation)
                            } else {
                                onPostMoment(postText)
                            }
                            postText = ""
                            showPostDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Bagikan", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showPostDialog = false }) {
                    Text("Batal", color = NeutralMedium)
                }
            }
        )
    }

    // Fullscreen Photo Modal
    fullscreenPhotoUrl?.let { photoUrl ->
        Dialog(
            onDismissRequest = { fullscreenPhotoUrl = null },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.95f))
                    .clickable { fullscreenPhotoUrl = null }
            ) {
                AsyncImage(
                    model = photoUrl,
                    contentDescription = "Foto Penuh",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                )

                // Close button
                IconButton(
                    onClick = { fullscreenPhotoUrl = null },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(20.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.6f))
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
}

@Composable
fun MomentCard(
    item: MomentItem,
    onToggleLike: () -> Unit,
    onPhotoClick: (String) -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val heartColor by animateColorAsState(
        targetValue = if (item.isLiked) Color(0xFFE53935) else NeutralMedium,
        label = "heartColor"
    )

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("moment_card_${item.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Avatar, Name, Location, Time
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                LovyAvatar(
                    name = item.authorName,
                    avatarColorHex = item.authorAvatarHex,
                    avatarUrl = item.authorAvatarUrl,
                    size = 46.dp,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.authorName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeutralDark,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = item.timeAgo,
                            fontSize = 11.5.sp,
                            color = NeutralMedium
                        )

                        if (!item.locationTag.isNullOrBlank()) {
                            Text(text = "•", fontSize = 11.sp, color = NeutralMedium)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = EmeraldGreen,
                                    modifier = Modifier.size(11.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = item.locationTag,
                                    fontSize = 11.sp,
                                    color = EmeraldGreen,
                                    fontWeight = FontWeight.Medium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Moment Content Text
            Text(
                text = item.content,
                fontSize = 14.5.sp,
                color = NeutralDark,
                lineHeight = 22.sp
            )

            // Large Photo Display (Fitur Utama: Tampilan Foto Lebih Besar & Jelas)
            if (!item.imageUrl.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFFF1F3F4))
                        .clickable { onPhotoClick(item.imageUrl) }
                        .testTag("moment_image_${item.id}")
                ) {
                    AsyncImage(
                        model = item.imageUrl,
                        contentDescription = "Foto Momen ${item.authorName}",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Subtle zoom / fullscreen pill at bottom right
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(10.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.Black.copy(alpha = 0.55f))
                            .padding(horizontal = 9.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Fullscreen,
                                contentDescription = "Perbesar",
                                tint = Color.White,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Perbesar",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            HorizontalDivider(color = NeutralBorder.copy(alpha = 0.6f), thickness = 0.6.dp)

            Spacer(modifier = Modifier.height(8.dp))

            // Action Row: Like, Comment, Share
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Like Button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(onClick = onToggleLike)
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                        .testTag("btn_like_${item.id}")
                ) {
                    Icon(
                        imageVector = if (item.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Suka",
                        tint = heartColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${item.likesCount}",
                        fontSize = 13.sp,
                        color = heartColor,
                        fontWeight = if (item.isLiked) FontWeight.Bold else FontWeight.Medium
                    )
                }

                // Comment Button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { /* Comment feedback */ }
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ChatBubbleOutline,
                        contentDescription = "Komentar",
                        tint = NeutralMedium,
                        modifier = Modifier.size(19.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${item.commentsCount}",
                        fontSize = 13.sp,
                        color = NeutralMedium,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Share Button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(onClick = onShareClick)
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Bagikan",
                        tint = NeutralMedium,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Bagikan",
                        fontSize = 12.5.sp,
                        color = NeutralMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
