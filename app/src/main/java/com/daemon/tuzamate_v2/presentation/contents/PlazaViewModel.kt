package com.daemon.tuzamate_v2.presentation.contents

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daemon.tuzamate_v2.data.model.BoardType
import com.daemon.tuzamate_v2.data.model.PostPreview
import com.daemon.tuzamate_v2.data.repository.PostRepository
import com.daemon.tuzamate_v2.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlazaViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    enum class ContentType {
        PLAZA,      // 일반 게시글 목록
        MY_POSTS,   // 내가 작성한 글
        MY_LIKES,   // 내가 좋아요한 글
        MY_SCRAPS   // 내가 스크랩한 글
    }

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

    private var currentContentType: ContentType = ContentType.PLAZA
    
    fun setContentType(contentType: ContentType) {
        currentContentType = contentType
        loadPosts(isRefresh = true)
    }

    fun loadPosts(isRefresh: Boolean = false) {
        if (isRefresh) {
            nextCursor = null
            allPosts.clear()
        }

        when (currentContentType) {
            ContentType.PLAZA -> loadPlazaPosts(isRefresh)
            ContentType.MY_POSTS -> loadMyPosts(isRefresh)
            ContentType.MY_LIKES -> loadMyLikes(isRefresh)
            ContentType.MY_SCRAPS -> loadMyScraps(isRefresh)
        }
    }

    private fun loadPlazaPosts(isRefresh: Boolean) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val response = postRepository.getPosts(BoardType.FREE, nextCursor)
                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    response.body()?.result?.let { result ->
                        val posts = result.postPreviewDTOList ?: emptyList()
                        if (isRefresh) {
                            allPosts.clear()
                            allPosts.addAll(posts)
                        } else {
                            allPosts.addAll(posts)
                        }
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

    private fun loadMyPosts(isRefresh: Boolean) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val cursor = if (isRefresh) 0 else (nextCursor ?: 0)
                Log.d("PlazaViewModel", "Loading my posts with cursor: $cursor")
                val response = profileRepository.getProfilePosts(cursor)
                Log.d("PlazaViewModel", "Response code: ${response.code()}, isSuccessful: ${response.isSuccessful}")

                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("PlazaViewModel", "Response body: $body")
                    Log.d("PlazaViewModel", "isSuccess: ${body?.isSuccess}, result: ${body?.result}")

                    if (body?.isSuccess == true) {
                        val result = body.result
                        if (result != null) {
                            val myPosts = result.scraps ?: emptyList()
                            Log.d("PlazaViewModel", "Posts count: ${myPosts.size}")

                            // MyPostItem을 PostPreview로 변환
                            val posts = myPosts.map { item ->
                                PostPreview(
                                    id = item.postId,
                                    title = item.title,
                                    content = item.contentPreview,
                                    likeNum = 0,
                                    author = "",
                                    commentNum = 0,
                                    isRead = false,
                                    createdAt = "",
                                    updatedAt = ""
                                )
                            }

                            if (isRefresh) {
                                allPosts.clear()
                                allPosts.addAll(posts)
                            } else {
                                allPosts.addAll(posts)
                            }
                            _posts.value = allPosts.toList()
                            nextCursor = result.cursor
                            _hasNext.value = result.hasNextPage
                        } else {
                            Log.e("PlazaViewModel", "Result is null")
                            _errorMessage.value = "데이터가 없습니다."
                        }
                    } else {
                        Log.e("PlazaViewModel", "Response not successful: ${body?.message}")
                        _errorMessage.value = body?.message ?: "내가 작성한 글을 불러오는데 실패했습니다."
                    }
                } else {
                    Log.e("PlazaViewModel", "HTTP error: ${response.code()}")
                    _errorMessage.value = "HTTP 오류: ${response.code()}"
                }
            } catch (e: Exception) {
                Log.e("PlazaViewModel", "Exception occurred", e)
                _errorMessage.value = "네트워크 오류가 발생했습니다: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun loadMyLikes(isRefresh: Boolean) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val cursor = if (isRefresh) 0 else (nextCursor ?: 0)
                Log.d("PlazaViewModel", "Loading my likes with cursor: $cursor")
                val response = profileRepository.getProfileLikes(cursor)
                Log.d("PlazaViewModel", "Response code: ${response.code()}, isSuccessful: ${response.isSuccessful}")

                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("PlazaViewModel", "Response body: $body")
                    Log.d("PlazaViewModel", "isSuccess: ${body?.isSuccess}, result: ${body?.result}")

                    if (body?.isSuccess == true) {
                        val result = body.result
                        if (result != null) {
                            val myPosts = result.scraps ?: emptyList()
                            Log.d("PlazaViewModel", "Likes count: ${myPosts.size}")

                            // MyPostItem을 PostPreview로 변환
                            val posts = myPosts.map { item ->
                                PostPreview(
                                    id = item.postId,
                                    title = item.title,
                                    content = item.contentPreview,
                                    likeNum = 0,
                                    author = "",
                                    commentNum = 0,
                                    isRead = false,
                                    createdAt = "",
                                    updatedAt = ""
                                )
                            }

                            if (isRefresh) {
                                allPosts.clear()
                                allPosts.addAll(posts)
                            } else {
                                allPosts.addAll(posts)
                            }
                            _posts.value = allPosts.toList()
                            nextCursor = result.cursor
                            _hasNext.value = result.hasNextPage
                        } else {
                            Log.e("PlazaViewModel", "Result is null")
                            _errorMessage.value = "데이터가 없습니다."
                        }
                    } else {
                        Log.e("PlazaViewModel", "Response not successful: ${body?.message}")
                        _errorMessage.value = body?.message ?: "좋아요한 글을 불러오는데 실패했습니다."
                    }
                } else {
                    Log.e("PlazaViewModel", "HTTP error: ${response.code()}")
                    _errorMessage.value = "HTTP 오류: ${response.code()}"
                }
            } catch (e: Exception) {
                Log.e("PlazaViewModel", "Exception occurred", e)
                _errorMessage.value = "네트워크 오류가 발생했습니다: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun loadMyScraps(isRefresh: Boolean) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val cursor = if (isRefresh) 0 else (nextCursor ?: 0)
                Log.d("PlazaViewModel", "Loading my scraps with cursor: $cursor")
                val response = profileRepository.getProfileScraps(cursor)
                Log.d("PlazaViewModel", "Response code: ${response.code()}, isSuccessful: ${response.isSuccessful}")

                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("PlazaViewModel", "Response body: $body")
                    Log.d("PlazaViewModel", "isSuccess: ${body?.isSuccess}, result: ${body?.result}")

                    if (body?.isSuccess == true) {
                        val result = body.result
                        if (result != null) {
                            val myPosts = result.scraps ?: emptyList()
                            Log.d("PlazaViewModel", "Scraps count: ${myPosts.size}")

                            // MyPostItem을 PostPreview로 변환
                            val posts = myPosts.map { item ->
                                PostPreview(
                                    id = item.postId,
                                    title = item.title,
                                    content = item.contentPreview,
                                    likeNum = 0,
                                    author = "",
                                    commentNum = 0,
                                    isRead = false,
                                    createdAt = "",
                                    updatedAt = ""
                                )
                            }

                            if (isRefresh) {
                                allPosts.clear()
                                allPosts.addAll(posts)
                            } else {
                                allPosts.addAll(posts)
                            }
                            _posts.value = allPosts.toList()
                            nextCursor = result.cursor
                            _hasNext.value = result.hasNextPage
                        } else {
                            Log.e("PlazaViewModel", "Result is null")
                            _errorMessage.value = "데이터가 없습니다."
                        }
                    } else {
                        Log.e("PlazaViewModel", "Response not successful: ${body?.message}")
                        _errorMessage.value = body?.message ?: "스크랩한 글을 불러오는데 실패했습니다."
                    }
                } else {
                    Log.e("PlazaViewModel", "HTTP error: ${response.code()}")
                    _errorMessage.value = "HTTP 오류: ${response.code()}"
                }
            } catch (e: Exception) {
                Log.e("PlazaViewModel", "Exception occurred", e)
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