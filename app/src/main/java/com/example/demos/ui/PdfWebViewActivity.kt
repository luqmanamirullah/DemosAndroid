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

        val file = intent.getStringExtra("policy_file")

        binding.apply {
            webView.webViewClient = WebViewClient()
            webView.settings.setSupportZoom(true)
            webView.settings.javaScriptEnabled = true
            webView.loadUrl("https://docs.google.com/gview?embedded=true&url=$file")
            webView.setDownloadListener(DownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            })
        }


    }
}

