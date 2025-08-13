package com.daemon.tuzamate_v2.data.repository

import com.daemon.tuzamate_v2.data.model.*
import com.daemon.tuzamate_v2.data.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getComments(
        postId: Int,
        cursor: Int? = null,
        size: Int? = 10
    ): Response<CommentListResponse> {
        return withContext(Dispatchers.IO) {
            apiService.getComments(postId, cursor, size)
        }
    }
    
    suspend fun createComment(
        postId: Int,
        request: CommentCreateRequest
    ): Response<CommentCreateResponse> {
        return withContext(Dispatchers.IO) {
            apiService.createComment(postId, request)
        }
    }
    
    suspend fun getCommentDetail(
        postId: Int,
        commentId: Int
    ): Response<CommentDetailResponse> {
        return withContext(Dispatchers.IO) {
            apiService.getCommentDetail(postId, commentId)
        }
    }
    
    suspend fun deleteComment(
        postId: Int,
        commentId: Int
    ): Response<CommentDeleteResponse> {
        return withContext(Dispatchers.IO) {
            apiService.deleteComment(postId, commentId)
        }
    }
    
    suspend fun updateComment(
        postId: Int,
        commentId: Int,
        request: CommentUpdateRequest
    ): Response<CommentUpdateResponse> {
        return withContext(Dispatchers.IO) {
            apiService.updateComment(postId, commentId, request)
        }
    }
}