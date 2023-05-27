package com.example.demos.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.demos.R
import com.example.demos.databinding.FragmentSearchNewsBinding
import com.example.demos.ui.MainActivity
import com.example.demos.ui.adapters.SearchNewsAdapter
import com.example.demos.ui.viewmodels.SearchViewModel
import com.example.demos.utils.Constants.Companion.SEARCH_DELAY
import com.example.demos.utils.Resource
import com.example.demos.utils.WrapContentLinearLayoutManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment : Fragment() {
    lateinit var binding: FragmentSearchNewsBinding
    lateinit var viewModel: SearchViewModel
    lateinit var searchNewsAdapter: SearchNewsAdapter
    lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchNewsBinding.inflate(layoutInflater)
        viewModel = (activity as MainActivity).searchViewModel
        handler = Handler()

        rvSearchNews()
        viewModel.searchNews("")
        var job: Job? = null
        binding.etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_DELAY)
                editable.let {
                    if (editable.toString().isNotEmpty()) {
                        viewModel.searchNews(editable.toString())
                    } else {
                        viewModel.searchNews("")
                    }
                }
            }
        }

        viewModel.searchNews.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    response.data?.let { res ->
                        searchNewsAdapter.differ.submitList(res.articles)
                    }
                }
                is Resource.Error -> {
                    Log.e("Aowkwkwkw Gagal", String())
                }
                is Resource.Loading -> {
                    searchNewsAdapter.differ.submitList(emptyList())
                }
            }
        })

        return (binding.root)
    }
    private fun rvSearchNews() = binding.rvSearchNews.apply {
        searchNewsAdapter = SearchNewsAdapter()
        adapter = searchNewsAdapter
        layoutManager = WrapContentLinearLayoutManager(requireContext())
    }
}