package com.example.demos.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.demos.R
import com.example.demos.databinding.ComponentSearchBinding
import com.example.demos.databinding.FragmentGovermentpolicyBinding
import com.example.demos.databinding.FragmentHomeBinding
import com.example.demos.repository.PolicyRepository
import com.example.demos.ui.MainActivity
import com.example.demos.ui.adapters.PolicyListsAdapter
import com.example.demos.ui.viewmodels.PolicyViewModel
import com.example.demos.utils.Resource
import com.example.demos.utils.SessionManager
import com.example.demos.utils.WrapContentLinearLayoutManager
import kotlinx.coroutines.launch

class GovermentPolicyFragment : Fragment() {
    private lateinit var policyListsAdapter: PolicyListsAdapter
    private lateinit var binding: FragmentGovermentpolicyBinding
    lateinit var viewModel: PolicyViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).policyViewModel
        SessionManager.getToken(requireContext())?.let {
            lifecycleScope.launch {
                viewModel.getPolicies(it)
            }
        }
        rvPolicies()
        viewModel.policies.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    response.data?.let {
                        policyListsAdapter.differ.submitList(it.data)
                    }
                }
                is Resource.Error -> {
                    response.message?.let {
                        Log.e("Policy Fragment", it)
                    }
                }
                is Resource.Loading -> {
                    policyListsAdapter.differ.submitList(emptyList())
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGovermentpolicyBinding.inflate(inflater, container, false)
        val searchComponentBinding = ComponentSearchBinding.bind(binding.root.findViewById(R.id.search_component))
        searchComponentBinding.searchComponent.setOnClickListener {
            val action = GovermentPolicyFragmentDirections.actionGovermentPolicyFragmentToSearchFragment()
            findNavController().navigate(action)
        }

        return binding.root
    }

    private fun rvPolicies() = binding.rvPolicies.apply{
        policyListsAdapter = PolicyListsAdapter()
        adapter = policyListsAdapter
        layoutManager = WrapContentLinearLayoutManager(requireContext())
    }
}