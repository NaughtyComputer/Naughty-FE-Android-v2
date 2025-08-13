package com.daemon.tuzamate_v2.data.model

import com.google.gson.annotations.SerializedName

data class NotificationItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("isRead")
    val isRead: Boolean,
    @SerializedName("createdAt")
    val createdAt: String? = null
)

data class NotificationListResult(
    @SerializedName("notifications")
    val notifications: List<NotificationItem>,
    @SerializedName("hasNext")
    val hasNext: Boolean,
    @SerializedName("nextCursor")
    val nextCursor: Int?
)

data class NotificationDetailResult(
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("isRead")
    val isRead: Boolean,
    @SerializedName("targetId")
    val targetId: Int
)

typealias NotificationListResponse = BaseResponse<NotificationListResult>
typealias NotificationDetailResponse = BaseResponse<NotificationDetailResult>
typealias NotificationDeleteResponse = BaseResponse<Unit>
typealias NotificationReadResponse = BaseResponse<NotificationDetailResult>