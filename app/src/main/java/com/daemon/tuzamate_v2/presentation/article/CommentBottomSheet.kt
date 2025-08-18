package com.daemon.tuzamate_v2.presentation.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.daemon.tuzamate_v2.databinding.BottomSheetCommentBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class CommentBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetCommentBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: CommentViewModel by viewModels()
    private lateinit var commentAdapter: CommentAdapter
    private var postId: Int = 0
    private var replyToCommentId: Int? = null
    
    companion object {
        fun newInstance(postId: Int): CommentBottomSheet {
            return CommentBottomSheet().apply {
                arguments = Bundle().apply {
                    putInt("postId", postId)
                }
            }
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postId = arguments?.getInt("postId") ?: 0
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetCommentBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupBottomSheet()
        setupRecyclerView()
        setupInputField()
        observeViewModel()
        
        viewModel.setPostId(postId)
    }
    
    private fun setupBottomSheet() {
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        
        dialog?.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as? com.google.android.material.bottomsheet.BottomSheetDialog
            val bottomSheet = bottomSheetDialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let { sheet ->
                val behavior = BottomSheetBehavior.from(sheet)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.skipCollapsed = true
                behavior.isDraggable = true
                
                val layoutParams = sheet.layoutParams
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                sheet.layoutParams = layoutParams
            }
        }
    }
    
    private fun setupRecyclerView() {
        commentAdapter = CommentAdapter(
            onLikeClick = { comment ->
                handleLikeClick(comment)
            },
            onReplyClick = { comment ->
                handleReplyClick(comment)
            }
        )
        
        binding.rvComments.apply {
            adapter = commentAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
    
    private fun setupInputField() {
        binding.btnSend.setOnClickListener {
            val commentText = binding.etComment.text.toString().trim()
            if (commentText.isNotEmpty()) {
                viewModel.createComment(commentText, replyToCommentId)
                binding.etComment.text.clear()
                binding.etComment.hint = "댓글을 입력하세요..."
                replyToCommentId = null
            }
        }
    }
    
    private fun observeViewModel() {
        viewModel.comments.observe(viewLifecycleOwner) { comments ->
            val uiModels = comments.map { comment ->
                CommentUIModel(
                    id = comment.id,
                    nickname = comment.writerName ?: "익명",
                    content = comment.content,
                    likeCount = 0,
                    replyCount = 0,
                    createdAt = getRelativeTimeSpan(comment.createdAt),
                    isLiked = false
                )
            }
            commentAdapter.submitList(uiModels)
            updateCommentCount(uiModels.size)
            updateEmptyView(uiModels.isEmpty())
            
            // 새 댓글이 추가되면 RecyclerView를 맨 위로 스크롤
            if (uiModels.isNotEmpty()) {
                binding.rvComments.scrollToPosition(0)
            }
        }
        
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        
        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                viewModel.clearError()
            }
        }
        
        viewModel.commentCreated.observe(viewLifecycleOwner) { created ->
            if (created) {
                // 키보드 숨기기
                binding.etComment.clearFocus()
                viewModel.resetCommentCreated()
            }
        }
    }
    
    private fun getRelativeTimeSpan(dateTimeString: String): String {
        return try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val date = dateFormat.parse(dateTimeString) ?: return dateTimeString
            
            val now = Date()
            val diffInMillis = now.time - date.time
            
            val minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis)
            val hours = TimeUnit.MILLISECONDS.toHours(diffInMillis)
            val days = TimeUnit.MILLISECONDS.toDays(diffInMillis)
            
            when {
                minutes < 1 -> "방금 전"
                minutes < 60 -> "${minutes}분 전"
                hours < 24 -> "${hours}시간 전"
                days < 30 -> "${days}일 전"
                days < 365 -> "${days / 30}개월 전"
                else -> "${days / 365}년 전"
            }
        } catch (e: Exception) {
            dateTimeString
        }
    }
    
    private fun updateCommentCount(count: Int) {
        binding.tvCommentCount.text = count.toString()
    }
    
    private fun updateEmptyView(isEmpty: Boolean) {
        binding.tvEmpty.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.rvComments.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }
    
    private fun handleLikeClick(comment: CommentUIModel) {
        // TODO: API 호출
    }
    
    private fun handleReplyClick(comment: CommentUIModel) {
        replyToCommentId = comment.id
        binding.etComment.hint = "${comment.nickname}님께 답글 작성 중..."
        binding.etComment.requestFocus()
    }
    
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

data class CommentUIModel(
    val id: Int,
    val nickname: String,
    val content: String,
    val likeCount: Int,
    val replyCount: Int,
    val createdAt: String,
    val isLiked: Boolean
)