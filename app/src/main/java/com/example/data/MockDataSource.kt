package com.example.data

import com.example.model.BottleMessage
import com.example.model.ChatConversation
import com.example.model.ChatMessage
import com.example.model.MomentItem
import com.example.model.User

object MockDataSource {
    val initialNearbyUsers = emptyList<User>()
    val initialConversations = emptyList<ChatConversation>()
    val initialMessages = emptyMap<String, List<ChatMessage>>()
    val oceanBottles = emptyList<BottleMessage>()
    val initialMoments = emptyList<MomentItem>()
    val initialMomentComments = emptyMap<String, List<com.example.model.MomentComment>>()
}
