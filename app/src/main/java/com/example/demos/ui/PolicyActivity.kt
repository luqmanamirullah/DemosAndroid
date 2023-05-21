package com.example.demos.ui

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.demos.R
import com.example.demos.databinding.ActivityPolicyBinding
import com.example.demos.repository.PolicyRepository
import com.example.demos.ui.adapters.ViewPagerAdapter
import com.example.demos.ui.viewmodels.PolicyViewModel
import com.example.demos.ui.viewmodels.PolicyViewModelProviderFactory
import com.example.demos.utils.Resource
import com.example.demos.utils.SessionManager
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class PolicyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPolicyBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    lateinit var policyViewModel: PolicyViewModel
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPolicyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this

        val policyRepository = PolicyRepository()
        policyViewModel = ViewModelProvider(
            this,
            PolicyViewModelProviderFactory(policyRepository)
        )[PolicyViewModel::class.java]

        val id = intent.getIntExtra("policy_id", -1)
        val title = intent.getStringExtra("policy_title")
        val source = intent.getStringExtra("policy_source")
        val theme = intent.getStringExtra("policy_theme")

        SessionManager.getToken(this)?.let {
            lifecycleScope.launch {
                policyViewModel.getPolicyFile(id, it)
            }
        }

        policyViewModel.file.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    binding.shimmerContainer.stopShimmer()
                    binding.shimmerContainer.visibility = View.GONE
                    binding.container.visibility = View.VISIBLE
                    val file = response.data?.data
                    response.data?.let {
                        binding.apply {
                            tvTitle.text = title
                            tvEntity.text = source
                            tvTheme.text = theme
                            container.setOnClickListener {
                                val intent = Intent(context, PdfWebViewActivity::class.java)
                                intent.putExtra("policy_file", file)
                                startActivity(intent)
                            }
                            ivPicture.setOnClickListener {
                                val intent = Intent(context, PdfWebViewActivity::class.java)
                                intent.putExtra("policy_file", file)
                                startActivity(intent)
                            }
                        }
                        // Load the PDF cover
                        file?.let {
                            loadPDFCover(file, 0) // Load the first page of the PDF
                        }
                    }
                }
                is Resource.Error -> {
                    binding.shimmerContainer.stopShimmer()
                    binding.shimmerContainer.visibility = View.GONE
                    binding.container.visibility = View.VISIBLE
                    response.message?.let { Log.e("GetFile", it) }
                }
                is Resource.Loading -> {
                    binding.container.visibility = View.GONE
                    binding.shimmerContainer.startShimmer()
                }
                else -> { Log.e("Unknwon", "Error")}
            }
        })

        viewPagerAdapter = ViewPagerAdapter(this, id)
        binding.viewPager.adapter = viewPagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Details"
                1 -> tab.text = "Opinions"
            }
        }.attach()

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnComment.setOnClickListener{
            val intent = Intent(this, CommentActivity::class.java)
            intent.putExtra("policy_id", id)
            intent.putExtra("policy_source", source)
            intent.putExtra("policy_title", title)
            intent.putExtra("policy_theme", theme)
            startActivity(intent)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel() // Cancel the coroutine scope when the activity is destroyed
    }

    private fun loadPDFCover(pdfFile: String, pageNumber: Int) {
        coroutineScope.launch {
            try {
                val pdfInputStream = withContext(Dispatchers.IO) {
                    URL(pdfFile).openStream()
                }

                val file = File(cacheDir, "cover.pdf")

                withContext(Dispatchers.IO) {
                    FileOutputStream(file).use { output ->
                        pdfInputStream.copyTo(output)
                    }
                }

                val parcelFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
                val pdfRenderer = PdfRenderer(parcelFileDescriptor)

                withContext(Dispatchers.Main) {
                    val coverPage = pdfRenderer.openPage(pageNumber)
                    val bitmap = Bitmap.createBitmap(coverPage.width, coverPage.height, Bitmap.Config.ARGB_8888)
                    coverPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                    coverPage.close()
                    pdfRenderer.close()
                    parcelFileDescriptor.close()

                    val imageView = findViewById<ImageView>(R.id.ivPicture)
                    Glide.with(this@PolicyActivity).load(bitmap).into(binding.ivPicture)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        intent.putExtra("openFragmentPolicy", true)
        startActivity(intent)
        finish()
    }
}
