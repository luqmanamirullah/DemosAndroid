package com.example.demos.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.demos.api.example.RetrofitInstance
import com.example.demos.databinding.FragmentSearchBinding
import com.example.demos.ui.MainActivity
import com.example.demos.ui.adapters.ExampleAdapter
import com.example.demos.ui.adapters.NewsListsAdapter
import com.example.demos.ui.adapters.TrendListsAdapter
import com.example.demos.ui.viewmodels.HomeViewModel
import com.example.demos.utils.Constants
import com.example.demos.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var newsListsAdapter: NewsListsAdapter
    lateinit var viewModel: HomeViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        viewModel = (activity as MainActivity).homeViewModel
        Log.d("SearchFragment", "this is search fragment")

        rvSearchResult()
        viewModel.search("")
        var job: Job? = null
        binding.etSearch.addTextChangedListener{editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(Constants.SEARCH_DELAY)
                editable?.let{
                    if (editable.toString().isNotEmpty()){
                        viewModel.search(editable.toString())
                    } else {
                        viewModel.search("")
                    }
                }
            }
        }

        viewModel.searchMatch.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    response.data?.let { newsResponse ->
                        newsListsAdapter.differ.submitList(newsResponse.data)
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

        return binding.root
    }

    private fun rvSearchResult() = binding.rvSearchResult.apply {
        newsListsAdapter = NewsListsAdapter()
        adapter = newsListsAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }


}
