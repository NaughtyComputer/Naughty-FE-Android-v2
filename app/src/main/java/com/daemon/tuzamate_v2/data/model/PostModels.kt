package com.daemon.tuzamate_v2.data.model

import com.google.gson.annotations.SerializedName

enum class BoardType {
    FREE, INFO
}

data class PostPreview(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("likeNum")
    val likeNum: Int,
    @SerializedName("author")
    val author: String,
    @SerializedName("commentNum")
    val commentNum: Int,
    @SerializedName("isRead")
    val isRead: Boolean,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)

data class PostListResult(
    @SerializedName("postPreviewDTOList")
    val postPreviewDTOList: List<PostPreview>,
    @SerializedName("nextCursor")
    val nextCursor: Int?,
    @SerializedName("hasNext")
    val hasNext: Boolean
)

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

data class PostDetail(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("likeNum")
    val likeNum: Int,
    @SerializedName("author")
    val author: String,
    @SerializedName("commentNum")
    val commentNum: Int,
    @SerializedName("isRead")
    val isRead: Boolean,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)

data class DeletePostResult(
    @SerializedName("id")
    val id: Int,
    @SerializedName("deletedAt")
    val deletedAt: String?
)

data class UpdatePostRequest(
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String
)

data class UpdatePostResult(
    @SerializedName("id")
    val id: Int,
    @SerializedName("updatedAt")
    val updatedAt: String
)

typealias PostListResponse = BaseResponse<PostListResult>
typealias CreatePostResponse = BaseResponse<CreatePostResult>
typealias PostDetailResponse = BaseResponse<PostDetail>
typealias DeletePostResponse = BaseResponse<DeletePostResult>
typealias UpdatePostResponse = BaseResponse<UpdatePostResult>
typealias LikeResponse = BaseResponse<String>
typealias ScrapResponse = BaseResponse<String>