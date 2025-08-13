package com.daemon.tuzamate_v2.presentation.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.daemon.tuzamate_v2.data.model.BoardType
import com.daemon.tuzamate_v2.data.model.CommentCreateRequest
import com.daemon.tuzamate_v2.data.model.CommentUpdateRequest
import com.daemon.tuzamate_v2.data.model.InitProfileRequest
import com.daemon.tuzamate_v2.data.model.UpdatePostRequest
import com.daemon.tuzamate_v2.data.repository.CommentRepository
import com.daemon.tuzamate_v2.data.repository.NotificationRepository
import com.daemon.tuzamate_v2.data.repository.PostRepository
import com.daemon.tuzamate_v2.data.repository.ProfileRepository
import com.daemon.tuzamate_v2.databinding.FragmentApiTestBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ApiTestFragment : Fragment() {
    
    private var _binding: FragmentApiTestBinding? = null
    private val binding get() = _binding!!
    
    @Inject
    lateinit var profileRepository: ProfileRepository
    
    @Inject
    lateinit var postRepository: PostRepository
    
    @Inject
    lateinit var notificationRepository: NotificationRepository
    
    @Inject
    lateinit var commentRepository: CommentRepository
    
    private lateinit var testAdapter: ApiTestAdapter
    private val testResults = mutableListOf<ApiTestResult>()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentApiTestBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }
    
    private fun setupUI() {
        testAdapter = ApiTestAdapter(testResults)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = testAdapter
        }
        
        binding.btnRunAllTests.setOnClickListener {
            runAllTests()
        }
        
        binding.btnClearResults.setOnClickListener {
            testResults.clear()
            testAdapter.notifyDataSetChanged()
        }
    }
    
    private fun runAllTests() {
        testResults.clear()
        
        lifecycleScope.launch {
            // 1. 프로필 조회
            testGetProfile()
            
            // 2. 프로필 초기화
            testInitProfile()
            
            // 3. 작성한 게시글 조회
            testGetProfilePosts()
            
            // 4. 좋아요한 게시글 조회
            testGetProfileLikes()
            
            // 5. 스크랩한 게시글 조회
            testGetProfileScraps()
            
            // 6. 게시글 작성
            testCreatePost()
            
            // 7. 알림 목록 조회
            testGetNotifications()
            
            // 8. 단일 알림 조회
            testGetNotificationDetail()
            
            // 9. 알림 읽음 처리
            testMarkNotificationAsRead()
            
            // 10. 알림 삭제
            testDeleteNotification()
            
            // 11. 댓글 목록 조회
            testGetComments()
            
            // 12. 댓글 생성
            testCreateComment()
            
            // 13. 댓글 상세 조회
            testGetCommentDetail()
            
            // 14. 댓글 수정
            testUpdateComment()
            
            // 15. 댓글 삭제
            testDeleteComment()
            
            // 16. 게시글 목록 조회
            testGetPosts()
            
            // 17. 게시글 상세 조회
            testGetPostDetail()
            
            // 18. 게시글 수정
            testUpdatePost()
            
            // 19. 게시글 삭제
            testDeletePost()
            
            // 20. 게시글 좋아요
            testLikePost()
            
            // 21. 게시글 좋아요 취소
            testUnlikePost()
            
            // 22. 게시글 스크랩
            testScrapPost()
            
            // 23. 게시글 스크랩 취소
            testUnscrapPost()
        }
    }
    
    private suspend fun testGetProfile() {
        val testName = "프로필 조회"
        try {
            val response = profileRepository.getProfile()
            
            val result = if (response.isSuccessful) {
                val profile = response.body()
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.SUCCESS,
                    message = "Email: ${profile?.result?.myInfo?.email}\n" +
                            "Nickname: ${profile?.result?.myInfo?.nickname}",
                    responseCode = response.code()
                )
            } else {
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.FAILURE,
                    message = response.errorBody()?.string() ?: "Unknown error",
                    responseCode = response.code()
                )
            }
            
            testResults.add(result)
            testAdapter.notifyItemInserted(testResults.size - 1)
        } catch (e: Exception) {
            testResults.add(
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.ERROR,
                    message = e.message ?: "Exception occurred",
                    responseCode = -1
                )
            )
            testAdapter.notifyItemInserted(testResults.size - 1)
        }
    }
    
    private suspend fun testInitProfile() {
        val testName = "프로필 초기화"
        try {
            val request = InitProfileRequest(
                nickname = "테스트유저${System.currentTimeMillis() % 1000}",
                experience = "재테크 경험 없음"
            )
            val response = profileRepository.initProfile(request)
            
            val result = if (response.isSuccessful) {
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.SUCCESS,
                    message = "프로필 초기화 성공",
                    responseCode = response.code()
                )
            } else {
                val errorBody = response.errorBody()?.string()
                ApiTestResult(
                    testName = testName,
                    status = if (errorBody?.contains("USER40002") == true) TestStatus.SUCCESS else TestStatus.FAILURE,
                    message = errorBody ?: "Unknown error",
                    responseCode = response.code()
                )
            }
            
            testResults.add(result)
            testAdapter.notifyItemInserted(testResults.size - 1)
        } catch (e: Exception) {
            testResults.add(
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.ERROR,
                    message = e.message ?: "Exception occurred",
                    responseCode = -1
                )
            )
            testAdapter.notifyItemInserted(testResults.size - 1)
        }
    }
    
    private suspend fun testGetProfilePosts() {
        val testName = "작성한 게시글 조회"
        try {
            val response = profileRepository.getProfilePosts(cursor = 0, offset = 10)
            
            val result = if (response.isSuccessful) {
                val posts = response.body()
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.SUCCESS,
                    message = "게시글 수: ${posts?.result?.scraps?.size ?: 0}\n" +
                            "다음 페이지: ${posts?.result?.hasNextPage}",
                    responseCode = response.code()
                )
            } else {
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.FAILURE,
                    message = response.errorBody()?.string() ?: "Unknown error",
                    responseCode = response.code()
                )
            }
            
            testResults.add(result)
            testAdapter.notifyItemInserted(testResults.size - 1)
        } catch (e: Exception) {
            testResults.add(
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.ERROR,
                    message = e.message ?: "Exception occurred",
                    responseCode = -1
                )
            )
            testAdapter.notifyItemInserted(testResults.size - 1)
        }
    }
    
    private suspend fun testGetProfileLikes() {
        val testName = "좋아요한 게시글 조회"
        try {
            val response = profileRepository.getProfileLikes(cursor = 0, offset = 10)
            
            val result = if (response.isSuccessful) {
                val likes = response.body()
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.SUCCESS,
                    message = "좋아요 수: ${likes?.result?.scraps?.size ?: 0}\n" +
                            "다음 페이지: ${likes?.result?.hasNextPage}",
                    responseCode = response.code()
                )
            } else {
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.FAILURE,
                    message = response.errorBody()?.string() ?: "Unknown error",
                    responseCode = response.code()
                )
            }
            
            testResults.add(result)
            testAdapter.notifyItemInserted(testResults.size - 1)
        } catch (e: Exception) {
            testResults.add(
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.ERROR,
                    message = e.message ?: "Exception occurred",
                    responseCode = -1
                )
            )
            testAdapter.notifyItemInserted(testResults.size - 1)
        }
    }
    
    private suspend fun testGetProfileScraps() {
        val testName = "스크랩한 게시글 조회"
        try {
            val response = profileRepository.getProfileScraps(cursor = 0, offset = 10)
            
            val result = if (response.isSuccessful) {
                val scraps = response.body()
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.SUCCESS,
                    message = "스크랩 수: ${scraps?.result?.scraps?.size ?: 0}\n" +
                            "다음 페이지: ${scraps?.result?.hasNextPage}",
                    responseCode = response.code()
                )
            } else {
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.FAILURE,
                    message = response.errorBody()?.string() ?: "Unknown error",
                    responseCode = response.code()
                )
            }
            
            testResults.add(result)
            testAdapter.notifyItemInserted(testResults.size - 1)
        } catch (e: Exception) {
            testResults.add(
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.ERROR,
                    message = e.message ?: "Exception occurred",
                    responseCode = -1
                )
            )
            testAdapter.notifyItemInserted(testResults.size - 1)
        }
    }
    
    private suspend fun testCreatePost() {
        val testName = "게시글 작성"
        try {
            val response = postRepository.createPost(
                boardType = BoardType.FREE,
                title = "API 테스트 - ${System.currentTimeMillis()}",
                content = "테스트 내용입니다."
            )
            
            val result = if (response.isSuccessful) {
                val post = response.body()
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.SUCCESS,
                    message = "Post ID: ${post?.result?.id}\n" +
                            "Created: ${post?.result?.createdAt}",
                    responseCode = response.code()
                )
            } else {
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.FAILURE,
                    message = response.errorBody()?.string() ?: "Unknown error",
                    responseCode = response.code()
                )
            }
            
            testResults.add(result)
            testAdapter.notifyItemInserted(testResults.size - 1)
        } catch (e: Exception) {
            testResults.add(
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.ERROR,
                    message = e.message ?: "Exception occurred",
                    responseCode = -1
                )
            )
            testAdapter.notifyItemInserted(testResults.size - 1)
        }
    }
    
    private suspend fun testGetNotifications() {
        val testName = "알림 목록 조회"
        try {
            val response = notificationRepository.getNotifications(cursor = null, size = 10)
            
            val result = if (response.isSuccessful) {
                val notifications = response.body()
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.SUCCESS,
                    message = "알림 수: ${notifications?.result?.notifications?.size ?: 0}\n" +
                            "다음 페이지: ${notifications?.result?.hasNext}",
                    responseCode = response.code()
                )
            } else {
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.FAILURE,
                    message = response.errorBody()?.string() ?: "Unknown error",
                    responseCode = response.code()
                )
            }
            
            testResults.add(result)
            testAdapter.notifyItemInserted(testResults.size - 1)
        } catch (e: Exception) {
            testResults.add(
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.ERROR,
                    message = e.message ?: "Exception occurred",
                    responseCode = -1
                )
            )
            testAdapter.notifyItemInserted(testResults.size - 1)
        }
    }
    
    private suspend fun testGetNotificationDetail() {
        val testName = "단일 알림 조회"
        try {
            // 테스트용 알림 ID (실제로는 존재하지 않을 수 있음)
            val testNotificationId = 1
            val response = notificationRepository.getNotification(testNotificationId)
            
            val result = if (response.isSuccessful) {
                val notification = response.body()
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.SUCCESS,
                    message = "제목: ${notification?.result?.title}\n" +
                            "읽음 여부: ${notification?.result?.isRead}",
                    responseCode = response.code()
                )
            } else {
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.FAILURE,
                    message = response.errorBody()?.string() ?: "Unknown error",
                    responseCode = response.code()
                )
            }
            
            testResults.add(result)
            testAdapter.notifyItemInserted(testResults.size - 1)
        } catch (e: Exception) {
            testResults.add(
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.ERROR,
                    message = e.message ?: "Exception occurred",
                    responseCode = -1
                )
            )
            testAdapter.notifyItemInserted(testResults.size - 1)
        }
    }
    
    private suspend fun testMarkNotificationAsRead() {
        val testName = "알림 읽음 처리"
        try {
            // 테스트용 알림 ID
            val testNotificationId = 1
            val response = notificationRepository.markNotificationAsRead(testNotificationId)
            
            val result = if (response.isSuccessful) {
                val notification = response.body()
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.SUCCESS,
                    message = "알림 읽음 처리 완료\n" +
                            "읽음 상태: ${notification?.result?.isRead}",
                    responseCode = response.code()
                )
            } else {
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.FAILURE,
                    message = response.errorBody()?.string() ?: "Unknown error",
                    responseCode = response.code()
                )
            }
            
            testResults.add(result)
            testAdapter.notifyItemInserted(testResults.size - 1)
        } catch (e: Exception) {
            testResults.add(
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.ERROR,
                    message = e.message ?: "Exception occurred",
                    responseCode = -1
                )
            )
            testAdapter.notifyItemInserted(testResults.size - 1)
        }
    }
    
    private suspend fun testDeleteNotification() {
        val testName = "알림 삭제"
        try {
            // 테스트용 알림 ID
            val testNotificationId = 1
            val response = notificationRepository.deleteNotification(testNotificationId)
            
            val result = if (response.isSuccessful) {
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.SUCCESS,
                    message = "알림 삭제 성공",
                    responseCode = response.code()
                )
            } else {
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.FAILURE,
                    message = response.errorBody()?.string() ?: "Unknown error",
                    responseCode = response.code()
                )
            }
            
            testResults.add(result)
            testAdapter.notifyItemInserted(testResults.size - 1)
        } catch (e: Exception) {
            testResults.add(
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.ERROR,
                    message = e.message ?: "Exception occurred",
                    responseCode = -1
                )
            )
            testAdapter.notifyItemInserted(testResults.size - 1)
        }
    }
    
    private suspend fun testGetComments() {
        val testName = "댓글 목록 조회"
        try {
            // 테스트용 postId
            val testPostId = 4
            val response = commentRepository.getComments(testPostId, cursor = null, size = 10)
            
            val result = if (response.isSuccessful) {
                val comments = response.body()
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.SUCCESS,
                    message = "댓글 수: ${comments?.result?.commentPreviewListDTO?.size ?: 0}\n" +
                            "다음 페이지: ${comments?.result?.hasNext}",
                    responseCode = response.code()
                )
            } else {
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.FAILURE,
                    message = response.errorBody()?.string() ?: "Unknown error",
                    responseCode = response.code()
                )
            }
            
            testResults.add(result)
            testAdapter.notifyItemInserted(testResults.size - 1)
        } catch (e: Exception) {
            testResults.add(
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.ERROR,
                    message = e.message ?: "Exception occurred",
                    responseCode = -1
                )
            )
            testAdapter.notifyItemInserted(testResults.size - 1)
        }
    }
    
    private suspend fun testCreateComment() {
        val testName = "댓글 생성"
        try {
            val testPostId = 4
            val request = CommentCreateRequest(
                content = "테스트 댓글 ${System.currentTimeMillis()}",
                parentId = null
            )
            val response = commentRepository.createComment(testPostId, request)
            
            val result = if (response.isSuccessful) {
                val comment = response.body()
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.SUCCESS,
                    message = "댓글 ID: ${comment?.result?.id}\n" +
                            "생성 시간: ${comment?.result?.createdAt}",
                    responseCode = response.code()
                )
            } else {
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.FAILURE,
                    message = response.errorBody()?.string() ?: "Unknown error",
                    responseCode = response.code()
                )
            }
            
            testResults.add(result)
            testAdapter.notifyItemInserted(testResults.size - 1)
        } catch (e: Exception) {
            testResults.add(
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.ERROR,
                    message = e.message ?: "Exception occurred",
                    responseCode = -1
                )
            )
            testAdapter.notifyItemInserted(testResults.size - 1)
        }
    }
    
    private suspend fun testGetCommentDetail() {
        val testName = "댓글 상세 조회"
        try {
            val testPostId = 4
            val testCommentId = 1
            val response = commentRepository.getCommentDetail(testPostId, testCommentId)
            
            val result = if (response.isSuccessful) {
                val comment = response.body()
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.SUCCESS,
                    message = "댓글 내용: ${comment?.result?.content}\n" +
                            "작성자: ${comment?.result?.writerName ?: "Unknown"}",
                    responseCode = response.code()
                )
            } else {
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.FAILURE,
                    message = response.errorBody()?.string() ?: "Unknown error",
                    responseCode = response.code()
                )
            }
            
            testResults.add(result)
            testAdapter.notifyItemInserted(testResults.size - 1)
        } catch (e: Exception) {
            testResults.add(
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.ERROR,
                    message = e.message ?: "Exception occurred",
                    responseCode = -1
                )
            )
            testAdapter.notifyItemInserted(testResults.size - 1)
        }
    }
    
    private suspend fun testUpdateComment() {
        val testName = "댓글 수정"
        try {
            val testPostId = 4
            val testCommentId = 1
            val request = CommentUpdateRequest(
                content = "수정된 댓글 ${System.currentTimeMillis()}"
            )
            val response = commentRepository.updateComment(testPostId, testCommentId, request)
            
            val result = if (response.isSuccessful) {
                val comment = response.body()
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.SUCCESS,
                    message = "댓글 수정 성공\n" +
                            "수정 시간: ${comment?.result?.updatedAt}",
                    responseCode = response.code()
                )
            } else {
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.FAILURE,
                    message = response.errorBody()?.string() ?: "Unknown error",
                    responseCode = response.code()
                )
            }
            
            testResults.add(result)
            testAdapter.notifyItemInserted(testResults.size - 1)
        } catch (e: Exception) {
            testResults.add(
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.ERROR,
                    message = e.message ?: "Exception occurred",
                    responseCode = -1
                )
            )
            testAdapter.notifyItemInserted(testResults.size - 1)
        }
    }
    
    private suspend fun testDeleteComment() {
        val testName = "댓글 삭제"
        try {
            val testPostId = 4
            val testCommentId = 1
            val response = commentRepository.deleteComment(testPostId, testCommentId)
            
            val result = if (response.isSuccessful) {
                val comment = response.body()
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.SUCCESS,
                    message = "댓글 삭제 성공\n" +
                            "삭제 시간: ${comment?.result?.deletedAt}",
                    responseCode = response.code()
                )
            } else {
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.FAILURE,
                    message = response.errorBody()?.string() ?: "Unknown error",
                    responseCode = response.code()
                )
            }
            
            testResults.add(result)
            testAdapter.notifyItemInserted(testResults.size - 1)
        } catch (e: Exception) {
            testResults.add(
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.ERROR,
                    message = e.message ?: "Exception occurred",
                    responseCode = -1
                )
            )
            testAdapter.notifyItemInserted(testResults.size - 1)
        }
    }
    
    private suspend fun testGetPosts() {
        val testName = "게시글 목록 조회"
        try {
            val response = postRepository.getPosts(BoardType.FREE, cursor = null, size = 10)
            
            val result = if (response.isSuccessful) {
                val posts = response.body()
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.SUCCESS,
                    message = "게시글 수: ${posts?.result?.postPreviewDTOList?.size ?: 0}\n" +
                            "다음 페이지: ${posts?.result?.hasNext}",
                    responseCode = response.code()
                )
            } else {
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.FAILURE,
                    message = response.errorBody()?.string() ?: "Unknown error",
                    responseCode = response.code()
                )
            }
            
            testResults.add(result)
            testAdapter.notifyItemInserted(testResults.size - 1)
        } catch (e: Exception) {
            testResults.add(
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.ERROR,
                    message = e.message ?: "Exception occurred",
                    responseCode = -1
                )
            )
            testAdapter.notifyItemInserted(testResults.size - 1)
        }
    }
    
    private suspend fun testGetPostDetail() {
        val testName = "게시글 상세 조회"
        try {
            val testPostId = 19
            val response = postRepository.getPostDetail(BoardType.FREE, testPostId)
            
            val result = if (response.isSuccessful) {
                val post = response.body()
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.SUCCESS,
                    message = "제목: ${post?.result?.title}\n" +
                            "작성자: ${post?.result?.author}\n" +
                            "좋아요: ${post?.result?.likeNum}",
                    responseCode = response.code()
                )
            } else {
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.FAILURE,
                    message = response.errorBody()?.string() ?: "Unknown error",
                    responseCode = response.code()
                )
            }
            
            testResults.add(result)
            testAdapter.notifyItemInserted(testResults.size - 1)
        } catch (e: Exception) {
            testResults.add(
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.ERROR,
                    message = e.message ?: "Exception occurred",
                    responseCode = -1
                )
            )
            testAdapter.notifyItemInserted(testResults.size - 1)
        }
    }
    
    private suspend fun testUpdatePost() {
        val testName = "게시글 수정"
        try {
            val testPostId = 19
            val request = UpdatePostRequest(
                title = "수정된 제목 ${System.currentTimeMillis()}",
                content = "수정된 내용 ${System.currentTimeMillis()}"
            )
            val response = postRepository.updatePost(BoardType.FREE, testPostId, request)
            
            val result = if (response.isSuccessful) {
                val post = response.body()
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.SUCCESS,
                    message = "게시글 수정 성공\n" +
                            "수정 시간: ${post?.result?.updatedAt}",
                    responseCode = response.code()
                )
            } else {
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.FAILURE,
                    message = response.errorBody()?.string() ?: "Unknown error",
                    responseCode = response.code()
                )
            }
            
            testResults.add(result)
            testAdapter.notifyItemInserted(testResults.size - 1)
        } catch (e: Exception) {
            testResults.add(
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.ERROR,
                    message = e.message ?: "Exception occurred",
                    responseCode = -1
                )
            )
            testAdapter.notifyItemInserted(testResults.size - 1)
        }
    }
    
    private suspend fun testDeletePost() {
        val testName = "게시글 삭제"
        try {
            val testPostId = 20
            val response = postRepository.deletePost(BoardType.FREE, testPostId)
            
            val result = if (response.isSuccessful) {
                val post = response.body()
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.SUCCESS,
                    message = "게시글 삭제 성공\n" +
                            "게시글 ID: ${post?.result?.id}",
                    responseCode = response.code()
                )
            } else {
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.FAILURE,
                    message = response.errorBody()?.string() ?: "Unknown error",
                    responseCode = response.code()
                )
            }
            
            testResults.add(result)
            testAdapter.notifyItemInserted(testResults.size - 1)
        } catch (e: Exception) {
            testResults.add(
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.ERROR,
                    message = e.message ?: "Exception occurred",
                    responseCode = -1
                )
            )
            testAdapter.notifyItemInserted(testResults.size - 1)
        }
    }
    
    private suspend fun testLikePost() {
        val testName = "게시글 좋아요"
        try {
            val testPostId = 19
            val response = postRepository.likePost(testPostId)
            
            val result = if (response.isSuccessful) {
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.SUCCESS,
                    message = "좋아요 성공: ${response.body()?.result}",
                    responseCode = response.code()
                )
            } else {
                val errorBody = response.errorBody()?.string()
                ApiTestResult(
                    testName = testName,
                    status = if (errorBody?.contains("LIKE400") == true) TestStatus.SUCCESS else TestStatus.FAILURE,
                    message = errorBody ?: "Unknown error",
                    responseCode = response.code()
                )
            }
            
            testResults.add(result)
            testAdapter.notifyItemInserted(testResults.size - 1)
        } catch (e: Exception) {
            testResults.add(
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.ERROR,
                    message = e.message ?: "Exception occurred",
                    responseCode = -1
                )
            )
            testAdapter.notifyItemInserted(testResults.size - 1)
        }
    }
    
    private suspend fun testUnlikePost() {
        val testName = "게시글 좋아요 취소"
        try {
            val testPostId = 19
            val response = postRepository.unlikePost(testPostId)
            
            val result = if (response.isSuccessful) {
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.SUCCESS,
                    message = "좋아요 취소: ${response.body()?.result}",
                    responseCode = response.code()
                )
            } else {
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.FAILURE,
                    message = response.errorBody()?.string() ?: "Unknown error",
                    responseCode = response.code()
                )
            }
            
            testResults.add(result)
            testAdapter.notifyItemInserted(testResults.size - 1)
        } catch (e: Exception) {
            testResults.add(
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.ERROR,
                    message = e.message ?: "Exception occurred",
                    responseCode = -1
                )
            )
            testAdapter.notifyItemInserted(testResults.size - 1)
        }
    }
    
    private suspend fun testScrapPost() {
        val testName = "게시글 스크랩"
        try {
            val testPostId = 19
            val response = postRepository.scrapPost(testPostId)
            
            val result = if (response.isSuccessful) {
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.SUCCESS,
                    message = "스크랩 성공: ${response.body()?.result}",
                    responseCode = response.code()
                )
            } else {
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.FAILURE,
                    message = response.errorBody()?.string() ?: "Unknown error",
                    responseCode = response.code()
                )
            }
            
            testResults.add(result)
            testAdapter.notifyItemInserted(testResults.size - 1)
        } catch (e: Exception) {
            testResults.add(
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.ERROR,
                    message = e.message ?: "Exception occurred",
                    responseCode = -1
                )
            )
            testAdapter.notifyItemInserted(testResults.size - 1)
        }
    }
    
    private suspend fun testUnscrapPost() {
        val testName = "게시글 스크랩 취소"
        try {
            val testPostId = 19
            val response = postRepository.unscrapPost(testPostId)
            
            val result = if (response.isSuccessful) {
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.SUCCESS,
                    message = "스크랩 취소: ${response.body()?.result}",
                    responseCode = response.code()
                )
            } else {
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.FAILURE,
                    message = response.errorBody()?.string() ?: "Unknown error",
                    responseCode = response.code()
                )
            }
            
            testResults.add(result)
            testAdapter.notifyItemInserted(testResults.size - 1)
        } catch (e: Exception) {
            testResults.add(
                ApiTestResult(
                    testName = testName,
                    status = TestStatus.ERROR,
                    message = e.message ?: "Exception occurred",
                    responseCode = -1
                )
            )
            testAdapter.notifyItemInserted(testResults.size - 1)
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}