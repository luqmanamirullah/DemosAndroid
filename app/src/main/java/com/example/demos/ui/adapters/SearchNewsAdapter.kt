package com.example.demos.ui.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.demos.R
import com.example.demos.databinding.ImageContainer2Binding
import com.example.demos.models.newsFromInternet.Article

class SearchNewsAdapter : RecyclerView.Adapter<SearchNewsAdapter.SearchNewsViewHolder>() {

    inner class SearchNewsViewHolder(val binding: ImageContainer2Binding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchNewsViewHolder {
        val binding = ImageContainer2Binding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SearchNewsViewHolder(binding)
    }

    private val diffCall = object : DiffUtil.ItemCallback<Article>() {

        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCall)

    override fun onBindViewHolder(holder: SearchNewsViewHolder, position: Int) {
        val highlight = differ.currentList[position]
        holder.binding.apply {
            tvTitle.text = highlight.title
            tvDescripton.text = highlight.description
            Glide.with(holder.itemView).load(highlight.urlToImage).into(ivImage)
            root.setOnClickListener {
                val bundle = Bundle().apply {
                    putSerializable("article", highlight)
                }

                Navigation.findNavController(holder.itemView).navigate(
                    R.id.action_searchNewsFragment_to_articleFragment,
                    bundle
                )
            }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size
}