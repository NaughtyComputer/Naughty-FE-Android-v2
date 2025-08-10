package com.daemon.tuzamate_v2.data.repository

import com.daemon.tuzamate_v2.data.model.ProfileResponse
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
}