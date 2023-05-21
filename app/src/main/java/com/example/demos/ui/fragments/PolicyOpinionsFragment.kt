package com.example.demos.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.demos.R
import com.example.demos.databinding.FragmentPolicyOpinionsBinding
import com.example.demos.databinding.ItemPolicyOpinionsBinding
import com.example.demos.ui.PolicyActivity
import com.example.demos.ui.adapters.OpinionsAdapter
import com.example.demos.ui.viewmodels.PolicyViewModel
import com.example.demos.utils.Resource
import com.example.demos.utils.SessionManager
import com.example.demos.utils.WrapContentLinearLayoutManager
import kotlinx.coroutines.launch

class PolicyOpinionsFragment : Fragment() {
    private lateinit var viewModel: PolicyViewModel
    private lateinit var opinionsAdapter: OpinionsAdapter
    lateinit var binding: FragmentPolicyOpinionsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPolicyOpinionsBinding.inflate(layoutInflater)
        val id = arguments?.getInt("policy_id", -1) ?: -1

        rvOpinions()
        viewModel = (activity as PolicyActivity).policyViewModel
        SessionManager.getToken(requireContext())?.let {
            lifecycleScope.launch {
                viewModel.getOpinions(id, it)
            }
        }
        viewModel.opinions.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    response.data?.let {
                        opinionsAdapter.differ.submitList(it.data)
                    }
                }
                is Resource.Error -> {
                    response.message?.let {
                        Log.e("Opinions", it)
                    }
                }
                is Resource.Loading -> {
                    opinionsAdapter.differ.submitList(emptyList())
                }
            }
        })
        return binding.root
    }

    private fun rvOpinions()= binding.rvOpinions.apply {
        opinionsAdapter = OpinionsAdapter()
        adapter = opinionsAdapter
        layoutManager = WrapContentLinearLayoutManager(requireContext())
    }

    companion object {
        fun newInstance(id: Int): PolicyOpinionsFragment{
            val fragment = PolicyOpinionsFragment()
            val args = Bundle()
            args.putInt("policy_id", id)
            fragment.arguments = args
            return fragment
        }
    }

}