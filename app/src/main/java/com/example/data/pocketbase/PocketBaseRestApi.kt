package com.example.data.pocketbase

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PocketBaseRestApi {

    @GET("api/health")
    suspend fun healthCheck(): Response<Map<String, Any?>>

    // ================= Auth Providers & OAuth2 =================
    @GET("api/collections/users/auth-methods")
    suspend fun getAuthMethods(): Response<Map<String, Any?>>

    @POST("api/collections/users/auth-with-oauth2")
    suspend fun authWithOAuth2(
        @Body body: Map<String, @JvmSuppressWildcards Any?>
    ): Response<Map<String, Any?>>

    @POST("api/collections/users/auth-with-password")
    suspend fun authWithPassword(
        @Body body: Map<String, @JvmSuppressWildcards Any?>
    ): Response<Map<String, Any?>>

    // ================= Users =================
    @GET("api/collections/users/records")
    suspend fun getUsers(
        @Query("perPage") perPage: Int = 50,
        @Query("sort") sort: String = "-last_active_at",
        @Query("filter") filter: String? = null
    ): Response<PocketBasePage<PocketBaseUserRecord>>

    @GET("api/collections/users/records/{id}")
    suspend fun getUserById(
        @Path("id") id: String
    ): Response<PocketBaseUserRecord>

    @POST("api/collections/users/records")
    suspend fun createUser(
        @Body user: Map<String, @JvmSuppressWildcards Any?>
    ): Response<PocketBaseUserRecord>

    @PATCH("api/collections/users/records/{id}")
    suspend fun updateUser(
        @Path("id") id: String,
        @Body updates: Map<String, @JvmSuppressWildcards Any?>
    ): Response<PocketBaseUserRecord>

    @DELETE("api/collections/users/records/{id}")
    suspend fun deleteUser(
        @Path("id") id: String
    ): Response<Unit>

    // ================= Messages =================
    @GET("api/collections/messages/records")
    suspend fun getMessages(
        @Query("filter") filter: String? = null,
        @Query("sort") sort: String = "created_at_ms",
        @Query("perPage") perPage: Int = 100
    ): Response<PocketBasePage<PocketBaseMessageRecord>>

    @POST("api/collections/messages/records")
    suspend fun createMessage(
        @Body message: Map<String, @JvmSuppressWildcards Any?>
    ): Response<PocketBaseMessageRecord>

    @PATCH("api/collections/messages/records/{id}")
    suspend fun updateMessage(
        @Path("id") id: String,
        @Body updates: Map<String, @JvmSuppressWildcards Any?>
    ): Response<PocketBaseMessageRecord>

    @DELETE("api/collections/messages/records/{id}")
    suspend fun deleteMessage(
        @Path("id") id: String
    ): Response<Unit>

    // ================= Ocean Bottles =================
    @GET("api/collections/bottles/records")
    suspend fun getBottles(
        @Query("sort") sort: String = "-created_at_ms",
        @Query("perPage") perPage: Int = 50
    ): Response<PocketBasePage<PocketBaseBottleRecord>>

    @POST("api/collections/bottles/records")
    suspend fun createBottle(
        @Body bottle: Map<String, @JvmSuppressWildcards Any?>
    ): Response<PocketBaseBottleRecord>

    @DELETE("api/collections/bottles/records/{id}")
    suspend fun deleteBottle(
        @Path("id") id: String
    ): Response<Unit>

    // ================= Moments =================
    @GET("api/collections/moments/records")
    suspend fun getMoments(
        @Query("sort") sort: String = "-created_at_ms",
        @Query("perPage") perPage: Int = 50
    ): Response<PocketBasePage<PocketBaseMomentRecord>>

    @POST("api/collections/moments/records")
    suspend fun createMoment(
        @Body moment: Map<String, @JvmSuppressWildcards Any?>
    ): Response<PocketBaseMomentRecord>

    @PATCH("api/collections/moments/records/{id}")
    suspend fun updateMoment(
        @Path("id") id: String,
        @Body updates: Map<String, @JvmSuppressWildcards Any?>
    ): Response<PocketBaseMomentRecord>

    @DELETE("api/collections/moments/records/{id}")
    suspend fun deleteMoment(
        @Path("id") id: String
    ): Response<Unit>
}
