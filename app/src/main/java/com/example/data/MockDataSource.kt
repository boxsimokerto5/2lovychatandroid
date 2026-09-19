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
            city = "Tebet, Jaksel"
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
            city = "Pancoran, Jaksel"
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
            city = "Kuningan, Jaksel"
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
            city = "Mampang, Jaksel"
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
            city = "Setiabudi, Jaksel"
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
            city = "Kalibata, Jaksel"
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
            city = "Cilandak, Jaksel"
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
            city = "Kebayoran Baru, Jaksel"
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
            city = "Kemang, Jaksel"
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
            city = "Gandaria, Jaksel"
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
            isOnline = true
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
            isOnline = true
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
            isOnline = true
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
            timeAgo = "10 menit yang lalu",
            content = "Menikmati senja sambil dengar musik favorit. Hari yang cukup melelahkan tapi bersyukur banget ☕🌅",
            likesCount = 12,
            isLiked = false,
            commentsCount = 3
        ),
        MomentItem(
            id = "mom2",
            authorName = "Rian Pratama",
            authorAvatarHex = 0xFF42A5F5,
            timeAgo = "2 jam yang lalu",
            content = "Hasil jepretan sore ini di Bundaran HI. Jakarta selalu punya cerita di balik gemerlap lampunya 📸🌃",
            likesCount = 28,
            isLiked = true,
            commentsCount = 7
        ),
        MomentItem(
            id = "mom3",
            authorName = "Clara Monica",
            authorAvatarHex = 0xFF26C6DA,
            timeAgo = "5 jam yang lalu",
            content = "Baru selesai bikin ilustrasi baru! Jangan lupa tersenyum hari ini kawan Lovy Chat 😊🎨",
            likesCount = 45,
            isLiked = false,
            commentsCount = 11
        )
    )
}
