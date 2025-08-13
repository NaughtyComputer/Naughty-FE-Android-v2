package com.daemon.tuzamate_v2.data.repository

import com.daemon.tuzamate_v2.data.model.*
import com.daemon.tuzamate_v2.data.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getProfile(): Response<ProfileResponse> {
        return withContext(Dispatchers.IO) {
            apiService.getProfile()
        }
    }
    
    suspend fun deleteProfile(nickname: String): Response<DeleteProfileResponse> {
        return withContext(Dispatchers.IO) {
            apiService.deleteProfile(DeleteProfileRequest(nickname))
        }
    }
    
    suspend fun initProfile(request: InitProfileRequest): Response<InitProfileResponse> {
        return withContext(Dispatchers.IO) {
            apiService.initProfile(request)
        }
    }
    
    suspend fun getProfileLikes(cursor: Int = 0, offset: Int = 10): Response<LikesResponse> {
        return withContext(Dispatchers.IO) {
            apiService.getProfileLikes(cursor, offset)
        }
    }
    
    suspend fun getProfilePosts(cursor: Int = 0, offset: Int = 10): Response<PostsResponse> {
        return withContext(Dispatchers.IO) {
            apiService.getProfilePosts(cursor, offset)
        }
    }
    
    suspend fun getProfileScraps(cursor: Int = 0, offset: Int = 10): Response<ScrapsResponse> {
        return withContext(Dispatchers.IO) {
            apiService.getProfileScraps(cursor, offset)
        }
    }
}