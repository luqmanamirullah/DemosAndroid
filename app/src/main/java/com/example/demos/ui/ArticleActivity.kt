package com.example.demos.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.demos.R
import com.example.demos.databinding.ActivityArticleBinding
import com.example.demos.repository.ArticleRepository
import com.example.demos.ui.viewmodels.ArticleViewModel
import com.example.demos.ui.viewmodels.ArticleViewModelProviderFactory
import com.example.demos.ui.viewmodels.HomeViewModel
import com.example.demos.ui.viewmodels.HomeViewModelProviderFactory
import com.example.demos.utils.Resource

class ArticleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArticleBinding
    private lateinit var viewModel: ArticleViewModel
    private lateinit var articleRepository: ArticleRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        articleRepository = ArticleRepository()
        viewModel = ViewModelProvider(this, ArticleViewModelProviderFactory(articleRepository))[ArticleViewModel::class.java]
        binding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val id = intent.getIntExtra("news_id", -1)
        viewModel.getNewsDetail(id)

        viewModel.article.observe(this, Observer { response ->
            when(response){
                is Resource.Success -> {
                    response.data?.let { articleResponse ->
                        Glide.with(this).load(articleResponse.data.image_url).into(binding.ivArticle)
                        binding.apply {
                            tvType.text = articleResponse.data.type
                            tvTitle.text = articleResponse.data.title
                            tvAuthor.text = "author by: ${articleResponse.data.author}"
                            tvCreatedAt.text = articleResponse.data.created_at
                            tvContent.text = "\t${articleResponse.data.content}"
                        }
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        Log.e("News Data", "An error occured: $message")
                    }
                }
                else -> {
                    Log.e("Unknwon", "Error")
                }
            }
        })
        binding.btnBack.setOnClickListener {
            intent = Intent (this, MainActivity::class.java).also {
                startActivity(it)
            }
        }
    }
}