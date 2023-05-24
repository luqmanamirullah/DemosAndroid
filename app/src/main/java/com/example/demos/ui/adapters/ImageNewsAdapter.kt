package com.example.demos.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.demos.databinding.ImageContainerBinding
import com.example.demos.models.newsFromInternet.Article
import java.util.Objects

class ImageNewsAdapter(private val viewPager2: ViewPager2):
    RecyclerView.Adapter<ImageNewsAdapter.ImageViewHolder>(){

    private var itemCount: Int = 0
    inner class ImageViewHolder(val binding: ImageContainerBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ImageContainerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ImageViewHolder(binding)
    }
    private val diffCall = object : DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCall)

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val highlight = differ.currentList[position]
        holder.binding.apply {
            txtTitle.text = highlight.category.indexOfFirst { true }.toString()
            txtDescripton.text = highlight.title
            Glide.with(holder.itemView).load(highlight.image_url).into(ivPicture)
        }
        if (position == differ.currentList.size - 1){
            viewPager2.post{
                viewPager2.setCurrentItem(0, false)
            }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

}