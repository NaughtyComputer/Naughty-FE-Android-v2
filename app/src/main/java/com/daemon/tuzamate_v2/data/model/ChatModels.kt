package com.daemon.tuzamate_v2.data.model

import com.google.gson.annotations.SerializedName

// Chat Request
data class ChatRequest(
    @SerializedName("username")
    val username: String,
    @SerializedName("session_id")
    val sessionId: String?,
    @SerializedName("message")
    val message: String
)

// Chat Response
data class ChatResult(
    @SerializedName("task_id")
    val taskId: String? = null,
    @SerializedName("session_id")
    val sessionId: String,
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("response")
    val response: String? = null
)

// Task Status Response
data class TaskResult(
    @SerializedName("status")
    val status: String,
    @SerializedName("result")
    val result: TaskResponseContent? = null
)

data class TaskResponseContent(
    @SerializedName("type")
    val type: String,
    @SerializedName("response")
    val response: String
)

// Chat Message for UI
data class ChatMessage(
    val message: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)