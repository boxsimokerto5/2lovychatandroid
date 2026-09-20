package com.example.util

object AppStrings {

    fun resolveLang(lang: AppLanguage): AppLanguage {
        return if (lang == AppLanguage.LOCAL) {
            GeoLanguageDetector.detectLocalLanguage()
        } else {
            lang
        }
    }

    // Motto (New friends, fun friends)
    fun motto(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "结识新朋友，畅享新乐趣"
        AppLanguage.JAPANESE -> "新しい友達、楽しい時間"
        AppLanguage.KOREAN -> "새로운 친구, 즐거운 만남"
        AppLanguage.ARABIC -> "أصدقاء جدد، لحظات ممتعة"
        AppLanguage.SPANISH -> "Nuevos amigos, momentos divertidos"
        AppLanguage.FRENCH -> "Nouveaux amis, moments formidables"
        AppLanguage.GERMAN -> "Neue Freunde, tolle Momente"
        AppLanguage.RUSSIAN -> "Новые друзья, яркие моменты"
        AppLanguage.PORTUGUESE -> "Novos amigos, momentos divertidos"
        AppLanguage.INDONESIAN -> "Teman baru, teman seru"
        else -> "New friends, fun friends"
    }

    // Tabs
    fun tabLive(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "直播"
        AppLanguage.JAPANESE -> "ライブ"
        AppLanguage.KOREAN -> "라이브"
        AppLanguage.ARABIC -> "بث مباشر"
        AppLanguage.SPANISH -> "En vivo"
        AppLanguage.FRENCH -> "En direct"
        AppLanguage.GERMAN -> "Live"
        AppLanguage.RUSSIAN -> "Эфир"
        AppLanguage.PORTUGUESE -> "Ao vivo"
        AppLanguage.INDONESIAN -> "Siaran"
        else -> "Live"
    }

    fun tabChat(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "聊天"
        AppLanguage.JAPANESE -> "チャット"
        AppLanguage.KOREAN -> "채팅"
        AppLanguage.ARABIC -> "دردشة"
        AppLanguage.SPANISH -> "Chats"
        AppLanguage.FRENCH -> "Discussions"
        AppLanguage.GERMAN -> "Chats"
        AppLanguage.RUSSIAN -> "Чаты"
        AppLanguage.PORTUGUESE -> "Conversas"
        AppLanguage.INDONESIAN -> "Obrolan"
        else -> "Chats"
    }

    fun tabFriends(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "好友"
        AppLanguage.JAPANESE -> "友達"
        AppLanguage.KOREAN -> "친구"
        AppLanguage.ARABIC -> "الأصدقاء"
        AppLanguage.SPANISH -> "Amigos"
        AppLanguage.FRENCH -> "Amis"
        AppLanguage.GERMAN -> "Freunde"
        AppLanguage.RUSSIAN -> "Друзья"
        AppLanguage.PORTUGUESE -> "Amigos"
        AppLanguage.INDONESIAN -> "Teman"
        else -> "Friends"
    }

    fun tabDiscover(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "探索"
        AppLanguage.JAPANESE -> "見つける"
        AppLanguage.KOREAN -> "탐색"
        AppLanguage.ARABIC -> "استكشاف"
        AppLanguage.SPANISH -> "Explorar"
        AppLanguage.FRENCH -> "Découvrir"
        AppLanguage.GERMAN -> "Entdecken"
        AppLanguage.RUSSIAN -> "Обзор"
        AppLanguage.PORTUGUESE -> "Explorar"
        AppLanguage.INDONESIAN -> "Temukan"
        else -> "Discover"
    }

    fun tabMoments(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "动态"
        AppLanguage.JAPANESE -> "モーメント"
        AppLanguage.KOREAN -> "모먼트"
        AppLanguage.ARABIC -> "لحظات"
        AppLanguage.SPANISH -> "Momentos"
        AppLanguage.FRENCH -> "Moments"
        AppLanguage.GERMAN -> "Momente"
        AppLanguage.RUSSIAN -> "Моменты"
        AppLanguage.PORTUGUESE -> "Momentos"
        AppLanguage.INDONESIAN -> "Momen"
        else -> "Moments"
    }

