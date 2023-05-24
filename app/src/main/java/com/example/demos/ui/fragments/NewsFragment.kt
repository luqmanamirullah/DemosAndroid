package com.example.demos.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.demos.databinding.FragmentNewsBinding
import com.example.demos.models.newsFromInternet.Article
import com.example.demos.ui.MainActivity
import com.example.demos.ui.adapters.ImageNewsAdapter
import com.example.demos.ui.viewmodels.NewsViewModel
import com.example.demos.utils.Resource
import java.lang.Math.abs

class NewsFragment : Fragment() {
    lateinit var binding: FragmentNewsBinding
    lateinit var viewModel: NewsViewModel
    lateinit var imageNewsAdapter: ImageNewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNewsBinding.inflate(layoutInflater)
        viewModel = (activity as MainActivity).newsViewModel
        imageNewsAdapter = ImageNewsAdapter(binding.viewPager)


        viewModel.getNews("top")
        viewModel.news.observe(viewLifecycleOwner, Observer {response ->
            when(response){
                is Resource.Success -> {
                    response.data?.let {res ->
                        val topThree = Article.getFirstThreeArticles(res.results)
                        imageNewsAdapter.differ.submitList(topThree)
                        init()
                        setUpTransformer()
                        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
                            override fun onPageSelected(position: Int) {
                                super.onPageSelected(position)
                            }
                        })
                    }
                }
                is Resource.Error -> {
                    response.message?.let {
                        Log.e("Error Anjing",it)
                    }
                }
                is Resource.Loading -> {
                    imageNewsAdapter.differ.submitList(emptyList())
                }
            }
        })

        return (binding.root)

    }

    private val runnable = Runnable {
        binding.viewPager.currentItem = binding.viewPager.currentItem + 1
    }

    private fun init(){
        binding.viewPager.apply {
            adapter = imageNewsAdapter

            offscreenPageLimit = 3
            clipToOutline = false
            clipToPadding = false
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }
    }

    private fun setUpTransformer(){
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer{page, positiion->
            val r = 1 - abs(positiion)
            page.scaleY = 0.85f + r + 0.14f
        }

        binding.viewPager.setPageTransformer(transformer)
    }
}