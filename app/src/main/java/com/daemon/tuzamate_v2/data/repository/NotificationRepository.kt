package com.daemon.tuzamate_v2.data.repository

import com.daemon.tuzamate_v2.data.model.*
import com.daemon.tuzamate_v2.data.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getNotifications(cursor: Int? = null, size: Int? = null): Response<NotificationListResponse> {
        return withContext(Dispatchers.IO) {
            apiService.getNotifications(cursor, size)
        }
    }
    
    suspend fun getNotification(notificationId: Int): Response<NotificationDetailResponse> {
        return withContext(Dispatchers.IO) {
            apiService.getNotification(notificationId)
        }
    }
    
    suspend fun deleteNotification(notificationId: Int): Response<NotificationDeleteResponse> {
        return withContext(Dispatchers.IO) {
            apiService.deleteNotification(notificationId)
        }
    }
    
    suspend fun markNotificationAsRead(notificationId: Int): Response<NotificationReadResponse> {
        return withContext(Dispatchers.IO) {
            apiService.markNotificationAsRead(notificationId)
        }
    }
}