    fun tabProfile(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "我的"
        AppLanguage.JAPANESE -> "マイページ"
        AppLanguage.KOREAN -> "프로필"
        AppLanguage.ARABIC -> "صفحتي"
        AppLanguage.SPANISH -> "Perfil"
        AppLanguage.FRENCH -> "Profil"
        AppLanguage.GERMAN -> "Profil"
        AppLanguage.RUSSIAN -> "Профиль"
        AppLanguage.PORTUGUESE -> "Perfil"
        AppLanguage.INDONESIAN -> "Saya"
        else -> "Profile"
    }

    // Splash
    fun splashSub(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "附近的人 • 漂流瓶 • 精彩动态"
        AppLanguage.JAPANESE -> "近くの人 • ボトルメッセージ • モーメント"
        AppLanguage.KOREAN -> "주변 사람 • 유리병 편지 • 모먼트"
        AppLanguage.ARABIC -> "أشخاص بالجوار • رسالة في زجاجة • لحظات"
        AppLanguage.SPANISH -> "Personas cercanas • Mensajes en botella • Momentos"
        AppLanguage.INDONESIAN -> "Teman Sekitar • Pesan Botol • Momen"
        else -> "Nearby Friends • Bottle Messages • Moments"
    }

    // Login Screen
    fun welcomeBack(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "欢迎回来"
        AppLanguage.JAPANESE -> "おかえりなさい"
        AppLanguage.KOREAN -> "다시 오신 것을 환영합니다"
        AppLanguage.ARABIC -> "مرحباً بعودتك"
        AppLanguage.SPANISH -> "Bienvenido de nuevo"
        AppLanguage.FRENCH -> "Bon retour"
        AppLanguage.GERMAN -> "Willkommen zurück"
        AppLanguage.RUSSIAN -> "С возвращением"
        AppLanguage.PORTUGUESE -> "Bem-vindo de volta"
        AppLanguage.INDONESIAN -> "Selamat Datang Kembali"
        else -> "Welcome Back"
    }

    fun createAccount(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "创建新账号"
        AppLanguage.JAPANESE -> "新規アカウント作成"
        AppLanguage.KOREAN -> "새 계정 만들기"
        AppLanguage.ARABIC -> "إنشاء حساب جديد"
        AppLanguage.SPANISH -> "Crear cuenta nueva"
        AppLanguage.FRENCH -> "Créer un compte"
        AppLanguage.GERMAN -> "Konto erstellen"
        AppLanguage.RUSSIAN -> "Создать аккаунт"
        AppLanguage.PORTUGUESE -> "Criar nova conta"
        AppLanguage.INDONESIAN -> "Buat Akun Baru"
        else -> "Create New Account"
    }

