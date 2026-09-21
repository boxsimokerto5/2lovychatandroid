package com.example.data

import com.example.model.BottleMessage
import com.example.model.ChatConversation
import com.example.model.ChatMessage
import com.example.model.Gender
import com.example.model.MomentItem
import com.example.model.User

object MockDataSource {
    val initialNearbyUsers = listOf(
        User(
            id = "u1",
            name = "Siti Rahma",
            gender = Gender.FEMALE,
            age = 22,
            distanceMeters = 95,
            bio = "Suka ngopi dan denger musik santai ☕🎧",
            avatarColorHex = 0xFFEC407A,
            isOnline = true,
            city = "Tebet, Jaksel",
            avatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=300&q=80"
        ),
        User(
            id = "u2",
            name = "Rian Pratama",
            gender = Gender.MALE,
            age = 25,
            distanceMeters = 230,
            bio = "Fotografi & touring. Cari temen ngobrol asik 📸",
            avatarColorHex = 0xFF42A5F5,
            isOnline = true,
            city = "Pancoran, Jaksel",
            avatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=300&q=80"
        ),
        User(
            id = "u3",
            name = "Nadia Putri",
            gender = Gender.FEMALE,
            age = 21,
            distanceMeters = 410,
            bio = "Mahasiswi semester akhir butuh hiburan ✨",
            avatarColorHex = 0xFFAB47BC,
            isOnline = false,
            city = "Kuningan, Jaksel",
            avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=300&q=80"
        ),
        User(
            id = "u4",
            name = "Dimas Anggara",
            gender = Gender.MALE,
            age = 27,
            distanceMeters = 680,
            bio = "Pecinta kucing dan kuliner pedas 🐱🍜",
            avatarColorHex = 0xFF26A69A,
            isOnline = true,
            city = "Mampang, Jaksel",
            avatarUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=300&q=80"
        ),
        User(
            id = "u5",
            name = "Alya Zahra",
            gender = Gender.FEMALE,
            age = 23,
            distanceMeters = 920,
            bio = "Halo semuanya! Senang bisa kenalan di Lovy Chat 😊",
            avatarColorHex = 0xFFFF7043,
            isOnline = true,
            city = "Setiabudi, Jaksel",
            avatarUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9?auto=format&fit=crop&w=300&q=80"
        ),
        User(
            id = "u6",
            name = "Budi Santoso",
            gender = Gender.MALE,
            age = 26,
            distanceMeters = 1450,
            bio = "Badminton & traveling enthusiast 🏸✈️",
            avatarColorHex = 0xFF5C6BC0,
            isOnline = false,
            city = "Kalibata, Jaksel",
            avatarUrl = "https://images.unsplash.com/photo-1522075469751-3a6694fb2f61?auto=format&fit=crop&w=300&q=80"
        ),
        User(
            id = "u7",
            name = "Clara Monica",
            gender = Gender.FEMALE,
            age = 24,
            distanceMeters = 1800,
            bio = "Design & art lover. Sapa aja jangan ragu 🎨",
            avatarColorHex = 0xFF26C6DA,
            isOnline = true,
            city = "Cilandak, Jaksel",
            avatarUrl = "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?auto=format&fit=crop&w=300&q=80"
        ),
        User(
            id = "u8",
            name = "Fajar Nugraha",
            gender = Gender.MALE,
            age = 24,
            distanceMeters = 2100,
            bio = "Software engineer yang hobi ngopi & fotografi ☕💻",
            avatarColorHex = 0xFF3F51B5,
            isOnline = true,
            city = "Kebayoran Baru, Jaksel",
            avatarUrl = "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?auto=format&fit=crop&w=300&q=80"
        ),
        User(
            id = "u9",
            name = "Tiara Andini",
            gender = Gender.FEMALE,
            age = 20,
            distanceMeters = 2450,
            bio = "Penyanyi kamar mandi & suka nonton film santai 🎬✨",
            avatarColorHex = 0xFFE91E63,
            isOnline = true,
            city = "Kemang, Jaksel",
            avatarUrl = "https://images.unsplash.com/photo-1529626455594-4ff0802cfb7e?auto=format&fit=crop&w=300&q=80"
        ),
        User(
            id = "u10",
            name = "Reza Rahardian",
            gender = Gender.MALE,
            age = 28,
            distanceMeters = 2800,
            bio = "Fitness & healthy lifestyle enthusiast 🏋️‍♂️🥗",
            avatarColorHex = 0xFF009688,
            isOnline = false,
            city = "Gandaria, Jaksel",
            avatarUrl = "https://images.unsplash.com/photo-1492562080023-ab3db95bfbce?auto=format&fit=crop&w=300&q=80"
        ),
        User(
            id = "u11",
            name = "Maya Safitri",
            gender = Gender.FEMALE,
            age = 22,
            distanceMeters = 3100,
            bio = "Pecinta kucing dan matcha latte 🍵🐈",
            avatarColorHex = 0xFF8BC34A,
            isOnline = true,
            city = "Pejaten, Jaksel"
        ),
        User(
            id = "u12",
            name = "Kevin Sanjaya",
            gender = Gender.MALE,
            age = 25,
            distanceMeters = 3400,
            bio = "Weekend gamer & suka kulineran malam 🎮🍔",
            avatarColorHex = 0xFFFF9800,
            isOnline = true,
            city = "Fatmawati, Jaksel"
        ),
        User(
            id = "u13",
            name = "Dinda Kirana",
            gender = Gender.FEMALE,
            age = 23,
            distanceMeters = 3750,
            bio = "Suka travelling & fotografi alam terbuka 📷🌿",
            avatarColorHex = 0xFF9C27B0,
            isOnline = false,
            city = "Pondok Indah, Jaksel"
        ),
        User(
            id = "u14",
            name = "Aris Wijaya",
            gender = Gender.MALE,
            age = 29,
            distanceMeters = 4100,
            bio = "Startup enthusiast, diskusi santai & sharing ide 📈🚀",
            avatarColorHex = 0xFF607D8B,
            isOnline = true,
            city = "Senayan, Jakpus"
        ),
        User(
            id = "u15",
            name = "Bella Cantika",
            gender = Gender.FEMALE,
            age = 21,
            distanceMeters = 4500,
            bio = "Content creator & pencinta fashion lokal 👗💄",
            avatarColorHex = 0xFFFF4081,
            isOnline = true,
            city = "Sudirman, Jaksel"
        ),
        User(
            id = "u16",
            name = "Galih Prakoso",
            gender = Gender.MALE,
            age = 26,
            distanceMeters = 4900,
            bio = "Gitaris akustik & suka dengerin musik indie 🎸🎶",
            avatarColorHex = 0xFF795548,
            isOnline = false,
            city = "Blok M, Jaksel"
        ),
        User(
            id = "u17",
            name = "Zahra Amalia",
            gender = Gender.FEMALE,
            age = 24,
            distanceMeters = 5300,
            bio = "Pecinta buku & jalan sore di taman kota 📚🌤️",
            avatarColorHex = 0xFF00BCD4,
            isOnline = true,
            city = "Ragunan, Jaksel"
        ),
        User(
            id = "u18",
            name = "Hendra Setiawan",
            gender = Gender.MALE,
            age = 27,
            distanceMeters = 5800,
            bio = "Jogging pagi & suka ngobrol santai seputar hobi 🏃‍♂️☕",
            avatarColorHex = 0xFF4CAF50,
            isOnline = true,
            city = "Pasar Minggu, Jaksel"
        ),
        User(
            id = "u19",
            name = "Nabila Laila",
            gender = Gender.FEMALE,
            age = 22,
            distanceMeters = 6200,
            bio = "Suka masak & mencoba resep kuliner baru 🍳🍰",
            avatarColorHex = 0xFFFF5722,
            isOnline = true,
            city = "Tanjung Barat, Jaksel"
        ),
        User(
            id = "u20",
            name = "Bayu Aji",
            gender = Gender.MALE,
            age = 25,
            distanceMeters = 1200,
            bio = "Suka bulutangkis & kuliner malam 🏸🍢",
            avatarColorHex = 0xFF3F51B5,
            isOnline = true,
            city = "Cikini, Jakpus"
        ),
        User(
            id = "u21",
            name = "Putri Maharani",
            gender = Gender.FEMALE,
            age = 23,
            distanceMeters = 1350,
            bio = "Pecinta seni rupa & pameran galeri 🎨🖼️",
            avatarColorHex = 0xFFE91E63,
            isOnline = true,
            city = "Menteng, Jakpus",
            avatarUrl = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?auto=format&fit=crop&w=300&q=80"
        ),
        User(
            id = "u22",
            name = "Ilham Ramadhan",
            gender = Gender.MALE,
            age = 27,
            distanceMeters = 1500,
            bio = "Pecinta kopi seduh manual & buku sastra ☕📖",
            avatarColorHex = 0xFF00796B,
            isOnline = false,
            city = "Karet, Jaksel"
        ),
        User(
            id = "u23",
            name = "Amanda Syakieb",
            gender = Gender.FEMALE,
            age = 22,
            distanceMeters = 1650,
            bio = "Mahasiswi arsitektur, suka jalan sore 🏛️🌤️",
            avatarColorHex = 0xFF8E24AA,
            isOnline = true,
            city = "Benhil, Jakpus"
        ),
        User(
            id = "u24",
            name = "Dennis Pratama",
            gender = Gender.MALE,
            age = 26,
            distanceMeters = 1820,
            bio = "Gamer PC, suka nongkrong santai 🎮🎧",
            avatarColorHex = 0xFF1E88E5,
            isOnline = true,
            city = "Slipi, Jakbar"
        ),
        User(
            id = "u25",
            name = "Rina Anggraini",
            gender = Gender.FEMALE,
            age = 24,
            distanceMeters = 1980,
            bio = "Suka baking cake & dengerin podcast komedi 🧁🎙️",
            avatarColorHex = 0xFFD81B60,
            isOnline = true,
            city = "Palmerah, Jakbar"
        ),
        User(
            id = "u26",
            name = "Farhan Maulana",
            gender = Gender.MALE,
            age = 28,
            distanceMeters = 2150,
            bio = "Desainer interior & kolektor vinyl lawas 🛋️📻",
            avatarColorHex = 0xFF5D4037,
            isOnline = false,
            city = "Tomang, Jakbar"
        ),
        User(
            id = "u27",
            name = "Jessica Iskandar",
            gender = Gender.FEMALE,
            age = 21,
            distanceMeters = 2300,
            bio = "Dance cover K-Pop & suka foto estetik 💃📸",
            avatarColorHex = 0xFFFB8C00,
            isOnline = true,
            city = "Grogol, Jakbar"
        ),
        User(
            id = "u28",
            name = "Arif Hidayat",
            gender = Gender.MALE,
            age = 25,
            distanceMeters = 2450,
            bio = "Pecinta gunung & camping akhir pekan 🏕️⛰️",
            avatarColorHex = 0xFF43A047,
            isOnline = true,
            city = "Kemanggisan, Jakbar"
        ),
        User(
            id = "u29",
            name = "Sherly Wijaya",
            gender = Gender.FEMALE,
            age = 23,
            distanceMeters = 2600,
            bio = "Pencinta matcha latte & anime santai 🍵🎏",
            avatarColorHex = 0xFF00ACC1,
            isOnline = true,
            city = "Tanjung Duren, Jakbar"
        ),
        User(
            id = "u30",
            name = "Rizky Febian",
            gender = Gender.MALE,
            age = 26,
            distanceMeters = 2780,
            bio = "Musisi akustik & pencipta lagu indie 🎸🎙️",
            avatarColorHex = 0xFF3949AB,
            isOnline = true,
            city = "Kebon Jeruk, Jakbar"
        ),
        User(
            id = "u31",
            name = "Gita Gutawa",
            gender = Gender.FEMALE,
            age = 22,
            distanceMeters = 2950,
            bio = "Penyanyi choir & penyuka kucing oren 🎶🐈",
            avatarColorHex = 0xFFF06292,
            isOnline = false,
            city = "Kedoya, Jakbar"
        ),
        User(
            id = "u32",
            name = "Andre Taulany",
            gender = Gender.MALE,
            age = 29,
            distanceMeters = 3100,
            bio = "Kolektor motor klasik & obrolan santai 🛵😄",
            avatarColorHex = 0xFF6D4C41,
            isOnline = true,
            city = "Meruya, Jakbar"
        ),
        User(
            id = "u33",
            name = "Tasya Farasya",
            gender = Gender.FEMALE,
            age = 25,
            distanceMeters = 3300,
            bio = "Beauty & skincare reviewer, sapa aja ya 💄✨",
            avatarColorHex = 0xFFBA68C8,
            isOnline = true,
            city = "Puri Indah, Jakbar"
        ),
        User(
            id = "u34",
            name = "Rio Dewanto",
            gender = Gender.MALE,
            age = 28,
            distanceMeters = 3500,
            bio = "Barista & pengamat film dokumenter ☕🎞️",
            avatarColorHex = 0xFF546E7A,
            isOnline = true,
            city = "Joglo, Jakbar"
        ),
        User(
            id = "u35",
            name = "Isyana Sarasvati",
            gender = Gender.FEMALE,
            age = 24,
            distanceMeters = 3700,
            bio = "Pianis klasik & suka kulineran unik 🎹🍰",
            avatarColorHex = 0xFF26A69A,
            isOnline = true,
            city = "Bintaro, Tangsel"
        ),
        User(
            id = "u36",
            name = "Tulus Rusydi",
            gender = Gender.MALE,
            age = 27,
            distanceMeters = 3900,
            bio = "Penikmat lagu syahdu & jalan santai 🍃🎧",
            avatarColorHex = 0xFF7CB342,
            isOnline = false,
            city = "Pondok Ranji, Tangsel"
        ),
        User(
            id = "u37",
            name = "Raisa Andriana",
            gender = Gender.FEMALE,
            age = 23,
            distanceMeters = 4100,
            bio = "Suka nyanyi & bikin playlist chill 🎤📻",
            avatarColorHex = 0xFFFFA726,
            isOnline = true,
            city = "Ciputat, Tangsel"
        ),
        User(
            id = "u38",
            name = "Afgan Syahreza",
            gender = Gender.MALE,
            age = 26,
            distanceMeters = 4300,
            bio = "Pecinta kacamata & lagu pop jazz 👓🎷",
            avatarColorHex = 0xFF42A5F5,
            isOnline = true,
            city = "Pamulang, Tangsel"
        ),
        User(
            id = "u39",
            name = "Maudy Ayunda",
            gender = Gender.FEMALE,
            age = 24,
            distanceMeters = 4500,
            bio = "Bookworm, suka diskusi ide baru 📚💡",
            avatarColorHex = 0xFFAB47BC,
            isOnline = true,
            city = "BSD City, Tangsel"
        ),
        User(
            id = "u40",
            name = "Vidi Aldiano",
            gender = Gender.MALE,
            age = 25,
            distanceMeters = 4700,
            bio = "Social butterfly, podcasting & hangout 🎙️🌟",
            avatarColorHex = 0xFF26C6DA,
            isOnline = true,
            city = "Serpong, Tangsel"
        ),
        User(
            id = "u41",
            name = "Chelsea Islan",
            gender = Gender.FEMALE,
            age = 22,
            distanceMeters = 4900,
            bio = "Aktivis lingkungan & pencinta teater 🌿🎭",
            avatarColorHex = 0xFFEC407A,
            isOnline = true,
            city = "Alam Sutera, Tangerang"
        ),
        User(
            id = "u42",
            name = "Nicholas Saputra",
            gender = Gender.MALE,
            age = 28,
            distanceMeters = 5100,
            bio = "Travelling fotografer & cinta alam 📷🐘",
            avatarColorHex = 0xFF37474F,
            isOnline = false,
            city = "Gading Serpong, Tangerang"
        ),
        User(
            id = "u43",
            name = "Dian Sastro",
            gender = Gender.FEMALE,
            age = 26,
            distanceMeters = 5300,
            bio = "Produser film indie & penari tradisional 🎬💃",
            avatarColorHex = 0xFF8D6E63,
            isOnline = true,
            city = "Karawaci, Tangerang"
        ),
        User(
            id = "u44",
            name = "Chicco Jerikho",
            gender = Gender.MALE,
            age = 29,
            distanceMeters = 5500,
            bio = "Pecinta kopi single origin & lari maraton ☕🏃",
            avatarColorHex = 0xFF5C6BC0,
            isOnline = true,
            city = "Cikokol, Tangerang"
        ),
        User(
            id = "u45",
            name = "Pevita Pearce",
            gender = Gender.FEMALE,
            age = 23,
            distanceMeters = 5800,
            bio = "Gamer kasual & yoga enthusiast 🎮🧘‍♀️",
            avatarColorHex = 0xFFFF7043,
            isOnline = true,
            city = "Batuceper, Tangerang"
        )
    )

