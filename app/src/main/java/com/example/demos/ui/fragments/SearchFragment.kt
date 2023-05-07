package com.example.demos.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.demos.api.example.RetrofitInstance
import com.example.demos.databinding.FragmentSearchBinding
import com.example.demos.ui.adapters.ExampleAdapter
import retrofit2.HttpException
import java.io.IOException

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var exampleAdapter: ExampleAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        Log.d("SearchFragment", "this is search fragment")
        setupRecyclerView()
        lifecycleScope.launchWhenCreated {
            val response = try {
                RetrofitInstance.api.getExamples()
            } catch (e: IOException){
                Log.e("TAG", "IOException, you might not have internet")
                return@launchWhenCreated
            } catch (e: HttpException){
                Log.e("TAG", "HttpException, unexpected response")
                return@launchWhenCreated
            }
            if (response.isSuccessful && response.body() != null){
                exampleAdapter.examples = response.body()!!
                Log.e("Test", response.body().toString())
            } else {
                Log.e("TAG", "Response not successful")
            }
        }

        return binding.root
    }

    private fun setupRecyclerView() = binding.rvExample.apply {
        exampleAdapter = ExampleAdapter()
        adapter = exampleAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

}
