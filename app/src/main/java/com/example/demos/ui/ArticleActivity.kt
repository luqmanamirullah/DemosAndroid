package com.example.demos.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.demos.databinding.ActivityArticleBinding
import com.example.demos.repository.ArticleRepository
import com.example.demos.ui.viewmodels.ArticleViewModel
import com.example.demos.ui.viewmodels.ArticleViewModelProviderFactory
import com.example.demos.utils.Resource

class ArticleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArticleBinding
    private lateinit var viewModel: ArticleViewModel
    private lateinit var articleRepository: ArticleRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        articleRepository = ArticleRepository()
//        viewModel = ViewModelProvider(this, ArticleViewModelProviderFactory(articleRepository))[ArticleViewModel::class.java]
//
//        val id = intent.getIntExtra("news_id", -1)
//        viewModel.getNewsDetail(id)
//
//        viewModel.article.observe(this, Observer { response ->
//            when(response){
//                is Resource.Success -> {
//                    binding.shimmerViewContainer.stopShimmer()
//                    binding.shimmerViewContainer.visibility = View.GONE
//                    binding.ctArticle.visibility = View.VISIBLE
//                    response.data?.let { articleResponse ->
//                        Glide.with(this).load(articleResponse.data.image_url).into(binding.ivArticle)
//                        binding.apply {
//                            tvType.text = articleResponse.data.type
//                            tvTitle.text = articleResponse.data.title
//                            tvAuthor.text = "author by: ${articleResponse.data.author}"
//                            tvCreatedAt.text = articleResponse.data.created_at
//                            tvContent.text = "\t${articleResponse.data.content}"
//                        }
//                    }
//                }
//                is Resource.Error -> {
//                    response.message?.let { message ->
//                        Log.e("News Data", "An error occured: $message")
//                    }
//                    binding.shimmerViewContainer.stopShimmer()
//                    binding.shimmerViewContainer.visibility = View.GONE
//                    binding.ctArticle.visibility = View.VISIBLE
//                }
//                is Resource.Loading -> {
//                    binding.ctArticle.visibility = View.GONE
//                    binding.shimmerViewContainer.startShimmer()
//                }
//                else -> {
//                    Log.e("Unknwon", "Error")
//                }
//            }
//        })



        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        intent.putExtra("openFragmentHome", true)
        startActivity(intent)
        finish()
    }
}
