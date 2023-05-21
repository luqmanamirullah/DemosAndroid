package com.example.demos.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.example.demos.databinding.FragmentSearchBinding
import com.example.demos.ui.MainActivity
import com.example.demos.ui.adapters.NewsListsAdapter
import com.example.demos.ui.adapters.PolicyListsAdapter
import com.example.demos.ui.viewmodels.HomeViewModel
import com.example.demos.utils.Constants
import com.example.demos.utils.Resource
import com.example.demos.utils.WrapContentLinearLayoutManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var newsListsAdapter: NewsListsAdapter
    private lateinit var policyListsAdapter: PolicyListsAdapter
    lateinit var viewModel: HomeViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        viewModel = (activity as MainActivity).homeViewModel
        Log.d("SearchFragment", "this is search fragment")

        rvNewsResult()
        rvPolicyResult()
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
                    response.data?.let { res ->
                        if (res.news.isNullOrEmpty()){
                            binding.rvNewsResult.visibility = View.GONE
                        } else {
                            binding.rvNewsResult.visibility = View.VISIBLE
                            newsListsAdapter.differ.submitList(res.news)
                        }
                        if (res.policy.isNullOrEmpty()){
                            binding.rvPolicyResult.visibility = View.GONE
                        } else {
                            binding.rvPolicyResult.visibility = View.VISIBLE
                            policyListsAdapter.differ.submitList(res.policy)
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

        return binding.root
    }

    private fun rvNewsResult() = binding.rvNewsResult.apply {
        newsListsAdapter = NewsListsAdapter()
        adapter = newsListsAdapter
        layoutManager = WrapContentLinearLayoutManager(requireContext())
    }

    private fun rvPolicyResult() = binding.rvPolicyResult.apply {
        policyListsAdapter = PolicyListsAdapter()
        adapter = policyListsAdapter
        layoutManager = WrapContentLinearLayoutManager(requireContext())
    }


}
