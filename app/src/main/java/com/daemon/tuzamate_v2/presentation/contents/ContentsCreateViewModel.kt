package com.daemon.tuzamate_v2.presentation.contents

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daemon.tuzamate_v2.data.model.BoardType
import com.daemon.tuzamate_v2.data.model.CreatePostRequest
import com.daemon.tuzamate_v2.data.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContentsCreateViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _createSuccess = MutableLiveData<Boolean>()
    val createSuccess: LiveData<Boolean> get() = _createSuccess

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun createPost(title: String, content: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val response = postRepository.createPost(BoardType.FREE, title = title, content = content)
                
                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    _createSuccess.value = true
                } else {
                    _errorMessage.value = "게시물 작성에 실패했습니다."
                }
            } catch (e: Exception) {
                _errorMessage.value = "네트워크 오류가 발생했습니다: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}