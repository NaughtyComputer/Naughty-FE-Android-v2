package com.daemon.tuzamate_v2.data.model

import com.google.gson.annotations.SerializedName

data class KakaoLoginRequest(
    @SerializedName("accessToken")
    val accessToken: String
)

data class LoginResult(
    @SerializedName("userId")
    val userId: Long,
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String,
    @SerializedName("refreshTokenExpire")
    val refreshTokenExpire: String
)

typealias KakaoLoginResponse = BaseResponse<LoginResult>

