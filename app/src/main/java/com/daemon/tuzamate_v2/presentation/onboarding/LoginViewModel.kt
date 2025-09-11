package com.daemon.tuzamate_v2.presentation.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daemon.tuzamate_v2.data.repository.AuthRepository
import com.daemon.tuzamate_v2.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager
) : ViewModel() {
    
    private val _loginState = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState> get() = _loginState
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading
    
    fun kakaoLogin(accessToken: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _loginState.value = LoginState.Loading
            
            try {
                val response = authRepository.kakaoLogin(accessToken)
                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    response.body()?.result?.let { loginResult ->
                        // 토큰 저장
                        tokenManager.saveTokens(
                            accessToken = loginResult.accessToken,
                            refreshToken = loginResult.refreshToken
                        )
                        // 사용자 정보 저장 (userId만 저장, email과 nickname은 나중에 업데이트)
                        tokenManager.saveUserInfo(
                            userId = loginResult.userId,
                            email = "",
                            nickname = ""
                        )
                    }
                    _loginState.value = LoginState.Success
                } else {
                    val errorMessage = response.body()?.message ?: "로그인에 실패했습니다."
                    _loginState.value = LoginState.Error(errorMessage)
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("네트워크 오류가 발생했습니다: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}

sealed class LoginState {
    object Loading : LoginState()
    object Success : LoginState()
    data class Error(val message: String) : LoginState()
}