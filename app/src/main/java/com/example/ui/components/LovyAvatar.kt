package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.ui.theme.EmeraldGreen

@Composable
fun LovyAvatar(
    name: String,
    avatarColorHex: Long,
    avatarUrl: String? = null,
    size: Dp = 48.dp,
    fontSize: TextUnit = 18.sp,
    isOnline: Boolean? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(size)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(Color(avatarColorHex))
        ) {
            if (!avatarUrl.isNullOrBlank()) {
                AsyncImage(
                    model = avatarUrl,
                    contentDescription = name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            } else {
                // Tampilan default: Logo resmi Lovy Chat saat foto profil tidak dipasang atau dikosongkan
                Image(
                    painter = painterResource(id = R.drawable.ic_lovy_logo),
                    contentDescription = "Logo Lovy Chat",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            }
        }

        if (isOnline == true) {
            val badgeSize = (size * 0.28f).coerceIn(10.dp, 16.dp)
            Box(
                modifier = Modifier
                    .size(badgeSize)
                    .clip(CircleShape)
                    .background(EmeraldGreen)
                    .border(2.dp, Color.White, CircleShape)
                    .align(Alignment.BottomEnd)
            )
        }
    }
}
