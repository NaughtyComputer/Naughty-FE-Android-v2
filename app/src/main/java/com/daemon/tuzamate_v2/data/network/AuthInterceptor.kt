package com.daemon.tuzamate_v2.data.network

import com.daemon.tuzamate_v2.utils.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {
    
    // 토큰이 필요 없는 엔드포인트 리스트
    private val publicEndpoints = listOf(
        "auth/kakao-login",
        "auth/login",
        "auth/register",
        "auth/refresh"
    )
    
    // 임시 하드코딩된 토큰 (테스트용)
    private val testToken = ""
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url.toString()
        
        // 인증이 필요 없는 엔드포인트인지 확인
        val isPublicEndpoint = publicEndpoints.any { endpoint ->
            url.contains(endpoint)
        }
        
        // 인증이 필요 없는 엔드포인트면 토큰 없이 요청
        if (isPublicEndpoint) {
            return chain.proceed(request)
        }
        
        // 임시: 테스트 토큰 사용 (카카오 로그인 구현 완료 후 제거)
        val newRequest = request.newBuilder()
            .addHeader("Authorization", "Bearer $testToken")
            .build()
        
        // 원래 코드 (카카오 로그인 구현 완료 후 주석 해제)
        /*
        // 저장된 토큰 가져오기
        val token = tokenManager.getAccessToken()
        
        // 토큰이 있으면 헤더에 추가
        val newRequest = if (token != null) {
            request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            request
        }
        */
        
        return chain.proceed(newRequest)
    }
}