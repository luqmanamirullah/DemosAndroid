package com.example.demos.ui.adapters

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.demos.R
import com.example.demos.databinding.ImageContainerBinding
import com.example.demos.models.newsFromInternet.Article
import com.example.demos.ui.PdfWebViewActivity

class ImageNewsAdapter(private val viewPager2: ViewPager2):
    RecyclerView.Adapter<ImageNewsAdapter.ImageViewHolder>(){
    private lateinit var imageDefault: ArrayList<Int>


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
            txtTitle.text = highlight.source.name
            txtDescripton.text = highlight.title
            Glide.with(holder.itemView).load(highlight.urlToImage).into(ivPicture)

            root.setOnClickListener {
                val bundle = Bundle().apply {
                    putSerializable("article", highlight)
                }

                val navController = Navigation.findNavController(holder.itemView)
                navController.navigate(
                    R.id.action_newsFragment_to_articleFragment,
                    bundle
                )
            }
        }

        val loopRunnable = Runnable {
            val itemCount = differ.currentList.size
            if (position == itemCount - 1) {
                viewPager2.setCurrentItem(0, false)
            }
        }

        viewPager2.post(loopRunnable)
        viewPager2.removeCallbacks(loopRunnable)
    }


    override fun getItemCount(): Int = differ.currentList.size

}