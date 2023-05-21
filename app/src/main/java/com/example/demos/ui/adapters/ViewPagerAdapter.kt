package com.example.demos.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.demos.ui.fragments.PolicyDetailsFragment
import com.example.demos.ui.fragments.PolicyOpinionsFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity, private val id: Int): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        when(position){
            0 -> {
                return PolicyDetailsFragment.newInstance(id)
            }
            1 -> {
                return PolicyOpinionsFragment.newInstance(id)
            }
            else -> {
                return PolicyDetailsFragment.newInstance(id)
            }
        }
    }
}