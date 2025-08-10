package com.daemon.tuzamate_v2.data.model

import com.google.gson.annotations.SerializedName

enum class BoardType {
    FREE, INFO
}

data class CreatePostRequest(
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String
)

data class CreatePostResult(
    @SerializedName("id")
    val id: Long,
    @SerializedName("createdAt")
    val createdAt: String
)

typealias CreatePostResponse = BaseResponse<CreatePostResult>