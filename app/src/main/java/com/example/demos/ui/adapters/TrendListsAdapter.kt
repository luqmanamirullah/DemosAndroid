package com.example.demos.ui.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.demos.databinding.ItemTrendNewsBinding
import com.example.demos.databinding.ItemTrendNewsSkeletonBinding
import com.example.demos.models.trending.Trend
import com.example.demos.ui.ArticleActivity
import com.example.demos.utils.Constants
import java.util.Timer
import java.util.TimerTask

class TrendListsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    inner class TrendsListViewHolder(val binding: ItemTrendNewsBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class SkeletonViewHolder(val binding: ItemTrendNewsSkeletonBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            Constants.LOADING_VIEW_TYPE -> {
                val binding =
                    ItemTrendNewsSkeletonBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                Log.e("Loading", "Is Loading")
                SkeletonViewHolder(binding)
            }
            Constants.DONE_VIEW_TYPE -> {
                val binding =
                    ItemTrendNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                Log.e("Done", "Is Done")
                TrendsListViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

        private val diffCallback = object : DiffUtil.ItemCallback<Trend>() {
            override fun areItemsTheSame(oldItem: Trend, newItem: Trend): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Trend, newItem: Trend): Boolean {
                return oldItem == newItem
            }
        }

    val differ = AsyncListDiffer(this, diffCallback)


    override fun getItemCount(): Int = if (differ.currentList.isEmpty()){
        1
    } else{
        differ.currentList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (differ.currentList.isEmpty()) {
            Log.e("the possiton now is", "${position}")
            Constants.LOADING_VIEW_TYPE
        } else {
            Log.e("the possiton now is", "${position}")
            Constants.DONE_VIEW_TYPE
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TrendsListViewHolder -> {
                val trend = differ.currentList.getOrNull(position)
                trend?.let {
                    holder.binding.apply {
                        tvType.text = trend.type
                        tvTagline.text = trend.title
                        tvViews.text = trend.view.toString()

                        root.setOnClickListener {
                            val intent = Intent(holder.itemView.context, ArticleActivity::class.java)
                            intent.putExtra("news_id", trend.id)
                            holder.itemView.context.startActivity(intent)
                        }
                    }
                    holder.binding.root.visibility = View.VISIBLE
                } ?: run {
                    holder.binding.root.visibility = View.GONE
                }
            }
            is SkeletonViewHolder -> {
                holder.binding.root.visibility = if (differ.currentList.isEmpty() && position == 0) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        }
    }

}