package com.daemon.tuzamate_v2.utils

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreditManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    
    private val _creditFlow = MutableStateFlow(getCurrentCredit())
    val creditFlow: StateFlow<Int> = _creditFlow.asStateFlow()
    
    companion object {
        private const val PREFS_NAME = "credit_prefs"
        private const val KEY_CREDIT = "user_credit"
        private const val INITIAL_CREDIT = 300 // 초기 크레딧
        private const val CREDIT_PER_POST_VIEW = 10 // 게시글 조회 시 획득 크레딧
        private const val CREDIT_PER_AI_CHAT = 5 // AI 채팅 시 차감 크레딧
        private const val POST_VIEW_TIME_THRESHOLD = 5000L // 5초 이상 봐야 크레딧 지급 (밀리초)
    }
    
    fun getCurrentCredit(): Int {
        return prefs.getInt(KEY_CREDIT, INITIAL_CREDIT)
    }
    
    fun addCredit(amount: Int) {
        val currentCredit = getCurrentCredit()
        val newCredit = currentCredit + amount
        saveCredit(newCredit)
        _creditFlow.value = newCredit
    }
    
    fun deductCredit(amount: Int): Boolean {
        val currentCredit = getCurrentCredit()
        return if (currentCredit >= amount) {
            val newCredit = currentCredit - amount
            saveCredit(newCredit)
            _creditFlow.value = newCredit
            true
        } else {
            false // 크레딧 부족
        }
    }
    
    fun hasEnoughCredit(amount: Int): Boolean {
        return getCurrentCredit() >= amount
    }
    
    fun addCreditForPostView() {
        addCredit(CREDIT_PER_POST_VIEW)
    }
    
    fun deductCreditForAIChat(): Boolean {
        return deductCredit(CREDIT_PER_AI_CHAT)
    }
    
    fun getCreditForAIChat(): Int {
        return CREDIT_PER_AI_CHAT
    }
    
    fun getPostViewTimeThreshold(): Long {
        return POST_VIEW_TIME_THRESHOLD
    }
    
    private fun saveCredit(credit: Int) {
        prefs.edit().putInt(KEY_CREDIT, credit).apply()
    }
    
    fun resetCredit() {
        saveCredit(INITIAL_CREDIT)
        _creditFlow.value = INITIAL_CREDIT
    }
}