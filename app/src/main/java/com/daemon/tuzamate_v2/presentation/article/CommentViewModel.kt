package com.daemon.tuzamate_v2.presentation.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daemon.tuzamate_v2.data.model.CommentCreateRequest
import com.daemon.tuzamate_v2.data.model.CommentPreview
import com.daemon.tuzamate_v2.data.repository.CommentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentViewModel @Inject constructor(
    private val commentRepository: CommentRepository
) : ViewModel() {

    private val _comments = MutableLiveData<List<CommentPreview>>()
    val comments: LiveData<List<CommentPreview>> = _comments

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _commentCreated = MutableLiveData<Boolean>()
    val commentCreated: LiveData<Boolean> = _commentCreated

    private var currentCursor: Int? = null
    private var hasNextPage = false
    private var postId: Int = 0

    fun setPostId(id: Int) {
        postId = id
        loadComments()
    }

    fun loadComments() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                val response = commentRepository.getComments(
                    postId = postId,
                    cursor = null,
                    size = 20
                )
                
                if (response.isSuccessful) {
                    response.body()?.let { baseResponse ->
                        if (baseResponse.status == "OK" || baseResponse.status == "SUCCESS") {
                            baseResponse.result?.let { result ->
                                _comments.value = result.commentPreviewListDTO
                                hasNextPage = result.hasNext
                                currentCursor = result.nextCursor
                            } ?: run {
                                _comments.value = emptyList()
                            }
                        } else {
                            _error.value = baseResponse.message
                        }
                    }
                } else {
                    _error.value = "서버 오류가 발생했습니다. (${response.code()})"
                }
            } catch (e: Exception) {
                _error.value = "네트워크 오류: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadMoreComments() {
        if (!hasNextPage || _isLoading.value == true) return
        
        viewModelScope.launch {
            _isLoading.value = true
            
            try {
                val response = commentRepository.getComments(
                    postId = postId,
                    cursor = currentCursor,
                    size = 20
                )
                
                if (response.isSuccessful) {
                    response.body()?.let { baseResponse ->
                        if (baseResponse.status == "OK" || baseResponse.status == "SUCCESS") {
                            baseResponse.result?.let { result ->
                                val currentList = _comments.value ?: emptyList()
                                _comments.value = currentList + result.commentPreviewListDTO
                                hasNextPage = result.hasNext
                                currentCursor = result.nextCursor
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                _error.value = "더 많은 댓글을 불러올 수 없습니다."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createComment(content: String, parentId: Int? = null) {
        viewModelScope.launch {
            _error.value = null
            
            try {
                val request = CommentCreateRequest(
                    content = content,
                    parentId = parentId
                )
                
                val response = commentRepository.createComment(postId, request)
                
                if (response.isSuccessful) {
                    response.body()?.let { baseResponse ->
                        if (baseResponse.status == "OK" || baseResponse.status == "SUCCESS") {
                            _commentCreated.value = true
                            // 댓글 목록을 즉시 새로고침
                            loadComments()
                        } else {
                            _error.value = baseResponse.message
                        }
                    }
                } else {
                    _error.value = "서버 오류가 발생했습니다. (${response.code()})"
                }
            } catch (e: Exception) {
                _error.value = "네트워크 오류: ${e.message}"
            }
        }
    }

    fun clearError() {
        _error.value = null
    }

    fun resetCommentCreated() {
        _commentCreated.value = false
    }
}