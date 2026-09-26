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

    fun bannerTag(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "探索世界"
        AppLanguage.JAPANESE -> "世界を探検"
        AppLanguage.KOREAN -> "세상 탐색"
        AppLanguage.ARABIC -> "استكشف العالم"
        AppLanguage.SPANISH -> "EXPLORAR EL MUNDO"
        AppLanguage.INDONESIAN -> "JELAJAHI DUNIA"
        else -> "EXPLORE THE WORLD"
    }

    fun bannerTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "发现身边与远方的新朋友"
        AppLanguage.JAPANESE -> "近くや世界中の新しい友達を見つけよう"
        AppLanguage.KOREAN -> "가까운 곳과 먼 곳의 새로운 친구를 찾아보세요"
        AppLanguage.ARABIC -> "اكتشف أصدقاء جدد في الجوار وحول العالم"
        AppLanguage.SPANISH -> "Encuentra amigos cerca y en el mundo"
        AppLanguage.INDONESIAN -> "Temukan Teman Baru di Sekitarmu & Dunia"
        else -> "Find New Friends Nearby & Worldwide"
    }

    fun bannerDesc(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "使用附近雷达、漂流瓶与生活动态开始精彩连接"
        AppLanguage.JAPANESE -> "レーダー、ボトルメッセージ、日常の投稿で繋がろう"
        AppLanguage.KOREAN -> "레이더, 유리병 편지, 일상 스토리로 인연을 시작하세요"
        AppLanguage.ARABIC -> "استخدم الرادار وزجاجة الرسائل واللحظات للتواصل فوراً"
        AppLanguage.SPANISH -> "Usa el radar, mensajes en botella y momentos para conectar"
        AppLanguage.INDONESIAN -> "Gunakan radar sekitar, pesan botol, & linimasa cerita seru"
        else -> "Use nearby radar, bottle messages & stories to connect"
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

    fun profileTapToView(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "点击查看资料详情 →"
        AppLanguage.JAPANESE -> "タップしてプロフィール詳細を表示 →"
        AppLanguage.KOREAN -> "프로필 세부정보 보기 →"
        AppLanguage.ARABIC -> "انقر لعرض تفاصيل الملف الشخصي ←"
        AppLanguage.SPANISH -> "Toca para ver detalles del perfil →"
        AppLanguage.INDONESIAN -> "Ketuk untuk lihat detail profil →"
        else -> "Tap to view profile details →"
    }

    fun lovyIdLabel(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "Lovy ID"
        AppLanguage.JAPANESE -> "Lovy ID"
        AppLanguage.INDONESIAN -> "ID Lovy"
        else -> "Lovy ID"
    }

    fun menuUserProfile(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "个人资料详情"
        AppLanguage.JAPANESE -> "プロフィール詳細"
        AppLanguage.KOREAN -> "사용자 프로필 세부정보"
        AppLanguage.ARABIC -> "تفاصيل الملف الشخصي"
        AppLanguage.SPANISH -> "Detalles del perfil"
        AppLanguage.INDONESIAN -> "Detail Profil Pengguna"
        else -> "User Profile Details"
    }

    fun menuUserProfileSub(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "昵称、个人简介、照片及账号信息"
        AppLanguage.JAPANESE -> "表示名、自己紹介、写真、アカウント情報"
        AppLanguage.KOREAN -> "닉네임, 소개글, 사진 및 계정 정보"
        AppLanguage.ARABIC -> "الاسم والسيرة الذاتية والصور ومعلومات الحساب"
        AppLanguage.SPANISH -> "Nombre, biografía, fotos e información de cuenta"
        AppLanguage.INDONESIAN -> "Nama tampilan, bio, foto & info akun"
        else -> "Display name, bio, photos & account info"
    }

    fun menuMyMoments(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "我的动态"
        AppLanguage.JAPANESE -> "マイモーメント"
        AppLanguage.KOREAN -> "내 모먼트"
        AppLanguage.ARABIC -> "لحظاتي"
        AppLanguage.SPANISH -> "Mis momentos"
        AppLanguage.INDONESIAN -> "Momen Saya"
        else -> "My Moments"
    }

    fun menuMyMomentsSub(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "您的照片与日常动态收藏"
        AppLanguage.JAPANESE -> "日々の写真やストーリーのコレクション"
        AppLanguage.KOREAN -> "일상 사진 및 스토리 컬렉션"
        AppLanguage.ARABIC -> "مجموعتك من الصور والقصص اليومية"
        AppLanguage.SPANISH -> "Colección de fotos e historias diarias"
        AppLanguage.INDONESIAN -> "Koleksi foto dan cerita harianmu"
        else -> "Collection of your daily photos and stories"
    }

    fun menuMyBottles(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "我的漂流瓶"
        AppLanguage.JAPANESE -> "マイボトル"
        AppLanguage.KOREAN -> "내 바다 유리병"
        AppLanguage.ARABIC -> "رسائلي في الزجاجة"
        AppLanguage.SPANISH -> "Mis botellas del océano"
        AppLanguage.INDONESIAN -> "Botol Lautan Saya"
        else -> "My Ocean Bottles"
    }

    fun menuMyBottlesSub(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "您扔进大海的漂流瓶列表"
        AppLanguage.JAPANESE -> "あなたが海に流したボトル一覧"
        AppLanguage.KOREAN -> "내가 바다에 띄운 유리병 목록"
        AppLanguage.ARABIC -> "قائمة الزجاجات التي قمت برميها"
        AppLanguage.SPANISH -> "Lista de botellas que has lanzado"
        AppLanguage.INDONESIAN -> "Daftar botol yang pernah kamu lempar"
        else -> "List of bottles you have thrown"
    }

    fun menuPrivacyLocation(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "隐私与位置"
        AppLanguage.JAPANESE -> "プライバシーと位置情報"
        AppLanguage.KOREAN -> "개인정보 및 위치"
        AppLanguage.ARABIC -> "الخصوصية والموقع"
        AppLanguage.SPANISH -> "Privacidad y ubicación"
        AppLanguage.INDONESIAN -> "Privasi & Lokasi"
        else -> "Privacy & Location"
    }

    fun privacyIncognitoActive(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "隐身模式已开启"
        AppLanguage.JAPANESE -> "ステルスモード有効"
        AppLanguage.KOREAN -> "시크릿 모드 활성화"
        AppLanguage.ARABIC -> "وضع التخفي نشط"
        AppLanguage.SPANISH -> "Modo incógnito activo"
        AppLanguage.INDONESIAN -> "Mode Penyamaran aktif"
        else -> "Incognito mode active"
    }

    fun privacyExactDistanceHidden(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "精确距离已隐藏"
        AppLanguage.JAPANESE -> "正確な距離は非公開"
        AppLanguage.KOREAN -> "정확한 거리 숨김"
        AppLanguage.ARABIC -> "المسافة الدقيقة مخفية"
        AppLanguage.SPANISH -> "Distancia exacta oculta"
        AppLanguage.INDONESIAN -> "Jarak persis disembunyikan"
        else -> "Exact distance hidden"
    }

    fun privacyNearbyVisible(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "附近可见已开启"
        AppLanguage.JAPANESE -> "周辺への表示有効"
        AppLanguage.KOREAN -> "주변 검색 노출 활성화"
        AppLanguage.ARABIC -> "الظهور بالقرب نشط"
        AppLanguage.SPANISH -> "Visibilidad cercana activa"
        AppLanguage.INDONESIAN -> "Visibilitas sekitar aktif"
        else -> "Nearby visibility active"
    }

    fun menuBlockedUsers(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "已屏蔽用户"
        AppLanguage.JAPANESE -> "ブロックしたユーザー"
        AppLanguage.KOREAN -> "차단된 사용자"
        AppLanguage.ARABIC -> "المستخدمون المحظورون"
        AppLanguage.SPANISH -> "Usuarios bloqueados"
        AppLanguage.INDONESIAN -> "Pengguna Diblokir"
        else -> "Blocked Users"
    }

    fun blockedUsersEmpty(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "没有已屏蔽的用户"
        AppLanguage.JAPANESE -> "ブロックしたユーザーはいません"
        AppLanguage.KOREAN -> "차단된 사용자가 없습니다"
        AppLanguage.ARABIC -> "لا يوجد مستخدمون محظورون"
        AppLanguage.SPANISH -> "No hay usuarios bloqueados"
        AppLanguage.INDONESIAN -> "Tidak ada pengguna diblokir"
        else -> "No blocked users"
    }

    fun menuTestNotification(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "测试通知与震动"
        AppLanguage.JAPANESE -> "通知とバイブのテスト"
        AppLanguage.KOREAN -> "알림 및 진동 테스트"
        AppLanguage.ARABIC -> "اختبار الإشعارات والاهتزاز"
        AppLanguage.SPANISH -> "Probar notificaciones y vibración"
        AppLanguage.INDONESIAN -> "Uji Notifikasi & Getar"
        else -> "Test Notifications & Vibration"
    }

    fun menuTestNotificationSub(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "点击测试弹窗声音与设备震动"
        AppLanguage.JAPANESE -> "タップしてポップアップ音と振動をテスト"
        AppLanguage.KOREAN -> "팝업 소리 및 기기 진동 테스트"
        AppLanguage.ARABIC -> "انقر لاختبار صوت الإشعار واهتزاز الجهاز"
        AppLanguage.SPANISH -> "Toca para probar sonido emergente y vibración"
        AppLanguage.INDONESIAN -> "Tekan untuk tes suara pop-up & getaran perangkat"
        else -> "Tap to test pop-up sound & device vibration"
    }

    fun menuPrivacyPolicy(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "隐私政策与条款"
        AppLanguage.JAPANESE -> "プライバシーポリシーと利用規約"
        AppLanguage.KOREAN -> "개인정보 처리방침 및 약관"
        AppLanguage.ARABIC -> "سياسة الخصوصية والشروط"
        AppLanguage.SPANISH -> "Política de privacidad y términos"
        AppLanguage.INDONESIAN -> "Kebijakan Privasi & Ketentuan"
        else -> "Privacy Policy & Terms"
    }

    fun menuPrivacyPolicySub(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "位置、相机、广告数据与账号权限指南"
        AppLanguage.JAPANESE -> "位置情報、カメラ、広告データ、アカウントの権限ガイド"
        AppLanguage.KOREAN -> "위치, 카메라, 광고 데이터 및 계정 권한 가이드"
        AppLanguage.ARABIC -> "دليل أذونات الموقع والكاميرا والإعلانات والحساب"
        AppLanguage.SPANISH -> "Guía de permisos de ubicación, cámara, anuncios y cuenta"
        AppLanguage.INDONESIAN -> "Panduan izin lokasi, kamera, data iklan & akun"
        else -> "Permissions guide for location, camera, ads & account data"
    }

    fun qrCodeButtonDesc(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "打开我的二维码"
        AppLanguage.JAPANESE -> "マイQRコードを開く"
        AppLanguage.INDONESIAN -> "Buka Kode QR Saya"
        else -> "Open My QR Code"
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

    fun menuDeleteAccount(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "注销账号"
        AppLanguage.JAPANESE -> "アカウント削除"
        AppLanguage.KOREAN -> "계정 삭제"
        AppLanguage.ARABIC -> "حذف الحساب"
        AppLanguage.SPANISH -> "Eliminar cuenta"
        AppLanguage.FRENCH -> "Supprimer le compte"
        AppLanguage.GERMAN -> "Konto löschen"
        AppLanguage.RUSSIAN -> "Удалить аккаунт"
        AppLanguage.PORTUGUESE -> "Excluir conta"
        AppLanguage.INDONESIAN -> "Hapus Akun"
        else -> "Delete Account"
    }

    fun menuDeleteAccountSub(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "永久删除个人资料、聊天记录与所有数据"
        AppLanguage.JAPANESE -> "プロフィール、チャット、全データを完全削除"
        AppLanguage.INDONESIAN -> "Hapus profil, pesan, dan data akun permanen"
        else -> "Permanently delete your profile, chats, and data"
    }

    fun deleteAccountDialogTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "永久注销账号？"
        AppLanguage.JAPANESE -> "アカウントを完全に削除しますか？"
        AppLanguage.INDONESIAN -> "Hapus Akun Permanen?"
        else -> "Delete Account Permanently?"
    }

    fun deleteAccountDialogDesc(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "此操作不可撤销。您的个人资料、聊天记录、好友关系和已发布的动态都将从服务器永久删除，符合 Google Play 隐私安全规范。"
        AppLanguage.JAPANESE -> "この操作は元に戻せません。プロフィール、チャット履歴、友達リスト、投稿したモーメントはサーバーから完全に削除されます。"
        AppLanguage.INDONESIAN -> "Tindakan ini tidak dapat dibatalkan. Seluruh data profil, riwayat percakapan, dan momen Anda akan dihapus secara permanen dari server Lovy Chat sesuai standar kebijakan privasi Google Play."
        else -> "This action cannot be undone. All your profile data, chat history, and moments will be permanently deleted from Lovy Chat servers in compliance with Google Play privacy policies."
    }

    fun deleteAccountConfirmButton(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "确认注销"
        AppLanguage.JAPANESE -> "削除する"
        AppLanguage.INDONESIAN -> "Hapus Akun Saya"
        else -> "Delete My Account"
    }

    fun deleteAccountCancelButton(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "取消"
        AppLanguage.JAPANESE -> "キャンセル"
        AppLanguage.INDONESIAN -> "Batal"
        else -> "Cancel"
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
    fun nearbyScanning(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "正在扫描附近雷达..."
        AppLanguage.JAPANESE -> "周囲のレーダーをスキャン中..."
        AppLanguage.KOREAN -> "주변 레이더 스캔 중..."
        AppLanguage.ARABIC -> "جاري مسح الرادار القريب..."
        AppLanguage.SPANISH -> "Escaneando radar cercano..."
        AppLanguage.FRENCH -> "Scan du radar à proximité..."
        AppLanguage.GERMAN -> "Radar in der Nähe wird gescannt..."
        AppLanguage.RUSSIAN -> "Сканирование радара поблизости..."
        AppLanguage.PORTUGUESE -> "Escaneando radar próximo..."
        AppLanguage.INDONESIAN -> "Memindai radar sekitarmu..."
        else -> "Scanning radar nearby..."
    }

    fun nearbyEmpty(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "雷达范围内暂无好友"
        AppLanguage.JAPANESE -> "レーダー内にユーザーが見つかりません"
        AppLanguage.KOREAN -> "레이더에서 친구를 찾을 수 없습니다"
        AppLanguage.ARABIC -> "لم يتم العثور على مستخدمين في الرادار"
        AppLanguage.SPANISH -> "No se encontraron usuarios en el radar"
        AppLanguage.FRENCH -> "Aucun utilisateur trouvé sur le radar"
        AppLanguage.GERMAN -> "Keine Benutzer auf dem Radar gefunden"
        AppLanguage.RUSSIAN -> "Пользователи на радаре не найдены"
        AppLanguage.PORTUGUESE -> "Nenhum usuário encontrado no radar"
        AppLanguage.INDONESIAN -> "Belum ada teman di radar ini"
        else -> "No users found on radar"
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

    // Common navigation & action
    fun btnBack(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "返回"
        AppLanguage.JAPANESE -> "戻る"
        AppLanguage.KOREAN -> "뒤로"
        AppLanguage.ARABIC -> "رجوع"
        AppLanguage.SPANISH -> "Volver"
        AppLanguage.FRENCH -> "Retour"
        AppLanguage.GERMAN -> "Zurück"
        AppLanguage.RUSSIAN -> "Назад"
        AppLanguage.PORTUGUESE -> "Voltar"
        AppLanguage.INDONESIAN -> "Kembali"
        else -> "Back"
    }

    fun btnSave(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "保存"
        AppLanguage.JAPANESE -> "保存"
        AppLanguage.KOREAN -> "저장"
        AppLanguage.ARABIC -> "حفظ"
        AppLanguage.SPANISH -> "Guardar"
        AppLanguage.FRENCH -> "Enregistrer"
        AppLanguage.GERMAN -> "Speichern"
        AppLanguage.RUSSIAN -> "Сохранить"
        AppLanguage.PORTUGUESE -> "Salvar"
        AppLanguage.INDONESIAN -> "Simpan"
        else -> "Save"
    }

    fun btnCancel(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "取消"
        AppLanguage.JAPANESE -> "キャンセル"
        AppLanguage.KOREAN -> "취소"
        AppLanguage.ARABIC -> "إلغاء"
        AppLanguage.SPANISH -> "Cancelar"
        AppLanguage.FRENCH -> "Annuler"
        AppLanguage.GERMAN -> "Abbrechen"
        AppLanguage.RUSSIAN -> "Отмена"
        AppLanguage.PORTUGUESE -> "Cancelar"
        AppLanguage.INDONESIAN -> "Batal"
        else -> "Cancel"
    }

    // Bottle Screen Strings
    fun oceanTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "Lovy 奇幻海洋"
        AppLanguage.JAPANESE -> "Lovy 魅惑の海"
        AppLanguage.KOREAN -> "Lovy 신비의 바다"
        AppLanguage.ARABIC -> "محيط Lovy الغامض"
        AppLanguage.SPANISH -> "Océano de Misterio Lovy"
        AppLanguage.FRENCH -> "Océan de Mystère Lovy"
        AppLanguage.GERMAN -> "Lovy Geheimnisvolles Meer"
        AppLanguage.RUSSIAN -> "Таинственный океан Lovy"
        AppLanguage.PORTUGUESE -> "Oceano Misterioso Lovy"
        AppLanguage.INDONESIAN -> "Lautan Misteri Lovy Chat"
        else -> "Lovy Mystery Ocean"
    }

    fun oceanSubtitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "投掷内心倾诉，或是捞起远方新朋友的漂流瓶"
        AppLanguage.JAPANESE -> "心の手紙を流すか、新しい友達からのボトルを釣りましょう"
        AppLanguage.KOREAN -> "마음을 담은 편지를 띄우거나 새로운 친구의 유리병을 낚아보세요"
        AppLanguage.ARABIC -> "ارمي كلمات قلبك أو اصطد رسائل من أصدقاء جدد حول العالم"
        AppLanguage.SPANISH -> "Lanza tus pensamientos o pesca mensajes de nuevos amigos"
        AppLanguage.FRENCH -> "Lancez vos pensées ou repêchez des messages de nouveaux amis"
        AppLanguage.GERMAN -> "Wirf deine Gedanken ins Meer oder fische Flaschen von neuen Freunden"
        AppLanguage.RUSSIAN -> "Отправьте послание в океан или выловите бутылку нового друга"
        AppLanguage.PORTUGUESE -> "Lance seus pensamentos ou pesque mensagens de novos amigos"
        AppLanguage.INDONESIAN -> "Lempar kata hatimu atau pancing pesan dari sahabat baru"
        else -> "Toss your thoughts or fish messages from new friends across the globe"
    }

    fun tabFishedBottles(lang: AppLanguage, count: Int): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "捞到的瓶子 ($count)"
        AppLanguage.JAPANESE -> "釣ったボトル ($count)"
        AppLanguage.KOREAN -> "낚은 유리병 ($count)"
        AppLanguage.ARABIC -> "الزجاجات المصطادة ($count)"
        AppLanguage.SPANISH -> "Pescadas ($count)"
        AppLanguage.FRENCH -> "Repêchées ($count)"
        AppLanguage.GERMAN -> "Gefischt ($count)"
        AppLanguage.RUSSIAN -> "Выловленные ($count)"
        AppLanguage.PORTUGUESE -> "Pescadas ($count)"
        AppLanguage.INDONESIAN -> "Botol Diambil ($count)"
        else -> "Fished Bottles ($count)"
    }

    fun tabMyBottles(lang: AppLanguage, count: Int): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "我的瓶子 ($count)"
        AppLanguage.JAPANESE -> "流したボトル ($count)"
        AppLanguage.KOREAN -> "내가 띄운 병 ($count)"
        AppLanguage.ARABIC -> "زجاجاتي ($count)"
        AppLanguage.SPANISH -> "Mis Botellas ($count)"
        AppLanguage.FRENCH -> "Mes Bouteilles ($count)"
        AppLanguage.GERMAN -> "Meine Flaschen ($count)"
        AppLanguage.RUSSIAN -> "Мои бутылки ($count)"
        AppLanguage.PORTUGUESE -> "Minhas Garrafas ($count)"
        AppLanguage.INDONESIAN -> "Botol Saya ($count)"
        else -> "My Bottles ($count)"
    }

    fun emptyFishedTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "暂无捞到的漂流瓶"
        AppLanguage.JAPANESE -> "まだ釣ったボトルがありません"
        AppLanguage.KOREAN -> "아직 낚은 유리병이 없습니다"
        AppLanguage.ARABIC -> "لم تصطد أي زجاجة بعد"
        AppLanguage.SPANISH -> "Aún no has pescado ninguna botella"
        AppLanguage.FRENCH -> "Aucune bouteille repêchée pour le moment"
        AppLanguage.GERMAN -> "Noch keine Flaschen gefischt"
        AppLanguage.RUSSIAN -> "Пока нет выловленных бутылок"
        AppLanguage.PORTUGUESE -> "Nenhuma garrafa pescada ainda"
        AppLanguage.INDONESIAN -> "Belum Ada Botol yang Diambil"
        else -> "No Fished Bottles Yet"
    }

    fun emptyFishedDesc(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "Lovy 海洋蕴藏着来自各地的问候，点击上方“捞瓶子”开启你的奇遇吧！"
        AppLanguage.JAPANESE -> "海には世界中のメッセージが漂っています。上のボタンからボトルを釣ってみましょう！"
        AppLanguage.KOREAN -> "Lovy 바다에는 전 세계의 메시지가 떠다닙니다. 위의 버튼을 눌러 첫 유리병을 낚아보세요!"
        AppLanguage.ARABIC -> "يحتفظ محيط Lovy بآلاف الرسائل من مدن مختلفة. اضغط على الزر بالأعلى لصيد أول زجاجة!"
        AppLanguage.SPANISH -> "El océano Lovy guarda miles de mensajes. ¡Toca el botón arriba para pescar tu primera botella!"
        AppLanguage.FRENCH -> "L'océan Lovy regorge de messages. Appuyez sur le bouton ci-dessus pour repêcher votre première bouteille !"
        AppLanguage.GERMAN -> "Das Meer birgt tausende Nachrichten. Tippe oben auf 'Flasche fischen', um deine erste zu finden!"
        AppLanguage.RUSSIAN -> "Океан Lovy хранит тысячи посланий. Нажмите кнопку выше, чтобы выловить первую бутылку!"
        AppLanguage.PORTUGUESE -> "O oceano Lovy guarda mensagens misteriosas. Toque acima para pescar sua primeira garrafa!"
        AppLanguage.INDONESIAN -> "Lautan Lovy menyimpan ribuan pesan misteri dari berbagai kota. Ketuk tombol 'Ambil Botol' di atas untuk menjaring botol pertamamu!"
        else -> "The ocean holds thousands of mysterious messages from around the world. Tap 'Fish Bottle' above to catch your first one!"
    }

    fun emptyMyBottlesTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "你还没有扔过漂流瓶"
        AppLanguage.JAPANESE -> "まだボトルを流していません"
        AppLanguage.KOREAN -> "아직 유리병을 띄우지 않았습니다"
        AppLanguage.ARABIC -> "لم تقم برمي أي زجاجة بعد"
        AppLanguage.SPANISH -> "Aún no has lanzado ninguna botella"
        AppLanguage.FRENCH -> "Vous n'avez pas encore lancé de bouteille"
        AppLanguage.GERMAN -> "Du hast noch keine Flasche geworfen"
        AppLanguage.RUSSIAN -> "Вы еще не бросали бутылки"
        AppLanguage.PORTUGUESE -> "Você ainda não lançou nenhuma garrafa"
        AppLanguage.INDONESIAN -> "Kamu Belum Pernah Melempar Botol"
        else -> "You Haven't Tossed a Bottle Yet"
    }

    fun emptyMyBottlesDesc(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "写下你的心声或暖心问候，让它漂向远方被有缘人发现吧！"
        AppLanguage.JAPANESE -> "思いや温かいメッセージを書いて、海に流してみましょう！"
        AppLanguage.KOREAN -> "따뜻한 인사나 진솔한 마음을 담아 바다로 띄워보세요!"
        AppLanguage.ARABIC -> "اكتب ما يجول في خاطرك أو تحية دافئة ودعها تطفو في البحر ليكتشفها الآخرون!"
        AppLanguage.SPANISH -> "Escribe tus pensamientos o saludos y déjalos flotar en el océano."
        AppLanguage.FRENCH -> "Écrivez vos pensées et laissez-les flotter vers de nouvelles rencontres !"
        AppLanguage.GERMAN -> "Schreibe deine Gedanken auf und lass sie ins Meer treiben!"
        AppLanguage.RUSSIAN -> "Напишите теплое послание и пустите его по волнам!"
        AppLanguage.PORTUGUESE -> "Escreva seus sentimentos ou saudações e lance-os no oceano!"
        AppLanguage.INDONESIAN -> "Tulis kata hatimu, salam hangat, atau curhatan dan hanyutkan ke lautan untuk ditemukan pengguna lain!"
        else -> "Write your thoughts or warm greetings and let them float across the sea to be discovered!"
    }

    fun throwDialogTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "写下漂流瓶寄语"
        AppLanguage.JAPANESE -> "ボトルメッセージを書く"
        AppLanguage.KOREAN -> "유리병 편지 작성하기"
        AppLanguage.ARABIC -> "كتابة رسالة في زجاجة"
        AppLanguage.SPANISH -> "Escribir Mensaje en Botella"
        AppLanguage.FRENCH -> "Écrire une bouteille à la mer"
        AppLanguage.GERMAN -> "Flaschenpost verfassen"
        AppLanguage.RUSSIAN -> "Написать послание в бутылке"
        AppLanguage.PORTUGUESE -> "Escrever Mensagem na Garrafa"
        AppLanguage.INDONESIAN -> "Tulis Pesan dalam Botol"
        else -> "Write Message in a Bottle"
    }

    fun throwDialogPlaceholder(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "写下你想对世界说的话..."
        AppLanguage.JAPANESE -> "世界に届けたいメッセージを入力..."
        AppLanguage.KOREAN -> "세상에 전하고 싶은 메시지를 적어보세요..."
        AppLanguage.ARABIC -> "اكتب ما ترغب في مشاركته مع العالم..."
        AppLanguage.SPANISH -> "Escribe lo que quieras compartir con el mundo..."
        AppLanguage.FRENCH -> "Écrivez ce que vous souhaitez partager..."
        AppLanguage.GERMAN -> "Teile deine Gedanken mit der Welt..."
        AppLanguage.RUSSIAN -> "Напишите то, чем хотите поделиться..."
        AppLanguage.PORTUGUESE -> "Escreva o que você gostaria de dizer ao mundo..."
        AppLanguage.INDONESIAN -> "Tulis apa saja yang ingin kamu bagikan ke dunia..."
        else -> "Write whatever you would like to share with the world..."
    }

    fun btnTossNow(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "扔入海洋"
        AppLanguage.JAPANESE -> "海へ流す"
        AppLanguage.KOREAN -> "바다로 띄우기"
        AppLanguage.ARABIC -> "رمي في المحيط"
        AppLanguage.SPANISH -> "Lanzar al Océano"
        AppLanguage.FRENCH -> "Jeter à la mer"
        AppLanguage.GERMAN -> "Ins Meer werfen"
        AppLanguage.RUSSIAN -> "Бросить в океан"
        AppLanguage.PORTUGUESE -> "Lançar ao Oceano"
        AppLanguage.INDONESIAN -> "Lempar ke Lautan"
        else -> "Toss into the Sea"
    }

    fun btnReply(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "回复"
        AppLanguage.JAPANESE -> "返信する"
        AppLanguage.KOREAN -> "답장하기"
        AppLanguage.ARABIC -> "رد"
        AppLanguage.SPANISH -> "Responder"
        AppLanguage.FRENCH -> "Répondre"
        AppLanguage.GERMAN -> "Antworten"
        AppLanguage.RUSSIAN -> "Ответить"
        AppLanguage.PORTUGUESE -> "Responder"
        AppLanguage.INDONESIAN -> "Balas"
        else -> "Reply"
    }

    fun btnRelease(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "放生瓶子"
        AppLanguage.JAPANESE -> "海に戻す"
        AppLanguage.KOREAN -> "다시 방생하기"
        AppLanguage.ARABIC -> "إعادة للبحر"
        AppLanguage.SPANISH -> "Devolver al mar"
        AppLanguage.FRENCH -> "Remettre à la mer"
        AppLanguage.GERMAN -> "Freilassen"
        AppLanguage.RUSSIAN -> "Отпустить"
        AppLanguage.PORTUGUESE -> "Devolver ao mar"
        AppLanguage.INDONESIAN -> "Hanyutkan Lagi"
        else -> "Release Back"
    }

    // Nearby Screen Strings
    fun nearbyTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "附近的人"
        AppLanguage.JAPANESE -> "近くの人"
        AppLanguage.KOREAN -> "주변 친구"
        AppLanguage.ARABIC -> "أشخاص بالجوار"
        AppLanguage.SPANISH -> "Personas Cercanas"
        AppLanguage.FRENCH -> "Personnes à proximité"
        AppLanguage.GERMAN -> "Personen in der Nähe"
        AppLanguage.RUSSIAN -> "Люди рядом"
        AppLanguage.PORTUGUESE -> "Pessoas Próximas"
        AppLanguage.INDONESIAN -> "Pengguna di Sekitar"
        else -> "People Nearby"
    }

    fun radarCenterLabel(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "雷达中心:"
        AppLanguage.JAPANESE -> "レーダー中心:"
        AppLanguage.KOREAN -> "레이더 중심:"
        AppLanguage.ARABIC -> "مركز الرادار:"
        AppLanguage.SPANISH -> "Centro del radar:"
        AppLanguage.FRENCH -> "Centre du radar :"
        AppLanguage.GERMAN -> "Radarzentrum:"
        AppLanguage.RUSSIAN -> "Центр радара:"
        AppLanguage.PORTUGUESE -> "Centro do radar:"
        AppLanguage.INDONESIAN -> "Pusat Radar:"
        else -> "Radar Center:"
    }

    fun btnRefreshGps(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "刷新 GPS"
        AppLanguage.JAPANESE -> "GPS更新"
        AppLanguage.KOREAN -> "GPS 갱신"
        AppLanguage.ARABIC -> "تحديث GPS"
        AppLanguage.SPANISH -> "Actualizar GPS"
        AppLanguage.FRENCH -> "Actualiser GPS"
        AppLanguage.GERMAN -> "GPS aktualisieren"
        AppLanguage.RUSSIAN -> "Обновить GPS"
        AppLanguage.PORTUGUESE -> "Atualizar GPS"
        AppLanguage.INDONESIAN -> "Perbarui GPS"
        else -> "Update GPS"
    }

    fun filterAll(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "全部"
        AppLanguage.JAPANESE -> "すべて"
        AppLanguage.KOREAN -> "전체"
        AppLanguage.ARABIC -> "الكل"
        AppLanguage.SPANISH -> "Todos"
        AppLanguage.FRENCH -> "Tous"
        AppLanguage.GERMAN -> "Alle"
        AppLanguage.RUSSIAN -> "Все"
        AppLanguage.PORTUGUESE -> "Todos"
        AppLanguage.INDONESIAN -> "Semua"
        else -> "All"
    }

    fun filterMale(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "男生"
        AppLanguage.JAPANESE -> "男性"
        AppLanguage.KOREAN -> "남성"
        AppLanguage.ARABIC -> "ذكور"
        AppLanguage.SPANISH -> "Hombres"
        AppLanguage.FRENCH -> "Hommes"
        AppLanguage.GERMAN -> "Männer"
        AppLanguage.RUSSIAN -> "Мужчины"
        AppLanguage.PORTUGUESE -> "Homens"
        AppLanguage.INDONESIAN -> "Pria"
        else -> "Men"
    }

    fun filterFemale(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "女生"
        AppLanguage.JAPANESE -> "女性"
        AppLanguage.KOREAN -> "여성"
        AppLanguage.ARABIC -> "إناث"
        AppLanguage.SPANISH -> "Mujeres"
        AppLanguage.FRENCH -> "Femmes"
        AppLanguage.GERMAN -> "Frauen"
        AppLanguage.RUSSIAN -> "Женщины"
        AppLanguage.PORTUGUESE -> "Mulheres"
        AppLanguage.INDONESIAN -> "Wanita"
        else -> "Women"
    }

    fun filterOnlineOnly(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "仅看在线"
        AppLanguage.JAPANESE -> "オンラインのみ"
        AppLanguage.KOREAN -> "온라인만"
        AppLanguage.ARABIC -> "المتصلون فقط"
        AppLanguage.SPANISH -> "Solo en línea"
        AppLanguage.FRENCH -> "En ligne seulement"
        AppLanguage.GERMAN -> "Nur online"
        AppLanguage.RUSSIAN -> "Только в сети"
        AppLanguage.PORTUGUESE -> "Apenas online"
        AppLanguage.INDONESIAN -> "Hanya Online"
        else -> "Online Only"
    }

    // Profile Screen Strings
    fun profileDetailsTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "个人资料详情"
        AppLanguage.JAPANESE -> "プロフィール詳細"
        AppLanguage.KOREAN -> "프로필 상세"
        AppLanguage.ARABIC -> "تفاصيل الملف الشخصي"
        AppLanguage.SPANISH -> "Detalles del Perfil"
        AppLanguage.FRENCH -> "Détails du profil"
        AppLanguage.GERMAN -> "Profildetails"
        AppLanguage.RUSSIAN -> "Данные профиля"
        AppLanguage.PORTUGUESE -> "Detalhes do Perfil"
        AppLanguage.INDONESIAN -> "Detail Profil Pengguna"
        else -> "User Profile Details"
    }

    fun aboutMeBio(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "关于我 / 个性签名"
        AppLanguage.JAPANESE -> "自己紹介 / バイオ"
        AppLanguage.KOREAN -> "자기소개 / 바이오"
        AppLanguage.ARABIC -> "نبذة عني"
        AppLanguage.SPANISH -> "Sobre Mí / Biografía"
        AppLanguage.FRENCH -> "À propos de moi / Bio"
        AppLanguage.GERMAN -> "Über mich / Bio"
        AppLanguage.RUSSIAN -> "Обо мне / Биография"
        AppLanguage.PORTUGUESE -> "Sobre Mim / Bio"
        AppLanguage.INDONESIAN -> "Tentang Saya / Bio"
        else -> "About Me / Bio"
    }

    fun cityDomicile(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "常住城市"
        AppLanguage.JAPANESE -> "居住都市"
        AppLanguage.KOREAN -> "거주 도시"
        AppLanguage.ARABIC -> "المدينة"
        AppLanguage.SPANISH -> "Ciudad de Residencia"
        AppLanguage.FRENCH -> "Ville de résidence"
        AppLanguage.GERMAN -> "Wohnort"
        AppLanguage.RUSSIAN -> "Город проживания"
        AppLanguage.PORTUGUESE -> "Cidade de Residência"
        AppLanguage.INDONESIAN -> "Domisili Kota"
        else -> "Current City"
    }

    // --- CHATS TAB ---
    fun chatsTitle(lang: AppLanguage): String = tabChat(lang)

    fun chatsSearchPlaceholder(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "搜索聊天记录..."
        AppLanguage.JAPANESE -> "チャットを検索..."
        AppLanguage.KOREAN -> "대화 내용 검색..."
        AppLanguage.ARABIC -> "البحث في المحادثات..."
        AppLanguage.SPANISH -> "Buscar conversaciones..."
        AppLanguage.FRENCH -> "Rechercher des conversations..."
        AppLanguage.GERMAN -> "Unterhaltungen durchsuchen..."
        AppLanguage.RUSSIAN -> "Поиск сообщений..."
        AppLanguage.PORTUGUESE -> "Pesquisar conversas..."
        AppLanguage.INDONESIAN -> "Cari percakapan..."
        else -> "Search conversations..."
    }

    fun chatsEmptyTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "暂无聊天记录"
        AppLanguage.JAPANESE -> "チャットはまだありません"
        AppLanguage.KOREAN -> "아직 대화가 없습니다"
        AppLanguage.ARABIC -> "لا توجد محادثات حتى الآن"
        AppLanguage.SPANISH -> "Aún no hay conversaciones"
        AppLanguage.FRENCH -> "Pas encore de discussion"
        AppLanguage.GERMAN -> "Noch keine Chats vorhanden"
        AppLanguage.RUSSIAN -> "Чатов пока нет"
        AppLanguage.PORTUGUESE -> "Nenhuma conversa ainda"
        AppLanguage.INDONESIAN -> "Belum ada obrolan"
        else -> "No conversations yet"
    }

    fun chatsEmptyDesc(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "跟附近的人打个招呼或捞个漂流瓶开始畅聊吧！"
        AppLanguage.JAPANESE -> "近くの人に挨拶するか、ボトルを拾ってチャットを始めましょう！"
        AppLanguage.KOREAN -> "주변 친구에게 인사를 건네거나 유리병을 건져 대화를 시작해보세요!"
        AppLanguage.ARABIC -> "قل مرحباً للمستخدمين القريبين أو اصطد زجاجة لبدء الدردشة!"
        AppLanguage.SPANISH -> "¡Saluda a personas cercanas o pesca una botella para comenzar a chatear!"
        AppLanguage.FRENCH -> "Dites bonjour aux personnes à proximité ou pêchez une bouteille pour commencer à discuter !"
        AppLanguage.GERMAN -> "Grüße Leute in der Nähe oder fische eine Flaschenpost, um zu chatten!"
        AppLanguage.RUSSIAN -> "Поздоровайтесь с людьми поблизости или выловите бутылку, чтобы начать общаться!"
        AppLanguage.PORTUGUESE -> "Diga olá para pessoas próximas ou pesque uma garrafa para começar a conversar!"
        AppLanguage.INDONESIAN -> "Sapa teman di sekitar atau pancing botol untuk mulai mengobrol!"
        else -> "Say hi to nearby people or fish a bottle to start chatting!"
    }

    fun chatsDeleteConfirmTitle(lang: AppLanguage, count: Int): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> if (count == 1) "删除 1 条聊天？" else "删除选中的 $count 条聊天？"
        AppLanguage.JAPANESE -> if (count == 1) "1 件のチャットを削除しますか？" else "選択した $count 件のチャットを削除しますか？"
        AppLanguage.KOREAN -> if (count == 1) "대화 1개를 삭제하시겠습니까?" else "선택한 $count 개의 대화를 삭제하시겠습니까?"
        AppLanguage.ARABIC -> if (count == 1) "حذف محادثة واحدة؟" else "حذف $count محادثات محددة؟"
        AppLanguage.SPANISH -> if (count == 1) "¿Eliminar 1 conversación?" else "¿Eliminar $count conversaciones seleccionadas?"
        AppLanguage.FRENCH -> if (count == 1) "Supprimer 1 discussion ?" else "Supprimer les $count discussions sélectionnées ?"
        AppLanguage.GERMAN -> if (count == 1) "1 Chat löschen?" else "$count ausgewählte Chats löschen?"
        AppLanguage.RUSSIAN -> if (count == 1) "Удалить 1 чат?" else "Удалить выбранные чаты ($count)?"
        AppLanguage.PORTUGUESE -> if (count == 1) "Excluir 1 conversa?" else "Excluir $count conversas selecionadas?"
        AppLanguage.INDONESIAN -> if (count == 1) "Hapus 1 obrolan?" else "Hapus $count obrolan terpilih?"
        else -> if (count == 1) "Delete 1 chat?" else "Delete $count selected chats?"
    }

    fun chatsDeleteConfirmDesc(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "与该用户的聊天记录将从您的设备中清除。"
        AppLanguage.JAPANESE -> "このユーザーとのメッセージ履歴は端末から削除されます。"
        AppLanguage.KOREAN -> "이 사용자와의 메시지 내역이 기기에서 삭제됩니다."
        AppLanguage.ARABIC -> "سيتم حذف سجل الرسائل مع هذا المستخدم من جهازك."
        AppLanguage.SPANISH -> "El historial de mensajes con este usuario se eliminará de tu dispositivo."
        AppLanguage.FRENCH -> "L'historique des messages avec cet utilisateur sera supprimé de votre appareil."
        AppLanguage.GERMAN -> "Der Nachrichtenverlauf mit diesem Benutzer wird von deinem Gerät gelöscht."
        AppLanguage.RUSSIAN -> "История сообщений с этим пользователем будет удалена с вашего устройства."
        AppLanguage.PORTUGUESE -> "O histórico de mensagens com este usuário será apagado do seu dispositivo."
        AppLanguage.INDONESIAN -> "Riwayat pesan dengan pengguna ini akan dihapus dari perangkat Anda."
        else -> "Message history with this user will be removed from your device."
    }

    fun chatsBtnDelete(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "删除聊天"
        AppLanguage.JAPANESE -> "チャットを削除"
        AppLanguage.KOREAN -> "대화 삭제"
        AppLanguage.ARABIC -> "حذف المحادثة"
        AppLanguage.SPANISH -> "Eliminar chats"
        AppLanguage.FRENCH -> "Supprimer"
        AppLanguage.GERMAN -> "Chats löschen"
        AppLanguage.RUSSIAN -> "Удалить чаты"
        AppLanguage.PORTUGUESE -> "Excluir conversas"
        AppLanguage.INDONESIAN -> "Hapus Obrolan"
        else -> "Delete Chats"
    }

    fun chatsSelectAll(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "全选"
        AppLanguage.JAPANESE -> "すべて選択"
        AppLanguage.KOREAN -> "전체 선택"
        AppLanguage.ARABIC -> "تحديد الكل"
        AppLanguage.SPANISH -> "Seleccionar todo"
        AppLanguage.FRENCH -> "Tout sélectionner"
        AppLanguage.GERMAN -> "Alle auswählen"
        AppLanguage.RUSSIAN -> "Выбрать все"
        AppLanguage.PORTUGUESE -> "Selecionar tudo"
        AppLanguage.INDONESIAN -> "Pilih Semua"
        else -> "Select All"
    }

    fun chatsStartChat(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "发起聊天"
        AppLanguage.JAPANESE -> "チャット開始"
        AppLanguage.KOREAN -> "채팅 시작"
        AppLanguage.ARABIC -> "بدء دردشة"
        AppLanguage.SPANISH -> "Iniciar chat"
        AppLanguage.FRENCH -> "Démarrer"
        AppLanguage.GERMAN -> "Chat starten"
        AppLanguage.RUSSIAN -> "Начать чат"
        AppLanguage.PORTUGUESE -> "Iniciar conversa"
        AppLanguage.INDONESIAN -> "Mulai Chat"
        else -> "Start Chat"
    }

    fun chatsReadStatus(lang: AppLanguage, isRead: Boolean): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> if (isRead) "已读" else "已送达"
        AppLanguage.JAPANESE -> if (isRead) "既読" else "送信済み"
        AppLanguage.KOREAN -> if (isRead) "읽음" else "전송됨"
        AppLanguage.ARABIC -> if (isRead) "تمت القراءة" else "تم الإرسال"
        AppLanguage.SPANISH -> if (isRead) "Leído" else "Enviado"
        AppLanguage.FRENCH -> if (isRead) "Lu" else "Envoyé"
        AppLanguage.GERMAN -> if (isRead) "Gelesen" else "Gesendet"
        AppLanguage.RUSSIAN -> if (isRead) "Прочитано" else "Отправлено"
        AppLanguage.PORTUGUESE -> if (isRead) "Lido" else "Enviado"
        AppLanguage.INDONESIAN -> if (isRead) "Dibaca" else "Terkirim"
        else -> if (isRead) "Read" else "Sent"
    }

    fun chatsTyping(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "正在输入..."
        AppLanguage.JAPANESE -> "入力中..."
        AppLanguage.KOREAN -> "입력 중..."
        AppLanguage.ARABIC -> "يكتب الآن..."
        AppLanguage.SPANISH -> "Escribiendo..."
        AppLanguage.FRENCH -> "En train d'écrire..."
        AppLanguage.GERMAN -> "Tippt..."
        AppLanguage.RUSSIAN -> "Печатает..."
        AppLanguage.PORTUGUESE -> "Digitando..."
        AppLanguage.INDONESIAN -> "Sedang mengetik..."
        else -> "Typing..."
    }

    // --- FRIENDS TAB ---
    fun friendsTitle(lang: AppLanguage): String = tabFriends(lang)

    fun friendsSearchPlaceholder(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "搜索好友..."
        AppLanguage.JAPANESE -> "友達を検索..."
        AppLanguage.KOREAN -> "친구 검색..."
        AppLanguage.ARABIC -> "البحث عن أصدقاء..."
        AppLanguage.SPANISH -> "Buscar amigos..."
        AppLanguage.FRENCH -> "Rechercher des amis..."
        AppLanguage.GERMAN -> "Freunde suchen..."
        AppLanguage.RUSSIAN -> "Поиск друзей..."
        AppLanguage.PORTUGUESE -> "Pesquisar amigos..."
        AppLanguage.INDONESIAN -> "Cari teman..."
        else -> "Search friends..."
    }

    fun friendsNewFriendsTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "新的朋友"
        AppLanguage.JAPANESE -> "新しい友達"
        AppLanguage.KOREAN -> "새로운 친구"
        AppLanguage.ARABIC -> "أصدقاء جدد"
        AppLanguage.SPANISH -> "Nuevos Amigos"
        AppLanguage.FRENCH -> "Nouveaux amis"
        AppLanguage.GERMAN -> "Neue Freunde"
        AppLanguage.RUSSIAN -> "Новые друзья"
        AppLanguage.PORTUGUESE -> "Novos Amigos"
        AppLanguage.INDONESIAN -> "Teman Baru"
        else -> "New Friends"
    }

    fun friendsFindNearby(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "寻找附近朋友"
        AppLanguage.JAPANESE -> "近くの友達を探す"
        AppLanguage.KOREAN -> "주변 친구 찾기"
        AppLanguage.ARABIC -> "البحث عن أصدقاء بالجوار"
        AppLanguage.SPANISH -> "Buscar Amigos Cercanos"
        AppLanguage.FRENCH -> "Trouver des amis proches"
        AppLanguage.GERMAN -> "Freunde in der Nähe finden"
        AppLanguage.RUSSIAN -> "Найти друзей рядом"
        AppLanguage.PORTUGUESE -> "Buscar Amigos Próximos"
        AppLanguage.INDONESIAN -> "Cari Teman Sekitar"
        else -> "Find Friends Nearby"
    }

    fun friendsMyContacts(lang: AppLanguage, count: Int = -1): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> if (count >= 0) "我的好友 ($count)" else "我的好友"
        AppLanguage.JAPANESE -> if (count >= 0) "連絡先リスト ($count)" else "連絡先リスト"
        AppLanguage.KOREAN -> if (count >= 0) "내 연락처 ($count)" else "내 연락처"
        AppLanguage.ARABIC -> if (count >= 0) "جهات الاتصال ($count)" else "جهات الاتصال"
        AppLanguage.SPANISH -> if (count >= 0) "Mis Contactos ($count)" else "Mis Contactos"
        AppLanguage.FRENCH -> if (count >= 0) "Mes contacts ($count)" else "Mes contacts"
        AppLanguage.GERMAN -> if (count >= 0) "Meine Kontakte ($count)" else "Meine Kontakte"
        AppLanguage.RUSSIAN -> if (count >= 0) "Мои контакты ($count)" else "Мои контакты"
        AppLanguage.PORTUGUESE -> if (count >= 0) "Meus Contatos ($count)" else "Meus Contatos"
        AppLanguage.INDONESIAN -> if (count >= 0) "Kontak Saya ($count)" else "Kontak Saya"
        else -> if (count >= 0) "My Contacts ($count)" else "My Contacts"
    }

    fun friendsMyContacts(lang: AppLanguage): String = friendsMyContacts(lang, -1)

    fun friendsClearAll(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "清空好友"
        AppLanguage.JAPANESE -> "すべて削除"
        AppLanguage.KOREAN -> "모두 삭제"
        AppLanguage.ARABIC -> "مسح الكل"
        AppLanguage.SPANISH -> "Borrar todos"
        AppLanguage.FRENCH -> "Tout effacer"
        AppLanguage.GERMAN -> "Alle löschen"
        AppLanguage.RUSSIAN -> "Очистить всех"
        AppLanguage.PORTUGUESE -> "Limpar todos"
        AppLanguage.INDONESIAN -> "Hapus Semua"
        else -> "Clear All"
    }

    fun friendsEmptyTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "暂无好友联系人"
        AppLanguage.JAPANESE -> "連絡先がまだありません"
        AppLanguage.KOREAN -> "등록된 친구가 없습니다"
        AppLanguage.ARABIC -> "لا توجد جهات اتصال حتى الآن"
        AppLanguage.SPANISH -> "Sin contactos de amigos aún"
        AppLanguage.FRENCH -> "Aucun contact d'ami"
        AppLanguage.GERMAN -> "Noch keine Kontakte"
        AppLanguage.RUSSIAN -> "Список друзей пуст"
        AppLanguage.PORTUGUESE -> "Nenhum amigo nos contatos"
        AppLanguage.INDONESIAN -> "Belum Ada Kontak Teman"
        else -> "No Friends Yet"
    }

    fun friendsEmptyDesc(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "在雷达中向身边的人打招呼，开启对话并将他们添加至好友列表吧。"
        AppLanguage.JAPANESE -> "レーダー機能で近くのユーザーに挨拶して会話を始め、友達を追加しましょう。"
        AppLanguage.KOREAN -> "레이더를 통해 주변 사용자에게 인사를 건네고 친구로 등록해보세요."
        AppLanguage.ARABIC -> "ابدأ المحادثة بإلقاء التحية على المستخدمين القريبين لإضافتهم إلى جهات الاتصال."
        AppLanguage.SPANISH -> "Comienza una conversación saludando a personas cercanas en el radar para agregarlas a tus contactos."
        AppLanguage.FRENCH -> "Commencez une conversation en saluant les utilisateurs proches sur le radar pour les ajouter à vos contacts."
        AppLanguage.GERMAN -> "Starte eine Unterhaltung mit Personen in der Nähe, um sie zu deinen Kontakten hinzuzufügen."
        AppLanguage.RUSSIAN -> "Поздоровайтесь с пользователями поблизости через радар, чтобы добавить их в контакты."
        AppLanguage.PORTUGUESE -> "Comece uma conversa dando um olá para pessoas próximas no radar e adicione-as aos seus contatos."
        AppLanguage.INDONESIAN -> "Mulai percakapan dengan menyapa pengguna di sekitar melalui fitur radar untuk menambahkan teman ke kontak."
        else -> "Start a conversation by greeting nearby users on the radar to add friends to your contacts."
    }

    fun friendsDeleteTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "删除好友联系人"
        AppLanguage.JAPANESE -> "友達の連絡先を削除"
        AppLanguage.KOREAN -> "친구 연락처 삭제"
        AppLanguage.ARABIC -> "حذف جهة الاتصال"
        AppLanguage.SPANISH -> "Eliminar Contacto de Amigo"
        AppLanguage.FRENCH -> "Supprimer le contact"
        AppLanguage.GERMAN -> "Freundeskontakt löschen"
        AppLanguage.RUSSIAN -> "Удалить контакт друга"
        AppLanguage.PORTUGUESE -> "Excluir Contato de Amigo"
        AppLanguage.INDONESIAN -> "Hapus Kontak Teman"
        else -> "Delete Friend Contact"
    }

    fun friendsDeleteDesc(lang: AppLanguage, name: String): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "您确定要将“$name”从好友列表中移除吗？"
        AppLanguage.JAPANESE -> "「$name」を連絡先リストから削除してもよろしいですか？"
        AppLanguage.KOREAN -> "정말 \"$name\" 님을 친구 목록에서 삭제하시겠습니까?"
        AppLanguage.ARABIC -> "هل أنت متأكد من حذف \"$name\" من قائمة جهات الاتصال؟"
        AppLanguage.SPANISH -> "¿Estás seguro de que deseas eliminar a \"$name\" de tu lista de amigos?"
        AppLanguage.FRENCH -> "Voulez-vous vraiment supprimer « $name » de votre liste d'amis ?"
        AppLanguage.GERMAN -> "Möchtest du \"$name\" wirklich aus deiner Freundesliste entfernen?"
        AppLanguage.RUSSIAN -> "Вы уверены, что хотите удалить \"$name\" из списка друзей?"
        AppLanguage.PORTUGUESE -> "Tem certeza de que deseja remover \"$name\" da sua lista de amigos?"
        AppLanguage.INDONESIAN -> "Apakah Anda yakin ingin menghapus \"$name\" dari daftar kontak teman?"
        else -> "Are you sure you want to remove \"$name\" from your friends list?"
    }

    fun friendsClearAllTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "清空所有好友"
        AppLanguage.JAPANESE -> "すべての連絡先を消去"
        AppLanguage.KOREAN -> "모든 친구 목록 비우기"
        AppLanguage.ARABIC -> "مسح جميع الأصدقاء"
        AppLanguage.SPANISH -> "Borrar Todos los Contactos"
        AppLanguage.FRENCH -> "Effacer tous les amis"
        AppLanguage.GERMAN -> "Alle Freunde entfernen"
        AppLanguage.RUSSIAN -> "Очистить всех друзей"
        AppLanguage.PORTUGUESE -> "Limpar Todos os Amigos"
        AppLanguage.INDONESIAN -> "Bersihkan Semua Teman"
        else -> "Clear All Friends"
    }

    fun friendsClearAllDesc(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "确定删除列表中的所有好友联系人吗？您随时可以通过雷达再次打招呼添加。"
        AppLanguage.JAPANESE -> "すべての友達連絡先を削除しますか？レーダーでいつでも再検索できます。"
        AppLanguage.KOREAN -> "모든 친구를 목록에서 삭제하시겠습니까? 레이더를 통해 언제든지 다시 찾을 수 있습니다."
        AppLanguage.ARABIC -> "هل تريد حذف جميع جهات الاتصال؟ يمكنك دائماً البحث عن أصدقاء جدد عبر الرادار."
        AppLanguage.SPANISH -> "¿Eliminar todos los contactos de esta lista? Podrás volver a saludarlos desde el radar en cualquier momento."
        AppLanguage.FRENCH -> "Supprimer tous les contacts de cette liste ? Vous pourrez toujours en retrouver via le radar."
        AppLanguage.GERMAN -> "Alle Kontakte aus der Liste entfernen? Du kannst jederzeit über das Radar neue Freunde finden."
        AppLanguage.RUSSIAN -> "Удалить все контакты из списка? Вы всегда сможете снова найти друзей через радар."
        AppLanguage.PORTUGUESE -> "Excluir todos os contatos desta lista? Você sempre pode reencontrá-los pelo radar."
        AppLanguage.INDONESIAN -> "Hapus semua kontak teman dari daftar ini? Anda tetap dapat menyapa dan mencari teman baru kapan saja melalui radar."
        else -> "Remove all friends from this list? You can still greet and find new friends anytime via radar."
    }

    fun friendsFavorite(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "特别关注"
        AppLanguage.JAPANESE -> "お気に入り"
        AppLanguage.KOREAN -> "즐겨찾기"
        AppLanguage.ARABIC -> "المفضلة"
        AppLanguage.SPANISH -> "Favorito"
        AppLanguage.FRENCH -> "Favori"
        AppLanguage.GERMAN -> "Favorit"
        AppLanguage.RUSSIAN -> "Избранное"
        AppLanguage.PORTUGUESE -> "Favorito"
        AppLanguage.INDONESIAN -> "Favorit"
        else -> "Favorite"
    }

    fun friendsOnline(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "在线"
        AppLanguage.JAPANESE -> "オンライン"
        AppLanguage.KOREAN -> "온라인"
        AppLanguage.ARABIC -> "متصل"
        AppLanguage.SPANISH -> "En línea"
        AppLanguage.FRENCH -> "En ligne"
        AppLanguage.GERMAN -> "Online"
        AppLanguage.RUSSIAN -> "В сети"
        AppLanguage.PORTUGUESE -> "Online"
        AppLanguage.INDONESIAN -> "Online"
        else -> "Online"
    }

    // --- NEW FRIENDS SCREEN ---
    fun newFriendsSubtitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "发来聊天但尚未成为好友的用户"
        AppLanguage.JAPANESE -> "メッセージを受信したがまだ友達ではないユーザー"
        AppLanguage.KOREAN -> "메시지를 보냈지만 아직 친구로 등록되지 않은 사용자"
        AppLanguage.ARABIC -> "مستخدمون أرسلوا رسائل لكنهم ليسوا أصدقاء بعد"
        AppLanguage.SPANISH -> "Usuarios que enviaron mensajes pero aún no son amigos"
        AppLanguage.FRENCH -> "Utilisateurs ayant envoyé un message mais non ajoutés"
        AppLanguage.GERMAN -> "Benutzer, die geschrieben haben, aber noch keine Freunde sind"
        AppLanguage.RUSSIAN -> "Пользователи, написавшие сообщение, но еще не добавленные"
        AppLanguage.PORTUGUESE -> "Usuários que enviaram mensagens mas ainda não são amigos"
        AppLanguage.INDONESIAN -> "Pengguna yang mengirim chat tapi belum berteman"
        else -> "Users who sent chats but are not friends yet"
    }

    fun newFriendsBannerDesc(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "新用户的消息会在此等待您的批准。在您同意或接受请求之前，这些消息不会出现在聊天列表中。"
        AppLanguage.JAPANESE -> "友達リストにない新しいユーザーからのメッセージは、承認または無視されるまでここに保留されます。承認されるとチャット一覧に移動します。"
        AppLanguage.KOREAN -> "친구 목록에 없는 새 사용자의 메시지는 승인 또는 거절될 때까지 여기에 보관됩니다. 승인되면 대화 목록에 추가됩니다."
        AppLanguage.ARABIC -> "تبقى رسائل المستخدمين الجدد غير الموجودين في قائمة الأصدقاء هنا في انتظار الموافقة أو التجاهل. تظهر في المحادثات فقط بعد الموافقة."
        AppLanguage.SPANISH -> "Los mensajes de nuevos usuarios que no están en tus amigos esperarán tu aprobación aquí. Solo entrarán a tus chats cuando los aceptes."
        AppLanguage.FRENCH -> "Les messages des nouvelles personnes non amies restent ici en attente d'approbation. Ils n'apparaîtront dans les discussions qu'une fois acceptés."
        AppLanguage.GERMAN -> "Nachrichten von neuen Kontakten warten hier auf deine Bestätigung. Sie gelangen erst nach der Annahme in deine Chat-Liste."
        AppLanguage.RUSSIAN -> "Сообщения от новых пользователей ждут вашего одобрения здесь. Они появятся в чатах только после того, как вы примете запрос."
        AppLanguage.PORTUGUESE -> "Mensagens de novos usuários aguardam sua aprovação aqui. Elas só entram na lista de conversas após você aceitar."
        AppLanguage.INDONESIAN -> "Pesan dari teman baru yang belum ada di daftar teman hanya berada di sini menunggu disetujui atau diabaikan. Setelah disetujui, obrolan akan langsung masuk ke menu Obrolan."
        else -> "Messages from new users not in your friends list will stay here awaiting approval or ignore. Once approved, the chat will appear in your Chats."
    }

    fun newFriendsEmptyTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "暂无新的好友请求"
        AppLanguage.JAPANESE -> "新しい友達はいません"
        AppLanguage.KOREAN -> "새로운 친구 요청이 없습니다"
        AppLanguage.ARABIC -> "لا توجد طلبات صداقة جديدة"
        AppLanguage.SPANISH -> "No hay nuevos amigos aún"
        AppLanguage.FRENCH -> "Aucune nouvelle demande"
        AppLanguage.GERMAN -> "Keine neuen Freundschaftsanfragen"
        AppLanguage.RUSSIAN -> "Новых запросов пока нет"
        AppLanguage.PORTUGUESE -> "Sem novos amigos por enquanto"
        AppLanguage.INDONESIAN -> "Belum Ada Teman Baru"
        else -> "No New Friends Yet"
    }

    fun newFriendsEmptyDesc(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "如果有其他用户向您发送聊天或打招呼，他们将直接显示在此页面中。"
        AppLanguage.JAPANESE -> "他のユーザーからチャットや挨拶が届くと、このページに表示されます。"
        AppLanguage.KOREAN -> "저장되지 않은 다른 사용자가 메시지나 인사를 보내면 여기에 표시됩니다."
        AppLanguage.ARABIC -> "إذا أرسل لك مستخدمون آخرون رسائل أو تحيات، فستظهر مباشرة في هذه الصفحة."
        AppLanguage.SPANISH -> "Si otros usuarios te envían mensajes o saludos, aparecerán directamente aquí."
        AppLanguage.FRENCH -> "Si d'autres personnes vous envoient un message, elles apparaîtront ici."
        AppLanguage.GERMAN -> "Wenn dir andere Personen schreiben oder grüßen, erscheinen sie direkt auf dieser Seite."
        AppLanguage.RUSSIAN -> "Если другие пользователи отправят вам сообщение или привет, они появятся здесь."
        AppLanguage.PORTUGUESE -> "Se outros usuários enviarem mensagens ou saudações, eles aparecerão aqui."
        AppLanguage.INDONESIAN -> "Jika ada pengguna lain yang belum Anda simpan mengirimkan chat atau salam, mereka akan langsung masuk ke halaman ini."
        else -> "If other users send you chats or greetings, they will appear right on this page."
    }

    fun newFriendsBtnAccept(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "接受"
        AppLanguage.JAPANESE -> "承認"
        AppLanguage.KOREAN -> "수락"
        AppLanguage.ARABIC -> "قبول"
        AppLanguage.SPANISH -> "Aceptar"
        AppLanguage.FRENCH -> "Accepter"
        AppLanguage.GERMAN -> "Annehmen"
        AppLanguage.RUSSIAN -> "Принять"
        AppLanguage.PORTUGUESE -> "Aceitar"
        AppLanguage.INDONESIAN -> "Terima"
        else -> "Accept"
    }

    fun newFriendsBtnIgnore(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "忽略"
        AppLanguage.JAPANESE -> "無視"
        AppLanguage.KOREAN -> "무시"
        AppLanguage.ARABIC -> "تجاهل"
        AppLanguage.SPANISH -> "Ignorar"
        AppLanguage.FRENCH -> "Ignorer"
        AppLanguage.GERMAN -> "Ignorieren"
        AppLanguage.RUSSIAN -> "Пропустить"
        AppLanguage.PORTUGUESE -> "Ignorar"
        AppLanguage.INDONESIAN -> "Abaikan"
        else -> "Ignore"
    }

    fun newFriendsBtnReply(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "回复聊天"
        AppLanguage.JAPANESE -> "返信する"
        AppLanguage.KOREAN -> "답장하기"
        AppLanguage.ARABIC -> "الرد"
        AppLanguage.SPANISH -> "Responder"
        AppLanguage.FRENCH -> "Répondre"
        AppLanguage.GERMAN -> "Antworten"
        AppLanguage.RUSSIAN -> "Ответить"
        AppLanguage.PORTUGUESE -> "Responder"
        AppLanguage.INDONESIAN -> "Balas Chat"
        else -> "Reply"
    }

    fun newFriendsDefaultGreeting(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "你好！很高兴认识你 👋"
        AppLanguage.JAPANESE -> "こんにちは！よろしくお願いします 👋"
        AppLanguage.KOREAN -> "안녕하세요! 반갑습니다 👋"
        AppLanguage.ARABIC -> "مرحباً! سعيد بمعرفتك 👋"
        AppLanguage.SPANISH -> "¡Hola! Mucho gusto 👋"
        AppLanguage.FRENCH -> "Salut ! Ravi de faire ta connaissance 👋"
        AppLanguage.GERMAN -> "Hallo! Schön dich kennenzulernen 👋"
        AppLanguage.RUSSIAN -> "Привет! Приятно познакомиться 👋"
        AppLanguage.PORTUGUESE -> "Olá! Prazer em conhecer 👋"
        AppLanguage.INDONESIAN -> "Halo! Salam kenal ya 👋"
        else -> "Hello! Nice to meet you 👋"
    }

    // --- CHAT DETAIL SCREEN ---
    fun chatBlockedNotice(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "该用户已被拉黑，无法发送消息。"
        AppLanguage.JAPANESE -> "このユーザーはブロックされているため送信できません。"
        AppLanguage.KOREAN -> "차단된 사용자입니다. 메시지를 보낼 수 없습니다."
        AppLanguage.ARABIC -> "المستخدم محظور. لا يمكنك إرسال الرسائل."
        AppLanguage.SPANISH -> "Usuario bloqueado. No se pueden enviar mensajes."
        AppLanguage.FRENCH -> "Utilisateur bloqué. Impossible d'envoyer un message."
        AppLanguage.GERMAN -> "Benutzer blockiert. Nachrichten können nicht gesendet werden."
        AppLanguage.RUSSIAN -> "Пользователь заблокирован. Отправка недоступна."
        AppLanguage.PORTUGUESE -> "Usuário bloqueado. Não é possível enviar mensagens."
        AppLanguage.INDONESIAN -> "Pengguna diblokir. Tidak dapat mengirim pesan."
        else -> "User is blocked. Cannot send messages."
    }

    fun chatUnblockBtn(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "解除拉黑"
        AppLanguage.JAPANESE -> "ブロック解除"
        AppLanguage.KOREAN -> "차단 해제"
        AppLanguage.ARABIC -> "إلغاء الحظر"
        AppLanguage.SPANISH -> "Desbloquear"
        AppLanguage.FRENCH -> "Débloquer"
        AppLanguage.GERMAN -> "Freigeben"
        AppLanguage.RUSSIAN -> "Разблокировать"
        AppLanguage.PORTUGUESE -> "Desbloquear"
        AppLanguage.INDONESIAN -> "Buka Blokir"
        else -> "Unblock"
    }

    fun chatNotFriendNotice(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "该用户尚未在您的好友列表中"
        AppLanguage.JAPANESE -> "連絡先にまだ登録されていません"
        AppLanguage.KOREAN -> "아직 내 연락처에 없는 사용자입니다"
        AppLanguage.ARABIC -> "المستخدم ليس في قائمة أصدقائك"
        AppLanguage.SPANISH -> "El usuario aún no está en tus contactos"
        AppLanguage.FRENCH -> "Cet utilisateur n'est pas dans vos contacts"
        AppLanguage.GERMAN -> "Benutzer ist noch nicht in deinen Kontakten"
        AppLanguage.RUSSIAN -> "Пользователь еще не добавлен в друзья"
        AppLanguage.PORTUGUESE -> "Usuário ainda não está nos seus contatos"
        AppLanguage.INDONESIAN -> "Pengguna belum ada di Kontak Saya"
        else -> "User is not in your contacts yet"
    }

    fun chatAddFriendBtn(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "添加好友"
        AppLanguage.JAPANESE -> "友達に追加"
        AppLanguage.KOREAN -> "친구 추가"
        AppLanguage.ARABIC -> "إضافة صديق"
        AppLanguage.SPANISH -> "Agregar Amigo"
        AppLanguage.FRENCH -> "Ajouter en ami"
        AppLanguage.GERMAN -> "Als Freund hinzufügen"
        AppLanguage.RUSSIAN -> "Добавить в друзья"
        AppLanguage.PORTUGUESE -> "Adicionar Amigo"
        AppLanguage.INDONESIAN -> "Tambah Teman"
        else -> "Add Friend"
    }

    fun chatDeleteDialogTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "删除消息？"
        AppLanguage.JAPANESE -> "メッセージを削除しますか？"
        AppLanguage.KOREAN -> "메시지를 삭제하시겠습니까?"
        AppLanguage.ARABIC -> "حذف الرسالة؟"
        AppLanguage.SPANISH -> "¿Eliminar mensaje?"
        AppLanguage.FRENCH -> "Supprimer le message ?"
        AppLanguage.GERMAN -> "Nachricht löschen?"
        AppLanguage.RUSSIAN -> "Удалить сообщение?"
        AppLanguage.PORTUGUESE -> "Excluir mensagem?"
        AppLanguage.INDONESIAN -> "Hapus pesan?"
        else -> "Delete message?"
    }

    fun chatDeleteDialogDescEveryone(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "您可以仅为您自己删除此消息，或为聊天中的所有人撤回。"
        AppLanguage.JAPANESE -> "このメッセージは自分のみ、または全員から削除できます。"
        AppLanguage.KOREAN -> "이 메시지를 나에게서만 삭제하거나 모든 사용자에게서 삭제할 수 있습니다."
        AppLanguage.ARABIC -> "يمكنك حذف هذه الرسالة لنفسك فقط أو للجميع في هذه المحادثة."
        AppLanguage.SPANISH -> "Puedes eliminar este mensaje solo para ti o para todos en el chat."
        AppLanguage.FRENCH -> "Vous pouvez supprimer ce message pour vous ou pour tout le monde."
        AppLanguage.GERMAN -> "Du kannst diese Nachricht nur für dich oder für alle im Chat löschen."
        AppLanguage.RUSSIAN -> "Вы можете удалить это сообщение для себя или для всех в этом чате."
        AppLanguage.PORTUGUESE -> "Você pode excluir esta mensagem apenas para você ou para todos."
        AppLanguage.INDONESIAN -> "Anda dapat menghapus pesan ini hanya untuk Anda, atau untuk semua orang di obrolan ini."
        else -> "You can delete this message just for you, or for everyone in this chat."
    }

    fun chatDeleteDialogDescMe(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "此消息将从您的设备中删除，您将无法再看到它。"
        AppLanguage.JAPANESE -> "このメッセージはあなたのチャットから削除され、再表示されません。"
        AppLanguage.KOREAN -> "이 메시지는 내 채팅 목록에서 삭제되며 더 이상 표시되지 않습니다."
        AppLanguage.ARABIC -> "سيتم حذف هذه الرسالة من محادثتك ولن تظهر لك مجدداً."
        AppLanguage.SPANISH -> "Este mensaje se eliminará de tu chat y ya no será visible para ti."
        AppLanguage.FRENCH -> "Ce message sera supprimé de votre discussion et ne sera plus visible."
        AppLanguage.GERMAN -> "Diese Nachricht wird aus deinem Chat gelöscht und nicht mehr angezeigt."
        AppLanguage.RUSSIAN -> "Это сообщение будет удалено из вашего чата и больше не будет видно."
        AppLanguage.PORTUGUESE -> "Esta mensagem será apagada da sua conversa e não ficará visível."
        AppLanguage.INDONESIAN -> "Pesan ini akan dihapus dari obrolan Anda dan tidak akan terlihat lagi oleh Anda."
        else -> "This message will be removed from your chat and won't be visible to you anymore."
    }

    fun chatDeleteForEveryone(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "为所有人删除"
        AppLanguage.JAPANESE -> "全員から削除"
        AppLanguage.KOREAN -> "모든 사람에게서 삭제"
        AppLanguage.ARABIC -> "حذف لدى الجميع"
        AppLanguage.SPANISH -> "Eliminar para todos"
        AppLanguage.FRENCH -> "Supprimer pour tous"
        AppLanguage.GERMAN -> "Für alle löschen"
        AppLanguage.RUSSIAN -> "Удалить для всех"
        AppLanguage.PORTUGUESE -> "Excluir para todos"
        AppLanguage.INDONESIAN -> "Hapus untuk Semua Orang"
        else -> "Delete for Everyone"
    }

    fun chatDeleteForMe(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "仅对我删除"
        AppLanguage.JAPANESE -> "自分のみ削除"
        AppLanguage.KOREAN -> "나에게서만 삭제"
        AppLanguage.ARABIC -> "حذف لدي فقط"
        AppLanguage.SPANISH -> "Eliminar para mí"
        AppLanguage.FRENCH -> "Supprimer pour moi"
        AppLanguage.GERMAN -> "Nur für mich löschen"
        AppLanguage.RUSSIAN -> "Удалить для меня"
        AppLanguage.PORTUGUESE -> "Excluir para mim"
        AppLanguage.INDONESIAN -> "Hapus untuk Saya"
        else -> "Delete for Me"
    }

    fun chatReplyingTo(lang: AppLanguage, name: String = ""): String = if (name.isBlank()) {
        when (resolveLang(lang)) {
            AppLanguage.CHINESE -> "回复消息"
            AppLanguage.JAPANESE -> "返信する"
            AppLanguage.KOREAN -> "답장하기"
            AppLanguage.ARABIC -> "الرد على الرسالة"
            AppLanguage.SPANISH -> "Responder mensaje"
            AppLanguage.FRENCH -> "Répondre"
            AppLanguage.GERMAN -> "Antworten"
            AppLanguage.RUSSIAN -> "Ответить на сообщение"
            AppLanguage.PORTUGUESE -> "Responder mensagem"
            AppLanguage.INDONESIAN -> "Membalas pesan"
            else -> "Replying to message"
        }
    } else {
        when (resolveLang(lang)) {
            AppLanguage.CHINESE -> "回复 $name"
            AppLanguage.JAPANESE -> "$name に返信中"
            AppLanguage.KOREAN -> "$name 님에게 답장 중"
            AppLanguage.ARABIC -> "الرد على $name"
            AppLanguage.SPANISH -> "Respondiendo a $name"
            AppLanguage.FRENCH -> "En réponse à $name"
            AppLanguage.GERMAN -> "Antwort an $name"
            AppLanguage.RUSSIAN -> "Ответ для $name"
            AppLanguage.PORTUGUESE -> "Respondendo a $name"
            AppLanguage.INDONESIAN -> "Membalas $name"
            else -> "Replying to $name"
        }
    }

    fun chatPhotoReady(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "照片已准备发送"
        AppLanguage.JAPANESE -> "写真を送信する準備ができました"
        AppLanguage.KOREAN -> "사진 전송 준비 완료"
        AppLanguage.ARABIC -> "الصورة جاهزة للإرسال"
        AppLanguage.SPANISH -> "Foto lista para enviar"
        AppLanguage.FRENCH -> "Photo prête à être envoyée"
        AppLanguage.GERMAN -> "Foto bereit zum Senden"
        AppLanguage.RUSSIAN -> "Фото готово к отправке"
        AppLanguage.PORTUGUESE -> "Foto pronta para enviar"
        AppLanguage.INDONESIAN -> "Foto siap dikirim"
        else -> "Photo ready to send"
    }

    fun chatTypePlaceholder(lang: AppLanguage, hasPhoto: Boolean): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> if (hasPhoto) "添加图片说明..." else "输入消息..."
        AppLanguage.JAPANESE -> if (hasPhoto) "写真に説明を追加..." else "メッセージを入力..."
        AppLanguage.KOREAN -> if (hasPhoto) "사진 설명 추가..." else "메시지 입력..."
        AppLanguage.ARABIC -> if (hasPhoto) "إضافة تعليق للصورة..." else "اكتب رسالة..."
        AppLanguage.SPANISH -> if (hasPhoto) "Añadir pie de foto..." else "Escribe un mensaje..."
        AppLanguage.FRENCH -> if (hasPhoto) "Ajouter une légende..." else "Tapez un message..."
        AppLanguage.GERMAN -> if (hasPhoto) "Bildbeschreibung hinzufügen..." else "Nachricht schreiben..."
        AppLanguage.RUSSIAN -> if (hasPhoto) "Добавить подпись..." else "Введите сообщение..."
        AppLanguage.PORTUGUESE -> if (hasPhoto) "Adicionar legenda..." else "Digite uma mensagem..."
        AppLanguage.INDONESIAN -> if (hasPhoto) "Tambah keterangan foto..." else "Ketik pesan..."
        else -> if (hasPhoto) "Add photo caption..." else "Type a message..."
    }

    fun chatSendBtn(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "发送"
        AppLanguage.JAPANESE -> "送信"
        AppLanguage.KOREAN -> "전송"
        AppLanguage.ARABIC -> "إرسال"
        AppLanguage.SPANISH -> "Enviar"
        AppLanguage.FRENCH -> "Envoyer"
        AppLanguage.GERMAN -> "Senden"
        AppLanguage.RUSSIAN -> "Отправить"
        AppLanguage.PORTUGUESE -> "Enviar"
        AppLanguage.INDONESIAN -> "Kirim"
        else -> "Send"
    }

    fun chatProfileDetail(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "好友资料详情"
        AppLanguage.JAPANESE -> "友達のプロフィール"
        AppLanguage.KOREAN -> "채팅 상대 프로필"
        AppLanguage.ARABIC -> "ملف الصديق الشخصي"
        AppLanguage.SPANISH -> "Perfil del Compañero de Chat"
        AppLanguage.FRENCH -> "Profil de l'interlocuteur"
        AppLanguage.GERMAN -> "Profil des Chatpartners"
        AppLanguage.RUSSIAN -> "Профиль собеседника"
        AppLanguage.PORTUGUESE -> "Perfil do Contato"
        AppLanguage.INDONESIAN -> "Profil Teman Obrolan"
        else -> "Chat Partner Profile"
    }

    fun chatBioStatus(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "签名 & 状态"
        AppLanguage.JAPANESE -> "自己紹介 & ステータス"
        AppLanguage.KOREAN -> "소개 & 상태"
        AppLanguage.ARABIC -> "النبذة والحالة"
        AppLanguage.SPANISH -> "Biografía y Estado"
        AppLanguage.FRENCH -> "Bio & Statut"
        AppLanguage.GERMAN -> "Bio & Status"
        AppLanguage.RUSSIAN -> "О себе и статус"
        AppLanguage.PORTUGUESE -> "Bio & Status"
        AppLanguage.INDONESIAN -> "Bio & Status"
        else -> "Bio & Status"
    }

    fun chatRecentMoments(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "最新动态 & 照片"
        AppLanguage.JAPANESE -> "最新のモーメント & 写真"
        AppLanguage.KOREAN -> "최근 모먼트 & 사진"
        AppLanguage.ARABIC -> "أحدث اللحظات والصور"
        AppLanguage.SPANISH -> "Momentos y Fotos Recientes"
        AppLanguage.FRENCH -> "Moments & photos récents"
        AppLanguage.GERMAN -> "Neueste Momente & Fotos"
        AppLanguage.RUSSIAN -> "Недавние моменты и фото"
        AppLanguage.PORTUGUESE -> "Momentos e Fotos Recentes"
        AppLanguage.INDONESIAN -> "Momen & Foto Terbaru"
        else -> "Recent Moments & Photos"
    }

    fun chatNoMoments(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "暂无动态"
        AppLanguage.JAPANESE -> "モーメントがありません"
        AppLanguage.KOREAN -> "등록된 모먼트가 없습니다"
        AppLanguage.ARABIC -> "لا توجد لحظات حتى الآن"
        AppLanguage.SPANISH -> "Aún no hay momentos"
        AppLanguage.FRENCH -> "Aucun moment"
        AppLanguage.GERMAN -> "Noch keine Momente"
        AppLanguage.RUSSIAN -> "Моментов пока нет"
        AppLanguage.PORTUGUESE -> "Nenhum momento ainda"
        AppLanguage.INDONESIAN -> "Belum Ada Momen"
        else -> "No Moments Yet"
    }

    fun chatContinueBtn(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "继续聊天"
        AppLanguage.JAPANESE -> "チャットを続ける"
        AppLanguage.KOREAN -> "채팅 계속하기"
        AppLanguage.ARABIC -> "متابعة الدردشة"
        AppLanguage.SPANISH -> "Continuar Chat"
        AppLanguage.FRENCH -> "Poursuivre la discussion"
        AppLanguage.GERMAN -> "Chat fortsetzen"
        AppLanguage.RUSSIAN -> "Продолжить чат"
        AppLanguage.PORTUGUESE -> "Continuar Conversa"
        AppLanguage.INDONESIAN -> "Lanjutkan Chat"
        else -> "Continue Chat"
    }

    fun chatSayHiBackBtn(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "回声问候 👋"
        AppLanguage.JAPANESE -> "挨拶を返す 👋"
        AppLanguage.KOREAN -> "맞인사하기 👋"
        AppLanguage.ARABIC -> "رد التحية 👋"
        AppLanguage.SPANISH -> "Saludar de vuelta 👋"
        AppLanguage.FRENCH -> "Rendre le salut 👋"
        AppLanguage.GERMAN -> "Zurückgrüßen 👋"
        AppLanguage.RUSSIAN -> "Поздороваться в ответ 👋"
        AppLanguage.PORTUGUESE -> "Cumprimentar de volta 👋"
        AppLanguage.INDONESIAN -> "Sapa Balik 👋"
        else -> "Say Hi Back 👋"
    }

    fun chatQuickSuggestions(lang: AppLanguage): List<String> = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> listOf("你好！👋", "在哪里呢？", "交个朋友吧 😊", "太棒了！")
        AppLanguage.JAPANESE -> listOf("こんにちは！👋", "どこにいますか？", "仲良くしてください 😊", "いいね！")
        AppLanguage.KOREAN -> listOf("안녕하세요! 👋", "어디 계세요?", "친하게 지내요 😊", "좋네요!")
        AppLanguage.ARABIC -> listOf("مرحباً! 👋", "أين أنت؟", "نتعرف؟ 😊", "رائع جداً!")
        AppLanguage.SPANISH -> listOf("¡Hola! 👋", "¿Dónde estás?", "¡Mucho gusto! 😊", "¡Genial!")
        AppLanguage.FRENCH -> listOf("Salut ! 👋", "Où es-tu ?", "Faisons connaissance 😊", "Super !")
        AppLanguage.GERMAN -> listOf("Hallo! 👋", "Wo bist du?", "Lass uns kennenlernen 😊", "Klasse!")
        AppLanguage.RUSSIAN -> listOf("Привет! 👋", "Ты где?", "Давай общаться 😊", "Круто!")
        AppLanguage.PORTUGUESE -> listOf("Olá! 👋", "Onde você está?", "Vamos nos conhecer 😊", "Legal!")
        AppLanguage.INDONESIAN -> listOf("Halo! 👋", "Lagi di mana?", "Kenalan dong 😊", "Asik nih!")
        else -> listOf("Hello! 👋", "Where are you?", "Let's connect 😊", "Awesome!")
    }

    // --- MOMENTS SCREEN ---
    fun momentsTitle(lang: AppLanguage): String = tabMoments(lang)

    fun momentsSubtitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "身边的精彩故事与生活瞬间"
        AppLanguage.JAPANESE -> "身近な友達の写真や日常のストーリー"
        AppLanguage.KOREAN -> "내 주변 친구들의 사진과 일상 이야기"
        AppLanguage.ARABIC -> "صور وقصص من أصدقاء في منطقتك"
        AppLanguage.SPANISH -> "Fotos y relatos de amigos a tu alrededor"
        AppLanguage.FRENCH -> "Photos et récits de personnes proches"
        AppLanguage.GERMAN -> "Fotos & Geschichten von Freunden in deiner Nähe"
        AppLanguage.RUSSIAN -> "Фотографии и истории людей вокруг вас"
        AppLanguage.PORTUGUESE -> "Fotos e histórias de amigos ao seu redor"
        AppLanguage.INDONESIAN -> "Foto & cerita dari teman sekitarmu"
        else -> "Photos & stories from friends around you"
    }

    fun momentsRefreshToast(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "正在刷新最新动态..."
        AppLanguage.JAPANESE -> "最新のモーメントを更新中..."
        AppLanguage.KOREAN -> "최신 모먼트를 새로고침하는 중..."
        AppLanguage.ARABIC -> "جاري تحديث اللحظات..."
        AppLanguage.SPANISH -> "Actualizando momentos..."
        AppLanguage.FRENCH -> "Actualisation des moments..."
        AppLanguage.GERMAN -> "Momente werden aktualisiert..."
        AppLanguage.RUSSIAN -> "Обновление ленты моментов..."
        AppLanguage.PORTUGUESE -> "Atualizando momentos..."
        AppLanguage.INDONESIAN -> "Memperbarui momen terbaru..."
        else -> "Refreshing latest moments..."
    }

    fun momentsViewingCount(lang: AppLanguage, count: Int): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "正在查看附近的 $count 条最新动态"
        AppLanguage.JAPANESE -> "近くの $count 件の最新モーメントを表示中"
        AppLanguage.KOREAN -> "주변 최신 모먼트 $count 개 확인 중"
        AppLanguage.ARABIC -> "عرض $count من أحدث اللحظات بالجوار"
        AppLanguage.SPANISH -> "Viendo $count momentos recientes cerca"
        AppLanguage.FRENCH -> "Affichage de $count moments récents"
        AppLanguage.GERMAN -> "Zeigt $count aktuelle Momente in der Nähe"
        AppLanguage.RUSSIAN -> "Просмотр $count свежих моментов поблизости"
        AppLanguage.PORTUGUESE -> "Visualizando $count momentos recentes"
        AppLanguage.INDONESIAN -> "Melihat $count momen terbaru di sekitar"
        else -> "Viewing $count latest moments nearby"
    }

    fun momentsEmptyTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "暂无精彩动态"
        AppLanguage.JAPANESE -> "モーメントがまだありません"
        AppLanguage.KOREAN -> "아직 모먼트가 없습니다"
        AppLanguage.ARABIC -> "لا توجد لحظات حتى الآن"
        AppLanguage.SPANISH -> "Aún no hay momentos"
        AppLanguage.FRENCH -> "Aucun moment pour l'instant"
        AppLanguage.GERMAN -> "Noch keine Momente vorhanden"
        AppLanguage.RUSSIAN -> "В ленте пока пусто"
        AppLanguage.PORTUGUESE -> "Nenhum momento ainda"
        AppLanguage.INDONESIAN -> "Belum Ada Momen"
        else -> "No Moments Yet"
    }

    fun momentsEmptyDesc(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "周围还没有人发布照片或故事。成为第一个分享生活精彩的人吧！"
        AppLanguage.JAPANESE -> "まだ写真や投稿がありません。最初のストーリーを共有してみましょう！"
        AppLanguage.KOREAN -> "아직 공유된 사진이나 이야기가 없습니다. 첫 번째 모먼트를 공유해보세요!"
        AppLanguage.ARABIC -> "لم تتم مشاركة أي قصص بعد. كن أول من يشارك لحظات رائعة!"
        AppLanguage.SPANISH -> "Aún no hay historias ni fotos compartidas. ¡Sé el primero en compartir un momento!"
        AppLanguage.FRENCH -> "Aucune histoire partagée. Soyez le premier à partager un moment sympa !"
        AppLanguage.GERMAN -> "Noch keine Beiträge geteilt. Sei der Erste, der einen tollen Moment teilt!"
        AppLanguage.RUSSIAN -> "Здесь пока нет историй или фотографий. Станьте первым, кто поделится моментом!"
        AppLanguage.PORTUGUESE -> "Nenhuma foto compartilhada ainda. Seja o primeiro a compartilhar um momento!"
        AppLanguage.INDONESIAN -> "Belum ada cerita atau foto yang dibagikan. Jadilah yang pertama membagikan momen seru!"
        else -> "No stories or photos shared yet. Be the first to share an exciting moment!"
    }

    fun momentsShareBtn(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "分享动态"
        AppLanguage.JAPANESE -> "モーメントを投稿"
        AppLanguage.KOREAN -> "모먼트 공유"
        AppLanguage.ARABIC -> "مشاركة لحظة"
        AppLanguage.SPANISH -> "Compartir Momento"
        AppLanguage.FRENCH -> "Partager un moment"
        AppLanguage.GERMAN -> "Moment teilen"
        AppLanguage.RUSSIAN -> "Поделиться"
        AppLanguage.PORTUGUESE -> "Compartilhar Momento"
        AppLanguage.INDONESIAN -> "Bagikan Momen"
        else -> "Share Moment"
    }

    fun momentsCreateTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "发布新动态"
        AppLanguage.JAPANESE -> "新しいモーメントを作成"
        AppLanguage.KOREAN -> "새 모먼트 작성"
        AppLanguage.ARABIC -> "نشر لحظة جديدة"
        AppLanguage.SPANISH -> "Publicar Nuevo Momento"
        AppLanguage.FRENCH -> "Nouveau moment"
        AppLanguage.GERMAN -> "Neuen Moment posten"
        AppLanguage.RUSSIAN -> "Создать публикацию"
        AppLanguage.PORTUGUESE -> "Publicar Novo Momento"
        AppLanguage.INDONESIAN -> "Bagikan Momen Baru"
        else -> "Share New Moment"
    }

    fun momentsTimelineNotice(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "分享至动态广场"
        AppLanguage.JAPANESE -> "タイムラインに公開"
        AppLanguage.KOREAN -> "타임라인에 공유"
        AppLanguage.ARABIC -> "مشاركة على الخط الزمني"
        AppLanguage.SPANISH -> "Compartir en el muro"
        AppLanguage.FRENCH -> "Partager sur le fil"
        AppLanguage.GERMAN -> "Im Feed teilen"
        AppLanguage.RUSSIAN -> "Опубликовать в ленте"
        AppLanguage.PORTUGUESE -> "Compartilhar no feed"
        AppLanguage.INDONESIAN -> "Berbagi momen ke linimasa"
        else -> "Share moment to timeline"
    }

    fun momentsInputPlaceholder(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "今天有什么新鲜事？记录下您的生活瞬间..."
        AppLanguage.JAPANESE -> "今日の出来事は？活動や思いを共有しましょう..."
        AppLanguage.KOREAN -> "오늘 무슨 일이 있었나요? 일상을 공유해보세요..."
        AppLanguage.ARABIC -> "ما هي قصتك اليوم؟ شارك أنشطتك..."
        AppLanguage.SPANISH -> "¿Cuál es tu historia hoy? Comparte tus actividades..."
        AppLanguage.FRENCH -> "Quelle est votre histoire aujourd'hui ? Racontez vos activités..."
        AppLanguage.GERMAN -> "Was ist deine Geschichte heute? Erzähle von deinen Aktivitäten..."
        AppLanguage.RUSSIAN -> "О чем вы думаете сегодня? Расскажите о своих делах..."
        AppLanguage.PORTUGUESE -> "Qual é a sua história hoje? Conte suas atividades..."
        AppLanguage.INDONESIAN -> "Apa ceritamu hari ini? Ceritakan aktivitasmu..."
        else -> "What's your story today? Share your activities..."
    }

    fun momentsLocationLabel(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "动态位置"
        AppLanguage.JAPANESE -> "位置情報"
        AppLanguage.KOREAN -> "모먼트 위치"
        AppLanguage.ARABIC -> "موقع اللحظة"
        AppLanguage.SPANISH -> "Ubicación del Momento"
        AppLanguage.FRENCH -> "Lieu du moment"
        AppLanguage.GERMAN -> "Ort des Moments"
        AppLanguage.RUSSIAN -> "Местоположение"
        AppLanguage.PORTUGUESE -> "Localização do Momento"
        AppLanguage.INDONESIAN -> "Lokasi Momen"
        else -> "Moment Location"
    }

    fun momentsPickPhoto(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "从相册选择照片"
        AppLanguage.JAPANESE -> "ギャラリーから写真を選択"
        AppLanguage.KOREAN -> "갤러리에서 사진 선택"
        AppLanguage.ARABIC -> "اختيار صورة من المعرض"
        AppLanguage.SPANISH -> "Elegir Foto de la Galería"
        AppLanguage.FRENCH -> "Choisir depuis la galerie"
        AppLanguage.GERMAN -> "Foto aus Galerie wählen"
        AppLanguage.RUSSIAN -> "Выбрать фото из галереи"
        AppLanguage.PORTUGUESE -> "Escolher Foto da Galeria"
        AppLanguage.INDONESIAN -> "Pilih Foto dari Galeri"
        else -> "Pick Photo from Gallery"
    }

    fun momentsUploading(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "正在上传照片..."
        AppLanguage.JAPANESE -> "写真をアップロード中..."
        AppLanguage.KOREAN -> "사진 업로드 중..."
        AppLanguage.ARABIC -> "جاري رفع الصورة..."
        AppLanguage.SPANISH -> "Subiendo foto..."
        AppLanguage.FRENCH -> "Téléversement de la photo..."
        AppLanguage.GERMAN -> "Foto wird hochgeladen..."
        AppLanguage.RUSSIAN -> "Загрузка фото..."
        AppLanguage.PORTUGUESE -> "Enviando foto..."
        AppLanguage.INDONESIAN -> "Mengunggah foto..."
        else -> "Uploading photo..."
    }

    fun momentsCommentsTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "评论"
        AppLanguage.JAPANESE -> "コメント"
        AppLanguage.KOREAN -> "댓글"
        AppLanguage.ARABIC -> "التعليقات"
        AppLanguage.SPANISH -> "Comentarios"
        AppLanguage.FRENCH -> "Commentaires"
        AppLanguage.GERMAN -> "Kommentare"
        AppLanguage.RUSSIAN -> "Комментарии"
        AppLanguage.PORTUGUESE -> "Comentários"
        AppLanguage.INDONESIAN -> "Komentar"
        else -> "Comments"
    }

    fun momentsNoCommentsTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "暂无评论"
        AppLanguage.JAPANESE -> "コメントはまだありません"
        AppLanguage.KOREAN -> "아직 댓글이 없습니다"
        AppLanguage.ARABIC -> "لا توجد تعليقات حتى الآن"
        AppLanguage.SPANISH -> "Sin comentarios aún"
        AppLanguage.FRENCH -> "Aucun commentaire"
        AppLanguage.GERMAN -> "Noch keine Kommentare"
        AppLanguage.RUSSIAN -> "Комментариев пока нет"
        AppLanguage.PORTUGUESE -> "Nenhum comentário ainda"
        AppLanguage.INDONESIAN -> "Belum Ada Komentar"
        else -> "No Comments Yet"
    }

    fun momentsNoCommentsDesc(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "快来做第一个发表评论互动的人吧！"
        AppLanguage.JAPANESE -> "最初に挨拶やコメントを残してみましょう！"
        AppLanguage.KOREAN -> "가장 먼저 인사를 건네고 댓글을 달아보세요!"
        AppLanguage.ARABIC -> "كن أول من يلقي التحية ويترك تعليقاً لطيفاً!"
        AppLanguage.SPANISH -> "¡Sé el primero en saludar y dejar un comentario!"
        AppLanguage.FRENCH -> "Soyez le premier à saluer et à laisser un commentaire !"
        AppLanguage.GERMAN -> "Sei der Erste, der grüßt und einen Kommentar hinterlässt!"
        AppLanguage.RUSSIAN -> "Будьте первым, кто поздоровается и оставит комментарий!"
        AppLanguage.PORTUGUESE -> "Seja o primeiro a dizer olá e deixar um comentário!"
        AppLanguage.INDONESIAN -> "Jadilah yang pertama menyapa dan memberi komentar!"
        else -> "Be the first to say hi and leave a friendly comment!"
    }

    fun momentsWriteComment(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "写下友善的评论..."
        AppLanguage.JAPANESE -> "温かいコメントを書く..."
        AppLanguage.KOREAN -> "따뜻한 댓글을 남겨보세요..."
        AppLanguage.ARABIC -> "اكتب تعليقاً لطيفاً..."
        AppLanguage.SPANISH -> "Escribe un comentario amable..."
        AppLanguage.FRENCH -> "Écrivez un commentaire sympathique..."
        AppLanguage.GERMAN -> "Schreibe einen freundlichen Kommentar..."
        AppLanguage.RUSSIAN -> "Напишите доброжелательный комментарий..."
        AppLanguage.PORTUGUESE -> "Escreva um comentário amigável..."
        AppLanguage.INDONESIAN -> "Tulis komentar ramah..."
        else -> "Write a friendly comment..."
    }

    fun momentsCommentSent(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "评论已发送！"
        AppLanguage.JAPANESE -> "コメントを送信しました！"
        AppLanguage.KOREAN -> "댓글이 전송되었습니다!"
        AppLanguage.ARABIC -> "تم إرسال التعليق!"
        AppLanguage.SPANISH -> "¡Comentario enviado!"
        AppLanguage.FRENCH -> "Commentaire envoyé !"
        AppLanguage.GERMAN -> "Kommentar gesendet!"
        AppLanguage.RUSSIAN -> "Комментарий отправлен!"
        AppLanguage.PORTUGUESE -> "Comentário enviado!"
        AppLanguage.INDONESIAN -> "Komentar terkirim!"
        else -> "Comment sent!"
    }

    fun momentsDeleteTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "删除动态？"
        AppLanguage.JAPANESE -> "モーメントを削除しますか？"
        AppLanguage.KOREAN -> "모먼트를 삭제하시겠습니까?"
        AppLanguage.ARABIC -> "حذف اللحظة؟"
        AppLanguage.SPANISH -> "¿Eliminar momento?"
        AppLanguage.FRENCH -> "Supprimer le moment ?"
        AppLanguage.GERMAN -> "Moment löschen?"
        AppLanguage.RUSSIAN -> "Удалить публикацию?"
        AppLanguage.PORTUGUESE -> "Excluir momento?"
        AppLanguage.INDONESIAN -> "Hapus Momen?"
        else -> "Delete Moment?"
    }

    fun momentsDeleteDesc(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "您确定要删除此条动态吗？此操作无法撤销。"
        AppLanguage.JAPANESE -> "このモーメントを削除してもよろしいですか？この操作は元に戻せません。"
        AppLanguage.KOREAN -> "이 모먼트를 정말 삭제하시겠습니까? 삭제 후 취소할 수 없습니다."
        AppLanguage.ARABIC -> "هل أنت متأكد من حذف هذه اللحظة؟ لا يمكن التراجع عن هذا الإجراء."
        AppLanguage.SPANISH -> "¿Estás seguro de que deseas eliminar este momento? Esta acción no se puede deshacer."
        AppLanguage.FRENCH -> "Voulez-vous vraiment supprimer ce moment ? Cette action est irréversible."
        AppLanguage.GERMAN -> "Möchtest du diesen Moment wirklich löschen? Dies kann nicht rückgängig gemacht werden."
        AppLanguage.RUSSIAN -> "Вы уверены, что хотите удалить эту запись? Действие нельзя отменить."
        AppLanguage.PORTUGUESE -> "Tem certeza de que deseja excluir este momento? Esta ação não pode ser desfeita."
        AppLanguage.INDONESIAN -> "Apakah kamu yakin ingin menghapus momen ini? Tindakan ini tidak dapat dibatalkan."
        else -> "Are you sure you want to delete this moment? This action cannot be undone."
    }

    // --- NEARBY RADAR & LIST ---
    fun nearbyInteractiveRadar(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "互动雷达"
        AppLanguage.JAPANESE -> "インタラクティブレーダー"
        AppLanguage.KOREAN -> "인터랙티브 레이더"
        AppLanguage.ARABIC -> "رادار تفاعلي"
        AppLanguage.SPANISH -> "Radar Interactivo"
        AppLanguage.FRENCH -> "Radar interactif"
        AppLanguage.GERMAN -> "Interaktives Radar"
        AppLanguage.RUSSIAN -> "Интерактивный радар"
        AppLanguage.PORTUGUESE -> "Radar Interativo"
        AppLanguage.INDONESIAN -> "Radar Interaktif"
        else -> "Interactive Radar"
    }

    fun nearbyUserList(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "用户列表"
        AppLanguage.JAPANESE -> "ユーザー一覧"
        AppLanguage.KOREAN -> "사용자 목록"
        AppLanguage.ARABIC -> "قائمة المستخدمين"
        AppLanguage.SPANISH -> "Lista de Usuarios"
        AppLanguage.FRENCH -> "Liste des utilisateurs"
        AppLanguage.GERMAN -> "Benutzerliste"
        AppLanguage.RUSSIAN -> "Список пользователей"
        AppLanguage.PORTUGUESE -> "Lista de Usuários"
        AppLanguage.INDONESIAN -> "Daftar Pengguna"
        else -> "User List"
    }

    fun nearbyStatusOnlineText(lang: AppLanguage, displayed: Int, total: Int, isFiltered: Boolean): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> if (isFiltered) "显示雷达内 $total 人中的 $displayed 位在线用户 🟢" else "在您周围发现了 $displayed 位在线用户 🟢"
        AppLanguage.JAPANESE -> if (isFiltered) "レーダー内の $total 人中 $displayed 人のオンラインユーザーを表示中 🟢" else "あなたの周りで $displayed 人のオンラインユーザーが見つかりました 🟢"
        AppLanguage.KOREAN -> if (isFiltered) "주변 $total 명 중 온라인 상태인 $displayed 명 표시 중 🟢" else "내 주변 온라인 친구 $displayed 명 발견 🟢"
        AppLanguage.ARABIC -> if (isFiltered) "عرض $displayed من أصل $total مستخدم متصل في الرادار 🟢" else "تم العثور على $displayed مستخدم متصل في منطقتك 🟢"
        AppLanguage.SPANISH -> if (isFiltered) "Mostrando $displayed de $total personas en línea en tu radar 🟢" else "Se encontraron $displayed personas en línea cerca 🟢"
        AppLanguage.FRENCH -> if (isFiltered) "Affichage de $displayed sur $total personnes en ligne sur votre radar 🟢" else "$displayed personnes en ligne trouvées à proximité 🟢"
        AppLanguage.GERMAN -> if (isFiltered) "Zeigt $displayed von $total Online-Personen auf deinem Radar 🟢" else "$displayed Online-Personen in deiner Nähe gefunden 🟢"
        AppLanguage.RUSSIAN -> if (isFiltered) "Отображается $displayed из $total пользователей в сети на радаре 🟢" else "Найдено $displayed пользователей в сети поблизости 🟢"
        AppLanguage.PORTUGUESE -> if (isFiltered) "Exibindo $displayed de $total pessoas online no seu radar 🟢" else "Encontradas $displayed pessoas online na sua área 🟢"
        AppLanguage.INDONESIAN -> if (isFiltered) "Menampilkan $displayed dari $total orang online dalam radar sekitarmu 🟢" else "Ditemukan $displayed orang yang sedang online di sekitarmu 🟢"
        else -> if (isFiltered) "Showing $displayed of $total online people in your nearby radar 🟢" else "Found $displayed online people in your area 🟢"
    }

    fun nearbyStatusAllText(lang: AppLanguage, displayed: Int, total: Int, isUnlocked: Boolean): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> if (isUnlocked) "发现身边 $displayed 位好友（全部已解锁 ✨）" else "显示身边 $total 位好友中的 $displayed 位"
        AppLanguage.JAPANESE -> if (isUnlocked) "周囲に $displayed 人のユーザーが見つかりました（すべて解放済み ✨）" else "周囲の $total 人中 $displayed 人を表示中"
        AppLanguage.KOREAN -> if (isUnlocked) "주변 $displayed 명의 친구를 찾았습니다 (모두 잠금 해제됨 ✨)" else "주변 $total 명 중 $displayed 명 표시 중"
        AppLanguage.ARABIC -> if (isUnlocked) "تم العثور على $displayed شخص في منطقتك (تم الفتح بالكامل ✨)" else "عرض $displayed من أصل $total شخص في الرادار"
        AppLanguage.SPANISH -> if (isUnlocked) "Se encontraron $displayed personas cerca (Todos Desbloqueados ✨)" else "Mostrando $displayed de $total personas en tu radar"
        AppLanguage.FRENCH -> if (isUnlocked) "$displayed personnes trouvées (Tout est débloqué ✨)" else "Affichage de $displayed sur $total personnes"
        AppLanguage.GERMAN -> if (isUnlocked) "$displayed Personen in deiner Nähe gefunden (Alle freigeschaltet ✨)" else "Zeigt $displayed von $total Personen auf deinem Radar"
        AppLanguage.RUSSIAN -> if (isUnlocked) "Найдено $displayed человек поблизости (Все разблокированы ✨)" else "Отображается $displayed из $total человек на радаре"
        AppLanguage.PORTUGUESE -> if (isUnlocked) "Encontradas $displayed pessoas na sua área (Todos Desbloqueados ✨)" else "Exibindo $displayed de $total pessoas no radar"
        AppLanguage.INDONESIAN -> if (isUnlocked) "Ditemukan $displayed orang dalam radius sekitarmu (Semua Terbuka ✨)" else "Menampilkan $displayed dari $total orang dalam radar sekitarmu"
        else -> if (isUnlocked) "Found $displayed people in your area (All Unlocked ✨)" else "Showing $displayed of $total people in your nearby radar"
    }

    fun nearbyEmptyOnlineTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "附近暂无在线用户"
        AppLanguage.JAPANESE -> "近くにオンラインユーザーがいません"
        AppLanguage.KOREAN -> "주변에 온라인 사용자가 없습니다"
        AppLanguage.ARABIC -> "لا يوجد مستخدمون متصلون بالقرب منك"
        AppLanguage.SPANISH -> "No hay usuarios en línea cerca"
        AppLanguage.FRENCH -> "Aucun utilisateur en ligne à proximité"
        AppLanguage.GERMAN -> "Keine Online-Benutzer in der Nähe"
        AppLanguage.RUSSIAN -> "Рядом нет пользователей в сети"
        AppLanguage.PORTUGUESE -> "Nenhum usuário online por perto"
        AppLanguage.INDONESIAN -> "Belum Ada Pengguna Online di Sekitar"
        else -> "No Online Users Nearby"
    }

    fun nearbyEmptyOnlineDesc(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "当前身边没有在线好友。您可以关闭“仅看在线”筛选或稍后重新扫描。"
        AppLanguage.JAPANESE -> "現在近くにオンラインのユーザーはいません。「オンラインのみ」フィルターをオフにするか再試行してください。"
        AppLanguage.KOREAN -> "현재 온라인 상태인 주변 친구가 없습니다. '온라인만' 필터를 해제하거나 다시 스캔해보세요."
        AppLanguage.ARABIC -> "لا يوجد أصدقاء متصلون حالياً بالقرب منك. يمكنك إيقاف فلتر 'المتصلون فقط' أو إعادة المسح."
        AppLanguage.SPANISH -> "No hay amigos en línea cerca en este momento. Puedes desactivar el filtro 'Solo en línea' o escanear de nuevo."
        AppLanguage.FRENCH -> "Aucun ami en ligne à proximité. Désactivez le filtre « En ligne seulement » ou relancez le scan."
        AppLanguage.GERMAN -> "Zurzeit sind keine Freunde in der Nähe online. Schalte den 'Nur online'-Filter aus oder scanne erneut."
        AppLanguage.RUSSIAN -> "Поблизости сейчас нет пользователей в сети. Отключите фильтр «Только в сети» или повторите поиск."
        AppLanguage.PORTUGUESE -> "Nenhum amigo online por perto agora. Você pode desativar o filtro 'Apenas online' ou escanear novamente."
        AppLanguage.INDONESIAN -> "Saat ini belum ada teman di sekitar yang sedang online. Anda dapat mematikan filter 'Hanya Online' atau pindai ulang nanti."
        else -> "No friends nearby are currently online. You can turn off the 'Online Only' filter or scan again later."
    }

    fun nearbyEmptyGeneralTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "附近暂无其他用户"
        AppLanguage.JAPANESE -> "近くにユーザーが見つかりません"
        AppLanguage.KOREAN -> "주변에 사용자가 없습니다"
        AppLanguage.ARABIC -> "لم يتم العثور على مستخدمين بالجوار"
        AppLanguage.SPANISH -> "Aún no hay usuarios cerca"
        AppLanguage.FRENCH -> "Aucun utilisateur à proximité"
        AppLanguage.GERMAN -> "Keine Benutzer in der Nähe gefunden"
        AppLanguage.RUSSIAN -> "Поблизости не найдено пользователей"
        AppLanguage.PORTUGUESE -> "Nenhum usuário encontrado por perto"
        AppLanguage.INDONESIAN -> "Belum Ada Pengguna di Sekitar"
        else -> "No Users Nearby Yet"
    }

    fun nearbyEmptyGeneralDesc(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "当前位置附近暂无其他活跃用户。请确保 GPS 已开启并尝试重新扫描！"
        AppLanguage.JAPANESE -> "現在地周辺にアクティブなユーザーがいません。GPSを有効にして再度スキャンしてください！"
        AppLanguage.KOREAN -> "현재 위치 주변에 활동 중인 사용자가 없습니다. GPS가 켜져 있는지 확인하고 다시 스캔해보세요!"
        AppLanguage.ARABIC -> "لا يوجد مستخدمون نشطون بالقرب من موقعك الحالي. تأكد من تفعيل GPS وحاول المسح مجدداً!"
        AppLanguage.SPANISH -> "No hay otros usuarios activos cerca de tu ubicación ahora. ¡Asegúrate de tener el GPS activado y vuelve a escanear!"
        AppLanguage.FRENCH -> "Aucun utilisateur actif près de votre position. Vérifiez que le GPS est activé et réessayez !"
        AppLanguage.GERMAN -> "Keine weiteren aktiven Benutzer an deinem Standort gefunden. Aktiviere GPS und scanne erneut!"
        AppLanguage.RUSSIAN -> "Поблизости нет активных пользователей. Убедитесь, что GPS включен, и повторите поиск!"
        AppLanguage.PORTUGUESE -> "Nenhum usuário ativo perto da sua localização. Verifique se o GPS está ativado e tente escanear novamente!"
        AppLanguage.INDONESIAN -> "Belum ada pengguna aktif lain di sekitar lokasi Anda saat ini. Pastikan GPS aktif dan coba pindai ulang!"
        else -> "No other active users found near your location right now. Ensure GPS is enabled and try scanning again!"
    }

    fun nearbyScanNow(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "立即扫描"
        AppLanguage.JAPANESE -> "今すぐスキャン"
        AppLanguage.KOREAN -> "지금 스캔"
        AppLanguage.ARABIC -> "مسح الآن"
        AppLanguage.SPANISH -> "Escanear Ahora"
        AppLanguage.FRENCH -> "Scanner maintenant"
        AppLanguage.GERMAN -> "Jetzt scannen"
        AppLanguage.RUSSIAN -> "Искать сейчас"
        AppLanguage.PORTUGUESE -> "Escanear Agora"
        AppLanguage.INDONESIAN -> "Pindai Sekarang"
        else -> "Scan Now"
    }

    fun nearbyScanAgain(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "重新扫描"
        AppLanguage.JAPANESE -> "再スキャン"
        AppLanguage.KOREAN -> "다시 스캔"
        AppLanguage.ARABIC -> "إعادة المسح"
        AppLanguage.SPANISH -> "Escanear de Nuevo"
        AppLanguage.FRENCH -> "Scanner à nouveau"
        AppLanguage.GERMAN -> "Erneut scannen"
        AppLanguage.RUSSIAN -> "Искать снова"
        AppLanguage.PORTUGUESE -> "Escanear Novamente"
        AppLanguage.INDONESIAN -> "Pindai Ulang"
        else -> "Scan Again"
    }

    // --- USER PROFILE BOTTOM SHEET ---
    fun sheetBioTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "个性签名 & 关于我"
        AppLanguage.JAPANESE -> "自己紹介 & プロフィール"
        AppLanguage.KOREAN -> "자기소개 & 정보"
        AppLanguage.ARABIC -> "النبذة وعني"
        AppLanguage.SPANISH -> "Biografía y Acerca de Mí"
        AppLanguage.FRENCH -> "Bio & À propos de moi"
        AppLanguage.GERMAN -> "Bio & Über mich"
        AppLanguage.RUSSIAN -> "О себе и статус"
        AppLanguage.PORTUGUESE -> "Bio & Sobre Mim"
        AppLanguage.INDONESIAN -> "Bio & Tentang Saya"
        else -> "Bio & About Me"
    }

    fun sheetMomentsTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "动态 & 最新故事"
        AppLanguage.JAPANESE -> "モーメント & 最新ストーリー"
        AppLanguage.KOREAN -> "모먼트 & 최근 이야기"
        AppLanguage.ARABIC -> "اللحظات وأحدث القصص"
        AppLanguage.SPANISH -> "Momentos y Relatos Recientes"
        AppLanguage.FRENCH -> "Moments & histoires récentes"
        AppLanguage.GERMAN -> "Momente & aktuelle Geschichten"
        AppLanguage.RUSSIAN -> "Моменты и свежие истории"
        AppLanguage.PORTUGUESE -> "Momentos e Histórias Recentes"
        AppLanguage.INDONESIAN -> "Momen & Cerita Terbaru"
        else -> "Moments & Recent Stories"
    }

    fun sheetNoMoments(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "尚未发布过动态"
        AppLanguage.JAPANESE -> "まだモーメントを共有していません"
        AppLanguage.KOREAN -> "아직 공유한 모먼트가 없습니다"
        AppLanguage.ARABIC -> "لم تتم مشاركة أي لحظات بعد"
        AppLanguage.SPANISH -> "Aún no ha compartido fotos ni momentos"
        AppLanguage.FRENCH -> "N'a pas encore partagé de photos"
        AppLanguage.GERMAN -> "Hat noch keine Momente geteilt"
        AppLanguage.RUSSIAN -> "Еще не делился(ась) моментами"
        AppLanguage.PORTUGUESE -> "Ainda não compartilhou momentos"
        AppLanguage.INDONESIAN -> "Belum membagikan foto momen"
        else -> "No moment photos shared yet"
    }

    fun sheetSayHi(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "打招呼"
        AppLanguage.JAPANESE -> "挨拶する"
        AppLanguage.KOREAN -> "인사하기"
        AppLanguage.ARABIC -> "إلقاء التحية"
        AppLanguage.SPANISH -> "Saludar"
        AppLanguage.FRENCH -> "Saluer"
        AppLanguage.GERMAN -> "Grüßen"
        AppLanguage.RUSSIAN -> "Привет"
        AppLanguage.PORTUGUESE -> "Dar Olá"
        AppLanguage.INDONESIAN -> "Sapa"
        else -> "Say Hi"
    }

    fun sheetChat(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "发消息"
        AppLanguage.JAPANESE -> "チャット"
        AppLanguage.KOREAN -> "채팅 보내기"
        AppLanguage.ARABIC -> "إرسال رسالة"
        AppLanguage.SPANISH -> "Enviar Mensaje"
        AppLanguage.FRENCH -> "Envoyer un message"
        AppLanguage.GERMAN -> "Nachricht senden"
        AppLanguage.RUSSIAN -> "Написать"
        AppLanguage.PORTUGUESE -> "Enviar Mensagem"
        AppLanguage.INDONESIAN -> "Kirim Pesan"
        else -> "Send Message"
    }

    fun sheetBlock(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "拉黑用户"
        AppLanguage.JAPANESE -> "ブロック"
        AppLanguage.KOREAN -> "차단하기"
        AppLanguage.ARABIC -> "حظر"
        AppLanguage.SPANISH -> "Bloquear"
        AppLanguage.FRENCH -> "Bloquer"
        AppLanguage.GERMAN -> "Blockieren"
        AppLanguage.RUSSIAN -> "Заблокировать"
        AppLanguage.PORTUGUESE -> "Bloquear"
        AppLanguage.INDONESIAN -> "Blokir"
        else -> "Block"
    }

    fun sheetUnblock(lang: AppLanguage): String = chatUnblockBtn(lang)

    fun sheetReport(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "举报"
        AppLanguage.JAPANESE -> "報告する"
        AppLanguage.KOREAN -> "신고하기"
        AppLanguage.ARABIC -> "إبلاغ"
        AppLanguage.SPANISH -> "Reportar"
        AppLanguage.FRENCH -> "Signaler"
        AppLanguage.GERMAN -> "Melden"
        AppLanguage.RUSSIAN -> "Пожаловаться"
        AppLanguage.PORTUGUESE -> "Denunciar"
        AppLanguage.INDONESIAN -> "Laporkan"
        else -> "Report"
    }

    fun sheetBlockConfirmTitle(lang: AppLanguage, name: String): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "拉黑 $name？"
        AppLanguage.JAPANESE -> "「$name」をブロックしますか？"
        AppLanguage.KOREAN -> "$name 님을 차단하시겠습니까?"
        AppLanguage.ARABIC -> "حظر $name؟"
        AppLanguage.SPANISH -> "¿Bloquear a $name?"
        AppLanguage.FRENCH -> "Bloquer $name ?"
        AppLanguage.GERMAN -> "$name blockieren?"
        AppLanguage.RUSSIAN -> "Заблокировать $name?"
        AppLanguage.PORTUGUESE -> "Bloquear $name?"
        AppLanguage.INDONESIAN -> "Blokir $name?"
        else -> "Block $name?"
    }

    fun sheetBlockConfirmDesc(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "拉黑后该用户将无法向您发送消息，也不会出现在您附近的雷达中。"
        AppLanguage.JAPANESE -> "ブロックすると、このユーザーはあなたにメッセージを送信できなくなり、レーダーにも表示されなくなります。"
        AppLanguage.KOREAN -> "차단하면 이 사용자는 회원님께 메시지를 보낼 수 없으며 주변 레이더에도 나타나지 않습니다."
        AppLanguage.ARABIC -> "لن يتمكن هذا المستخدم من إرسال رسائل إليك ولن يظهر في رادار الأشخاص القريبين منك."
        AppLanguage.SPANISH -> "Este usuario no podrá enviarte mensajes ni aparecerá en tu radar cercano."
        AppLanguage.FRENCH -> "Cet utilisateur ne pourra plus vous envoyer de messages et n'apparaîtra plus sur votre radar."
        AppLanguage.GERMAN -> "Dieser Benutzer kann dir keine Nachrichten mehr senden und wird nicht mehr auf deinem Radar angezeigt."
        AppLanguage.RUSSIAN -> "Этот пользователь не сможет отправлять вам сообщения и не будет отображаться на радаре."
        AppLanguage.PORTUGUESE -> "Este usuário não poderá enviar mensagens para você e não aparecerá no seu radar."
        AppLanguage.INDONESIAN -> "Pengguna ini tidak akan dapat mengirimi Anda pesan dan tidak akan muncul di radar sekitar Anda."
        else -> "This user will not be able to message you and will not appear in your nearby radar."
    }

    // --- REPORT DIALOG ---
    fun reportTitleUser(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "举报用户"
        AppLanguage.JAPANESE -> "ユーザーを報告"
        AppLanguage.KOREAN -> "사용자 신고"
        AppLanguage.ARABIC -> "الإبلاغ عن مستخدم"
        AppLanguage.SPANISH -> "Reportar Usuario"
        AppLanguage.FRENCH -> "Signaler l'utilisateur"
        AppLanguage.GERMAN -> "Benutzer melden"
        AppLanguage.RUSSIAN -> "Пожаловаться на пользователя"
        AppLanguage.PORTUGUESE -> "Denunciar Usuário"
        AppLanguage.INDONESIAN -> "Laporkan Pengguna"
        else -> "Report User"
    }

    fun reportTitleMoment(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "举报动态"
        AppLanguage.JAPANESE -> "モーメントを報告"
        AppLanguage.KOREAN -> "모먼트 신고"
        AppLanguage.ARABIC -> "الإبلاغ عن اللحظة"
        AppLanguage.SPANISH -> "Reportar Momento"
        AppLanguage.FRENCH -> "Signaler le moment"
        AppLanguage.GERMAN -> "Moment melden"
        AppLanguage.RUSSIAN -> "Пожаловаться на публикацию"
        AppLanguage.PORTUGUESE -> "Denunciar Momento"
        AppLanguage.INDONESIAN -> "Laporkan Momen"
        else -> "Report Moment"
    }

    fun reportTitleMessage(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "举报消息"
        AppLanguage.JAPANESE -> "メッセージを報告"
        AppLanguage.KOREAN -> "메시지 신고"
        AppLanguage.ARABIC -> "الإبلاغ عن الرسالة"
        AppLanguage.SPANISH -> "Reportar Mensaje"
        AppLanguage.FRENCH -> "Signaler le message"
        AppLanguage.GERMAN -> "Nachricht melden"
        AppLanguage.RUSSIAN -> "Пожаловаться на сообщение"
        AppLanguage.PORTUGUESE -> "Denunciar Mensagem"
        AppLanguage.INDONESIAN -> "Laporkan Pesan"
        else -> "Report Message"
    }

    fun reportSubtitle(lang: AppLanguage, targetName: String): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "举报“$targetName”违反社区守则的行为"
        AppLanguage.JAPANESE -> "「$targetName」のコミュニティガイドライン違反を報告"
        AppLanguage.KOREAN -> "\"$targetName\" 님의 커뮤니티 가이드라인 위반 신고"
        AppLanguage.ARABIC -> "الإبلاغ عن مخالفة قواعد المجتمع لـ \"$targetName\""
        AppLanguage.SPANISH -> "Reportar contenido que viola las normas comunitarias para \"$targetName\""
        AppLanguage.FRENCH -> "Signaler un contenu enfreignant les règles pour « $targetName »"
        AppLanguage.GERMAN -> "Verstoß gegen die Richtlinien für \"$targetName\" melden"
        AppLanguage.RUSSIAN -> "Пожаловаться на нарушение правил сообщества \"$targetName\""
        AppLanguage.PORTUGUESE -> "Denunciar violação das regras da comunidade para \"$targetName\""
        AppLanguage.INDONESIAN -> "Laporkan konten yang melanggar aturan komunitas untuk \"$targetName\""
        else -> "Report content violating community rules for \"$targetName\""
    }

    fun reportSelectReason(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "选择举报原因"
        AppLanguage.JAPANESE -> "報告理由を選択"
        AppLanguage.KOREAN -> "신고 사유 선택"
        AppLanguage.ARABIC -> "اختر سبب الإبلاغ"
        AppLanguage.SPANISH -> "Seleccionar Motivo"
        AppLanguage.FRENCH -> "Sélectionner une raison"
        AppLanguage.GERMAN -> "Grund auswählen"
        AppLanguage.RUSSIAN -> "Выберите причину"
        AppLanguage.PORTUGUESE -> "Selecione o Motivo"
        AppLanguage.INDONESIAN -> "Pilih Alasan Pelaporan"
        else -> "Select Report Reason"
    }

    fun reportNotesPlaceholder(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "补充说明（选填）..."
        AppLanguage.JAPANESE -> "追加の詳細（任意）..."
        AppLanguage.KOREAN -> "추가 설명 (선택 사항)..."
        AppLanguage.ARABIC -> "ملاحظات إضافية (اختياري)..."
        AppLanguage.SPANISH -> "Detalles adicionales (opcional)..."
        AppLanguage.FRENCH -> "Détails supplémentaires (facultatif)..."
        AppLanguage.GERMAN -> "Zusätzliche Angaben (optional)..."
        AppLanguage.RUSSIAN -> "Дополнительные сведения (необязательно)..."
        AppLanguage.PORTUGUESE -> "Detalhes adicionais (opcional)..."
        AppLanguage.INDONESIAN -> "Keterangan tambahan (opsional)..."
        else -> "Additional details (optional)..."
    }

    fun reportAlsoBlockCheckbox(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "同时拉黑该用户"
        AppLanguage.JAPANESE -> "同時にこのユーザーをブロックする"
        AppLanguage.KOREAN -> "이 사용자를 함께 차단하기"
        AppLanguage.ARABIC -> "حظر هذا المستخدم أيضاً"
        AppLanguage.SPANISH -> "Bloquear a este usuario también"
        AppLanguage.FRENCH -> "Bloquer également cet utilisateur"
        AppLanguage.GERMAN -> "Diesen Benutzer ebenfalls blockieren"
        AppLanguage.RUSSIAN -> "Также заблокировать пользователя"
        AppLanguage.PORTUGUESE -> "Bloquear este usuário também"
        AppLanguage.INDONESIAN -> "Blokir pengguna ini sekaligus"
        else -> "Also block this user"
    }

    fun reportSubmitBtn(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "提交举报"
        AppLanguage.JAPANESE -> "報告を送信"
        AppLanguage.KOREAN -> "신고 제출"
        AppLanguage.ARABIC -> "إرسال البلاغ"
        AppLanguage.SPANISH -> "Enviar Reporte"
        AppLanguage.FRENCH -> "Envoyer le signalement"
        AppLanguage.GERMAN -> "Meldung absenden"
        AppLanguage.RUSSIAN -> "Отправить жалобу"
        AppLanguage.PORTUGUESE -> "Enviar Denúncia"
        AppLanguage.INDONESIAN -> "Kirim Laporan"
        else -> "Submit Report"
    }

    fun reportSuccessToast(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "举报已提交，感谢您协助维护良好的社区环境！"
        AppLanguage.JAPANESE -> "報告を受け付けました。安心できる環境作りにご協力いただきありがとうございます！"
        AppLanguage.KOREAN -> "신고가 접수되었습니다. 안전한 커뮤니티를 만드는 데 도움을 주셔서 감사합니다!"
        AppLanguage.ARABIC -> "تم إرسال البلاغ بنجاح. شكراً لمساعدتك في الحفاظ على مجتمع آمن!"
        AppLanguage.SPANISH -> "¡Reporte enviado! Gracias por ayudarnos a mantener segura la comunidad."
        AppLanguage.FRENCH -> "Signalement envoyé. Merci de nous aider à préserver la communauté !"
        AppLanguage.GERMAN -> "Meldung gesendet. Danke für deine Mithilfe zur Sicherheit der Community!"
        AppLanguage.RUSSIAN -> "Жалоба отправлена. Спасибо за помощь в поддержании безопасности сообщества!"
        AppLanguage.PORTUGUESE -> "Denúncia enviada com sucesso. Obrigado por manter a comunidade segura!"
        AppLanguage.INDONESIAN -> "Laporan berhasil dikirim. Terima kasih telah menjaga kenyamanan komunitas!"
        else -> "Report submitted successfully. Thank you for keeping our community safe!"
    }

    fun reportReasonsUser(lang: AppLanguage): List<String> = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> listOf("骚扰 / 仇恨言论 / 威胁", "垃圾广告 / 商业欺诈", "不当或低俗头像", "虚假账号 / 冒充他人", "其他可疑行为")
        AppLanguage.JAPANESE -> listOf("嫌がらせ / ヘイト / 脅迫", "スパム / 詐欺行為", "不適切なプロフィール画像", "なりすまし / 偽アカウント", "その他の不審な行為")
        AppLanguage.KOREAN -> listOf("괴롭힘 / 혐오 발언 / 위협", "스팸 / 상업적 사기", "부적절한 프로필 사진", "사칭 / 가짜 계정", "기타 의심스러운 행동")
        AppLanguage.ARABIC -> listOf("مضايقة / خطاب كراهية / تهديد", "احتيال / رسائل غير مرغوب فيها", "صورة شخصية غير لائقة", "انتحال شخصية / حساب وهمي", "سلوك مشبوه آخر")
        AppLanguage.SPANISH -> listOf("Acoso / Mensajes de odio / Amenazas", "Spam / Estafa comercial", "Foto de perfil inapropiada", "Cuenta falsa / Suplantación", "Otro comportamiento sospechoso")
        AppLanguage.FRENCH -> listOf("Harcèlement / Haine / Menaces", "Spam / Escroquerie", "Photo de profil inappropriée", "Faux compte / Usurpation", "Autre comportement suspect")
        AppLanguage.GERMAN -> listOf("Belästigung / Hassrede / Drohung", "Spam / Betrug", "Unangemessenes Profilbild", "Gefälschtes Konto", "Anderes verdächtiges Verhalten")
        AppLanguage.RUSSIAN -> listOf("Оскорбления / Угрозы / Вражда", "Спам / Мошенничество", "Неподобающее фото профиля", "Фейковый аккаунт", "Другое подозрительное поведение")
        AppLanguage.PORTUGUESE -> listOf("Assédio / Discurso de ódio / Ameaças", "Spam / Golpe comercial", "Foto de perfil inadequada", "Conta falsa / Falsidade", "Outro comportamento suspeito")
        AppLanguage.INDONESIAN -> listOf("Pelecehan / Ujaran Kebencian / Ancaman", "Spam / Penipuan Komersial", "Foto Profil Tidak Pantas / Vulgar", "Akun Palsu / Meniru Orang Lain", "Perilaku Mencurigakan Lainnya")
        else -> listOf("Harassment / Hate Speech / Threats", "Spam / Commercial Fraud", "Inappropriate Profile Photo", "Fake Account / Impersonation", "Other Suspicious Behavior")
    }

    fun reportReasonsMoment(lang: AppLanguage): List<String> = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> listOf("低俗 / 色情内容", "垃圾广告 / 非法推广", "仇恨言论 / 歧视", "暴力 / 危险内容", "侵权 / 其他违规")
        AppLanguage.JAPANESE -> listOf("不適切 / 成人向けコンテンツ", "スパム / 違法広告", "ヘイトスピーチ / 差別", "暴力 / 危険なコンテンツ", "著作権侵害 / その他")
        AppLanguage.KOREAN -> listOf("음란물 / 성인용 콘텐츠", "스팸 / 불법 광고", "혐오 발언 / 차별", "폭력 / 유해한 콘텐츠", "저작권 침해 / 기타")
        AppLanguage.ARABIC -> listOf("محتوى غير لائق / إباحي", "إعلانات غير قانونية / احتيال", "خطاب كراهية / تمييز", "عنف / محتوى خطير", "انتهاك حقوق أو أخرى")
        AppLanguage.SPANISH -> listOf("Contenido vulgar / Adulto", "Spam / Publicidad engañosa", "Discurso de odio / Discriminación", "Violencia / Contenido peligroso", "Violación de derechos / Otros")
        AppLanguage.FRENCH -> listOf("Contenu vulgaire / Pour adultes", "Spam / Publicité illégale", "Discours haineux / Discrimination", "Violence / Contenu dangereux", "Violation de droits / Autre")
        AppLanguage.GERMAN -> listOf("Unangemessener / Anstößiger Inhalt", "Spam / Betrug", "Hassrede / Diskriminierung", "Gewalt / Gefährliche Inhalte", "Urheberrechtsverletzung / Sonstiges")
        AppLanguage.RUSSIAN -> listOf("Неподобающий контент", "Спам / Нелегальная реклама", "Язык вражды / Дискриминация", "Насилие / Опасный контент", "Нарушение авторских прав / Другое")
        AppLanguage.PORTUGUESE -> listOf("Conteúdo vulgar / Adulto", "Spam / Publicidade ilegal", "Discurso de ódio / Discriminação", "Violência / Conteúdo perigoso", "Violação de direitos / Outro")
        AppLanguage.INDONESIAN -> listOf("Konten Vulgar / Pornografi", "Spam / Iklan Ilegal / Scam", "Ujaran Kebencian / Diskriminasi", "Kekerasan / Konten Berbahaya", "Pelanggaran Hak Cipta / Lainnya")
        else -> listOf("Inappropriate / Adult Content", "Spam / Illegal Ads / Scam", "Hate Speech / Discrimination", "Violence / Dangerous Content", "Copyright Infringement / Other")
    }

    fun reportReasonsMessage(lang: AppLanguage): List<String> = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> listOf("侮辱骚扰言论", "钓鱼网站或欺诈链接", "低俗色情照片", "其他违规信息")
        AppLanguage.JAPANESE -> listOf("不適切なメッセージ / 嫌がらせ", "フィッシング / 詐欺リンク", "不適切な画像", "その他の違反")
        AppLanguage.KOREAN -> listOf("모욕 및 괴롭힘 메시지", "피싱 또는 사기 링크", "부적절한 사진", "기타 위반 사항")
        AppLanguage.ARABIC -> listOf("رسائل مسيئة أو مزعجة", "روابط احتيال أو تصيد", "صور غير لائقة", "انتهاكات أخرى")
        AppLanguage.SPANISH -> listOf("Mensajes ofensivos / Acoso", "Enlaces fraudulentos o phishing", "Fotos inapropiadas", "Otras infracciones")
        AppLanguage.FRENCH -> listOf("Messages offensants / Harcèlement", "Liens suspects / Hameçonnage", "Photos inappropriées", "Autre infraction")
        AppLanguage.GERMAN -> listOf("Beleidigende Nachrichten", "Phishing oder Betrugslinks", "Unangemessene Fotos", "Andere Verstöße")
        AppLanguage.RUSSIAN -> listOf("Оскорбительные сообщения", "Фишинг или мошеннические ссылки", "Неподобающие фото", "Другие нарушения")
        AppLanguage.PORTUGUESE -> listOf("Mensagens ofensivas / Assédio", "Links suspeitos / Fraude", "Fotos inadequadas", "Outras violações")
        AppLanguage.INDONESIAN -> listOf("Pesan menghina / Kasar / Pelecehan", "Tautan penipuan atau phishing", "Foto tidak pantas dalam chat", "Pelanggaran pesan lainnya")
        else -> listOf("Offensive messages / Harassment", "Phishing or scam links", "Inappropriate photos in chat", "Other message violations")
    }

    // --- QR CODE DIALOGS ---
    fun qrMyTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "我的二维码名片"
        AppLanguage.JAPANESE -> "マイQRコード"
        AppLanguage.KOREAN -> "내 QR 코드"
        AppLanguage.ARABIC -> "رمز QR لملفي الشخصي"
        AppLanguage.SPANISH -> "Mi Código QR de Perfil"
        AppLanguage.FRENCH -> "Mon code QR de profil"
        AppLanguage.GERMAN -> "Mein Profil-QR-Code"
        AppLanguage.RUSSIAN -> "Мой QR-код"
        AppLanguage.PORTUGUESE -> "Meu Código QR de Perfil"
        AppLanguage.INDONESIAN -> "Kode QR Profil Saya"
        else -> "My Profile QR Code"
    }

    fun qrMySubtitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "展示或分享此二维码，让新朋友在 Lovy Chat 中快速添加您。"
        AppLanguage.JAPANESE -> "このQRコードを提示または共有して、Lovy Chatですぐに友達追加してもらいましょう。"
        AppLanguage.KOREAN -> "이 QR 코드를 보여주거나 공유하여 Lovy Chat에서 친구를 바로 추가해보세요."
        AppLanguage.ARABIC -> "أظهر أو شارك هذا الرمز ليتمكن الأصدقاء الجدد من إضافتك مباشرة على Lovy Chat."
        AppLanguage.SPANISH -> "Muestra o comparte este código para que nuevos amigos te agreguen en Lovy Chat."
        AppLanguage.FRENCH -> "Montrez ou partagez ce code pour que de nouveaux amis vous ajoutent sur Lovy Chat."
        AppLanguage.GERMAN -> "Zeige oder teile diesen Code, damit neue Freunde dich direkt auf Lovy Chat hinzufügen."
        AppLanguage.RUSSIAN -> "Покажите или отправьте этот QR-код, чтобы друзья могли быстро добавить вас в Lovy Chat."
        AppLanguage.PORTUGUESE -> "Mostre ou compartilhe este código para que novos amigos adicionem você no Lovy Chat."
        AppLanguage.INDONESIAN -> "Tunjukkan atau bagikan kode ini agar teman baru dapat langsung menambahkanmu di Lovy Chat"
        else -> "Show or share this code so new friends can directly add you on Lovy Chat"
    }

    fun qrShareBtn(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "分享二维码"
        AppLanguage.JAPANESE -> "QRを共有"
        AppLanguage.KOREAN -> "QR 공유"
        AppLanguage.ARABIC -> "مشاركة الرمز"
        AppLanguage.SPANISH -> "Compartir QR"
        AppLanguage.FRENCH -> "Partager QR"
        AppLanguage.GERMAN -> "QR teilen"
        AppLanguage.RUSSIAN -> "Поделиться QR"
        AppLanguage.PORTUGUESE -> "Compartilhar QR"
        AppLanguage.INDONESIAN -> "Bagikan QR"
        else -> "Share QR"
    }

    fun qrCopyIdBtn(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "复制 ID"
        AppLanguage.JAPANESE -> "IDをコピー"
        AppLanguage.KOREAN -> "ID 복사"
        AppLanguage.ARABIC -> "نسخ المعرف"
        AppLanguage.SPANISH -> "Copiar ID"
        AppLanguage.FRENCH -> "Copier l'ID"
        AppLanguage.GERMAN -> "ID kopieren"
        AppLanguage.RUSSIAN -> "Копировать ID"
        AppLanguage.PORTUGUESE -> "Copiar ID"
        AppLanguage.INDONESIAN -> "Salin ID"
        else -> "Copy ID"
    }

    fun qrCopiedToast(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "Lovy ID 已成功复制到剪贴板！"
        AppLanguage.JAPANESE -> "Lovy ID をクリップボードにコピーしました！"
        AppLanguage.KOREAN -> "Lovy ID 가 클립보드에 복사되었습니다!"
        AppLanguage.ARABIC -> "تم نسخ معرف Lovy Chat إلى الحافظة!"
        AppLanguage.SPANISH -> "¡ID de Lovy Chat copiado al portapapeles!"
        AppLanguage.FRENCH -> "ID Lovy Chat copié dans le presse-papiers !"
        AppLanguage.GERMAN -> "Lovy Chat ID in die Zwischenablage kopiert!"
        AppLanguage.RUSSIAN -> "ID Lovy Chat скопирован в буфер обмена!"
        AppLanguage.PORTUGUESE -> "ID do Lovy Chat copiado para a área de transferência!"
        AppLanguage.INDONESIAN -> "ID Lovy Chat berhasil disalin ke papan klip!"
        else -> "Lovy Chat ID copied to clipboard!"
    }

    fun qrShareBody(lang: AppLanguage, lovyId: String): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "嗨！在 Lovy Chat 上与我联系并添加我为好友，我的 Lovy ID 是：$lovyId"
        AppLanguage.JAPANESE -> "こんにちは！Lovy Chatで私を追加してつながりましょう。Lovy ID: $lovyId"
        AppLanguage.KOREAN -> "안녕하세요! Lovy Chat에서 저를 친구로 추가해주세요. Lovy ID: $lovyId"
        AppLanguage.ARABIC -> "مرحباً! تواصل معي وأضفني على Lovy Chat باستخدام معرف Lovy: $lovyId"
        AppLanguage.SPANISH -> "¡Hola! Conéctate conmigo y agrégame en Lovy Chat con mi Lovy ID: $lovyId"
        AppLanguage.FRENCH -> "Bonjour ! Contactez-moi et ajoutez-moi sur Lovy Chat avec mon ID Lovy : $lovyId"
        AppLanguage.GERMAN -> "Hallo! Kontaktiere mich und füge mich auf Lovy Chat mit meiner Lovy-ID hinzu: $lovyId"
        AppLanguage.RUSSIAN -> "Привет! Свяжитесь со мной и добавьте в друзья в Lovy Chat по моему Lovy ID: $lovyId"
        AppLanguage.PORTUGUESE -> "Olá! Conecte-se comigo e me adicione no Lovy Chat com meu ID Lovy: $lovyId"
        AppLanguage.INDONESIAN -> "Hai! Hubungi dan tambahkan saya di Lovy Chat dengan ID Lovy: $lovyId"
        else -> "Hi! Connect with me and add me on Lovy Chat with Lovy ID: $lovyId"
    }

    fun qrFlashlightDesc(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "手电筒"
        AppLanguage.JAPANESE -> "懐中電灯"
        AppLanguage.KOREAN -> "손전등"
        AppLanguage.ARABIC -> "المصباح اليدوي"
        AppLanguage.SPANISH -> "Linterna"
        AppLanguage.FRENCH -> "Lampe torche"
        AppLanguage.GERMAN -> "Taschenlampe"
        AppLanguage.RUSSIAN -> "Фонарик"
        AppLanguage.PORTUGUESE -> "Lanterna"
        AppLanguage.INDONESIAN -> "Senter"
        else -> "Flashlight"
    }

    fun qrScannerTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "扫码添加好友"
        AppLanguage.JAPANESE -> "QRコードをスキャン"
        AppLanguage.KOREAN -> "QR / 바코드 스캔"
        AppLanguage.ARABIC -> "مسح رمز QR أو الباركود"
        AppLanguage.SPANISH -> "Escanear Código QR"
        AppLanguage.FRENCH -> "Scanner le code QR"
        AppLanguage.GERMAN -> "QR-Code scannen"
        AppLanguage.RUSSIAN -> "Сканировать QR-код"
        AppLanguage.PORTUGUESE -> "Escanear Código QR"
        AppLanguage.INDONESIAN -> "Pindai Barcode / QR"
        else -> "Scan QR / Barcode"
    }

    fun qrScannerSubtitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "将摄像头对准好友的二维码，或从相册中选择图片"
        AppLanguage.JAPANESE -> "カメラを友達のQRコードに向けるか、ギャラリーから画像を選択してください"
        AppLanguage.KOREAN -> "친구의 QR 코드에 카메라를 맞추거나 갤러리에서 사진을 선택하세요"
        AppLanguage.ARABIC -> "وجّه الكاميرا نحو رمز صديقك أو اختر صورة من المعرض"
        AppLanguage.SPANISH -> "Apunta la cámara al código QR de un amigo o elígelo de la galería"
        AppLanguage.FRENCH -> "Pointez la caméra vers le code QR d'un ami ou choisissez depuis la galerie"
        AppLanguage.GERMAN -> "Richte die Kamera auf den QR-Code oder wähle ein Bild aus der Galerie"
        AppLanguage.RUSSIAN -> "Наведите камеру на QR-код друга или выберите изображение из галереи"
        AppLanguage.PORTUGUESE -> "Aponte a câmera para o código QR ou escolha uma foto da galeria"
        AppLanguage.INDONESIAN -> "Arahkan kamera ke kode QR teman atau unggah gambar dari galeri"
        else -> "Point camera at friend's QR code or upload an image from gallery"
    }

    fun qrNotFoundToast(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "未检测到有效的二维码"
        AppLanguage.JAPANESE -> "有効なQRコードが検出されませんでした"
        AppLanguage.KOREAN -> "유효한 QR 코드가 감지되지 않았습니다"
        AppLanguage.ARABIC -> "لم يتم العثور على رمز QR صالح"
        AppLanguage.SPANISH -> "No se detectó ningún código QR válido"
        AppLanguage.FRENCH -> "Aucun code QR valide détecté"
        AppLanguage.GERMAN -> "Kein gültiger QR-Code erkannt"
        AppLanguage.RUSSIAN -> "Действительный QR-код не обнаружен"
        AppLanguage.PORTUGUESE -> "Nenhum código QR válido detectado"
        AppLanguage.INDONESIAN -> "Tidak ada kode QR yang terdeteksi"
        else -> "No valid QR code detected"
    }

    fun qrUserNotFound(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "未找到该 ID 或二维码对应的用户"
        AppLanguage.JAPANESE -> "このIDまたはQRコードのユーザーは見つかりませんでした"
        AppLanguage.KOREAN -> "해당 ID 또는 QR 코드의 사용자를 찾을 수 없습니다"
        AppLanguage.ARABIC -> "لم يتم العثور على مستخدم بهذا المعرف"
        AppLanguage.SPANISH -> "No se encontró ningún usuario con este ID o código"
        AppLanguage.FRENCH -> "Aucun utilisateur trouvé avec cet identifiant"
        AppLanguage.GERMAN -> "Kein Benutzer mit dieser ID gefunden"
        AppLanguage.RUSSIAN -> "Пользователь с таким ID не найден"
        AppLanguage.PORTUGUESE -> "Nenhum usuário encontrado com este ID"
        AppLanguage.INDONESIAN -> "Pengguna dengan ID atau barcode ini tidak ditemukan"
        else -> "User with this ID or barcode was not found"
    }

    // --- PROFILE TAB ITEMS & DESCRIPTIONS ---
    fun profileMenuDetailTitle(lang: AppLanguage): String = profileDetailsTitle(lang)

    fun profileMenuDetailSub(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "昵称、个性签名、头像及账户信息"
        AppLanguage.JAPANESE -> "表示名、自己紹介、写真およびアカウント情報"
        AppLanguage.KOREAN -> "표시 이름, 소개글, 사진 및 계정 정보"
        AppLanguage.ARABIC -> "الاسم المعروض والنبذة والصورة ومعلومات الحساب"
        AppLanguage.SPANISH -> "Nombre visible, bio, foto e info de la cuenta"
        AppLanguage.FRENCH -> "Nom affiché, bio, photo et infos du compte"
        AppLanguage.GERMAN -> "Anzeigename, Bio, Foto und Kontoinformationen"
        AppLanguage.RUSSIAN -> "Имя, статус, фото и данные аккаунта"
        AppLanguage.PORTUGUESE -> "Nome de exibição, bio, foto e dados da conta"
        AppLanguage.INDONESIAN -> "Nama tampilan, bio, foto & info akun"
        else -> "Display name, bio, photo & account info"
    }

    fun profileMenuMomentsTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "我的动态"
        AppLanguage.JAPANESE -> "マイモーメント"
        AppLanguage.KOREAN -> "내 모먼트"
        AppLanguage.ARABIC -> "لحظاتي"
        AppLanguage.SPANISH -> "Mis Momentos"
        AppLanguage.FRENCH -> "Mes moments"
        AppLanguage.GERMAN -> "Meine Momente"
        AppLanguage.RUSSIAN -> "Мои моменты"
        AppLanguage.PORTUGUESE -> "Meus Momentos"
        AppLanguage.INDONESIAN -> "Momen Saya"
        else -> "My Moments"
    }

    fun profileMenuMomentsSub(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "记录和查看生活中的照片与故事"
        AppLanguage.JAPANESE -> "日常の写真や思い出のコレクション"
        AppLanguage.KOREAN -> "나의 일상 사진과 이야기 모음"
        AppLanguage.ARABIC -> "مجموعتك من الصور والقصص اليومية"
        AppLanguage.SPANISH -> "Colección de fotos y relatos de tu día a día"
        AppLanguage.FRENCH -> "Collection de photos et récits du quotidien"
        AppLanguage.GERMAN -> "Sammlung deiner täglichen Fotos und Geschichten"
        AppLanguage.RUSSIAN -> "Коллекция ваших ежедневных фото и историй"
        AppLanguage.PORTUGUESE -> "Sua coleção de fotos e histórias diárias"
        AppLanguage.INDONESIAN -> "Koleksi foto dan cerita harianmu"
        else -> "Collection of your daily photos and stories"
    }

    fun profileMenuBottleTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "我的漂流瓶"
        AppLanguage.JAPANESE -> "私のボトル"
        AppLanguage.KOREAN -> "내 유리병 편지"
        AppLanguage.ARABIC -> "زجاجات المحيط الخاصة بي"
        AppLanguage.SPANISH -> "Mis Botellas del Océano"
        AppLanguage.FRENCH -> "Mes bouteilles à la mer"
        AppLanguage.GERMAN -> "Meine Flaschenpost"
        AppLanguage.RUSSIAN -> "Мои бутылки с посланием"
        AppLanguage.PORTUGUESE -> "Minhas Garrafas do Oceano"
        AppLanguage.INDONESIAN -> "Botol Lautan Saya"
        else -> "My Ocean Bottles"
    }

    fun profileMenuBottleSub(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "查看您投放至大海的漂流瓶列表"
        AppLanguage.JAPANESE -> "海に投げたボトルのリストを確認"
        AppLanguage.KOREAN -> "바다에 띄운 유리병 편지 목록"
        AppLanguage.ARABIC -> "قائمة الزجاجات التي قمت برميها في البحر"
        AppLanguage.SPANISH -> "Lista de botellas que has lanzado al mar"
        AppLanguage.FRENCH -> "Liste des bouteilles jetées à la mer"
        AppLanguage.GERMAN -> "Liste deiner ins Meer geworfenen Flaschen"
        AppLanguage.RUSSIAN -> "Список брошенных вами в океан бутылок"
        AppLanguage.PORTUGUESE -> "Lista de garrafas que você lançou ao mar"
        AppLanguage.INDONESIAN -> "Daftar botol yang pernah kamu lempar"
        else -> "List of bottles you have thrown into the ocean"
    }

    fun profileMenuPrivacyTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "隐私与位置设置"
        AppLanguage.JAPANESE -> "プライバシー & 位置情報"
        AppLanguage.KOREAN -> "개인정보 & 위치 설정"
        AppLanguage.ARABIC -> "الخصوصية والموقع"
        AppLanguage.SPANISH -> "Privacidad y Ubicación"
        AppLanguage.FRENCH -> "Confidentialité & Localisation"
        AppLanguage.GERMAN -> "Privatsphäre & Standort"
        AppLanguage.RUSSIAN -> "Конфиденциальность и геопозиция"
        AppLanguage.PORTUGUESE -> "Privacidade e Localização"
        AppLanguage.INDONESIAN -> "Privasi & Lokasi"
        else -> "Privacy & Location"
    }

    fun profileMenuBlockedTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "黑名单管理"
        AppLanguage.JAPANESE -> "ブロック中のユーザー"
        AppLanguage.KOREAN -> "차단된 사용자"
        AppLanguage.ARABIC -> "المستخدمون المحظورون"
        AppLanguage.SPANISH -> "Usuarios Bloqueados"
        AppLanguage.FRENCH -> "Utilisateurs bloqués"
        AppLanguage.GERMAN -> "Blockierte Benutzer"
        AppLanguage.RUSSIAN -> "Заблокированные пользователи"
        AppLanguage.PORTUGUESE -> "Usuários Bloqueados"
        AppLanguage.INDONESIAN -> "Pengguna Diblokir"
        else -> "Blocked Users"
    }

    fun profileMenuNotifTestTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "测试通知与振动"
        AppLanguage.JAPANESE -> "通知と振動のテスト"
        AppLanguage.KOREAN -> "알림 및 진동 테스트"
        AppLanguage.ARABIC -> "اختبار الإشعارات والاهتزاز"
        AppLanguage.SPANISH -> "Probar Notificación y Vibración"
        AppLanguage.FRENCH -> "Tester les notifications et vibrations"
        AppLanguage.GERMAN -> "Benachrichtigung & Vibration testen"
        AppLanguage.RUSSIAN -> "Проверить уведомления и вибрацию"
        AppLanguage.PORTUGUESE -> "Testar Notificações e Vibração"
        AppLanguage.INDONESIAN -> "Uji Notifikasi & Getar"
        else -> "Test Notification & Vibration"
    }

    fun profileMenuNotifTestSub(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "点击测试消息弹窗提示音与设备振动"
        AppLanguage.JAPANESE -> "タップしてポップアップ音と振動をテスト"
        AppLanguage.KOREAN -> "탭하여 알림 팝업 및 기기 진동 테스트"
        AppLanguage.ARABIC -> "اضغط لاختبار صوت الإشعار واهتزاز الجهاز"
        AppLanguage.SPANISH -> "Toca para probar el sonido emergente y la vibración"
        AppLanguage.FRENCH -> "Touchez pour tester le son et les vibrations"
        AppLanguage.GERMAN -> "Tippen, um Benachrichtigungston und Vibration zu testen"
        AppLanguage.RUSSIAN -> "Нажмите для проверки звука и вибрации"
        AppLanguage.PORTUGUESE -> "Toque para testar som pop-up e vibração"
        AppLanguage.INDONESIAN -> "Tekan untuk tes suara pop-up & getaran perangkat"
        else -> "Tap to test pop-up sound & device vibration"
    }

    fun profileMenuPrivacyPolicyTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "隐私政策与条款"
        AppLanguage.JAPANESE -> "プライバシーポリシー & 利用規約"
        AppLanguage.KOREAN -> "개인정보 처리방침 & 이용약관"
        AppLanguage.ARABIC -> "سياسة الخصوصية والشروط"
        AppLanguage.SPANISH -> "Política de Privacidad y Términos"
        AppLanguage.FRENCH -> "Politique de confidentialité & Conditions"
        AppLanguage.GERMAN -> "Datenschutzerklärung & Bedingungen"
        AppLanguage.RUSSIAN -> "Политика конфиденциальности и условия"
        AppLanguage.PORTUGUESE -> "Política de Privacidade e Termos"
        AppLanguage.INDONESIAN -> "Kebijakan Privasi & Ketentuan"
        else -> "Privacy Policy & Terms"
    }

    fun profileTapDetailsHint(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "点击查看个人资料详情 →"
        AppLanguage.JAPANESE -> "タップしてプロフィール詳細を確認 →"
        AppLanguage.KOREAN -> "탭하여 프로필 상세 보기 →"
        AppLanguage.ARABIC -> "انقر لعرض تفاصيل الملف الشخصي ←"
        AppLanguage.SPANISH -> "Toca para ver detalles del perfil →"
        AppLanguage.FRENCH -> "Touchez pour voir le profil détaillé →"
        AppLanguage.GERMAN -> "Tippen für Profildetails →"
        AppLanguage.RUSSIAN -> "Нажмите, чтобы просмотреть профиль →"
        AppLanguage.PORTUGUESE -> "Toque para ver detalhes do perfil →"
        AppLanguage.INDONESIAN -> "Ketuk untuk lihat detail profil →"
        else -> "Tap to view profile details →"
    }

    fun tagBlocked(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "已拉黑"
        AppLanguage.JAPANESE -> "ブロック中"
        AppLanguage.KOREAN -> "차단됨"
        AppLanguage.ARABIC -> "محظور"
        AppLanguage.SPANISH -> "Bloqueado"
        AppLanguage.FRENCH -> "Bloqué"
        AppLanguage.GERMAN -> "Blockiert"
        AppLanguage.RUSSIAN -> "Заблокирован"
        AppLanguage.PORTUGUESE -> "Bloqueado"
        AppLanguage.INDONESIAN -> "Diblokir"
        else -> "Blocked"
    }

    fun chatTapViewProfile(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "轻触查看资料"
        AppLanguage.JAPANESE -> "タップしてプロフィールを表示"
        AppLanguage.KOREAN -> "프로필 보기"
        AppLanguage.ARABIC -> "انقر لعرض الملف"
        AppLanguage.SPANISH -> "Toca para ver perfil"
        AppLanguage.FRENCH -> "Toucher pour voir le profil"
        AppLanguage.GERMAN -> "Tippen für Profil"
        AppLanguage.RUSSIAN -> "Нажмите для профиля"
        AppLanguage.PORTUGUESE -> "Toque para ver perfil"
        AppLanguage.INDONESIAN -> "Ketuk lihat profil"
        else -> "Tap to view profile"
    }

    fun chatBlockedSubtitle(lang: AppLanguage): String = "${tagBlocked(lang)} • ${chatTapViewProfile(lang)}"
    fun chatOnlineSubtitle(lang: AppLanguage): String = "${friendsOnline(lang)} • ${chatTapViewProfile(lang)}"

    fun commonPhoto(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "照片"
        AppLanguage.JAPANESE -> "写真"
        AppLanguage.KOREAN -> "사진"
        AppLanguage.ARABIC -> "صورة"
        AppLanguage.SPANISH -> "Foto"
        AppLanguage.FRENCH -> "Photo"
        AppLanguage.GERMAN -> "Foto"
        AppLanguage.RUSSIAN -> "Фото"
        AppLanguage.PORTUGUESE -> "Foto"
        AppLanguage.INDONESIAN -> "Foto"
        else -> "Photo"
    }

    fun chatYou(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "您"
        AppLanguage.JAPANESE -> "あなた"
        AppLanguage.KOREAN -> "나"
        AppLanguage.ARABIC -> "أنت"
        AppLanguage.SPANISH -> "Tú"
        AppLanguage.FRENCH -> "Vous"
        AppLanguage.GERMAN -> "Du"
        AppLanguage.RUSSIAN -> "Вы"
        AppLanguage.PORTUGUESE -> "Você"
        AppLanguage.INDONESIAN -> "Anda"
        else -> "You"
    }

    fun chatCancelReply(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "取消回复"
        AppLanguage.JAPANESE -> "返信をキャンセル"
        AppLanguage.KOREAN -> "답장 취소"
        AppLanguage.ARABIC -> "إلغاء الرد"
        AppLanguage.SPANISH -> "Cancelar respuesta"
        AppLanguage.FRENCH -> "Annuler la réponse"
        AppLanguage.GERMAN -> "Antwort abbrechen"
        AppLanguage.RUSSIAN -> "Отменить ответ"
        AppLanguage.PORTUGUESE -> "Cancelar resposta"
        AppLanguage.INDONESIAN -> "Batal Balas"
        else -> "Cancel reply"
    }

    fun chatPhotoInputHint(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "输入文字说明或点击发送按钮"
        AppLanguage.JAPANESE -> "説明を入力するか送信ボタンを押してください"
        AppLanguage.KOREAN -> "설명을 입력하거나 전송 버튼을 누르세요"
        AppLanguage.ARABIC -> "اكتب تعليقاً أو اضغط زر الإرسال"
        AppLanguage.SPANISH -> "Escribe un pie de foto o presiona enviar"
        AppLanguage.FRENCH -> "Tapez une légende ou appuyez sur envoyer"
        AppLanguage.GERMAN -> "Bildbeschreibung eingeben oder Senden tippen"
        AppLanguage.RUSSIAN -> "Введите описание или нажмите отправить"
        AppLanguage.PORTUGUESE -> "Digite uma legenda ou toque em enviar"
        AppLanguage.INDONESIAN -> "Ketik keterangan atau tekan tombol kirim"
        else -> "Type a caption or press send button"
    }

    fun chatSendPhotoTooltip(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "发送照片"
        AppLanguage.JAPANESE -> "写真を送信"
        AppLanguage.KOREAN -> "사진 보내기"
        AppLanguage.ARABIC -> "إرسال صورة"
        AppLanguage.SPANISH -> "Enviar Foto"
        AppLanguage.FRENCH -> "Envoyer une photo"
        AppLanguage.GERMAN -> "Foto senden"
        AppLanguage.RUSSIAN -> "Отправить фото"
        AppLanguage.PORTUGUESE -> "Enviar Foto"
        AppLanguage.INDONESIAN -> "Kirim Foto"
        else -> "Send Photo"
    }

    fun chatPhotoTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "聊天照片"
        AppLanguage.JAPANESE -> "チャットの写真"
        AppLanguage.KOREAN -> "채팅 사진"
        AppLanguage.ARABIC -> "صورة المحادثة"
        AppLanguage.SPANISH -> "Foto del Chat"
        AppLanguage.FRENCH -> "Photo de la discussion"
        AppLanguage.GERMAN -> "Chat-Foto"
        AppLanguage.RUSSIAN -> "Фото из чата"
        AppLanguage.PORTUGUESE -> "Foto da Conversa"
        AppLanguage.INDONESIAN -> "Foto Obrolan"
        else -> "Chat Photo"
    }

    fun partnerProfileBlockedBanner(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "该用户在您的黑名单中。无法接收其消息，且不会出现在附近列表中。"
        AppLanguage.JAPANESE -> "このユーザーはブロックされています。メッセージを受信できず、近くの人にも表示されません。"
        AppLanguage.KOREAN -> "이 사용자는 차단 목록에 있습니다. 메시지를 받을 수 없으며 주변 목록에 표시되지 않습니다."
        AppLanguage.ARABIC -> "هذا المستخدم في قائمة الحظر. لا يمكنه إرسال رسائل ولا يظهر في الجوار."
        AppLanguage.SPANISH -> "Este usuario está en tu lista de bloqueados. No puede enviar mensajes ni aparecer en cercanos."
        AppLanguage.FRENCH -> "Cet utilisateur est bloqué. Il ne peut plus envoyer de message ni apparaître à proximité."
        AppLanguage.GERMAN -> "Dieser Benutzer ist blockiert. Er kann keine Nachrichten senden und erscheint nicht in der Nähe."
        AppLanguage.RUSSIAN -> "Этот пользователь заблокирован. Он не может отправлять сообщения и скрыт из поиска рядом."
        AppLanguage.PORTUGUESE -> "Este usuário está na sua lista de bloqueados. Ele não pode enviar mensagens."
        AppLanguage.INDONESIAN -> "Pengguna ini berada dalam daftar blokir Anda. Tidak dapat mengirim pesan dan tidak muncul di Orang di Sekitar."
        else -> "This user is on your blocked list. Cannot send messages and won't appear in People Nearby."
    }

    fun partnerDistanceAway(lang: AppLanguage, dist: String): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "距离 $dist"
        AppLanguage.JAPANESE -> "距離 $dist"
        AppLanguage.KOREAN -> "$dist 거리"
        AppLanguage.ARABIC -> "يبعد $dist"
        AppLanguage.SPANISH -> "A $dist de distancia"
        AppLanguage.FRENCH -> "À $dist"
        AppLanguage.GERMAN -> "$dist entfernt"
        AppLanguage.RUSSIAN -> "В $dist от вас"
        AppLanguage.PORTUGUESE -> "A $dist de distância"
        AppLanguage.INDONESIAN -> "$dist dari Anda"
        else -> "$dist away"
    }

    fun chatPartnerNoMoments(lang: AppLanguage, name: String): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "$name 尚未分享任何动态或照片。"
        AppLanguage.JAPANESE -> "$name はまだアクティブな写真やモーメントを共有していません。"
        AppLanguage.KOREAN -> "$name 님이 아직 사진이나 모먼트를 공유하지 않았습니다."
        AppLanguage.ARABIC -> "لم يقم $name بمشاركة أي صور أو لحظات بعد."
        AppLanguage.SPANISH -> "$name aún no ha compartido fotos ni momentos."
        AppLanguage.FRENCH -> "$name n'a pas encore partagé de photos ou de moments."
        AppLanguage.GERMAN -> "$name hat noch keine Fotos oder Momente geteilt."
        AppLanguage.RUSSIAN -> "$name еще не поделился(лась) фотографиями или моментами."
        AppLanguage.PORTUGUESE -> "$name ainda não compartilhou fotos ou momentos."
        AppLanguage.INDONESIAN -> "$name belum membagikan foto atau momen yang aktif."
        else -> "$name hasn't shared any active photos or moments yet."
    }

    fun chatUnblockPartnerBtn(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "解除对该用户的拉黑"
        AppLanguage.JAPANESE -> "ユーザーのブロックを解除"
        AppLanguage.KOREAN -> "사용자 차단 해제"
        AppLanguage.ARABIC -> "إلغاء حظر المستخدم"
        AppLanguage.SPANISH -> "Desbloquear Usuario"
        AppLanguage.FRENCH -> "Débloquer l'utilisateur"
        AppLanguage.GERMAN -> "Benutzer freigeben"
        AppLanguage.RUSSIAN -> "Разблокировать пользователя"
        AppLanguage.PORTUGUESE -> "Desbloquear Usuário"
        AppLanguage.INDONESIAN -> "Buka Blokir Pengguna"
        else -> "Unblock User"
    }

    fun chatBlockPartnerBtn(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "拉黑此用户"
        AppLanguage.JAPANESE -> "このユーザーをブロック"
        AppLanguage.KOREAN -> "이 사용자 차단하기"
        AppLanguage.ARABIC -> "حظر هذا المستخدم"
        AppLanguage.SPANISH -> "Bloquear a este Usuario"
        AppLanguage.FRENCH -> "Bloquer cet utilisateur"
        AppLanguage.GERMAN -> "Diesen Benutzer blockieren"
        AppLanguage.RUSSIAN -> "Заблокировать пользователя"
        AppLanguage.PORTUGUESE -> "Bloquear este Usuário"
        AppLanguage.INDONESIAN -> "Blokir Pengguna Ini"
        else -> "Block this User"
    }

    fun chatReportPartnerBtn(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "举报此用户"
        AppLanguage.JAPANESE -> "このユーザーを通報"
        AppLanguage.KOREAN -> "이 사용자 신고하기"
        AppLanguage.ARABIC -> "الإبلاغ عن هذا المستخدم"
        AppLanguage.SPANISH -> "Reportar a este Usuario"
        AppLanguage.FRENCH -> "Signaler cet utilisateur"
        AppLanguage.GERMAN -> "Diesen Benutzer melden"
        AppLanguage.RUSSIAN -> "Пожаловаться на пользователя"
        AppLanguage.PORTUGUESE -> "Denunciar este Usuário"
        AppLanguage.INDONESIAN -> "Laporkan Pengguna Ini"
        else -> "Report this User"
    }

    fun chatBlockPartnerConfirmTitle(lang: AppLanguage, name: String): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "拉黑 $name？"
        AppLanguage.JAPANESE -> "$name をブロックしますか？"
        AppLanguage.KOREAN -> "$name 님을 차단하시겠습니까?"
        AppLanguage.ARABIC -> "حظر $name؟"
        AppLanguage.SPANISH -> "¿Bloquear a $name?"
        AppLanguage.FRENCH -> "Bloquer $name ?"
        AppLanguage.GERMAN -> "$name blockieren?"
        AppLanguage.RUSSIAN -> "Заблокировать $name?"
        AppLanguage.PORTUGUESE -> "Bloquear $name?"
        AppLanguage.INDONESIAN -> "Blokir $name?"
        else -> "Block $name?"
    }

    fun chatBlockPartnerConfirmDesc(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "该用户将无法再向您发送消息，也不会出现在“附近的人”中。"
        AppLanguage.JAPANESE -> "このユーザーはあなたにメッセージを送信できなくなり、近くの人にも表示されなくなります。"
        AppLanguage.KOREAN -> "이 사용자는 더 이상 메시지를 보낼 수 없으며 주변 사람 목록에도 나타나지 않습니다."
        AppLanguage.ARABIC -> "لن يتمكن هذا المستخدم من إرسال رسائل إليك ولن يظهر في قائمة القريبين."
        AppLanguage.SPANISH -> "Este usuario ya no podrá enviarte mensajes ni aparecerá en personas cercanas."
        AppLanguage.FRENCH -> "Cet utilisateur ne pourra plus vous envoyer de message et n'apparaîtra plus à proximité."
        AppLanguage.GERMAN -> "Dieser Benutzer kann dir keine Nachrichten mehr senden und wird nicht mehr in der Nähe angezeigt."
        AppLanguage.RUSSIAN -> "Этот пользователь больше не сможет отправлять вам сообщения и не появится в списке рядом."
        AppLanguage.PORTUGUESE -> "Este usuário não poderá mais enviar mensagens nem aparecer nas proximidades."
        AppLanguage.INDONESIAN -> "Pengguna ini tidak akan dapat mengirim pesan lagi kepadamu dan tidak akan muncul di daftar Orang di Sekitar."
        else -> "This user will no longer be able to send you messages and won't appear in People Nearby."
    }

    fun photoZoomHint(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "放大照片"
        AppLanguage.JAPANESE -> "写真を拡大"
        AppLanguage.KOREAN -> "사진 확대"
        AppLanguage.ARABIC -> "تكبير الصورة"
        AppLanguage.SPANISH -> "Ampliar Foto"
        AppLanguage.FRENCH -> "Agrandir la photo"
        AppLanguage.GERMAN -> "Foto vergrößern"
        AppLanguage.RUSSIAN -> "Увеличить фото"
        AppLanguage.PORTUGUESE -> "Ampliar Foto"
        AppLanguage.INDONESIAN -> "Perbesar Foto"
        else -> "Zoom Photo"
    }

    fun btnLike(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "点赞"
        AppLanguage.JAPANESE -> "いいね"
        AppLanguage.KOREAN -> "좋아요"
        AppLanguage.ARABIC -> "إعجاب"
        AppLanguage.SPANISH -> "Me gusta"
        AppLanguage.FRENCH -> "J'aime"
        AppLanguage.GERMAN -> "Gefällt mir"
        AppLanguage.RUSSIAN -> "Нравится"
        AppLanguage.PORTUGUESE -> "Curtir"
        AppLanguage.INDONESIAN -> "Suka"
        else -> "Like"
    }

    fun chatStatusRead(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "已读（双蓝勾）"
        AppLanguage.JAPANESE -> "既読（青いチェック）"
        AppLanguage.KOREAN -> "읽음 (파란색 체크 2개)"
        AppLanguage.ARABIC -> "تمت القراءة"
        AppLanguage.SPANISH -> "Mensaje leído"
        AppLanguage.FRENCH -> "Message lu"
        AppLanguage.GERMAN -> "Gelesen"
        AppLanguage.RUSSIAN -> "Прочитано"
        AppLanguage.PORTUGUESE -> "Mensagem lida"
        AppLanguage.INDONESIAN -> "Pesan dibaca (Centang dua biru)"
        else -> "Message read (Two blue ticks)"
    }

    fun chatStatusSent(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "已发送"
        AppLanguage.JAPANESE -> "送信済み"
        AppLanguage.KOREAN -> "전송됨"
        AppLanguage.ARABIC -> "تم الإرسال"
        AppLanguage.SPANISH -> "Mensaje enviado"
        AppLanguage.FRENCH -> "Message envoyé"
        AppLanguage.GERMAN -> "Gesendet"
        AppLanguage.RUSSIAN -> "Отправлено"
        AppLanguage.PORTUGUESE -> "Mensagem enviada"
        AppLanguage.INDONESIAN -> "Pesan terkirim (Centang dua abu-abu)"
        else -> "Message sent (Two grey ticks)"
    }

    fun chatMessageFallback(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "消息"
        AppLanguage.JAPANESE -> "メッセージ"
        AppLanguage.KOREAN -> "메시지"
        AppLanguage.ARABIC -> "رسالة"
        AppLanguage.SPANISH -> "Mensaje"
        AppLanguage.FRENCH -> "Message"
        AppLanguage.GERMAN -> "Nachricht"
        AppLanguage.RUSSIAN -> "Сообщение"
        AppLanguage.PORTUGUESE -> "Mensagem"
        AppLanguage.INDONESIAN -> "Pesan"
        else -> "Message"
    }

    // --- PARTNER PROFILE & DIALOGS ---
    fun partnerProfileSheetTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "用户资料"
        AppLanguage.JAPANESE -> "プロフィール詳細"
        AppLanguage.KOREAN -> "프로필 상세"
        AppLanguage.ARABIC -> "الملف التعريفي"
        AppLanguage.SPANISH -> "Perfil del Usuario"
        AppLanguage.FRENCH -> "Profil de l'utilisateur"
        AppLanguage.GERMAN -> "Benutzerprofil"
        AppLanguage.RUSSIAN -> "Профиль пользователя"
        AppLanguage.PORTUGUESE -> "Perfil do Usuário"
        AppLanguage.INDONESIAN -> "Profil Teman"
        else -> "User Profile"
    }

    fun commonClose(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "关闭"
        AppLanguage.JAPANESE -> "閉じる"
        AppLanguage.KOREAN -> "닫기"
        AppLanguage.ARABIC -> "إغلاق"
        AppLanguage.SPANISH -> "Cerrar"
        AppLanguage.FRENCH -> "Fermer"
        AppLanguage.GERMAN -> "Schließen"
        AppLanguage.RUSSIAN -> "Закрыть"
        AppLanguage.PORTUGUESE -> "Fechar"
        AppLanguage.INDONESIAN -> "Tutup"
        else -> "Close"
    }

    fun commonVerified(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "已认证"
        AppLanguage.JAPANESE -> "認証済み"
        AppLanguage.KOREAN -> "인증됨"
        AppLanguage.ARABIC -> "تم التحقق"
        AppLanguage.SPANISH -> "Verificado"
        AppLanguage.FRENCH -> "Vérifié"
        AppLanguage.GERMAN -> "Verifiziert"
        AppLanguage.RUSSIAN -> "Подтвержден"
        AppLanguage.PORTUGUESE -> "Verificado"
        AppLanguage.INDONESIAN -> "Terverifikasi"
        else -> "Verified"
    }

    fun genderLabel(lang: AppLanguage, gender: Any?): String {
        val isFemale = gender?.toString()?.contains("FEMALE", ignoreCase = true) == true
        return when (resolveLang(lang)) {
            AppLanguage.CHINESE -> if (isFemale) "女性" else "男性"
            AppLanguage.JAPANESE -> if (isFemale) "女性" else "男性"
            AppLanguage.KOREAN -> if (isFemale) "여성" else "남성"
            AppLanguage.ARABIC -> if (isFemale) "أنثى" else "ذكر"
            AppLanguage.SPANISH -> if (isFemale) "Mujer" else "Hombre"
            AppLanguage.FRENCH -> if (isFemale) "Femme" else "Homme"
            AppLanguage.GERMAN -> if (isFemale) "Weiblich" else "Männlich"
            AppLanguage.RUSSIAN -> if (isFemale) "Женский" else "Мужской"
            AppLanguage.PORTUGUESE -> if (isFemale) "Feminino" else "Masculino"
            AppLanguage.INDONESIAN -> if (isFemale) "Perempuan" else "Laki-laki"
            else -> if (isFemale) "Female" else "Male"
        }
    }

    fun ageYears(lang: AppLanguage, age: Int): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "$age 岁"
        AppLanguage.JAPANESE -> "$age 歳"
        AppLanguage.KOREAN -> "$age 세"
        AppLanguage.ARABIC -> "$age سنة"
        AppLanguage.SPANISH -> "$age años"
        AppLanguage.FRENCH -> "$age ans"
        AppLanguage.GERMAN -> "$age Jahre"
        AppLanguage.RUSSIAN -> "$age лет"
        AppLanguage.PORTUGUESE -> "$age anos"
        AppLanguage.INDONESIAN -> "$age thn"
        else -> "$age yrs"
    }

    fun locationDistance(lang: AppLanguage, city: String, distance: Any?): String {
        val distStr = when (distance) {
            is Number -> if (distance.toDouble() < 1.0) "< 1 km" else "%.1f km".format(distance.toDouble())
            null -> ""
            else -> distance.toString()
        }
        return if (distStr.isBlank()) city else if (city.isBlank()) distStr else "$city • $distStr"
    }

    fun partnerProfileBio(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "个人简介"
        AppLanguage.JAPANESE -> "自己紹介"
        AppLanguage.KOREAN -> "소개"
        AppLanguage.ARABIC -> "نبذة شخصية"
        AppLanguage.SPANISH -> "Biografía"
        AppLanguage.FRENCH -> "Bio"
        AppLanguage.GERMAN -> "Biografie"
        AppLanguage.RUSSIAN -> "О себе"
        AppLanguage.PORTUGUESE -> "Biografia"
        AppLanguage.INDONESIAN -> "Tentang Saya (Bio)"
        else -> "About Me (Bio)"
    }

    fun partnerProfileRecentMoments(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "动态与照片"
        AppLanguage.JAPANESE -> "最近のモーメント"
        AppLanguage.KOREAN -> "최근 모먼트"
        AppLanguage.ARABIC -> "أحدث اللحظات"
        AppLanguage.SPANISH -> "Momentos Recientes"
        AppLanguage.FRENCH -> "Moments récents"
        AppLanguage.GERMAN -> "Neueste Momente"
        AppLanguage.RUSSIAN -> "Недавние моменты"
        AppLanguage.PORTUGUESE -> "Momentos Recentes"
        AppLanguage.INDONESIAN -> "Momen Terbaru"
        else -> "Recent Moments"
    }

    fun partnerProfileMomentsCount(lang: AppLanguage, count: Int): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "$count 条动态"
        AppLanguage.JAPANESE -> "$count 件の投稿"
        AppLanguage.KOREAN -> "$count 개의 모먼트"
        AppLanguage.ARABIC -> "$count لحظة"
        AppLanguage.SPANISH -> "$count momentos"
        AppLanguage.FRENCH -> "$count moments"
        AppLanguage.GERMAN -> "$count Momente"
        AppLanguage.RUSSIAN -> "$count моментов"
        AppLanguage.PORTUGUESE -> "$count momentos"
        AppLanguage.INDONESIAN -> "$count Momen"
        else -> "$count Moments"
    }

    fun partnerProfileNoMoments(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "暂无发布的动态"
        AppLanguage.JAPANESE -> "投稿されたモーメントはありません"
        AppLanguage.KOREAN -> "게시된 모먼트가 없습니다"
        AppLanguage.ARABIC -> "لم تتم مشاركة أي لحظات بعد"
        AppLanguage.SPANISH -> "No hay momentos compartidos"
        AppLanguage.FRENCH -> "Aucun moment partagé"
        AppLanguage.GERMAN -> "Noch keine Momente geteilt"
        AppLanguage.RUSSIAN -> "Нет опубликованных моментов"
        AppLanguage.PORTUGUESE -> "Nenhum momento compartilhado"
        AppLanguage.INDONESIAN -> "Belum ada momen dibagikan"
        else -> "No moments shared yet"
    }

    fun partnerProfileNoMomentsDesc(lang: AppLanguage, name: String): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "$name 还没有分享过照片或动态。"
        AppLanguage.JAPANESE -> "$name はまだ写真や投稿をシェアしていません。"
        AppLanguage.KOREAN -> "$name 님이 아직 사진이나 이야기를 게시하지 않았습니다."
        AppLanguage.ARABIC -> "لم يشارك $name أي صور أو قصص بعد."
        AppLanguage.SPANISH -> "$name aún no ha compartido fotos ni historias."
        AppLanguage.FRENCH -> "$name n'a pas encore partagé de photos ou d'histoires."
        AppLanguage.GERMAN -> "$name hat noch keine Fotos oder Geschichten geteilt."
        AppLanguage.RUSSIAN -> "$name еще не опубликовал(а) фотографии или истории."
        AppLanguage.PORTUGUESE -> "$name ainda não compartilhou fotos ou histórias."
        AppLanguage.INDONESIAN -> "$name belum membagikan foto atau cerita terbaru."
        else -> "$name hasn't shared any photos or stories yet."
    }

    fun partnerProfileContinueChat(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "继续聊天"
        AppLanguage.JAPANESE -> "チャットを続ける"
        AppLanguage.KOREAN -> "대화 이어가기"
        AppLanguage.ARABIC -> "متابعة الدردشة"
        AppLanguage.SPANISH -> "Continuar chat"
        AppLanguage.FRENCH -> "Continuer"
        AppLanguage.GERMAN -> "Chat fortsetzen"
        AppLanguage.RUSSIAN -> "Продолжить чат"
        AppLanguage.PORTUGUESE -> "Continuar conversa"
        AppLanguage.INDONESIAN -> "Lanjut Obrolan"
        else -> "Continue Chat"
    }

    fun partnerProfileGreetingText(lang: AppLanguage, name: String): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "你好 $name！很高兴认识你 👋"
        AppLanguage.JAPANESE -> "こんにちは $name さん！よろしくお願いします 👋"
        AppLanguage.KOREAN -> "안녕하세요 $name 님! 반갑습니다 👋"
        AppLanguage.ARABIC -> "مرحباً $name! سعيد بمعرفتك 👋"
        AppLanguage.SPANISH -> "¡Hola $name! Mucho gusto en saludarte 👋"
        AppLanguage.FRENCH -> "Salut $name ! Ravi de te rencontrer 👋"
        AppLanguage.GERMAN -> "Hallo $name! Schön dich kennenzulernen 👋"
        AppLanguage.RUSSIAN -> "Привет, $name! Приятно познакомиться 👋"
        AppLanguage.PORTUGUESE -> "Olá $name! Prazer em te conhecer 👋"
        AppLanguage.INDONESIAN -> "Halo $name! Salam kenal ya 👋 Senang bisa terhubung."
        else -> "Hello $name! Nice to meet you 👋 Glad to connect."
    }

    fun partnerProfileSendGreeting(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "发送问候"
        AppLanguage.JAPANESE -> "挨拶を送る"
        AppLanguage.KOREAN -> "인사 보내기"
        AppLanguage.ARABIC -> "إرسال تحية"
        AppLanguage.SPANISH -> "Enviar saludo"
        AppLanguage.FRENCH -> "Envoyer un message"
        AppLanguage.GERMAN -> "Gruß senden"
        AppLanguage.RUSSIAN -> "Отправить привет"
        AppLanguage.PORTUGUESE -> "Enviar saudação"
        AppLanguage.INDONESIAN -> "Kirim Salam"
        else -> "Send Greeting"
    }

    fun partnerProfileUnblockUser(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "解除用户拉黑"
        AppLanguage.JAPANESE -> "ブロックを解除する"
        AppLanguage.KOREAN -> "사용자 차단 해제"
        AppLanguage.ARABIC -> "إلغاء حظر المستخدم"
        AppLanguage.SPANISH -> "Desbloquear usuario"
        AppLanguage.FRENCH -> "Débloquer l'utilisateur"
        AppLanguage.GERMAN -> "Benutzer freigeben"
        AppLanguage.RUSSIAN -> "Разблокировать пользователя"
        AppLanguage.PORTUGUESE -> "Desbloquear usuário"
        AppLanguage.INDONESIAN -> "Buka Blokir Pengguna"
        else -> "Unblock User"
    }

    fun partnerProfileBlockUser(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "拉黑此用户"
        AppLanguage.JAPANESE -> "このユーザーをブロック"
        AppLanguage.KOREAN -> "이 사용자 차단하기"
        AppLanguage.ARABIC -> "حظر هذا المستخدم"
        AppLanguage.SPANISH -> "Bloquear a este usuario"
        AppLanguage.FRENCH -> "Bloquer cet utilisateur"
        AppLanguage.GERMAN -> "Diesen Benutzer blockieren"
        AppLanguage.RUSSIAN -> "Заблокировать пользователя"
        AppLanguage.PORTUGUESE -> "Bloquear este usuário"
        AppLanguage.INDONESIAN -> "Blokir Pengguna"
        else -> "Block User"
    }

    fun partnerProfileReportUser(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "举报此用户"
        AppLanguage.JAPANESE -> "このユーザーを通報"
        AppLanguage.KOREAN -> "이 사용자 신고하기"
        AppLanguage.ARABIC -> "إبلاغ عن هذا المستخدم"
        AppLanguage.SPANISH -> "Denunciar a este usuario"
        AppLanguage.FRENCH -> "Signaler cet utilisateur"
        AppLanguage.GERMAN -> "Benutzer melden"
        AppLanguage.RUSSIAN -> "Пожаловаться на пользователя"
        AppLanguage.PORTUGUESE -> "Denunciar este usuário"
        AppLanguage.INDONESIAN -> "Laporkan Pengguna"
        else -> "Report User"
    }

    fun partnerProfileBlockConfirmTitle(lang: AppLanguage, name: String): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "拉黑 $name？"
        AppLanguage.JAPANESE -> "「$name」をブロックしますか？"
        AppLanguage.KOREAN -> "$name 님을 차단하시겠습니까?"
        AppLanguage.ARABIC -> "حظر $name؟"
        AppLanguage.SPANISH -> "¿Bloquear a $name?"
        AppLanguage.FRENCH -> "Bloquer $name ?"
        AppLanguage.GERMAN -> "$name blockieren?"
        AppLanguage.RUSSIAN -> "Заблокировать $name?"
        AppLanguage.PORTUGUESE -> "Bloquear $name?"
        AppLanguage.INDONESIAN -> "Blokir $name?"
        else -> "Block $name?"
    }

    fun partnerProfileBlockConfirmDesc(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "拉黑后，对方将无法向您发送任何消息。"
        AppLanguage.JAPANESE -> "ブロックすると、このユーザーからメッセージを受信できなくなります。"
        AppLanguage.KOREAN -> "차단하면 상대방이 나에게 더 이상 메시지를 보낼 수 없습니다."
        AppLanguage.ARABIC -> "بعد الحظر، لن يتمكن هذا المستخدم من مراسلتك مجدداً."
        AppLanguage.SPANISH -> "Este usuario ya no podrá enviarte mensajes."
        AppLanguage.FRENCH -> "Cet utilisateur ne pourra plus vous envoyer de messages."
        AppLanguage.GERMAN -> "Dieser Benutzer kann dir keine Nachrichten mehr senden."
        AppLanguage.RUSSIAN -> "Этот пользователь больше не сможет отправлять вам сообщения."
        AppLanguage.PORTUGUESE -> "Este usuário não poderá mais enviar mensagens para você."
        AppLanguage.INDONESIAN -> "Pengguna ini tidak akan dapat mengirim pesan kepada Anda lagi."
        else -> "This user will no longer be able to send you messages."
    }

    fun btnBlock(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "拉黑"
        AppLanguage.JAPANESE -> "ブロック"
        AppLanguage.KOREAN -> "차단"
        AppLanguage.ARABIC -> "حظر"
        AppLanguage.SPANISH -> "Bloquear"
        AppLanguage.FRENCH -> "Bloquer"
        AppLanguage.GERMAN -> "Blockieren"
        AppLanguage.RUSSIAN -> "Заблокировать"
        AppLanguage.PORTUGUESE -> "Bloquear"
        AppLanguage.INDONESIAN -> "Blokir"
        else -> "Block"
    }

    fun btnDelete(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "删除"
        AppLanguage.JAPANESE -> "削除"
        AppLanguage.KOREAN -> "삭제"
        AppLanguage.ARABIC -> "حذف"
        AppLanguage.SPANISH -> "Eliminar"
        AppLanguage.FRENCH -> "Supprimer"
        AppLanguage.GERMAN -> "Löschen"
        AppLanguage.RUSSIAN -> "Удалить"
        AppLanguage.PORTUGUESE -> "Excluir"
        AppLanguage.INDONESIAN -> "Hapus"
        else -> "Delete"
    }

    fun partnerProfilePhotoTitle(lang: AppLanguage, name: String): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "$name 的头像"
        AppLanguage.JAPANESE -> "$name のプロフィール写真"
        AppLanguage.KOREAN -> "$name 님의 프로필 사진"
        AppLanguage.ARABIC -> "صورة ملف $name"
        AppLanguage.SPANISH -> "Foto de perfil de $name"
        AppLanguage.FRENCH -> "Photo de profil de $name"
        AppLanguage.GERMAN -> "Profilbild von $name"
        AppLanguage.RUSSIAN -> "Фото профиля $name"
        AppLanguage.PORTUGUESE -> "Foto de perfil de $name"
        AppLanguage.INDONESIAN -> "Foto Profil $name"
        else -> "$name's Profile Photo"
    }

    fun commonZoomPhoto(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "放大照片"
        AppLanguage.JAPANESE -> "写真を拡大"
        AppLanguage.KOREAN -> "사진 확대"
        AppLanguage.ARABIC -> "تكبير الصورة"
        AppLanguage.SPANISH -> "Ampliar foto"
        AppLanguage.FRENCH -> "Agrandir la photo"
        AppLanguage.GERMAN -> "Foto vergrößern"
        AppLanguage.RUSSIAN -> "Увеличить фото"
        AppLanguage.PORTUGUESE -> "Ampliar foto"
        AppLanguage.INDONESIAN -> "Perbesar Foto"
        else -> "Zoom Photo"
    }

    fun momentsLike(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "赞"
        AppLanguage.JAPANESE -> "いいね"
        AppLanguage.KOREAN -> "좋아요"
        AppLanguage.ARABIC -> "إعجاب"
        AppLanguage.SPANISH -> "Me gusta"
        AppLanguage.FRENCH -> "J'aime"
        AppLanguage.GERMAN -> "Gefällt mir"
        AppLanguage.RUSSIAN -> "Нравится"
        AppLanguage.PORTUGUESE -> "Curtir"
        AppLanguage.INDONESIAN -> "Suka"
        else -> "Like"
    }

    fun momentsComment(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "评论"
        AppLanguage.JAPANESE -> "コメント"
        AppLanguage.KOREAN -> "댓글"
        AppLanguage.ARABIC -> "تعليق"
        AppLanguage.SPANISH -> "Comentario"
        AppLanguage.FRENCH -> "Commentaire"
        AppLanguage.GERMAN -> "Kommentar"
        AppLanguage.RUSSIAN -> "Комментарий"
        AppLanguage.PORTUGUESE -> "Comentário"
        AppLanguage.INDONESIAN -> "Komentar"
        else -> "Comment"
    }

    fun partnerMomentLikesCount(lang: AppLanguage, count: Int): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "$count 次赞"
        AppLanguage.JAPANESE -> "$count いいね"
        AppLanguage.KOREAN -> "$count 좋아요"
        AppLanguage.ARABIC -> "$count إعجاب"
        AppLanguage.SPANISH -> "$count Me gusta"
        AppLanguage.FRENCH -> "$count j'aime"
        AppLanguage.GERMAN -> "$count Gefällt mir"
        AppLanguage.RUSSIAN -> "$count отметок «Нравится»"
        AppLanguage.PORTUGUESE -> "$count curtidas"
        AppLanguage.INDONESIAN -> "$count Suka"
        else -> "$count Likes"
    }

    fun partnerMomentCommentsCount(lang: AppLanguage, count: Int): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "$count 条评论"
        AppLanguage.JAPANESE -> "$count 件のコメント"
        AppLanguage.KOREAN -> "$count 개의 댓글"
        AppLanguage.ARABIC -> "$count تعليق"
        AppLanguage.SPANISH -> "$count comentarios"
        AppLanguage.FRENCH -> "$count commentaires"
        AppLanguage.GERMAN -> "$count Kommentare"
        AppLanguage.RUSSIAN -> "$count комментариев"
        AppLanguage.PORTUGUESE -> "$count comentários"
        AppLanguage.INDONESIAN -> "$count Komentar"
        else -> "$count Comments"
    }

    fun chatPhotoPreviewTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "照片预览"
        AppLanguage.JAPANESE -> "写真プレビュー"
        AppLanguage.KOREAN -> "사진 미리보기"
        AppLanguage.ARABIC -> "معاينة الصورة"
        AppLanguage.SPANISH -> "Vista previa de la foto"
        AppLanguage.FRENCH -> "Aperçu de la photo"
        AppLanguage.GERMAN -> "Fotovorschau"
        AppLanguage.RUSSIAN -> "Просмотр фото"
        AppLanguage.PORTUGUESE -> "Prévia da foto"
        AppLanguage.INDONESIAN -> "Pratinjau Foto"
        else -> "Photo Preview"
    }

    fun qrScanPrompt(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "扫描二维码添加好友"
        AppLanguage.JAPANESE -> "QRコードをスキャンして友達追加"
        AppLanguage.KOREAN -> "QR 코드를 스캔하여 친구 추가"
        AppLanguage.ARABIC -> "امسح رمز الاستجابة السريعة لإضافة صديق"
        AppLanguage.SPANISH -> "Escanear código QR para agregar amigo"
        AppLanguage.FRENCH -> "Scanner le code QR"
        AppLanguage.GERMAN -> "QR-Code scannen"
        AppLanguage.RUSSIAN -> "Сканировать QR-код"
        AppLanguage.PORTUGUESE -> "Escanear código QR"
        AppLanguage.INDONESIAN -> "Pindai Kode QR / Barcode ID"
        else -> "Scan QR Code / Barcode ID"
    }

    // --- FRIENDS TAB HELPERS & ALIASES ---
    fun friendsNewFriends(lang: AppLanguage): String = friendsNewFriendsTitle(lang)
    fun friendsNearby(lang: AppLanguage): String = friendsFindNearby(lang)
    fun friendsFindNearbyBtn(lang: AppLanguage): String = friendsFindNearby(lang)
    fun friendsDeleteFriendDialogTitle(lang: AppLanguage): String = friendsDeleteTitle(lang)
    fun friendsDeleteFriendDialogMessage(lang: AppLanguage, name: String): String = friendsDeleteDesc(lang, name)
    fun friendsClearAllDialogTitle(lang: AppLanguage): String = friendsClearAllTitle(lang)
    fun friendsClearAllDialogMessage(lang: AppLanguage): String = friendsClearAllDesc(lang)

    fun friendsRemoveFavorite(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "取消特别关注"
        AppLanguage.JAPANESE -> "お気に入りから解除"
        AppLanguage.KOREAN -> "즐겨찾기 해제"
        AppLanguage.ARABIC -> "إزالة من المفضلة"
        AppLanguage.SPANISH -> "Quitar de favoritos"
        AppLanguage.FRENCH -> "Retirer des favoris"
        AppLanguage.GERMAN -> "Aus Favoriten entfernen"
        AppLanguage.RUSSIAN -> "Удалить из избранного"
        AppLanguage.PORTUGUESE -> "Remover dos favoritos"
        AppLanguage.INDONESIAN -> "Hapus dari Favorit"
        else -> "Remove from Favorites"
    }

    fun friendsAddFavorite(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "设为特别关注"
        AppLanguage.JAPANESE -> "お気に入りに追加"
        AppLanguage.KOREAN -> "즐겨찾기에 추가"
        AppLanguage.ARABIC -> "إضافة إلى المفضلة"
        AppLanguage.SPANISH -> "Agregar a favoritos"
        AppLanguage.FRENCH -> "Ajouter aux favoris"
        AppLanguage.GERMAN -> "Zu Favoriten hinzufügen"
        AppLanguage.RUSSIAN -> "Добавить в избранное"
        AppLanguage.PORTUGUESE -> "Adicionar aos favoritos"
        AppLanguage.INDONESIAN -> "Tambah ke Favorit"
        else -> "Add to Favorites"
    }

    // --- MOMENTS SCREEN HELPERS ---
    fun reportTypeMoment(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "动态"
        AppLanguage.JAPANESE -> "モーメント"
        AppLanguage.KOREAN -> "모먼트"
        AppLanguage.ARABIC -> "لحظة"
        AppLanguage.SPANISH -> "Momento"
        AppLanguage.FRENCH -> "Moment"
        AppLanguage.GERMAN -> "Moment"
        AppLanguage.RUSSIAN -> "Момент"
        AppLanguage.PORTUGUESE -> "Momento"
        AppLanguage.INDONESIAN -> "Momen"
        else -> "Moment"
    }

    fun sheetLike(lang: AppLanguage): String = momentsLike(lang)
    fun sheetComments(lang: AppLanguage): String = momentsComment(lang)
    fun sheetShare(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "分享"
        AppLanguage.JAPANESE -> "シェア"
        AppLanguage.KOREAN -> "공유"
        AppLanguage.ARABIC -> "مشاركة"
        AppLanguage.SPANISH -> "Compartir"
        AppLanguage.FRENCH -> "Partager"
        AppLanguage.GERMAN -> "Teilen"
        AppLanguage.RUSSIAN -> "Поделиться"
        AppLanguage.PORTUGUESE -> "Compartilhar"
        AppLanguage.INDONESIAN -> "Bagikan"
        else -> "Share"
    }

    // --- NEW FRIENDS SCREEN ALIASES ---
    fun newFriendsTitle(lang: AppLanguage): String = friendsNewFriendsTitle(lang)
    fun newFriendsInfoBanner(lang: AppLanguage): String = newFriendsBannerDesc(lang)
    fun newFriendsFindNearbyBtn(lang: AppLanguage): String = friendsFindNearby(lang)
    fun newFriendsSimulateBtn(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "模拟新消息"
        AppLanguage.JAPANESE -> "着信シミュレーション"
        AppLanguage.KOREAN -> "메시지 시뮬레이션"
        AppLanguage.ARABIC -> "محاكاة رسالة جديدة"
        AppLanguage.SPANISH -> "Simular mensaje"
        AppLanguage.FRENCH -> "Simuler un message"
        AppLanguage.GERMAN -> "Nachricht simulieren"
        AppLanguage.RUSSIAN -> "Симуляция сообщения"
        AppLanguage.PORTUGUESE -> "Simular mensagem"
        AppLanguage.INDONESIAN -> "Simulasi Chat Masuk"
        else -> "Simulate Incoming Chat"
    }
    fun newFriendsIgnore(lang: AppLanguage): String = newFriendsBtnIgnore(lang)
    fun newFriendsReplyChat(lang: AppLanguage): String = newFriendsBtnReply(lang)
    fun newFriendsAccept(lang: AppLanguage): String = newFriendsBtnAccept(lang)

    // --- GOOGLE SIGN IN EXCLUSIVE SCREEN STRINGS ---
    fun googleSignInTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "使用 Google 账号登录"
        AppLanguage.JAPANESE -> "Google アカウントでログイン"
        AppLanguage.KOREAN -> "Google 계정으로 로그인"
        AppLanguage.ARABIC -> "تسجيل الدخول بحساب Google"
        AppLanguage.SPANISH -> "Iniciar sesión con Google"
        AppLanguage.FRENCH -> "Se connecter avec Google"
        AppLanguage.GERMAN -> "Mit Google anmelden"
        AppLanguage.RUSSIAN -> "Войти через Google"
        AppLanguage.PORTUGUESE -> "Entrar com o Google"
        AppLanguage.INDONESIAN -> "Masuk dengan Akun Google"
        else -> "Sign in with Google Account"
    }

    fun googleSignInSubtitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "使用永久账号畅享即时聊天、附近好友和精彩动态。"
        AppLanguage.JAPANESE -> "永久アカウントで、チャットや近くの友達、モーメントを安全に保存。"
        AppLanguage.KOREAN -> "영구 계정으로 채팅, 주변 친구, 소중한 모먼트를 안전하게 관리하세요."
        AppLanguage.ARABIC -> "حساب دائم لجميع محادثاتك وأصدقائك القريبين ولحظاتك المميزة."
        AppLanguage.SPANISH -> "Una cuenta permanente para todos tus chats, amigos cercanos y momentos."
        AppLanguage.FRENCH -> "Un compte permanent pour toutes vos discussions, amis à proximité et moments."
        AppLanguage.GERMAN -> "Ein dauerhafter Account für alle Ihre Chats, Freunde in der Nähe und Momente."
        AppLanguage.RUSSIAN -> "Постоянный аккаунт для всех чатов, друзей поблизости и моментов."
        AppLanguage.PORTUGUESE -> "Uma conta permanente para todos os seus bate-papos, amigos e momentos."
        AppLanguage.INDONESIAN -> "Satu akun permanen untuk obrolan, teman sekitar, dan momen berharga Anda."
        else -> "One permanent account for all your chats, nearby friends, and moments."
    }

    fun googleSignInButton(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "继续使用 Google 账号"
        AppLanguage.JAPANESE -> "Google で続行"
        AppLanguage.KOREAN -> "Google로 계속하기"
        AppLanguage.ARABIC -> "المتابعة باستخدام Google"
        AppLanguage.SPANISH -> "Continuar con Google"
        AppLanguage.FRENCH -> "Continuer avec Google"
        AppLanguage.GERMAN -> "Mit Google fortfahren"
        AppLanguage.RUSSIAN -> "Продолжить с Google"
        AppLanguage.PORTUGUESE -> "Continuar com o Google"
        AppLanguage.INDONESIAN -> "Lanjutkan dengan Google"
        else -> "Continue with Google"
    }

    fun googleSignInBenefit1(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "永久 ID 与动态保存"
        AppLanguage.JAPANESE -> "永久 ID と投稿の保存"
        AppLanguage.KOREAN -> "영구 ID 및 모먼트 보존"
        AppLanguage.ARABIC -> "معرّف دائم وحفظ اللحظات"
        AppLanguage.SPANISH -> "ID permanente y momentos seguros"
        AppLanguage.FRENCH -> "ID permanent et moments conservés"
        AppLanguage.GERMAN -> "Dauerhafte ID & sichere Momente"
        AppLanguage.RUSSIAN -> "Постоянный ID и сохранение данных"
        AppLanguage.PORTUGUESE -> "ID permanente e momentos salvos"
        AppLanguage.INDONESIAN -> "ID Akun & Momen Permanen"
        else -> "Permanent ID & Moments"
    }

    fun googleSignInBenefit1Desc(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "您的动态、照片与聊天记录在重新登录时永不丢失。"
        AppLanguage.JAPANESE -> "再ログイン時も投稿や写真、チャットが消えることはありません。"
        AppLanguage.KOREAN -> "다시 로그인해도 모먼트, 사진, 대화 내용이 사라지지 않습니다."
        AppLanguage.ARABIC -> "لن تضيع لحظاتك أو صورك أو محادثاتك عند تسجيل الدخول مرة أخرى."
        AppLanguage.SPANISH -> "Tus momentos, fotos y chats permanecen seguros al volver a entrar."
        AppLanguage.FRENCH -> "Vos moments, photos et discussions restent intacts à chaque reconnexion."
        AppLanguage.GERMAN -> "Ihre Momente, Fotos und Chats gehen beim erneuten Anmelden nicht verloren."
        AppLanguage.RUSSIAN -> "Ваши моменты, фото и чаты надежно сохраняются при повторном входе."
        AppLanguage.PORTUGUESE -> "Seus momentos, fotos e conversas não desaparecem ao entrar novamente."
        AppLanguage.INDONESIAN -> "Momen, foto, dan cerita Anda tersimpan aman dan tidak akan hilang saat login ulang."
        else -> "Your moments, photos, and stories are securely preserved and won't vanish when re-logging."
    }

    fun googleSignInBenefit2(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "一键即开，无需记密码"
        AppLanguage.JAPANESE -> "パスワード不要のワンタップログイン"
        AppLanguage.KOREAN -> "비밀번호 없는 원클릭 간편 로그인"
        AppLanguage.ARABIC -> "تسجيل دخول فوري بنقرة واحدة"
        AppLanguage.SPANISH -> "Acceso en 1 clic sin contraseñas"
        AppLanguage.FRENCH -> "Connexion en 1 clic sans mot de passe"
        AppLanguage.GERMAN -> "1-Klick-Anmeldung ohne Passwort"
        AppLanguage.RUSSIAN -> "Вход в 1 клик без паролей"
        AppLanguage.PORTUGUESE -> "Login em 1 clique sem senhas"
        AppLanguage.INDONESIAN -> "Aman & Bebas Lupa Sandi"
        else -> "1-Click Password-Free Login"
    }

    fun googleSignInBenefit2Desc(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "受 Google Identity 安全保护，无需繁琐记忆密码。"
        AppLanguage.JAPANESE -> "Google の高水準セキュリティで、パスワードを忘れる心配がありません。"
        AppLanguage.KOREAN -> "Google Identity의 검증된 보안으로 비밀번호 분실 걱정 없이 안전합니다."
        AppLanguage.ARABIC -> "محمي بنظام أمان Google، دون الحاجة لحفظ أو إعادة تعيين كلمات المرور."
        AppLanguage.SPANISH -> "Protegido por Google Identity, sin el estrés de olvidar contraseñas."
        AppLanguage.FRENCH -> "Sécurisé par Google Identity, fini les oublis de mot de passe."
        AppLanguage.GERMAN -> "Geschützt durch Google Identity, kein lästiges Merken von Passwörtern."
        AppLanguage.RUSSIAN -> "Защищено Google Identity — забудьте о восстановлении паролей."
        AppLanguage.PORTUGUESE -> "Protegido pelo Google Identity, sem a dor de cabeça de esquecer senhas."
        AppLanguage.INDONESIAN -> "Masuk instan terlindungi oleh sistem Google Identity, bebas repot lupa sandi."
        else -> "Instant login protected by Google Identity without password hassles."
    }

    fun googleSignInBenefit3(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "即时头像与个人资料"
        AppLanguage.JAPANESE -> "プロフィールの自動設定"
        AppLanguage.KOREAN -> "프로필 자동 연결"
        AppLanguage.ARABIC -> "ملف شخصي جاهز فوراً"
        AppLanguage.SPANISH -> "Perfil listo al instante"
        AppLanguage.FRENCH -> "Profil prêt instantanément"
        AppLanguage.GERMAN -> "Profil sofort einsatzbereit"
        AppLanguage.RUSSIAN -> "Мгновенная настройка профиля"
        AppLanguage.PORTUGUESE -> "Perfil pronto instantaneamente"
        AppLanguage.INDONESIAN -> "Profil Otomatis Terhubung"
        else -> "Instant Profile Connection"
    }

    fun googleSignInBenefit3Desc(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "名称与头像立即可用，轻松结识新朋友。"
        AppLanguage.JAPANESE -> "お名前やアイコンがそのまま使え、すぐに友達作りをはじめられます。"
        AppLanguage.KOREAN -> "이름과 프로필 사진이 바로 연동되어 즉시 소통을 시작할 수 있습니다."
        AppLanguage.ARABIC -> "اسمك وصورتك الشخصية جاهزان مباشرة لبدء التعرف على أصدقاء جدد."
        AppLanguage.SPANISH -> "Tu nombre y foto de avatar listos para chatear de inmediato."
        AppLanguage.FRENCH -> "Votre nom et votre avatar sont immédiatement prêts pour échanger."
        AppLanguage.GERMAN -> "Ihr Name und Avatar sind sofort startklar für neue Bekanntschaften."
        AppLanguage.RUSSIAN -> "Ваше имя и аватар сразу готовы к общению с новыми людьми."
        AppLanguage.PORTUGUESE -> "Seu nome e foto prontos para começar a conversar na hora."
        AppLanguage.INDONESIAN -> "Nama dan foto profil Google langsung siap untuk menyapa teman-teman baru."
        else -> "Your Google name and avatar are immediately ready to meet new friends."
    }

    fun googleSignInTrustBadge(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "Google 官方安全验证与隐私保护"
        AppLanguage.JAPANESE -> "Google 公式認証による高い安全性とプライバシー"
        AppLanguage.KOREAN -> "Google 공식 인증 및 개인정보 보호"
        AppLanguage.ARABIC -> "حماية وأمان موثوق به من Google"
        AppLanguage.SPANISH -> "Autenticación segura y protegida por Google"
        AppLanguage.FRENCH -> "Authentification sécurisée certifiée par Google"
        AppLanguage.GERMAN -> "Sichere Authentifizierung durch Google"
        AppLanguage.RUSSIAN -> "Безопасная аутентификация через сервисы Google"
        AppLanguage.PORTUGUESE -> "Autenticação segura protegida pelo Google"
        AppLanguage.INDONESIAN -> "Autentikasi resmi & aman terlindungi Google Identity"
        else -> "Official secure authentication protected by Google Identity"
    }

    // Privacy Policy & Terms ModalBottomSheet Strings
    fun privacyPolicyDialogTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "隐私政策与条款"
        AppLanguage.JAPANESE -> "プライバシーポリシー & 利用規約"
        AppLanguage.KOREAN -> "개인정보 처리방침 & 이용약관"
        AppLanguage.ARABIC -> "سياسة الخصوصية والشروط"
        AppLanguage.SPANISH -> "Política de Privacidad y Términos"
        AppLanguage.FRENCH -> "Politique de confidentialité & Conditions"
        AppLanguage.GERMAN -> "Datenschutzerklärung & Bedingungen"
        AppLanguage.RUSSIAN -> "Политика конфиденциальности и условия"
        AppLanguage.PORTUGUESE -> "Política de Privacidade e Termos"
        AppLanguage.INDONESIAN -> "Kebijakan Privasi & Ketentuan"
        else -> "Privacy Policy & Terms"
    }

    fun privacyPolicyDialogIntro(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "Lovy Chat 致力于严格遵循 Google Play 开发者政策标准，保护用户数据隐私与安全。"
        AppLanguage.JAPANESE -> "Lovy Chat は、Google Play デベロッパー ポリシーの基準に準拠し、ユーザーのデータ プライバシーとセキュリティの保護に取り組んでいます。"
        AppLanguage.KOREAN -> "Lovy Chat은 Google Play 개발자 프로그램 정책 표준에 따라 사용자 개인정보 보호 및 데이터 보안을 준수합니다."
        AppLanguage.ARABIC -> "يلتزم Lovy Chat بحماية خصوصية بيانات المستخدمين وأمانهم وفقاً لمعايير سياسة مطوري Google Play."
        AppLanguage.SPANISH -> "Lovy Chat se compromete a proteger la privacidad y seguridad de los datos según las políticas para desarrolladores de Google Play."
        AppLanguage.FRENCH -> "Lovy Chat s'engage à protéger la confidentialité et la sécurité des données selon les règles du programme pour les développeurs Google Play."
        AppLanguage.GERMAN -> "Lovy Chat verpflichtet sich zum Schutz der Datenprivatsphäre und -sicherheit gemäß den Google Play-Entwicklerrichtlinien."
        AppLanguage.RUSSIAN -> "Lovy Chat соблюдает стандарты политики Google Play для разработчиков по защите данных и безопасности."
        AppLanguage.PORTUGUESE -> "O Lovy Chat tem o compromisso de proteger a privacidade e segurança dos dados conforme as políticas do desenvolvedor do Google Play."
        AppLanguage.INDONESIAN -> "Lovy Chat berkomitmen melindungi privasi data dan keamanan pengguna sesuai standar Google Play Developer Policy."
        else -> "Lovy Chat is committed to protecting user data privacy and security in accordance with Google Play Developer Policy standards."
    }

    fun privacyPolicySec1Title(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "📍 1. 位置权限 (GPS)"
        AppLanguage.JAPANESE -> "📍 1. 位置情報の権限 (GPS)"
        AppLanguage.KOREAN -> "📍 1. 위치 권한 (GPS)"
        AppLanguage.ARABIC -> "📍 1. إذن الموقع (GPS)"
        AppLanguage.SPANISH -> "📍 1. Permiso de ubicación (GPS)"
        AppLanguage.FRENCH -> "📍 1. Autorisation de localisation (GPS)"
        AppLanguage.GERMAN -> "📍 1. Standortberechtigung (GPS)"
        AppLanguage.RUSSIAN -> "📍 1. Доступ к геоданным (GPS)"
        AppLanguage.PORTUGUESE -> "📍 1. Permissão de Localização (GPS)"
        AppLanguage.INDONESIAN -> "📍 1. Penggunaan Izin Lokasi (GPS)"
        else -> "📍 1. Location Permission (GPS)"
    }

    fun privacyPolicySec1Content(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "• 仅在应用程序前台打开时访问位置。\n• 仅用于“附近用户”（好友雷达）功能。\n• 您可以随时在“隐私与位置”菜单中开启隐身模式或隐藏精确距离。"
        AppLanguage.JAPANESE -> "• 位置情報はアプリが起動中で前面にある時のみアクセスされます。\n• 「周辺のユーザー」（友達レーダー）機能のためだけに使用されます。\n• いつでも「プライバシーと位置」設定でゴーストモードの有効化や正確な距離の非表示が可能です。"
        AppLanguage.KOREAN -> "• 앱이 포그라운드에서 실행 중일 때만 위치에 접근합니다.\n• 오직 '주변 사용자'(친구 레이더) 기능을 위해서만 사용됩니다.\n• 언제든지 '개인정보 및 위치' 메뉴에서 고스트 모드를 켜거나 정확한 거리를 숨길 수 있습니다."
        AppLanguage.ARABIC -> "• يتم الوصول للموقع فقط أثناء استخدام التطبيق في المقدمة.\n• يُستخدم حصرياً لميزة 'المستخدمون القريبون' (رادار الأصدقاء).\n• يمكنك تفعيل وضع التخفي أو إخفاء المسافة الدقيقة في أي وقت من إعدادات الخصوصية والموقع."
        AppLanguage.SPANISH -> "• Solo se accede a la ubicación cuando la aplicación está abierta (primer plano).\n• Se utiliza exclusivamente para la función 'Usuarios Cercanos' (Radar de amigos).\n• Puedes activar el Modo Fantasma u ocultar la distancia exacta en Privacidad y Ubicación."
        AppLanguage.FRENCH -> "• La localisation n'est consultée que lorsque l'application est active (premier plan).\n• Utilisée uniquement pour la fonction 'Utilisateurs proches' (Radar d'amis).\n• Vous pouvez activer le Mode Furtif ou masquer la distance exacte dans Confidentialité et localisation."
        AppLanguage.GERMAN -> "• Standort wird nur abgefragt, wenn die App aktiv im Vordergrund läuft.\n• Wird ausschließlich für 'Benutzer in der Nähe' (Freunde-Radar) verwendet.\n• Sie können jederzeit den Geistermodus aktivieren oder die genaue Entfernung in den Datenschutz-Einstellungen verbergen."
        AppLanguage.RUSSIAN -> "• Геолокация используется только при активном приложении (на переднем плане).\n• Используется исключительно для функции «Люди рядом» (Радар друзей).\n• Вы можете включить режим невидимки или скрыть точное расстояние в настройках приватности."
        AppLanguage.PORTUGUESE -> "• A localização só é acessada quando o app estiver em primeiro plano.\n• Usada apenas para o recurso 'Usuários Próximos' (Radar de Amigos).\n• Você pode ativar o Modo Fantasma ou ocultar a distância exata em Privacidade e Localização."
        AppLanguage.INDONESIAN -> "• Lokasi hanya diakses saat aplikasi sedang aktif dibuka (Foreground).\n• Digunakan semata-mata untuk fitur 'Pengguna Sekitar' (Radar Teman).\n• Anda dapat mengaktifkan Mode Penyamaran atau menyembunyikan jarak persis kapan saja di menu Privasi & Lokasi."
        else -> "• Location is accessed only while the app is actively in use (Foreground).\n• Used solely for the 'Nearby Users' feature (Friends Radar).\n• You can enable Ghost Mode or hide exact distance anytime in Privacy & Location settings."
    }

    fun privacyPolicySec2Title(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "📷 2. 相机权限"
        AppLanguage.JAPANESE -> "📷 2. カメラの権限"
        AppLanguage.KOREAN -> "📷 2. 카메라 권한"
        AppLanguage.ARABIC -> "📷 2. إذن الكاميرا"
        AppLanguage.SPANISH -> "📷 2. Permiso de cámara"
        AppLanguage.FRENCH -> "📷 2. Autorisation de l'appareil photo"
        AppLanguage.GERMAN -> "📷 2. Kamerazugriff"
        AppLanguage.RUSSIAN -> "📷 2. Доступ к камере"
        AppLanguage.PORTUGUESE -> "📷 2. Permissão de Câmera"
        AppLanguage.INDONESIAN -> "📷 2. Penggunaan Izin Kamera"
        else -> "📷 2. Camera Permission"
    }

    fun privacyPolicySec2Content(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "• 用于即时扫描好友二维码/条形码。\n• 用于在您选择拍照时直接拍摄头像或聊天图片。\n• 相机绝不在后台运行或录制。"
        AppLanguage.JAPANESE -> "• 友達のQRコード／バーコードの即座な読み取りに使用されます。\n• カメラ撮影を選んだ場合にプロフィールやチャット用写真の直接撮影に使用されます。\n• バックグラウンドでカメラが動作・録画することは一切ありません。"
        AppLanguage.KOREAN -> "• 친구의 QR 코드/바코드를 즉시 스캔하는 데 사용됩니다.\n• 카메라 촬영을 선택한 경우 프로필 또는 채팅 사진을 직접 촬영하는 데 사용됩니다.\n• 카메라는 백그라운드에서 절대 작동하지 않습니다."
        AppLanguage.ARABIC -> "• يُستخدم لمسح رمز الاستجابة السريعة (QR) للأصدقاء فوراً.\n• يُستخدم لالتقاط صورة شخصية أو صور المحادثة مباشرة عند استخدام الكاميرا.\n• لا تسجل الكاميرا أبداً في الخلفية."
        AppLanguage.SPANISH -> "• Se usa para escanear el código QR/código de barras de amigos al instante.\n• Se usa para tomar fotos de perfil o de chat si decides usar la cámara.\n• La cámara nunca graba en segundo plano."
        AppLanguage.FRENCH -> "• Utilisé pour scanner instantanément le code QR de vos amis.\n• Utilisé pour prendre des photos de profil ou de discussion avec l'appareil photo.\n• L'appareil photo n'enregistre jamais en arrière-plan."
        AppLanguage.GERMAN -> "• Wird verwendet, um den QR-Code von Freunden sofort zu scannen.\n• Wird für Profilfotos oder Chat-Aufnahmen bei Kameranutzung verwendet.\n• Die Kamera nimmt niemals im Hintergrund auf."
        AppLanguage.RUSSIAN -> "• Используется для мгновенного сканирования QR-кода друзей.\n• Используется для фото профиля или снимков для чата при съемке.\n• Камера никогда не работает в фоновом режиме."
        AppLanguage.PORTUGUESE -> "• Usada para ler QR Codes de amigos instantaneamente.\n• Usada para tirar fotos de perfil ou mensagens ao escolher a câmera.\n• A câmera nunca grava em segundo plano."
        AppLanguage.INDONESIAN -> "• Digunakan untuk memindai Barcode / QR Code teman secara instan.\n• Digunakan untuk mengambil foto profil atau gambar obrolan secara langsung jika Anda memilih menggunakan kamera.\n• Kamera tidak pernah merekam di latar belakang."
        else -> "• Used to scan friends' Barcode / QR Code instantly.\n• Used to take profile photos or chat pictures directly when choosing the camera.\n• The camera never records in the background."
    }

    fun privacyPolicySec3Title(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "📢 3. 广告服务与广告 ID (AD_ID)"
        AppLanguage.JAPANESE -> "📢 3. 広告配信と広告 ID (AD_ID)"
        AppLanguage.KOREAN -> "📢 3. 광고 서비스 및 광고 ID (AD_ID)"
        AppLanguage.ARABIC -> "📢 3. خدمة الإعلانات ومعرّف الإعلانات (AD_ID)"
        AppLanguage.SPANISH -> "📢 3. Servicio de anuncios e ID de publicidad (AD_ID)"
        AppLanguage.FRENCH -> "📢 3. Services publicitaires & ID publicitaire (AD_ID)"
        AppLanguage.GERMAN -> "📢 3. Werbedienste & Werbe-ID (AD_ID)"
        AppLanguage.RUSSIAN -> "📢 3. Реклама и рекламный идентификатор (AD_ID)"
        AppLanguage.PORTUGUESE -> "📢 3. Serviços de Anúncios e ID de Publicidade (AD_ID)"
        AppLanguage.INDONESIAN -> "📢 3. Layanan Iklan & ID Iklan (AD_ID)"
        else -> "📢 3. Ads & Advertising ID (AD_ID)"
    }

    fun privacyPolicySec3Content(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "• 应用通过 Unity/ironSource SDK 使用 Google Play 广告 ID (AD_ID) 展示横幅广告。\n• 广告数据严格遵循 Google Play 隐私规范进行处理。"
        AppLanguage.JAPANESE -> "• バナー広告の表示のため、Unity/ironSource SDK 経由で Google Play 広告 ID (AD_ID) を使用します。\n• 広告データは Google Play のプライバシー規約に従い安全に管理されます。"
        AppLanguage.KOREAN -> "• 배너 광고 제공을 위해 Unity/ironSource SDK를 통해 Google Play 광고 ID(AD_ID)를 사용합니다.\n• 광고 데이터는 Google Play 개인정보 보호 정책에 따라 관리됩니다."
        AppLanguage.ARABIC -> "• يستخدم التطبيق معرّف إعلانات Google Play (AD_ID) عبر Unity/ironSource لعرض إعلانات البانر.\n• تُدار بيانات الإعلانات وفق سياسات خصوصية Google Play."
        AppLanguage.SPANISH -> "• La app usa el ID de publicidad de Google Play (AD_ID) vía Unity/ironSource para banners publicitarios.\n• Los datos publicitarios se gestionan conforme a las directivas de Google Play."
        AppLanguage.FRENCH -> "• L'application utilise l'identifiant publicitaire Google Play (AD_ID) via Unity/ironSource pour afficher des bannières.\n• Les données sont gérées selon les règles de Google Play."
        AppLanguage.GERMAN -> "• Die App nutzt die Google Play-Werbe-ID (AD_ID) über das Unity/ironSource SDK für Werbebanner.\n• Werbedaten werden konform zu den Google Play-Richtlinien verarbeitet."
        AppLanguage.RUSSIAN -> "• Приложение использует рекламный идентификатор Google Play (AD_ID) через SDK Unity/ironSource для баннеров.\n• Данные обрабатываются в соответствии с правилами Google Play."
        AppLanguage.PORTUGUESE -> "• O aplicativo utiliza o ID de Publicidade do Google Play (AD_ID) via Unity/ironSource para exibir banners.\n• Dados publicitários são geridos conforme as diretrizes do Google Play."
        AppLanguage.INDONESIAN -> "• Aplikasi menggunakan Google Play Advertising ID (AD_ID) melalui SDK Unity/ironSource untuk menayangkan banner iklan.\n• Data periklanan dikelola sesuai pedoman privasi Google Play."
        else -> "• The app uses the Google Play Advertising ID (AD_ID) via Unity/ironSource SDK to deliver ad banners.\n• Advertising data is managed in compliance with Google Play privacy policies."
    }

    fun privacyPolicySec4Title(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "🛡️ 4. 用户内容规范与反骚扰 (UGC)"
        AppLanguage.JAPANESE -> "🛡️ 4. ユーザーコンテンツと保護方針 (UGC)"
        AppLanguage.KOREAN -> "🛡️ 4. 사용자 생성 콘텐츠 및 괴롭힘 방지 (UGC)"
        AppLanguage.ARABIC -> "🛡️ 4. محتوى المستخدمين ومكافحة المضايقة (UGC)"
        AppLanguage.SPANISH -> "🛡️ 4. Contenido de usuarios y seguridad (UGC)"
        AppLanguage.FRENCH -> "🛡️ 4. Contenu utilisateur & Sécurité (UGC)"
        AppLanguage.GERMAN -> "🛡️ 4. Nutzerinhalte & Schutz vor Belästigung (UGC)"
        AppLanguage.RUSSIAN -> "🛡️ 4. Пользовательский контент и безопасность (UGC)"
        AppLanguage.PORTUGUESE -> "🛡️ 4. Conteúdo do Usuário e Segurança (UGC)"
        AppLanguage.INDONESIAN -> "🛡️ 4. Konten Pengguna & Anti-Pelecehan (UGC)"
        else -> "🛡️ 4. User Content & Safety (UGC)"
    }

    fun privacyPolicySec4Content(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "• Lovy Chat 严厉禁止垃圾信息、色情、仇恨言论及骚扰行为。\n• 在每个好友资料、聊天和动态中均提供举报和屏蔽按钮。\n• 违规用户将被严厉处罚并封禁。"
        AppLanguage.JAPANESE -> "• Lovy Chat はスパム、ポルノ、ヘイトスピーチ、嫌がらせ行為を固く禁止しています。\n• すべてのプロフィール、チャット、モーメントに通報・ブロック機能が用意されています。\n• 規約違反者には厳正な対処が行われます。"
        AppLanguage.KOREAN -> "• Lovy Chat은 스팸, 음란물, 증오 표현 및 괴롭힘을 엄격히 금지합니다.\n• 모든 프로필, 채팅, 모먼트에 신고 및 차단 버튼이 지원됩니다.\n• 위반 행위 적발 시 즉각적인 제재가 적용됩니다."
        AppLanguage.ARABIC -> "• يحظر Lovy Chat بشدة جميع أنواع الرسائل غير المرغوبة والمحتوى غير اللائق وخطاب الكراهية والمضايقة.\n• أزرار الإبلاغ والحظر متوفرة في كل ملف ومحادثة ولحظة.\n• سيتم اتخاذ إجراءات رادعة بحق المخالفين."
        AppLanguage.SPANISH -> "• Lovy Chat prohíbe el spam, contenido explícito, odio y acoso.\n• Hay botones de Denunciar y Bloquear en cada perfil, chat y momento.\n• Los infractores serán sancionados rigurosamente."
        AppLanguage.FRENCH -> "• Lovy Chat interdit strictement le spam, le contenu explicite, la haine et le harcèlement.\n• Des boutons Signaler et Bloquer sont disponibles sur chaque profil, chat et moment.\n• Des mesures fermes sont appliquées aux contrevenants."
        AppLanguage.GERMAN -> "• Lovy Chat verbietet Spam, Pornografie, Hassrede und jede Form von Belästigung.\n• Melde- und Blockier-Funktionen sind in Profilen, Chats und Momenten verfügbar.\n• Verstöße führen zu sofortigen Sanktionen."
        AppLanguage.RUSSIAN -> "• Lovy Chat строго запрещает спам, неприемлемый контент, оскорбления и домогательства.\n• Кнопки «Пожаловаться» и «Заблокировать» доступны в каждом профиле, чате и моменте.\n• К нарушителям применяются строгие меры."
        AppLanguage.PORTUGUESE -> "• O Lovy Chat proíbe qualquer tipo de spam, pornografia, ódio e assédio.\n• Botões de Denunciar e Bloquear estão presentes em perfis, chats e momentos.\n• Usuários que violarem as regras serão suspensos."
        AppLanguage.INDONESIAN -> "• Lovy Chat melarang segala bentuk spam, pornografi, ujaran kebencian, dan pelecehan.\n• Disediakan tombol Laporkan dan Blokir pada setiap profil teman, obrolan, dan momen.\n• Pengguna yang melanggar akan ditindak tegas."
        else -> "• Lovy Chat strictly prohibits spam, pornography, hate speech, and harassment.\n• Report and Block buttons are readily available on every user profile, chat, and moment.\n• Violators will be subject to strict disciplinary actions."
    }

    fun privacyPolicySec5Title(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "🗑️ 5. 账号及数据删除权利"
        AppLanguage.JAPANESE -> "🗑️ 5. アカウントとデータの削除権"
        AppLanguage.KOREAN -> "🗑️ 5. 계정 및 데이터 영구 삭제 권리"
        AppLanguage.ARABIC -> "🗑️ 5. حق حذف الحساب والبيانات"
        AppLanguage.SPANISH -> "🗑️ 5. Derecho a eliminar cuenta y datos"
        AppLanguage.FRENCH -> "🗑️ 5. Droit à la suppression du compte & des données"
        AppLanguage.GERMAN -> "🗑️ 5. Recht auf Kontolöschung & Daten"
        AppLanguage.RUSSIAN -> "🗑️ 5. Право на удаление аккаунта и данных"
        AppLanguage.PORTUGUESE -> "🗑️ 5. Direito de Excluir Conta e Dados"
        AppLanguage.INDONESIAN -> "🗑️ 5. Hak Hapus Akun & Data (Account Deletion)"
        else -> "🗑️ 5. Account & Data Deletion Rights"
    }

    fun privacyPolicySec5Content(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "• 您有权随时在“我的”页面通过“永久注销账号”按钮，删除个人账号、所有聊天记录及资料数据。"
        AppLanguage.JAPANESE -> "• プロフィール画面の「アカウントを完全に削除」ボタンから、いつでもアカウント、全チャット履歴、プロフィールデータを削除できます。"
        AppLanguage.KOREAN -> "• 프로필 화면의 '계정 영구 삭제' 버튼을 통해 언제든지 계정, 전체 대화 기록 및 프로필 데이터를 삭제할 수 있습니다."
        AppLanguage.ARABIC -> "• يحق لك حذف حسابك وكافة سجل المحادثات وبيانات الملف الشخصي في أي وقت عبر زر 'حذف الحساب نهائياً' في صفحة الملف الشخصي."
        AppLanguage.SPANISH -> "• Tienes derecho a eliminar tu cuenta, historial de chats y datos de perfil en cualquier momento mediante el botón 'Eliminar cuenta permanentemente' en Perfil."
        AppLanguage.FRENCH -> "• Vous avez le droit de supprimer votre compte, l'ensemble de l'historique et vos données à tout moment via le bouton 'Supprimer définitivement le compte'."
        AppLanguage.GERMAN -> "• Sie haben das Recht, Ihr Konto, alle Chatverläufe und Profildaten jederzeit über die Schaltfläche 'Konto dauerhaft löschen' im Profil zu entfernen."
        AppLanguage.RUSSIAN -> "• Вы имеете право в любой момент удалить свой аккаунт, историю сообщений и профиль с помощью кнопки «Удалить аккаунт навсегда»."
        AppLanguage.PORTUGUESE -> "• Você pode excluir sua conta, histórico de conversas e dados de perfil a qualquer momento pelo botão 'Excluir Conta Permanentemente' no Perfil."
        AppLanguage.INDONESIAN -> "• Anda berhak menghapus akun dan seluruh riwayat obrolan serta data profil kapan saja melalui tombol 'Hapus Akun Permanen' di halaman Profil."
        else -> "• You have the right to delete your account, entire chat history, and profile data at any time via the 'Delete Account Permanently' button in the Profile tab."
    }

    fun privacyPolicyUnderstandButton(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "我知道了"
        AppLanguage.JAPANESE -> "了解しました"
        AppLanguage.KOREAN -> "확인했습니다"
        AppLanguage.ARABIC -> "أنا أفهم"
        AppLanguage.SPANISH -> "Entendido"
        AppLanguage.FRENCH -> "J'ai compris"
        AppLanguage.GERMAN -> "Verstanden"
        AppLanguage.RUSSIAN -> "Понятно"
        AppLanguage.PORTUGUESE -> "Entendi"
        AppLanguage.INDONESIAN -> "Saya Mengerti"
        else -> "I Understand"
    }

    // About Lovy Chat ModalBottomSheet Strings
    fun aboutAppDialogTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "关于 Lovy Chat"
        AppLanguage.JAPANESE -> "Lovy Chat について"
        AppLanguage.KOREAN -> "Lovy Chat 정보"
        AppLanguage.ARABIC -> "حول Lovy Chat"
        AppLanguage.SPANISH -> "Acerca de Lovy Chat"
        AppLanguage.FRENCH -> "À propos de Lovy Chat"
        AppLanguage.GERMAN -> "Über Lovy Chat"
        AppLanguage.RUSSIAN -> "О приложении Lovy Chat"
        AppLanguage.PORTUGUESE -> "Sobre o Lovy Chat"
        AppLanguage.INDONESIAN -> "Tentang Lovy Chat"
        else -> "About Lovy Chat"
    }

    fun aboutAppTagline(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "新朋友，身边的精彩畅聊 ✨"
        AppLanguage.JAPANESE -> "新しい友達、身近で楽しいチャット ✨"
        AppLanguage.KOREAN -> "새로운 친구, 내 주변의 신나는 대화 ✨"
        AppLanguage.ARABIC -> "أصدقاء جدد ومحادثات ممتعة من حولك ✨"
        AppLanguage.SPANISH -> "Nuevos amigos y charlas emocionantes a tu alrededor ✨"
        AppLanguage.FRENCH -> "De nouveaux amis et des discussions passionnantes autour de vous ✨"
        AppLanguage.GERMAN -> "Neue Freunde, spannende Gespräche in deiner Nähe ✨"
        AppLanguage.RUSSIAN -> "Новые друзья и яркое общение рядом с вами ✨"
        AppLanguage.PORTUGUESE -> "Novos amigos e conversas empolgantes ao seu redor ✨"
        AppLanguage.INDONESIAN -> "Teman baru, obrolan seru di sekitarmu ✨"
        else -> "New friends, exciting chats all around you ✨"
    }

    fun aboutAppVersionBadge(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "官方版本 1.0.0 (2026)"
        AppLanguage.JAPANESE -> "公式バージョン 1.0.0 (2026)"
        AppLanguage.KOREAN -> "공식 버전 1.0.0 (2026)"
        AppLanguage.ARABIC -> "الإصدار الرسمي 1.0.0 (2026)"
        AppLanguage.SPANISH -> "Versión Oficial 1.0.0 (2026)"
        AppLanguage.FRENCH -> "Version Officielle 1.0.0 (2026)"
        AppLanguage.GERMAN -> "Offizielle Version 1.0.0 (2026)"
        AppLanguage.RUSSIAN -> "Официальная версия 1.0.0 (2026)"
        AppLanguage.PORTUGUESE -> "Versão Oficial 1.0.0 (2026)"
        AppLanguage.INDONESIAN -> "Versi 1.0.0 Resmi (2026)"
        else -> "Official Version 1.0.0 (2026)"
    }

    fun aboutAppOverview(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "Lovy Chat 是一款现代社交聊天平台，让您轻松发现附近的新朋友、分享日常动态，安全快速地畅快交流。"
        AppLanguage.JAPANESE -> "Lovy Chat は、近くの新しい友達を見つけ、日々の瞬間を共有し、素早く安全に楽しく交流できるモダンなソーシャルチャットアプリです。"
        AppLanguage.KOREAN -> "Lovy Chat은 주변의 새로운 친구를 찾고 일상을 공유하며 안전하고 즐겁게 소통할 수 있는 현대적인 소셜 채팅 플랫폼입니다."
        AppLanguage.ARABIC -> "Lovy Chat هي منصة دردشة اجتماعية حديثة تتيح لك العثور على أصدقاء جدد في الجوار ومشاركة اللحظات اليومية بأمان وسرعة ومرح."
        AppLanguage.SPANISH -> "Lovy Chat es una moderna plataforma de chat social que te permite encontrar nuevos amigos cercanos, compartir momentos diarios y conectar de forma rápida y segura."
        AppLanguage.FRENCH -> "Lovy Chat est une plateforme de messagerie moderne qui vous permet de trouver de nouveaux amis à proximité, de partager vos moments et d'échanger en toute sécurité."
        AppLanguage.GERMAN -> "Lovy Chat ist eine moderne soziale Chat-Plattform, mit der Sie neue Freunde in der Nähe finden, tägliche Momente teilen und sicher kommunizieren können."
        AppLanguage.RUSSIAN -> "Lovy Chat — это современная социальная платформа для поиска друзей поблизости, обмена моментами и безопасного общения."
        AppLanguage.PORTUGUESE -> "O Lovy Chat é uma plataforma de chat social moderna que facilita encontrar novos amigos por perto, compartilhar momentos e conversar com segurança."
        AppLanguage.INDONESIAN -> "Lovy Chat adalah platform obrolan sosial modern yang memudahkan kamu menemukan teman baru di sekitar, berbagi momen harian, dan bertukar cerita secara cepat, aman, dan menyenangkan."
        else -> "Lovy Chat is a modern social chat platform that makes it easy to find new friends nearby, share daily moments, and connect quickly, safely, and joyfully."
    }

    fun aboutAppCoreFeaturesTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "核心特色"
        AppLanguage.JAPANESE -> "主な機能"
        AppLanguage.KOREAN -> "주요 기능"
        AppLanguage.ARABIC -> "الميزات الرئيسية"
        AppLanguage.SPANISH -> "Funciones Destacadas"
        AppLanguage.FRENCH -> "Fonctionnalités principales"
        AppLanguage.GERMAN -> "Hauptfunktionen"
        AppLanguage.RUSSIAN -> "Основные функции"
        AppLanguage.PORTUGUESE -> "Recursos Principais"
        AppLanguage.INDONESIAN -> "Fitur Unggulan"
        else -> "Key Features"
    }

    fun aboutAppFeat1Title(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "附近好友雷达"
        AppLanguage.JAPANESE -> "周辺の友達レーダー"
        AppLanguage.KOREAN -> "주변 친구 레이더"
        AppLanguage.ARABIC -> "رادار الأصدقاء في الجوار"
        AppLanguage.SPANISH -> "Radar de Amigos Cercanos"
        AppLanguage.FRENCH -> "Radar d'amis proches"
        AppLanguage.GERMAN -> "Freunde-Radar in der Nähe"
        AppLanguage.RUSSIAN -> "Радар друзей поблизости"
        AppLanguage.PORTUGUESE -> "Radar de Amigos Próximos"
        AppLanguage.INDONESIAN -> "Radar Teman Sekitar"
        else -> "Nearby Friends Radar"
    }

    fun aboutAppFeat1Desc(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "基于 GPS 发现身边好友，具备灵活的距离控制和隐私隐身模式。"
        AppLanguage.JAPANESE -> "GPS を基に近くの友達を発見。距離の調整やゴーストモードによるプライバシー保護に対応。"
        AppLanguage.KOREAN -> "GPS 기반으로 가까운 친구를 찾으며 거리 제어 및 고스트 모드 프라이버시를 지원합니다."
        AppLanguage.ARABIC -> "اعثر على أصدقاء مقربين عبر GPS مع التحكم بالمسافة ووضع التخفي لحماية الخصوصية."
        AppLanguage.SPANISH -> "Encuentra amigos cercanos con GPS, control de distancia y modo fantasma de privacidad."
        AppLanguage.FRENCH -> "Trouvez des amis proches grâce au GPS avec contrôle de distance et mode furtif."
        AppLanguage.GERMAN -> "Entdecke Freunde in der Nähe via GPS mit Abstandskontrolle und Geistermodus."
        AppLanguage.RUSSIAN -> "Находите друзей поблизости через GPS с контролем расстояния и режимом невидимки."
        AppLanguage.PORTUGUESE -> "Encontre amigos próximos via GPS com controle de distância e modo fantasma."
        AppLanguage.INDONESIAN -> "Temukan teman terdekat berbasis GPS dengan kendali jarak dan privasi penyamaran."
        else -> "Discover nearby friends via GPS with distance control and ghost privacy mode."
    }

    fun aboutAppFeat2Title(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "即时畅聊消息"
        AppLanguage.JAPANESE -> "高速リアルタイムメッセージ"
        AppLanguage.KOREAN -> "빠른 실시간 메시지"
        AppLanguage.ARABIC -> "رسائل فورية سريعة"
        AppLanguage.SPANISH -> "Mensajería Rápida en Tiempo Real"
        AppLanguage.FRENCH -> "Messagerie instantanée & temps réel"
        AppLanguage.GERMAN -> "Schnelle Echtzeit-Nachrichten"
        AppLanguage.RUSSIAN -> "Быстрые сообщения в реальном времени"
        AppLanguage.PORTUGUESE -> "Mensagens Rápidas em Tempo Real"
        AppLanguage.INDONESIAN -> "Pesan Cepat & Realtime"
        else -> "Fast & Realtime Messaging"
    }

    fun aboutAppFeat2Desc(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "即时发送文字与照片，并配有已读回执标记。"
        AppLanguage.JAPANESE -> "テキストや写真を即座に送信。メッセージの既読ステータス表示付き。"
        AppLanguage.KOREAN -> "읽음 확인 표시와 함께 텍스트 및 사진을 즉시 전송합니다."
        AppLanguage.ARABIC -> "أرسل الرسائل النصية والصور فوراً مع علامات قراءة الرسائل."
        AppLanguage.SPANISH -> "Envía textos y fotos al instante con indicadores de estado de lectura."
        AppLanguage.FRENCH -> "Envoyez des textes et des photos instantanément avec accusés de lecture."
        AppLanguage.GERMAN -> "Sende Texte & Fotos sofort mit Lesebestätigungshäkchen."
        AppLanguage.RUSSIAN -> "Мгновенная отправка текста и фото с отметками о прочтении."
        AppLanguage.PORTUGUESE -> "Envie textos e fotos instantaneamente com confirmação de leitura."
        AppLanguage.INDONESIAN -> "Kirim pesan teks & foto instan dengan tanda centang status pesan terbaca."
        else -> "Send text & photo messages instantly with read receipt status checks."
    }

    fun aboutAppFeat3Title(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "扫描条形码与二维码"
        AppLanguage.JAPANESE -> "バーコード & QR コード読み取り"
        AppLanguage.KOREAN -> "바코드 & QR 코드 스캔"
        AppLanguage.ARABIC -> "مسح الباركود ورمز QR"
        AppLanguage.SPANISH -> "Escanear Código de Barras y QR"
        AppLanguage.FRENCH -> "Scanner Barcode & Code QR"
        AppLanguage.GERMAN -> "Barcode- & QR-Code-Scan"
        AppLanguage.RUSSIAN -> "Сканирование штрихкодов и QR-кодов"
        AppLanguage.PORTUGUESE -> "Escanear Código de Barras e QR"
        AppLanguage.INDONESIAN -> "Pindai Barcode & QR Code"
        else -> "Scan Barcode & QR Code"
    }

    fun aboutAppFeat3Desc(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "无需费力输入号码或账号，秒速完成好友添加。"
        AppLanguage.JAPANESE -> "番号やIDの手動入力なしで、瞬時に友達を追加できます。"
        AppLanguage.KOREAN -> "번호나 아이디를 입력할 필요 없이 순식간에 친구를 추가하세요."
        AppLanguage.ARABIC -> "أضف أصدقاءك في ثوانٍ دون الحاجة لكتابة أرقام أو معرّفات."
        AppLanguage.SPANISH -> "Agrega amigos al instante sin molestarte en escribir números o identificadores."
        AppLanguage.FRENCH -> "Ajoutez des amis instantanément sans avoir à saisir de numéros ou d'identifiants."
        AppLanguage.GERMAN -> "Füge Freunde sofort hinzu, ohne mühsam Nummern oder IDs einzutippen."
        AppLanguage.RUSSIAN -> "Добавляйте друзей за секунду без необходимости вводить номера или ID."
        AppLanguage.PORTUGUESE -> "Adicione amigos em um instante sem precisar digitar números ou IDs."
        AppLanguage.INDONESIAN -> "Tambah teman langsung dalam sekejap tanpa repot mengetik nomor atau ID."
        else -> "Add friends instantly in a flash without typing phone numbers or IDs."
    }

    fun aboutAppFeat4Title(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "漂流瓶 (Drift Bottle)"
        AppLanguage.JAPANESE -> "海流の漂流瓶 (Drift Bottle)"
        AppLanguage.KOREAN -> "바다 유리병 편지 (Drift Bottle)"
        AppLanguage.ARABIC -> "زجاجة المحيط (Drift Bottle)"
        AppLanguage.SPANISH -> "Botella del Océano (Drift Bottle)"
        AppLanguage.FRENCH -> "Bouteille à la mer (Drift Bottle)"
        AppLanguage.GERMAN -> "Flaschenpost (Drift Bottle)"
        AppLanguage.RUSSIAN -> "Бутылка в океане (Drift Bottle)"
        AppLanguage.PORTUGUESE -> "Garrafa no Oceano (Drift Bottle)"
        AppLanguage.INDONESIAN -> "Botol Lautan (Drift Bottle)"
        else -> "Ocean Drift Bottle"
    }

    fun aboutAppFeat4Desc(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "向汪洋大海掷出一条神秘漂流瓶，随机遇见远方新朋友。"
        AppLanguage.JAPANESE -> "海へランダムなメッセージを流し、新しい友達との偶然の出会いを楽しめます。"
        AppLanguage.KOREAN -> "바다로 메시지를 띄워 멀리 있는 새로운 친구와 인연을 맺어보세요."
        AppLanguage.ARABIC -> "ألقِ رسالة عشوائية في المحيط لتتواصل مع أصدقاء جدد حول العالم."
        AppLanguage.SPANISH -> "Lanza un mensaje aleatorio al mar para conectar con nuevos amigos."
        AppLanguage.FRENCH -> "Lancez une bouteille à la mer pour vous connecter avec de nouveaux amis."
        AppLanguage.GERMAN -> "Wirf eine Nachricht ins Meer, um neue Freunde zu finden."
        AppLanguage.RUSSIAN -> "Отправьте послание через океан, чтобы познакомиться с новыми людьми."
        AppLanguage.PORTUGUESE -> "Jogue uma mensagem no oceano para se conectar com novas pessoas."
        AppLanguage.INDONESIAN -> "Lempar pesan acak melintasi lautan untuk terhubung dengan teman baru."
        else -> "Cast a random message across the ocean to connect with new friends."
    }

    fun aboutAppFeat5Title(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "动态与故事 (Moments)"
        AppLanguage.JAPANESE -> "モーメント & ストーリー"
        AppLanguage.KOREAN -> "모먼트 & 스토리"
        AppLanguage.ARABIC -> "اللحظات والقصص (Moments)"
        AppLanguage.SPANISH -> "Momentos e Historias"
        AppLanguage.FRENCH -> "Moments & Histoires"
        AppLanguage.GERMAN -> "Momente & Storys"
        AppLanguage.RUSSIAN -> "Моменты и истории"
        AppLanguage.PORTUGUESE -> "Momentos e Histórias"
        AppLanguage.INDONESIAN -> "Momen & Cerita"
        else -> "Moments & Stories"
    }

    fun aboutAppFeat5Desc(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "分享每日照片与心情，获得好友的真诚点赞与温馨评论。"
        AppLanguage.JAPANESE -> "日々の写真や近況を投稿し、友達からのいいねやコメントで交流。"
        AppLanguage.KOREAN -> "일상 사진과 글을 올리고 친구들의 좋아요와 댓글로 소통하세요."
        AppLanguage.ARABIC -> "شارك صورك ويومياتك وتلقَّ الإعجابات والتعليقات من الأصدقاء."
        AppLanguage.SPANISH -> "Comparte fotos y estados diarios con me gusta y comentarios de tus amigos."
        AppLanguage.FRENCH -> "Partagez des photos et des publications avec likes et commentaires de vos amis."
        AppLanguage.GERMAN -> "Teile tägliche Fotos und Status mit Likes und Kommentaren von Freunden."
        AppLanguage.RUSSIAN -> "Делитесь фото и статусами, получая лайки и комментарии друзей."
        AppLanguage.PORTUGUESE -> "Compartilhe fotos e status diários com curtidas e comentários de amigos."
        AppLanguage.INDONESIAN -> "Bagikan foto dan status harian dengan suka serta komentar dari teman."
        else -> "Share daily photos and statuses with likes and comments from friends."
    }

    fun aboutAppSecurityTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "🛡️ 值得信赖的隐私与安全"
        AppLanguage.JAPANESE -> "🛡️ 信頼のプライバシー & セキュリティ"
        AppLanguage.KOREAN -> "🛡️ 신뢰할 수 있는 프라이버시 & 보안"
        AppLanguage.ARABIC -> "🛡️ خصوصية وأمان موثوق"
        AppLanguage.SPANISH -> "🛡️ Privacidad y Seguridad Confiables"
        AppLanguage.FRENCH -> "🛡️ Confidentialité & Sécurité de confiance"
        AppLanguage.GERMAN -> "🛡️ Vertrauenswürdige Privatsphäre & Sicherheit"
        AppLanguage.RUSSIAN -> "🛡️ Надежная защита и безопасность"
        AppLanguage.PORTUGUESE -> "🛡️ Privacidade e Segurança Confiáveis"
        AppLanguage.INDONESIAN -> "🛡️ Privasi & Keamanan Terpercaya"
        else -> "🛡️ Trusted Privacy & Security"
    }

    fun aboutAppSecurityDesc(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "所有连接均采用 HTTPS/TLS 安全加密传输。内置用户拉黑、违规举报及一键永久注销账号功能。"
        AppLanguage.JAPANESE -> "全接続に安全な HTTPS/TLS 暗号化を採用。ユーザーのブロック、違反通報、アカウントの永久削除に対応。"
        AppLanguage.KOREAN -> "모든 연결은 안전한 HTTPS/TLS 암호화로 보호됩니다. 차단, 신고 및 계정 영구 삭제 기능 탑재."
        AppLanguage.ARABIC -> "جميع الاتصالات مشفرة ببروتوكول HTTPS/TLS الآمن. يتضمن حظر المستخدمين، الإبلاغ، وحذف الحساب نهائياً."
        AppLanguage.SPANISH -> "Todas las conexiones usan cifrado seguro HTTPS/TLS. Incluye bloqueo de usuarios, denuncias y eliminación permanente de cuenta."
        AppLanguage.FRENCH -> "Toutes les connexions utilisent le chiffrement HTTPS/TLS. Comprend le blocage d'utilisateurs, le signalement et la suppression définitive du compte."
        AppLanguage.GERMAN -> "Alle Verbindungen sind mit HTTPS/TLS verschlüsselt. Inklusive Blockierfunktion, Meldungen und dauerhafter Kontolöschung."
        AppLanguage.RUSSIAN -> "Все соединения защищены шифрованием HTTPS/TLS. Встроены функции блокировки, жалоб и полного удаления аккаунта."
        AppLanguage.PORTUGUESE -> "Todas as conexões usam criptografia segura HTTPS/TLS. Inclui bloqueio de usuários, denúncias e exclusão permanente de conta."
        AppLanguage.INDONESIAN -> "Seluruh koneksi menggunakan enkripsi aman HTTPS/TLS. Dilengkapi sistem pemblokiran pengguna, pelaporan pelanggaran, dan penghapusan akun permanen mandiri."
        else -> "All connections use secure HTTPS/TLS encryption. Features user blocking, violation reporting, and permanent account deletion."
    }

    fun aboutAppDevCredit(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "由 Geccko Creator 精心开发"
        AppLanguage.JAPANESE -> "Geccko Creator により開発"
        AppLanguage.KOREAN -> "Geccko Creator 제작"
        AppLanguage.ARABIC -> "تم التطوير بواسطة Geccko Creator"
        AppLanguage.SPANISH -> "Desarrollado por Geccko Creator"
        AppLanguage.FRENCH -> "Développé par Geccko Creator"
        AppLanguage.GERMAN -> "Entwickelt von Geccko Creator"
        AppLanguage.RUSSIAN -> "Разработано Geccko Creator"
        AppLanguage.PORTUGUESE -> "Desenvolvido por Geccko Creator"
        AppLanguage.INDONESIAN -> "Dikembangkan oleh Geccko Creator"
        else -> "Developed by Geccko Creator"
    }

    fun aboutAppCopyright(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "© 2026 Lovy Chat. 保留所有权利。"
        AppLanguage.JAPANESE -> "© 2026 Lovy Chat. 無断転載を禁じます。"
        AppLanguage.KOREAN -> "© 2026 Lovy Chat. 모든 권리 보유."
        AppLanguage.ARABIC -> "© 2026 Lovy Chat. جميع الحقوق محفوظة."
        AppLanguage.SPANISH -> "© 2026 Lovy Chat. Todos los derechos reservados."
        AppLanguage.FRENCH -> "© 2026 Lovy Chat. Tous droits réservés."
        AppLanguage.GERMAN -> "© 2026 Lovy Chat. Alle Rechte vorbehalten."
        AppLanguage.RUSSIAN -> "© 2026 Lovy Chat. Все права защищены."
        AppLanguage.PORTUGUESE -> "© 2026 Lovy Chat. Todos os direitos reservados."
        AppLanguage.INDONESIAN -> "© 2026 Lovy Chat. Hak cipta dilindungi undang-undang."
        else -> "© 2026 Lovy Chat. All rights reserved."
    }

    fun aboutAppPrivacyPolicyButton(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "隐私政策"
        AppLanguage.JAPANESE -> "プライバシーポリシー"
        AppLanguage.KOREAN -> "개인정보 처리방침"
        AppLanguage.ARABIC -> "سياسة الخصوصية"
        AppLanguage.SPANISH -> "Política de Privacidad"
        AppLanguage.FRENCH -> "Politique de confidentialité"
        AppLanguage.GERMAN -> "Datenschutzerklärung"
        AppLanguage.RUSSIAN -> "Политика конфиденциальности"
        AppLanguage.PORTUGUESE -> "Política de Privacidade"
        AppLanguage.INDONESIAN -> "Kebijakan Privasi"
        else -> "Privacy Policy"
    }

    fun aboutAppCloseButton(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "关闭"
        AppLanguage.JAPANESE -> "閉じる"
        AppLanguage.KOREAN -> "닫기"
        AppLanguage.ARABIC -> "إغلاق"
        AppLanguage.SPANISH -> "Cerrar"
        AppLanguage.FRENCH -> "Fermer"
        AppLanguage.GERMAN -> "Schließen"
        AppLanguage.RUSSIAN -> "Закрыть"
        AppLanguage.PORTUGUESE -> "Fechar"
        AppLanguage.INDONESIAN -> "Tutup"
        else -> "Close"
    }

    // --- ADDITIONAL MULTI-LANGUAGE STRINGS ---

    fun qrAccessCameraRequired(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "需要相机权限"
        AppLanguage.JAPANESE -> "カメラへのアクセスが必要です"
        AppLanguage.KOREAN -> "카메라 접근 권한 필요"
        AppLanguage.ARABIC -> "مطلوب إذن الكاميرا"
        AppLanguage.SPANISH -> "Acceso a la Cámara Requerido"
        AppLanguage.FRENCH -> "Accès à la caméra requis"
        AppLanguage.GERMAN -> "Kamerazugriff erforderlich"
        AppLanguage.RUSSIAN -> "Требуется доступ к камере"
        AppLanguage.PORTUGUESE -> "Acesso à Câmera Necessário"
        AppLanguage.INDONESIAN -> "Akses Kamera Diperlukan"
        else -> "Camera Access Required"
    }

    fun qrAccessCameraDesc(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "如需直接扫描二维码或条形码，请允许相机权限。您也可以从相册上传二维码图片。"
        AppLanguage.JAPANESE -> "QRコードを直接スキャンするには、カメラへのアクセスを許可してください。ギャラリーから画像をアップロードすることもできます。"
        AppLanguage.KOREAN -> "QR 코드를 직접 스캔하려면 카메라 접근을 허용하세요. 갤러리에서 QR 이미지를 업로드할 수도 있습니다."
        AppLanguage.ARABIC -> "لمسح رموز QR مباشرة، يرجى السماح للتطبيق بالوصول إلى الكاميرا. يمكنك أيضاً رفع صورة من المعرض."
        AppLanguage.SPANISH -> "Para escanear códigos QR directamente, permite el acceso a la cámara. También puedes subir una imagen desde la galería."
        AppLanguage.FRENCH -> "Pour scanner directement les codes QR, autorisez l'accès à la caméra. Vous pouvez aussi importer une image depuis la galerie."
        AppLanguage.GERMAN -> "Um QR-Codes direkt zu scannen, erlaube den Kamerazugriff. Du kannst auch ein Bild aus der Galerie hochladen."
        AppLanguage.RUSSIAN -> "Чтобы сканировать QR-коды напрямую, разрешите доступ к камере. Вы также можете загрузить изображение из галереи."
        AppLanguage.PORTUGUESE -> "Para escanear códigos QR diretamente, permita o acesso à câmera. Você também pode enviar uma imagem da galeria."
        AppLanguage.INDONESIAN -> "Untuk memindai kode QR atau barcode secara langsung, izinkan aplikasi mengakses kamera. Anda juga tetap dapat mengunggah gambar kode QR dari galeri foto."
        else -> "To scan QR codes or barcodes directly, allow camera access. You can also upload a QR image from your gallery."
    }

    fun qrGrantCameraBtn(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "允许相机"
        AppLanguage.JAPANESE -> "カメラを許可"
        AppLanguage.KOREAN -> "카메라 허용"
        AppLanguage.ARABIC -> "السماح بالكاميرا"
        AppLanguage.SPANISH -> "Permitir Cámara"
        AppLanguage.FRENCH -> "Autoriser la caméra"
        AppLanguage.GERMAN -> "Kamera erlauben"
        AppLanguage.RUSSIAN -> "Разрешить камеру"
        AppLanguage.PORTUGUESE -> "Permitir Câmera"
        AppLanguage.INDONESIAN -> "Izinkan Kamera"
        else -> "Allow Camera"
    }

    fun qrUploadFromGalleryBtn(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "从相册上传"
        AppLanguage.JAPANESE -> "ギャラリーから選択"
        AppLanguage.KOREAN -> "갤러리에서 업로드"
        AppLanguage.ARABIC -> "تحميل من المعرض"
        AppLanguage.SPANISH -> "Subir desde Galería"
        AppLanguage.FRENCH -> "Importer depuis la galerie"
        AppLanguage.GERMAN -> "Aus Galerie hochladen"
        AppLanguage.RUSSIAN -> "Загрузить из галереи"
        AppLanguage.PORTUGUESE -> "Enviar da Galeria"
        AppLanguage.INDONESIAN -> "Unggah dari Galeri"
        else -> "Upload from Gallery"
    }

    fun qrUploadPhotoBtn(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "上传照片"
        AppLanguage.JAPANESE -> "写真をアップロード"
        AppLanguage.KOREAN -> "사진 업로드"
        AppLanguage.ARABIC -> "تحميل صورة"
        AppLanguage.SPANISH -> "Subir Foto"
        AppLanguage.FRENCH -> "Télécharger une photo"
        AppLanguage.GERMAN -> "Foto hochladen"
        AppLanguage.RUSSIAN -> "Загрузить фото"
        AppLanguage.PORTUGUESE -> "Enviar Foto"
        AppLanguage.INDONESIAN -> "Unggah Foto"
        else -> "Upload Photo"
    }

    fun qrMyQrBtn(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "我的二维码"
        AppLanguage.JAPANESE -> "マイQR"
        AppLanguage.KOREAN -> "내 QR"
        AppLanguage.ARABIC -> "رمزي"
        AppLanguage.SPANISH -> "Mi QR"
        AppLanguage.FRENCH -> "Mon QR"
        AppLanguage.GERMAN -> "Mein QR"
        AppLanguage.RUSSIAN -> "Мой QR"
        AppLanguage.PORTUGUESE -> "Meu QR"
        AppLanguage.INDONESIAN -> "QR Saya"
        else -> "My QR"
    }

    fun qrScanSubtitlePrompt(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "将相机对准好友的二维码"
        AppLanguage.JAPANESE -> "カメラを友達のQRコードに向けてください"
        AppLanguage.KOREAN -> "친구의 QR 코드에 카메라를 맞춰주세요"
        AppLanguage.ARABIC -> "وجّه الكاميرا نحو رمز صديقك"
        AppLanguage.SPANISH -> "Apunta la cámara al código QR de tu amigo"
        AppLanguage.FRENCH -> "Pointez la caméra vers le code QR d'un ami"
        AppLanguage.GERMAN -> "Richte die Kamera auf den QR-Code deines Freundes"
        AppLanguage.RUSSIAN -> "Наведите камеру на QR-код друга"
        AppLanguage.PORTUGUESE -> "Aponte a câmera para o código QR do amigo"
        AppLanguage.INDONESIAN -> "Arahkan kamera ke Kode QR teman"
        else -> "Point camera at friend's QR code"
    }

    fun qrScannedSuccessTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "二维码扫描成功"
        AppLanguage.JAPANESE -> "スキャンに成功しました"
        AppLanguage.KOREAN -> "코드 스캔 성공"
        AppLanguage.ARABIC -> "تم مسح الرمز بنجاح"
        AppLanguage.SPANISH -> "Código Escaneado con Éxito"
        AppLanguage.FRENCH -> "Code scanné avec succès"
        AppLanguage.GERMAN -> "Code erfolgreich gescannt"
        AppLanguage.RUSSIAN -> "Код успешно отсканирован"
        AppLanguage.PORTUGUESE -> "Código Escaneado com Sucesso"
        AppLanguage.INDONESIAN -> "Kode Berhasil Dipindai"
        else -> "Code Successfully Scanned"
    }

    fun qrIsSelfNotice(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "这是您自己的个人主页二维码"
        AppLanguage.JAPANESE -> "これはあなた自身のプロフィールQRコードです"
        AppLanguage.KOREAN -> "이것은 회원님 본인의 프로필 QR 코드입니다"
        AppLanguage.ARABIC -> "هذا هو رمز QR لملفك الشخصي الخاص"
        AppLanguage.SPANISH -> "Este es el código QR de tu propio perfil"
        AppLanguage.FRENCH -> "Ceci est le code QR de votre propre profil"
        AppLanguage.GERMAN -> "Dies ist dein eigener Profil-QR-Code"
        AppLanguage.RUSSIAN -> "Это ваш собственный QR-код профиля"
        AppLanguage.PORTUGUESE -> "Este é o código QR do seu próprio perfil"
        AppLanguage.INDONESIAN -> "Ini adalah kode QR profil akun Anda sendiri"
        else -> "This is your own profile QR code"
    }

    fun qrAlreadyFriendNotice(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "已在您的好友列表中"
        AppLanguage.JAPANESE -> "すでに友達リストに登録されています"
        AppLanguage.KOREAN -> "이미 친구 목록에 등록되어 있습니다"
        AppLanguage.ARABIC -> "موجود بالفعل في قائمة أصدقائك"
        AppLanguage.SPANISH -> "Ya está en tu lista de amigos"
        AppLanguage.FRENCH -> "Déjà dans votre liste d'amis"
        AppLanguage.GERMAN -> "Bereits in deiner Freundesliste"
        AppLanguage.RUSSIAN -> "Уже в вашем списке друзей"
        AppLanguage.PORTUGUESE -> "Já está na sua lista de amigos"
        AppLanguage.INDONESIAN -> "Sudah ada di daftar Teman Anda"
        else -> "Already in your Friends list"
    }

    fun qrAddAsFriendBtn(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "添加为好友"
        AppLanguage.JAPANESE -> "友達に追加"
        AppLanguage.KOREAN -> "친구로 추가"
        AppLanguage.ARABIC -> "إضافة كصديق"
        AppLanguage.SPANISH -> "Agregar como Amigo"
        AppLanguage.FRENCH -> "Ajouter en ami"
        AppLanguage.GERMAN -> "Als Freund hinzufügen"
        AppLanguage.RUSSIAN -> "Добавить в друзья"
        AppLanguage.PORTUGUESE -> "Adicionar como Amigo"
        AppLanguage.INDONESIAN -> "Tambahkan Sebagai Teman"
        else -> "Add as Friend"
    }

    fun qrSayHiDirectBtn(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "打招呼并直接发消息"
        AppLanguage.JAPANESE -> "挨拶して直接メッセージを送る"
        AppLanguage.KOREAN -> "인사하고 바로 메시지 보내기"
        AppLanguage.ARABIC -> "التحية وإرسال رسالة مباشرة"
        AppLanguage.SPANISH -> "Saludar y Enviar Mensaje Directo"
        AppLanguage.FRENCH -> "Dire bonjour et envoyer un message"
        AppLanguage.GERMAN -> "Hallo sagen & direkt schreiben"
        AppLanguage.RUSSIAN -> "Поздороваться и написать напрямую"
        AppLanguage.PORTUGUESE -> "Cumprimentar e Enviar Mensagem Direta"
        AppLanguage.INDONESIAN -> "Sapa & Kirim Pesan Langsung"
        else -> "Say Hi & Message Directly"
    }

    fun qrOpenChatBtn(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "打开聊天"
        AppLanguage.JAPANESE -> "チャットを開く"
        AppLanguage.KOREAN -> "채팅 열기"
        AppLanguage.ARABIC -> "فتح الدردشة"
        AppLanguage.SPANISH -> "Abrir Chat"
        AppLanguage.FRENCH -> "Ouvrir la discussion"
        AppLanguage.GERMAN -> "Chat öffnen"
        AppLanguage.RUSSIAN -> "Открыть чат"
        AppLanguage.PORTUGUESE -> "Abrir Conversa"
        AppLanguage.INDONESIAN -> "Buka Obrolan"
        else -> "Open Chat"
    }

    fun qrCameraPermissionDenied(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "相机权限被拒绝。您仍可从相册上传二维码图片。"
        AppLanguage.JAPANESE -> "カメラの権限が拒否されました。ギャラリーから画像をアップロードできます。"
        AppLanguage.KOREAN -> "카메라 권한이 거부되었습니다. 갤러리에서 QR 이미지를 업로드할 수 있습니다."
        AppLanguage.ARABIC -> "تم رفض إذن الكاميرا. يمكنك مع ذلك تحميل صورة رمز QR من المعرض."
        AppLanguage.SPANISH -> "Permiso de cámara denegado. Aún puedes subir una imagen de QR desde la galería."
        AppLanguage.FRENCH -> "Permission caméra refusée. Vous pouvez toujours importer une image QR depuis la galerie."
        AppLanguage.GERMAN -> "Kameraberechtigung verweigert. Du kannst weiterhin ein QR-Bild aus der Galerie hochladen."
        AppLanguage.RUSSIAN -> "В разрешении камеры отказано. Вы всё ещё можете загрузить QR-код из галереи."
        AppLanguage.PORTUGUESE -> "Permissão de câmera negada. Você ainda pode enviar uma imagem QR da galeria."
        AppLanguage.INDONESIAN -> "Izin kamera ditolak. Anda tetap dapat mengunggah gambar QR dari galeri."
        else -> "Camera permission denied. You can still upload a QR image from your gallery."
    }

    fun qrNoCodeFoundInImage(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "所选图片中未找到二维码或条形码"
        AppLanguage.JAPANESE -> "選択した画像にQRコードまたはバーコードが見つかりません"
        AppLanguage.KOREAN -> "선택한 이미지에서 QR 코드 또는 바코드를 찾을 수 없습니다"
        AppLanguage.ARABIC -> "لم يتم العثور على رمز QR أو باركود في الصورة المحددة"
        AppLanguage.SPANISH -> "No se encontró ningún código QR o de barras en la imagen seleccionada"
        AppLanguage.FRENCH -> "Aucun code QR ou code-barres trouvé dans l'image sélectionnée"
        AppLanguage.GERMAN -> "Kein QR-Code oder Barcode im ausgewählten Bild gefunden"
        AppLanguage.RUSSIAN -> "В выбранном изображении не найден QR-код или штрихкод"
        AppLanguage.PORTUGUESE -> "Nenhum código QR ou código de barras encontrado na imagem selecionada"
        AppLanguage.INDONESIAN -> "Tidak ditemukan Kode QR atau Barcode pada gambar yang dipilih"
        else -> "No QR code or Barcode found in selected image"
    }

    fun qrProcessingGalleryImage(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "正在处理相册图片..."
        AppLanguage.JAPANESE -> "ギャラリー画像を処理中..."
        AppLanguage.KOREAN -> "갤러리 이미지 처리 중..."
        AppLanguage.ARABIC -> "جاري معالجة صورة المعرض..."
        AppLanguage.SPANISH -> "Procesando imagen de la galería..."
        AppLanguage.FRENCH -> "Traitement de l'image de la galerie..."
        AppLanguage.GERMAN -> "Galeriebild wird verarbeitet..."
        AppLanguage.RUSSIAN -> "Обработка изображения из галереи..."
        AppLanguage.PORTUGUESE -> "Processando imagem da galeria..."
        AppLanguage.INDONESIAN -> "Memproses gambar galeri..."
        else -> "Processing gallery image..."
    }

    fun qrSearchingUserData(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "正在查找好友信息..."
        AppLanguage.JAPANESE -> "友達のデータを検索中..."
        AppLanguage.KOREAN -> "친구 정보 검색 중..."
        AppLanguage.ARABIC -> "جاري البحث عن بيانات الصديق..."
        AppLanguage.SPANISH -> "Buscando datos de tu amigo..."
        AppLanguage.FRENCH -> "Recherche des informations de l'ami..."
        AppLanguage.GERMAN -> "Freundesdaten werden gesucht..."
        AppLanguage.RUSSIAN -> "Поиск данных друга..."
        AppLanguage.PORTUGUESE -> "Buscando dados do amigo..."
        AppLanguage.INDONESIAN -> "Mencari data teman..."
        else -> "Searching friend data..."
    }

    fun qrInvalidUserId(lang: AppLanguage, code: String): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "无效的用户 ID: $code"
        AppLanguage.JAPANESE -> "無効なユーザーID: $code"
        AppLanguage.KOREAN -> "유효하지 않은 사용자 ID: $code"
        AppLanguage.ARABIC -> "معرف مستخدم غير صالح: $code"
        AppLanguage.SPANISH -> "ID de usuario no válido: $code"
        AppLanguage.FRENCH -> "Identifiant utilisateur invalide : $code"
        AppLanguage.GERMAN -> "Ungültige Benutzer-ID: $code"
        AppLanguage.RUSSIAN -> "Недействительный ID пользователя: $code"
        AppLanguage.PORTUGUESE -> "ID de usuário inválido: $code"
        AppLanguage.INDONESIAN -> "ID pengguna tidak valid: $code"
        else -> "Invalid user ID: $code"
    }

    fun qrUserNotFoundWithCode(lang: AppLanguage, code: String): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "未找到用户: $code"
        AppLanguage.JAPANESE -> "ユーザーが見つかりません: $code"
        AppLanguage.KOREAN -> "사용자를 찾을 수 없습니다: $code"
        AppLanguage.ARABIC -> "المستخدم غير موجود: $code"
        AppLanguage.SPANISH -> "Usuario no encontrado: $code"
        AppLanguage.FRENCH -> "Utilisateur non trouvé : $code"
        AppLanguage.GERMAN -> "Benutzer nicht gefunden: $code"
        AppLanguage.RUSSIAN -> "Пользователь не найден: $code"
        AppLanguage.PORTUGUESE -> "Usuário não encontrado: $code"
        AppLanguage.INDONESIAN -> "Pengguna tidak ditemukan: $code"
        else -> "User not found: $code"
    }

    fun blockedUsersTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "已屏蔽用户列表"
        AppLanguage.JAPANESE -> "ブロックしたユーザー一覧"
        AppLanguage.KOREAN -> "차단된 사용자 목록"
        AppLanguage.ARABIC -> "قائمة المستخدمين المحظورين"
        AppLanguage.SPANISH -> "Lista de Usuarios Bloqueados"
        AppLanguage.FRENCH -> "Liste des utilisateurs bloqués"
        AppLanguage.GERMAN -> "Liste blockierter Benutzer"
        AppLanguage.RUSSIAN -> "Список заблокированных пользователей"
        AppLanguage.PORTUGUESE -> "Lista de Usuários Bloqueados"
        AppLanguage.INDONESIAN -> "Daftar Pengguna Diblokir"
        else -> "Blocked Users List"
    }

    fun blockedUsersCount(lang: AppLanguage, count: Int): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "$count 位用户已被屏蔽"
        AppLanguage.JAPANESE -> "$count 人のユーザーをブロック中"
        AppLanguage.KOREAN -> "$count 명의 사용자 차단됨"
        AppLanguage.ARABIC -> "تم حظر $count مستخدم"
        AppLanguage.SPANISH -> "$count usuarios bloqueados"
        AppLanguage.FRENCH -> "$count utilisateurs bloqués"
        AppLanguage.GERMAN -> "$count Benutzer blockiert"
        AppLanguage.RUSSIAN -> "$count пользователей заблокировано"
        AppLanguage.PORTUGUESE -> "$count usuários bloqueados"
        AppLanguage.INDONESIAN -> "$count pengguna diblokir"
        else -> "$count users blocked"
    }

    fun blockedUsersEmptyTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "没有被屏蔽的用户"
        AppLanguage.JAPANESE -> "ブロックしたユーザーはいません"
        AppLanguage.KOREAN -> "차단된 사용자가 없습니다"
        AppLanguage.ARABIC -> "لا يوجد مستخدمون محظورون"
        AppLanguage.SPANISH -> "No hay usuarios bloqueados"
        AppLanguage.FRENCH -> "Aucun utilisateur bloqué"
        AppLanguage.GERMAN -> "Keine blockierten Benutzer"
        AppLanguage.RUSSIAN -> "Нет заблокированных пользователей"
        AppLanguage.PORTUGUESE -> "Nenhum usuário bloqueado"
        AppLanguage.INDONESIAN -> "Tidak ada pengguna yang diblokir"
        else -> "No blocked users"
    }

    fun blockedUsersEmptyDesc(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "您在聊天中屏蔽的用户将显示在此处。"
        AppLanguage.JAPANESE -> "チャットでブロックしたユーザーはここに表示されます。"
        AppLanguage.KOREAN -> "채팅에서 차단한 사용자가 여기에 표시됩니다."
        AppLanguage.ARABIC -> "المستخدمون الذين تحظرهم في الدردشة سيظهرون هنا."
        AppLanguage.SPANISH -> "Los usuarios que bloquees en los chats aparecerán aquí."
        AppLanguage.FRENCH -> "Les utilisateurs que vous bloquez apparaîtront ici."
        AppLanguage.GERMAN -> "Benutzer, die du im Chat blockierst, erscheinen hier."
        AppLanguage.RUSSIAN -> "Пользователи, которых вы заблокировали в чате, появятся здесь."
        AppLanguage.PORTUGUESE -> "Os usuários que você bloquear no chat aparecerão aqui."
        AppLanguage.INDONESIAN -> "Pengguna yang Anda blokir di ruang chat akan muncul di sini."
        else -> "Users you block in chats will appear here."
    }

    fun blockedUsersNotice(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "以下用户无法向您发送消息或在“附近”中看到您："
        AppLanguage.JAPANESE -> "以下のユーザーはあなたにメッセージを送信したり、周辺検索であなたを表示したりできません："
        AppLanguage.KOREAN -> "아래 사용자는 회원님에게 메시지를 보내거나 주변에서 회원님을 볼 수 없습니다:"
        AppLanguage.ARABIC -> "لا يمكن للمستخدمين أدناه مراسلتك أو رؤيتك في ميزة بالقرب مني:"
        AppLanguage.SPANISH -> "Los siguientes usuarios no pueden enviarte mensajes ni verte en Cerca:"
        AppLanguage.FRENCH -> "Les utilisateurs ci-dessous ne peuvent pas vous envoyer de messages ni vous voir dans À proximité :"
        AppLanguage.GERMAN -> "Die folgenden Benutzer können dir keine Nachrichten senden oder dich in der Nähe sehen:"
        AppLanguage.RUSSIAN -> "Пользователи ниже не могут отправлять вам сообщения или видеть вас в «Рядом»:"
        AppLanguage.PORTUGUESE -> "Os usuários abaixo não podem enviar mensagens para você nem ver você no Perto de Mim:"
        AppLanguage.INDONESIAN -> "Pengguna di bawah ini tidak dapat mengirimi Anda pesan atau melihat Anda di Sekitar Saya:"
        else -> "The users below cannot send you messages or view you in Nearby:"
    }

    fun blockedUserUnblockedToast(lang: AppLanguage, name: String): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "已解除对 $name 的屏蔽"
        AppLanguage.JAPANESE -> "$name のブロックを解除しました"
        AppLanguage.KOREAN -> "$name 님의 차단이 해제되었습니다"
        AppLanguage.ARABIC -> "تم إلغاء حظر $name"
        AppLanguage.SPANISH -> "$name desbloqueado"
        AppLanguage.FRENCH -> "$name a été débloqué"
        AppLanguage.GERMAN -> "Blockierung für $name aufgehoben"
        AppLanguage.RUSSIAN -> "Блокировка с $name снята"
        AppLanguage.PORTUGUESE -> "Bloqueio de $name removido"
        AppLanguage.INDONESIAN -> "Blokir untuk $name dibuka"
        else -> "Unblocked $name"
    }

    fun btnUnblock(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "解除屏蔽"
        AppLanguage.JAPANESE -> "ブロック解除"
        AppLanguage.KOREAN -> "차단 해제"
        AppLanguage.ARABIC -> "إلغاء الحظر"
        AppLanguage.SPANISH -> "Desbloquear"
        AppLanguage.FRENCH -> "Débloquer"
        AppLanguage.GERMAN -> "Entsperren"
        AppLanguage.RUSSIAN -> "Разблокировать"
        AppLanguage.PORTUGUESE -> "Desbloquear"
        AppLanguage.INDONESIAN -> "Buka Blokir"
        else -> "Unblock"
    }

    fun privacyShowOnlineStatusTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "显示在线状态"
        AppLanguage.JAPANESE -> "オンライン状態を表示"
        AppLanguage.KOREAN -> "온라인 상태 표시"
        AppLanguage.ARABIC -> "إظهار حالة الاتصال"
        AppLanguage.SPANISH -> "Mostrar Estado en Línea"
        AppLanguage.FRENCH -> "Afficher le statut en ligne"
        AppLanguage.GERMAN -> "Online-Status anzeigen"
        AppLanguage.RUSSIAN -> "Показывать статус «В сети»"
        AppLanguage.PORTUGUESE -> "Mostrar Status Online"
        AppLanguage.INDONESIAN -> "Tampilkan Status Online"
        else -> "Show Online Status"
    }

    fun privacyShowOnlineStatusDesc(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "在您活跃使用 Lovy Chat 时显示在线徽标。"
        AppLanguage.JAPANESE -> "Lovy Chatを開いているときにオンラインバッジを表示します。"
        AppLanguage.KOREAN -> "Lovy Chat을 사용 중일 때 온라인 표시를 보여줍니다."
        AppLanguage.ARABIC -> "يعرض شارة الاتصال عندما تستخدم Lovy Chat بنشاط."
        AppLanguage.SPANISH -> "Muestra una insignia en línea cuando estás usando Lovy Chat activamente."
        AppLanguage.FRENCH -> "Affiche un badge en ligne lorsque vous utilisez activement Lovy Chat."
        AppLanguage.GERMAN -> "Zeigt einen Online-Status an, wenn du Lovy Chat aktiv nutzt."
        AppLanguage.RUSSIAN -> "Показывает значок «В сети», когда вы активно используете Lovy Chat."
        AppLanguage.PORTUGUESE -> "Exibe o selo online quando você estiver usando o Lovy Chat ativamente."
        AppLanguage.INDONESIAN -> "Menampilkan tanda online ketika Anda sedang aktif membuka Lovy Chat."
        else -> "Displays an online badge when you are actively using Lovy Chat."
    }

    fun privacyNearbyRadarTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "在附近中展示我"
        AppLanguage.JAPANESE -> "周辺レーダーに自分を表示"
        AppLanguage.KOREAN -> "주변 레이더에 나 표시"
        AppLanguage.ARABIC -> "إظهاري في ميزة بالقرب مني"
        AppLanguage.SPANISH -> "Mostrarme en Cerca"
        AppLanguage.FRENCH -> "Me montrer dans À proximité"
        AppLanguage.GERMAN -> "Mich in der Nähe anzeigen"
        AppLanguage.RUSSIAN -> "Показывать меня в «Рядом»"
        AppLanguage.PORTUGUESE -> "Mostrar-me no Perto de Mim"
        AppLanguage.INDONESIAN -> "Tampilkan Saya di Sekitar"
        else -> "Show Me in Nearby"
    }

    fun privacyNearbyRadarDescVisible(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "您的主页处于活跃状态，其他人可在附近雷达中发现您。"
        AppLanguage.JAPANESE -> "プロフィールが公開され、周辺レーダーで他のユーザーに見つけてもらえます。"
        AppLanguage.KOREAN -> "프로필이 활성화되어 주변 레이더에서 다른 사용자가 나를 찾을 수 있습니다."
        AppLanguage.ARABIC -> "ملفك الشخصي نشط ويمكن للآخرين العثور عليك في رادار بالقرب مني."
        AppLanguage.SPANISH -> "Tu perfil está activo y otras personas pueden descubrirte en el radar de Cerca."
        AppLanguage.FRENCH -> "Votre profil est actif et détectable par les autres sur le radar À proximité."
        AppLanguage.GERMAN -> "Dein Profil ist aktiv und für andere im Umkreis-Radar sichtbar."
        AppLanguage.RUSSIAN -> "Ваш профиль активен и виден другим на радаре «Рядом»."
        AppLanguage.PORTUGUESE -> "Seu perfil está ativo e pode ser encontrado por outras pessoas no radar Perto de Mim."
        AppLanguage.INDONESIAN -> "Profil Anda aktif dan dapat ditemukan oleh pengguna lain di radar 'Di Sekitar Saya'."
        else -> "Your profile is active and discoverable by others on the Nearby radar."
    }

    fun privacyNearbyRadarDescHidden(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "隐身模式已开启。您的主页在附近雷达中已隐藏。"
        AppLanguage.JAPANESE -> "ステルスモード有効。周辺レーダーからプロフィールが隠されます。"
        AppLanguage.KOREAN -> "시크릿 모드 활성화. 주변 레이더에서 프로필이 숨겨집니다."
        AppLanguage.ARABIC -> "وضع التخفي نشط. تم إخفاء ملفك الشخصي من رادار بالقرب مني."
        AppLanguage.SPANISH -> "Modo incógnito activo. Tu perfil está oculto del radar de Cerca."
        AppLanguage.FRENCH -> "Mode incognito actif. Votre profil est masqué du radar À proximité."
        AppLanguage.GERMAN -> "Inkognito-Modus aktiv. Dein Profil ist im Umkreis-Radar unsichtbar."
        AppLanguage.RUSSIAN -> "Режим невидимки активен. Ваш профиль скрыт от радара «Рядом»."
        AppLanguage.PORTUGUESE -> "Modo incógnito ativo. Seu perfil está oculto no radar Perto de Mim."
        AppLanguage.INDONESIAN -> "Mode Penyamaran aktif. Profil Anda disembunyikan dari radar pencarian orang sekitar."
        else -> "Incognito mode active. Your profile is hidden from the Nearby radar."
    }

    fun privacyHideDistanceTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "隐藏精确距离"
        AppLanguage.JAPANESE -> "正確な距離を非公開"
        AppLanguage.KOREAN -> "정확한 거리 숨기기"
        AppLanguage.ARABIC -> "إخفاء المسافة الدقيقة"
        AppLanguage.SPANISH -> "Ocultar Distancia Exacta"
        AppLanguage.FRENCH -> "Masquer la distance exacte"
        AppLanguage.GERMAN -> "Genaue Entfernung verbergen"
        AppLanguage.RUSSIAN -> "Скрыть точное расстояние"
        AppLanguage.PORTUGUESE -> "Ocultar Distância Exata"
        AppLanguage.INDONESIAN -> "Sembunyikan Jarak Persis"
        else -> "Hide Exact Distance"
    }

    fun privacyHideDistanceDescHidden(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "精确距离已隐藏。其他人只能看到您的城市或区域名称。"
        AppLanguage.JAPANESE -> "正確な距離を非公開にします。他のユーザーには都市名またはエリア名のみが表示されます。"
        AppLanguage.KOREAN -> "정확한 거리가 숨겨집니다. 다른 사용자에게는 도시 또는 지역 이름만 표시됩니다."
        AppLanguage.ARABIC -> "المسافة الدقيقة مخفية. يمكن للآخرين فقط رؤية اسم مدينتك أو منطقتك."
        AppLanguage.SPANISH -> "La distancia exacta está oculta. Los demás solo pueden ver tu ciudad o región."
        AppLanguage.FRENCH -> "La distance exacte est masquée. Les autres ne peuvent voir que le nom de votre ville ou région."
        AppLanguage.GERMAN -> "Die genaue Entfernung ist verborgen. Andere sehen nur deinen Stadt- oder Gebietsnamen."
        AppLanguage.RUSSIAN -> "Точное расстояние скрыто. Другие видят только название вашего города или региона."
        AppLanguage.PORTUGUESE -> "A distância exata está oculta. Outros só podem ver o nome da sua cidade ou região."
        AppLanguage.INDONESIAN -> "Jarak meter/km disembunyikan. Orang lain hanya dapat melihat nama kota/wilayah Anda."
        else -> "Exact distance is hidden. Others can only see your city or area name."
    }

    fun privacyHideDistanceDescVisible(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "其他用户可以查看与您的预估距离（米或公里）。"
        AppLanguage.JAPANESE -> "他のユーザーはあなたからの推定距離（メートルまたはキロメートル）を確認できます。"
        AppLanguage.KOREAN -> "다른 사용자가 회원님과의 예상 거리(미터 또는 킬로미터)를 볼 수 있습니다."
        AppLanguage.ARABIC -> "يمكن للمستخدمين الآخرين رؤية مسافة تقديرية بالأمتار أو الكيلومترات منك."
        AppLanguage.SPANISH -> "Otros usuarios pueden ver una distancia estimada en metros o kilómetros desde tu ubicación."
        AppLanguage.FRENCH -> "Les autres utilisateurs peuvent voir une distance approximative en mètres ou kilomètres."
        AppLanguage.GERMAN -> "Andere Benutzer können eine geschätzte Entfernung in Metern oder Kilometern sehen."
        AppLanguage.RUSSIAN -> "Другие пользователи могут видеть примерное расстояние в метрах или километрах до вас."
        AppLanguage.PORTUGUESE -> "Outros usuários podem ver uma distância estimada em metros ou quilômetros de você."
        AppLanguage.INDONESIAN -> "Pengguna lain dapat melihat perkiraan jarak meter atau kilometer dari lokasi Anda."
        else -> "Other users can see an estimated distance in meters or kilometers from you."
    }

    fun privacyLocationPermissionTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "设备 GPS 与位置权限"
        AppLanguage.JAPANESE -> "端末のGPS・位置情報権限"
        AppLanguage.KOREAN -> "기기 GPS 및 위치 권한"
        AppLanguage.ARABIC -> "إذن موقع GPS للجهاز"
        AppLanguage.SPANISH -> "Permiso de GPS y Ubicación del Dispositivo"
        AppLanguage.FRENCH -> "Permission de localisation et GPS"
        AppLanguage.GERMAN -> "GPS- & Standortberechtigung des Geräts"
        AppLanguage.RUSSIAN -> "Разрешение на местоположение и GPS"
        AppLanguage.PORTUGUESE -> "Permissão de GPS e Localização do Dispositivo"
        AppLanguage.INDONESIAN -> "Izin Lokasi & GPS Perangkat"
        else -> "Device GPS & Location Permission"
    }

    fun privacyLocationPermissionGranted(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "已授予 GPS 权限 • 启用"
        AppLanguage.JAPANESE -> "GPS権限許可済み • 有効"
        AppLanguage.KOREAN -> "GPS 권한 허용됨 • 활성"
        AppLanguage.ARABIC -> "تم منح إذن GPS • نشط"
        AppLanguage.SPANISH -> "Permiso de GPS concedido • Activo"
        AppLanguage.FRENCH -> "Permission GPS accordée • Actif"
        AppLanguage.GERMAN -> "GPS-Berechtigung erteilt • Aktiv"
        AppLanguage.RUSSIAN -> "Разрешение GPS предоставлено • Активно"
        AppLanguage.PORTUGUESE -> "Permissão de GPS concedida • Ativo"
        AppLanguage.INDONESIAN -> "Izin GPS diberikan • Aktif"
        else -> "GPS permission granted • Active"
    }

    fun privacyLocationPermissionDenied(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "未授予位置权限"
        AppLanguage.JAPANESE -> "位置情報権限が許可されていません"
        AppLanguage.KOREAN -> "위치 권한이 허용되지 않음"
        AppLanguage.ARABIC -> "لم يتم منح إذن الموقع"
        AppLanguage.SPANISH -> "Permiso de ubicación no concedido"
        AppLanguage.FRENCH -> "Permission de localisation non accordée"
        AppLanguage.GERMAN -> "Standortberechtigung nicht erteilt"
        AppLanguage.RUSSIAN -> "Разрешение на местоположение не предоставлено"
        AppLanguage.PORTUGUESE -> "Permissão de localização não concedida"
        AppLanguage.INDONESIAN -> "Izin lokasi belum diberikan"
        else -> "Location permission not granted"
    }

    fun privacyUpdatedToast(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "隐私与位置设置已更新"
        AppLanguage.JAPANESE -> "プライバシーと位置情報の設定を更新しました"
        AppLanguage.KOREAN -> "개인정보 및 위치 설정이 업데이트되었습니다"
        AppLanguage.ARABIC -> "تم تحديث إعدادات الخصوصية والموقع"
        AppLanguage.SPANISH -> "Ajustes de privacidad y ubicación actualizados"
        AppLanguage.FRENCH -> "Paramètres de confidentialité et de localisation mis à jour"
        AppLanguage.GERMAN -> "Datenschutz- und Standorteinstellungen aktualisiert"
        AppLanguage.RUSSIAN -> "Настройки конфиденциальности и местоположения обновлены"
        AppLanguage.PORTUGUESE -> "Configurações de privacidade e localização atualizadas"
        AppLanguage.INDONESIAN -> "Pengaturan privasi & lokasi diperbarui"
        else -> "Privacy and location settings updated"
    }

    fun momentsDeletedToast(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "动态已成功删除"
        AppLanguage.JAPANESE -> "モーメントを削除しました"
        AppLanguage.KOREAN -> "모먼트가 삭제되었습니다"
        AppLanguage.ARABIC -> "تم حذف المنشور بنجاح"
        AppLanguage.SPANISH -> "Momento eliminado con éxito"
        AppLanguage.FRENCH -> "Moment supprimé avec succès"
        AppLanguage.GERMAN -> "Moment erfolgreich gelöscht"
        AppLanguage.RUSSIAN -> "Момент успешно удален"
        AppLanguage.PORTUGUESE -> "Momento excluído com sucesso"
        AppLanguage.INDONESIAN -> "Momen berhasil dihapus"
        else -> "Moment deleted successfully"
    }

    fun momentsDeleteOnlyAuthor(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "仅动态发布者可以删除此内容"
        AppLanguage.JAPANESE -> "このモーメントを削除できるのは投稿者のみです"
        AppLanguage.KOREAN -> "작성자만 이 모먼트를 삭제할 수 있습니다"
        AppLanguage.ARABIC -> "يمكن للكاتب فقط حذف هذا المنشور"
        AppLanguage.SPANISH -> "Solo el autor puede eliminar este momento"
        AppLanguage.FRENCH -> "Seul l'auteur peut supprimer ce moment"
        AppLanguage.GERMAN -> "Nur der Autor kann diesen Moment löschen"
        AppLanguage.RUSSIAN -> "Только автор может удалить этот момент"
        AppLanguage.PORTUGUESE -> "Apenas o autor pode excluir este momento"
        AppLanguage.INDONESIAN -> "Hanya pembuat momen yang dapat menghapus postingan ini"
        else -> "Only the author can delete this moment"
    }

    fun profilePhotoClearedToast(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "头像已清除，现使用 Lovy Chat 图标作为头像。"
        AppLanguage.JAPANESE -> "プロフィール写真をリセットしました。Lovy Chatロゴが使用されます。"
        AppLanguage.KOREAN -> "프로필 사진이 초기화되었습니다. Lovy Chat 로고가 프로필로 사용됩니다."
        AppLanguage.ARABIC -> "تمت إزالة صورة الملف الشخصي. شعار Lovy Chat أصبح صورتك الشخصية."
        AppLanguage.SPANISH -> "Foto de perfil eliminada. El logo de Lovy Chat es ahora tu foto de perfil."
        AppLanguage.FRENCH -> "Photo de profil supprimée. Le logo Lovy Chat est maintenant votre photo de profil."
        AppLanguage.GERMAN -> "Profilbild zurückgesetzt. Das Lovy Chat Logo ist nun dein Profilbild."
        AppLanguage.RUSSIAN -> "Фото профиля удалено. Логотип Lovy Chat теперь ваше фото профиля."
        AppLanguage.PORTUGUESE -> "Foto de perfil removida. O logotipo do Lovy Chat agora é sua foto de perfil."
        AppLanguage.INDONESIAN -> "Foto profil dikosongkan. Logo Lovy Chat aktif sebagai foto profil Anda."
        else -> "Profile picture cleared. Lovy Chat logo is now your profile picture."
    }

    fun profileClearPhotoOption(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "清除头像（使用 Lovy Chat 图标）"
        AppLanguage.JAPANESE -> "リセット（Lovy Chatロゴを使用）"
        AppLanguage.KOREAN -> "사진 지우기 (Lovy Chat 로고 사용)"
        AppLanguage.ARABIC -> "إزالة الصورة (استخدام شعار Lovy Chat)"
        AppLanguage.SPANISH -> "Quitar Foto (Usar Logo de Lovy Chat)"
        AppLanguage.FRENCH -> "Effacer la photo (utiliser le logo Lovy Chat)"
        AppLanguage.GERMAN -> "Foto löschen (Lovy Chat Logo verwenden)"
        AppLanguage.RUSSIAN -> "Удалить фото (использовать логотип Lovy Chat)"
        AppLanguage.PORTUGUESE -> "Remover Foto (Usar Logo do Lovy Chat)"
        AppLanguage.INDONESIAN -> "Kosongkan (Gunakan Logo Lovy Chat)"
        else -> "Clear Photo (Use Lovy Chat Logo)"
    }

    fun chatCannotOpenLink(lang: AppLanguage, url: String): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "无法打开链接: $url"
        AppLanguage.JAPANESE -> "リンクを開けません: $url"
        AppLanguage.KOREAN -> "링크를 열 수 없습니다: $url"
        AppLanguage.ARABIC -> "تعذر فتح الرابط: $url"
        AppLanguage.SPANISH -> "No se puede abrir el enlace: $url"
        AppLanguage.FRENCH -> "Impossible d'ouvrir le lien : $url"
        AppLanguage.GERMAN -> "Link kann nicht geöffnet werden: $url"
        AppLanguage.RUSSIAN -> "Не удалось открыть ссылку: $url"
        AppLanguage.PORTUGUESE -> "Não foi possível abrir o link: $url"
        AppLanguage.INDONESIAN -> "Tidak dapat membuka tautan: $url"
        else -> "Cannot open link: $url"
    }

    // --- USER PROFILE BOTTOM SHEET ---
    fun userProfileSheetTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "用户资料"
        AppLanguage.JAPANESE -> "ユーザープロフィール"
        AppLanguage.KOREAN -> "사용자 프로필"
        AppLanguage.ARABIC -> "الملف الشخصي للمستخدم"
        AppLanguage.SPANISH -> "Perfil de Usuario"
        AppLanguage.FRENCH -> "Profil de l'utilisateur"
        AppLanguage.GERMAN -> "Benutzerprofil"
        AppLanguage.RUSSIAN -> "Профиль пользователя"
        AppLanguage.PORTUGUESE -> "Perfil do Usuário"
        AppLanguage.INDONESIAN -> "Profil Pengguna"
        else -> "User Profile"
    }

    fun userProfileBlockedBanner(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "此用户在您的黑名单中。"
        AppLanguage.JAPANESE -> "このユーザーはブロックリストに入っています。"
        AppLanguage.KOREAN -> "이 사용자는 차단 목록에 있습니다."
        AppLanguage.ARABIC -> "هذا المستخدم في قائمة الحظر الخاصة بك."
        AppLanguage.SPANISH -> "Este usuario está en tu lista de bloqueados."
        AppLanguage.FRENCH -> "Cet utilisateur est sur votre liste de blocage."
        AppLanguage.GERMAN -> "Dieser Benutzer ist auf deiner Blockierliste."
        AppLanguage.RUSSIAN -> "Этот пользователь находится в черном списке."
        AppLanguage.PORTUGUESE -> "Este usuário está na sua lista de bloqueados."
        AppLanguage.INDONESIAN -> "Pengguna ini berada dalam daftar blokir Anda."
        else -> "This user is on your block list."
    }

    fun statusOnline(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "● 在线"
        AppLanguage.JAPANESE -> "● オンライン"
        AppLanguage.KOREAN -> "● 온라인"
        AppLanguage.ARABIC -> "● متصل"
        AppLanguage.SPANISH -> "● En línea"
        AppLanguage.FRENCH -> "● En ligne"
        AppLanguage.GERMAN -> "● Online"
        AppLanguage.RUSSIAN -> "● В сети"
        AppLanguage.PORTUGUESE -> "● Online"
        AppLanguage.INDONESIAN -> "● Online"
        else -> "● Online"
    }

    fun statusOffline(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "离线"
        AppLanguage.JAPANESE -> "オフライン"
        AppLanguage.KOREAN -> "오프라인"
        AppLanguage.ARABIC -> "غير متصل"
        AppLanguage.SPANISH -> "Desconectado"
        AppLanguage.FRENCH -> "Hors ligne"
        AppLanguage.GERMAN -> "Offline"
        AppLanguage.RUSSIAN -> "Не в сети"
        AppLanguage.PORTUGUESE -> "Desconectado"
        AppLanguage.INDONESIAN -> "Offline"
        else -> "Offline"
    }

    fun userProfileStartChat(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "打招呼并开始聊天 👋"
        AppLanguage.JAPANESE -> "挨拶してチャット開始 👋"
        AppLanguage.KOREAN -> "인사하고 대화 시작하기 👋"
        AppLanguage.ARABIC -> "إلقاء التحية وبدء المحادثة 👋"
        AppLanguage.SPANISH -> "Saludar e Iniciar Chat 👋"
        AppLanguage.FRENCH -> "Dire bonjour & discuter 👋"
        AppLanguage.GERMAN -> "Grüßen & Chat starten 👋"
        AppLanguage.RUSSIAN -> "Поздороваться и начать чат 👋"
        AppLanguage.PORTUGUESE -> "Dar Olá e Iniciar Chat 👋"
        AppLanguage.INDONESIAN -> "Sapa & Mulai Chat 👋"
        else -> "Say Hi & Start Chat 👋"
    }

    fun userProfileRecentMoments(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "最新动态照片"
        AppLanguage.JAPANESE -> "最近のモーメント写真"
        AppLanguage.KOREAN -> "최근 모먼트 사진"
        AppLanguage.ARABIC -> "أحدث صور اللحظات"
        AppLanguage.SPANISH -> "Fotos de Momentos Recientes"
        AppLanguage.FRENCH -> "Photos de moments récents"
        AppLanguage.GERMAN -> "Neueste Moment-Fotos"
        AppLanguage.RUSSIAN -> "Свежие фото моментов"
        AppLanguage.PORTUGUESE -> "Fotos de Momentos Recentes"
        AppLanguage.INDONESIAN -> "Foto Momen Terbaru"
        else -> "Recent Moment Photos"
    }

    fun userProfileMomentsCount(lang: AppLanguage, count: Int): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "$count 张动态照片"
        AppLanguage.JAPANESE -> "$count 枚のモーメント写真"
        AppLanguage.KOREAN -> "$count 장의 모먼트 사진"
        AppLanguage.ARABIC -> "$count صورة لحظة"
        AppLanguage.SPANISH -> "$count fotos de momentos"
        AppLanguage.FRENCH -> "$count photos de moments"
        AppLanguage.GERMAN -> "$count Moment-Fotos"
        AppLanguage.RUSSIAN -> "$count фото моментов"
        AppLanguage.PORTUGUESE -> "$count fotos de momentos"
        AppLanguage.INDONESIAN -> "$count Foto Momen"
        else -> "$count Moment Photos"
    }

    fun userProfileNoMoments(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "暂无动态"
        AppLanguage.JAPANESE -> "モーメントがまだありません"
        AppLanguage.KOREAN -> "아직 모먼트가 없습니다"
        AppLanguage.ARABIC -> "لا توجد لحظات بعد"
        AppLanguage.SPANISH -> "Aún no hay momentos"
        AppLanguage.FRENCH -> "Aucun moment pour le moment"
        AppLanguage.GERMAN -> "Noch keine Momente"
        AppLanguage.RUSSIAN -> "Пока нет моментов"
        AppLanguage.PORTUGUESE -> "Ainda não há momentos"
        AppLanguage.INDONESIAN -> "Belum Ada Momen"
        else -> "No Moments Yet"
    }

    fun userProfileNoMomentsDesc(lang: AppLanguage, name: String): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "$name 还没有分享过照片或动态故事。"
        AppLanguage.JAPANESE -> "$name はまだモーメント写真や投稿をシェアしていません。"
        AppLanguage.KOREAN -> "$name 님이 아직 모먼트 사진이나 이야기를 공유하지 않았습니다."
        AppLanguage.ARABIC -> "لم يشارك $name أي صور أو قصص للحظات بعد."
        AppLanguage.SPANISH -> "$name aún no ha compartido fotos ni historias de momentos."
        AppLanguage.FRENCH -> "$name n'a pas encore partagé de photos ou d'histoires."
        AppLanguage.GERMAN -> "$name hat noch keine Moment-Fotos oder Geschichten geteilt."
        AppLanguage.RUSSIAN -> "$name еще не делился(ась) фотографиями или историями моментов."
        AppLanguage.PORTUGUESE -> "$name ainda não compartilhou fotos ou histórias de momentos."
        AppLanguage.INDONESIAN -> "$name belum membagikan foto atau cerita momen."
        else -> "$name hasn't shared any moment photos or stories yet."
    }

    fun userProfileUnblockBtn(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "解除用户拉黑"
        AppLanguage.JAPANESE -> "ブロックを解除"
        AppLanguage.KOREAN -> "사용자 차단 해제"
        AppLanguage.ARABIC -> "إلغاء حظر المستخدم"
        AppLanguage.SPANISH -> "Desbloquear usuario"
        AppLanguage.FRENCH -> "Débloquer l'utilisateur"
        AppLanguage.GERMAN -> "Benutzer freigeben"
        AppLanguage.RUSSIAN -> "Разблокировать пользователя"
        AppLanguage.PORTUGUESE -> "Desbloquear Usuário"
        AppLanguage.INDONESIAN -> "Buka Blokir Pengguna"
        else -> "Unblock User"
    }

    fun userProfileBlockBtn(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "拉黑此用户"
        AppLanguage.JAPANESE -> "このユーザーをブロック"
        AppLanguage.KOREAN -> "이 사용자 차단하기"
        AppLanguage.ARABIC -> "حظر هذا المستخدم"
        AppLanguage.SPANISH -> "Bloquear a este usuario"
        AppLanguage.FRENCH -> "Bloquer cet utilisateur"
        AppLanguage.GERMAN -> "Diesen Benutzer blockieren"
        AppLanguage.RUSSIAN -> "Заблокировать пользователя"
        AppLanguage.PORTUGUESE -> "Bloquear este usuário"
        AppLanguage.INDONESIAN -> "Blokir Pengguna Ini"
        else -> "Block This User"
    }

    fun userProfileReportBtn(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "举报此用户"
        AppLanguage.JAPANESE -> "このユーザーを通報"
        AppLanguage.KOREAN -> "이 사용자 신고하기"
        AppLanguage.ARABIC -> "إبلاغ عن هذا المستخدم"
        AppLanguage.SPANISH -> "Denunciar a este usuario"
        AppLanguage.FRENCH -> "Signaler cet utilisateur"
        AppLanguage.GERMAN -> "Diesen Benutzer melden"
        AppLanguage.RUSSIAN -> "Пожаловаться на пользователя"
        AppLanguage.PORTUGUESE -> "Denunciar este usuário"
        AppLanguage.INDONESIAN -> "Laporkan Pengguna Ini"
        else -> "Report This User"
    }

    fun userProfileBlockConfirmTitle(lang: AppLanguage, name: String): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "拉黑 $name？"
        AppLanguage.JAPANESE -> "「$name」をブロックしますか？"
        AppLanguage.KOREAN -> "$name 님을 차단하시겠습니까?"
        AppLanguage.ARABIC -> "حظر $name؟"
        AppLanguage.SPANISH -> "¿Bloquear a $name?"
        AppLanguage.FRENCH -> "Bloquer $name ?"
        AppLanguage.GERMAN -> "$name blockieren?"
        AppLanguage.RUSSIAN -> "Заблокировать $name?"
        AppLanguage.PORTUGUESE -> "Bloquear $name?"
        AppLanguage.INDONESIAN -> "Blokir $name?"
        else -> "Block $name?"
    }

    fun userProfileBlockConfirmDesc(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "拉黑后此用户将无法向您发送消息，也不会出现在附近雷达中。"
        AppLanguage.JAPANESE -> "ブロックすると、このユーザーはメッセージを送信できなくなり、レーダーにも表示されなくなります。"
        AppLanguage.KOREAN -> "이 사용자는 더 이상 메시지를 보낼 수 없으며 주변 레이더에도 나타나지 않습니다."
        AppLanguage.ARABIC -> "لن يتمكن هذا المستخدم من إرسال رسائل ولن يظهر في رادار الأشخاص القريبين منك."
        AppLanguage.SPANISH -> "Este usuario ya no podrá enviarte mensajes y no aparecerá en tu radar cercano."
        AppLanguage.FRENCH -> "Cet utilisateur ne pourra plus envoyer de messages et n'apparaîtra plus sur votre radar."
        AppLanguage.GERMAN -> "Dieser Benutzer kann keine Nachrichten mehr senden und wird nicht mehr auf deinem Radar angezeigt."
        AppLanguage.RUSSIAN -> "Этот пользователь не сможет отправлять вам сообщения и не появится на радаре поблизости."
        AppLanguage.PORTUGUESE -> "Este usuário não poderá mais enviar mensagens e não aparecerá no seu radar próximo."
        AppLanguage.INDONESIAN -> "Pengguna ini tidak akan dapat mengirim pesan lagi dan tidak akan muncul di radar sekitar Anda."
        else -> "This user will no longer be able to send messages and won't appear on your nearby radar."
    }

    fun commonOpen(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "打开"
        AppLanguage.JAPANESE -> "開く"
        AppLanguage.KOREAN -> "열기"
        AppLanguage.ARABIC -> "فتح"
        AppLanguage.SPANISH -> "Abrir"
        AppLanguage.FRENCH -> "Ouvrir"
        AppLanguage.GERMAN -> "Öffnen"
        AppLanguage.RUSSIAN -> "Открыть"
        AppLanguage.PORTUGUESE -> "Abrir"
        AppLanguage.INDONESIAN -> "Buka"
        else -> "Open"
    }

    fun commonLike(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "赞"
        AppLanguage.JAPANESE -> "いいね"
        AppLanguage.KOREAN -> "좋아요"
        AppLanguage.ARABIC -> "إعجاب"
        AppLanguage.SPANISH -> "Me gusta"
        AppLanguage.FRENCH -> "J'aime"
        AppLanguage.GERMAN -> "Gefällt mir"
        AppLanguage.RUSSIAN -> "Нравится"
        AppLanguage.PORTUGUESE -> "Curtir"
        AppLanguage.INDONESIAN -> "Suka"
        else -> "Like"
    }

    fun commonComment(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "评论"
        AppLanguage.JAPANESE -> "コメント"
        AppLanguage.KOREAN -> "댓글"
        AppLanguage.ARABIC -> "تعليق"
        AppLanguage.SPANISH -> "Comentar"
        AppLanguage.FRENCH -> "Commentaire"
        AppLanguage.GERMAN -> "Kommentar"
        AppLanguage.RUSSIAN -> "Комментарий"
        AppLanguage.PORTUGUESE -> "Comentar"
        AppLanguage.INDONESIAN -> "Komentar"
        else -> "Comment"
    }

    // --- USER PROFILE EDIT & DETAIL SCREEN ---
    fun profileDetailTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "用户个人资料"
        AppLanguage.JAPANESE -> "プロフィール詳細"
        AppLanguage.KOREAN -> "사용자 프로필 상세"
        AppLanguage.ARABIC -> "تفاصيل الملف الشخصي"
        AppLanguage.SPANISH -> "Detalles del Perfil"
        AppLanguage.FRENCH -> "Détails du profil"
        AppLanguage.GERMAN -> "Benutzerprofil-Details"
        AppLanguage.RUSSIAN -> "Детали профиля"
        AppLanguage.PORTUGUESE -> "Detalhes do Perfil"
        AppLanguage.INDONESIAN -> "Detail Profil Pengguna"
        else -> "User Profile Details"
    }

    fun profileQrCodeDesc(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "我的个人二维码"
        AppLanguage.JAPANESE -> "マイプロフィールQRコード"
        AppLanguage.KOREAN -> "내 프로필 QR 코드"
        AppLanguage.ARABIC -> "رمز QR لملفي الشخصي"
        AppLanguage.SPANISH -> "Código QR de Mi Perfil"
        AppLanguage.FRENCH -> "Mon QR code de profil"
        AppLanguage.GERMAN -> "Mein Profil-QR-Code"
        AppLanguage.RUSSIAN -> "QR-код моего профиля"
        AppLanguage.PORTUGUESE -> "Código QR do Meu Perfil"
        AppLanguage.INDONESIAN -> "Kode QR Profil Saya"
        else -> "My Profile QR Code"
    }

    fun profileEditButton(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "编辑资料"
        AppLanguage.JAPANESE -> "プロフィール編集"
        AppLanguage.KOREAN -> "프로필 수정"
        AppLanguage.ARABIC -> "تعديل الملف الشخصي"
        AppLanguage.SPANISH -> "Editar Perfil"
        AppLanguage.FRENCH -> "Modifier le profil"
        AppLanguage.GERMAN -> "Profil bearbeiten"
        AppLanguage.RUSSIAN -> "Редактировать профиль"
        AppLanguage.PORTUGUESE -> "Editar Perfil"
        AppLanguage.INDONESIAN -> "Edit Profil"
        else -> "Edit Profile"
    }

    fun profileChangePhoto(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "更换头像"
        AppLanguage.JAPANESE -> "写真を変更"
        AppLanguage.KOREAN -> "사진 변경"
        AppLanguage.ARABIC -> "تغيير الصورة"
        AppLanguage.SPANISH -> "Cambiar Foto"
        AppLanguage.FRENCH -> "Changer de photo"
        AppLanguage.GERMAN -> "Foto ändern"
        AppLanguage.RUSSIAN -> "Сменить фото"
        AppLanguage.PORTUGUESE -> "Mudar Foto"
        AppLanguage.INDONESIAN -> "Ubah Foto"
        else -> "Change Photo"
    }

    fun profileAboutMe(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "关于我（个性签名）"
        AppLanguage.JAPANESE -> "自己紹介（バイオ）"
        AppLanguage.KOREAN -> "자기소개 (소개글)"
        AppLanguage.ARABIC -> "نبذة عني (السيرة)"
        AppLanguage.SPANISH -> "Sobre Mí (Biografía)"
        AppLanguage.FRENCH -> "À propos de moi (Bio)"
        AppLanguage.GERMAN -> "Über mich (Bio)"
        AppLanguage.RUSSIAN -> "О себе (Статус)"
        AppLanguage.PORTUGUESE -> "Sobre Mim (Biografia)"
        AppLanguage.INDONESIAN -> "Tentang Saya (Bio)"
        else -> "About Me (Bio)"
    }

    fun profileEmptyBioHint(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "暂未填写个性签名。点击编辑按钮添加简介。"
        AppLanguage.JAPANESE -> "まだ自己紹介が書かれていません。編集ボタンを押して追加しましょう。"
        AppLanguage.KOREAN -> "작성된 소개글이 없습니다. 수정 버튼을 눌러 추가해보세요."
        AppLanguage.ARABIC -> "لم تتم كتابة نبذة شخصية بعد. انقر على زر التعديل لإضافة نبذة."
        AppLanguage.SPANISH -> "Aún no hay biografía escrita. Toca el botón editar para agregar una."
        AppLanguage.FRENCH -> "Aucune bio écrite pour le moment. Appuyez sur modifier pour en ajouter une."
        AppLanguage.GERMAN -> "Noch keine Biografie vorhanden. Tippe auf Bearbeiten, um eine hinzuzufügen."
        AppLanguage.RUSSIAN -> "Статус еще не написан. Нажмите редактировать, чтобы добавить информацию."
        AppLanguage.PORTUGUESE -> "Nenhuma biografia escrita ainda. Toque em editar para adicionar uma."
        AppLanguage.INDONESIAN -> "Belum ada bio yang ditulis. Ketuk tombol edit untuk menambahkan bio."
        else -> "No bio written yet. Tap edit to add a bio."
    }

    fun profileAccountInfo(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "账户信息"
        AppLanguage.JAPANESE -> "アカウント情報"
        AppLanguage.KOREAN -> "계정 정보"
        AppLanguage.ARABIC -> "معلومات الحساب"
        AppLanguage.SPANISH -> "Información de la Cuenta"
        AppLanguage.FRENCH -> "Informations du compte"
        AppLanguage.GERMAN -> "Kontoinformationen"
        AppLanguage.RUSSIAN -> "Информация об аккаунте"
        AppLanguage.PORTUGUESE -> "Informações da Conta"
        AppLanguage.INDONESIAN -> "Informasi Akun"
        else -> "Account Information"
    }

    fun profileCityDomicile(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "常住城市"
        AppLanguage.JAPANESE -> "居住都市"
        AppLanguage.KOREAN -> "거주 도시"
        AppLanguage.ARABIC -> "مدينة الإقامة"
        AppLanguage.SPANISH -> "Ciudad de Residencia"
        AppLanguage.FRENCH -> "Ville de résidence"
        AppLanguage.GERMAN -> "Wohnort"
        AppLanguage.RUSSIAN -> "Город проживания"
        AppLanguage.PORTUGUESE -> "Cidade de Residência"
        AppLanguage.INDONESIAN -> "Kota Domisili"
        else -> "City of Domicile"
    }

    fun profileAutoGps(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "自动定位"
        AppLanguage.JAPANESE -> "地図自動"
        AppLanguage.KOREAN -> "지도 자동"
        AppLanguage.ARABIC -> "تحديد تلقائي"
        AppLanguage.SPANISH -> "GPS Automático"
        AppLanguage.FRENCH -> "GPS automatique"
        AppLanguage.GERMAN -> "Karten-Automatik"
        AppLanguage.RUSSIAN -> "Авто-GPS"
        AppLanguage.PORTUGUESE -> "GPS Automático"
        AppLanguage.INDONESIAN -> "Otomatis Peta"
        else -> "Auto GPS"
    }

    fun profileGpsDetecting(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "正在检测 GPS 位置..."
        AppLanguage.JAPANESE -> "GPS位置情報を検出中..."
        AppLanguage.KOREAN -> "GPS 위치 감지 중..."
        AppLanguage.ARABIC -> "جارٍ تحديد موقع GPS..."
        AppLanguage.SPANISH -> "Detectando ubicación GPS..."
        AppLanguage.FRENCH -> "Détection de la position GPS..."
        AppLanguage.GERMAN -> "GPS-Standort wird ermittelt..."
        AppLanguage.RUSSIAN -> "Определение GPS-координат..."
        AppLanguage.PORTUGUESE -> "Detectando localização GPS..."
        AppLanguage.INDONESIAN -> "Mendeteksi posisi GPS..."
        else -> "Detecting GPS location..."
    }

    fun profileCityHint(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "其他用户将在雷达和聊天中看到此信息"
        AppLanguage.JAPANESE -> "レーダーやチャットで他のユーザーに表示されます"
        AppLanguage.KOREAN -> "레이더 및 대화에서 다른 사용자에게 표시됩니다"
        AppLanguage.ARABIC -> "مرئي للمستخدمين الآخرين في الرادار والمحادثات"
        AppLanguage.SPANISH -> "Visible para otros usuarios en radar y chats"
        AppLanguage.FRENCH -> "Visible par les autres utilisateurs sur le radar & les chats"
        AppLanguage.GERMAN -> "Sichtbar für andere Benutzer auf Radar und in Chats"
        AppLanguage.RUSSIAN -> "Видно другим пользователям на радаре и в чатах"
        AppLanguage.PORTUGUESE -> "Visível para outros usuários no radar e conversas"
        AppLanguage.INDONESIAN -> "Dilihat oleh pengguna lain di radar & obrolan"
        else -> "Visible to other users on radar & chats"
    }

    fun profileSyncGps(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "同步地图位置"
        AppLanguage.JAPANESE -> "地図の位置を同期"
        AppLanguage.KOREAN -> "지도 위치 동기화"
        AppLanguage.ARABIC -> "مزامنة موقع الخريطة"
        AppLanguage.SPANISH -> "Sincronizar Ubicación de Mapa"
        AppLanguage.FRENCH -> "Synchroniser la position"
        AppLanguage.GERMAN -> "Standort synchronisieren"
        AppLanguage.RUSSIAN -> "Синхронизировать координаты"
        AppLanguage.PORTUGUESE -> "Sincronizar Localização do Mapa"
        AppLanguage.INDONESIAN -> "Sinkronkan Lokasi Peta"
        else -> "Sync Map Location"
    }

    fun profileGenderAge(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "性别与年龄"
        AppLanguage.JAPANESE -> "性別 & 年齢"
        AppLanguage.KOREAN -> "성별 & 나이"
        AppLanguage.ARABIC -> "الجنس والعمر"
        AppLanguage.SPANISH -> "Género y Edad"
        AppLanguage.FRENCH -> "Sexe & Âge"
        AppLanguage.GERMAN -> "Geschlecht & Alter"
        AppLanguage.RUSSIAN -> "Пол и возраст"
        AppLanguage.PORTUGUESE -> "Gênero e Idade"
        AppLanguage.INDONESIAN -> "Jenis Kelamin & Usia"
        else -> "Gender & Age"
    }

    fun profileEmail(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "账户邮箱"
        AppLanguage.JAPANESE -> "アカウントメール"
        AppLanguage.KOREAN -> "계정 이메일"
        AppLanguage.ARABIC -> "البريد الإلكتروني للحساب"
        AppLanguage.SPANISH -> "Correo de la Cuenta"
        AppLanguage.FRENCH -> "E-mail du compte"
        AppLanguage.GERMAN -> "Konto-E-Mail"
        AppLanguage.RUSSIAN -> "Электронная почта"
        AppLanguage.PORTUGUESE -> "E-mail da Conta"
        AppLanguage.INDONESIAN -> "Email Akun"
        else -> "Account Email"
    }

    fun profileNotConnected(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "未连接"
        AppLanguage.JAPANESE -> "未連携"
        AppLanguage.KOREAN -> "연결되지 않음"
        AppLanguage.ARABIC -> "غير متصل"
        AppLanguage.SPANISH -> "No conectado"
        AppLanguage.FRENCH -> "Non connecté"
        AppLanguage.GERMAN -> "Nicht verbunden"
        AppLanguage.RUSSIAN -> "Не подключен"
        AppLanguage.PORTUGUESE -> "Não conectado"
        AppLanguage.INDONESIAN -> "Belum terhubung"
        else -> "Not connected"
    }

    fun profileLovyId(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "唯一 Lovy ID"
        AppLanguage.JAPANESE -> "固有のLovy ID"
        AppLanguage.KOREAN -> "고유 Lovy ID"
        AppLanguage.ARABIC -> "معرف Lovy الفريد"
        AppLanguage.SPANISH -> "Lovy ID Único"
        AppLanguage.FRENCH -> "ID Lovy Unique"
        AppLanguage.GERMAN -> "Eindeutige Lovy-ID"
        AppLanguage.RUSSIAN -> "Уникальный Lovy ID"
        AppLanguage.PORTUGUESE -> "Lovy ID Único"
        AppLanguage.INDONESIAN -> "Lovy ID Unik"
        else -> "Unique Lovy ID"
    }

    fun profileEditDialogTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "编辑个人资料"
        AppLanguage.JAPANESE -> "プロフィール詳細を編集"
        AppLanguage.KOREAN -> "프로필 상세 수정"
        AppLanguage.ARABIC -> "تعديل تفاصيل الملف الشخصي"
        AppLanguage.SPANISH -> "Editar Detalles del Perfil"
        AppLanguage.FRENCH -> "Modifier les détails du profil"
        AppLanguage.GERMAN -> "Profildetails bearbeiten"
        AppLanguage.RUSSIAN -> "Редактировать профиль"
        AppLanguage.PORTUGUESE -> "Editar Detalhes do Perfil"
        AppLanguage.INDONESIAN -> "Edit Detail Profil"
        else -> "Edit Profile Details"
    }

    fun profilePhotoSection(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "个人头像"
        AppLanguage.JAPANESE -> "プロフィール写真"
        AppLanguage.KOREAN -> "프로필 사진"
        AppLanguage.ARABIC -> "صورة الملف الشخصي"
        AppLanguage.SPANISH -> "Foto de Perfil"
        AppLanguage.FRENCH -> "Photo de profil"
        AppLanguage.GERMAN -> "Profilbild"
        AppLanguage.RUSSIAN -> "Фото профиля"
        AppLanguage.PORTUGUESE -> "Foto de Perfil"
        AppLanguage.INDONESIAN -> "Foto Profil"
        else -> "Profile Photo"
    }

    fun profilePhotoSectionDesc(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "管理您的头像。您可以上传新照片，或清除头像以使用 Lovy Chat 官方图标作为默认头像。"
        AppLanguage.JAPANESE -> "プロフィール写真を管理します。新しい写真をアップロードするか、リセットしてLovy Chatの公式ロゴを使用できます。"
        AppLanguage.KOREAN -> "프로필 사진을 관리합니다. 새 사진을 업로드하거나 초기화하여 기본 Lovy Chat 로고를 사용할 수 있습니다."
        AppLanguage.ARABIC -> "إدارة صورتك الشخصية. يمكنك تحميل صورة جديدة أو إزالتها لاستخدام شعار Lovy Chat كافتراضي."
        AppLanguage.SPANISH -> "Administra tu foto de perfil. Sube una nueva o quítala para usar el logo oficial de Lovy Chat."
        AppLanguage.FRENCH -> "Gérez votre photo. Téléversez-en une nouvelle ou effacez-la pour utiliser le logo officiel Lovy Chat."
        AppLanguage.GERMAN -> "Verwalte dein Profilbild. Lade ein neues Foto hoch oder nutze das offizielle Lovy Chat Logo als Standard."
        AppLanguage.RUSSIAN -> "Управляйте фото профиля. Загрузите новое фото или используйте официальный логотип Lovy Chat."
        AppLanguage.PORTUGUESE -> "Gerencie sua foto de perfil. Carregue uma nova foto ou use o logotipo oficial do Lovy Chat."
        AppLanguage.INDONESIAN -> "Kelola foto profil Anda. Anda dapat mengunggah foto baru atau mengosongkan foto profil untuk menggunakan logo resmi Lovy Chat sebagai profil default."
        else -> "Manage your profile picture. Upload a new photo or clear it to use the official Lovy Chat logo as default."
    }

    fun profileViewZoomPhoto(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "查看并缩放头像（双指缩放）"
        AppLanguage.JAPANESE -> "写真を表示＆拡大（ピンチズーム）"
        AppLanguage.KOREAN -> "사진 보기 및 확대 (두 손가락 줌)"
        AppLanguage.ARABIC -> "عرض وتكبير الصورة (تكبير بإصبعين)"
        AppLanguage.SPANISH -> "Ver y Ampliar Foto (Zoom con 2 dedos)"
        AppLanguage.FRENCH -> "Afficher & agrandir la photo (zoom)"
        AppLanguage.GERMAN -> "Foto ansehen & vergrößern (Zweifinger-Zoom)"
        AppLanguage.RUSSIAN -> "Просмотр и масштабирование (зум 2 пальцами)"
        AppLanguage.PORTUGUESE -> "Ver e Ampliar Foto (Zoom com 2 dedos)"
        AppLanguage.INDONESIAN -> "Lihat & Perbesar Foto Profil (Zoom 2 Jari)"
        else -> "View & Enlarge Profile Photo (2-Finger Zoom)"
    }

    fun profilePickGalleryPhoto(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "从相册选择新照片"
        AppLanguage.JAPANESE -> "ギャラリーから新しい写真を選択"
        AppLanguage.KOREAN -> "갤러리에서 새 사진 선택"
        AppLanguage.ARABIC -> "اختيار صورة جديدة من المعرض"
        AppLanguage.SPANISH -> "Elegir Nueva Foto de la Galería"
        AppLanguage.FRENCH -> "Choisir une nouvelle photo de la galerie"
        AppLanguage.GERMAN -> "Neues Foto aus der Galerie wählen"
        AppLanguage.RUSSIAN -> "Выбрать новое фото из галереи"
        AppLanguage.PORTUGUESE -> "Escolher Nova Foto da Galeria"
        AppLanguage.INDONESIAN -> "Pilih Foto Baru dari Galeri"
        else -> "Choose New Photo from Gallery"
    }

    fun profileLogoActive(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "官方 Lovy Chat 图标已设为您的头像"
        AppLanguage.JAPANESE -> "公式Lovy Chatロゴがプロフィール写真として有効です"
        AppLanguage.KOREAN -> "공식 Lovy Chat 로고가 프로필 사진으로 설정되어 있습니다"
        AppLanguage.ARABIC -> "شعار Lovy Chat الرسمي مفعل كصورتك الشخصية"
        AppLanguage.SPANISH -> "El logo oficial de Lovy Chat está activo como tu foto de perfil"
        AppLanguage.FRENCH -> "Le logo officiel Lovy Chat est actif comme photo de profil"
        AppLanguage.GERMAN -> "Das offizielle Lovy Chat Logo ist als dein Profilbild aktiv"
        AppLanguage.RUSSIAN -> "Официальный логотип Lovy Chat активен как фото профиля"
        AppLanguage.PORTUGUESE -> "O logotipo oficial do Lovy Chat está ativo como sua foto de perfil"
        AppLanguage.INDONESIAN -> "Logo Resmi Lovy Chat aktif sebagai foto profil Anda"
        else -> "Official Lovy Chat logo is active as your profile picture"
    }

    fun profileDisplayNameLabel(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "显示名称 (Display Name)"
        AppLanguage.JAPANESE -> "表示名 (Display Name)"
        AppLanguage.KOREAN -> "표시 이름 (Display Name)"
        AppLanguage.ARABIC -> "اسم العرض (Display Name)"
        AppLanguage.SPANISH -> "Nombre para Mostrar"
        AppLanguage.FRENCH -> "Nom d'affichage"
        AppLanguage.GERMAN -> "Anzeigename"
        AppLanguage.RUSSIAN -> "Отображаемое имя"
        AppLanguage.PORTUGUESE -> "Nome de Exibição"
        AppLanguage.INDONESIAN -> "Nama Tampilan (Display Name)"
        else -> "Display Name"
    }

    fun profileEmailLabel(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "绑定邮箱 (Connected)"
        AppLanguage.JAPANESE -> "連携メールアドレス"
        AppLanguage.KOREAN -> "연결된 이메일"
        AppLanguage.ARABIC -> "البريد المتصل"
        AppLanguage.SPANISH -> "Correo Vinculado"
        AppLanguage.FRENCH -> "E-mail connecté"
        AppLanguage.GERMAN -> "Verknüpfte E-Mail"
        AppLanguage.RUSSIAN -> "Привязанная почта"
        AppLanguage.PORTUGUESE -> "E-mail Conectado"
        AppLanguage.INDONESIAN -> "Email Akun (Terkoneksi)"
        else -> "Connected Account Email"
    }

    fun profileGenderLabel(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "性别"
        AppLanguage.JAPANESE -> "性別"
        AppLanguage.KOREAN -> "성별"
        AppLanguage.ARABIC -> "الجنس"
        AppLanguage.SPANISH -> "Género"
        AppLanguage.FRENCH -> "Sexe"
        AppLanguage.GERMAN -> "Geschlecht"
        AppLanguage.RUSSIAN -> "Пол"
        AppLanguage.PORTUGUESE -> "Gênero"
        AppLanguage.INDONESIAN -> "Jenis Kelamin"
        else -> "Gender"
    }

    fun profileAgeLabel(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "年龄（周岁）"
        AppLanguage.JAPANESE -> "年齢（歳）"
        AppLanguage.KOREAN -> "나이 (세)"
        AppLanguage.ARABIC -> "العمر (بالسنوات)"
        AppLanguage.SPANISH -> "Edad (Años)"
        AppLanguage.FRENCH -> "Âge (Années)"
        AppLanguage.GERMAN -> "Alter (Jahre)"
        AppLanguage.RUSSIAN -> "Возраст (лет)"
        AppLanguage.PORTUGUESE -> "Idade (Anos)"
        AppLanguage.INDONESIAN -> "Usia (Tahun)"
        else -> "Age (Years)"
    }

    fun profileBioLabel(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "个性签名 / 简短状态"
        AppLanguage.JAPANESE -> "自己紹介 / 短いひとこと"
        AppLanguage.KOREAN -> "소개글 / 짧은 상태메시지"
        AppLanguage.ARABIC -> "النبذة / الحالة القصيرة"
        AppLanguage.SPANISH -> "Biografía / Estado Corto"
        AppLanguage.FRENCH -> "Bio / Statut court"
        AppLanguage.GERMAN -> "Bio / Kurzer Status"
        AppLanguage.RUSSIAN -> "Статус / О себе"
        AppLanguage.PORTUGUESE -> "Biografia / Status Curto"
        AppLanguage.INDONESIAN -> "Bio / Status Singkat"
        else -> "Bio / Short Status"
    }

    fun profileCityLabel(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "常住城市 / 区域位置"
        AppLanguage.JAPANESE -> "居住都市 / 地域"
        AppLanguage.KOREAN -> "거주 도시 / 지역"
        AppLanguage.ARABIC -> "مدينة الإقامة / الموقع"
        AppLanguage.SPANISH -> "Ciudad / Ubicación de Domicilio"
        AppLanguage.FRENCH -> "Ville / Lieu de domicile"
        AppLanguage.GERMAN -> "Stadt / Wohnort"
        AppLanguage.RUSSIAN -> "Город / Местоположение"
        AppLanguage.PORTUGUESE -> "Cidade / Localização de Domicílio"
        AppLanguage.INDONESIAN -> "Kota / Lokasi Domisili"
        else -> "City / Domicile Location"
    }

    fun profileUseGpsLocation(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "使用检测到的 GPS 位置"
        AppLanguage.JAPANESE -> "検出されたGPS位置を使用"
        AppLanguage.KOREAN -> "감지된 GPS 위치 사용"
        AppLanguage.ARABIC -> "استخدام موقع GPS المكتشف"
        AppLanguage.SPANISH -> "Usar Ubicación GPS Detectada"
        AppLanguage.FRENCH -> "Utiliser la position GPS détectée"
        AppLanguage.GERMAN -> "Ermittelten GPS-Standort nutzen"
        AppLanguage.RUSSIAN -> "Использовать определенный GPS"
        AppLanguage.PORTUGUESE -> "Usar Localização GPS Detectada"
        AppLanguage.INDONESIAN -> "Gunakan Lokasi GPS Terdeteksi"
        else -> "Use Detected GPS Location"
    }

    fun profileApplyGps(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "应用"
        AppLanguage.JAPANESE -> "適用"
        AppLanguage.KOREAN -> "적용"
        AppLanguage.ARABIC -> "تطبيق"
        AppLanguage.SPANISH -> "Aplicar"
        AppLanguage.FRENCH -> "Appliquer"
        AppLanguage.GERMAN -> "Anwenden"
        AppLanguage.RUSSIAN -> "Применить"
        AppLanguage.PORTUGUESE -> "Aplicar"
        AppLanguage.INDONESIAN -> "Terapkan"
        else -> "Apply"
    }

    fun profileGpsNotice(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "您的常住位置会根据地图 GPS 自动精确到区县和城市级别。"
        AppLanguage.JAPANESE -> "あなたの居住位置は地図GPSによって市区町村レベルまで自動検出されます。"
        AppLanguage.KOREAN -> "거주 위치는 지도 GPS를 통해 구/군 및 도시 수준까지 자동 감지됩니다."
        AppLanguage.ARABIC -> "يتم تحديد موقع إقامتك تلقائياً من خريطة GPS حتى مستوى المنطقة والمدينة."
        AppLanguage.SPANISH -> "Tu ubicación de domicilio se detecta automáticamente desde el GPS del mapa hasta el nivel de distrito y ciudad."
        AppLanguage.FRENCH -> "Votre domicile est automatiquement détecté à partir du GPS jusqu'au niveau du quartier et de la ville."
        AppLanguage.GERMAN -> "Dein Wohnort wird automatisch über das Karten-GPS bis auf Bezirks- und Stadtebene ermittelt."
        AppLanguage.RUSSIAN -> "Ваше местоположение автоматически определяется по GPS до уровня района и города."
        AppLanguage.PORTUGUESE -> "Sua localização de domicílio é detectada automaticamente do GPS do mapa até o nível de distrito e cidade."
        AppLanguage.INDONESIAN -> "Lokasi domisili Anda terdeteksi otomatis dari GPS peta hingga tingkat kecamatan & kota."
        else -> "Your domicile location is automatically detected from map GPS up to district & city level."
    }

    fun profileUpdatedSuccess(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "资料更新成功！"
        AppLanguage.JAPANESE -> "プロフィールを更新しました！"
        AppLanguage.KOREAN -> "프로필이 성공적으로 업데이트되었습니다!"
        AppLanguage.ARABIC -> "تم تحديث الملف الشخصي بنجاح!"
        AppLanguage.SPANISH -> "¡Perfil actualizado con éxito!"
        AppLanguage.FRENCH -> "Profil mis à jour avec succès !"
        AppLanguage.GERMAN -> "Profil erfolgreich aktualisiert!"
        AppLanguage.RUSSIAN -> "Профиль успешно обновлен!"
        AppLanguage.PORTUGUESE -> "Perfil atualizado com sucesso!"
        AppLanguage.INDONESIAN -> "Profil berhasil diperbarui!"
        else -> "Profile updated successfully!"
    }

    fun profileUpdateFailed(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "保存资料失败"
        AppLanguage.JAPANESE -> "プロフィールの保存に失敗しました"
        AppLanguage.KOREAN -> "프로필 저장에 실패했습니다"
        AppLanguage.ARABIC -> "فشل حفظ الملف الشخصي"
        AppLanguage.SPANISH -> "Error al guardar el perfil"
        AppLanguage.FRENCH -> "Échec de l'enregistrement du profil"
        AppLanguage.GERMAN -> "Profil konnte nicht gespeichert werden"
        AppLanguage.RUSSIAN -> "Не удалось сохранить профиль"
        AppLanguage.PORTUGUESE -> "Falha ao salvar perfil"
        AppLanguage.INDONESIAN -> "Gagal menyimpan profil"
        else -> "Failed to save profile"
    }

    // --- MOMENTS SCREEN ---
    fun momentsPhotoTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "动态照片"
        AppLanguage.JAPANESE -> "モーメント写真"
        AppLanguage.KOREAN -> "모먼트 사진"
        AppLanguage.ARABIC -> "صورة اللحظة"
        AppLanguage.SPANISH -> "Foto del Momento"
        AppLanguage.FRENCH -> "Photo du moment"
        AppLanguage.GERMAN -> "Moment-Foto"
        AppLanguage.RUSSIAN -> "Фото момента"
        AppLanguage.PORTUGUESE -> "Foto do Momento"
        AppLanguage.INDONESIAN -> "Foto Momen"
        else -> "Moment Photo"
    }

    fun momentsImageLoadFailed(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "图片加载失败"
        AppLanguage.JAPANESE -> "画像を読み込めませんでした"
        AppLanguage.KOREAN -> "이미지를 불러오지 못했습니다"
        AppLanguage.ARABIC -> "فشل تحميل الصورة"
        AppLanguage.SPANISH -> "Error al cargar la imagen"
        AppLanguage.FRENCH -> "Échec du chargement de l'image"
        AppLanguage.GERMAN -> "Bild konnte nicht geladen werden"
        AppLanguage.RUSSIAN -> "Не удалось загрузить фото"
        AppLanguage.PORTUGUESE -> "Falha ao carregar imagem"
        AppLanguage.INDONESIAN -> "Gagal memuat gambar"
        else -> "Failed to load image"
    }

    fun momentsDefaultMyBio(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "我在 Lovy Chat 的个人主页 ✨"
        AppLanguage.JAPANESE -> "Lovy Chatのマイプロフィール ✨"
        AppLanguage.KOREAN -> "Lovy Chat 프로필입니다 ✨"
        AppLanguage.ARABIC -> "ملفي الشخصي في Lovy Chat ✨"
        AppLanguage.SPANISH -> "Mi perfil en Lovy Chat ✨"
        AppLanguage.FRENCH -> "Mon profil sur Lovy Chat ✨"
        AppLanguage.GERMAN -> "Mein Profil auf Lovy Chat ✨"
        AppLanguage.RUSSIAN -> "Мой профиль в Lovy Chat ✨"
        AppLanguage.PORTUGUESE -> "Meu perfil no Lovy Chat ✨"
        AppLanguage.INDONESIAN -> "Profil saya di Lovy Chat ✨"
        else -> "My profile on Lovy Chat ✨"
    }

    fun momentsDefaultUserBio(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "你好！很高兴在 Lovy Chat 分享精彩动态和故事 ✨"
        AppLanguage.JAPANESE -> "こんにちは！Lovy Chatでモーメントや日常をシェアできて嬉しいです ✨"
        AppLanguage.KOREAN -> "안녕하세요! Lovy Chat에서 모먼트와 이야기를 함께 나눠요 ✨"
        AppLanguage.ARABIC -> "مرحباً! سعيد بمشاركة اللحظات والقصص الممتعة في Lovy Chat ✨"
        AppLanguage.SPANISH -> "¡Hola! Encantado de compartir momentos e historias divertidas en Lovy Chat ✨"
        AppLanguage.FRENCH -> "Bonjour ! Heureux de partager des moments et des histoires sur Lovy Chat ✨"
        AppLanguage.GERMAN -> "Hallo! Freue mich, tolle Momente auf Lovy Chat zu teilen ✨"
        AppLanguage.RUSSIAN -> "Привет! Рад делиться яркими моментами и историями в Lovy Chat ✨"
        AppLanguage.PORTUGUESE -> "Olá! Adoro compartilhar momentos e histórias no Lovy Chat ✨"
        AppLanguage.INDONESIAN -> "Halo! Senang bisa berbagi momen dan cerita seru di Lovy Chat ✨"
        else -> "Hello! Glad to share moments and fun stories on Lovy Chat ✨"
    }

    // --- NEARBY SCREEN ENHANCEMENTS ---
    fun nearbyOnlineOnlyFilter(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "🟢 仅看在线"
        AppLanguage.JAPANESE -> "🟢 オンラインのみ"
        AppLanguage.KOREAN -> "🟢 온라인만"
        AppLanguage.ARABIC -> "🟢 المتصلون فقط"
        AppLanguage.SPANISH -> "🟢 Solo en línea"
        AppLanguage.FRENCH -> "🟢 En ligne seulement"
        AppLanguage.GERMAN -> "🟢 Nur online"
        AppLanguage.RUSSIAN -> "🟢 Только в сети"
        AppLanguage.PORTUGUESE -> "🟢 Apenas online"
        AppLanguage.INDONESIAN -> "🟢 Hanya Online"
        else -> "🟢 Online Only"
    }

    fun nearbyUnlockMoreTitle(lang: AppLanguage, hiddenCount: Int, tier: Int, nextTargetLimit: Int): String {
        return when (resolveLang(lang)) {
            AppLanguage.CHINESE -> if (tier >= 4) "解锁全部附近好友 ($nextTargetLimit 人)" else "多解锁 $hiddenCount 位附近好友"
            AppLanguage.JAPANESE -> if (tier >= 4) "近くのユーザーを最大解放 ($nextTargetLimit 人)" else "近くの友達をあと $hiddenCount 人アンロック"
            AppLanguage.KOREAN -> if (tier >= 4) "주변 친구 최대 잠금 해제 ($nextTargetLimit 명)" else "주변 친구 $hiddenCount 명 더 잠금 해제"
            AppLanguage.ARABIC -> if (tier >= 4) "فتح الحد الأقصى للمستخدمين القريبين ($nextTargetLimit شخص)" else "فتح $hiddenCount صديق إضافي بالجوار"
            AppLanguage.SPANISH -> if (tier >= 4) "Desbloquear Máximo de Personas Cercanas ($nextTargetLimit)" else "Desbloquear $hiddenCount Amigos Cercanos Más"
            AppLanguage.FRENCH -> if (tier >= 4) "Débloquer le maximum de personnes proches ($nextTargetLimit)" else "Débloquer $hiddenCount amis de plus"
            AppLanguage.GERMAN -> if (tier >= 4) "Maximale Personen in der Nähe freischalten ($nextTargetLimit)" else "Noch $hiddenCount Freunde in der Nähe freischalten"
            AppLanguage.RUSSIAN -> if (tier >= 4) "Открыть максимум пользователей поблизости ($nextTargetLimit чел.)" else "Открыть еще $hiddenCount чел. поблизости"
            AppLanguage.PORTUGUESE -> if (tier >= 4) "Desbloquear Máximo de Pessoas Próximas ($nextTargetLimit)" else "Desbloquear Mais $hiddenCount Amigos Próximos"
            AppLanguage.INDONESIAN -> if (tier >= 4) "Buka Maksimal Teman Sekitar ($nextTargetLimit User)" else "Buka $hiddenCount Teman Sekitar Lagi"
            else -> if (tier >= 4) "Unlock Maximum Nearby Friends ($nextTargetLimit Users)" else "Unlock $hiddenCount More Nearby Friends"
        }
    }

    fun nearbyUnlockMoreDesc(lang: AppLanguage, tier: Int): String {
        val count = when (tier) {
            0 -> 30
            1 -> 45
            2 -> 70
            3 -> 100
            else -> 125
        }
        return when (resolveLang(lang)) {
            AppLanguage.CHINESE -> "观看简短视频即可展示多达 $count 位身边的活跃用户！"
            AppLanguage.JAPANESE -> "短い動画を視聴して、周囲のアクティブユーザーを最大 $count 人まで表示！"
            AppLanguage.KOREAN -> "짧은 영상을 시청하고 주변 활동 사용자 최대 $count 명을 확인해보세요!"
            AppLanguage.ARABIC -> "شاهد فيديو قصير لعرض ما يصل إلى $count مستخدم نشط في منطقتك!"
            AppLanguage.SPANISH -> "¡Mira un video corto para mostrar hasta $count personas activas cerca!"
            AppLanguage.FRENCH -> "Regardez une courte vidéo pour afficher jusqu'à $count personnes actives près de chez vous !"
            AppLanguage.GERMAN -> "Schau ein kurzes Video, um bis zu $count aktive Personen in deiner Nähe zu sehen!"
            AppLanguage.RUSSIAN -> "Посмотрите короткое видео, чтобы открыть до $count активных пользователей поблизости!"
            AppLanguage.PORTUGUESE -> "Assista a um vídeo curto para exibir até $count pessoas ativas perto de você!"
            AppLanguage.INDONESIAN -> "Tonton video singkat untuk menampilkan hingga $count pengguna aktif di sekitar Anda."
            else -> "Watch a short video to display up to $count active nearby users."
        }
    }

    fun nearbyUnlockSuccessToast(lang: AppLanguage, tier: Int, nextTargetLimit: Int): String {
        return when (resolveLang(lang)) {
            AppLanguage.CHINESE -> if (tier >= 4) "恭喜！已解锁最大附近用户（$nextTargetLimit 人）🎉" else "恭喜！附近用户数量已提升至 $nextTargetLimit 人 🎉"
            AppLanguage.JAPANESE -> if (tier >= 4) "おめでとうございます！最大の $nextTargetLimit 人をアンロックしました 🎉" else "周囲のユーザー表示数が $nextTargetLimit 人に増加しました 🎉"
            AppLanguage.KOREAN -> if (tier >= 4) "축하합니다! 최대 주변 인원 ($nextTargetLimit 명)이 잠금 해제되었습니다 🎉" else "주변 인원이 $nextTargetLimit 명으로 늘어났습니다 🎉"
            AppLanguage.ARABIC -> if (tier >= 4) "تهانينا! تم فتح الحد الأقصى للمستخدمين ($nextTargetLimit شخص) 🎉" else "تهانينا! تم زيادة المستخدمين في منطقتك إلى $nextTargetLimit 🎉"
            AppLanguage.SPANISH -> if (tier >= 4) "¡Felicidades! Se desbloqueó el máximo de personas ($nextTargetLimit) 🎉" else "¡Éxito! Usuarios cercanos ampliados a $nextTargetLimit personas 🎉"
            AppLanguage.FRENCH -> if (tier >= 4) "Félicitations ! Le maximum de personnes ($nextTargetLimit) est débloqué 🎉" else "Succès ! Utilisateurs proches étendus à $nextTargetLimit personnes 🎉"
            AppLanguage.GERMAN -> if (tier >= 4) "Glückwunsch! Maximal $nextTargetLimit Personen freigeschaltet 🎉" else "Erfolg! Personen in der Nähe auf $nextTargetLimit erweitert 🎉"
            AppLanguage.RUSSIAN -> if (tier >= 4) "Поздравляем! Открыт максимум пользователей ($nextTargetLimit чел.) 🎉" else "Успешно! Количество пользователей расширено до $nextTargetLimit 🎉"
            AppLanguage.PORTUGUESE -> if (tier >= 4) "Parabéns! Desbloqueado o máximo de pessoas ($nextTargetLimit) 🎉" else "Sucesso! Usuários próximos expandidos para $nextTargetLimit 🎉"
            AppLanguage.INDONESIAN -> if (tier >= 4) "Selamat! Pengguna sekitar maksimal ($nextTargetLimit orang) telah terbuka 🎉" else "Selamat! Pengguna sekitar ditambah menjadi $nextTargetLimit orang 🎉"
            else -> if (tier >= 4) "Success! Maximum nearby users ($nextTargetLimit people) unlocked 🎉" else "Success! Nearby users expanded to $nextTargetLimit people 🎉"
        }
    }

    fun nearbyWatchAdButton(lang: AppLanguage, tier: Int): String {
        val extra = when (tier) {
            0 -> "+18"
            1 -> "+15"
            2 -> "+25"
            3 -> "+30"
            else -> "+25"
        }
        return when (resolveLang(lang)) {
            AppLanguage.CHINESE -> "观看广告 ($extra 用户)"
            AppLanguage.JAPANESE -> "広告を見る ($extra 人)"
            AppLanguage.KOREAN -> "광고 시청 ($extra 명)"
            AppLanguage.ARABIC -> "مشاهدة إعلان ($extra مستخدم)"
            AppLanguage.SPANISH -> "Ver Anuncio ($extra Usuarios)"
            AppLanguage.FRENCH -> "Regarder la pub ($extra utilisateurs)"
            AppLanguage.GERMAN -> "Werbung ansehen ($extra Benutzer)"
            AppLanguage.RUSSIAN -> "Смотреть рекламу ($extra чел.)"
            AppLanguage.PORTUGUESE -> "Ver Anúncio ($extra Usuários)"
            AppLanguage.INDONESIAN -> "Tonton Iklan ($extra Pengguna)"
            else -> "Watch Ad ($extra Users)"
        }
    }

    // --- PRIVACY & LOCATION SETTINGS ---
    fun privacyLocationTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "隐私与定位"
        AppLanguage.JAPANESE -> "プライバシー & 位置情報"
        AppLanguage.KOREAN -> "개인정보 & 위치"
        AppLanguage.ARABIC -> "الخصوصية والموقع"
        AppLanguage.SPANISH -> "Privacidad y Ubicación"
        AppLanguage.FRENCH -> "Confidentialité & Localisation"
        AppLanguage.GERMAN -> "Datenschutz & Standort"
        AppLanguage.RUSSIAN -> "Конфиденциальность и гео"
        AppLanguage.PORTUGUESE -> "Privacidade e Localização"
        AppLanguage.INDONESIAN -> "Privasi & Lokasi"
        else -> "Privacy & Location"
    }

    fun privacyShowMeNearby(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "在附近雷达中显示我"
        AppLanguage.JAPANESE -> "近くのレーダーに自分を表示"
        AppLanguage.KOREAN -> "내 주변 레이더에 나를 표시"
        AppLanguage.ARABIC -> "إظهاري في الرادار القريب"
        AppLanguage.SPANISH -> "Mostrarme en Radar Cercano"
        AppLanguage.FRENCH -> "M'afficher sur le radar"
        AppLanguage.GERMAN -> "Mich auf dem Radar anzeigen"
        AppLanguage.RUSSIAN -> "Показывать меня на радаре"
        AppLanguage.PORTUGUESE -> "Mostrar-me no Radar Próximo"
        AppLanguage.INDONESIAN -> "Tampilkan Saya di Sekitar"
        else -> "Show Me in Nearby Radar"
    }

    fun privacyShowMeNearbyDesc(lang: AppLanguage, isVisible: Boolean): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> if (isVisible) "您的资料已启用，其他用户可在“附近的人”雷达中发现您。" else "隐身模式已激活。您的资料已从附近雷达中隐藏。"
        AppLanguage.JAPANESE -> if (isVisible) "プロフィールは公開されており、近くのレーダーで他のユーザーに見つけてもらえます。" else "ゴーストモード有効中。近くのレーダーから非表示になっています。"
        AppLanguage.KOREAN -> if (isVisible) "프로필이 공개되어 '내 주변' 레이더에서 다른 사용자가 나를 찾을 수 있습니다." else "고스트 모드 활성화됨. 주변 탐색 레이더에서 프로필이 숨겨집니다."
        AppLanguage.ARABIC -> if (isVisible) "ملفك الشخصي نشط ويمكن للمستخدمين الآخرين العثور عليك في الرادار." else "وضع التخفي مفعل. تم إخفاء ملفك الشخصي من رادار البحث."
        AppLanguage.SPANISH -> if (isVisible) "Tu perfil está activo y otras personas pueden encontrarte en el radar cercano." else "Modo incógnito activo. Tu perfil está oculto del radar de personas cercanas."
        AppLanguage.FRENCH -> if (isVisible) "Votre profil est visible et trouvable par les autres utilisateurs à proximité." else "Mode fantôme actif. Votre profil est masqué du radar."
        AppLanguage.GERMAN -> if (isVisible) "Dein Profil ist aktiv und andere können dich auf dem Radar finden." else "Geist-Modus aktiv. Dein Profil ist auf dem Umgebungsradar unsichtbar."
        AppLanguage.RUSSIAN -> if (isVisible) "Ваш профиль виден другим пользователям на радаре поблизости." else "Режим инкогнито включен. Профиль скрыт с радара поблизости."
        AppLanguage.PORTUGUESE -> if (isVisible) "Seu perfil está ativo e outras pessoas podem encontrá-lo no radar." else "Modo invisível ativo. Seu perfil está oculto do radar próximo."
        AppLanguage.INDONESIAN -> if (isVisible) "Profil Anda aktif dan dapat ditemukan oleh pengguna lain di radar 'Di Sekitar Saya'." else "Mode Penyamaran aktif. Profil Anda disembunyikan dari radar pencarian orang sekitar."
        else -> if (isVisible) "Your profile is active and can be found by others in the nearby radar." else "Incognito mode active. Your profile is hidden from the nearby radar."
    }

    fun privacyHideExactDistance(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "隐藏精确距离"
        AppLanguage.JAPANESE -> "正確な距離を非表示"
        AppLanguage.KOREAN -> "정확한 거리 숨기기"
        AppLanguage.ARABIC -> "إخفاء المسافة الدقيقة"
        AppLanguage.SPANISH -> "Ocultar Distancia Exacta"
        AppLanguage.FRENCH -> "Masquer la distance exacte"
        AppLanguage.GERMAN -> "Genaue Entfernung verbergen"
        AppLanguage.RUSSIAN -> "Скрыть точное расстояние"
        AppLanguage.PORTUGUESE -> "Ocultar Distância Exata"
        AppLanguage.INDONESIAN -> "Sembunyikan Jarak Persis"
        else -> "Hide Exact Distance"
    }

    fun privacyHideExactDistanceDesc(lang: AppLanguage, isHidden: Boolean): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> if (isHidden) "距离已隐藏，他人仅能看到您的城市或地区名称。" else "其他用户可以看到您的大致米数或公里数距离。"
        AppLanguage.JAPANESE -> if (isHidden) "距離は非表示になり、他の人にはあなたの都市/地域名のみが表示されます。" else "他のユーザーはおよその距離（メートル/km）を確認できます。"
        AppLanguage.KOREAN -> if (isHidden) "정확한 거리가 숨겨지며 도시/지역 이름만 표시됩니다." else "다른 사용자가 대략적인 거리(m/km)를 확인할 수 있습니다."
        AppLanguage.ARABIC -> if (isHidden) "المسافة بالأمتار/الكيلومترات مخفية، يرى الآخرون اسم مدينتك فقط." else "يمكن للمستخدمين الآخرين رؤية المسافة التقريبية بالأمتار أو الكيلومترات."
        AppLanguage.SPANISH -> if (isHidden) "Distancia oculta. Los demás solo verán tu ciudad o región." else "Otras personas pueden ver la distancia estimada en metros o km."
        AppLanguage.FRENCH -> if (isHidden) "Distance masquée. Seul le nom de votre ville/région sera visible." else "Les autres peuvent voir la distance estimée en mètres ou km."
        AppLanguage.GERMAN -> if (isHidden) "Genaue Distanz verborgen. Andere sehen nur deine Stadt/Region." else "Andere Benutzer können die ungefähre Entfernung in Metern/km sehen."
        AppLanguage.RUSSIAN -> if (isHidden) "Расстояние скрыто. Другие видят только название города/региона." else "Другие пользователи могут видеть примерное расстояние в метрах или км."
        AppLanguage.PORTUGUESE -> if (isHidden) "Distância oculta. Outras pessoas verão apenas sua cidade ou região." else "Outros usuários podem ver a distância aproximada em metros ou km."
        AppLanguage.INDONESIAN -> if (isHidden) "Jarak meter/km disembunyikan. Orang lain hanya dapat melihat nama kota/wilayah Anda." else "Pengguna lain dapat melihat perkiraan jarak meter atau kilometer dari lokasi Anda."
        else -> if (isHidden) "Exact distance is hidden. Others will only see your city/region name." else "Other users can see an estimated distance in meters or kilometers."
    }

    fun privacyShowOnlineStatus(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "显示在线状态"
        AppLanguage.JAPANESE -> "オンライン状態を表示"
        AppLanguage.KOREAN -> "온라인 상태 표시"
        AppLanguage.ARABIC -> "إظهار حالة الاتصال"
        AppLanguage.SPANISH -> "Mostrar Estado en Línea"
        AppLanguage.FRENCH -> "Afficher le statut en ligne"
        AppLanguage.GERMAN -> "Online-Status anzeigen"
        AppLanguage.RUSSIAN -> "Показывать статус «В сети»"
        AppLanguage.PORTUGUESE -> "Mostrar Status Online"
        AppLanguage.INDONESIAN -> "Tampilkan Status Online"
        else -> "Show Online Status"
    }

    fun privacyGpsPermissionTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "设备位置权限 (GPS)"
        AppLanguage.JAPANESE -> "デバイスの位置情報権限 (GPS)"
        AppLanguage.KOREAN -> "기기 위치 권한 (GPS)"
        AppLanguage.ARABIC -> "إذن موقع الجهاز (GPS)"
        AppLanguage.SPANISH -> "Permiso de Ubicación del Dispositivo (GPS)"
        AppLanguage.FRENCH -> "Autorisation de localisation (GPS)"
        AppLanguage.GERMAN -> "Gerätestandort-Berechtigung (GPS)"
        AppLanguage.RUSSIAN -> "Разрешение на геолокацию (GPS)"
        AppLanguage.PORTUGUESE -> "Permissão de Localização do Dispositivo (GPS)"
        AppLanguage.INDONESIAN -> "Izin Lokasi Perangkat (GPS)"
        else -> "Device Location Permission (GPS)"
    }

    fun privacyGpsStatus(lang: AppLanguage, hasPermission: Boolean): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> if (hasPermission) "GPS 权限已授予 • 正常工作" else "未授予位置权限"
        AppLanguage.JAPANESE -> if (hasPermission) "GPS権限許可済み • アクティブ" else "位置情報の権限がありません"
        AppLanguage.KOREAN -> if (hasPermission) "GPS 권한 허용됨 • 활성" else "위치 권한이 허용되지 않음"
        AppLanguage.ARABIC -> if (hasPermission) "تم منح إذن GPS • نشط" else "لم يتم منح إذن الموقع"
        AppLanguage.SPANISH -> if (hasPermission) "Permiso GPS concedido • Activo" else "Permiso de ubicación no concedido"
        AppLanguage.FRENCH -> if (hasPermission) "Autorisation GPS accordée • Actif" else "Autorisation de localisation manquante"
        AppLanguage.GERMAN -> if (hasPermission) "GPS-Berechtigung erteilt • Aktiv" else "Standortberechtigung nicht erteilt"
        AppLanguage.RUSSIAN -> if (hasPermission) "Доступ к GPS предоставлен • Активен" else "Доступ к геолокации не предоставлен"
        AppLanguage.PORTUGUESE -> if (hasPermission) "Permissão GPS concedida • Ativo" else "Permissão de localização não concedida"
        AppLanguage.INDONESIAN -> if (hasPermission) "Izin GPS diberikan • Aktif" else "Izin lokasi belum diberikan"
        else -> if (hasPermission) "GPS permission granted • Active" else "Location permission not granted"
    }

    fun privacyGrantGpsButton(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "授予 GPS 访问权限"
        AppLanguage.JAPANESE -> "GPSアクセスを許可"
        AppLanguage.KOREAN -> "GPS 접근 권한 허용"
        AppLanguage.ARABIC -> "السماح بالوصول إلى GPS"
        AppLanguage.SPANISH -> "Permitir Acceso a GPS"
        AppLanguage.FRENCH -> "Autoriser l'accès GPS"
        AppLanguage.GERMAN -> "GPS-Zugriff erlauben"
        AppLanguage.RUSSIAN -> "Разрешить доступ к GPS"
        AppLanguage.PORTUGUESE -> "Permitir Acesso ao GPS"
        AppLanguage.INDONESIAN -> "Izinkan Akses GPS"
        else -> "Grant GPS Access"
    }

    fun privacyOpenGpsSettings(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "打开手机定位设置"
        AppLanguage.JAPANESE -> "端末の位置情報設定を開く"
        AppLanguage.KOREAN -> "휴대폰 위치 설정 열기"
        AppLanguage.ARABIC -> "فتح إعدادات موقع الهاتف"
        AppLanguage.SPANISH -> "Abrir Ajustes de Ubicación del Teléfono"
        AppLanguage.FRENCH -> "Ouvrir les paramètres de localisation"
        AppLanguage.GERMAN -> "Standorteinstellungen öffnen"
        AppLanguage.RUSSIAN -> "Открыть настройки геопозиции"
        AppLanguage.PORTUGUESE -> "Abrir Configurações de Localização"
        AppLanguage.INDONESIAN -> "Buka Pengaturan Lokasi HP"
        else -> "Open Phone Location Settings"
    }

    fun privacySettingsUpdatedToast(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "隐私与定位设置已更新"
        AppLanguage.JAPANESE -> "プライバシーと位置情報の設定を更新しました"
        AppLanguage.KOREAN -> "개인정보 및 위치 설정이 업데이트되었습니다"
        AppLanguage.ARABIC -> "تم تحديث إعدادات الخصوصية والموقع"
        AppLanguage.SPANISH -> "Configuración de privacidad y ubicación actualizada"
        AppLanguage.FRENCH -> "Paramètres de confidentialité & localisation mis à jour"
        AppLanguage.GERMAN -> "Datenschutz- und Standorteinstellungen aktualisiert"
        AppLanguage.RUSSIAN -> "Настройки конфиденциальности и локации обновлены"
        AppLanguage.PORTUGUESE -> "Configurações de privacidade e localização atualizadas"
        AppLanguage.INDONESIAN -> "Pengaturan privasi & lokasi diperbarui"
        else -> "Privacy & location settings updated"
    }

    fun commonDone(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "完成"
        AppLanguage.JAPANESE -> "完了"
        AppLanguage.KOREAN -> "완료"
        AppLanguage.ARABIC -> "تم"
        AppLanguage.SPANISH -> "Listo"
        AppLanguage.FRENCH -> "Terminé"
        AppLanguage.GERMAN -> "Fertig"
        AppLanguage.RUSSIAN -> "Готово"
        AppLanguage.PORTUGUESE -> "Concluído"
        AppLanguage.INDONESIAN -> "Selesai"
        else -> "Done"
    }

    fun unblockDialogTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "解除用户拉黑？"
        AppLanguage.JAPANESE -> "ブロックを解除しますか？"
        AppLanguage.KOREAN -> "사용자 차단을 해제하시겠습니까?"
        AppLanguage.ARABIC -> "إلغاء حظر المستخدم؟"
        AppLanguage.SPANISH -> "¿Desbloquear usuario?"
        AppLanguage.FRENCH -> "Débloquer l'utilisateur ?"
        AppLanguage.GERMAN -> "Benutzer freigeben?"
        AppLanguage.RUSSIAN -> "Разблокировать пользователя?"
        AppLanguage.PORTUGUESE -> "Desbloquear usuário?"
        AppLanguage.INDONESIAN -> "Buka Blokir Pengguna?"
        else -> "Unblock User?"
    }

    fun unblockDialogDesc(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "解除后，该用户将能够再次向您发送消息。"
        AppLanguage.JAPANESE -> "解除すると、このユーザーはあなたに再度メッセージを送信できるようになります。"
        AppLanguage.KOREAN -> "차단을 해제하면 이 사용자가 회원님께 다시 메시지를 보낼 수 있습니다."
        AppLanguage.ARABIC -> "سيتمكن هذا المستخدم من إرسال رسائل إليك مرة أخرى."
        AppLanguage.SPANISH -> "Este usuario podrá enviarte mensajes de nuevo."
        AppLanguage.FRENCH -> "Cet utilisateur pourra de nouveau vous envoyer des messages."
        AppLanguage.GERMAN -> "Dieser Benutzer kann dir wieder Nachrichten senden."
        AppLanguage.RUSSIAN -> "Этот пользователь снова сможет отправлять вам сообщения."
        AppLanguage.PORTUGUESE -> "Este usuário poderá enviar mensagens para você novamente."
        AppLanguage.INDONESIAN -> "Pengguna ini akan dapat mengirim pesan kepada Anda lagi."
        else -> "This user will be able to send you messages again."
    }

    fun unblockSuccessToast(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "已成功解除拉黑"
        AppLanguage.JAPANESE -> "ブロックを解除しました"
        AppLanguage.KOREAN -> "차단이 해제되었습니다"
        AppLanguage.ARABIC -> "تم إلغاء الحظر بنجاح"
        AppLanguage.SPANISH -> "Usuario desbloqueado"
        AppLanguage.FRENCH -> "Utilisateur débloqué"
        AppLanguage.GERMAN -> "Benutzer freigegeben"
        AppLanguage.RUSSIAN -> "Пользователь разблокирован"
        AppLanguage.PORTUGUESE -> "Usuário desbloqueado"
        AppLanguage.INDONESIAN -> "Pengguna dibuka dari blokir"
        else -> "User unblocked"
    }

    // --- BOTTLE SCREEN ---
    fun bottleFishedSuccessTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "成功打捞到一个漂流瓶！"
        AppLanguage.JAPANESE -> "ボトルメッセージを釣り上げました！"
        AppLanguage.KOREAN -> "유리병 편지를 성공적으로 건졌습니다!"
        AppLanguage.ARABIC -> "تم اصطياد زجاجة بنجاح!"
        AppLanguage.SPANISH -> "¡Botella Pescada con Éxito!"
        AppLanguage.FRENCH -> "Bouteille pêchée avec succès !"
        AppLanguage.GERMAN -> "Flaschenpost erfolgreich gefischt!"
        AppLanguage.RUSSIAN -> "Бутылка успешно выловлена!"
        AppLanguage.PORTUGUESE -> "Garrafa Pescada com Sucesso!"
        AppLanguage.INDONESIAN -> "Botol Berhasil Dipancing!"
        else -> "Bottle Fished Successfully!"
    }

    fun bottleFoundAt(lang: AppLanguage, location: String): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "发现于 $location"
        AppLanguage.JAPANESE -> "$location で発見"
        AppLanguage.KOREAN -> "$location 에서 발견됨"
        AppLanguage.ARABIC -> "تم العثور عليها في $location"
        AppLanguage.SPANISH -> "Encontrada en $location"
        AppLanguage.FRENCH -> "Trouvée à $location"
        AppLanguage.GERMAN -> "Gefunden in $location"
        AppLanguage.RUSSIAN -> "Найдено в $location"
        AppLanguage.PORTUGUESE -> "Encontrada em $location"
        AppLanguage.INDONESIAN -> "Ditemukan di $location"
        else -> "Found in $location"
    }

    fun bottleDriftingAt(lang: AppLanguage, location: String): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "漂流在 $location"
        AppLanguage.JAPANESE -> "$location を漂流中"
        AppLanguage.KOREAN -> "$location 에서 표류 중"
        AppLanguage.ARABIC -> "تطفو في $location"
        AppLanguage.SPANISH -> "Flotando en $location"
        AppLanguage.FRENCH -> "Dérive à $location"
        AppLanguage.GERMAN -> "Treibt in $location"
        AppLanguage.RUSSIAN -> "Дрейфует в $location"
        AppLanguage.PORTUGUESE -> "Derivando em $location"
        AppLanguage.INDONESIAN -> "Hanyut di $location"
        else -> "Drifting in $location"
    }

    fun bottleTakenFromOcean(lang: AppLanguage, location: String): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "$location • 打捞自海洋"
        AppLanguage.JAPANESE -> "$location • 海から釣り上げました"
        AppLanguage.KOREAN -> "$location • 바다에서 건져냄"
        AppLanguage.ARABIC -> "$location • تم التقاطها من المحيط"
        AppLanguage.SPANISH -> "$location • Rescatada del Océano"
        AppLanguage.FRENCH -> "$location • Pêchée dans l'océan"
        AppLanguage.GERMAN -> "$location • Aus dem Meer gefischt"
        AppLanguage.RUSSIAN -> "$location • Выловлено из океана"
        AppLanguage.PORTUGUESE -> "$location • Pescada no Oceano"
        AppLanguage.INDONESIAN -> "$location • Diambil dari Lautan"
        else -> "$location • Fished from the Ocean"
    }

    fun bottleMyBottleTag(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "🍾 我的漂流瓶"
        AppLanguage.JAPANESE -> "🍾 マイボトル"
        AppLanguage.KOREAN -> "🍾 내 유리병"
        AppLanguage.ARABIC -> "🍾 زجاجتي"
        AppLanguage.SPANISH -> "🍾 Mi Botella"
        AppLanguage.FRENCH -> "🍾 Ma bouteille"
        AppLanguage.GERMAN -> "🍾 Meine Flaschenpost"
        AppLanguage.RUSSIAN -> "🍾 Моя бутылка"
        AppLanguage.PORTUGUESE -> "🍾 Minha Garrafa"
        AppLanguage.INDONESIAN -> "🍾 Botol Saya"
        else -> "🍾 My Bottle"
    }

    fun bottleFishedTag(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "🎣 已打捞"
        AppLanguage.JAPANESE -> "🎣 釣り上げ済み"
        AppLanguage.KOREAN -> "🎣 건져냄"
        AppLanguage.ARABIC -> "🎣 تم الاصطياد"
        AppLanguage.SPANISH -> "🎣 Pescada"
        AppLanguage.FRENCH -> "🎣 Pêchée"
        AppLanguage.GERMAN -> "🎣 Gefischt"
        AppLanguage.RUSSIAN -> "🎣 Выловлено"
        AppLanguage.PORTUGUESE -> "🎣 Pescada"
        AppLanguage.INDONESIAN -> "🎣 Diambil"
        else -> "🎣 Fished"
    }

    // --- ACTIVITY NOTIFICATIONS & TIME AGO ---
    fun notificationsTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "活动通知"
        AppLanguage.JAPANESE -> "お知らせ"
        AppLanguage.KOREAN -> "활동 알림"
        AppLanguage.ARABIC -> "إشعارات النشاط"
        AppLanguage.SPANISH -> "Notificaciones de Actividad"
        AppLanguage.FRENCH -> "Notifications d'activité"
        AppLanguage.GERMAN -> "Aktivitätsbenachrichtigungen"
        AppLanguage.RUSSIAN -> "Уведомления о событиях"
        AppLanguage.PORTUGUESE -> "Notificações de Atividade"
        AppLanguage.INDONESIAN -> "Pemberitahuan Aktivitas"
        else -> "Activity Notifications"
    }

    fun notificationsNewBadge(lang: AppLanguage, count: Int): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "$count 条新"
        AppLanguage.JAPANESE -> "$count 件の新着"
        AppLanguage.KOREAN -> "${count}개 신규"
        AppLanguage.ARABIC -> "$count جديد"
        AppLanguage.SPANISH -> "$count Nuevas"
        AppLanguage.FRENCH -> "$count Nouveau(x)"
        AppLanguage.GERMAN -> "$count Neu"
        AppLanguage.RUSSIAN -> "$count новых"
        AppLanguage.PORTUGUESE -> "$count Novas"
        AppLanguage.INDONESIAN -> "$count Baru"
        else -> "$count New"
    }

    fun notificationsSubtitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "最新互动与系统动态信息"
        AppLanguage.JAPANESE -> "最近のアクティビティとインタラクション情報"
        AppLanguage.KOREAN -> "최근 활동 및 상호작용 업데이트"
        AppLanguage.ARABIC -> "أحدث الأنشطة وتحديثات التفاعل"
        AppLanguage.SPANISH -> "Actividades recientes e información de interacción"
        AppLanguage.FRENCH -> "Activités récentes et mises à jour"
        AppLanguage.GERMAN -> "Kürzliche Aktivitäten & Interaktionen"
        AppLanguage.RUSSIAN -> "Недавняя активность и обновления"
        AppLanguage.PORTUGUESE -> "Atividades recentes e informações de interação"
        AppLanguage.INDONESIAN -> "Aktivitas singkat & info interaksi"
        else -> "Recent activities & interaction updates"
    }

    fun notificationsMarkAllRead(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "标为已读"
        AppLanguage.JAPANESE -> "既読にする"
        AppLanguage.KOREAN -> "모두 읽음"
        AppLanguage.ARABIC -> "تحديد كمقروء"
        AppLanguage.SPANISH -> "Marcar Leídas"
        AppLanguage.FRENCH -> "Marquer comme lu"
        AppLanguage.GERMAN -> "Als gelesen markieren"
        AppLanguage.RUSSIAN -> "Прочитано"
        AppLanguage.PORTUGUESE -> "Marcar Lidas"
        AppLanguage.INDONESIAN -> "Tandai Dibaca"
        else -> "Mark as Read"
    }

    fun notificationsClearAll(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "全部清空"
        AppLanguage.JAPANESE -> "クリア"
        AppLanguage.KOREAN -> "지우기"
        AppLanguage.ARABIC -> "مسح الكل"
        AppLanguage.SPANISH -> "Limpiar"
        AppLanguage.FRENCH -> "Effacer"
        AppLanguage.GERMAN -> "Leeren"
        AppLanguage.RUSSIAN -> "Очистить"
        AppLanguage.PORTUGUESE -> "Limpar"
        AppLanguage.INDONESIAN -> "Bersihkan"
        else -> "Clear All"
    }

    fun notificationsCategoryAll(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "全部"
        AppLanguage.JAPANESE -> "すべて"
        AppLanguage.KOREAN -> "전체"
        AppLanguage.ARABIC -> "الكل"
        AppLanguage.SPANISH -> "Todos"
        AppLanguage.FRENCH -> "Tous"
        AppLanguage.GERMAN -> "Alle"
        AppLanguage.RUSSIAN -> "Все"
        AppLanguage.PORTUGUESE -> "Todos"
        AppLanguage.INDONESIAN -> "Semua"
        else -> "All"
    }

    fun notificationsCategoryFriends(lang: AppLanguage): String = when (resolveLang(lang)) {
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

    fun notificationsCategoryChats(lang: AppLanguage): String = when (resolveLang(lang)) {
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

    fun notificationsCategorySystem(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "系统"
        AppLanguage.JAPANESE -> "システム"
        AppLanguage.KOREAN -> "시스템"
        AppLanguage.ARABIC -> "النظام"
        AppLanguage.SPANISH -> "Sistema"
        AppLanguage.FRENCH -> "Système"
        AppLanguage.GERMAN -> "System"
        AppLanguage.RUSSIAN -> "Система"
        AppLanguage.PORTUGUESE -> "Sistema"
        AppLanguage.INDONESIAN -> "Sistem"
        else -> "System"
    }

    fun notificationsCategoryRadar(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "附近雷达"
        AppLanguage.JAPANESE -> "レーダー"
        AppLanguage.KOREAN -> "주변 레이더"
        AppLanguage.ARABIC -> "الرادار"
        AppLanguage.SPANISH -> "Radar"
        AppLanguage.FRENCH -> "Radar"
        AppLanguage.GERMAN -> "Radar"
        AppLanguage.RUSSIAN -> "Радар"
        AppLanguage.PORTUGUESE -> "Radar"
        AppLanguage.INDONESIAN -> "Radar Sekitar"
        else -> "Nearby Radar"
    }

    fun notificationsEmptyTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "暂无通知"
        AppLanguage.JAPANESE -> "通知はまだありません"
        AppLanguage.KOREAN -> "알림이 아직 없습니다"
        AppLanguage.ARABIC -> "لا توجد إشعارات حتى الآن"
        AppLanguage.SPANISH -> "Aún no hay notificaciones"
        AppLanguage.FRENCH -> "Aucune notification pour le moment"
        AppLanguage.GERMAN -> "Noch keine Benachrichtigungen"
        AppLanguage.RUSSIAN -> "Уведомлений пока нет"
        AppLanguage.PORTUGUESE -> "Ainda não há notificações"
        AppLanguage.INDONESIAN -> "Belum Ada Pemberitahuan"
        else -> "No Notifications Yet"
    }

    fun notificationsEmptyDesc(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "附近的雷达问候、新好友以及系统提示等最新互动将显示在这里。"
        AppLanguage.JAPANESE -> "レーダーからのあいさつ、新しい友達、システム情報などの新着アクティビティがここに表示されます。"
        AppLanguage.KOREAN -> "주변 레이더 인사, 새 친구 요청 및 시스템 정보가 여기에 표시됩니다."
        AppLanguage.ARABIC -> "ستظهر هنا التفاعلات الجديدة مثل تحيات الرادار والأصدقاء الجدد وتنبيهات النظام."
        AppLanguage.SPANISH -> "Nuevas interacciones como saludos de radar, nuevos amigos y avisos del sistema aparecerán aquí."
        AppLanguage.FRENCH -> "Les nouvelles interactions telles que les saluts radar, nouveaux amis et alertes système apparaîtront ici."
        AppLanguage.GERMAN -> "Neue Interaktionen wie Radar-Grüße, neue Freunde und System-Infos erscheinen hier."
        AppLanguage.RUSSIAN -> "Здесь появятся новые действия: приветствия с радара, новые друзья и системные уведомления."
        AppLanguage.PORTUGUESE -> "Novas interações como saudações de radar, novos amigos e avisos do sistema aparecerão aqui."
        AppLanguage.INDONESIAN -> "Aktivitas interaksi baru seperti sapaan radar, teman baru, dan info sistem akan tampil di sini."
        else -> "New interactions like radar greetings, new friends, and system updates will appear here."
    }

    fun timeAgoJustNow(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "刚刚"
        AppLanguage.JAPANESE -> "たった今"
        AppLanguage.KOREAN -> "방금 전"
        AppLanguage.ARABIC -> "الآن"
        AppLanguage.SPANISH -> "Hace un momento"
        AppLanguage.FRENCH -> "À l'instant"
        AppLanguage.GERMAN -> "Gerade eben"
        AppLanguage.RUSSIAN -> "Только что"
        AppLanguage.PORTUGUESE -> "Agora mesmo"
        AppLanguage.INDONESIAN -> "Baru saja"
        else -> "Just now"
    }

    fun timeAgoMinutes(lang: AppLanguage, minutes: Long): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "${minutes}分钟前"
        AppLanguage.JAPANESE -> "${minutes}分前"
        AppLanguage.KOREAN -> "${minutes}분 전"
        AppLanguage.ARABIC -> "منذ $minutes دقيقة"
        AppLanguage.SPANISH -> "Hace ${minutes}m"
        AppLanguage.FRENCH -> "Il y a ${minutes} min"
        AppLanguage.GERMAN -> "Vor ${minutes}m"
        AppLanguage.RUSSIAN -> "${minutes} мин. назад"
        AppLanguage.PORTUGUESE -> "Há ${minutes}m"
        AppLanguage.INDONESIAN -> "${minutes}m lalu"
        else -> "${minutes}m ago"
    }

    fun timeAgoHours(lang: AppLanguage, hours: Long): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "${hours}小时前"
        AppLanguage.JAPANESE -> "${hours}時間前"
        AppLanguage.KOREAN -> "${hours}시간 전"
        AppLanguage.ARABIC -> "منذ $hours ساعة"
        AppLanguage.SPANISH -> "Hace ${hours}h"
        AppLanguage.FRENCH -> "Il y a ${hours} h"
        AppLanguage.GERMAN -> "Vor ${hours} Std."
        AppLanguage.RUSSIAN -> "${hours} ч. назад"
        AppLanguage.PORTUGUESE -> "Há ${hours}h"
        AppLanguage.INDONESIAN -> "${hours}j lalu"
        else -> "${hours}h ago"
    }

    fun timeAgoDays(lang: AppLanguage, days: Long): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "${days}天前"
        AppLanguage.JAPANESE -> "${days}日前"
        AppLanguage.KOREAN -> "${days}일 전"
        AppLanguage.ARABIC -> "منذ $days يوم"
        AppLanguage.SPANISH -> "Hace ${days}d"
        AppLanguage.FRENCH -> "Il y a ${days} j"
        AppLanguage.GERMAN -> "Vor ${days} Tagen"
        AppLanguage.RUSSIAN -> "${days} дн. назад"
        AppLanguage.PORTUGUESE -> "Há ${days}d"
        AppLanguage.INDONESIAN -> "${days}h lalu"
        else -> "${days}d ago"
    }

    // Default Names & Prompts
    fun defaultUserName(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "Lovy 用户"
        AppLanguage.JAPANESE -> "Lovyユーザー"
        AppLanguage.KOREAN -> "Lovy 사용자"
        AppLanguage.ARABIC -> "مستخدم Lovy"
        AppLanguage.SPANISH -> "Usuario de Lovy"
        AppLanguage.FRENCH -> "Utilisateur Lovy"
        AppLanguage.GERMAN -> "Lovy-Nutzer"
        AppLanguage.RUSSIAN -> "Пользователь Lovy"
        AppLanguage.PORTUGUESE -> "Usuário Lovy"
        AppLanguage.INDONESIAN -> "Pengguna Lovy"
        else -> "Lovy User"
    }

    fun defaultFriendName(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "Lovy 好友"
        AppLanguage.JAPANESE -> "Lovyフレンド"
        AppLanguage.KOREAN -> "Lovy 친구"
        AppLanguage.ARABIC -> "صديق Lovy"
        AppLanguage.SPANISH -> "Amigo Lovy"
        AppLanguage.FRENCH -> "Ami Lovy"
        AppLanguage.GERMAN -> "Lovy-Freund"
        AppLanguage.RUSSIAN -> "Друг Lovy"
        AppLanguage.PORTUGUESE -> "Amigo Lovy"
        AppLanguage.INDONESIAN -> "Teman Lovy"
        else -> "Lovy Friend"
    }

    fun notifWelcomeTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "欢迎来到 Lovy Chat ✨"
        AppLanguage.JAPANESE -> "Lovy Chatへようこそ ✨"
        AppLanguage.KOREAN -> "Lovy Chat에 오신 것을 환영합니다 ✨"
        AppLanguage.ARABIC -> "مرحباً بك في Lovy Chat ✨"
        AppLanguage.SPANISH -> "¡Bienvenido a Lovy Chat! ✨"
        AppLanguage.FRENCH -> "Bienvenue sur Lovy Chat ✨"
        AppLanguage.GERMAN -> "Willkommen bei Lovy Chat ✨"
        AppLanguage.RUSSIAN -> "Добро пожаловать в Lovy Chat ✨"
        AppLanguage.PORTUGUESE -> "Bem-vindo ao Lovy Chat ✨"
        AppLanguage.INDONESIAN -> "Selamat Datang di Lovy Chat ✨"
        else -> "Welcome to Lovy Chat ✨"
    }

    fun notifWelcomeDesc(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "开始发现附近的新朋友，分享精彩瞬间并畅聊吧！"
        AppLanguage.JAPANESE -> "近くの新しい友達を見つけて、モーメントを共有してチャットを楽しみましょう！"
        AppLanguage.KOREAN -> "주변의 새로운 친구를 찾고 모먼트를 공유하며 즐겁게 대화해보세요!"
        AppLanguage.ARABIC -> "ابدأ باكتشاف أصدقاء جدد بالقرب منك وشارك اللحظات واستمتع بالدردشة!"
        AppLanguage.SPANISH -> "¡Descubre nuevos amigos cercanos, comparte momentos y disfruta del chat!"
        AppLanguage.FRENCH -> "Trouvez de nouveaux amis proches, partagez des moments et discutez !"
        AppLanguage.GERMAN -> "Finde neue Freunde in deiner Nähe, teile Momente und genieße das Chatten!"
        AppLanguage.RUSSIAN -> "Находите новых друзей поблизости, делитесь моментами и общайтесь с удовольствием!"
        AppLanguage.PORTUGUESE -> "Descubra novos amigos próximos, compartilhe momentos e divirta-se conversando!"
        AppLanguage.INDONESIAN -> "Mulai temukan teman baru di sekitar, bagikan momen, dan nikmati obrolan!"
        else -> "Start finding new friends nearby, share moments, and enjoy chatting!"
    }

    fun notifRadarActiveTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "附近雷达已激活 📍"
        AppLanguage.JAPANESE -> "周辺レーダー起動中 📍"
        AppLanguage.KOREAN -> "주변 탐색 레이더 활성 📍"
        AppLanguage.ARABIC -> "رادار الأصدقاء نشط 📍"
        AppLanguage.SPANISH -> "Radar Cercano Activo 📍"
        AppLanguage.FRENCH -> "Radar de proximité actif 📍"
        AppLanguage.GERMAN -> "Umgebungsradar aktiv 📍"
        AppLanguage.RUSSIAN -> "Радар поблизости активен 📍"
        AppLanguage.PORTUGUESE -> "Radar Próximo Ativo 📍"
        AppLanguage.INDONESIAN -> "Radar Sekitar Aktif 📍"
        else -> "Nearby Radar Active 📍"
    }

    fun notifRadarActiveDesc(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "好友雷达系统已就绪，正在安全发现附近的活跃用户。"
        AppLanguage.JAPANESE -> "友達検索システムが安全に近くのユーザーを見つける準備ができました。"
        AppLanguage.KOREAN -> "친구 찾기 시스템이 주변 사용자를 안전하게 검색할 준비가 되었습니다."
        AppLanguage.ARABIC -> "نظام البحث عن الأصدقاء جاهز للعثور على المستخدمين القريبين بأمان."
        AppLanguage.SPANISH -> "El radar de amigos está listo para encontrar usuarios cercanos de forma segura."
        AppLanguage.FRENCH -> "Le radar est prêt à détecter les personnes proches en toute sécurité."
        AppLanguage.GERMAN -> "Das Freundesradar ist bereit, Nutzer in der Nähe sicher zu finden."
        AppLanguage.RUSSIAN -> "Система поиска готова безопасно находить пользователей поблизости."
        AppLanguage.PORTUGUESE -> "O radar de amigos está pronto para encontrar pessoas próximas com segurança."
        AppLanguage.INDONESIAN -> "Sistem pelacak teman siap menemukan pengguna terdekat dengan aman."
        else -> "Friend radar is ready to safely discover nearby users."
    }

    fun notifFriendRequestTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "新的好友请求 🤝"
        AppLanguage.JAPANESE -> "新しい友達リクエスト 🤝"
        AppLanguage.KOREAN -> "새 친구 요청 🤝"
        AppLanguage.ARABIC -> "طلب صداقة جديد 🤝"
        AppLanguage.SPANISH -> "Nueva Solicitud de Amistad 🤝"
        AppLanguage.FRENCH -> "Nouvelle demande d'ami 🤝"
        AppLanguage.GERMAN -> "Neue Freundschaftsanfrage 🤝"
        AppLanguage.RUSSIAN -> "Новый запрос в друзья 🤝"
        AppLanguage.PORTUGUESE -> "Nova Solicitação de Amizade 🤝"
        AppLanguage.INDONESIAN -> "Permintaan Pertemanan Baru 🤝"
        else -> "New Friend Request 🤝"
    }

    fun notifFriendRequestDesc(lang: AppLanguage, name: String): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "$name 想加您为好友。"
        AppLanguage.JAPANESE -> "$name さんがあなたと友達になりたがっています。"
        AppLanguage.KOREAN -> "$name 님이 친구가 되고 싶어 합니다."
        AppLanguage.ARABIC -> "$name يريد أن يكون صديقك."
        AppLanguage.SPANISH -> "$name quiere ser tu amigo(a)."
        AppLanguage.FRENCH -> "$name souhaite devenir votre ami(e)."
        AppLanguage.GERMAN -> "$name möchte dein Freund werden."
        AppLanguage.RUSSIAN -> "$name хочет добавить вас в друзья."
        AppLanguage.PORTUGUESE -> "$name quer ser seu amigo(a)."
        AppLanguage.INDONESIAN -> "$name ingin berteman dengan Anda."
        else -> "$name wants to be friends with you."
    }

    fun notifBottleCaughtTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "捞到了漂流瓶 🌊"
        AppLanguage.JAPANESE -> "漂流ボトルを拾いました 🌊"
        AppLanguage.KOREAN -> "바다의 유리병을 건졌습니다 🌊"
        AppLanguage.ARABIC -> "تم اصطياد زجاجة أمنيات 🌊"
        AppLanguage.SPANISH -> "¡Mensaje en botella pescado! 🌊"
        AppLanguage.FRENCH -> "Une bouteille à la mer repêchée 🌊"
        AppLanguage.GERMAN -> "Eine Flaschenpost geangelt 🌊"
        AppLanguage.RUSSIAN -> "Выловлено послание в бутылке 🌊"
        AppLanguage.PORTUGUESE -> "Mensagem na garrafa pescada! 🌊"
        AppLanguage.INDONESIAN -> "Pesan Botol Samudra Terjaring 🌊"
        else -> "Ocean Bottle Message Caught 🌊"
    }

    fun notifBottleCaughtDesc(lang: AppLanguage, name: String): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "来自 $name 的漂流瓶已从海洋中被捞起，快来看看吧。"
        AppLanguage.JAPANESE -> "$name さんからの漂流ボトルが海から引き揚げられました。内容を確認しましょう。"
        AppLanguage.KOREAN -> "$name 님의 바다 유리병 편지가 건져졌습니다. 확인해보세요."
        AppLanguage.ARABIC -> "تم التقاط رسالة الزجاجة من $name من المحيط، تحقق منها الآن."
        AppLanguage.SPANISH -> "El mensaje en botella de $name ha sido sacado del océano."
        AppLanguage.FRENCH -> "La bouteille à la mer de $name a été repêchée dans l'océan."
        AppLanguage.GERMAN -> "Die Flaschenpost von $name wurde aus dem Ozean gefischt."
        AppLanguage.RUSSIAN -> "Послание в бутылке от $name было выловлено из океана."
        AppLanguage.PORTUGUESE -> "A mensagem na garrafa de $name foi retirada do oceano."
        AppLanguage.INDONESIAN -> "Pesan botol dari $name telah terangkat dari samudra, buka untuk membacanya."
        else -> "A bottle message from $name was fished from the ocean, open to read it."
    }

    fun notifNearbyGreetTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "附近好友的新问候 👋"
        AppLanguage.JAPANESE -> "近くの友達からの挨拶 👋"
        AppLanguage.KOREAN -> "주변 친구의 인사 👋"
        AppLanguage.ARABIC -> "تحية من صديق قريب 👋"
        AppLanguage.SPANISH -> "Saludo de un amigo cercano 👋"
        AppLanguage.FRENCH -> "Salut d'un ami proche 👋"
        AppLanguage.GERMAN -> "Gruß von einem Freund in der Nähe 👋"
        AppLanguage.RUSSIAN -> "Приветствие от друга поблизости 👋"
        AppLanguage.PORTUGUESE -> "Saudação de um amigo próximo 👋"
        AppLanguage.INDONESIAN -> "Sapaan dari Teman Sekitar 👋"
        else -> "Greeting from Nearby Friend 👋"
    }

    fun notifNearbyGreetDesc(lang: AppLanguage, name: String): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "$name 在附近雷达向您打了个招呼！"
        AppLanguage.JAPANESE -> "$name さんが周辺レーダーであなたに挨拶しました！"
        AppLanguage.KOREAN -> "$name 님이 주변 탐색 레이더에서 인사를 건넸습니다!"
        AppLanguage.ARABIC -> "$name يلقي عليك التحية في رادار الأصدقاء القريبين!"
        AppLanguage.SPANISH -> "¡$name te ha saludado en el radar cercano!"
        AppLanguage.FRENCH -> "$name vous a salué sur le radar de proximité !"
        AppLanguage.GERMAN -> "$name hat dich auf dem Umgebungsradar gegrüßt!"
        AppLanguage.RUSSIAN -> "$name поприветствовал(а) вас на радаре поблизости!"
        AppLanguage.PORTUGUESE -> "$name enviou uma saudação no radar próximo!"
        AppLanguage.INDONESIAN -> "$name menyapa Anda melalui radar sekitar!"
        else -> "$name waved at you on the nearby radar!"
    }

    fun notifNewChatMessageTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "新聊天消息 💬"
        AppLanguage.JAPANESE -> "新着メッセージ 💬"
        AppLanguage.KOREAN -> "새 채팅 메시지 💬"
        AppLanguage.ARABIC -> "رسالة دردشة جديدة 💬"
        AppLanguage.SPANISH -> "Nuevo mensaje de chat 💬"
        AppLanguage.FRENCH -> "Nouveau message de discussion 💬"
        AppLanguage.GERMAN -> "Neue Chat-Nachricht 💬"
        AppLanguage.RUSSIAN -> "Новое сообщение в чате 💬"
        AppLanguage.PORTUGUESE -> "Nova mensagem de conversa 💬"
        AppLanguage.INDONESIAN -> "Pesan Obrolan Baru 💬"
        else -> "New Chat Message 💬"
    }

    fun notifNewChatMessageDesc(lang: AppLanguage, name: String): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "您收到了来自 $name 的新消息。"
        AppLanguage.JAPANESE -> "$name さんから新しいメッセージが届きました。"
        AppLanguage.KOREAN -> "$name 님으로부터 새 메시지가 도착했습니다."
        AppLanguage.ARABIC -> "وصلتك رسالة جديدة من $name."
        AppLanguage.SPANISH -> "Has recibido un nuevo mensaje de $name."
        AppLanguage.FRENCH -> "Vous avez reçu un nouveau message de $name."
        AppLanguage.GERMAN -> "Du hast eine neue Nachricht von $name erhalten."
        AppLanguage.RUSSIAN -> "Вы получили новое сообщение от $name."
        AppLanguage.PORTUGUESE -> "Você recebeu uma nova mensagem de $name."
        AppLanguage.INDONESIAN -> "Anda menerima pesan baru dari $name."
        else -> "You received a new message from $name."
    }

    fun notificationChannelName(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "Lovy Chat 消息与通知"
        AppLanguage.JAPANESE -> "Lovy Chat メッセージと通知"
        AppLanguage.KOREAN -> "Lovy Chat 메시지 및 알림"
        AppLanguage.ARABIC -> "رسائل وإشعارات Lovy Chat"
        AppLanguage.SPANISH -> "Mensajes y Notificaciones de Lovy Chat"
        AppLanguage.FRENCH -> "Messages et notifications Lovy Chat"
        AppLanguage.GERMAN -> "Lovy Chat Nachrichten & Benachrichtigungen"
        AppLanguage.RUSSIAN -> "Сообщения и уведомления Lovy Chat"
        AppLanguage.PORTUGUESE -> "Mensagens e Notificações do Lovy Chat"
        AppLanguage.INDONESIAN -> "Pesan & Notifikasi Lovy Chat"
        else -> "Lovy Chat Messages & Notifications"
    }

    fun notificationChannelDesc(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "双向聊天消息、附近好友问候及动态通知"
        AppLanguage.JAPANESE -> "ダイレクトメッセージ、近くの友達の挨拶、モーメントの通知"
        AppLanguage.KOREAN -> "양방향 채팅 메시지, 주변 친구 인사 및 모먼트 알림"
        AppLanguage.ARABIC -> "إشعارات رسائل الدردشة المباشرة وتحيات الأصدقاء واللحظات"
        AppLanguage.SPANISH -> "Notificaciones de mensajes de chat, saludos de amigos cercanos y momentos"
        AppLanguage.FRENCH -> "Notifications de messages, saluts d'amis proches et moments"
        AppLanguage.GERMAN -> "Benachrichtigungen für Chat-Nachrichten, Radar-Grüße und Momente"
        AppLanguage.RUSSIAN -> "Уведомления о сообщениях, приветствиях поблизости и моментах"
        AppLanguage.PORTUGUESE -> "Notificações de mensagens de chat, saudações de amigos próximos e momentos"
        AppLanguage.INDONESIAN -> "Notifikasi pesan obrolan 2 arah, sapaan teman sekitar, dan momen"
        else -> "Notifications for 2-way chat messages, nearby friend greetings, and moments"
    }

    fun loginPermissionsAndPrivacyBtn(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "应用权限与隐私说明"
        AppLanguage.JAPANESE -> "アプリの権限とプライバシー"
        AppLanguage.KOREAN -> "앱 권한 및 개인정보 보호"
        AppLanguage.ARABIC -> "أذونات التطبيق والخصوصية"
        AppLanguage.SPANISH -> "Permisos de la App y Privacidad"
        AppLanguage.FRENCH -> "Autorisations de l'application & Confidentialité"
        AppLanguage.GERMAN -> "App-Berechtigungen & Datenschutz"
        AppLanguage.RUSSIAN -> "Разрешения и конфиденциальность"
        AppLanguage.PORTUGUESE -> "Permissões do App e Privacidade"
        AppLanguage.INDONESIAN -> "Izin Akses & Privasi Aplikasi"
        else -> "App Permissions & Privacy"
    }

    fun loginGoogleFailedToast(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "连接 Google 账号失败"
        AppLanguage.JAPANESE -> "Googleアカウントの接続に失敗しました"
        AppLanguage.KOREAN -> "Google 계정 연결에 실패했습니다"
        AppLanguage.ARABIC -> "فشل ربط حساب Google"
        AppLanguage.SPANISH -> "Error al conectar la cuenta de Google"
        AppLanguage.FRENCH -> "Échec de connexion au compte Google"
        AppLanguage.GERMAN -> "Verbindung mit Google-Konto fehlgeschlagen"
        AppLanguage.RUSSIAN -> "Не удалось подключить аккаунт Google"
        AppLanguage.PORTUGUESE -> "Falha ao conectar conta do Google"
        AppLanguage.INDONESIAN -> "Gagal menghubungkan akun Google"
        else -> "Failed to connect Google account"
    }

    fun imageLoadFailed(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "无法加载图片"
        AppLanguage.JAPANESE -> "画像を読み込めませんでした"
        AppLanguage.KOREAN -> "이미지를 불러오지 못했습니다"
        AppLanguage.ARABIC -> "فشل تحميل الصورة"
        AppLanguage.SPANISH -> "No se pudo cargar la imagen"
        AppLanguage.FRENCH -> "Impossible de charger l'image"
        AppLanguage.GERMAN -> "Bild konnte nicht geladen werden"
        AppLanguage.RUSSIAN -> "Не удалось загрузить изображение"
        AppLanguage.PORTUGUESE -> "Falha ao carregar a imagem"
        AppLanguage.INDONESIAN -> "Gagal memuat gambar"
        else -> "Failed to load image"
    }

    fun filterClosest(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "最近"
        AppLanguage.JAPANESE -> "近い順"
        AppLanguage.KOREAN -> "가장 가까운"
        AppLanguage.ARABIC -> "الأقرب"
        AppLanguage.SPANISH -> "Más cercanos"
        AppLanguage.FRENCH -> "Plus proches"
        AppLanguage.GERMAN -> "Nächste"
        AppLanguage.RUSSIAN -> "Ближайшие"
        AppLanguage.PORTUGUESE -> "Mais próximos"
        AppLanguage.INDONESIAN -> "Terdekat"
        else -> "Closest"
    }

    fun nearbySearchingSignal(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "正在搜寻信号..."
        AppLanguage.JAPANESE -> "信号を検索中..."
        AppLanguage.KOREAN -> "신호 검색 중..."
        AppLanguage.ARABIC -> "جاري البحث عن إشارة..."
        AppLanguage.SPANISH -> "Buscando señal..."
        AppLanguage.FRENCH -> "Recherche de signal..."
        AppLanguage.GERMAN -> "Signal wird gesucht..."
        AppLanguage.RUSSIAN -> "Поиск сигнала..."
        AppLanguage.PORTUGUESE -> "Buscando sinal..."
        AppLanguage.INDONESIAN -> "Mencari sinyal..."
        else -> "Searching signal..."
    }

    fun nearbyAllUsersDisplayed(lang: AppLanguage, count: Int): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "已显示全部附近用户（最多 $count 位活跃用户）"
        AppLanguage.JAPANESE -> "近くのすべてのユーザーを表示しました（最大 $count 人のアクティブ）"
        AppLanguage.KOREAN -> "모든 주변 사용자가 표시되었습니다 (최대 $count 명 활동 중)"
        AppLanguage.ARABIC -> "تم عرض جميع المستخدمين القريبين (الحد الأقصى $count مستخدم نشط)"
        AppLanguage.SPANISH -> "Todos los usuarios cercanos están visibles (Máx $count activos)"
        AppLanguage.FRENCH -> "Tous les utilisateurs proches sont affichés (Max $count actifs)"
        AppLanguage.GERMAN -> "Alle Benutzer in der Nähe werden angezeigt (Max. $count aktiv)"
        AppLanguage.RUSSIAN -> "Все пользователи поблизости отображены (Макс. $count активных)"
        AppLanguage.PORTUGUESE -> "Todos os usuários próximos foram exibidos (Máx $count ativos)"
        AppLanguage.INDONESIAN -> "Semua pengguna sekitar telah berhasil ditampilkan (Maksimal $count user aktif)"
        else -> "All nearby users are now displayed (Max $count active users)"
    }

    fun nearbyUnlockingUsersToast(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "正在为您解锁附近用户 ✨"
        AppLanguage.JAPANESE -> "近くのユーザーをアンロックしています ✨"
        AppLanguage.KOREAN -> "주변 사용자를 잠금 해제 중입니다 ✨"
        AppLanguage.ARABIC -> "جاري فتح المستخدمين القريبين لك ✨"
        AppLanguage.SPANISH -> "Desbloqueando usuarios cercanos para ti ✨"
        AppLanguage.FRENCH -> "Déblocage des utilisateurs proches pour vous ✨"
        AppLanguage.GERMAN -> "Benutzer in der Nähe werden für dich freigeschaltet ✨"
        AppLanguage.RUSSIAN -> "Открываем пользователей поблизости для вас ✨"
        AppLanguage.PORTUGUESE -> "Desbloqueando pessoas próximas para você ✨"
        AppLanguage.INDONESIAN -> "Membuka pengguna sekitar untuk Anda ✨"
        else -> "Unlocking nearby users for you ✨"
    }

    fun radarRadiusExpanded(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "雷达已扩展 • 半径 15 公里"
        AppLanguage.JAPANESE -> "レーダー拡張中 • 半径 15 km"
        AppLanguage.KOREAN -> "확장 레이더 • 반경 15km"
        AppLanguage.ARABIC -> "رادار موسع • نطاق 15 كم"
        AppLanguage.SPANISH -> "Radar Ampliado • Radio de 15 km"
        AppLanguage.FRENCH -> "Radar étendu • Rayon de 15 km"
        AppLanguage.GERMAN -> "Erweitertes Radar • 15 km Radius"
        AppLanguage.RUSSIAN -> "Расширенный радар • Радиус 15 км"
        AppLanguage.PORTUGUESE -> "Radar Expandido • Raio de 15 km"
        AppLanguage.INDONESIAN -> "Radar Diperluas • Radius 15 km"
        else -> "Expanded Radar • 15 km Radius"
    }

    fun radarRadiusActive(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "雷达已激活 • 半径 5 公里"
        AppLanguage.JAPANESE -> "レーダー作動中 • 半径 5 km"
        AppLanguage.KOREAN -> "활성 레이더 • 반경 5km"
        AppLanguage.ARABIC -> "رادار نشط • نطاق 5 كم"
        AppLanguage.SPANISH -> "Radar Activo • Radio de 5 km"
        AppLanguage.FRENCH -> "Radar actif • Rayon de 5 km"
        AppLanguage.GERMAN -> "Aktives Radar • 5 km Radius"
        AppLanguage.RUSSIAN -> "Активный радар • Радиус 5 км"
        AppLanguage.PORTUGUESE -> "Radar Ativo • Raio de 5 km"
        AppLanguage.INDONESIAN -> "Radar Aktif • Radius 5 km"
        else -> "Active Radar • 5 km Radius"
    }

    fun radarYou(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "您"
        AppLanguage.JAPANESE -> "あなた"
        AppLanguage.KOREAN -> "나"
        AppLanguage.ARABIC -> "أنت"
        AppLanguage.SPANISH -> "Tú"
        AppLanguage.FRENCH -> "Vous"
        AppLanguage.GERMAN -> "Du"
        AppLanguage.RUSSIAN -> "Вы"
        AppLanguage.PORTUGUESE -> "Você"
        AppLanguage.INDONESIAN -> "Anda"
        else -> "You"
    }

    fun radarTouchHint(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "轻点雷达上的头像查看资料并打招呼"
        AppLanguage.JAPANESE -> "レーダー上のアイコンをタップしてプロフィール確認 & 挨拶"
        AppLanguage.KOREAN -> "레이더 아바타를 터치해 프로필 확인 및 인사하세요"
        AppLanguage.ARABIC -> "المس الصورة الرمزية في الرادار لعرض الملف الشخصي والتحية"
        AppLanguage.SPANISH -> "Toca el avatar en el radar para ver perfil y saludar"
        AppLanguage.FRENCH -> "Touchez un avatar sur le radar pour voir le profil et saluer"
        AppLanguage.GERMAN -> "Tippe auf ein Radar-Avatar, um das Profil zu sehen und zu grüßen"
        AppLanguage.RUSSIAN -> "Коснитесь аватара на радаре, чтобы открыть профиль и поздороваться"
        AppLanguage.PORTUGUESE -> "Toque no avatar no radar para ver o perfil e cumprimentar"
        AppLanguage.INDONESIAN -> "Sentuh avatar di radar untuk melihat profil & menyapa"
        else -> "Tap avatar on radar to view profile & say hi"
    }

    fun radarMaxActive(lang: AppLanguage, count: Int): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "雷达最大范围已激活 • 已展示 $count 位用户 ✨"
        AppLanguage.JAPANESE -> "最大レーダー稼働中 • $count 人のユーザーを表示 ✨"
        AppLanguage.KOREAN -> "최대 레이더 활성화 • ${count}명의 사용자 표시 중 ✨"
        AppLanguage.ARABIC -> "الرادار في أقصى مدى • تم إظهار $count مستخدم ✨"
        AppLanguage.SPANISH -> "Radar Máximo Activo • $count Usuarios Desbloqueados ✨"
        AppLanguage.FRENCH -> "Radar maximal actif • $count utilisateurs débloqués ✨"
        AppLanguage.GERMAN -> "Maximales Radar aktiv • $count Benutzer freigeschaltet ✨"
        AppLanguage.RUSSIAN -> "Максимальный радар активен • Открыто $count пользователей ✨"
        AppLanguage.PORTUGUESE -> "Radar Máximo Ativo • $count Usuários Desbloqueados ✨"
        AppLanguage.INDONESIAN -> "Radar Maksimal Aktif • $count Pengguna Terbuka ✨"
        else -> "Max Radar Active • $count Users Unlocked ✨"
    }

    fun radarExpandPreparingToast(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "正在准备雷达... 正在为您展示身边的好友 ✨"
        AppLanguage.JAPANESE -> "レーダー準備中... 近くのユーザーを表示しています ✨"
        AppLanguage.KOREAN -> "레이더 준비 중... 주변 사용자를 표시합니다 ✨"
        AppLanguage.ARABIC -> "جاري تجهيز الرادار... جاري عرض المستخدمين القريبين منك ✨"
        AppLanguage.SPANISH -> "Preparando radar... Mostrando personas cercanas para ti ✨"
        AppLanguage.FRENCH -> "Préparation du radar... Affichage des personnes proches pour vous ✨"
        AppLanguage.GERMAN -> "Radar wird vorbereitet... Benutzer in der Nähe werden angezeigt ✨"
        AppLanguage.RUSSIAN -> "Подготовка радара... Открываем пользователей рядом с вами ✨"
        AppLanguage.PORTUGUESE -> "Preparando radar... Exibindo pessoas próximas para você ✨"
        AppLanguage.INDONESIAN -> "Mempersiapkan radar... Menampilkan pengguna sekitar untuk Anda ✨"
        else -> "Preparing radar... Unlocking nearby users for you ✨"
    }

    fun qrAddLovyFriendSubtitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "添加 Lovy 好友"
        AppLanguage.JAPANESE -> "Lovy 友達を追加"
        AppLanguage.KOREAN -> "Lovy 친구 추가"
        AppLanguage.ARABIC -> "إضافة أصدقاء Lovy"
        AppLanguage.SPANISH -> "Agregar Amigo Lovy"
        AppLanguage.FRENCH -> "Ajouter un ami Lovy"
        AppLanguage.GERMAN -> "Lovy-Freund hinzufügen"
        AppLanguage.RUSSIAN -> "Добавить друга в Lovy"
        AppLanguage.PORTUGUESE -> "Adicionar Amigo Lovy"
        AppLanguage.INDONESIAN -> "Tambah Teman Lovy"
        else -> "Add Lovy Friends"
    }

    fun qrCopyIdTooltip(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "复制 ID"
        AppLanguage.JAPANESE -> "IDをコピー"
        AppLanguage.KOREAN -> "ID 복사"
        AppLanguage.ARABIC -> "نسخ المعرّف"
        AppLanguage.SPANISH -> "Copiar ID"
        AppLanguage.FRENCH -> "Copier l'identifiant"
        AppLanguage.GERMAN -> "ID kopieren"
        AppLanguage.RUSSIAN -> "Скопировать ID"
        AppLanguage.PORTUGUESE -> "Copiar ID"
        AppLanguage.INDONESIAN -> "Salin ID"
        else -> "Copy ID"
    }

    fun adSponsored(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "赞助"
        AppLanguage.JAPANESE -> "スポンサー"
        AppLanguage.KOREAN -> "스폰서"
        AppLanguage.ARABIC -> "إعلان ممول"
        AppLanguage.SPANISH -> "Patrocinado"
        AppLanguage.FRENCH -> "Sponsorisé"
        AppLanguage.GERMAN -> "Gesponsert"
        AppLanguage.RUSSIAN -> "Спонсировано"
        AppLanguage.PORTUGUESE -> "Patrocinado"
        AppLanguage.INDONESIAN -> "Bersponsor"
        else -> "Sponsored"
    }

    fun adVisit(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "访问"
        AppLanguage.JAPANESE -> "詳細を見る"
        AppLanguage.KOREAN -> "방문하기"
        AppLanguage.ARABIC -> "زيارة"
        AppLanguage.SPANISH -> "Visitar"
        AppLanguage.FRENCH -> "Visiter"
        AppLanguage.GERMAN -> "Besuchen"
        AppLanguage.RUSSIAN -> "Перейти"
        AppLanguage.PORTUGUESE -> "Visitar"
        AppLanguage.INDONESIAN -> "Kunjungi"
        else -> "Visit"
    }

    fun adBadge(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "广告"
        AppLanguage.JAPANESE -> "広告"
        AppLanguage.KOREAN -> "광고"
        AppLanguage.ARABIC -> "إعلان"
        AppLanguage.SPANISH -> "ANUNCIO"
        AppLanguage.FRENCH -> "PUB"
        AppLanguage.GERMAN -> "ANZEIGE"
        AppLanguage.RUSSIAN -> "РЕКЛАМА"
        AppLanguage.PORTUGUESE -> "ANÚNCIO"
        AppLanguage.INDONESIAN -> "IKLAN"
        else -> "AD"
    }

    fun languagePickerTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "选择语言 / Language (LO - EN)"
        AppLanguage.JAPANESE -> "言語を選択 / Language (LO - EN)"
        AppLanguage.KOREAN -> "언어 선택 / Language (LO - EN)"
        AppLanguage.ARABIC -> "اختر اللغة / Language (LO - EN)"
        AppLanguage.SPANISH -> "Seleccionar Idioma / Language (LO - EN)"
        AppLanguage.FRENCH -> "Choisir la langue / Language (LO - EN)"
        AppLanguage.GERMAN -> "Sprache wählen / Language (LO - EN)"
        AppLanguage.RUSSIAN -> "Выбор языка / Language (LO - EN)"
        AppLanguage.PORTUGUESE -> "Escolher Idioma / Language (LO - EN)"
        AppLanguage.INDONESIAN -> "Pilih Bahasa / Language (LO - EN)"
        else -> "Select Language (LO - EN)"
    }

    fun languagePickerDesc(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "LO - EN 系统自动检测全球任何国家/地区的本地语言（中文、日语、阿拉伯语、印尼语等）。"
        AppLanguage.JAPANESE -> "LO - ENシステムは世界中のあらゆる国の現地言語（中国語、日本語、アラビア語、インドネシア語など）を自動検出します。"
        AppLanguage.KOREAN -> "LO - EN 시스템은 전 세계 어느 국가의 현지 언어(중국어, 일본어, 아랍어, 인도네시아어 등)든 자동 감지합니다."
        AppLanguage.ARABIC -> "نظام LO - EN يكتشف تلقائياً اللغة المحلية لأي بلد في العالم (العربية، الصينية، اليابانية، الإندونيسية، وغيرها)."
        AppLanguage.SPANISH -> "El sistema LO - EN detecta automáticamente el idioma local de cualquier país del mundo (chino, japonés, árabe, indonesio, etc.)."
        AppLanguage.FRENCH -> "Le système LO - EN détecte automatiquement la langue locale de n'importe quel pays au monde (chinois, japonais, arabe, indonésien, etc.)."
        AppLanguage.GERMAN -> "Das LO - EN-System erkennt automatisch die Landessprache jedes Landes weltweit (Chinesisch, Japanisch, Arabisch, Indonesisch usw.)."
        AppLanguage.RUSSIAN -> "Система LO - EN автоматически определяет местный язык любой страны мира (китайский, японский, арабский, индонезийский и др.)."
        AppLanguage.PORTUGUESE -> "O sistema LO - EN detecta automaticamente o idioma local de qualquer país do mundo (chinês, japonês, árabe, indonésio, etc.)."
        AppLanguage.INDONESIAN -> "Sistem LO - EN otomatis mendeteksi bahasa lokal negara manapun di seluruh dunia (Cina, Jepang, Arab, Indonesia, dll)."
        else -> "LO - EN system automatically detects the local language of any country worldwide (Chinese, Japanese, Arabic, Indonesian, etc.)."
    }

    fun languagePickerLocalAutoTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "🌐 LO (本地国家语言自动匹配)"
        AppLanguage.JAPANESE -> "🌐 LO (国別ローカル自動)"
        AppLanguage.KOREAN -> "🌐 LO (국가별 현지 자동)"
        AppLanguage.ARABIC -> "🌐 LO (تلقائي محلي حسب البلد)"
        AppLanguage.SPANISH -> "🌐 LO (Local Automático del País)"
        AppLanguage.FRENCH -> "🌐 LO (Local automatique par pays)"
        AppLanguage.GERMAN -> "🌐 LO (Lokal automatisch)"
        AppLanguage.RUSSIAN -> "🌐 LO (Локально авто)"
        AppLanguage.PORTUGUESE -> "🌐 LO (Local Automático do País)"
        AppLanguage.INDONESIAN -> "🌐 LO (Lokal Otomatis Negara)"
        else -> "🌐 LO (Local Country Auto)"
    }

    fun languagePickerDetectedLang(lang: AppLanguage, name: String, nativeName: String): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "检测到的语言：$name ($nativeName)"
        AppLanguage.JAPANESE -> "検出された言語: $name ($nativeName)"
        AppLanguage.KOREAN -> "감지된 언어: $name ($nativeName)"
        AppLanguage.ARABIC -> "اللغة المكتشفة: $name ($nativeName)"
        AppLanguage.SPANISH -> "Idioma detectado: $name ($nativeName)"
        AppLanguage.FRENCH -> "Langue détectée : $name ($nativeName)"
        AppLanguage.GERMAN -> "Erkannte Sprache: $name ($nativeName)"
        AppLanguage.RUSSIAN -> "Обнаруженный язык: $name ($nativeName)"
        AppLanguage.PORTUGUESE -> "Idioma detectado: $name ($nativeName)"
        AppLanguage.INDONESIAN -> "Bahasa terdeteksi: $name ($nativeName)"
        else -> "Detected language: $name ($nativeName)"
    }

    fun languagePickerDetectedLocation(lang: AppLanguage, area: String): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "位置：$area"
        AppLanguage.JAPANESE -> "位置: $area"
        AppLanguage.KOREAN -> "위치: $area"
        AppLanguage.ARABIC -> "الموقع: $area"
        AppLanguage.SPANISH -> "Ubicación: $area"
        AppLanguage.FRENCH -> "Emplacement : $area"
        AppLanguage.GERMAN -> "Standort: $area"
        AppLanguage.RUSSIAN -> "Местоположение: $area"
        AppLanguage.PORTUGUESE -> "Localização: $area"
        AppLanguage.INDONESIAN -> "Lokasi: $area"
        else -> "Location: $area"
    }

    fun languagePickerEnglishTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "🇬🇧 EN (全球通用英语)"
        AppLanguage.JAPANESE -> "🇬🇧 EN (グローバル英語)"
        AppLanguage.KOREAN -> "🇬🇧 EN (글로벌 영어)"
        AppLanguage.ARABIC -> "🇬🇧 EN (إنجليزية عالمية)"
        AppLanguage.SPANISH -> "🇬🇧 EN (Inglés Global)"
        AppLanguage.FRENCH -> "🇬🇧 EN (Anglais global)"
        AppLanguage.GERMAN -> "🇬🇧 EN (Globales Englisch)"
        AppLanguage.RUSSIAN -> "🇬🇧 EN (Глобальный английский)"
        AppLanguage.PORTUGUESE -> "🇬🇧 EN (Inglês Global)"
        AppLanguage.INDONESIAN -> "🇬🇧 EN (English Global)"
        else -> "🇬🇧 EN (English Global)"
    }

    fun languagePickerEnglishDesc(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "国际语言模式"
        AppLanguage.JAPANESE -> "国際言語モード"
        AppLanguage.KOREAN -> "국제 언어 모드"
        AppLanguage.ARABIC -> "وضع اللغة الدولية"
        AppLanguage.SPANISH -> "Modo de idioma internacional"
        AppLanguage.FRENCH -> "Mode langue internationale"
        AppLanguage.GERMAN -> "Internationaler Sprachmodus"
        AppLanguage.RUSSIAN -> "Международный языковой режим"
        AppLanguage.PORTUGUESE -> "Modo de idioma internacional"
        AppLanguage.INDONESIAN -> "Mode bahasa internasional"
        else -> "International language mode"
    }

    fun languagePickerTestOtherLangs(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "直接体验其他国家语言："
        AppLanguage.JAPANESE -> "他の国の言語を直接試す:"
        AppLanguage.KOREAN -> "다른 국가 언어 직접 테스트:"
        AppLanguage.ARABIC -> "تجربة لغات البلدان الأخرى مباشرة:"
        AppLanguage.SPANISH -> "Probar directamente idiomas de otros países:"
        AppLanguage.FRENCH -> "Tester directement les langues d'autres pays :"
        AppLanguage.GERMAN -> "Sprachen anderer Länder direkt testen:"
        AppLanguage.RUSSIAN -> "Опробовать языки других стран:"
        AppLanguage.PORTUGUESE -> "Testar diretamente idiomas de outros países:"
        AppLanguage.INDONESIAN -> "Uji Coba Langsung Bahasa Negara Lain:"
        else -> "Directly Test Other Country Languages:"
    }

    fun deleteAccountSuccessToast(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "您的账号及所有数据已成功删除。"
        AppLanguage.JAPANESE -> "アカウントとすべてのデータが正常に削除されました。"
        AppLanguage.KOREAN -> "계정 및 모든 데이터가 성공적으로 삭제되었습니다."
        AppLanguage.ARABIC -> "تم حذف حسابك وجميع بياناتك بنجاح."
        AppLanguage.SPANISH -> "Tu cuenta y todos tus datos se han eliminado correctamente."
        AppLanguage.FRENCH -> "Votre compte et toutes vos données ont été supprimés avec succès."
        AppLanguage.GERMAN -> "Dein Konto und alle Daten wurden erfolgreich gelöscht."
        AppLanguage.RUSSIAN -> "Ваш аккаунт и все данные были успешно удалены."
        AppLanguage.PORTUGUESE -> "Sua conta e todos os dados foram excluídos com sucesso."
        AppLanguage.INDONESIAN -> "Akun dan seluruh data Anda telah berhasil dihapus."
        else -> "Your account and all data have been successfully deleted."
    }

    fun testNotificationSampleMessage(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "通知与震动反馈运行正常！📳✨"
        AppLanguage.JAPANESE -> "通知とバイブレーションが正常に機能しています！📳✨"
        AppLanguage.KOREAN -> "알림 및 진동 효과가 정상 작동 중입니다! 📳✨"
        AppLanguage.ARABIC -> "الإشعارات والاهتزاز يعملان بشكل مثالي! 📳✨"
        AppLanguage.SPANISH -> "¡Las notificaciones y la vibración funcionan de manera óptima! 📳✨"
        AppLanguage.FRENCH -> "Les notifications et la vibration fonctionnent de manière optimale ! 📳✨"
        AppLanguage.GERMAN -> "Benachrichtigungen und Vibration funktionieren optimal! 📳✨"
        AppLanguage.RUSSIAN -> "Уведомления и вибрация работают отлично! 📳✨"
        AppLanguage.PORTUGUESE -> "Notificações e vibração funcionando de forma ideal! 📳✨"
        AppLanguage.INDONESIAN -> "Notifikasi & efek getar berhasil berfungsi optimal! 📳✨"
        else -> "Notifications & vibration feedback are functioning optimally! 📳✨"
    }

    fun testNotificationTriggerToast(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "正在触发新消息通知与震动 🔔📳"
        AppLanguage.JAPANESE -> "新着メッセージ通知とバイブレーションを実行 🔔📳"
        AppLanguage.KOREAN -> "새 메시지 알림 및 진동 실행 중 🔔📳"
        AppLanguage.ARABIC -> "تفعيل إشعار الرسالة الجديدة والاهتزاز 🔔📳"
        AppLanguage.SPANISH -> "Activando notificación y vibración de nuevo mensaje 🔔📳"
        AppLanguage.FRENCH -> "Déclenchement de la notification et vibration de nouveau message 🔔📳"
        AppLanguage.GERMAN -> "Benachrichtigung & Vibration für neue Nachricht ausgelöst 🔔📳"
        AppLanguage.RUSSIAN -> "Запуск уведомления и вибрации для нового сообщения 🔔📳"
        AppLanguage.PORTUGUESE -> "Disparando notificação e vibração de nova mensagem 🔔📳"
        AppLanguage.INDONESIAN -> "Memicu notifikasi & efek getar pesan baru 🔔📳"
        else -> "Triggering new message notification & vibration 🔔📳"
    }

    fun locationPermissionGrantedToast(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "位置权限已成功开启"
        AppLanguage.JAPANESE -> "位置情報の権限が有効になりました"
        AppLanguage.KOREAN -> "위치 권한이 활성화되었습니다"
        AppLanguage.ARABIC -> "تم تفعيل إذن الموقع بنجاح"
        AppLanguage.SPANISH -> "Permiso de ubicación activado con éxito"
        AppLanguage.FRENCH -> "Autorisation de localisation activée avec succès"
        AppLanguage.GERMAN -> "Standortberechtigung erfolgreich aktiviert"
        AppLanguage.RUSSIAN -> "Разрешение на местоположение успешно включено"
        AppLanguage.PORTUGUESE -> "Permissão de localização ativada com sucesso"
        AppLanguage.INDONESIAN -> "Izin lokasi berhasil diaktifkan"
        else -> "Location permission successfully enabled"
    }

    fun locationPermissionDeniedToast(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "尚未授予位置权限"
        AppLanguage.JAPANESE -> "位置情報の権限が許可されていません"
        AppLanguage.KOREAN -> "위치 권한이 부여되지 않았습니다"
        AppLanguage.ARABIC -> "لم يتم منح إذن الموقع"
        AppLanguage.SPANISH -> "Permiso de ubicación no concedido"
        AppLanguage.FRENCH -> "Autorisation de localisation non accordée"
        AppLanguage.GERMAN -> "Standortberechtigung nicht erteilt"
        AppLanguage.RUSSIAN -> "Разрешение на местоположение не предоставлено"
        AppLanguage.PORTUGUESE -> "Permissão de localização não concedida"
        AppLanguage.INDONESIAN -> "Izin lokasi belum diberikan"
        else -> "Location permission not granted"
    }

    fun developerModeToast(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "开发者模式：服务器设置 ☁️"
        AppLanguage.JAPANESE -> "開発者モード: サーバー設定 ☁️"
        AppLanguage.KOREAN -> "개발자 모드: 서버 설정 ☁️"
        AppLanguage.ARABIC -> "وضع المطور: إعدادات الخادم ☁️"
        AppLanguage.SPANISH -> "Modo Desarrollador: Configuración del Servidor ☁️"
        AppLanguage.FRENCH -> "Mode développeur : Paramètres du serveur ☁️"
        AppLanguage.GERMAN -> "Entwicklermodus: Servereinstellungen ☁️"
        AppLanguage.RUSSIAN -> "Режим разработчика: Настройки сервера ☁️"
        AppLanguage.PORTUGUESE -> "Modo Desenvolvedor: Configurações do Servidor ☁️"
        AppLanguage.INDONESIAN -> "Mode Pengembang: Pengaturan Server ☁️"
        else -> "Developer Mode: Server Settings ☁️"
    }

    fun chatLinkOpenError(lang: AppLanguage, url: String): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "无法打开链接：$url"
        AppLanguage.JAPANESE -> "リンクを開けません: $url"
        AppLanguage.KOREAN -> "링크를 열 수 없습니다: $url"
        AppLanguage.ARABIC -> "تعذر فتح الرابط: $url"
        AppLanguage.SPANISH -> "No se puede abrir el enlace: $url"
        AppLanguage.FRENCH -> "Impossible d'ouvrir le lien : $url"
        AppLanguage.GERMAN -> "Link kann nicht geöffnet werden: $url"
        AppLanguage.RUSSIAN -> "Не удалось открыть ссылку: $url"
        AppLanguage.PORTUGUESE -> "Não foi possível abrir o link: $url"
        AppLanguage.INDONESIAN -> "Tidak dapat membuka tautan: $url"
        else -> "Unable to open link: $url"
    }

    fun googleConnecting(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "正在连接 Google 账号..."
        AppLanguage.JAPANESE -> "Googleアカウントに接続中..."
        AppLanguage.KOREAN -> "Google 계정 연결 중..."
        AppLanguage.ARABIC -> "جاري الاتصال بحساب Google..."
        AppLanguage.SPANISH -> "Conectando cuenta de Google..."
        AppLanguage.FRENCH -> "Connexion au compte Google..."
        AppLanguage.GERMAN -> "Google-Konto wird verbunden..."
        AppLanguage.RUSSIAN -> "Подключение аккаунта Google..."
        AppLanguage.PORTUGUESE -> "Conectando conta do Google..."
        AppLanguage.INDONESIAN -> "Menghubungkan akun Google..."
        else -> "Connecting Google account..."
    }

    fun uploading(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "上传中..."
        AppLanguage.JAPANESE -> "アップロード中..."
        AppLanguage.KOREAN -> "업로드 중..."
        AppLanguage.ARABIC -> "جاري الرفع..."
        AppLanguage.SPANISH -> "Subiendo..."
        AppLanguage.FRENCH -> "Téléchargement..."
        AppLanguage.GERMAN -> "Wird hochgeladen..."
        AppLanguage.RUSSIAN -> "Загрузка..."
        AppLanguage.PORTUGUESE -> "Enviando..."
        AppLanguage.INDONESIAN -> "Mengunggah..."
        else -> "Uploading..."
    }

    fun supabaseTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "云端连接与同步"
        AppLanguage.JAPANESE -> "クラウド接続 & 同期"
        AppLanguage.KOREAN -> "클라우드 연결 & 동기화"
        AppLanguage.ARABIC -> "اتصال السحابة والمزامنة"
        AppLanguage.SPANISH -> "Conexión y Sincronización en la Nube"
        AppLanguage.FRENCH -> "Connexion cloud & Synchronisation"
        AppLanguage.GERMAN -> "Cloud-Verbindung & Synchronisierung"
        AppLanguage.RUSSIAN -> "Облачное подключение и синхронизация"
        AppLanguage.PORTUGUESE -> "Conexão na Nuvem e Sincronização"
        AppLanguage.INDONESIAN -> "Koneksi Cloud & Sinkronisasi"
        else -> "Cloud Connection & Sync"
    }

    fun supabaseConfigTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "凭证配置"
        AppLanguage.JAPANESE -> "認証情報の設定"
        AppLanguage.KOREAN -> "자격 증명 설정"
        AppLanguage.ARABIC -> "إعداد بيانات الاعتماد"
        AppLanguage.SPANISH -> "Configuración de Credenciales"
        AppLanguage.FRENCH -> "Configuration des identifiants"
        AppLanguage.GERMAN -> "Anmeldedaten-Konfiguration"
        AppLanguage.RUSSIAN -> "Настройка учетных данных"
        AppLanguage.PORTUGUESE -> "Configuração de Credenciais"
        AppLanguage.INDONESIAN -> "Konfigurasi Kredensial"
        else -> "Credentials Configuration"
    }

    fun supabaseBtnSave(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "保存"
        AppLanguage.JAPANESE -> "保存"
        AppLanguage.KOREAN -> "저장"
        AppLanguage.ARABIC -> "حفظ"
        AppLanguage.SPANISH -> "Guardar"
        AppLanguage.FRENCH -> "Enregistrer"
        AppLanguage.GERMAN -> "Speichern"
        AppLanguage.RUSSIAN -> "Сохранить"
        AppLanguage.PORTUGUESE -> "Salvar"
        AppLanguage.INDONESIAN -> "Simpan"
        else -> "Save"
    }

    fun supabaseBtnClear(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "清除凭证"
        AppLanguage.JAPANESE -> "認証情報を削除"
        AppLanguage.KOREAN -> "자격 증명 삭제"
        AppLanguage.ARABIC -> "مسح بيانات الاعتماد"
        AppLanguage.SPANISH -> "Borrar Credenciales"
        AppLanguage.FRENCH -> "Effacer les identifiants"
        AppLanguage.GERMAN -> "Anmeldedaten löschen"
        AppLanguage.RUSSIAN -> "Удалить учетные данные"
        AppLanguage.PORTUGUESE -> "Limpar Credenciais"
        AppLanguage.INDONESIAN -> "Hapus Kredensial"
        else -> "Clear Credentials"
    }

    fun supabaseSavedSnackbar(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "云端设置已成功保存！"
        AppLanguage.JAPANESE -> "クラウド設定を保存しました！"
        AppLanguage.KOREAN -> "클라우드 설정이 성공적으로 저장되었습니다!"
        AppLanguage.ARABIC -> "تم حفظ إعدادات السحابة بنجاح!"
        AppLanguage.SPANISH -> "¡Configuración de la nube guardada con éxito!"
        AppLanguage.FRENCH -> "Paramètres cloud enregistrés avec succès !"
        AppLanguage.GERMAN -> "Cloud-Einstellungen erfolgreich gespeichert!"
        AppLanguage.RUSSIAN -> "Облачные настройки успешно сохранены!"
        AppLanguage.PORTUGUESE -> "Configurações da nuvem salvas com sucesso!"
        AppLanguage.INDONESIAN -> "Pengaturan cloud berhasil disimpan!"
        else -> "Cloud settings saved successfully!"
    }

    fun supabaseScriptCopiedSnackbar(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "数据结构脚本已复制！"
        AppLanguage.JAPANESE -> "データ構造スクリプトをコピーしました！"
        AppLanguage.KOREAN -> "데이터 구조 스크립트가 복사되었습니다!"
        AppLanguage.ARABIC -> "تم نسخ سكريبت هيكل البيانات!"
        AppLanguage.SPANISH -> "¡Guión de estructura de datos copiado!"
        AppLanguage.FRENCH -> "Script de structure de données copié !"
        AppLanguage.GERMAN -> "Datenstruktur-Skript kopiert!"
        AppLanguage.RUSSIAN -> "Скрипт структуры данных скопирован!"
        AppLanguage.PORTUGUESE -> "Script de estrutura de dados copiado!"
        AppLanguage.INDONESIAN -> "Skrip struktur data berhasil disalin!"
        else -> "Data structure script copied!"
    }

    fun supabaseCronCopiedSnackbar(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "定时清理脚本已复制！"
        AppLanguage.JAPANESE -> "自動クリーンアップスクリプトをコピーしました！"
        AppLanguage.KOREAN -> "자동 정리 스크립트가 복사되었습니다!"
        AppLanguage.ARABIC -> "تم نسخ سكريبت التنظيف التلقائي!"
        AppLanguage.SPANISH -> "¡Guión de limpieza automática copiado!"
        AppLanguage.FRENCH -> "Script de nettoyage automatique copié !"
        AppLanguage.GERMAN -> "Bereinigungs-Cronjob-Skript kopiert!"
        AppLanguage.RUSSIAN -> "Скрипт автоочистки скопирован!"
        AppLanguage.PORTUGUESE -> "Script de limpeza automática copiado!"
        AppLanguage.INDONESIAN -> "Skrip cronjob pembersihan berhasil disalin!"
        else -> "Cleanup cronjob script copied!"
    }

    fun supabaseBtnCopyScript(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "复制代码"
        AppLanguage.JAPANESE -> "スクリプトをコピー"
        AppLanguage.KOREAN -> "스크립트 복사"
        AppLanguage.ARABIC -> "نسخ السكريبت"
        AppLanguage.SPANISH -> "Copiar Guión"
        AppLanguage.FRENCH -> "Copier le script"
        AppLanguage.GERMAN -> "Skript kopieren"
        AppLanguage.RUSSIAN -> "Скопировать скрипт"
        AppLanguage.PORTUGUESE -> "Copiar Script"
        AppLanguage.INDONESIAN -> "Salin Skrip"
        else -> "Copy Script"
    }

    fun supabaseStructureTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "数据同步结构"
        AppLanguage.JAPANESE -> "データ同期構造"
        AppLanguage.KOREAN -> "데이터 동기화 구조"
        AppLanguage.ARABIC -> "بنية مزامنة البيانات"
        AppLanguage.SPANISH -> "Estructura de Sincronización de Datos"
        AppLanguage.FRENCH -> "Structure de synchronisation des données"
        AppLanguage.GERMAN -> "Datensynchronisationsstruktur"
        AppLanguage.RUSSIAN -> "Структура синхронизации данных"
        AppLanguage.PORTUGUESE -> "Estrutura de Sincronização de Dados"
        AppLanguage.INDONESIAN -> "Struktur Sinkronisasi Data"
        else -> "Data Synchronization Structure"
    }

    fun supabaseStructureDesc(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "用于同步 Lovy Chat 消息和个人资料的云数据结构初始化脚本："
        AppLanguage.JAPANESE -> "Lovy Chat のメッセージとプロフィールを同期するためのクラウドデータ構造初期化スクリプト:"
        AppLanguage.KOREAN -> "Lovy Chat 메시지 및 프로필 동기화를 위한 클라우드 데이터 구조 초기화 스크립트:"
        AppLanguage.ARABIC -> "سكريبت تهيئة بنية البيانات السحابية لمزامنة رسائل وملفات Lovy Chat الشخصية:"
        AppLanguage.SPANISH -> "Script de inicialización de estructura de datos en la nube para sincronizar mensajes y perfiles de Lovy Chat:"
        AppLanguage.FRENCH -> "Script d'initialisation de la structure de données cloud pour synchroniser les messages et profils Lovy Chat :"
        AppLanguage.GERMAN -> "Skript zur Initialisierung der Cloud-Datenstruktur für die Synchronisierung von Lovy Chat-Nachrichten und -Profilen:"
        AppLanguage.RUSSIAN -> "Скрипт инициализации облачной структуры данных для синхронизации сообщений и профилей Lovy Chat:"
        AppLanguage.PORTUGUESE -> "Script de inicialização da estrutura de dados na nuvem para sincronizar mensagens e perfis do Lovy Chat:"
        AppLanguage.INDONESIAN -> "Skrip inisialisasi struktur data cloud untuk sinkronisasi pesan & profil Lovy Chat:"
        else -> "Cloud data structure initialization script for synchronizing Lovy Chat messages & profiles:"
    }

    fun supabaseCronTitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "定时任务与自动清理"
        AppLanguage.JAPANESE -> "定期クローン & 自動クリーンアップ"
        AppLanguage.KOREAN -> "크론 작업 & 자동 정리"
        AppLanguage.ARABIC -> "المهام المجدولة والتنظيف التلقائي"
        AppLanguage.SPANISH -> "Tareas Programadas y Limpieza Automática"
        AppLanguage.FRENCH -> "Tâches planifiées & Nettoyage automatique"
        AppLanguage.GERMAN -> "Cronjobs & Automatische Bereinigung"
        AppLanguage.RUSSIAN -> "Cronjob и автоматическая очистка"
        AppLanguage.PORTUGUESE -> "Tarefas Agendadas e Limpeza Automática"
        AppLanguage.INDONESIAN -> "Cronjob & Pembersihan Otomatis"
        else -> "Cronjob & Automatic Cleanup"
    }

    fun supabaseCronSubtitle(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "自动清理不活跃超过 15 天的账号、过期消息与无主数据"
        AppLanguage.JAPANESE -> "15日以上非アクティブなアカウント、古いメッセージ、孤立データを削除"
        AppLanguage.KOREAN -> "15일 이상 미활동 계정, 오래된 메시지, 고아 데이터 삭제"
        AppLanguage.ARABIC -> "حذف الحسابات غير النشطة > 15 يوماً، الرسائل القديمة، والبيانات المعلقة"
        AppLanguage.SPANISH -> "Eliminar cuentas inactivas > 15 días, mensajes antiguos y datos huérfanos"
        AppLanguage.FRENCH -> "Supprimer les comptes inactifs > 15 jours, les messages obsolètes et les données orphelines"
        AppLanguage.GERMAN -> "Inaktive Konten > 15 Tage, veraltete Nachrichten und verwaiste Daten löschen"
        AppLanguage.RUSSIAN -> "Удаление неактивных аккаунтов > 15 дней, устаревших сообщений и потерянных данных"
        AppLanguage.PORTUGUESE -> "Excluir contas inativas > 15 dias, mensagens antigas e dados órfãos"
        AppLanguage.INDONESIAN -> "Hapus akun inaktif > 15 hari, pesan usang, dan data yatim"
        else -> "Purge inactive accounts > 15 days, obsolete messages, and orphan data"
    }

    fun supabaseCronDesc(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "在 Supabase 控制台的 SQL Editor 中运行此脚本。它将安装 pg_cron 扩展、级联外键，并设置每日自动任务（UTC 03:00）清理 15 天未活跃的账户及过期历史消息。"
        AppLanguage.JAPANESE -> "SupabaseダッシュボードのSQL Editorでこのスクリプトを実行してください。pg_cron拡張、CASCADE外部キー、および毎日UTC 03:00に非アクティブアカウントと古いチャットを整理する自動化をセットアップします。"
        AppLanguage.KOREAN -> "Supabase 대시보드의 SQL Editor에서 이 스크립트를 실행하세요. pg_cron 확장, CASCADE 외래 키 및 매일 03:00 UTC에 15일 이상 미활동 계정과 오래된 대화를 정리하는 일일 자동화 기능을 설치합니다."
        AppLanguage.ARABIC -> "قم بتشغيل هذا السكريبت في محرر SQL في لوحة تحكم Supabase. يقوم بتثبيت إضافة pg_cron ومفاتيح الربط التلقائي، ووظيفة يومية (الساعة 03:00 UTC) لحذف الحسابات غير النشطة والرسائل القديمة."
        AppLanguage.SPANISH -> "Ejecuta este script en el Editor SQL de Supabase. Instala la extensión pg_cron, claves foráneas CASCADE y una función diaria (03:00 UTC) para purgar cuentas inactivas > 15 días y chats antiguos."
        AppLanguage.FRENCH -> "Exécutez ce script dans l'éditeur SQL de Supabase. Il installe l'extension pg_cron, les clés étrangères CASCADE et une tâche quotidienne (03:00 UTC) pour purger les comptes inactifs > 15 jours et l'historique obsolète."
        AppLanguage.GERMAN -> "Führe dieses Skript im SQL-Editor des Supabase-Dashboards aus. Es richtet pg_cron, CASCADE-Fremdschlüssel und eine tägliche Funktion (03:00 UTC) zur Bereinigung inaktiver Konten und alter Chats ein."
        AppLanguage.RUSSIAN -> "Запустите этот скрипт в редакторе SQL в панели Supabase. Он устанавливает расширение pg_cron, каскадные внешние ключи и ежедневную задачу (03:00 UTC) для очистки неактивных аккаунтов и старых чатов."
        AppLanguage.PORTUGUESE -> "Execute este script no Editor SQL do Supabase. Ele instala a extensão pg_cron, chaves estrangeiras CASCADE e uma rotina diária (03:00 UTC) para limpar contas inativas > 15 dias e conversas antigas."
        AppLanguage.INDONESIAN -> "Jalankan skrip ini di SQL Editor dashboard Supabase. Skrip ini memasang ekstensi pg_cron, foreign key CASCADE, dan fungsi otomatisasi harian (pukul 03:00 UTC) untuk menghapus akun tidak aktif > 15 hari dan membersihkan riwayat obrolan usang."
        else -> "Run this script in Supabase dashboard SQL Editor. It installs pg_cron extension, CASCADE foreign keys, and a daily automated function (03:00 UTC) to purge inactive accounts > 15 days and obsolete chat history."
    }

    fun friendAcceptedToast(lang: AppLanguage, name: String): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "已同意好友请求！$name 已加入聊天列表。"
        AppLanguage.JAPANESE -> "友達リクエストを承認しました！$name とのチャットが開始されました。"
        AppLanguage.KOREAN -> "친구 요청을 수락했습니다! $name 님이 대화 목록에 추가되었습니다."
        AppLanguage.ARABIC -> "تمت الموافقة على طلب الصداقة! تم إضافة $name إلى المحادثات."
        AppLanguage.SPANISH -> "¡Solicitud aceptada! $name se agregó a tus chats."
        AppLanguage.FRENCH -> "Demande acceptée ! $name a été ajouté aux discussions."
        AppLanguage.GERMAN -> "Freundschaftsanfrage angenommen! $name wurde zu den Chats hinzugefügt."
        AppLanguage.RUSSIAN -> "Запрос принят! $name добавлен(а) в список чатов."
        AppLanguage.PORTUGUESE -> "Pedido aceito! $name foi adicionado(a) às conversas."
        AppLanguage.INDONESIAN -> "Pertemanan disetujui! Obrolan dengan $name kini masuk ke menu Obrolan."
        else -> "Friend request accepted! $name added to Chats."
    }

    fun friendIgnoredToast(lang: AppLanguage, name: String): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "已忽略来自 $name 的好友请求。"
        AppLanguage.JAPANESE -> "$name からのリクエストを無視しました。"
        AppLanguage.KOREAN -> "$name 님의 요청을 무시했습니다."
        AppLanguage.ARABIC -> "تم تجاهل طلب $name."
        AppLanguage.SPANISH -> "Solicitud de $name ignorada."
        AppLanguage.FRENCH -> "Demande de $name ignorée."
        AppLanguage.GERMAN -> "Anfrage von $name ignoriert."
        AppLanguage.RUSSIAN -> "Запрос от $name проигнорирован."
        AppLanguage.PORTUGUESE -> "Pedido de $name ignorado."
        AppLanguage.INDONESIAN -> "Permintaan dari $name diabaikan."
        else -> "Request from $name ignored."
    }

    fun qrBarcodeNotRegisteredDesc(lang: AppLanguage, code: String): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "条形码 / 二维码 '$code' 在 Lovy Chat 用户数据库中不存在。请确保扫描的是有效的 Lovy Chat 个人二维码。"
        AppLanguage.JAPANESE -> "バーコード / QRコード「$code」はLovy Chatユーザーデータベースに登録されていません。正しいLovy ChatプロフィールQRコードをスキャンしてください。"
        AppLanguage.KOREAN -> "바코드 / QR 코드 '$code'가 Lovy Chat 사용자 데이터베이스에 등록되어 있지 않습니다. 올바른 Lovy Chat 프로필 QR 코드를 스캔해 주세요."
        AppLanguage.ARABIC -> "الرمز '$code' غير مسجل في قاعدة بيانات مستخدمي Lovy Chat. يرجى التأكد من مسح رمز QR صالح."
        AppLanguage.SPANISH -> "El código '$code' no está registrado en Lovy Chat. Asegúrate de escanear un código QR de perfil válido."
        AppLanguage.FRENCH -> "Le code '$code' n'est pas enregistré sur Lovy Chat. Veuillez scanner un code QR valide."
        AppLanguage.GERMAN -> "Der Code '$code' ist in Lovy Chat nicht registriert. Bitte scanne einen gültigen Profil-QR-Code."
        AppLanguage.RUSSIAN -> "Код '$code' не зарегистрирован в Lovy Chat. Убедитесь, что сканируете действительный QR-код профиля."
        AppLanguage.PORTUGUESE -> "O código '$code' não está registrado no Lovy Chat. Certifique-se de escanear um QR code de perfil válido."
        AppLanguage.INDONESIAN -> "Kode barcode / QR '$code' tidak terdaftar di database pengguna Lovy Chat. Pastikan yang dipindai adalah kode QR profil Lovy Chat yang valid."
        else -> "The barcode / QR code '$code' is not registered in Lovy Chat. Please ensure you scan a valid Lovy Chat profile QR code."
    }

    fun qrScanAgainBtn(lang: AppLanguage): String = when (resolveLang(lang)) {
        AppLanguage.CHINESE -> "重新扫描"
        AppLanguage.JAPANESE -> "もう一度スキャン"
        AppLanguage.KOREAN -> "다시 스캔하기"
        AppLanguage.ARABIC -> "إعادة المسح"
        AppLanguage.SPANISH -> "Escanear de nuevo"
        AppLanguage.FRENCH -> "Scanner à nouveau"
        AppLanguage.GERMAN -> "Erneut scannen"
        AppLanguage.RUSSIAN -> "Сканировать снова"
        AppLanguage.PORTUGUESE -> "Escanear novamente"
        AppLanguage.INDONESIAN -> "Pindai Lagi"
        else -> "Scan Again"
    }
}

