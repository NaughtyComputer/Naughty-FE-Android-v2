package com.daemon.tuzamate_v2.data.model

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @SerializedName("isSuccess")
    val isSuccess: Boolean,
    @SerializedName("status")
    val status: String,
    @SerializedName("code")
    val code: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("result")
    val result: T?
)