    val initialConversations = listOf(
        ChatConversation(
            id = "conv_u1",
            partnerId = "u1",
            partnerName = "Siti Rahma",
            partnerAvatarHex = 0xFFEC407A,
            partnerGender = Gender.FEMALE,
            lastMessage = "Hai juga! Kamu tinggal daerah mana nih?",
            lastTimestamp = System.currentTimeMillis() - 1000 * 60 * 5,
            unreadCount = 1,
            isOnline = true,
            partnerAvatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=300&q=80"
        ),
        ChatConversation(
            id = "conv_u2",
            partnerId = "u2",
            partnerName = "Rian Pratama",
            partnerAvatarHex = 0xFF42A5F5,
            partnerGender = Gender.MALE,
            lastMessage = "Besok ada rencana hunting foto gak?",
            lastTimestamp = System.currentTimeMillis() - 1000 * 60 * 45,
            unreadCount = 0,
            isOnline = true,
            partnerAvatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=300&q=80"
        ),
        ChatConversation(
            id = "conv_u5",
            partnerId = "u5",
            partnerName = "Alya Zahra",
            partnerAvatarHex = 0xFFFF7043,
            partnerGender = Gender.FEMALE,
            lastMessage = "Makasih udah nemu botolku yaa! Seneng banget bisa kenalan.",
            lastTimestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 3,
            unreadCount = 0,
            isOnline = true,
            partnerAvatarUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9?auto=format&fit=crop&w=300&q=80"
        )
    )

