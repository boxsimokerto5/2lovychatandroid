package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.WavingHand
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.model.Gender
import com.example.model.MomentItem
import com.example.model.User
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.NeutralBorder
import com.example.ui.theme.NeutralDark
import com.example.ui.theme.NeutralMedium
import com.example.ui.theme.ScreenBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileBottomSheet(
    user: User,
    existingMoments: List<MomentItem> = emptyList(),
    isBlocked: Boolean = false,
    momentComments: Map<String, List<com.example.model.MomentComment>> = emptyMap(),
    onAddComment: ((momentId: String, text: String) -> Unit)? = null,
    onToggleLikeMoment: ((String) -> Unit)? = null,
    language: com.example.util.AppLanguage = com.example.util.AppLanguage.INDONESIAN,
    onDismiss: () -> Unit,
    onSayHi: (User) -> Unit,
    onBlockUser: (() -> Unit)? = null,
    onUnblockUser: (() -> Unit)? = null,
    onReportUser: ((reason: String, notes: String, alsoBlock: Boolean) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var previewMoment by remember { mutableStateOf<MomentItem?>(null) }
    var viewingAvatarPhoto by remember { mutableStateOf<String?>(null) }
    var activeMomentForComments by remember { mutableStateOf<MomentItem?>(null) }
    var showBlockConfirmDialog by remember { mutableStateOf(false) }
    var showReportDialog by remember { mutableStateOf(false) }

    // Dapatkan momen pengguna (baik dari daftar asli ataupun foto momen yang dipersonalisasi)
    val userMoments = remember(user, existingMoments) {
        getUserMoments(user, existingMoments)
    }

    // Dukungan toggle like interaktif secara lokal di sheet
    val likedState = remember { mutableStateMapOf<String, Boolean>() }
    val likesCountDelta = remember { mutableStateMapOf<String, Int>() }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = Color.White,
        modifier = modifier.testTag("user_profile_bottom_sheet_${user.id}")
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header Bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = com.example.util.AppStrings.userProfileSheetTitle(language),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeutralDark
                )
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(32.dp).testTag("btn_close_profile_sheet")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = com.example.util.AppStrings.commonClose(language),
                        tint = NeutralMedium,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            if (isBlocked) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    color = Color(0xFFFFEBEE),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, Color(0xFFFFCDD2)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Block,
                            contentDescription = null,
                            tint = Color(0xFFD32F2F),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = com.example.util.AppStrings.userProfileBlockedBanner(language),
                            fontSize = 12.sp,
                            color = Color(0xFFC62828)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Avatar Besar dengan Status Online (Bisa diklik untuk zoom jika ada foto)
            val hasAvatarPhoto = !user.avatarUrl.isNullOrBlank()
            LovyAvatar(
                name = user.name,
                avatarColorHex = user.avatarColorHex,
                avatarUrl = user.avatarUrl,
                size = 88.dp,
                fontSize = 36.sp,
                isOnline = user.isOnline,
                modifier = if (hasAvatarPhoto) {
                    Modifier
                        .clip(CircleShape)
                        .clickable { viewingAvatarPhoto = user.avatarUrl }
                } else Modifier
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Nama & Centang Terverifikasi
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = user.name.ifBlank { "Pengguna Lovy" },
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeutralDark
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = com.example.util.AppStrings.commonVerified(language),
                    tint = EmeraldGreen,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Badge Gender & Usia yang Proporsional & Rapi (Tidak Terjepit)
            val badgeColor = if (user.gender == Gender.FEMALE) Color(0xFFFF4081) else Color(0xFF1976D2)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = badgeColor
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Icon(
                            imageVector = if (user.gender == Gender.FEMALE) Icons.Default.Female else Icons.Default.Male,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "${com.example.util.AppStrings.genderLabel(language, user.gender)} ${com.example.util.AppStrings.ageYears(language, user.age)}",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }

                // Status Online badge
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (user.isOnline) Color(0xFFE8F5E9) else Color(0xFFF5F5F5)
                ) {
                    Text(
                        text = if (user.isOnline) com.example.util.AppStrings.statusOnline(language) else com.example.util.AppStrings.statusOffline(language),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (user.isOnline) EmeraldGreen else NeutralMedium,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Jarak & Kota
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = EmeraldGreen,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = com.example.util.AppStrings.locationDistance(language, user.city, user.formattedDistance),
                    fontSize = 13.sp,
                    color = NeutralMedium,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Kartu Bio & Status
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = ScreenBackground),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.FormatQuote,
                            contentDescription = null,
                            tint = EmeraldGreen,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = com.example.util.AppStrings.sheetBioTitle(language),
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = NeutralMedium
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = user.bio.ifBlank { com.example.util.AppStrings.momentsDefaultUserBio(language) },
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = NeutralDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tombol Aksi Utama: Sapa Pengguna
            Button(
                onClick = {
                    onDismiss()
                    onSayHi(user)
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("btn_sheet_say_hi_${user.id}")
            ) {
                Icon(
                    imageVector = Icons.Default.WavingHand,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = com.example.util.AppStrings.userProfileStartChat(language),
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // SECTION: Foto Momen Pengguna
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.PhotoLibrary,
                        contentDescription = null,
                        tint = EmeraldGreen,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = com.example.util.AppStrings.userProfileRecentMoments(language),
                        fontSize = 15.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeutralDark
                    )
                }

                if (userMoments.isNotEmpty()) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = EmeraldGreen.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = com.example.util.AppStrings.userProfileMomentsCount(language, userMoments.size),
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = EmeraldGreen,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Daftar Foto Momen atau Empty State
            if (userMoments.isEmpty()) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFF9FBF9),
                    border = BorderStroke(1.dp, Color(0xFFE8F5E9)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp, horizontal = 16.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = EmeraldGreen.copy(alpha = 0.10f),
                            modifier = Modifier.size(48.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.PhotoLibrary,
                                    contentDescription = null,
                                    tint = EmeraldGreen,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = com.example.util.AppStrings.userProfileNoMoments(language),
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeutralDark
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = com.example.util.AppStrings.userProfileNoMomentsDesc(language, user.name),
                            fontSize = 12.5.sp,
                            color = NeutralMedium,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    userMoments.forEach { moment ->
                        val isLocallyLiked = likedState[moment.id] ?: moment.isLiked
                        val currentLikes = moment.likesCount + (likesCountDelta[moment.id] ?: 0)
                        val commentsForThis = momentComments[moment.id]
                        val accurateCommentsCount = maxOf(moment.commentsCount, commentsForThis?.size ?: 0)
                        val displayMoment = if (moment.commentsCount != accurateCommentsCount || moment.likesCount != currentLikes || moment.isLiked != isLocallyLiked) {
                            moment.copy(commentsCount = accurateCommentsCount, likesCount = currentLikes, isLiked = isLocallyLiked)
                        } else moment

                        UserProfileMomentCard(
                            moment = displayMoment,
                            isLiked = isLocallyLiked,
                            likesCount = currentLikes,
                            commentsCount = accurateCommentsCount,
                            language = language,
                            onPhotoClick = { previewMoment = displayMoment },
                            onToggleLike = {
                                val nextState = !isLocallyLiked
                                likedState[moment.id] = nextState
                                val delta = if (nextState) 1 else -1
                                likesCountDelta[moment.id] = (likesCountDelta[moment.id] ?: 0) + delta
                                onToggleLikeMoment?.invoke(moment.id)
                            },
                            onCommentClick = {
                                activeMomentForComments = displayMoment
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Tombol Blokir / Buka Blokir
            if (isBlocked) {
                OutlinedButton(
                    onClick = {
                        onUnblockUser?.invoke()
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = EmeraldGreen),
                    border = BorderStroke(1.dp, EmeraldGreen.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LockOpen,
                        contentDescription = null,
                        tint = EmeraldGreen,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(com.example.util.AppStrings.userProfileUnblockBtn(language), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
            } else {
                OutlinedButton(
                    onClick = { showBlockConfirmDialog = true },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD32F2F)),
                    border = BorderStroke(1.dp, Color(0xFFFFCDD2)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Block,
                        contentDescription = null,
                        tint = Color(0xFFD32F2F),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(com.example.util.AppStrings.userProfileBlockBtn(language), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }

                Spacer(modifier = Modifier.height(10.dp))
                OutlinedButton(
                    onClick = { showReportDialog = true },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFC62828)),
                    border = BorderStroke(1.dp, Color(0xFFFFCDD2)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("btn_report_user_${user.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Flag,
                        contentDescription = null,
                        tint = Color(0xFFC62828),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(com.example.util.AppStrings.userProfileReportBtn(language), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }

    if (showReportDialog) {
        ReportDialog(
            targetName = user.name,
            reportType = ReportType.USER,
            language = language,
            onDismiss = { showReportDialog = false },
            onSubmitReport = { reason, notes, alsoBlock ->
                onReportUser?.invoke(reason, notes, alsoBlock)
                onDismiss()
            }
        )
    }

    // Dialog Konfirmasi Blokir
    if (showBlockConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showBlockConfirmDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Block,
                    contentDescription = null,
                    tint = Color(0xFFD32F2F),
                    modifier = Modifier.size(32.dp)
                )
            },
            title = {
                Text(
                    text = com.example.util.AppStrings.userProfileBlockConfirmTitle(language, user.name),
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Text(
                    text = com.example.util.AppStrings.userProfileBlockConfirmDesc(language),
                    fontSize = 13.sp,
                    color = NeutralMedium,
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showBlockConfirmDialog = false
                        onBlockUser?.invoke()
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(com.example.util.AppStrings.btnBlock(language), color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showBlockConfirmDialog = false }) {
                    Text(com.example.util.AppStrings.btnCancel(language), color = NeutralDark)
                }
            }
        )
    }

    // Dialog Lightbox Foto Momen Full-Screen (dengan zoom 2 jari)
    previewMoment?.let { moment ->
        com.example.ui.components.ZoomablePhotoViewerDialog(
            photoUrl = moment.imageUrl,
            title = com.example.util.AppStrings.partnerProfilePhotoTitle(language, moment.authorName),
            onDismiss = { previewMoment = null }
        )
    }

    // Dialog Lightbox Foto Profil Full-Screen (dengan zoom 2 jari)
    viewingAvatarPhoto?.let { photoUrl ->
        com.example.ui.components.ZoomablePhotoViewerDialog(
            photoUrl = photoUrl,
            title = com.example.util.AppStrings.partnerProfilePhotoTitle(language, user.name),
            onDismiss = { viewingAvatarPhoto = null }
        )
    }

    // Modal Bottom Sheet untuk Melihat & Mengirim Komentar Momen Pengguna
    activeMomentForComments?.let { activeMoment ->
        val comments = momentComments[activeMoment.id] ?: emptyList()
        MomentCommentsBottomSheet(
            moment = activeMoment,
            comments = comments,
            language = language,
            onDismiss = { activeMomentForComments = null },
            onAddComment = { text ->
                onAddComment?.invoke(activeMoment.id, text)
            }
        )
    }
}

@Composable
fun UserProfileMomentCard(
    moment: MomentItem,
    isLiked: Boolean,
    likesCount: Int,
    commentsCount: Int = moment.commentsCount,
    language: com.example.util.AppLanguage = com.example.util.AppLanguage.INDONESIAN,
    onPhotoClick: () -> Unit,
    onToggleLike: () -> Unit,
    onCommentClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFEEEEEE)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            if (!moment.imageUrl.isNullOrBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                        .clickable { onPhotoClick() }
                ) {
                    AsyncImage(
                        model = moment.imageUrl,
                        contentDescription = "Foto momen dari ${moment.authorName}",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Overlay Fullscreen Icon
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.Black.copy(alpha = 0.5f),
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Fullscreen,
                                contentDescription = com.example.util.AppStrings.commonZoomPhoto(language),
                                tint = Color.White,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(com.example.util.AppStrings.commonOpen(language), fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.Medium)
                        }
                    }

                    // Location tag on photo bottom
                    if (!moment.locationTag.isNullOrBlank()) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .fillMaxWidth()
                                .background(
                                    Brush.verticalGradient(
                                        listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                                    )
                                )
                                .padding(horizontal = 10.dp, vertical = 8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = moment.locationTag,
                                    fontSize = 11.5.sp,
                                    color = Color.White,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }

            // Konten Teks & Aksi
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = moment.content,
                    fontSize = 13.5.sp,
                    lineHeight = 19.sp,
                    color = NeutralDark
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = moment.timeAgo,
                        fontSize = 11.5.sp,
                        color = NeutralMedium
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Like Button
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onToggleLike() }
                                .padding(horizontal = 6.dp, vertical = 3.dp)
                        ) {
                            Icon(
                                imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = com.example.util.AppStrings.commonLike(language),
                                tint = if (isLiked) Color(0xFFE91E63) else NeutralMedium,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "$likesCount",
                                fontSize = 12.sp,
                                fontWeight = if (isLiked) FontWeight.Bold else FontWeight.Normal,
                                color = if (isLiked) Color(0xFFE91E63) else NeutralMedium
                            )
                        }

                        // Comments
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onCommentClick() }
                                .padding(horizontal = 6.dp, vertical = 3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ChatBubbleOutline,
                                contentDescription = com.example.util.AppStrings.commonComment(language),
                                tint = EmeraldGreen,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "$commentsCount",
                                fontSize = 12.sp,
                                color = NeutralMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserPhotoPreviewDialog(
    moment: MomentItem,
    language: com.example.util.AppLanguage = com.example.util.AppLanguage.INDONESIAN,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.94f))
                .clickable { onDismiss() }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Header Dialog
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        LovyAvatar(
                            name = moment.authorName,
                            avatarColorHex = moment.authorAvatarHex,
                            avatarUrl = moment.authorAvatarUrl,
                            size = 40.dp,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = moment.authorName,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = moment.timeAgo,
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = com.example.util.AppStrings.commonClose(language),
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Foto Momen Ditengah
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(vertical = 12.dp)
                ) {
                    AsyncImage(
                        model = moment.imageUrl,
                        contentDescription = com.example.util.AppStrings.commonPhoto(language),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp))
                    )
                }

                // Keterangan di Bawah
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color.Black.copy(alpha = 0.6f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        if (!moment.locationTag.isNullOrBlank()) {
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
                                    text = moment.locationTag,
                                    fontSize = 12.sp,
                                    color = EmeraldGreen,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                        Text(
                            text = moment.content,
                            fontSize = 13.5.sp,
                            lineHeight = 19.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

/**
 * Mengambil foto momen asli yang telah diunggah oleh user dari database Supabase/lokal.
 * Jika belum ada momen yang diunggah, kembalikan daftar kosong (tanpa data dummy).
 */
fun getUserMoments(user: User, existingMoments: List<MomentItem>): List<MomentItem> {
    return existingMoments.filter {
        it.authorName.equals(user.name, ignoreCase = true) ||
        (it.authorId.isNotEmpty() && it.authorId == user.id)
    }
}

