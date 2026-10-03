package com.example.ui.components

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.example.BuildConfig
import com.example.model.AppUpdateInfo
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.NeutralBorder
import com.example.ui.theme.NeutralDark
import com.example.ui.theme.NeutralMedium
import com.example.ui.theme.ScreenBackground
import com.example.util.AppLanguage
import com.example.util.AppShareHelper
import com.example.util.AppStrings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForceUpdateDialog(
    updateInfo: AppUpdateInfo,
    language: AppLanguage = AppLanguage.INDONESIAN,
    onDismiss: () -> Unit = {}
) {
    val context = LocalContext.current
    val isForce = updateInfo.isForceUpdate

    // Kunci tombol Back jika ini adalah Pembaruan Wajib (Force Update)
    BackHandler(enabled = isForce) {
        // Blokir tombol Back - user wajib melakukan update
    }

    BasicAlertDialog(
        onDismissRequest = {
            if (!isForce) {
                onDismiss()
            }
        },
        properties = DialogProperties(
            dismissOnBackPress = !isForce,
            dismissOnClickOutside = !isForce,
            usePlatformDefaultWidth = false
        ),
        modifier = Modifier
            .fillMaxWidth(0.92f)
            .padding(16.dp)
            .testTag("dialog_force_update")
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            shadowElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Icon Badge
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = if (isForce) {
                                    listOf(Color(0xFF00C853), Color(0xFF00965E))
                                } else {
                                    listOf(Color(0xFF00B0FF), Color(0xFF0091EA))
                                }
                            )
                        )
                ) {
                    Icon(
                        imageVector = if (isForce) Icons.Default.RocketLaunch else Icons.Default.SystemUpdate,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(34.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Tag Status
                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = if (isForce) Color(0xFFFFEBEE) else EmeraldGreen.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = if (isForce) "Pembaruan Wajib (Critical)" else "Pembaruan Tersedia",
                        color = if (isForce) Color(0xFFD32F2F) else EmeraldGreen,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Title
                Text(
                    text = if (updateInfo.title.isNotBlank()) updateInfo.title else AppStrings.updateDialogTitle(language, isForce),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 19.sp,
                    color = NeutralDark,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Description
                Text(
                    text = if (updateInfo.message.isNotBlank()) updateInfo.message else {
                        if (isForce) AppStrings.updateDialogDescForce(language) else AppStrings.updateDialogDescOptional(language)
                    },
                    fontSize = 13.sp,
                    color = NeutralMedium,
                    lineHeight = 19.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Version Comparison Card
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = ScreenBackground),
                    border = BorderStroke(1.dp, NeutralBorder.copy(alpha = 0.7f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Versi Anda Saat Ini",
                                fontSize = 11.sp,
                                color = NeutralMedium,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "v${BuildConfig.VERSION_NAME} (Kode ${BuildConfig.VERSION_CODE})",
                                fontSize = 13.sp,
                                color = NeutralDark,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.NewReleases,
                            contentDescription = null,
                            tint = if (isForce) Color(0xFFD32F2F) else EmeraldGreen,
                            modifier = Modifier.size(20.dp)
                        )

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Versi Terbaru",
                                fontSize = 11.sp,
                                color = NeutralMedium,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "v${updateInfo.latestVersionName} (Kode ${updateInfo.latestVersionCode})",
                                fontSize = 13.sp,
                                color = if (isForce) Color(0xFFD32F2F) else EmeraldGreen,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Changelog / Peningkatan Fitur
                val changelogList = if (updateInfo.changelog.isNotEmpty()) updateInfo.changelog else listOf(
                    "Peningkatan stabilitas obrolan & pengiriman pesan",
                    "Akurasi pemindaian radar teman di sekitar yang lebih presisi",
                    "Pembaruan keamanan & kepatuhan penuh Google Play Store"
                )

                Spacer(modifier = Modifier.height(12.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    changelogList.forEach { note ->
                        Row(
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = EmeraldGreen,
                                modifier = Modifier
                                    .size(15.dp)
                                    .padding(top = 2.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = note,
                                fontSize = 12.sp,
                                color = NeutralDark,
                                lineHeight = 17.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(22.dp))

                // Primary Action Button: [Perbarui Sekarang di Google Play]
                Button(
                    onClick = {
                        AppShareHelper.openPlayStoreRating(context)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isForce) EmeraldGreen else Color(0xFF0091EA)
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("btn_proceed_update_playstore")
                ) {
                    Icon(
                        imageVector = Icons.Default.SystemUpdate,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = AppStrings.updateBtnNow(language),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.5.sp
                    )
                }

                // Optional Dismiss Button (Hanya jika bukan Force Update)
                if (!isForce) {
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("btn_dismiss_optional_update")
                    ) {
                        Text(
                            text = AppStrings.updateBtnLater(language),
                            color = NeutralMedium,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}
