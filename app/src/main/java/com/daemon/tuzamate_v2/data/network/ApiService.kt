package com.daemon.tuzamate_v2.data.network

import com.daemon.tuzamate_v2.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    
    @GET("profiles")
    suspend fun getProfile(): Response<ProfileResponse>
    
    @DELETE("profiles")
    suspend fun deleteProfile(
        @Body request: DeleteProfileRequest
    ): Response<DeleteProfileResponse>
    
    @POST("profiles/init-profile")
    suspend fun initProfile(
        @Body request: InitProfileRequest
    ): Response<InitProfileResponse>
    
    @GET("profiles/likes")
    suspend fun getProfileLikes(
        @Query("cursor") cursor: Int = 0,
        @Query("offset") offset: Int = 10
    ): Response<LikesResponse>
    
    @GET("profiles/posts")
    suspend fun getProfilePosts(
        @Query("cursor") cursor: Int = 0,
        @Query("offset") offset: Int = 10
    ): Response<PostsResponse>
    
    @GET("profiles/scraps")
    suspend fun getProfileScraps(
        @Query("cursor") cursor: Int = 0,
        @Query("offset") offset: Int = 10
    ): Response<ScrapsResponse>
    
    @POST("boards/{boardType}/posts")
    suspend fun createPost(
        @Path("boardType") boardType: String,
        @Body request: CreatePostRequest
    ): Response<CreatePostResponse>
    
    @GET("notification")
    suspend fun getNotifications(
        @Query("cursor") cursor: Int? = null,
        @Query("size") size: Int? = null
    ): Response<NotificationListResponse>
    
    @GET("notification/{notificationId}")
    suspend fun getNotification(
        @Path("notificationId") notificationId: Int
    ): Response<NotificationDetailResponse>
    
    @DELETE("notification/{notificationId}")
    suspend fun deleteNotification(
        @Path("notificationId") notificationId: Int
    ): Response<NotificationDeleteResponse>
    
    @PATCH("notification/{notificationId}")
    suspend fun markNotificationAsRead(
        @Path("notificationId") notificationId: Int
    ): Response<NotificationReadResponse>
    
}