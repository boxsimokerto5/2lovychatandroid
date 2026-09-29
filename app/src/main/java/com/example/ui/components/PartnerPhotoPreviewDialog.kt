package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.model.MomentItem
import com.example.ui.theme.EmeraldGreen
import com.example.util.AppLanguage
import com.example.util.AppStrings
import kotlinx.coroutines.launch

/**
 * Dialog Pratinjau Foto Momen Teman yang Sempurna dengan Fitur Carousel/Swipe:
 * - Geser ke kiri & kanan untuk berpindah antar-postingan foto teman dengan mulus.
 * - Indikator urutan foto (contoh: "Foto 2 dari 5" / titik indikator).
 * - Judul, waktu, lokasi, caption, serta jumlah Suka & Komentar berganti otomatis sesuai foto yang aktif.
 * - Zoom 2 jari (pinch-to-zoom) & double-tap zoom tetap aktif saat foto sedang diam.
 */
@Composable
fun PartnerPhotoPreviewDialog(
    language: AppLanguage = AppLanguage.INDONESIAN,
    moments: List<MomentItem>,
    initialMomentId: String? = null,
    partnerAvatarHex: Long = 0xFF00A86B,
    likedMoments: Set<String> = emptySet(),
    momentCommentsCount: Map<String, Int> = emptyMap(),
    onDismiss: () -> Unit,
    onToggleLike: (String) -> Unit,
    onCommentClick: ((MomentItem) -> Unit)? = null
) {
    val photoMoments = remember(moments) {
        moments.filter { !it.imageUrl.isNullOrBlank() }
    }

    if (photoMoments.isEmpty()) {
        LaunchedEffect(Unit) { onDismiss() }
        return
    }

    val initialIndex = remember(photoMoments, initialMomentId) {
        val found = photoMoments.indexOfFirst { it.id == initialMomentId }
        if (found >= 0) found else 0
    }

    val pagerState = rememberPagerState(
        initialPage = initialIndex,
        pageCount = { photoMoments.size }
    )
    val coroutineScope = rememberCoroutineScope()

    val currentMoment = photoMoments.getOrNull(pagerState.currentPage) ?: photoMoments.first()
    val isCurrentLiked = currentMoment.id in likedMoments || currentMoment.isLiked
    val currentLikesCount = if (isCurrentLiked && currentMoment.id in likedMoments && !currentMoment.isLiked) {
        currentMoment.likesCount + 1
    } else if (!isCurrentLiked && currentMoment.isLiked) {
        (currentMoment.likesCount - 1).coerceAtLeast(0)
    } else {
        currentMoment.likesCount
    }
    val currentCommentsCount = maxOf(
        currentMoment.commentsCount,
        momentCommentsCount[currentMoment.id] ?: 0
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.94f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Header Bar
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        LovyAvatar(
                            name = currentMoment.authorName,
                            avatarColorHex = partnerAvatarHex,
                            avatarUrl = currentMoment.authorAvatarUrl,
                            size = 38.dp,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = currentMoment.authorName.ifBlank { "Teman" },
                                    fontSize = 15.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                if (photoMoments.size > 1) {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = Color.White.copy(alpha = 0.22f)
                                    ) {
                                        Text(
                                            text = "${pagerState.currentPage + 1}/${photoMoments.size}",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF69F0AE),
                                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                            Text(
                                text = currentMoment.timeAgo,
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.75f)
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = AppStrings.commonClose(language),
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Center: Swipeable Horizontal Pager for Friend's Photos
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        val pageMoment = photoMoments[page]
                        var scale by remember { mutableFloatStateOf(1f) }
                        var offset by remember { mutableStateOf(Offset.Zero) }

                        LaunchedEffect(pagerState.currentPage) {
                            if (pagerState.currentPage != page) {
                                scale = 1f
                                offset = Offset.Zero
                            }
                        }

                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 4.dp)
                                .pointerInput(page) {
                                    detectTapGestures(
                                        onDoubleTap = { tapOffset ->
                                            if (scale > 1.2f) {
                                                scale = 1f
                                                offset = Offset.Zero
                                            } else {
                                                scale = 2.5f
                                                offset = Offset(
                                                    x = (size.width / 2f - tapOffset.x) * 1.5f,
                                                    y = (size.height / 2f - tapOffset.y) * 1.5f
                                                )
                                            }
                                        }
                                    )
                                }
                                .then(
                                    if (scale > 1.05f) {
                                        Modifier.pointerInput(page) {
                                            detectTransformGestures { _, pan, zoom, _ ->
                                                val newScale = (scale * zoom).coerceIn(1f, 4.5f)
                                                scale = newScale
                                                if (newScale > 1.05f) {
                                                    val maxOffsetX = (size.width * (newScale - 1f)) / 2f
                                                    val maxOffsetY = (size.height * (newScale - 1f)) / 2f
                                                    offset = Offset(
                                                        x = (offset.x + pan.x).coerceIn(-maxOffsetX, maxOffsetX),
                                                        y = (offset.y + pan.y).coerceIn(-maxOffsetY, maxOffsetY)
                                                    )
                                                } else {
                                                    scale = 1f
                                                    offset = Offset.Zero
                                                }
                                            }
                                        }
                                    } else Modifier
                                )
                        ) {
                            AsyncImage(
                                model = pageMoment.imageUrl,
                                contentDescription = "Foto momen ${page + 1}",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .graphicsLayer(
                                        scaleX = scale,
                                        scaleY = scale,
                                        translationX = offset.x,
                                        translationY = offset.y
                                    )
                            )
                        }
                    }

                    // Floating Previous Arrow
                    if (photoMoments.size > 1 && pagerState.currentPage > 0) {
                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                }
                            },
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(start = 4.dp)
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.55f))
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Foto sebelumnya",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    // Floating Next Arrow
                    if (photoMoments.size > 1 && pagerState.currentPage < photoMoments.size - 1) {
                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            },
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = 4.dp)
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.55f))
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Foto berikutnya",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                // Indicator Dots jika foto > 1
                if (photoMoments.size > 1) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        repeat(photoMoments.size) { index ->
                            val isSelected = pagerState.currentPage == index
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 3.dp)
                                    .size(width = if (isSelected) 18.dp else 6.dp, height = 6.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(if (isSelected) Color(0xFF00E676) else Color.White.copy(alpha = 0.35f))
                            )
                        }
                    }
                }

                // Bottom: Location, Caption, Likes & Comments Card
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFF1E1E1E),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .clickable(enabled = false) {}
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        if (!currentMoment.locationTag.isNullOrBlank()) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(bottom = 6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = EmeraldGreen,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = currentMoment.locationTag,
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.85f)
                                )
                            }
                        }

                        if (currentMoment.content.isNotBlank()) {
                            Text(
                                text = currentMoment.content,
                                fontSize = 14.sp,
                                lineHeight = 20.sp,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Tombol Suka untuk foto yang aktif
                            Button(
                                onClick = { onToggleLike(currentMoment.id) },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isCurrentLiked) Color(0xFFE91E63) else Color.White.copy(alpha = 0.15f)
                                ),
                                modifier = Modifier.height(36.dp)
                            ) {
                                Icon(
                                    imageVector = if (isCurrentLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = AppStrings.partnerMomentLikesCount(language, currentLikesCount),
                                    fontSize = 12.5.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            // Tombol Komentar untuk foto yang aktif
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .then(
                                        if (onCommentClick != null) Modifier.clickable { onCommentClick(currentMoment) }
                                        else Modifier
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ChatBubbleOutline,
                                    contentDescription = null,
                                    tint = Color.White.copy(alpha = 0.85f),
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = AppStrings.partnerMomentCommentsCount(language, currentCommentsCount),
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.85f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Overload pendukung untuk kompatibilitas panggilan dengan parameter single moment.
 */
@Composable
fun PartnerPhotoPreviewDialog(
    language: AppLanguage = AppLanguage.INDONESIAN,
    moment: MomentItem,
    partnerAvatarHex: Long = 0xFF00A86B,
    allPartnerMoments: List<MomentItem> = emptyList(),
    likedMoments: Set<String> = emptySet(),
    momentCommentsCount: Map<String, Int> = emptyMap(),
    onDismiss: () -> Unit,
    onToggleLike: (() -> Unit)? = null,
    onCommentClick: (() -> Unit)? = null
) {
    val candidateMoments = if (allPartnerMoments.isNotEmpty()) allPartnerMoments else listOf(moment)
    PartnerPhotoPreviewDialog(
        language = language,
        moments = candidateMoments,
        initialMomentId = moment.id,
        partnerAvatarHex = partnerAvatarHex,
        likedMoments = likedMoments,
        momentCommentsCount = momentCommentsCount,
        onDismiss = onDismiss,
        onToggleLike = { _ -> onToggleLike?.invoke() },
        onCommentClick = { _ -> onCommentClick?.invoke() }
    )
}
