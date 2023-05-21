package com.example.demos.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.pdf.PdfRenderer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.demos.R
import com.example.demos.databinding.ActivityCommentBinding
import com.example.demos.repository.PolicyRepository
import com.example.demos.ui.components.LoadingDialog
import com.example.demos.ui.viewmodels.PolicyViewModel
import com.example.demos.ui.viewmodels.PolicyViewModelProviderFactory
import com.example.demos.utils.Resource
import com.example.demos.utils.SessionManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class CommentActivity : AppCompatActivity() {
    lateinit var binding: ActivityCommentBinding
    lateinit var policyViewModel: PolicyViewModel
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    var isAgree: Int = 1
    private val loading = LoadingDialog(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getIntExtra("policy_id", -1)
        val title = intent.getStringExtra("policy_title")
        val source = intent.getStringExtra("policy_source")
        val theme = intent.getStringExtra("policy_theme")
        var content = ""
        val token = SessionManager.getToken(this)



        val policyRepository = PolicyRepository()
        policyViewModel = ViewModelProvider(
            this,
            PolicyViewModelProviderFactory(policyRepository)
        )[PolicyViewModel::class.java]

        token?.let {
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

        policyViewModel.createOpinionsResult.observe(this, Observer {response ->
            when(response){
                is Resource.Success ->{
                    loading.isDismiss()
                    showToast("Terimakasih telah memberikan opini :)")
                    val intent = Intent(this, PolicyActivity::class.java)
                    intent.putExtra("policy_id", id)
                    intent.putExtra("policy_source", source)
                    intent.putExtra("policy_title", title)
                    intent.putExtra("policy_theme", theme)
                    startActivity(intent)
                }
                is Resource.Error -> {
                    loading.isDismiss()
                    showToast("Error: Dilarang menentang pemerintah")
                }
                is Resource.Loading -> {
                    loading.startLoading()
                }
            }
        })

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
        showBottomSheetDialog()


        binding.apply {
            btnSubmit.setOnClickListener {
                if (etContent.text.isNullOrEmpty()){
                    showToast("Tolong masukan opinimu terlebih dahulu")
                } else {
                    content = etContent.text.toString()
                    token?.let {
                        lifecycleScope.launch {
                            policyViewModel.createOpinion(id, token, content, isAgree)
                        }
                    }
                }
            }
        }
    }

    private fun showBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_isagree_layout, null)
        bottomSheetDialog.setContentView(bottomSheetView)
        val btnAgree = bottomSheetView.findViewById<RelativeLayout>(R.id.ctAgree)
        val btnDegree = bottomSheetView.findViewById<RelativeLayout>(R.id.ctDegree)

        // Set the background tint based on the value of isAgree
        if (isAgree == 1) {
            btnAgree.backgroundTintList = ContextCompat.getColorStateList(this, R.color.red_500)
            btnDegree.backgroundTintList = ContextCompat.getColorStateList(this, R.color.white)
        } else {
            btnAgree.backgroundTintList = ContextCompat.getColorStateList(this, R.color.white)
            btnDegree.backgroundTintList = ContextCompat.getColorStateList(this, R.color.red_500)
        }

        btnAgree.setOnClickListener {
            isAgree = 1
            // Update the background tint
            btnAgree.backgroundTintList = ContextCompat.getColorStateList(this, R.color.red_500)
            btnDegree.backgroundTintList = ContextCompat.getColorStateList(this, R.color.white)

            // Update cvIsAgree background color here
            binding.cvIsAgree.setCardBackgroundColor(ContextCompat.getColor(this, R.color.green_200))
            binding.tvIsAgree.text = "Setuju"
        }

        btnDegree.setOnClickListener {
            isAgree = 0
            // Update the background tint
            btnAgree.backgroundTintList = ContextCompat.getColorStateList(this, R.color.white)
            btnDegree.backgroundTintList = ContextCompat.getColorStateList(this, R.color.red_500)

            // Update cvIsAgree background color here
            binding.cvIsAgree.setCardBackgroundColor(ContextCompat.getColor(this, R.color.red_500))
            binding.tvIsAgree.text = "Tidak Setuju"
        }

        val btnSave = bottomSheetView.findViewById<Button>(R.id.btnSave)
        btnSave.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.show()
    }



    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel() // Cancel the coroutine scope when the activity is destroyed
    }

    private fun showToast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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
                    Glide.with(this@CommentActivity).load(bitmap).into(binding.ivPicture)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


}