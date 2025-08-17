package com.daemon.tuzamate_v2.presentation.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daemon.tuzamate_v2.data.model.BoardType
import com.daemon.tuzamate_v2.data.model.PostDetail
import com.daemon.tuzamate_v2.data.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val postRepository: PostRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val postId: Int = savedStateHandle.get<Int>("postId") ?: 0
    private val boardType: String = savedStateHandle.get<String>("boardType") ?: "FREE"
    
    private val _postDetail = MutableLiveData<PostDetail>()
    val postDetail: LiveData<PostDetail> get() = _postDetail
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading
    
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage
    
    private val _isLiked = MutableLiveData<Boolean>(false)
    val isLiked: LiveData<Boolean> get() = _isLiked
    
    private val _isScraped = MutableLiveData<Boolean>(false)
    val isScraped: LiveData<Boolean> get() = _isScraped
    
    init {
        loadPostDetail()
    }
    
    fun loadPostDetail() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            try {
                val boardTypeEnum = BoardType.valueOf(boardType)
                val response = postRepository.getPostDetail(boardTypeEnum, postId)
                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    response.body()?.result?.let { detail ->
                        _postDetail.value = detail
                        _isLiked.value = detail.liked
                        _isScraped.value = detail.scraped
                    }
                } else {
                    _errorMessage.value = "게시글을 불러오는데 실패했습니다."
                }
            } catch (e: Exception) {
                _errorMessage.value = "네트워크 오류가 발생했습니다: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun toggleLike() {
        viewModelScope.launch {
            try {
                val currentLikeStatus = _isLiked.value ?: false
                val response = if (!currentLikeStatus) {
                    postRepository.likePost(postId)
                } else {
                    postRepository.unlikePost(postId)
                }
                
                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    _isLiked.value = !currentLikeStatus
                    _postDetail.value?.let { detail ->
                        _postDetail.value = detail.copy(
                            likeNum = if (!currentLikeStatus) detail.likeNum + 1 else detail.likeNum - 1
                        )
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = "좋아요 처리 중 오류가 발생했습니다."
            }
        }
    }
    
    fun toggleScrap() {
        viewModelScope.launch {
            try {
                val currentScrapStatus = _isScraped.value ?: false
                val response = if (!currentScrapStatus) {
                    postRepository.scrapPost(postId)
                } else {
                    postRepository.unscrapPost(postId)
                }
                
                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    _isScraped.value = !currentScrapStatus
                }
            } catch (e: Exception) {
                _errorMessage.value = "스크랩 처리 중 오류가 발생했습니다."
            }
        }
    }
}