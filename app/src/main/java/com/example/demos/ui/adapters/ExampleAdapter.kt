package com.example.demos.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.demos.databinding.ItemExampleBinding
import com.example.demos.models.Example

class ExampleAdapter : RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder>() {
    inner class ExampleViewHolder(val binding: ItemExampleBinding): RecyclerView.ViewHolder(binding.root) {

    }

    private val diffCallback = object : DiffUtil.ItemCallback<Example>(){
        override fun areItemsTheSame(oldItem: Example, newItem: Example): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Example, newItem: Example): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var examples: List<Example>
        get() = differ.currentList
        set(value) {differ.submitList(value)}

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExampleAdapter.ExampleViewHolder {
        return ExampleViewHolder(ItemExampleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: ExampleAdapter.ExampleViewHolder, position: Int) {
        holder.binding.apply {
            val example =  examples[position]
            tvTitle.text = example.title
        }
    }

    override fun getItemCount(): Int = examples.size
}