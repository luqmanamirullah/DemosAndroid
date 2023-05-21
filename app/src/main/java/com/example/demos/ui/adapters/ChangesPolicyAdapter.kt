package com.example.demos.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.demos.databinding.ItemPolicyDetailsBinding
import com.example.demos.databinding.ItemPolicyDetailsSkeletonBinding
import com.example.demos.utils.Constants

class ChangesPolicyAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class DetailsPolicyViewHolder(val binding: ItemPolicyDetailsBinding):
        RecyclerView.ViewHolder(binding.root)

    inner class SkeletonViewHolder(val binding: ItemPolicyDetailsSkeletonBinding):
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            Constants.DONE_VIEW_TYPE -> {
                val binding =
                    ItemPolicyDetailsBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                DetailsPolicyViewHolder(binding)
            }
            Constants.LOADING_VIEW_TYPE -> {
                val binding =
                    ItemPolicyDetailsSkeletonBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                SkeletonViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Any>(){
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return oldItem == newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is DetailsPolicyViewHolder -> {
                val changes = differ.currentList[position]
                changes?.let {
                    holder.binding.apply {
                        val bulletPoint: Int = position + 97 // ASCII value of 'a'
                        val bulletText = bulletPoint.toChar().toString()
                        tvBullet.text = "$bulletText)"
                        tvItemText.text = changes.toString()
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return if (differ.currentList.isEmpty()){
            1
        } else {
            differ.currentList.size
        }
    }

    override fun getItemViewType(position: Int): Int {
        return Constants.DONE_VIEW_TYPE
    }
}