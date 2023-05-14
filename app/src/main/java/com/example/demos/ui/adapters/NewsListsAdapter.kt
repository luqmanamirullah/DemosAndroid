package com.example.demos.ui.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.demos.databinding.ItemNewsListBinding
import com.example.demos.databinding.ItemNewsListSkeletonBinding
import com.example.demos.models.news.News
import com.example.demos.ui.ArticleActivity
import com.example.demos.utils.Constants

class NewsListsAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class NewsListsViewHolder(val binding: ItemNewsListBinding): RecyclerView.ViewHolder(binding.root)

    inner class SkeletonNewsListsViewHolder(val binding: ItemNewsListSkeletonBinding): RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<News>(){
        override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            Constants.LOADING_VIEW_TYPE -> {
                val binding =
                    ItemNewsListSkeletonBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )

                SkeletonNewsListsViewHolder(binding)
            }

            Constants.DONE_VIEW_TYPE -> {
                val binding =
                    ItemNewsListBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )

                NewsListsViewHolder(binding)
            }
            else -> throw  IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (differ.currentList.isEmpty()) {
            Constants.LOADING_VIEW_TYPE
        } else {
            Constants.DONE_VIEW_TYPE
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is NewsListsViewHolder -> {
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
            is SkeletonNewsListsViewHolder -> {
                holder.binding.root.visibility = if (differ.currentList.isEmpty() && position == 0) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }

        }
    }

    override fun getItemCount(): Int = if (differ.currentList.isEmpty()){
        1
    } else {
        differ.currentList.size
    }

}