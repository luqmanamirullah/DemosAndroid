package com.example.demos.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.demos.databinding.FragmentNewsBinding
import com.example.demos.models.newsFromInternet.Article
import com.example.demos.ui.MainActivity
import com.example.demos.ui.adapters.ImageNewsAdapter
import com.example.demos.ui.adapters.NewsListsAdapter
import com.example.demos.ui.viewmodels.NewsViewModel
import com.example.demos.utils.Resource
import com.example.demos.utils.WrapContentLinearLayoutManager

class NewsFragment : Fragment() {
    lateinit var binding: FragmentNewsBinding
    lateinit var viewModel: NewsViewModel
    lateinit var imageNewsAdapter: ImageNewsAdapter

    private lateinit var handler: Handler
    private lateinit var autoScrollRunnable: Runnable
    private lateinit var newsListsAdapter: NewsListsAdapter
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
        handler = Handler()

        rvLatestNews()
        viewModel.getNews("")
        viewModel.news.observe(viewLifecycleOwner, Observer {response ->
            when(response){
                is Resource.Success -> {
                    response.data?.let {res ->
                        val topThree = Article.getFirstThreeArticles(res.articles)
                        Log.e("top three result", "$topThree")
                        imageNewsAdapter.differ.submitList(topThree)
                        newsListsAdapter.differ.submitList(res.articles)
                        init()
                        setUpTransformer()
                        startAutoScroll()

//                        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
//                            override fun onPageSelected(position: Int) {
//                                super.onPageSelected(position)
//                                handler.removeCallbacks(runnable)
//                                handler.postDelayed(runnable, 10000)
//
//                            }
//                        })
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

//    override fun onPause() {
//        super.onPause()
//        handler.removeCallbacks(runnable)
//    }
//
//    override fun onResume() {
//        super.onResume()
//        handler.postDelayed(runnable, 2000)
//    }
//
//    private val runnable = Runnable {
//        binding.viewPager.currentItem = binding.viewPager.currentItem + 1
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopAutoScroll() // Stop auto-scrolling when the view is destroyed
    }

    private fun startAutoScroll(){
        autoScrollRunnable = Runnable {
            val currentItem = binding.viewPager.currentItem
            val nextItem = if (currentItem == imageNewsAdapter.itemCount - 1) 0 else currentItem + 1

            if (nextItem == 0 && currentItem == imageNewsAdapter.itemCount - 1) {
                binding.viewPager.setCurrentItem(nextItem, false)
            } else {
                binding.viewPager.currentItem = nextItem
            }

            handler.postDelayed(autoScrollRunnable, 10000) // Delay between auto-scrolls
        }
        handler.postDelayed(autoScrollRunnable, 10000)
    }

    private fun stopAutoScroll() {
        handler.removeCallbacks(autoScrollRunnable)
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
        transformer.addTransformer(MarginPageTransformer(0))
        transformer.addTransformer{page, position->
            val r = 1 - kotlin.math.abs(position)
            page.scaleY = 0.75f + r * 0.10f
            page.scaleX = 0.75f + r * 0.10f
        }

        binding.viewPager.setPageTransformer(transformer)
    }

    private fun rvLatestNews()= binding.rvLatestNews.apply{
        newsListsAdapter = NewsListsAdapter()
        adapter = newsListsAdapter
        layoutManager = WrapContentLinearLayoutManager(requireContext())
    }
}