package com.example.demos.utils

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class WrapContentLinearLayoutManager(
    context: Context, orientation: Int = RecyclerView.VERTICAL, reverse: Boolean = false
): LinearLayoutManager(context, orientation, reverse) {
    override fun onLayoutChildren(
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?
    ) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: IndexOutOfBoundsException){
            Log.e("Recycler View Bug", "meet a IOOBE in RecyclerView")
        }
    }
}