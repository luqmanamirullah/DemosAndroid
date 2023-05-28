package com.example.demos.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.demos.R
import com.example.demos.databinding.FragmentArticleBinding
import com.example.demos.ui.MainActivity
import com.example.demos.ui.viewmodels.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class ArticleFragment : Fragment() {
    lateinit var binding: FragmentArticleBinding
    private val args: ArticleFragmentArgs by navArgs()
    private lateinit var viewModel: NewsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentArticleBinding.inflate(layoutInflater)
        val news = args.article
        viewModel = (activity as MainActivity).newsViewModel
        binding.apply {
            webView.webViewClient = WebViewClient()
            webView.loadUrl(news.url)

            btnBack.setOnClickListener{
                findNavController().navigate(R.id.newsFragment)
            }

            btnSave.setOnClickListener {
                if (!btnSave.isSelected) {
                    viewModel.saveNews(news)
                    view?.let { v ->
                        Snackbar.make(v, "Article saved successfully", Snackbar.LENGTH_SHORT).show()
                    }
                } else {
                    viewModel.delelteNews(news)
                    view?.let { v ->
                        Snackbar.make(v, "Article deleted from your saved", Snackbar.LENGTH_SHORT).show()
                    }
                }
                btnSave.isSelected = !btnSave.isSelected
            }

        }
        return binding.root
    }



}