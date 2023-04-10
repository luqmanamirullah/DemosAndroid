package com.example.demos.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.demos.R
import com.example.demos.databinding.ComponentSearchBinding
import com.example.demos.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var searchComponentBinding: ComponentSearchBinding
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

}