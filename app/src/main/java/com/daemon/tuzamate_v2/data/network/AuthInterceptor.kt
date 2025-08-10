package com.daemon.tuzamate_v2.data.network

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor() : Interceptor {
    
    // 임시 하드코딩된 토큰 (테스트용)
    private val testToken = ""
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequest = request.newBuilder()
            .addHeader("Authorization", "Bearer $testToken")
            .build()
        return chain.proceed(newRequest)
    }
}