    val initialMessages = mapOf(
        "conv_u1" to listOf(
            ChatMessage("m1", "conv_u1", "Halo Siti, salam kenal dari Lovy Chat!", System.currentTimeMillis() - 1000 * 60 * 15, isFromMe = true),
            ChatMessage("m2", "conv_u1", "Halo juga! Salam kenal ya 😊", System.currentTimeMillis() - 1000 * 60 * 10, isFromMe = false),
            ChatMessage("m3", "conv_u1", "Tadi lihat kamu di fitur Pengguna Sekitar, jaraknya deket banget hehe", System.currentTimeMillis() - 1000 * 60 * 8, isFromMe = true),
            ChatMessage("m4", "conv_u1", "Hai juga! Kamu tinggal daerah mana nih?", System.currentTimeMillis() - 1000 * 60 * 5, isFromMe = false)
        ),
        "conv_u2" to listOf(
            ChatMessage("m21", "conv_u2", "Bro, kameramu pakai mirrorless apa?", System.currentTimeMillis() - 1000 * 60 * 90, isFromMe = true),
            ChatMessage("m22", "conv_u2", "Gua pakai Sony A6400 nih, asik buat street photography", System.currentTimeMillis() - 1000 * 60 * 70, isFromMe = false),
            ChatMessage("m23", "conv_u2", "Besok ada rencana hunting foto gak?", System.currentTimeMillis() - 1000 * 60 * 45, isFromMe = false)
        ),
        "conv_u5" to listOf(
            ChatMessage("m51", "conv_u5", "Halo! Aku barusan mancing botolmu di laut Lovy Chat 🌊", System.currentTimeMillis() - 1000 * 60 * 60 * 4, isFromMe = true),
            ChatMessage("m52", "conv_u5", "Makasih udah nemu botolku yaa! Seneng banget bisa kenalan.", System.currentTimeMillis() - 1000 * 60 * 60 * 3, isFromMe = false)
        )
    )

