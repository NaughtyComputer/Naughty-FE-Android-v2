package com.daemon.tuzamate_v2.data.model

import com.google.gson.annotations.SerializedName

data class Comment(
    @SerializedName("id")
    val id: Int,
    @SerializedName("postId")
    val postId: Int,
    @SerializedName("parentId")
    val parentId: Int? = null,
    @SerializedName("content")
    val content: String,
    @SerializedName("writerName")
    val writerName: String? = null,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("children")
    val children: List<Comment> = emptyList()
)

data class CommentPreview(
    @SerializedName("id")
    val id: Int,
    @SerializedName("postId")
    val postId: Int,
    @SerializedName("parentId")
    val parentId: Int? = null,
    @SerializedName("content")
    val content: String,
    @SerializedName("writerName")
    val writerName: String? = null,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)

data class CommentListResult(
    @SerializedName("commentPreviewListDTO")
    val commentPreviewListDTO: List<CommentPreview>,
    @SerializedName("hasNext")
    val hasNext: Boolean,
    @SerializedName("nextCursor")
    val nextCursor: Int? = null
)

data class CommentCreateRequest(
    @SerializedName("content")
    val content: String,
    @SerializedName("parentId")
    val parentId: Int? = null
)

data class CommentCreateResult(
    @SerializedName("id")
    val id: Int,
    @SerializedName("createdAt")
    val createdAt: String
)

data class CommentDeleteResult(
    @SerializedName("id")
    val id: Int,
    @SerializedName("deletedAt")
    val deletedAt: String
)

data class CommentUpdateRequest(
    @SerializedName("content")
    val content: String
)

data class CommentUpdateResult(
    @SerializedName("id")
    val id: Int,
    @SerializedName("updatedAt")
    val updatedAt: String
)

typealias CommentListResponse = BaseResponse<CommentListResult>
typealias CommentCreateResponse = BaseResponse<CommentCreateResult>
typealias CommentDetailResponse = BaseResponse<Comment>
typealias CommentDeleteResponse = BaseResponse<CommentDeleteResult>
typealias CommentUpdateResponse = BaseResponse<CommentUpdateResult>