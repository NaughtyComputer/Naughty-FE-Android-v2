package com.daemon.tuzamate_v2.presentation.contents

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.daemon.tuzamate_v2.databinding.ItemNewsletterBinding

class NewsLetterAdapter(
    private val onItemClick: (NewsLetter) -> Unit
) : ListAdapter<NewsLetter, NewsLetterAdapter.NewsLetterViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsLetterViewHolder {
        val binding = ItemNewsletterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NewsLetterViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: NewsLetterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class NewsLetterViewHolder(
        private val binding: ItemNewsletterBinding,
        private val onItemClick: (NewsLetter) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(newsLetter: NewsLetter) {
            binding.apply {

                // 클릭 이벤트
                root.setOnClickListener {
                    onItemClick(newsLetter)
                }
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<NewsLetter>() {
        override fun areItemsTheSame(oldItem: NewsLetter, newItem: NewsLetter): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NewsLetter, newItem: NewsLetter): Boolean {
            return oldItem == newItem
        }
    }
}