    val oceanBottles = listOf(
        BottleMessage(
            id = "b1",
            senderId = "u5",
            senderName = "Alya Zahra",
            senderGender = Gender.FEMALE,
            avatarHex = 0xFFFF7043,
            content = "Semoga siapa pun yang menemukan botol ini harinya menyenangkan & selalu bahagia! Semangat pejuang skripsi 🎓✨",
            thrownTimestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 2,
            locationHint = "Teluk Jakarta",
            replyCount = 3
        ),
        BottleMessage(
            id = "b2",
            senderId = "u2",
            senderName = "Rian Pratama",
            senderGender = Gender.MALE,
            avatarHex = 0xFF42A5F5,
            content = "Lagi pengen ngopi tapi temen-temen pada sibuk. Ada yang di sekitar Tebet gak? Ngopi santai yuk!",
            thrownTimestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 5,
            locationHint = "Laut Jawa",
            replyCount = 5
        ),
        BottleMessage(
            id = "b3",
            senderId = "u7",
            senderName = "Clara Monica",
            senderGender = Gender.FEMALE,
            avatarHex = 0xFF26C6DA,
            content = "Kadang hidup terasa berat, tapi inget laut itu luas, masalah kita cuma setitik ombak. Keep moving forward! 💙🌊",
            thrownTimestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 12,
            locationHint = "Selat Sunda",
            replyCount = 8
        ),
        BottleMessage(
            id = "b4",
            senderId = "u4",
            senderName = "Dimas Anggara",
            senderGender = Gender.MALE,
            avatarHex = 0xFF26A69A,
            content = "Rekomendasi seblak paling enak di Jaksel dong teman-teman botol? 🌶️🔥",
            thrownTimestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 18,
            locationHint = "Kepulauan Seribu",
            replyCount = 2
        )
    )

