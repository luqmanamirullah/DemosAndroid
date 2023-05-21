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
import com.example.demos.databinding.FragmentPolicyDetailsBinding
import com.example.demos.databinding.ItemPolicyDetailsBinding
import com.example.demos.models.policy.DetailsPolicy
import com.example.demos.ui.MainActivity
import com.example.demos.ui.PolicyActivity
import com.example.demos.ui.adapters.*
import com.example.demos.ui.viewmodels.PolicyViewModel
import com.example.demos.utils.Resource
import com.example.demos.utils.SessionManager
import com.example.demos.utils.WrapContentLinearLayoutManager
import kotlinx.coroutines.launch

class PolicyDetailsFragment : Fragment() {
    lateinit var binding: FragmentPolicyDetailsBinding
    private lateinit var viewModel: PolicyViewModel
    private lateinit var impactsPolicyAdapter: ImpactsPolicyAdapter
    private lateinit var changedsPolicyAdapter: ChangedsPolicyAdapter
    private lateinit var changesPolicyAdapter: ChangesPolicyAdapter
    private lateinit var repealsPolicyAdapter: RepealsPolicyAdapter
    private lateinit var appointedWithsPolicyAdapter: AppointedWithsPolicyAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPolicyDetailsBinding.inflate(layoutInflater)

        val id = arguments?.getInt("policy_id", -1) ?: -1
        rvAppointedWithsPolicy()
        rvChangedsPolicy()
        rvChangesPolicy()
        rvImpacts()
        rvRepealsPolicy()
        viewModel = (activity as PolicyActivity).policyViewModel
        SessionManager.getToken(requireContext())?.let {
            lifecycleScope.launch {
                viewModel.getDetailsPolicy(id, it)
            }
        }
        viewModel.detailsPolicy.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    binding.shimmerContainer.stopShimmer()
                    binding.shimmerContainer.visibility = View.GONE
                    binding.contianer.visibility = View.VISIBLE
                    response.data?.let { res ->
                        binding.apply {
                            tvExplanation.text = res.data.explanation
                            tvTitle.text = res.data.title
                            tvAppointedAt.text = res.data.appointed_at
                            tvEntity.text = res.data.entity
                            tvNumber.text = res.data.number.toString()
                            tvSource.text = res.data.source
                            tvTheme.text = res.data.theme
                            tvType.text = res.data.type
                            tvCreatedAt.text = res.data.created_at
                            tvUpdatedAt.text = res.data.updated_at
                        }
                        impactsPolicyAdapter.differ.submitList(res.data.impacts)
                        changesPolicyAdapter.differ.submitList(res.data.policy_changes)
                        changedsPolicyAdapter.differ.submitList(res.data.policy_changeds)
                        repealsPolicyAdapter.differ.submitList(res.data.policy_repeals)
                        appointedWithsPolicyAdapter.differ.submitList(res.data.policy_appointed_with)
                    }
                }
                is Resource.Error -> {
                    response.message?.let { Log.e("Details Policy", it) }
                }
                is Resource.Loading ->  {
                    binding.contianer.visibility = View.GONE
                    binding.shimmerContainer.startShimmer()
                }
            }
        })

        return binding.root
    }

    private fun rvImpacts()= binding.rvImpacts.apply{
        impactsPolicyAdapter = ImpactsPolicyAdapter()
        adapter = impactsPolicyAdapter
        layoutManager = WrapContentLinearLayoutManager(requireContext())
    }

    private fun rvChangedsPolicy() = binding.rvPolicyChangeds.apply {
        changedsPolicyAdapter = ChangedsPolicyAdapter()
        adapter = changedsPolicyAdapter
        layoutManager = WrapContentLinearLayoutManager(requireContext())
    }

    private fun rvChangesPolicy() = binding.rvPolicyChanges.apply {
        changesPolicyAdapter = ChangesPolicyAdapter()
        adapter = changesPolicyAdapter
        layoutManager = WrapContentLinearLayoutManager(requireContext())
    }

    private fun rvRepealsPolicy() = binding.rvPolicyRepals.apply {
        repealsPolicyAdapter = RepealsPolicyAdapter()
        adapter = repealsPolicyAdapter
        layoutManager = WrapContentLinearLayoutManager(requireContext())
    }

    private fun rvAppointedWithsPolicy() = binding.rvPolicyAppointedWiths.apply {
        appointedWithsPolicyAdapter = AppointedWithsPolicyAdapter()
        adapter = appointedWithsPolicyAdapter
        layoutManager = WrapContentLinearLayoutManager(requireContext())
    }

    companion object {
        fun newInstance(id: Int): PolicyDetailsFragment {
            val fragment = PolicyDetailsFragment()
            val args = Bundle()
            args.putInt("policy_id", id)
            fragment.arguments = args
            return fragment
        }
    }
}