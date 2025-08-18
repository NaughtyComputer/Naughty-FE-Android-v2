package com.daemon.tuzamate_v2.data.repository

import com.daemon.tuzamate_v2.data.model.KakaoLoginRequest
import com.daemon.tuzamate_v2.data.model.KakaoLoginResponse
import com.daemon.tuzamate_v2.data.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun kakaoLogin(accessToken: String): Response<KakaoLoginResponse> {
        return withContext(Dispatchers.IO) {
            apiService.kakaoLogin(KakaoLoginRequest(accessToken))
        }
    }
}