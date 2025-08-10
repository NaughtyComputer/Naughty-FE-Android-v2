package com.daemon.tuzamate_v2.data.network

import com.daemon.tuzamate_v2.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    
    @GET("profiles")
    suspend fun getProfile(): Response<ProfileResponse>
    
    @POST("boards/{boardType}/posts")
    suspend fun createPost(
        @Path("boardType") boardType: String,
        @Body request: CreatePostRequest
    ): Response<CreatePostResponse>
    
}