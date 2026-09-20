package com.example.ui.components

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.material.icons.filled.WavingHand
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Gender
import com.example.model.User
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.NeutralDark
import com.example.ui.theme.NeutralMedium
import com.example.util.AdManager
import com.example.util.AppLanguage
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

private fun Context.findActivity(): Activity? {
    var ctx = this
    while (ctx is ContextWrapper) {
        if (ctx is Activity) return ctx
        ctx = ctx.baseContext
    }
    return null
}

@Composable
fun NearbyRadarView(
    users: List<User>,
    totalNearbyCount: Int = users.size,
    isExpanded: Boolean = false,
    onSayHi: (User) -> Unit,
    onUserClick: ((User) -> Unit)? = null,
    onExpandNearby: (() -> Unit)? = null,
    language: AppLanguage = AppLanguage.INDONESIAN,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedUser by remember { mutableStateOf<User?>(null) }
    val hasHiddenUsers = !isExpanded && totalNearbyCount > users.size
    val hiddenCount = if (hasHiddenUsers) totalNearbyCount - users.size else 0

    // Animasi sapuan scanner (360 derajat)
    val infiniteTransition = rememberInfiniteTransition(label = "radar_anim")
    val sweepAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "sweep_angle"
    )

    // Animasi gelombang memancar 1
    val pulseProgress1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse_1"
    )

    // Animasi gelombang memancar 2
    val pulseProgress2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2400, delayMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse_2"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF091410))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Status bar info di bagian atas radar (padding rapat & elegan)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(9.dp)
                            .clip(CircleShape)
                            .background(if (isExpanded) Color(0xFF00E676) else EmeraldGreen)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isExpanded) {
                            if (language == AppLanguage.INDONESIAN) "Radar Diperluas • Radius 15 km" else "Expanded Radar • 15 km Radius"
                        } else {
                            if (language == AppLanguage.INDONESIAN) "Radar Aktif • Radius 5 km" else "Active Radar • 5 km Radius"
                        },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFA5D6A7)
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isExpanded) Color(0xFF2E7D32) else Color(0xFF1B382B))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = if (hasHiddenUsers) {
                            "${users.size} dari $totalNearbyCount Terdeteksi"
                        } else {
                            "${users.size} Terdeteksi"
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            // Area Lingkaran Radar Canvas + Avatars (padding minimal agar diameter radar maksimal)
            BoxWithConstraints(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp, vertical = 2.dp),
                contentAlignment = Alignment.Center
            ) {
                val radarDiameter = minOf(maxWidth, maxHeight)
                val radarRadiusPx = remember(radarDiameter) { 
                    // Perhitungan radius radar dalam pixel
                    (radarDiameter.value * 1.4f)
                }

                Box(
                    modifier = Modifier
                        .size(radarDiameter)
                        .clip(CircleShape)
                ) {
                    // 1. Gambar Garis Radar, Gelombang, dan Jarum Sapuan di Canvas
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val center = Offset(size.width / 2f, size.height / 2f)
                        val maxRadius = size.width / 2f

                        // Latar belakang gradasi radar
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(Color(0xFF132A20), Color(0xFF07140E)),
                                center = center,
                                radius = maxRadius
                            ),
                            radius = maxRadius,
                            center = center
                        )

                        // Garis sumbu silang (Crosshairs)
                        val axisColor = Color(0x3300E676)
                        drawLine(
                            color = axisColor,
                            start = Offset(center.x, 0f),
                            end = Offset(center.x, size.height),
                            strokeWidth = 1f
                        )
                        drawLine(
                            color = axisColor,
                            start = Offset(0f, center.y),
                            end = Offset(size.width, center.y),
                            strokeWidth = 1f
                        )

                        // Lingkaran konsentris jarak (500m, 1.5km, 3km, 5km)
                        val ringFractions = listOf(0.25f, 0.50f, 0.75f, 0.98f)
                        val ringColor = Color(0x4400E676)
                        val dashedRing = Stroke(
                            width = 1.5f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                        )

                        ringFractions.forEach { fraction ->
                            drawCircle(
                                color = ringColor,
                                radius = maxRadius * fraction,
                                center = center,
                                style = dashedRing
                            )
                        }

                        // Gelombang pulsa memancar 1
                        val pulseRadius1 = maxRadius * pulseProgress1
                        val pulseAlpha1 = (1f - pulseProgress1).coerceIn(0f, 0.6f)
                        drawCircle(
                            color = EmeraldGreen.copy(alpha = pulseAlpha1),
                            radius = pulseRadius1,
                            center = center,
                            style = Stroke(width = 3f)
                        )

                        // Gelombang pulsa memancar 2
                        val pulseRadius2 = maxRadius * pulseProgress2
                        val pulseAlpha2 = (1f - pulseProgress2).coerceIn(0f, 0.6f)
                        drawCircle(
                            color = EmeraldGreen.copy(alpha = pulseAlpha2),
                            radius = pulseRadius2,
                            center = center,
                            style = Stroke(width = 3f)
                        )

                        // Efek Sapuan Radar Berputar (Sweeping Beam)
                        rotate(degrees = sweepAngle, pivot = center) {
                            drawArc(
                                brush = Brush.sweepGradient(
                                    0.0f to Color.Transparent,
                                    0.75f to Color.Transparent,
                                    0.90f to EmeraldGreen.copy(alpha = 0.08f),
                                    1.0f to EmeraldGreen.copy(alpha = 0.35f),
                                    center = center
                                ),
                                startAngle = 0f,
                                sweepAngle = 360f,
                                useCenter = true
                            )
                            // Garis ujung jarum scanner bercahaya
                            drawLine(
                                color = Color(0xFFB9F6CA),
                                start = center,
                                end = Offset(center.x + maxRadius, center.y),
                                strokeWidth = 2.5f,
                                cap = StrokeCap.Round
                            )
                        }

                        // Lingkaran tepi terluar
                        drawCircle(
                            color = EmeraldGreen.copy(alpha = 0.5f),
                            radius = maxRadius - 1f,
                            center = center,
                            style = Stroke(width = 2.5f)
                        )
                    }

                    // 2. Avatar Posisi "Anda" di Titik Tengah Radar
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(EmeraldGreen)
                            .border(2.5.dp, Color(0xFFB9F6CA), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Anda",
                            color = Color.White,
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    // 3. Letakkan Avatar Teman Sekitar di Koordinat Polar Radar
                    val maxDistance = if (isExpanded) 10000f else 6500f // batas estimasi jarak dalam meter
                    val avatarSize = if (users.size > 8) 36.dp else 40.dp
                    val selectedAvatarSize = if (users.size > 8) 42.dp else 46.dp
                    val stepAngle = 360f / maxOf(users.size, 1)

                    users.forEachIndexed { index, user ->
                        // Hitung jarak radius relatif (0.20f hingga 0.86f agar tidak menumpuk di pusat atau terpotong di tepi)
                        val distRatio = (user.distanceMeters.toFloat() / maxDistance).coerceIn(0.20f, 0.86f)
                        
                        // Hitung sudut polar secara deterministik & merata
                        val seedAngle = ((index * stepAngle) + (user.id.hashCode() % 23)).let {
                            val mod = it % 360
                            if (mod < 0) mod + 360 else mod
                        }
                        val angleRad = Math.toRadians(seedAngle.toDouble())

                        // Konversi ke koordinat Cartesius (X, Y)
                        val radiusFactor = (radarDiameter.value / 2f) * distRatio
                        val xOffsetDp = (cos(angleRad) * radiusFactor).dp
                        val yOffsetDp = (sin(angleRad) * radiusFactor).dp

                        val isSelected = selectedUser?.id == user.id

                        Box(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .offset(x = xOffsetDp, y = yOffsetDp)
                                .testTag("radar_blip_${user.id}")
                                .clickable { selectedUser = user },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(if (isSelected) selectedAvatarSize else avatarSize)
                                        .shadow(6.dp, CircleShape)
                                        .border(
                                            width = if (isSelected) 3.dp else 1.5.dp,
                                            color = if (isSelected) Color(0xFFFFD54F) else Color(0xFFB9F6CA),
                                            shape = CircleShape
                                        )
                                ) {
                                    LovyAvatar(
                                        name = user.name,
                                        avatarColorHex = user.avatarColorHex,
                                        avatarUrl = user.avatarUrl,
                                        size = if (isSelected) selectedAvatarSize else avatarSize,
                                        fontSize = if (users.size > 8) 12.sp else 14.sp,
                                        isOnline = user.isOnline
                                    )
                                }

                                // Label nama dan jarak di bawah avatar
                                Box(
                                    modifier = Modifier
                                        .offset(y = (-2).dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color.Black.copy(alpha = 0.75f))
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = "${user.distanceMeters}m",
                                        fontSize = if (users.size > 8) 8.5.sp else 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color(0xFFFFD54F) else Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Area Kontrol di Bawah Radar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Teks petunjuk sentuh
                Text(
                    text = "Sentuh avatar di radar untuk melihat profil & menyapa",
                    fontSize = 11.5.sp,
                    color = Color(0xFF81C784),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                // Tombol "Cari Lebih Banyak di Radar" dengan Iklan Reward jika belum diperluas
                if (hasHiddenUsers && selectedUser == null) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF14291F)),
                        border = BorderStroke(1.dp, Color(0xFFFFB300).copy(alpha = 0.6f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp)
                    ) {
                        Button(
                            onClick = {
                                val activity = context.findActivity()
                                val adLaunched = AdManager.showRewardedVideo(activity = activity) {
                                    onExpandNearby?.invoke()
                                    Toast.makeText(
                                        context,
                                        if (language == AppLanguage.INDONESIAN) {
                                            "Selamat! Radar diperluas & $hiddenCount teman baru ditemukan 🎉"
                                        } else {
                                            "Success! Radar expanded & $hiddenCount new friends found 🎉"
                                        },
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                if (!adLaunched) {
                                    Toast.makeText(
                                        context,
                                        if (language == AppLanguage.INDONESIAN) {
                                            "Mempersiapkan radar... Menampilkan semua pengguna sekitar untuk Anda ✨"
                                        } else {
                                            "Preparing radar... Unlocking all nearby users for you ✨"
                                        },
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    onExpandNearby?.invoke()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(Color(0xFFE65100), Color(0xFFFF8F00))
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .testTag("radar_btn_expand_reward")
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f, fill = false)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(Color.White.copy(alpha = 0.25f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.PlayCircle,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = if (language == AppLanguage.INDONESIAN) {
                                                "Cari Lebih Banyak di Radar"
                                            } else {
                                                "Discover More on Radar"
                                            },
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Text(
                                            text = if (language == AppLanguage.INDONESIAN) {
                                                "+$hiddenCount teman baru • Tonton video singkat 🎬"
                                            } else {
                                                "+$hiddenCount new people • Watch short video 🎬"
                                            },
                                            fontSize = 11.sp,
                                            color = Color.White.copy(alpha = 0.9f)
                                        )
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color.White.copy(alpha = 0.25f))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "REWARD",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                } else if (isExpanded && selectedUser == null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(bottom = 6.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFF1B382B))
                            .border(1.dp, EmeraldGreen.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF69F0AE),
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (language == AppLanguage.INDONESIAN) {
                                "Radar Maksimal Aktif • ${users.size} Pengguna Terbuka ✨"
                            } else {
                                "Max Radar Active • ${users.size} Users Unlocked ✨"
                            },
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFE8F5E9)
                        )
                    }
                }
            }

            // 4. Kartu Pengguna Terpilih (Peek Profile Card) di bagian bawah
            AnimatedVisibility(
                visible = selectedUser != null,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
            ) {
                selectedUser?.let { user ->
                    Card(
                        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                            .testTag("radar_selected_user_card")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable(enabled = onUserClick != null) {
                                        onUserClick?.invoke(user)
                                    },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                LovyAvatar(
                                    name = user.name,
                                    avatarColorHex = user.avatarColorHex,
                                    avatarUrl = user.avatarUrl,
                                    size = 52.dp,
                                    fontSize = 20.sp,
                                    isOnline = user.isOnline
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = user.name,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = NeutralDark,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        val badgeColor = if (user.gender == Gender.FEMALE) Color(0xFFFF4081) else Color(0xFF2196F3)
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(badgeColor)
                                                .padding(horizontal = 4.dp, vertical = 1.dp)
                                        ) {
                                            Icon(
                                                imageVector = if (user.gender == Gender.FEMALE) Icons.Default.Female else Icons.Default.Male,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier.size(11.dp)
                                            )
                                        }
                                    }

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(top = 2.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.LocationOn,
                                            contentDescription = null,
                                            tint = EmeraldGreen,
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Spacer(modifier = Modifier.width(2.dp))
                                        Text(
                                            text = "${user.distanceMeters} m • ${user.city}",
                                            fontSize = 11.5.sp,
                                            color = NeutralMedium,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }

                                    Text(
                                        text = user.bio,
                                        fontSize = 12.sp,
                                        color = NeutralDark.copy(alpha = 0.85f),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // Tombol Sapa
                            Button(
                                onClick = { onSayHi(user) },
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.testTag("btn_say_hi_radar_${user.id}")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.WavingHand,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Sapa", fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
                            }

                            IconButton(
                                onClick = { selectedUser = null },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Tutup",
                                    tint = NeutralMedium,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
