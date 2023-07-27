package com.example.shoplocalxml.ui.history_search

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplocalxml.AppShopLocal.Companion.applicationContext
import com.example.shoplocalxml.R
import com.example.shoplocalxml.log


class SearchHistoryPanel(private val parent: ViewGroup, private val onHistorySearchHistoryListener: OnSearchHistoryListener) {
    private val layoutHistorySearch =  LayoutInflater.from(parent.context)
        .inflate(R.layout.history_search_panel, parent, false) as LinearLayout

    private lateinit var adapter: SearchAdapter


    fun setSearchQuery(value: String){
        adapter.searchQuery = value
        val header = layoutHistorySearch.findViewById<LinearLayout>(R.id.layerSearchButtons)
        if (value.isEmpty())
            header.visibility = View.VISIBLE
        else
            header.visibility = View.GONE
    }

    fun update(query: String) {
        adapter.searchQuery = query
    }

    fun show(items: List<String>, start: String){
        layoutHistorySearch.elevation = 10f
        layoutHistorySearch.findViewById<Button>(R.id.buttonClear).setOnClickListener {
            //adapter.clearHistory()
            onHistorySearchHistoryListener.clearSearchHistory()
            //hide()
        }

        val manager = LinearLayoutManager(layoutHistorySearch.context)
        adapter = SearchAdapter(items.toMutableList()) {query, delete ->
            if (delete)
                onHistorySearchHistoryListener.deleteSearchHistoryItem(query)
            else {
                onHistorySearchHistoryListener.clickSearchHistoryItem(query)
            }
        }
        adapter.searchQuery = start
        val recyclerView: RecyclerView = layoutHistorySearch.findViewById(R.id.recyclerViewSearch)
        recyclerView.layoutManager = manager
        recyclerView.adapter = adapter
        val animation = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_in_top)
        parent.addView(layoutHistorySearch)
        layoutHistorySearch.startAnimation(animation)
    }

    fun hide(){
        val animation = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_in_right)
        layoutHistorySearch.startAnimation(animation)
        parent.removeView(layoutHistorySearch)
    }


}