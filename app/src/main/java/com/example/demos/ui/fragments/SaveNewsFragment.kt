package com.example.demos.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.demos.databinding.FragmentSaveNewsBinding

class SaveNewsFragment : Fragment() {
    lateinit var binding: FragmentSaveNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSaveNewsBinding.inflate(layoutInflater)
        return (binding.root)
    }

}