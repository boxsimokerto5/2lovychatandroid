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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MomentItem
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.NeutralDark
import com.example.ui.theme.NeutralMedium
import com.example.ui.theme.ScreenBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MomentsScreen(
    moments: List<MomentItem>,
    onBack: () -> Unit,
    onToggleLike: (String) -> Unit,
    onPostMoment: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showPostDialog by remember { mutableStateOf(false) }
    var postText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Momen",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
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
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(moments, key = { it.id }) { item ->
                MomentCard(
                    item = item,
                    onToggleLike = { onToggleLike(item.id) }
                )
            }
        }
    }

    if (showPostDialog) {
        AlertDialog(
            onDismissRequest = { showPostDialog = false },
            title = {
                Text(text = "Bagikan Momen Baru", fontWeight = FontWeight.Bold, fontSize = 17.sp)
            },
            text = {
                OutlinedTextField(
                    value = postText,
                    onValueChange = { postText = it },
                    placeholder = { Text("Apa ceritamu hari ini?") },
                    minLines = 3,
                    maxLines = 5,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (postText.isNotBlank()) {
                            onPostMoment(postText)
                            postText = ""
                            showPostDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen)
                ) {
                    Text("Kirim")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPostDialog = false }) {
                    Text("Batal", color = NeutralMedium)
                }
            }
        )
    }
}

@Composable
fun MomentCard(
    item: MomentItem,
    onToggleLike: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color(item.authorAvatarHex))
                ) {
                    Text(
                        text = item.authorName.take(1),
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = item.authorName,
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeutralDark
                    )
                    Text(
                        text = item.timeAgo,
                        fontSize = 11.5.sp,
                        color = NeutralMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = item.content,
                fontSize = 14.sp,
                color = NeutralDark,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Action row: Like & Comment
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable(onClick = onToggleLike)
                        .padding(vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = if (item.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Suka",
                        tint = if (item.isLiked) Color(0xFFE53935) else NeutralMedium,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${item.likesCount}",
                        fontSize = 12.5.sp,
                        color = if (item.isLiked) Color(0xFFE53935) else NeutralMedium,
                        fontWeight = FontWeight.Medium
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ChatBubbleOutline,
                        contentDescription = "Komentar",
                        tint = NeutralMedium,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${item.commentsCount}",
                        fontSize = 12.5.sp,
                        color = NeutralMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
