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
    
    @GET("boards/{boardType}/posts")
    suspend fun getPosts(
        @Path("boardType") boardType: String,
        @Query("cursor") cursor: Int? = null,
        @Query("size") size: Int? = 10
    ): Response<PostListResponse>
    
    @POST("boards/{boardType}/posts")
    suspend fun createPost(
        @Path("boardType") boardType: String,
        @Body request: CreatePostRequest
    ): Response<CreatePostResponse>
    
    @GET("boards/{boardType}/posts/{postId}")
    suspend fun getPostDetail(
        @Path("boardType") boardType: String,
        @Path("postId") postId: Int
    ): Response<PostDetailResponse>
    
    @DELETE("boards/{boardType}/posts/{postId}")
    suspend fun deletePost(
        @Path("boardType") boardType: String,
        @Path("postId") postId: Int
    ): Response<DeletePostResponse>
    
    @PATCH("boards/{boardType}/posts/{postId}")
    suspend fun updatePost(
        @Path("boardType") boardType: String,
        @Path("postId") postId: Int,
        @Body request: UpdatePostRequest
    ): Response<UpdatePostResponse>
    
    @POST("posts/{postId}/likes")
    suspend fun likePost(
        @Path("postId") postId: Int
    ): Response<LikeResponse>
    
    @DELETE("posts/{postId}/likes")
    suspend fun unlikePost(
        @Path("postId") postId: Int
    ): Response<LikeResponse>
    
    @POST("posts/{postId}/scraps")
    suspend fun scrapPost(
        @Path("postId") postId: Int
    ): Response<ScrapResponse>
    
    @DELETE("posts/{postId}/scraps")
    suspend fun unscrapPost(
        @Path("postId") postId: Int
    ): Response<ScrapResponse>
    
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
    
    @GET("posts/{postId}/comments")
    suspend fun getComments(
        @Path("postId") postId: Int,
        @Query("cursor") cursor: Int? = null,
        @Query("size") size: Int? = 10
    ): Response<CommentListResponse>
    
    @POST("posts/{postId}/comments")
    suspend fun createComment(
        @Path("postId") postId: Int,
        @Body request: CommentCreateRequest
    ): Response<CommentCreateResponse>
    
    @GET("posts/{postId}/comments/{commentId}")
    suspend fun getCommentDetail(
        @Path("postId") postId: Int,
        @Path("commentId") commentId: Int
    ): Response<CommentDetailResponse>
    
    @DELETE("posts/{postId}/comments/{commentId}")
    suspend fun deleteComment(
        @Path("postId") postId: Int,
        @Path("commentId") commentId: Int
    ): Response<CommentDeleteResponse>
    
    @PATCH("posts/{postId}/comments/{commentId}")
    suspend fun updateComment(
        @Path("postId") postId: Int,
        @Path("commentId") commentId: Int,
        @Body request: CommentUpdateRequest
    ): Response<CommentUpdateResponse>
    
}