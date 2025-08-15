package com.daemon.tuzamate_v2.presentation.contents

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daemon.tuzamate_v2.data.model.BoardType
import com.daemon.tuzamate_v2.data.model.PostPreview
import com.daemon.tuzamate_v2.data.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlazaViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    private val _posts = MutableLiveData<List<PostPreview>>()
    val posts: LiveData<List<PostPreview>> get() = _posts
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading
    
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage
    
    private val _hasNext = MutableLiveData<Boolean>()
    val hasNext: LiveData<Boolean> get() = _hasNext
    
    private var nextCursor: Int? = null
    private val allPosts = mutableListOf<PostPreview>()
    private val likedPostIds = mutableSetOf<Int>()
    private val scrapedPostIds = mutableSetOf<Int>()
    
    fun loadPosts(isRefresh: Boolean = false) {
        if (isRefresh) {
            nextCursor = null
            allPosts.clear()
        }
        
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            try {
                val response = postRepository.getPosts(BoardType.FREE, nextCursor)
                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    response.body()?.result?.let { result ->
                        allPosts.addAll(result.postPreviewDTOList)
                        _posts.value = allPosts.toList()
                        nextCursor = result.nextCursor
                        _hasNext.value = result.hasNext
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
    
    fun toggleLike(postId: Int) {
        viewModelScope.launch {
            try {
                val isLiked = likedPostIds.contains(postId)
                val response = if (isLiked) {
                    postRepository.unlikePost(postId)
                } else {
                    postRepository.likePost(postId)
                }
                
                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    if (isLiked) {
                        likedPostIds.remove(postId)
                        updatePostLikeCount(postId, false)
                    } else {
                        likedPostIds.add(postId)
                        updatePostLikeCount(postId, true)
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = "좋아요 처리 중 오류가 발생했습니다."
            }
        }
    }
    
    fun toggleScrap(postId: Int) {
        viewModelScope.launch {
            try {
                val isScraped = scrapedPostIds.contains(postId)
                val response = if (isScraped) {
                    postRepository.unscrapPost(postId)
                } else {
                    postRepository.scrapPost(postId)
                }
                
                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    if (isScraped) {
                        scrapedPostIds.remove(postId)
                    } else {
                        scrapedPostIds.add(postId)
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = "스크랩 처리 중 오류가 발생했습니다."
            }
        }
    }
    
    fun isPostLiked(postId: Int): Boolean {
        return likedPostIds.contains(postId)
    }
    
    fun isPostScraped(postId: Int): Boolean {
        return scrapedPostIds.contains(postId)
    }
    
    private fun updatePostLikeCount(postId: Int, isLiked: Boolean) {
        val currentPosts = _posts.value ?: return
        val updatedPosts = currentPosts.map { post ->
            if (post.id == postId) {
                post.copy(likeNum = if (isLiked) post.likeNum + 1 else post.likeNum - 1)
            } else {
                post
            }
        }
        _posts.value = updatedPosts
    }
}