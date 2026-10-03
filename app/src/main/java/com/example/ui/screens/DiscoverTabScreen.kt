package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.NeutralBorder
import com.example.ui.theme.NeutralDark
import com.example.ui.theme.NeutralMedium
import com.example.ui.theme.ScreenBackground
import com.example.util.AppShareHelper

@Composable
fun DiscoverTabScreen(
    language: com.example.util.AppLanguage = com.example.util.AppLanguage.INDONESIAN,
    onNavigateToNearby: () -> Unit,
    onNavigateToBottle: () -> Unit,
    onNavigateToMoments: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ScreenBackground)
            .verticalScroll(rememberScrollState())
    ) {
        // Top Bar - Temukan (Compact & Elegant)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(EmeraldGreen)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = com.example.util.AppStrings.tabDiscover(language),
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            // Hero Banner matching user screenshot (Rapi & Lebih Ringkas)
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("discover_hero_banner")
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF00BA7C),
                                    Color(0xFF00965E)
                                )
                            )
                        )
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    // Faint compass watermark
                    Icon(
                        imageVector = Icons.Default.Explore,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .size(90.dp)
                            .align(Alignment.BottomEnd)
                            .alpha(0.18f)
                    )

                    Column(modifier = Modifier.fillMaxWidth(0.92f)) {
                        // Tag
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .background(Color.White.copy(alpha = 0.22f))
                                .padding(horizontal = 10.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = com.example.util.AppStrings.bannerTag(language),
                                color = Color.White,
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Title
                        Text(
                            text = com.example.util.AppStrings.bannerTitle(language),
                            color = Color.White,
                            fontSize = 15.5.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Desc
                        Text(
                            text = com.example.util.AppStrings.bannerDesc(language),
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 11.5.sp,
                            lineHeight = 15.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action Menu Card matching screenshot
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("discover_menu_card")
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Item 1: Pengguna di Sekitar
                    DiscoverMenuItem(
                        icon = Icons.Default.LocationOn,
                        iconTint = Color(0xFF00A86B),
                        iconBgColor = Color(0xFFE8F5E9),
                        title = com.example.util.AppStrings.menuNearby(language),
                        description = com.example.util.AppStrings.menuNearbySub(language),
                        testTag = "menu_nearby_people",
                        onClick = onNavigateToNearby
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(start = 76.dp, end = 16.dp),
                        thickness = 0.8.dp,
                        color = NeutralBorder
                    )

                    // Item 2: Pesan dalam Botol
                    DiscoverMenuItem(
                        icon = Icons.Default.Waves,
                        iconTint = Color(0xFF00ACC1),
                        iconBgColor = Color(0xFFE0F7FA),
                        title = com.example.util.AppStrings.menuBottle(language),
                        description = com.example.util.AppStrings.menuBottleSub(language),
                        testTag = "menu_bottle_message",
                        onClick = onNavigateToBottle
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(start = 76.dp, end = 16.dp),
                        thickness = 0.8.dp,
                        color = NeutralBorder
                    )

                    // Item 3: Momen (Feed Status)
                    DiscoverMenuItem(
                        icon = Icons.Default.CameraAlt,
                        iconTint = Color(0xFFFB8C00),
                        iconBgColor = Color(0xFFFFF3E0),
                        title = com.example.util.AppStrings.menuMoments(language),
                        description = com.example.util.AppStrings.menuMomentsSub(language),
                        testTag = "menu_moments",
                        onClick = onNavigateToMoments
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(start = 76.dp, end = 16.dp),
                        thickness = 0.8.dp,
                        color = NeutralBorder
                    )

                    // Item 4: Bagikan Aplikasi
                    DiscoverMenuItem(
                        icon = Icons.Default.Share,
                        iconTint = EmeraldGreen,
                        iconBgColor = EmeraldGreen.copy(alpha = 0.12f),
                        title = com.example.util.AppStrings.menuShareApp(language),
                        description = com.example.util.AppStrings.menuShareAppSub(language),
                        testTag = "menu_share_app",
                        onClick = { AppShareHelper.shareApp(context, language) }
                    )
                }
            }
        }
    }
}

@Composable
fun DiscoverMenuItem(
    icon: ImageVector,
    iconTint: Color,
    iconBgColor: Color,
    title: String,
    description: String,
    testTag: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag(testTag)
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        // Icon Container
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(iconBgColor)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconTint,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        // Titles
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 14.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = NeutralDark
            )
            Spacer(modifier = Modifier.height(1.dp))
            Text(
                text = description,
                fontSize = 11.5.sp,
                color = NeutralMedium,
                lineHeight = 15.sp
            )
        }

        // Chevron
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Color(0xFFB0BEC5),
            modifier = Modifier.size(20.dp)
        )
    }
}
