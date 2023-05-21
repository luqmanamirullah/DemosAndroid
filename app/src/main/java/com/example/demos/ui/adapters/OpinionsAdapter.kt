package com.example.demos.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.demos.R
import com.example.demos.databinding.ItemPolicyOpinionSkeletonBinding
import com.example.demos.databinding.ItemPolicyOpinionsBinding
import com.example.demos.models.opinion.Opinion
import com.example.demos.utils.Constants

class OpinionsAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class OpinionViewHolder(val binding: ItemPolicyOpinionsBinding):
        RecyclerView.ViewHolder(binding.root)

    inner class SkeletonOpinionViewHolder(val binding: ItemPolicyOpinionSkeletonBinding):
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            Constants.DONE_VIEW_TYPE -> {
                val binding =
                    ItemPolicyOpinionsBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                OpinionViewHolder(binding)
            }
            Constants.LOADING_VIEW_TYPE -> {
                val binding =
                    ItemPolicyOpinionSkeletonBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                SkeletonOpinionViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    private val diffCall = object : DiffUtil.ItemCallback<Opinion>(){
        override fun areItemsTheSame(oldItem: Opinion, newItem: Opinion): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Opinion, newItem: Opinion): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCall)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is OpinionViewHolder -> {
                val opinion = differ.currentList[position]
                opinion?.let { res ->
                    holder.binding.apply {
                        tvName.text = res.user_name
                        if (res.user_verify == 1) {
                            tvName.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                0,
                                0,
                                R.drawable.baseline_verified_24,
                                0
                            )
                        }
                        if (res.user_bio !== null) {
                            tvBio.visibility = View.VISIBLE
                            tvBio.text = res.user_bio
                        } else {
                            tvBio.visibility = View.GONE
                        }
                        tvOpini.text = res.opinion_content
                    }
                    Glide.with(holder.itemView).load(opinion.user_photo)
                        .into(holder.binding.ivProfile)
                }
            }
            is SkeletonOpinionViewHolder -> {
                holder.binding.root.visibility = if (differ.currentList.isEmpty() && position == 0) {
                    View.VISIBLE
                } else {
                    View.GONE
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
        return if (differ.currentList.isEmpty()){
            Constants.LOADING_VIEW_TYPE
        } else {
            Constants.DONE_VIEW_TYPE
        }
    }


}