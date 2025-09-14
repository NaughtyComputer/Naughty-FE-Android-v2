package com.daemon.tuzamate_v2.data.repository

import com.daemon.tuzamate_v2.data.model.*
import com.daemon.tuzamate_v2.data.network.ChatApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(
    private val chatApiService: ChatApiService
) {
    suspend fun sendChatMessage(
        username: String,
        sessionId: String?,
        message: String
    ): Response<BaseResponse<ChatResult>> {
        return withContext(Dispatchers.IO) {
            chatApiService.sendChatMessage(
                ChatRequest(
                    username = username,
                    sessionId = sessionId,
                    message = message
                )
            )
        }
    }

    suspend fun getTaskStatus(taskId: String): Response<BaseResponse<TaskResult>> {
        return withContext(Dispatchers.IO) {
            chatApiService.getTaskStatus(taskId)
        }
    }

    suspend fun endChatSession(sessionId: String): Response<BaseResponse<Any>> {
        return withContext(Dispatchers.IO) {
            chatApiService.endChatSession(sessionId)
        }
    }
    
    suspend fun handleProfileConflict(sessionId: String): Response<BaseResponse<ProfileConflictResult>> {
        return withContext(Dispatchers.IO) {
            chatApiService.handleProfileConflict(
                ProfileConflictRequest(
                    sessionId = sessionId,
                    choice = "yes"
                )
            )
        }
    }
}