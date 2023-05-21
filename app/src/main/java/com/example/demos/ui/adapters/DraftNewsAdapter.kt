package com.example.demos.ui.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.demos.R
import com.example.demos.databinding.ItemDraftNewsBinding
import com.example.demos.databinding.ItemDraftNewsSkeletonBinding
import com.example.demos.ui.viewmodels.HomeViewModel
import com.example.demos.utils.Constants


class DraftNewsAdapter(private val viewModel: HomeViewModel): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var selectedPosition: Int = 0
    private val diffCallback = object : DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    var differ = AsyncListDiffer(this, diffCallback)
    @SuppressLint("NotifyDataSetChanged")
    inner class DraftNewsViewHolder(val binding: ItemDraftNewsBinding): RecyclerView.ViewHolder(binding.root)
    inner class SkeletonDraftNewsViewHolder(val binding: ItemDraftNewsSkeletonBinding): RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            Constants.LOADING_VIEW_TYPE -> {
                val binding =
                    ItemDraftNewsSkeletonBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                SkeletonDraftNewsViewHolder(binding)
            }
            Constants.DONE_VIEW_TYPE -> {
                val binding =
                    ItemDraftNewsBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                DraftNewsViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
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
            is DraftNewsViewHolder -> {
                val type = differ.currentList[position]
                holder.binding.apply {
                    tvDraftName.text = type
                    tvDraftName.setOnClickListener {
                        if(position != RecyclerView.NO_POSITION){
                            selectedPosition = position
                            notifyDataSetChanged()
                            val selectedType = differ.currentList[position]
                            if (selectedType == "All" ){
                                viewModel.getNews(null)
                            } else {
                                viewModel.getNews(selectedType)
                            }
                        }
                    }
                    tvDraftName.isSelected = position == selectedPosition
                    tvDraftName.setTextColor(
                        if (tvDraftName.isSelected){
                            ContextCompat.getColor(root.context, R.color.white)
                        } else {
                            ContextCompat.getColor(root.context, R.color.gray_500)
                        }
                    )

                    root.setOnClickListener {
                        if(position != RecyclerView.NO_POSITION){
                            selectedPosition = position
                            notifyDataSetChanged()
                            val selectedType = differ.currentList[position]
                            if (selectedType == "All" ){
                                viewModel.getNews(null)
                            } else {
                                viewModel.getNews(selectedType)
                            }
                        }
                    }
                    root.isSelected = position == selectedPosition
                }
            }
            is SkeletonDraftNewsViewHolder -> {
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

}