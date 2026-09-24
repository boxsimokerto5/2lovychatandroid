package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.NeutralDark
import com.example.ui.theme.NeutralMedium

/**
 * Tipe izin atau deklarasi privasi yang didukung
 */
enum class DisclosureType {
    LOCATION,
    CAMERA,
    NOTIFICATION,
    DATA_PRIVACY
}

/**
 * Model data konfigurasi untuk dialog deklarasi izin terkemuka (Prominent Disclosure)
 */
data class DisclosureConfig(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val iconColor: Color,
    val iconBgColor: Color,
    val highlights: List<Pair<ImageVector, String>>,
    val confirmText: String = "Mengerti",
    val dismissText: String? = "Nanti Saja"
) {
    companion object {
        fun forLocation(): DisclosureConfig = DisclosureConfig(
            title = "Izinkan Penggunaan Lokasi",
            description = "Fitur lokasi digunakan untuk mendeteksi domisili kota secara akurat dan menampilkan teman baru di sekitar Anda pada radar Lovy Chat.",
            icon = Icons.Default.LocationOn,
            iconColor = Color(0xFFE53935),
            iconBgColor = Color(0xFFFFEBEE),
            highlights = listOf(
                Icons.Default.CheckCircle to "Hanya digunakan saat aplikasi dibuka (Foreground)",
                Icons.Default.Shield to "Tidak pernah melacak lokasi di latar belakang",
                Icons.Default.Security to "Koordinat presisi Anda tidak dibagikan langsung ke pengguna lain"
            ),
            confirmText = "Mengerti",
            dismissText = "Nanti Saja"
        )

        fun forCamera(): DisclosureConfig = DisclosureConfig(
            title = "Izinkan Penggunaan Kamera",
            description = "Fitur kamera digunakan untuk memindai Kode QR teman secara instan dan mengambil foto langsung untuk profil atau postingan momen Anda.",
            icon = Icons.Default.CameraAlt,
            iconColor = Color(0xFF1976D2),
            iconBgColor = Color(0xFFE3F2FD),
            highlights = listOf(
                Icons.Default.CheckCircle to "Kamera hanya aktif saat pemindaian QR atau ambil foto",
                Icons.Default.Shield to "Tidak pernah merekam atau mengambil gambar tanpa izin Anda",
                Icons.Default.Security to "Anda tetap dapat memilih foto dari galeri tanpa kamera"
            ),
            confirmText = "Mengerti",
            dismissText = "Nanti Saja"
        )

        fun forNotification(): DisclosureConfig = DisclosureConfig(
            title = "Izinkan Penggunaan Notifikasi",
            description = "Fitur notifikasi digunakan untuk mengirimkan info pesan obrolan masuk, sapaan teman sekitar, atau botol yang terjaring agar Anda tidak ketinggalan kabar penting.",
            icon = Icons.Default.NotificationsActive,
            iconColor = Color(0xFFF57C00),
            iconBgColor = Color(0xFFFFF3E0),
            highlights = listOf(
                Icons.Default.CheckCircle to "Pemberitahuan instan saat ada pesan baru masuk",
                Icons.Default.Shield to "Pengingat interaksi teman tanpa membuka aplikasi terus-menerus",
                Icons.Default.Security to "Dapat dinonaktifkan kapan saja melalui pengaturan sistem"
            ),
            confirmText = "Mengerti",
            dismissText = "Nanti Saja"
        )

        fun forDataPrivacy(): DisclosureConfig = DisclosureConfig(
            title = "Pemberitahuan Penting & Privasi",
            description = "Lovy Chat berkomitmen penuh melindungi privasi data pribadi dan keamanan akun Anda selama menggunakan layanan jejaring sosial ini.",
            icon = Icons.Default.Shield,
            iconColor = AccentOrange,
            iconBgColor = Color(0xFFFFF8E1),
            highlights = listOf(
                Icons.Default.Security to "Semua transmisi data dienkripsi secara aman saat transit",
                Icons.Default.Shield to "Dilengkapi sistem pelaporan dan pemblokiran akun pelanggar",
                Icons.Default.CheckCircle to "Hak mandiri untuk menghapus akun dan data permanen kapan saja"
            ),
            confirmText = "Mengerti & Setuju",
            dismissText = null
        )
    }
}

/**
 * Komponen Dialog Deklarasi Terkemuka (Prominent Disclosure & Explicit Consent)
 * Sesuai panduan resmi Google Play Developer Policy.
 */
@Composable
fun PermissionDisclosureDialog(
    type: DisclosureType,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    onReadPrivacyPolicy: (() -> Unit)? = null
) {
    val config = when (type) {
        DisclosureType.LOCATION -> DisclosureConfig.forLocation()
        DisclosureType.CAMERA -> DisclosureConfig.forCamera()
        DisclosureType.NOTIFICATION -> DisclosureConfig.forNotification()
        DisclosureType.DATA_PRIVACY -> DisclosureConfig.forDataPrivacy()
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("dialog_permission_disclosure_${type.name.lowercase()}")
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 28.dp)
                ) {
                    // Ikon Ilustrasi Elegan di Lingkaran Lembut
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                            .background(config.iconBgColor)
                            .border(2.dp, config.iconColor.copy(alpha = 0.2f), CircleShape)
                    ) {
                        // Lingkaran konsentris dalam
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.85f))
                        ) {
                            Icon(
                                imageVector = config.icon,
                                contentDescription = config.title,
                                tint = config.iconColor,
                                modifier = Modifier.size(42.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Judul Deklarasi
                    Text(
                        text = config.title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeutralDark,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Deskripsi Penjelasan Informatif
                    Text(
                        text = config.description,
                        fontSize = 13.5.sp,
                        lineHeight = 20.sp,
                        color = NeutralMedium,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Poin-poin Jaminan Kepatuhan & Keamanan
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFF8FAFC),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp, horizontal = 14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            config.highlights.forEach { (icon, text) ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = null,
                                        tint = EmeraldGreen,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = text,
                                        fontSize = 12.sp,
                                        lineHeight = 16.sp,
                                        color = NeutralDark,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Tombol Aksi Utama "Mengerti" (Gradient Pill)
                    Button(
                        onClick = onConfirm,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        ),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .background(
                                Brush.horizontalGradient(
                                    listOf(EmeraldGreen, AccentCyan)
                                ),
                                shape = RoundedCornerShape(24.dp)
                            )
                            .testTag("btn_disclosure_confirm")
                    ) {
                        Text(
                            text = config.confirmText,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    // Tombol Opsional "Nanti Saja"
                    if (!config.dismissText.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        TextButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("btn_disclosure_dismiss")
                        ) {
                            Text(
                                text = config.dismissText,
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = NeutralMedium
                            )
                        }
                    }

                    // Tautan Kebijakan Privasi jika disediakan
                    if (onReadPrivacyPolicy != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Baca Kebijakan Privasi",
                            fontSize = 12.sp,
                            color = EmeraldGreen,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .clickable { onReadPrivacyPolicy() }
                                .padding(4.dp)
                        )
                    }
                }
            }
        }
    }
}
