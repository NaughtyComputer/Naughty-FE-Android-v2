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

data class DeleteProfileRequest(
    @SerializedName("nickname")
    val nickname: String
)

data class InitProfileRequest(
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("experience")
    val experience: String
)

data class MyPostItem(
    @SerializedName("postId")
    val postId: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("contentPreview")
    val contentPreview: String
)

data class MyPostsResult(
    @SerializedName("scraps")
    val scraps: List<MyPostItem>?,
    @SerializedName("hasNextPage")
    val hasNextPage: Boolean,
    @SerializedName("cursor")
    val cursor: Int?
)

typealias ProfileResponse = BaseResponse<ProfileResult>
typealias DeleteProfileResponse = BaseResponse<Unit>
typealias InitProfileResponse = BaseResponse<Unit>
typealias LikesResponse = BaseResponse<MyPostsResult>
typealias PostsResponse = BaseResponse<MyPostsResult>
typealias ScrapsResponse = BaseResponse<MyPostsResult>