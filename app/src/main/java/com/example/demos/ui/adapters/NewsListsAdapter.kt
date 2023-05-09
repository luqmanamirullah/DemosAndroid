package com.example.demos.ui.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.demos.databinding.ItemNewsListBinding
import com.example.demos.models.news.News
import com.example.demos.ui.ArticleActivity

class NewsListsAdapter: RecyclerView.Adapter<NewsListsAdapter.NewsListsViewHolder>() {
    inner class NewsListsViewHolder(val binding: ItemNewsListBinding): RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<News>(){
        override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsListsViewHolder {
        return NewsListsViewHolder(ItemNewsListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: NewsListsViewHolder, position: Int) {
        val news = differ.currentList[position]
        holder.apply {
            Glide.with(itemView).load(news.image_url).into(binding.ivPicture)
            binding.apply {
                tvTitle.text = news.title
                tvPreText.text = news.content
                tvCreatedAt.text = news.created_at

                root.setOnClickListener {
                    val intent = Intent(itemView.context, ArticleActivity::class.java)
                    intent.putExtra("news_id", news.id)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

}