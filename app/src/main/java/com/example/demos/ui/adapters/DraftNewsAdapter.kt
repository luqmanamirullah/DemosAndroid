package com.example.demos.ui.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.demos.R
import com.example.demos.databinding.ItemDraftNewsBinding
import com.example.demos.ui.viewmodels.HomeViewModel
import kotlin.properties.Delegates

class DraftNewsAdapter(private val viewModel: HomeViewModel): RecyclerView.Adapter<DraftNewsAdapter.DraftNewsViewHolder>() {
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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DraftNewsViewHolder {
        return DraftNewsViewHolder(ItemDraftNewsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: DraftNewsViewHolder, position: Int) {
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

    override fun getItemCount(): Int = differ.currentList.size
}