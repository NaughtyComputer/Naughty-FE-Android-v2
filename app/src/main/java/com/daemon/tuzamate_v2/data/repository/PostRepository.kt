package com.daemon.tuzamate_v2.data.repository

import com.daemon.tuzamate_v2.data.model.BoardType
import com.daemon.tuzamate_v2.data.model.CreatePostRequest
import com.daemon.tuzamate_v2.data.model.CreatePostResponse
import com.daemon.tuzamate_v2.data.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun createPost(
        boardType: BoardType,
        title: String,
        content: String
    ): Response<CreatePostResponse> {
        return withContext(Dispatchers.IO) {
            apiService.createPost(
                boardType = boardType.name,
                request = CreatePostRequest(title, content)
            )
        }
    }
}