    val initialMoments = listOf(
        MomentItem(
            id = "mom1",
            authorName = "Siti Rahma",
            authorAvatarHex = 0xFFEC407A,
            authorAvatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=300&q=80",
            timeAgo = "10 menit yang lalu",
            content = "Menikmati senja santai sambil ngopi dan dengar playlist favorit di kafe langganan. Hari yang cukup melelahkan tapi bersyukur banget bisa rileks sejenak ☕🌅 Ada yang suka nongkrong di daerah Tebet juga?",
            likesCount = 24,
            isLiked = false,
            commentsCount = 5,
            imageUrl = "https://images.unsplash.com/photo-1501339847302-ac426a4a7cbb?auto=format&fit=crop&w=1000&q=80",
            locationTag = "Tebet, Jakarta Selatan"
        ),
        MomentItem(
            id = "mom1b",
            authorName = "Siti Rahma",
            authorAvatarHex = 0xFFEC407A,
            authorAvatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=300&q=80",
            timeAgo = "Kemarin",
            content = "Piknik akhir pekan di taman kota bersama sahabat 🌿🌸 Udara segar dan pemandangan hijau bikin pikiran tenang kembali.",
            likesCount = 56,
            isLiked = true,
            commentsCount = 8,
            imageUrl = "https://images.unsplash.com/photo-1517457373958-b7bdd4587205?auto=format&fit=crop&w=1000&q=80",
            locationTag = "Hutan Kota GBK, Jakarta"
        ),
        MomentItem(
            id = "mom2",
            authorName = "Rian Pratama",
            authorAvatarHex = 0xFF42A5F5,
            authorAvatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=300&q=80",
            timeAgo = "2 jam yang lalu",
            content = "Hasil jepretan hunting foto sore ini di Bundaran HI. Jakarta selalu punya seribu cerita di balik gemerlap lampu malamnya 📸🌃 Siapa yang hobi street photography juga?",
            likesCount = 48,
            isLiked = true,
            commentsCount = 12,
            imageUrl = "https://images.unsplash.com/photo-1519501025264-65ba15a82390?auto=format&fit=crop&w=1000&q=80",
            locationTag = "Bundaran HI, Jakarta Pusat"
        ),
        MomentItem(
            id = "mom2b",
            authorName = "Rian Pratama",
            authorAvatarHex = 0xFF42A5F5,
            authorAvatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=300&q=80",
            timeAgo = "1 hari yang lalu",
            content = "Secangkir kopi manual brew mengawali pagi yang produktif ☕📷 Kopi enak selalu berhasil memantik ide-ide baru.",
            likesCount = 37,
            isLiked = false,
            commentsCount = 4,
            imageUrl = "https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?auto=format&fit=crop&w=1000&q=80",
            locationTag = "Kopi Toko Djawa, Melawai"
        ),
        MomentItem(
            id = "mom3",
            authorName = "Clara Monica",
            authorAvatarHex = 0xFF26C6DA,
            authorAvatarUrl = "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?auto=format&fit=crop&w=300&q=80",
            timeAgo = "4 jam yang lalu",
            content = "Ruang kerja dan kanvas baru selesai ditata! Menemukan inspirasi warna-warni ceria untuk project berikutnya 🎨✨ Jangan lupa luangkan waktu untuk hal yang kamu cintai hari ini ya kawan Lovy Chat!",
            likesCount = 63,
            isLiked = false,
            commentsCount = 18,
            imageUrl = "https://images.unsplash.com/photo-1513364776144-60967b0f800f?auto=format&fit=crop&w=1000&q=80",
            locationTag = "Cilandak, Jakarta Selatan"
        ),
        MomentItem(
            id = "mom4",
            authorName = "Dimas Anggara",
            authorAvatarHex = 0xFF26A69A,
            authorAvatarUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=300&q=80",
            timeAgo = "6 jam yang lalu",
            content = "Mochi lagi asyik tidur pulas di sofa setelah seharian lari-larian keliling rumah 🐱💤 Kucing kalian kalau siang kerjanya tidur juga gak nih?",
            likesCount = 89,
            isLiked = true,
            commentsCount = 27,
            imageUrl = "https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?auto=format&fit=crop&w=1000&q=80",
            locationTag = "Mampang Prapatan, Jakarta"
        ),
        MomentItem(
            id = "mom5",
            authorName = "Alya Zahra",
            authorAvatarHex = 0xFFFF7043,
            authorAvatarUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9?auto=format&fit=crop&w=300&q=80",
            timeAgo = "8 jam yang lalu",
            content = "Healing sejenak menghirup udara laut yang segar dan mendengarkan deburan ombak 🌊☀️ Kadang kita butuh berhenti sejenak untuk melangkah lebih jauh.",
            likesCount = 112,
            isLiked = false,
            commentsCount = 34,
            imageUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=1000&q=80",
            locationTag = "Pantai Ancol, Jakarta Utara"
        ),
        MomentItem(
            id = "mom5b",
            authorName = "Alya Zahra",
            authorAvatarHex = 0xFFFF7043,
            authorAvatarUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9?auto=format&fit=crop&w=300&q=80",
            timeAgo = "2 hari yang lalu",
            content = "Graduation day! Alhamdulillah selesai juga perjuangan skripsi ini 🎓🎉 Terima kasih untuk semua doa dan support teman-teman!",
            likesCount = 145,
            isLiked = true,
            commentsCount = 42,
            imageUrl = "https://images.unsplash.com/photo-1523050854058-8df90110c9f1?auto=format&fit=crop&w=1000&q=80",
            locationTag = "Universitas Indonesia, Depok"
        )
    )
}
