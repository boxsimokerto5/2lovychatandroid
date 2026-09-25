package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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

import com.example.util.AppLanguage
import com.example.util.AppStrings

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
        fun forLocation(lang: AppLanguage = AppLanguage.INDONESIAN): DisclosureConfig {
            val resolved = AppStrings.resolveLang(lang)
            val title = when (resolved) {
                AppLanguage.CHINESE -> "允许使用位置信息"
                AppLanguage.JAPANESE -> "位置情報の利用を許可"
                AppLanguage.KOREAN -> "위치 권한 허용"
                AppLanguage.ARABIC -> "السماح بالوصول إلى الموقع"
                AppLanguage.SPANISH -> "Permitir acceso a la ubicación"
                AppLanguage.FRENCH -> "Autoriser la localisation"
                AppLanguage.GERMAN -> "Standortzugriff erlauben"
                AppLanguage.RUSSIAN -> "Доступ к геопозиции"
                AppLanguage.PORTUGUESE -> "Permitir acesso à localização"
                AppLanguage.INDONESIAN -> "Izinkan Penggunaan Lokasi"
                else -> "Allow Location Access"
            }
            val desc = when (resolved) {
                AppLanguage.CHINESE -> "位置信息用于精确显示您所在的城市，并在雷达上显示附近的交友用户。"
                AppLanguage.JAPANESE -> "位置情報は都市を特定し、レーダー上に近くの新しいユーザーを表示するために使用されます。"
                AppLanguage.KOREAN -> "위치 정보는 현재 거주 도시를 파악하고 레이더에서 주변 친구를 찾는 데 사용됩니다."
                AppLanguage.ARABIC -> "تُستخدم ميزة الموقع لتحديد مدينتك بدقة وعرض أصدقاء جدد بالقرب منك على رادار Lovy Chat."
                AppLanguage.SPANISH -> "La ubicación se utiliza para detectar tu ciudad y mostrar nuevos amigos cercanos en el radar de Lovy Chat."
                AppLanguage.FRENCH -> "La localisation est utilisée pour détecter votre ville et afficher les personnes proches sur le radar Lovy Chat."
                AppLanguage.GERMAN -> "Der Standort wird genutzt, um Ihre Stadt zu ermitteln und Personen in der Nähe auf dem Radar anzuzeigen."
                AppLanguage.RUSSIAN -> "Геопозиция нужна для определения вашего города и отображения людей поблизости на радаре."
                AppLanguage.PORTUGUESE -> "A localização é usada para identificar sua cidade e exibir amigos próximos no radar Lovy Chat."
                AppLanguage.INDONESIAN -> "Fitur lokasi digunakan untuk mendeteksi domisili kota secara akurat dan menampilkan teman baru di sekitar Anda pada radar Lovy Chat."
                else -> "Location is used to accurately detect your city and display new friends nearby on the Lovy Chat radar."
            }
            val h1 = when (resolved) {
                AppLanguage.CHINESE -> "仅在打开应用时使用 (前台)"
                AppLanguage.JAPANESE -> "アプリ使用中のみ動作 (フォアグラウンド)"
                AppLanguage.KOREAN -> "앱 실행 중에만 활성화 (포그라운드)"
                AppLanguage.ARABIC -> "يُستخدم فقط أثناء فتح التطبيق (الواجهة)"
                AppLanguage.SPANISH -> "Solo mientras la app está abierta"
                AppLanguage.FRENCH -> "Uniquement lorsque l'application est ouverte"
                AppLanguage.GERMAN -> "Nur bei geöffneter App aktiv"
                AppLanguage.RUSSIAN -> "Только при открытом приложении"
                AppLanguage.PORTUGUESE -> "Apenas enquanto o app estiver aberto"
                AppLanguage.INDONESIAN -> "Hanya digunakan saat aplikasi dibuka (Foreground)"
                else -> "Only active while the app is in use (Foreground)"
            }
            val h2 = when (resolved) {
                AppLanguage.CHINESE -> "绝不在后台跟踪位置"
                AppLanguage.JAPANESE -> "バックグラウンドでの追跡は一切行いません"
                AppLanguage.KOREAN -> "백그라운드에서 추적하지 않습니다"
                AppLanguage.ARABIC -> "لا يتم التتبع في الخلفية أبداً"
                AppLanguage.SPANISH -> "Nunca rastrea en segundo plano"
                AppLanguage.FRENCH -> "Pas de suivi en arrière-plan"
                AppLanguage.GERMAN -> "Kein Tracking im Hintergrund"
                AppLanguage.RUSSIAN -> "Не отслеживает в фоновом режиме"
                AppLanguage.PORTUGUESE -> "Nunca rastreia em segundo plano"
                AppLanguage.INDONESIAN -> "Tidak pernah melacak lokasi di latar belakang"
                else -> "Never tracks your location in the background"
            }
            val h3 = when (resolved) {
                AppLanguage.CHINESE -> "精确坐标不会直接公开"
                AppLanguage.JAPANESE -> "正確な座標は他人に直接共有されません"
                AppLanguage.KOREAN -> "정확한 좌표는 타인에게 공개되지 않습니다"
                AppLanguage.ARABIC -> "لا تتم مشاركة إحداثياتك الدقيقة مع الآخرين"
                AppLanguage.SPANISH -> "Tus coordenadas exactas no se comparten directamente"
                AppLanguage.FRENCH -> "Vos coordonnées exactes ne sont pas partagées"
                AppLanguage.GERMAN -> "Genaue Koordinaten werden nicht weitergegeben"
                AppLanguage.RUSSIAN -> "Точные координаты не передаются третьим лицам"
                AppLanguage.PORTUGUESE -> "Suas coordenadas exatas não são compartilhadas"
                AppLanguage.INDONESIAN -> "Koordinat presisi Anda tidak dibagikan langsung ke pengguna lain"
                else -> "Exact coordinates are protected and not directly shared"
            }
            return DisclosureConfig(
                title = title,
                description = desc,
                icon = Icons.Default.LocationOn,
                iconColor = Color(0xFFE53935),
                iconBgColor = Color(0xFFFFEBEE),
                highlights = listOf(
                    Icons.Default.CheckCircle to h1,
                    Icons.Default.Shield to h2,
                    Icons.Default.Security to h3
                ),
                confirmText = getConfirmText(resolved),
                dismissText = getDismissText(resolved)
            )
        }

        fun forCamera(lang: AppLanguage = AppLanguage.INDONESIAN): DisclosureConfig {
            val resolved = AppStrings.resolveLang(lang)
            val title = when (resolved) {
                AppLanguage.CHINESE -> "允许使用相机"
                AppLanguage.JAPANESE -> "カメラの利用を許可"
                AppLanguage.KOREAN -> "카메라 권한 허용"
                AppLanguage.ARABIC -> "السماح باستخدام الكاميرا"
                AppLanguage.SPANISH -> "Permitir acceso a la cámara"
                AppLanguage.FRENCH -> "Autoriser l'appareil photo"
                AppLanguage.GERMAN -> "Kamerazugriff erlauben"
                AppLanguage.RUSSIAN -> "Доступ к камере"
                AppLanguage.PORTUGUESE -> "Permitir acesso à câmera"
                AppLanguage.INDONESIAN -> "Izinkan Penggunaan Kamera"
                else -> "Allow Camera Access"
            }
            val desc = when (resolved) {
                AppLanguage.CHINESE -> "相机用于快速扫描好友二维码，以及拍摄即时头像或动态照片。"
                AppLanguage.JAPANESE -> "友達のQRコードスキャンやプロフィール写真・モーメント撮影に使用します。"
                AppLanguage.KOREAN -> "친구의 QR 코드를 스캔하고 프로필이나 모먼트 사진을 촬영하는 데 사용됩니다."
                AppLanguage.ARABIC -> "تُستخدم الكاميرا لمسح رموز QR للأصدقاء والتقاط صور الملف الشخصي أو المنشورات مباشرة."
                AppLanguage.SPANISH -> "La cámara se usa para escanear códigos QR de amigos y tomar fotos de perfil o momentos."
                AppLanguage.FRENCH -> "L'appareil photo sert à scanner les codes QR et prendre des photos de profil ou de moments."
                AppLanguage.GERMAN -> "Die Kamera dient zum Scannen von QR-Codes und zum Aufnehmen von Profilbildern oder Momenten."
                AppLanguage.RUSSIAN -> "Камера используется для сканирования QR-кодов и съемки фото профиля или моментов."
                AppLanguage.PORTUGUESE -> "A câmera é usada para ler QR codes de amigos e tirar fotos para seu perfil ou momentos."
                AppLanguage.INDONESIAN -> "Fitur kamera digunakan untuk memindai Kode QR teman secara instan dan mengambil foto langsung untuk profil atau postingan momen Anda."
                else -> "Camera is used to instantly scan friends' QR codes and capture photos for your profile or moments."
            }
            val h1 = when (resolved) {
                AppLanguage.CHINESE -> "仅在扫码或拍照时激活相机"
                AppLanguage.JAPANESE -> "スキャンまたは撮影時のみ有効化"
                AppLanguage.KOREAN -> "QR 스캔 및 촬영 시에만 카메라 활성화"
                AppLanguage.ARABIC -> "الكاميرا نشطة فقط أثناء مسح QR أو التصوير"
                AppLanguage.SPANISH -> "Solo activa al escanear o tomar fotos"
                AppLanguage.FRENCH -> "Active uniquement lors du scan ou de la prise de vue"
                AppLanguage.GERMAN -> "Nur beim Scannen oder Fotografieren aktiv"
                AppLanguage.RUSSIAN -> "Активна только при сканировании или съемке"
                AppLanguage.PORTUGUESE -> "Ativa apenas ao escanear ou tirar fotos"
                AppLanguage.INDONESIAN -> "Kamera hanya aktif saat pemindaian QR atau ambil foto"
                else -> "Camera is only active when scanning QR or taking photos"
            }
            val h2 = when (resolved) {
                AppLanguage.CHINESE -> "绝不在未经允许的情况下录像或拍照"
                AppLanguage.JAPANESE -> "許可なく撮影や録画を行うことはありません"
                AppLanguage.KOREAN -> "동의 없이 촬영하거나 녹화하지 않습니다"
                AppLanguage.ARABIC -> "لا يتم التقاط الصور أو التسجيل دون إذنك مطلقاً"
                AppLanguage.SPANISH -> "Nunca graba ni captura sin tu permiso"
                AppLanguage.FRENCH -> "N'enregistre jamais sans votre accord"
                AppLanguage.GERMAN -> "Nimmt niemals ohne Zustimmung auf"
                AppLanguage.RUSSIAN -> "Никогда не ведет запись без согласия"
                AppLanguage.PORTUGUESE -> "Nunca grava ou tira fotos sem permissão"
                AppLanguage.INDONESIAN -> "Tidak pernah merekam atau mengambil gambar tanpa izin Anda"
                else -> "Never records or captures images without your consent"
            }
            val h3 = when (resolved) {
                AppLanguage.CHINESE -> "您仍可以从手机相册中选择照片"
                AppLanguage.JAPANESE -> "アルバムから写真を選択することも可能です"
                AppLanguage.KOREAN -> "갤러리에서 직접 사진을 선택할 수도 있습니다"
                AppLanguage.ARABIC -> "يمكنك دائماً اختيار صور من المعرض بدون كاميرا"
                AppLanguage.SPANISH -> "Siempre puedes seleccionar fotos desde tu galería"
                AppLanguage.FRENCH -> "Vous pouvez toujours importer des photos depuis la galerie"
                AppLanguage.GERMAN -> "Fotos können auch aus der Galerie gewählt werden"
                AppLanguage.RUSSIAN -> "Вы всегда можете выбрать фото из галереи"
                AppLanguage.PORTUGUESE -> "Você sempre pode escolher fotos da galeria"
                AppLanguage.INDONESIAN -> "Anda tetap dapat memilih foto dari galeri tanpa kamera"
                else -> "You can still choose photos directly from your gallery"
            }
            return DisclosureConfig(
                title = title,
                description = desc,
                icon = Icons.Default.CameraAlt,
                iconColor = Color(0xFF1976D2),
                iconBgColor = Color(0xFFE3F2FD),
                highlights = listOf(
                    Icons.Default.CheckCircle to h1,
                    Icons.Default.Shield to h2,
                    Icons.Default.Security to h3
                ),
                confirmText = getConfirmText(resolved),
                dismissText = getDismissText(resolved)
            )
        }

        fun forNotification(lang: AppLanguage = AppLanguage.INDONESIAN): DisclosureConfig {
            val resolved = AppStrings.resolveLang(lang)
            val title = when (resolved) {
                AppLanguage.CHINESE -> "允许通知"
                AppLanguage.JAPANESE -> "通知の送信を許可"
                AppLanguage.KOREAN -> "알림 권한 허용"
                AppLanguage.ARABIC -> "السماح بالإشعارات"
                AppLanguage.SPANISH -> "Permitir notificaciones"
                AppLanguage.FRENCH -> "Autoriser les notifications"
                AppLanguage.GERMAN -> "Benachrichtigungen erlauben"
                AppLanguage.RUSSIAN -> "Разрешить уведомления"
                AppLanguage.PORTUGUESE -> "Permitir notificações"
                AppLanguage.INDONESIAN -> "Izinkan Penggunaan Notifikasi"
                else -> "Allow Notifications"
            }
            val desc = when (resolved) {
                AppLanguage.CHINESE -> "通知用于向您发送新聊天消息、附近好友打招呼或捞到漂流瓶的即时提醒。"
                AppLanguage.JAPANESE -> "新着メッセージ、近くの友達からの挨拶、漂流ボトルの通知を受け取るために使用します。"
                AppLanguage.KOREAN -> "새 메시지, 주변 친구의 인사, 낚은 유리병 알림을 실시간으로 전달합니다."
                AppLanguage.ARABIC -> "تُستخدم الإشعارات لتنبيهك بالرسائل الجديدة أو تحيات الأصدقاء القريبين أو الزجاجات المصطادة."
                AppLanguage.SPANISH -> "Las notificaciones te avisan de nuevos chats, saludos cercanos o botellas pescadas."
                AppLanguage.FRENCH -> "Les notifications vous informent des nouveaux messages, des saluts d'amis ou des bouteilles repêchées."
                AppLanguage.GERMAN -> "Benachrichtigungen informieren über neue Nachrichten, Grüße von Freunden oder gefischte Flaschen."
                AppLanguage.RUSSIAN -> "Уведомления сообщают о новых сообщениях, приветствиях поблизости или выловленных бутылках."
                AppLanguage.PORTUGUESE -> "As notificações avisam sobre novas mensagens, saudações de amigos ou garrafas pescadas."
                AppLanguage.INDONESIAN -> "Fitur notifikasi digunakan untuk mengirimkan info pesan obrolan masuk, sapaan teman sekitar, atau botol yang terjaring agar Anda tidak ketinggalan kabar penting."
                else -> "Notifications are used to deliver instant alerts for incoming chats, nearby greetings, or fished bottle messages."
            }
            val h1 = when (resolved) {
                AppLanguage.CHINESE -> "新聊天消息即时提醒"
                AppLanguage.JAPANESE -> "メッセージ着信時の即時通知"
                AppLanguage.KOREAN -> "새 메시지 도착 시 즉각적인 알림"
                AppLanguage.ARABIC -> "تنبيه فوري عند وصول رسائل جديدة"
                AppLanguage.SPANISH -> "Avisos instantáneos de nuevos mensajes"
                AppLanguage.FRENCH -> "Alertes instantanées pour les nouveaux messages"
                AppLanguage.GERMAN -> "Sofortige Benachrichtigung bei neuen Nachrichten"
                AppLanguage.RUSSIAN -> "Мгновенные оповещения о новых сообщениях"
                AppLanguage.PORTUGUESE -> "Avisos instantâneos de novas mensagens"
                AppLanguage.INDONESIAN -> "Pemberitahuan instan saat ada pesan baru masuk"
                else -> "Instant alerts whenever new messages arrive"
            }
            val h2 = when (resolved) {
                AppLanguage.CHINESE -> "无需一直保持打开应用也能收到互动提醒"
                AppLanguage.JAPANESE -> "アプリを開いていなくても大切な連絡を見逃しません"
                AppLanguage.KOREAN -> "앱을 계속 열어두지 않아도 중요한 소식을 놓치지 않습니다"
                AppLanguage.ARABIC -> "تذكير بالتفاعل دون الحاجة لفتح التطبيق باستمرار"
                AppLanguage.SPANISH -> "Mantente conectado sin tener la app abierta"
                AppLanguage.FRENCH -> "Restez informé sans garder l'application ouverte"
                AppLanguage.GERMAN -> "Interaktionen mitbekommen, ohne die App geöffnet zu halten"
                AppLanguage.RUSSIAN -> "Будьте на связи, не открывая приложение постоянно"
                AppLanguage.PORTUGUESE -> "Avisos de interação sem precisar manter o app aberto"
                AppLanguage.INDONESIAN -> "Pengingat interaksi teman tanpa membuka aplikasi terus-menerus"
                else -> "Stay updated on friend interactions without keeping the app open"
            }
            val h3 = when (resolved) {
                AppLanguage.CHINESE -> "可随时在系统设置中调整或关闭"
                AppLanguage.JAPANESE -> "端末の設定からいつでも変更やオフが可能です"
                AppLanguage.KOREAN -> "기기 설정에서 언제든 끄거나 조정할 수 있습니다"
                AppLanguage.ARABIC -> "يمكن تعديلها أو تعطيلها في أي وقت عبر إعدادات الجهاز"
                AppLanguage.SPANISH -> "Puedes desactivarlas en cualquier momento en los ajustes"
                AppLanguage.FRENCH -> "Désactivable à tout moment dans les réglages"
                AppLanguage.GERMAN -> "Jederzeit in den Systemeinstellungen anpassbar"
                AppLanguage.RUSSIAN -> "Можно настроить или отключить в любой момент в настройках"
                AppLanguage.PORTUGUESE -> "Pode ser ajustado ou desativado a qualquer momento"
                AppLanguage.INDONESIAN -> "Dapat dinonaktifkan kapan saja melalui pengaturan sistem"
                else -> "Easily managed or disabled anytime via system settings"
            }
            return DisclosureConfig(
                title = title,
                description = desc,
                icon = Icons.Default.NotificationsActive,
                iconColor = Color(0xFFF57C00),
                iconBgColor = Color(0xFFFFF3E0),
                highlights = listOf(
                    Icons.Default.CheckCircle to h1,
                    Icons.Default.Shield to h2,
                    Icons.Default.Security to h3
                ),
                confirmText = getConfirmText(resolved),
                dismissText = getDismissText(resolved)
            )
        }

        fun forDataPrivacy(lang: AppLanguage = AppLanguage.INDONESIAN): DisclosureConfig {
            val resolved = AppStrings.resolveLang(lang)
            val title = when (resolved) {
                AppLanguage.CHINESE -> "隐私与安全须知"
                AppLanguage.JAPANESE -> "プライバシーとセキュリティ"
                AppLanguage.KOREAN -> "개인정보 및 보안 안내"
                AppLanguage.ARABIC -> "إشعار الخصوصية والأمان"
                AppLanguage.SPANISH -> "Aviso de Privacidad y Seguridad"
                AppLanguage.FRENCH -> "Confidentialité et sécurité"
                AppLanguage.GERMAN -> "Datenschutz & Sicherheit"
                AppLanguage.RUSSIAN -> "Конфиденциальность и безопасность"
                AppLanguage.PORTUGUESE -> "Privacidade e Segurança"
                AppLanguage.INDONESIAN -> "Pemberitahuan Penting & Privasi"
                else -> "Important Privacy Notice"
            }
            val desc = when (resolved) {
                AppLanguage.CHINESE -> "Lovy Chat 致力于保护您的个人数据隐私和账户安全。"
                AppLanguage.JAPANESE -> "Lovy Chat はユーザーのプライバシーとアカウントのセキュリティ保護に取り組んでいます。"
                AppLanguage.KOREAN -> "Lovy Chat은 사용자의 소중한 개인정보와 계정 보안을 철저히 보호합니다."
                AppLanguage.ARABIC -> "يلتزم Lovy Chat بحماية خصوصية بياناتك الشخصية وأمان حسابك بالكامل."
                AppLanguage.SPANISH -> "Lovy Chat se compromete plenamente a proteger la privacidad y seguridad de tu cuenta."
                AppLanguage.FRENCH -> "Lovy Chat s'engage à protéger vos données personnelles et votre compte."
                AppLanguage.GERMAN -> "Lovy Chat setzt sich für den Schutz Ihrer Privatsphäre und Kontosicherheit ein."
                AppLanguage.RUSSIAN -> "Lovy Chat гарантирует защиту ваших личных данных и безопасности аккаунта."
                AppLanguage.PORTUGUESE -> "O Lovy Chat está totalmente comprometido em proteger sua privacidade e segurança."
                AppLanguage.INDONESIAN -> "Lovy Chat berkomitmen penuh melindungi privasi data pribadi dan keamanan akun Anda selama menggunakan layanan jejaring sosial ini."
                else -> "Lovy Chat is dedicated to protecting your personal data privacy and account security at all times."
            }
            val h1 = when (resolved) {
                AppLanguage.CHINESE -> "所有传输数据在传输过程中均经过加密保护"
                AppLanguage.JAPANESE -> "すべてのデータ転送は通信中に暗号化されます"
                AppLanguage.KOREAN -> "모든 데이터 전송은 암호화되어 안전하게 처리됩니다"
                AppLanguage.ARABIC -> "يتم تشفير جميع عمليات نقل البيانات أثناء النقل"
                AppLanguage.SPANISH -> "Todos los datos están cifrados en tránsito"
                AppLanguage.FRENCH -> "Toutes les données sont chiffrées en transit"
                AppLanguage.GERMAN -> "Alle Daten werden bei der Übertragung verschlüsselt"
                AppLanguage.RUSSIAN -> "Все данные шифруются при передаче"
                AppLanguage.PORTUGUESE -> "Todos os dados são criptografados em trânsito"
                AppLanguage.INDONESIAN -> "Semua transmisi data dienkripsi secara aman saat transit"
                else -> "All data transmission is encrypted securely in transit"
            }
            val h2 = when (resolved) {
                AppLanguage.CHINESE -> "配有防骚扰与不良用户举报和屏蔽机制"
                AppLanguage.JAPANESE -> "スパム・嫌がらせ防止の通報・ブロック機能を完備"
                AppLanguage.KOREAN -> "스팸 및 부적절한 사용자 신고와 차단 기능 지원"
                AppLanguage.ARABIC -> "نظام متكامل للإبلاغ وحظر المستخدمين المسيئين"
                AppLanguage.SPANISH -> "Herramientas de reporte y bloqueo para prevenir abusos"
                AppLanguage.FRENCH -> "Système de signalement et de blocage anti-harcèlement"
                AppLanguage.GERMAN -> "Melde- und Blockierfunktionen gegen Missbrauch"
                AppLanguage.RUSSIAN -> "Система жалоб и блокировок для защиты от спама"
                AppLanguage.PORTUGUESE -> "Sistema completo de denúncia e bloqueio de abusos"
                AppLanguage.INDONESIAN -> "Dilengkapi sistem pelaporan dan pemblokiran akun pelanggar"
                else -> "Equipped with user reporting and blocking protections"
            }
            val h3 = when (resolved) {
                AppLanguage.CHINESE -> "拥有随时永久注销账号及删除全部数据的权利"
                AppLanguage.JAPANESE -> "いつでもアカウントと全データを完全削除する権利"
                AppLanguage.KOREAN -> "언제든지 계정 및 데이터를 완전히 삭제할 권리 보장"
                AppLanguage.ARABIC -> "الحق الكامل في حذف حسابك وبياناتك نهائياً في أي وقت"
                AppLanguage.SPANISH -> "Derecho a eliminar permanentemente tu cuenta y datos"
                AppLanguage.FRENCH -> "Droit de supprimer définitivement votre compte et données"
                AppLanguage.GERMAN -> "Recht auf dauerhafte Löschung von Konto und Daten"
                AppLanguage.RUSSIAN -> "Право на удаление аккаунта и данных в любое время"
                AppLanguage.PORTUGUESE -> "Direito de excluir permanentemente sua conta e dados"
                AppLanguage.INDONESIAN -> "Hak mandiri untuk menghapus akun dan data permanen kapan saja"
                else -> "Full right to permanently delete your account and data anytime"
            }
            val confirm = when (resolved) {
                AppLanguage.CHINESE -> "理解并同意"
                AppLanguage.JAPANESE -> "同意して続ける"
                AppLanguage.KOREAN -> "확인 및 동의"
                AppLanguage.ARABIC -> "موافق ومتابعة"
                AppLanguage.SPANISH -> "Entendido y Aceptar"
                AppLanguage.FRENCH -> "Compris et accepter"
                AppLanguage.GERMAN -> "Verstanden & zustimmen"
                AppLanguage.RUSSIAN -> "Понятно и согласен"
                AppLanguage.PORTUGUESE -> "Entendido e concordar"
                AppLanguage.INDONESIAN -> "Mengerti & Setuju"
                else -> "Understood & Agree"
            }
            return DisclosureConfig(
                title = title,
                description = desc,
                icon = Icons.Default.Shield,
                iconColor = AccentOrange,
                iconBgColor = Color(0xFFFFF8E1),
                highlights = listOf(
                    Icons.Default.Security to h1,
                    Icons.Default.Shield to h2,
                    Icons.Default.CheckCircle to h3
                ),
                confirmText = confirm,
                dismissText = null
            )
        }

        private fun getConfirmText(lang: AppLanguage): String = when (lang) {
            AppLanguage.CHINESE -> "知道了"
            AppLanguage.JAPANESE -> "了解しました"
            AppLanguage.KOREAN -> "확인"
            AppLanguage.ARABIC -> "فهمت"
            AppLanguage.SPANISH -> "Entendido"
            AppLanguage.FRENCH -> "Compris"
            AppLanguage.GERMAN -> "Verstanden"
            AppLanguage.RUSSIAN -> "Понятно"
            AppLanguage.PORTUGUESE -> "Entendi"
            AppLanguage.INDONESIAN -> "Mengerti"
            else -> "Understood"
        }

        private fun getDismissText(lang: AppLanguage): String = when (lang) {
            AppLanguage.CHINESE -> "稍后再说"
            AppLanguage.JAPANESE -> "後で"
            AppLanguage.KOREAN -> "나중에"
            AppLanguage.ARABIC -> "ليس الآن"
            AppLanguage.SPANISH -> "Ahora no"
            AppLanguage.FRENCH -> "Plus tard"
            AppLanguage.GERMAN -> "Später"
            AppLanguage.RUSSIAN -> "Позже"
            AppLanguage.PORTUGUESE -> "Agora não"
            AppLanguage.INDONESIAN -> "Nanti Saja"
            else -> "Not Now"
        }
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
    language: AppLanguage = AppLanguage.INDONESIAN,
    onReadPrivacyPolicy: (() -> Unit)? = null
) {
    val resolvedLang = AppStrings.resolveLang(language)
    val config = when (type) {
        DisclosureType.LOCATION -> DisclosureConfig.forLocation(resolvedLang)
        DisclosureType.CAMERA -> DisclosureConfig.forCamera(resolvedLang)
        DisclosureType.NOTIFICATION -> DisclosureConfig.forNotification(resolvedLang)
        DisclosureType.DATA_PRIVACY -> DisclosureConfig.forDataPrivacy(resolvedLang)
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        BoxWithConstraints(
            modifier = modifier
                .fillMaxSize()
                .statusBarsPadding(),
            contentAlignment = Alignment.BottomCenter
        ) {
            val topPadding = if (maxHeight < 640.dp) 80.dp else 140.dp
            Card(
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = topPadding)
                    .fillMaxHeight()
                    .testTag("dialog_permission_disclosure_${type.name.lowercase()}")
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 24.dp, end = 24.dp, top = 22.dp, bottom = 12.dp)
                        .navigationBarsPadding()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                    ) {
                        // Ikon Ilustrasi Elegan di Lingkaran Lembut
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(88.dp)
                                .clip(CircleShape)
                                .background(config.iconBgColor)
                                .border(2.dp, config.iconColor.copy(alpha = 0.2f), CircleShape)
                        ) {
                            // Lingkaran konsentris dalam
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(66.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.85f))
                            ) {
                                Icon(
                                    imageVector = config.icon,
                                    contentDescription = config.title,
                                    tint = config.iconColor,
                                    modifier = Modifier.size(38.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Judul Deklarasi
                        Text(
                            text = config.title,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeutralDark,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Deskripsi Penjelasan Informatif
                        Text(
                            text = config.description,
                            fontSize = 13.5.sp,
                            lineHeight = 20.sp,
                            color = NeutralMedium,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

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
                    }

                    // Bottom Action Area
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 14.dp)
                    ) {
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
                            Spacer(modifier = Modifier.height(4.dp))
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
                            Spacer(modifier = Modifier.height(2.dp))
                            val privacyLabel = when (resolvedLang) {
                                AppLanguage.CHINESE -> "阅读隐私政策"
                                AppLanguage.JAPANESE -> "プライバシーポリシーを読む"
                                AppLanguage.KOREAN -> "개인정보 처리방침 읽기"
                                AppLanguage.ARABIC -> "قراءة سياسة الخصوصية"
                                AppLanguage.SPANISH -> "Leer Política de Privacidad"
                                AppLanguage.FRENCH -> "Lire la politique de confidentialité"
                                AppLanguage.GERMAN -> "Datenschutzerklärung lesen"
                                AppLanguage.RUSSIAN -> "Читать Политику конфиденциальности"
                                AppLanguage.PORTUGUESE -> "Ler Política de Privacidade"
                                AppLanguage.INDONESIAN -> "Baca Kebijakan Privasi"
                                else -> "Read Privacy Policy"
                            }
                            Text(
                                text = privacyLabel,
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
}
