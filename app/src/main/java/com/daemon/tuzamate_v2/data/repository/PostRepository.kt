package com.daemon.tuzamate_v2.data.repository

import com.daemon.tuzamate_v2.data.model.*
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
    suspend fun getPosts(
        boardType: BoardType,
        cursor: Int? = null,
        size: Int? = 10
    ): Response<PostListResponse> {
        return withContext(Dispatchers.IO) {
            apiService.getPosts(boardType.name, cursor, size)
        }
    }
    
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
    
    suspend fun getPostDetail(
        boardType: BoardType,
        postId: Int
    ): Response<PostDetailResponse> {
        return withContext(Dispatchers.IO) {
            apiService.getPostDetail(boardType.name, postId)
        }
    }
    
    suspend fun deletePost(
        boardType: BoardType,
        postId: Int
    ): Response<DeletePostResponse> {
        return withContext(Dispatchers.IO) {
            apiService.deletePost(boardType.name, postId)
        }
    }
    
    suspend fun updatePost(
        boardType: BoardType,
        postId: Int,
        request: UpdatePostRequest
    ): Response<UpdatePostResponse> {
        return withContext(Dispatchers.IO) {
            apiService.updatePost(boardType.name, postId, request)
        }
    }
    
    suspend fun likePost(postId: Int): Response<LikeResponse> {
        return withContext(Dispatchers.IO) {
            apiService.likePost(postId)
        }
    }
    
    suspend fun unlikePost(postId: Int): Response<LikeResponse> {
        return withContext(Dispatchers.IO) {
            apiService.unlikePost(postId)
        }
    }
    
    suspend fun scrapPost(postId: Int): Response<ScrapResponse> {
        return withContext(Dispatchers.IO) {
            apiService.scrapPost(postId)
        }
    }
    
    suspend fun unscrapPost(postId: Int): Response<ScrapResponse> {
        return withContext(Dispatchers.IO) {
            apiService.unscrapPost(postId)
        }
    }
}