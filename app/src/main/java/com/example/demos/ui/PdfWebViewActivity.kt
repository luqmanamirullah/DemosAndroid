package com.example.demos.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.DownloadListener
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.demos.databinding.ActivityPdfWebViewBinding


class PdfWebViewActivity() : AppCompatActivity() {
    lateinit var binding: ActivityPdfWebViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val news = intent.getStringExtra("url")

        news?.let{
            binding.apply {
                webView.webViewClient = WebViewClient()
                webView.loadUrl(news)
            }
        }

        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }


    }
}

