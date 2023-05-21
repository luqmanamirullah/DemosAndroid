package com.example.demos.ui.adapters

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.demos.databinding.ItemPolicyListsBinding
import com.example.demos.databinding.ItemPolicyListsSkeletonBinding
import com.example.demos.models.policy.Policy
import com.example.demos.ui.PolicyActivity
import com.example.demos.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class PolicyListsAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class PoliciesViewHolder(val binding: ItemPolicyListsBinding):
        RecyclerView.ViewHolder(binding.root)

    inner class SkeletonViewHolder(val binding: ItemPolicyListsSkeletonBinding):
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            Constants.LOADING_VIEW_TYPE -> {
                val binding =
                    ItemPolicyListsSkeletonBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                SkeletonViewHolder(binding)
            }
            Constants.DONE_VIEW_TYPE -> {
                val binding =
                    ItemPolicyListsBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                PoliciesViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Policy>(){
        override fun areItemsTheSame(oldItem: Policy, newItem: Policy): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Policy, newItem: Policy): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is PoliciesViewHolder -> {
                val policy = differ.currentList[position]

                policy?.let {
                    val context = holder.itemView.context
                    holder.binding.apply {
                        tvTitle.text = policy.policy_title
                        tvEntity.text = policy.policy_source
                        tvTheme.text = policy.policy_theme
                        tvOpinionCount.text = policy.opinion_count

                        root.setOnClickListener {
                            val intent = Intent(holder.itemView.context, PolicyActivity::class.java)
                            intent.putExtra("policy_id", policy.policy_id)
                            intent.putExtra("policy_source", policy.policy_source)
                            intent.putExtra("policy_title", policy.policy_title)
                            intent.putExtra("policy_theme", policy.policy_theme)
                            holder.itemView.context.startActivity(intent)
                        }
                    }
                    // Perform network operation and rendering using Kotlin coroutine
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            // Get the PDF cover
                            val pdfUrl = policy.policy_file // Replace with the URL of the PDF file
                            val pdfInputStream =
                                withContext(Dispatchers.IO) {
                                    URL(pdfUrl).openStream()
                                }
                            val file = File(context.cacheDir, "cover_${position}.pdf") // Store the PDF file in the cache directory
                            withContext(Dispatchers.IO) {
                                FileOutputStream(file).use { output ->
                                    pdfInputStream.copyTo(output)
                                }
                            }

                            // Continue with the rest of the code after the network operation
                            withContext(Dispatchers.Main) {
                                val parcelFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
                                val pdfRenderer = PdfRenderer(parcelFileDescriptor)

                                // Display the first page of the PDF using Glide on the main thread
                                withContext(Dispatchers.Main) {
                                    val coverPage = pdfRenderer.openPage(0)
                                    val bitmap = Bitmap.createBitmap(coverPage.width, coverPage.height, Bitmap.Config.ARGB_8888)
                                    coverPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                                    coverPage.close()
                                    pdfRenderer.close()
                                    parcelFileDescriptor.close()

                                    Glide.with(context).load(bitmap).into(holder.binding.ivPicture)
                                }
                            }
                        } catch (e: Exception) {
                            // Handle any exceptions that occurred during the network operation
                            e.printStackTrace()
                        }
                    }
                }
            }
            is SkeletonViewHolder -> {
                holder.binding.root.visibility = if(differ.currentList.isEmpty()){
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return if (differ.currentList.isEmpty()){
            3
        } else {
            differ.currentList.size
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (differ.currentList.isEmpty()){
            Constants.LOADING_VIEW_TYPE
        } else{
            Constants.DONE_VIEW_TYPE
        }
    }
}