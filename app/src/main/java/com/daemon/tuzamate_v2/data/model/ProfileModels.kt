package com.daemon.tuzamate_v2.data.model

import com.google.gson.annotations.SerializedName

data class ProfileResult(
    @SerializedName("myInfo")
    val myInfo: MyInfo,
    @SerializedName("investmentInfo")
    val investmentInfo: InvestmentInfo
)

data class MyInfo(
    @SerializedName("nickname")
    val nickname: String?,
    @SerializedName("gender")
    val gender: String?,
    @SerializedName("email")
    val email: String
)

data class InvestmentInfo(
    @SerializedName("income")
    val income: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("purpose")
    val purpose: String?
)

typealias ProfileResponse = BaseResponse<ProfileResult>