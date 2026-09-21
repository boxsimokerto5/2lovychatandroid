package com.example.data.supabase

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface SupabaseRestApi {

    @POST("rest/v1/nearby_users")
    @Headers("Prefer: return=representation,resolution=merge-duplicates")
    suspend fun upsertNearbyUser(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authHeader: String,
        @Body user: SupabaseUserDto
    ): Response<List<SupabaseUserDto>>

    @GET("rest/v1/nearby_users?select=*")
    suspend fun getNearbyUsers(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authHeader: String,
        @Query("limit") limit: Int = 30
    ): Response<List<SupabaseUserDto>>

    @PATCH("rest/v1/nearby_users")
    suspend fun updateUserActive(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authHeader: String,
        @Query("id") idFilter: String,
        @Body updates: Map<String, Long>
    ): Response<Unit>

    @GET("rest/v1/ocean_bottles?select=*&order=created_at.desc")
    suspend fun getOceanBottles(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authHeader: String,
        @Query("limit") limit: Int = 30
    ): Response<List<SupabaseBottleDto>>

    @POST("rest/v1/ocean_bottles")
    @Headers("Prefer: return=representation")
    suspend fun insertOceanBottle(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authHeader: String,
        @Body bottle: SupabaseBottleDto
    ): Response<List<SupabaseBottleDto>>

    @GET("rest/v1/chat_messages?select=*&order=created_at.asc")
    suspend fun getChatMessages(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authHeader: String,
        @Query("conversation_id") conversationFilter: String
    ): Response<List<SupabaseMessageDto>>

    @GET("rest/v1/chat_messages?select=*&order=created_at.desc")
    suspend fun getRecentMessages(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authHeader: String,
        @Query("conversation_id") conversationPattern: String,
        @Query("limit") limit: Int = 60
    ): Response<List<SupabaseMessageDto>>

    @POST("rest/v1/chat_messages")
    @Headers("Prefer: return=representation")
    suspend fun insertChatMessage(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authHeader: String,
        @Body message: SupabaseMessageDto
    ): Response<List<SupabaseMessageDto>>

    @PATCH("rest/v1/chat_messages")
    suspend fun markChatMessageDeleted(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authHeader: String,
        @Query("id") idFilter: String,
        @Body updates: Map<String, Boolean>
    ): Response<Unit>

    @PATCH("rest/v1/chat_messages")
    suspend fun markAllSenderMessagesDeleted(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authHeader: String,
        @Query("sender_id") senderFilter: String,
        @Body updates: Map<String, Boolean>
    ): Response<Unit>

    @GET("rest/v1/moments?select=*&order=created_at.desc")
    suspend fun getMoments(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authHeader: String,
        @Query("limit") limit: Int = 30
    ): Response<List<SupabaseMomentDto>>

    @POST("rest/v1/moments")
    @Headers("Prefer: return=representation")
    suspend fun insertMoment(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authHeader: String,
        @Body moment: SupabaseMomentDto
    ): Response<List<SupabaseMomentDto>>

    @DELETE("rest/v1/moments")
    suspend fun deleteMoment(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authHeader: String,
        @Query("id") idFilter: String
    ): Response<Unit>

    @GET("rest/v1/app_accounts?select=*")
    suspend fun getAccountByUsername(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authHeader: String,
        @Query("username") usernameFilter: String,
        @Query("limit") limit: Int = 1
    ): Response<List<SupabaseAccountDto>>

    @GET("rest/v1/app_accounts?select=*")
    suspend fun getAccountByGoogle(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authHeader: String,
        @Query("google_email") googleEmailFilter: String,
        @Query("limit") limit: Int = 1
    ): Response<List<SupabaseAccountDto>>

    @POST("rest/v1/app_accounts")
    @Headers("Prefer: return=representation,resolution=merge-duplicates")
    suspend fun upsertAccount(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authHeader: String,
        @Body account: SupabaseAccountDto
    ): Response<List<SupabaseAccountDto>>

    @PATCH("rest/v1/app_accounts")
    suspend fun updateAccountLoginTime(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authHeader: String,
        @Query("id") idFilter: String,
        @Body updates: Map<String, Long>
    ): Response<Unit>
}
