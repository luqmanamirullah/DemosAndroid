package com.example.demos.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demos.R
import com.example.demos.api.RetrofitInstance
import com.example.demos.databinding.ComponentSearchBinding
import com.example.demos.databinding.FragmentHomeBinding
import com.example.demos.ui.MainActivity
import com.example.demos.ui.adapters.DraftNewsAdapter
import com.example.demos.ui.adapters.NewsListsAdapter
import com.example.demos.ui.adapters.TrendListsAdapter
import com.example.demos.ui.viewmodels.HomeViewModel
import com.example.demos.utils.Resource
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var searchComponentBinding: ComponentSearchBinding
    private lateinit var trendListsAdapter: TrendListsAdapter
    private lateinit var newsListsAdapter: NewsListsAdapter
    private lateinit var draftNewsAdapter: DraftNewsAdapter
    lateinit var viewModel: HomeViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).homeViewModel

        rvTrendLists()
        lifecycleScope.launchWhenCreated {
            val response = try {
                RetrofitInstance.api.getTrends()
            } catch (e: IOException){
                Log.e("@MainActivity", "IOException, you might not have internet")
                return@launchWhenCreated
            } catch (e: HttpException){
                Log.e("@MainActivity", "HttpException, unexpected response")
                return@launchWhenCreated
            }
            if (response.isSuccessful && response.body() !== null){
                trendListsAdapter.trendLists = response.body()!!.data
            } else {
                Log.e("@MainActivity", "Response not successful")
            }
        }

        rvNewsLists()
        viewModel.news.observe(viewLifecycleOwner, Observer { response ->
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

        rvDraftNews()
        viewModel.newsType.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    response.data?.let { newsType ->
                        draftNewsAdapter.differ.submitList(newsType.data)
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        Log.e("News Type Data", "An error occured: $message")
                    }
                }
                else -> {
                    Log.e("Unknown", "Error")
                }
            }
        })
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        searchComponentBinding = ComponentSearchBinding.bind(binding.root.findViewById(R.id.search_component))
        Log.d("HomeFragment", "this home fragmetn")
        searchComponentBinding.searchComponent.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToSearchFragment()
            findNavController().navigate(action)
        }

        return binding.root
    }

    private fun rvTrendLists() = binding.rvTrendLists.apply {
        trendListsAdapter = TrendListsAdapter()
        adapter = trendListsAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun rvNewsLists() = binding.rvNewsLists.apply {
        newsListsAdapter = NewsListsAdapter()
        adapter = newsListsAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun rvDraftNews() = binding.rvDraftNews.apply {
        draftNewsAdapter = DraftNewsAdapter(viewModel)
        adapter = draftNewsAdapter
        layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
    }
}