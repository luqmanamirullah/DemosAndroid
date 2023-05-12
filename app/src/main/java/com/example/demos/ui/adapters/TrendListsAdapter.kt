package com.example.demos.ui.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.demos.databinding.ItemTrendNewsBinding
import com.example.demos.models.trending.Trend
import com.example.demos.ui.ArticleActivity

class TrendListsAdapter: RecyclerView.Adapter<TrendListsAdapter.TrendsListViewHolder>() {
    inner class TrendsListViewHolder(val binding: ItemTrendNewsBinding): RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Trend>(){
        override fun areItemsTheSame(oldItem: Trend, newItem: Trend): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Trend, newItem: Trend): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var trendLists: List<Trend>
        get() = differ.currentList
        set(value) {differ.submitList(value)}

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrendListsAdapter.TrendsListViewHolder {
       return TrendsListViewHolder(ItemTrendNewsBinding.inflate(
           LayoutInflater.from(parent.context),
           parent,
           false
       ))
    }

    override fun onBindViewHolder(holder: TrendListsAdapter.TrendsListViewHolder, position: Int) {
        holder.apply {
            val trend = trendLists[position]
            binding.apply {
                tvType.text = trend.type
                tvTagline.text = trend.title
                tvViews.text = trend.view.toString()

                root.setOnClickListener {
                    val intent = Intent(itemView.context, ArticleActivity::class.java)
                    intent.putExtra("news_id", trend.id)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun getItemCount(): Int = trendLists.size
}