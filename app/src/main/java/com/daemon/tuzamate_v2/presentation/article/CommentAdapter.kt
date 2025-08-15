package com.daemon.tuzamate_v2.presentation.article

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.daemon.tuzamate_v2.R
import com.daemon.tuzamate_v2.databinding.ItemCommentBinding

class CommentAdapter(
    private val onLikeClick: (CommentUIModel) -> Unit,
    private val onReplyClick: (CommentUIModel) -> Unit
) : ListAdapter<CommentUIModel, CommentAdapter.CommentViewHolder>(CommentDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding = ItemCommentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CommentViewHolder(binding, onLikeClick, onReplyClick)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CommentViewHolder(
        private val binding: ItemCommentBinding,
        private val onLikeClick: (CommentUIModel) -> Unit,
        private val onReplyClick: (CommentUIModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(comment: CommentUIModel) {
            binding.apply {
                tvNickname.text = comment.nickname
                tvContent.text = comment.content
                tvDate.text = comment.createdAt
                tvLikeCount.text = comment.likeCount.toString()
                
                if (comment.isLiked) {
                    ivLike.setImageResource(R.drawable.ic_like_filled)
                } else {
                    ivLike.setImageResource(R.drawable.ic_like_empty)
                }
                
                if (comment.replyCount > 0) {
                    replyCountContainer.visibility = View.VISIBLE
                    tvReplyCount.text = comment.replyCount.toString()
                } else {
                    replyCountContainer.visibility = View.GONE
                }
                
                btnLike.setOnClickListener {
                    onLikeClick(comment)
                }
                
                btnReply.setOnClickListener {
                    onReplyClick(comment)
                }
            }
        }
    }

    class CommentDiffCallback : DiffUtil.ItemCallback<CommentUIModel>() {
        override fun areItemsTheSame(oldItem: CommentUIModel, newItem: CommentUIModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CommentUIModel, newItem: CommentUIModel): Boolean {
            return oldItem == newItem
        }
    }
}