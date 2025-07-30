package com.daemon.tuzamate_v2.presentation.contents

data class NewsLetter(
    val id: Long,
    val title: String,
    val content: String,
    val author: String,
    val publishDate: String,
    val imageUrl: String? = null,
    val readTime: String,
    val category: String,
    val isRead: Boolean = false
)
