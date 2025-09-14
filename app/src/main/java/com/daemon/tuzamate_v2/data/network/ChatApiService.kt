package com.daemon.tuzamate_v2.data.network

import com.daemon.tuzamate_v2.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ChatApiService {
    
    @POST("chats/chat/")
    suspend fun sendChatMessage(
        @Body request: ChatRequest
    ): Response<BaseResponse<ChatResult>>
    
    @GET("chats/task/{taskId}/")
    suspend fun getTaskStatus(
        @Path("taskId") taskId: String
    ): Response<BaseResponse<TaskResult>>
    
    @DELETE("chats/session/{sessionId}/end/")
    suspend fun endChatSession(
        @Path("sessionId") sessionId: String
    ): Response<BaseResponse<Any>>
    
    @POST("chats/profile/conflict")
    suspend fun handleProfileConflict(
        @Body request: ProfileConflictRequest
    ): Response<BaseResponse<ProfileConflictResult>>
}