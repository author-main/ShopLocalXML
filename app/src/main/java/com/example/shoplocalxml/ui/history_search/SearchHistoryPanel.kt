package com.example.shoplocalxml.ui.history_search

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoplocalxml.AppShopLocal.Companion.applicationContext
import com.example.shoplocalxml.R


class SearchHistoryPanel(private val parent: ViewGroup, private val onHistorySearchHistoryListener: OnSearchHistoryListener) {
    private val layoutHistorySearch =  LayoutInflater.from(parent.context)
        .inflate(R.layout.history_search_panel, parent, false) as LinearLayout

    private lateinit var adapter: SearchAdapter

    fun setSearchQuery(value: String){

    }

    fun show(items: List<String>){
        layoutHistorySearch.findViewById<Button>(R.id.buttonClear).setOnClickListener {
            adapter.clearHistory()
            onHistorySearchHistoryListener.clearHistory()
            hide()
        }

        val manager = LinearLayoutManager(layoutHistorySearch.context)
        adapter = SearchAdapter(items) {query, delete ->

        }


        val animation = AnimationUtils.loadAnimation(applicationContext, com.example.shoplocalxml.R.anim.slide_in_top)
        parent.addView(layoutHistorySearch)
        layoutHistorySearch.startAnimation(animation)
    }

    private fun hide(){
        val animation = AnimationUtils.loadAnimation(applicationContext, com.example.shoplocalxml.R.anim.slide_in_right)
        layoutHistorySearch.startAnimation(animation)
        parent.removeView(layoutHistorySearch)
    }

    private fun clickItem(value: String) {
        onHistorySearchHistoryListener.clickItem(value)
        hide()
    }

    private fun deleteItem(value: String) {
        onHistorySearchHistoryListener.deleteItem(value)
    }
}