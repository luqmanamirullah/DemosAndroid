package com.example.demos.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.demos.databinding.FragmentSaveNewsBinding
import com.example.demos.ui.MainActivity
import com.example.demos.ui.adapters.NewsListsAdapter
import com.example.demos.ui.adapters.SearchNewsAdapter
import com.example.demos.ui.viewmodels.NewsViewModel
import com.example.demos.utils.WrapContentLinearLayoutManager
import com.google.android.material.snackbar.Snackbar

class SaveNewsFragment : Fragment() {
    lateinit var binding: FragmentSaveNewsBinding
    private lateinit var newsAdapter: NewsListsAdapter
    private lateinit var viewModel: NewsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSaveNewsBinding.inflate(layoutInflater)
        viewModel = (activity as MainActivity).newsViewModel

        val itemTouchCall = object  : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newsAdapter.differ.currentList[position]
                viewModel.delelteNews(article)
                view?.let { v ->
                    Snackbar.make(v, "Article deleted from your saved", Snackbar.LENGTH_SHORT).apply {
                        setAction("Undo"){
                            viewModel.saveNews(article)
                        }
                        show()
                    }
                }
            }
        }

        ItemTouchHelper(itemTouchCall).apply {
            attachToRecyclerView(binding.rvSavedNews)
        }

        rvSavedNews()
        viewModel.getSavedNews().observe(viewLifecycleOwner, Observer {news ->
            newsAdapter.differ.submitList(news)
        })


        return (binding.root)
    }

    private fun rvSavedNews() = binding.rvSavedNews.apply {
        newsAdapter = NewsListsAdapter()
        adapter = newsAdapter
        layoutManager = WrapContentLinearLayoutManager(requireContext())
    }

}