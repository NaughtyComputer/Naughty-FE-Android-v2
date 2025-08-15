package com.daemon.tuzamate_v2.presentation.contents

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.daemon.tuzamate_v2.R
import com.daemon.tuzamate_v2.data.model.PostPreview
import com.daemon.tuzamate_v2.databinding.ItemNewsletterBinding

class PlazaAdapter(
    private val onItemClick: (PostPreview) -> Unit,
    private val onLikeClick: (PostPreview) -> Unit
) : ListAdapter<PostPreview, PlazaAdapter.PlazaViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlazaViewHolder {
        val binding = ItemNewsletterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlazaViewHolder(binding, onItemClick, onLikeClick)
    }

    override fun onBindViewHolder(holder: PlazaViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PlazaViewHolder(
        private val binding: ItemNewsletterBinding,
        private val onItemClick: (PostPreview) -> Unit,
        private val onLikeClick: (PostPreview) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(post: PostPreview) {
            binding.apply {
                // Plaza에서는 크레딧 박스 숨김
                creditBox.visibility = View.INVISIBLE
                
                thumbnail.setImageResource(R.drawable.dummy)
                
                tvTitle.text = post.title
                
                // 좋아요, 댓글 수 표시
                tvLikeCount.text = post.likeNum.toString()
                tvCommentCount.text = post.commentNum.toString()
                
                // 클릭 이벤트
                root.setOnClickListener {
                    onItemClick(post)
                }
                
                likeBox.setOnClickListener {
                    onLikeClick(post)
                }
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<PostPreview>() {
        override fun areItemsTheSame(oldItem: PostPreview, newItem: PostPreview): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PostPreview, newItem: PostPreview): Boolean {
            return oldItem == newItem
        }
    }
}