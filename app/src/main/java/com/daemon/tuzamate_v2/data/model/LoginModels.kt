package com.daemon.tuzamate_v2.data.model

import com.google.gson.annotations.SerializedName

data class KakaoLoginRequest(
    @SerializedName("accessToken")
    val accessToken: String
)

typealias KakaoLoginResponse = BaseResponse<Unit>

