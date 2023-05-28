package com.example.demos.ui.adapters

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.Navigation
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.demos.R
import com.example.demos.databinding.ItemLatestNewsBinding
import com.example.demos.databinding.ItemNewsListBinding
import com.example.demos.databinding.ItemNewsListSkeletonBinding
import com.example.demos.models.news.News
import com.example.demos.models.newsFromInternet.Article
import com.example.demos.ui.ArticleActivity
import com.example.demos.utils.Constants
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class NewsListsAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class NewsListsViewHolder(val binding: ItemLatestNewsBinding): RecyclerView.ViewHolder(binding.root)

    inner class SkeletonNewsListsViewHolder(val binding: ItemNewsListSkeletonBinding): RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
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
                    ItemLatestNewsBinding.inflate(
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is NewsListsViewHolder -> {
                val news = differ.currentList[position]
                holder.apply {
                    Glide.with(itemView).load(news.urlToImage).into(binding.ivPicture)
                    binding.apply {

                        tvTitle.text = news.title
                        tvDate.text = dateFormatter(news.publishedAt)

                        root.setOnClickListener {
                            val bundle = Bundle().apply {
                                putSerializable("article", news)
                            }

                            val navController = Navigation.findNavController(holder.itemView)
                            val currentDestination = navController.currentDestination

                            if (currentDestination?.id == R.id.saveNewsFragment2) {
                                navController.navigate(
                                    R.id.action_saveNewsFragment2_to_articleFragment,
                                    bundle
                                )
                            } else {
                                navController.navigate(
                                    R.id.action_newsFragment_to_articleFragment,
                                    bundle
                                )
                            }
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
        3
    } else {
        differ.currentList.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun dateFormatter(date: String): String? {
        val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("id"))
        val dateTime = LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME)
        val formatted = dateTime.format(formatter)

        return formatted
    }

}