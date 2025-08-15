package com.daemon.tuzamate_v2.presentation.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.daemon.tuzamate_v2.databinding.BottomSheetCommentBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CommentBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetCommentBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var commentAdapter: CommentAdapter
    private var postId: Int = 0
    
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
        loadComments()
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
                sendComment(commentText)
                binding.etComment.text.clear()
            }
        }
    }
    
    private fun loadComments() {
        val sampleComments = listOf(
            CommentUIModel(
                id = 1,
                nickname = "사용자1",
                content = "좋은 게시글이네요!",
                likeCount = 5,
                replyCount = 2,
                createdAt = "5분 전",
                isLiked = false
            ),
            CommentUIModel(
                id = 2,
                nickname = "사용자2",
                content = "유익한 정보 감사합니다. 많은 도움이 되었어요!",
                likeCount = 3,
                replyCount = 0,
                createdAt = "10분 전",
                isLiked = true
            ),
            CommentUIModel(
                id = 3,
                nickname = "사용자3",
                content = "더 자세한 설명이 필요할 것 같아요",
                likeCount = 1,
                replyCount = 1,
                createdAt = "30분 전",
                isLiked = false
            )
        )
        
        commentAdapter.submitList(sampleComments)
        updateCommentCount(sampleComments.size)
        updateEmptyView(sampleComments.isEmpty())
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
        binding.etComment.hint = "${comment.nickname}님께 답글 작성 중..."
        binding.etComment.requestFocus()
    }
    
    private fun sendComment(content: String) {
        // TODO: API 호출
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