    fun loginSubtitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "登录以开始聊天并分享生活动态"
        AppLanguage.JAPANESE -> "ログインしてチャットと投稿をはじめよう"
        AppLanguage.KOREAN -> "로그인하여 채팅과 모먼트를 시작하세요"
        AppLanguage.ARABIC -> "سجل الدخول لبدء الدردشة ومشاركة اللحظات"
        AppLanguage.SPANISH -> "Inicia sesión para chatear y compartir momentos"
        AppLanguage.FRENCH -> "Connectez-vous pour discuter et partager"
        AppLanguage.GERMAN -> "Melden Sie sich an, um zu chatten"
        AppLanguage.RUSSIAN -> "Войдите, чтобы общаться и делиться моментами"
        AppLanguage.PORTUGUESE -> "Entre para conversar e compartilhar momentos"
        AppLanguage.INDONESIAN -> "Masuk untuk mulai mengobrol & berbagi momen"
        else -> "Sign in to start chatting & sharing moments"
    }

    fun signUpSubtitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "注册并发现你身边的新朋友"
        AppLanguage.JAPANESE -> "登録してあなたの周りの新しい友達を見つけよう"
        AppLanguage.KOREAN -> "가입하고 주변의 새로운 친구를 찾아보세요"
        AppLanguage.ARABIC -> "سجل واكتشف أصدقاء جدد حولك"
        AppLanguage.SPANISH -> "Regístrate y descubre nuevos amigos a tu alrededor"
        AppLanguage.FRENCH -> "Inscrivez-vous et découvrez de nouveaux amis"
        AppLanguage.GERMAN -> "Registrieren Sie sich und finden Sie neue Freunde"
        AppLanguage.RUSSIAN -> "Зарегистрируйтесь и найдите новых друзей рядом"
        AppLanguage.PORTUGUESE -> "Cadastre-se e descubra novos amigos ao seu redor"
        AppLanguage.INDONESIAN -> "Daftar dan temukan teman baru di sekitarmu"
        else -> "Sign up and discover new friends around you"
    }

    fun usernameLabel(lang: AppLanguage, isSignUp: Boolean): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> if (isSignUp) "昵称 / 用户名" else "用户名或 Lovy ID"
        AppLanguage.JAPANESE -> if (isSignUp) "ニックネーム / ユーザー名" else "ユーザー名またはID"
        AppLanguage.KOREAN -> if (isSignUp) "닉네임 / 사용자 이름" else "사용자 이름 또는 ID"
        AppLanguage.ARABIC -> if (isSignUp) "اسم المستخدم" else "اسم المستخدم أو معرف Lovy"
        AppLanguage.SPANISH -> if (isSignUp) "Apodo / Usuario" else "Usuario o ID Lovy"
        AppLanguage.INDONESIAN -> if (isSignUp) "Nama Panggilan / Username" else "Username atau ID Lovy"
        else -> if (isSignUp) "Nickname / Username" else "Username or Lovy ID"
    }

    fun usernamePlaceholder(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "例如：小林，小雨，或 lovy_user"
        AppLanguage.JAPANESE -> "例：サクラ、レン、または lovy_user"
        AppLanguage.KOREAN -> "예: 민준, 서연 또는 lovy_user"
        AppLanguage.ARABIC -> "مثال: أحمد، سارة، أو lovy_user"
        AppLanguage.SPANISH -> "Ejemplo: Carlos, Sofía o lovy_user"
        AppLanguage.INDONESIAN -> "Contoh: Rania, Dimas, atau lovy_user"
        else -> "e.g. Alex, Sarah, or lovy_user"
    }

    fun passwordLabel(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "密码"
        AppLanguage.JAPANESE -> "パスワード"
        AppLanguage.KOREAN -> "비밀번호"
        AppLanguage.ARABIC -> "كلمة المرور"
        AppLanguage.SPANISH -> "Contraseña"
        AppLanguage.FRENCH -> "Mot de passe"
        AppLanguage.GERMAN -> "Passwort"
        AppLanguage.RUSSIAN -> "Пароль"
        AppLanguage.PORTUGUESE -> "Senha"
        AppLanguage.INDONESIAN -> "Kata Sandi"
        else -> "Password"
    }

    fun passwordPlaceholder(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "至少 4 个字符"
        AppLanguage.JAPANESE -> "4文字以上"
        AppLanguage.KOREAN -> "최소 4자 이상"
        AppLanguage.ARABIC -> "4 أحرف على الأقل"
        AppLanguage.SPANISH -> "Al menos 4 caracteres"
        AppLanguage.INDONESIAN -> "Minimal 4 karakter"
        else -> "At least 4 characters"
    }

    fun btnLogin(lang: AppLanguage, isSignUp: Boolean): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> if (isSignUp) "立即注册" else "登录 Lovy Chat"
        AppLanguage.JAPANESE -> if (isSignUp) "新規登録" else "ログイン"
        AppLanguage.KOREAN -> if (isSignUp) "지금 가입하기" else "Lovy Chat 로그인"
        AppLanguage.ARABIC -> if (isSignUp) "سجل الآن" else "تسجيل الدخول"
        AppLanguage.SPANISH -> if (isSignUp) "Registrarse ahora" else "Iniciar sesión"
        AppLanguage.FRENCH -> if (isSignUp) "S'inscrire" else "Se connecter"
        AppLanguage.GERMAN -> if (isSignUp) "Jetzt registrieren" else "Anmelden"
        AppLanguage.RUSSIAN -> if (isSignUp) "Зарегистрироваться" else "Войти"
        AppLanguage.PORTUGUESE -> if (isSignUp) "Cadastrar agora" else "Entrar"
        AppLanguage.INDONESIAN -> if (isSignUp) "Daftar Sekarang" else "Masuk ke Lovy Chat"
        else -> if (isSignUp) "Register Now" else "Sign In to Lovy Chat"
    }

    fun btnGuest(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "以访客身份继续（探索）"
        AppLanguage.JAPANESE -> "ゲストとして続ける（体験）"
        AppLanguage.KOREAN -> "게스트로 둘러보기"
        AppLanguage.ARABIC -> "المتابعة كضيف (استكشاف)"
        AppLanguage.SPANISH -> "Continuar como invitado"
        AppLanguage.FRENCH -> "Continuer comme invité"
        AppLanguage.GERMAN -> "Als Gast fortfahren"
        AppLanguage.RUSSIAN -> "Войти как гость"
        AppLanguage.PORTUGUESE -> "Continuar como convidado"
        AppLanguage.INDONESIAN -> "Masuk Sebagai Tamu (Eksplorasi)"
        else -> "Continue as Guest (Explore)"
    }

    fun alreadyHaveAccount(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "已有账号？"
        AppLanguage.JAPANESE -> "すでにアカウントをお持ちですか？"
        AppLanguage.KOREAN -> "이미 계정이 있으신가요?"
        AppLanguage.ARABIC -> "هل لديك حساب بالفعل؟"
        AppLanguage.SPANISH -> "¿Ya tienes una cuenta?"
        AppLanguage.INDONESIAN -> "Sudah punya akun?"
        else -> "Already have an account?"
    }

    fun dontHaveAccount(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "还没有账号？"
        AppLanguage.JAPANESE -> "アカウントをお持ちでないですか？"
        AppLanguage.KOREAN -> "계정이 없으신가요?"
        AppLanguage.ARABIC -> "ليس لديك حساب؟"
        AppLanguage.SPANISH -> "¿No tienes una cuenta?"
        AppLanguage.INDONESIAN -> "Belum punya akun?"
        else -> "Don't have an account?"
    }

    fun signInAction(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "登录"
        AppLanguage.JAPANESE -> "ログイン"
        AppLanguage.KOREAN -> "로그인"
        AppLanguage.ARABIC -> "دخول"
        AppLanguage.SPANISH -> "Entrar"
        AppLanguage.FRENCH -> "Connexion"
        AppLanguage.GERMAN -> "Anmelden"
        AppLanguage.RUSSIAN -> "Войти"
        AppLanguage.PORTUGUESE -> "Entrar"
        AppLanguage.INDONESIAN -> "Masuk"
        else -> "Sign In"
    }

    fun signUpAction(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "注册"
        AppLanguage.JAPANESE -> "登録"
        AppLanguage.KOREAN -> "가입"
        AppLanguage.ARABIC -> "تسجيل"
        AppLanguage.SPANISH -> "Registrarse"
        AppLanguage.FRENCH -> "S'inscrire"
        AppLanguage.GERMAN -> "Registrieren"
        AppLanguage.RUSSIAN -> "Регистрация"
        AppLanguage.PORTUGUESE -> "Cadastrar"
        AppLanguage.INDONESIAN -> "Daftar Baru"
        else -> "Sign Up"
    }

    // Discover Screen
    fun discoverTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "探索"
        AppLanguage.JAPANESE -> "見つける"
        AppLanguage.KOREAN -> "탐색"
        AppLanguage.ARABIC -> "استكشاف"
        AppLanguage.SPANISH -> "Explorar"
        AppLanguage.INDONESIAN -> "Temukan"
        else -> "Discover"
    }

    fun discoverSubtitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "探索世界，建立美好连接"
        AppLanguage.JAPANESE -> "新しい出会いと世界を探検しよう"
        AppLanguage.KOREAN -> "새로운 인연과 세상을 만나보세요"
        AppLanguage.ARABIC -> "استكشف وتواصل مع أصدقاء جدد"
        AppLanguage.SPANISH -> "Explora y haz nuevas conexiones"
        AppLanguage.INDONESIAN -> "Jelajahi dan temukan koneksi baru"
        else -> "Explore and make new connections"
    }

    fun menuNearby(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "附近的人"
        AppLanguage.JAPANESE -> "近くの人"
        AppLanguage.KOREAN -> "주변 사람"
        AppLanguage.ARABIC -> "أشخاص بالجوار"
        AppLanguage.SPANISH -> "Personas cercanas"
        AppLanguage.FRENCH -> "Personnes à proximité"
        AppLanguage.GERMAN -> "Leute in der Nähe"
        AppLanguage.RUSSIAN -> "Люди рядом"
        AppLanguage.PORTUGUESE -> "Pessoas por perto"
        AppLanguage.INDONESIAN -> "Orang di Sekitar"
        else -> "People Nearby"
    }

    fun menuNearbySub(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "寻找身边的朋友，雷达实时探测"
        AppLanguage.JAPANESE -> "位置情報とレーダーで周りの友達を見つける"
        AppLanguage.KOREAN -> "레이더로 주변 친구를 실시간으로 탐색"
        AppLanguage.ARABIC -> "اعثر على أصدقاء جدد بالقرب من موقعك"
        AppLanguage.SPANISH -> "Encuentra nuevos amigos cerca de tu ubicación"
        AppLanguage.INDONESIAN -> "Temukan teman baru di sekelilingmu"
        else -> "Find new friends around your location"
    }

    fun menuBottle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "漂流瓶"
        AppLanguage.JAPANESE -> "ボトルメッセージ"
        AppLanguage.KOREAN -> "유리병 편지"
        AppLanguage.ARABIC -> "رسالة في زجاجة"
        AppLanguage.SPANISH -> "Mensaje en botella"
        AppLanguage.FRENCH -> "Bouteille à la mer"
        AppLanguage.GERMAN -> "Flaschenpost"
        AppLanguage.RUSSIAN -> "Послание в бутылке"
        AppLanguage.PORTUGUESE -> "Mensagem na garrafa"
        AppLanguage.INDONESIAN -> "Pesan dalam Botol"
        else -> "Message in a Bottle"
    }

    fun menuBottleSub(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "在大海中投掷与捞取神秘故事"
        AppLanguage.JAPANESE -> "海へ流し、見知らぬ誰かの物語を釣り上げよう"
        AppLanguage.KOREAN -> "바다에 이야기를 띄우고 건져보세요"
        AppLanguage.ARABIC -> "ارمِ واصطد قصصاً عشوائية عبر المحيط"
        AppLanguage.SPANISH -> "Lanza y pesca historias a través del océano"
        AppLanguage.INDONESIAN -> "Lempar & pancing cerita di lautan"
        else -> "Toss & fish random stories across the ocean"
    }

    fun menuMoments(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "动态与故事"
        AppLanguage.JAPANESE -> "モーメント＆投稿"
        AppLanguage.KOREAN -> "모먼트 & 스토리"
        AppLanguage.ARABIC -> "لحظات وقصص"
        AppLanguage.SPANISH -> "Momentos e historias"
        AppLanguage.INDONESIAN -> "Momen & Status"
        else -> "Moments & Stories"
    }

    fun menuMomentsSub(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "查看好友分享的最新美好瞬间"
        AppLanguage.JAPANESE -> "友達の最新の投稿や日常をチェック"
        AppLanguage.KOREAN -> "친구들의 최신 일상 이야기를 확인하세요"
        AppLanguage.ARABIC -> "تابع أحدث تحديثات وقصص الأصدقاء"
        AppLanguage.SPANISH -> "Mira las últimas publicaciones de amigos"
        AppLanguage.INDONESIAN -> "Lihat cerita terbaru dari teman"
        else -> "Check out updates from friends"
    }

    // Profile Screen
    fun profileTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "我的"
        AppLanguage.JAPANESE -> "マイページ"
        AppLanguage.KOREAN -> "프로필"
        AppLanguage.ARABIC -> "صفحتي"
        AppLanguage.SPANISH -> "Perfil"
        AppLanguage.INDONESIAN -> "Saya"
        else -> "Profile"
    }

    fun menuSupabase(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "云端同步"
        AppLanguage.JAPANESE -> "クラウド同期"
        AppLanguage.INDONESIAN -> "Sinkronisasi Cloud"
        else -> "Cloud Sync"
    }

    fun menuSupabaseSub(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "同步消息与动态设置"
        AppLanguage.JAPANESE -> "メッセージとストーリーの同期設定"
        AppLanguage.INDONESIAN -> "Sinkronisasi pesan & status"
        else -> "Sync messages & stories"
    }

    fun menuAbout(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "关于 Lovy Chat"
        AppLanguage.JAPANESE -> "Lovy Chat について"
        AppLanguage.KOREAN -> "Lovy Chat 정보"
        AppLanguage.ARABIC -> "حول Lovy Chat"
        AppLanguage.SPANISH -> "Acerca de Lovy Chat"
        AppLanguage.INDONESIAN -> "Tentang Lovy Chat"
        else -> "About Lovy Chat"
    }

    fun menuLogout(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "退出登录"
        AppLanguage.JAPANESE -> "ログアウト"
        AppLanguage.KOREAN -> "로그아웃"
        AppLanguage.ARABIC -> "تسجيل الخروج"
        AppLanguage.SPANISH -> "Cerrar sesión"
        AppLanguage.FRENCH -> "Déconnexion"
        AppLanguage.GERMAN -> "Abmelden"
        AppLanguage.RUSSIAN -> "Выйти из аккаунта"
        AppLanguage.PORTUGUESE -> "Sair da conta"
        AppLanguage.INDONESIAN -> "Keluar Akun"
        else -> "Sign Out"
    }

    fun menuLogoutSub(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "切换账号或返回登录界面"
        AppLanguage.JAPANESE -> "アカウントを切り替えるかログイン画面に戻る"
        AppLanguage.ARABIC -> "تبديل الحساب أو العودة لتسجيل الدخول"
        AppLanguage.SPANISH -> "Cambiar de cuenta o volver al inicio"
        AppLanguage.INDONESIAN -> "Ganti akun atau kembali ke layar masuk"
        else -> "Switch account or return to login screen"
    }

    fun languageSetting(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "语言设置 (LO - 本地自动 / EN)"
        AppLanguage.JAPANESE -> "言語設定 (LO - 現地自動 / EN)"
        AppLanguage.KOREAN -> "언어 설정 (LO - 자동 로컬 / EN)"
        AppLanguage.ARABIC -> "إعدادات اللغة (LO - محلي تلقائي / EN)"
        AppLanguage.SPANISH -> "Configuración de idioma (LO / EN)"
        AppLanguage.INDONESIAN -> "Bahasa (LO - Otomatis Lokal / EN)"
        else -> "Language (LO - Auto Local / EN)"
    }

    fun currentGeoHint(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "检测到当前国家语言：中文"
        AppLanguage.JAPANESE -> "現在の言語を検出：日本語"
        AppLanguage.KOREAN -> "현재 국가 언어 감지: 한국어"
        AppLanguage.ARABIC -> "تم اكتشاف لغة المنطقة: العربية"
        AppLanguage.SPANISH -> "Idioma local detectado: Español"
        AppLanguage.INDONESIAN -> "Terdeteksi bahasa lokal: Bahasa Indonesia"
        else -> "Detected local country language: English"
    }

    // Nearby Screen
    fun nearbyTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "附近的人"
        AppLanguage.JAPANESE -> "近くの人"
        AppLanguage.KOREAN -> "주변 사람"
        AppLanguage.ARABIC -> "أشخاص بالجوار"
        AppLanguage.SPANISH -> "Personas cercanas"
        AppLanguage.INDONESIAN -> "Orang di Sekitar"
        else -> "People Nearby"
    }

    fun nearbyScanning(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "正在扫描附近雷达..."
        AppLanguage.JAPANESE -> "周囲のレーダーをスキャン中..."
        AppLanguage.KOREAN -> "주변 레이더 스캔 중..."
        AppLanguage.ARABIC -> "جاري مسح الرادار القريب..."
        AppLanguage.SPANISH -> "Escaneando radar cercano..."
        AppLanguage.INDONESIAN -> "Memindai radar sekitarmu..."
        else -> "Scanning radar nearby..."
    }

    fun nearbyEmpty(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "雷达范围内暂无好友"
        AppLanguage.JAPANESE -> "レーダー内にユーザーが見つかりません"
        AppLanguage.KOREAN -> "레이더에서 친구를 찾을 수 없습니다"
        AppLanguage.ARABIC -> "لم يتم العثور على مستخدمين في الرادار"
        AppLanguage.SPANISH -> "No se encontraron usuarios en el radar"
        AppLanguage.INDONESIAN -> "Belum ada teman di radar ini"
        else -> "No users found on radar"
    }

    fun filterAll(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "全部"
        AppLanguage.JAPANESE -> "すべて"
        AppLanguage.KOREAN -> "전체"
        AppLanguage.ARABIC -> "الكل"
        AppLanguage.SPANISH -> "Todos"
        AppLanguage.INDONESIAN -> "Semua"
        else -> "All"
    }

    fun filterMale(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "男生"
        AppLanguage.JAPANESE -> "男性"
        AppLanguage.KOREAN -> "남성"
        AppLanguage.ARABIC -> "ذكور"
        AppLanguage.SPANISH -> "Hombres"
        AppLanguage.INDONESIAN -> "Pria"
        else -> "Male"
    }

    fun filterFemale(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "女生"
        AppLanguage.JAPANESE -> "女性"
        AppLanguage.KOREAN -> "여성"
        AppLanguage.ARABIC -> "إناث"
        AppLanguage.SPANISH -> "Mujeres"
        AppLanguage.INDONESIAN -> "Wanita"
        else -> "Female"
    }

    fun btnLoadMoreNearby(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "发现更多附近的人（看广告）"
        AppLanguage.JAPANESE -> "もっと探す（広告を視聴）"
        AppLanguage.KOREAN -> "더 많은 사람 찾기 (광고 시청)"
        AppLanguage.ARABIC -> "البحث عن المزيد (مشاهدة إعلان)"
        AppLanguage.SPANISH -> "Buscar más personas (Ver anuncio)"
        AppLanguage.INDONESIAN -> "Cari Lebih Banyak (Tonton Iklan)"
        else -> "Find More People (Watch Ad)"
    }

    fun btnLoadMoreNearbyTitle(lang: AppLanguage, hiddenCount: Int): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "附近还有 $hiddenCount+ 人！"
        AppLanguage.JAPANESE -> "あなたの周りにまだ $hiddenCount+ 人います！"
        AppLanguage.KOREAN -> "주변에 아직 $hiddenCount+ 명의 친구가 더 있습니다!"
        AppLanguage.ARABIC -> "لا يزال هناك $hiddenCount+ أشخاص بالقرب منك!"
        AppLanguage.SPANISH -> "¡Hay más de $hiddenCount personas cerca!"
        AppLanguage.INDONESIAN -> "Masih Ada $hiddenCount+ Orang di Sekitarmu!"
        else -> "There are $hiddenCount+ More People Nearby!"
    }

    fun btnLoadMoreNearbyDesc(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "观看短视频广告即可解锁并查看您身边的所有好友。"
        AppLanguage.JAPANESE -> "短い動画広告を視聴して、周りの友達をすべて表示します。"
        AppLanguage.KOREAN -> "짧은 동영상 광고를 시청하고 주변의 모든 친구를 확인하세요."
        AppLanguage.ARABIC -> "شاهد إعلاناً قصيراً لفتح وعرض جميع الأصدقاء في منطقتك."
        AppLanguage.SPANISH -> "Mira un breve anuncio en video para ver a todos los amigos a tu alrededor."
        AppLanguage.INDONESIAN -> "Tonton iklan video singkat untuk membuka dan melihat semua teman yang ada di sekitarmu."
        else -> "Watch a short video ad to unlock and view all friends in your area."
    }

    fun allNearbyLoaded(lang: AppLanguage, totalCount: Int): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "周围全部 $totalCount 位好友已显示 ✨"
        AppLanguage.JAPANESE -> "周囲の $totalCount 人のユーザーをすべて表示しました ✨"
        AppLanguage.KOREAN -> "주변의 $totalCount 명의 사용자가 모두 표시되었습니다 ✨"
        AppLanguage.ARABIC -> "تم عرض جميع المستخدمين القريبين البالغ عددهم $totalCount ✨"
        AppLanguage.SPANISH -> "Se muestran todos los $totalCount usuarios cercanos ✨"
        AppLanguage.INDONESIAN -> "Semua $totalCount pengguna di sekitarmu telah ditampilkan ✨"
        else -> "All $totalCount nearby users are now displayed ✨"
    }

    // Bottle Screen
    fun bottleTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "漂流瓶"
        AppLanguage.JAPANESE -> "ボトルメッセージ"
        AppLanguage.KOREAN -> "유리병 편지"
        AppLanguage.ARABIC -> "رسالة في زجاجة"
        AppLanguage.SPANISH -> "Mensaje en botella"
        AppLanguage.INDONESIAN -> "Pesan dalam Botol"
        else -> "Message in a Bottle"
    }

    fun btnTossBottle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "扔瓶子"
        AppLanguage.JAPANESE -> "ボトルを流す"
        AppLanguage.KOREAN -> "편지 띄우기"
        AppLanguage.ARABIC -> "رمي زجاجة"
        AppLanguage.SPANISH -> "Lanzar botella"
        AppLanguage.INDONESIAN -> "Lempar Botol"
        else -> "Toss a Bottle"
    }

    fun btnFishBottle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "捞瓶子"
        AppLanguage.JAPANESE -> "ボトルを釣る"
        AppLanguage.KOREAN -> "편지 낚기"
        AppLanguage.ARABIC -> "اصطياد زجاجة"
        AppLanguage.SPANISH -> "Pescar botella"
        AppLanguage.INDONESIAN -> "Pancing Botol"
        else -> "Fish a Bottle"
    }

    // Radar Strings
    fun radarActive(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "雷达已开启 • 5公里范围"
        AppLanguage.JAPANESE -> "レーダー起動中 • 半径5km"
        AppLanguage.KOREAN -> "레이더 활성화 • 반경 5km"
        AppLanguage.ARABIC -> "الرادار نشط • نطاق 5 كم"
        AppLanguage.SPANISH -> "Radar activo • Radio 5 km"
        AppLanguage.FRENCH -> "Radar actif • Rayon 5 km"
        AppLanguage.GERMAN -> "Radar aktiv • 5 km Radius"
        AppLanguage.RUSSIAN -> "Радар активен • Радиус 5 км"
        AppLanguage.PORTUGUESE -> "Radar ativo • Raio 5 km"
        AppLanguage.INDONESIAN -> "Radar Aktif • Radius 5 km"
        else -> "Radar Active • 5 km Radius"
    }

    fun radarHint(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "点击雷达上的头像查看资料并打招呼"
        AppLanguage.JAPANESE -> "レーダー上のアバターをタップしてプロフィール確認や挨拶ができます"
        AppLanguage.KOREAN -> "레이더의 프로필을 터치하여 확인하고 인사하세요"
        AppLanguage.ARABIC -> "المس الصورة في الرادار لعرض الملف الشخصي والتحية"
        AppLanguage.SPANISH -> "Toca un avatar en el radar para ver su perfil y saludar"
        AppLanguage.INDONESIAN -> "Sentuh avatar di radar untuk melihat profil & menyapa"
        else -> "Tap avatars on radar to view profile & say hi"
    }

    fun sayHi(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "打招呼"
        AppLanguage.JAPANESE -> "あいさつ"
        AppLanguage.KOREAN -> "인사하기"
        AppLanguage.ARABIC -> "مرحباً"
        AppLanguage.SPANISH -> "Saludar"
        AppLanguage.FRENCH -> "Saluer"
        AppLanguage.GERMAN -> "Grüßen"
        AppLanguage.RUSSIAN -> "Привет"
        AppLanguage.PORTUGUESE -> "Dar oi"
        AppLanguage.INDONESIAN -> "Sapa"
        else -> "Say Hi"
    }
}
