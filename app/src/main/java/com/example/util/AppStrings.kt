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
        AppLanguage.CHINESE -> "以下用户给您发送了消息或问候，但尚未在您的好友列表中。接受请求即可正式成为好友。"
        AppLanguage.JAPANESE -> "以下のユーザーからメッセージや挨拶が届いています。承認して友達リストに追加しましょう。"
        AppLanguage.KOREAN -> "아래 사용자들이 메시지나 인사를 보냈습니다. 수락하여 공식 친구로 추가하세요."
        AppLanguage.ARABIC -> "أرسل لك المستخدمون أدناه رسائل أو تحيات. اقبل الطلب لإضافتهم كأصدقاء رسميين."
        AppLanguage.SPANISH -> "Los siguientes usuarios te enviaron mensajes o saludos. Acepta para agregarlos a tus contactos."
        AppLanguage.FRENCH -> "Ces personnes vous ont envoyé un message. Acceptez pour les ajouter en ami."
        AppLanguage.GERMAN -> "Die folgenden Personen haben dir geschrieben. Akzeptiere die Anfrage, um Freunde zu werden."
        AppLanguage.RUSSIAN -> "Эти пользователи написали вам. Примите запрос, чтобы добавить их в друзья."
        AppLanguage.PORTUGUESE -> "Os usuários abaixo enviaram mensagens. Aceite o pedido para adicioná-los aos seus amigos."
        AppLanguage.INDONESIAN -> "Pengguna di bawah ini mengirimi Anda pesan obrolan atau salam, namun belum ada di Kontak Saya. Terima permintaan untuk menjadikannya teman resmi."
        else -> "Users below sent you messages or greetings. Accept to add them as official friends."
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
        AppLanguage.JAPANESE -> "マイクルコード"
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
}
