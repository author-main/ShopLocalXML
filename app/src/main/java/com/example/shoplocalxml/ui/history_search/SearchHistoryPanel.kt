package com.example.shoplocalxml.ui.history_search

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.LinearLayout
import com.example.shoplocalxml.AppShopLocal.Companion.applicationContext
import com.example.shoplocalxml.R


class SearchHistoryPanel(private val parent: ViewGroup, private val onHistorySearchHistoryListener: OnSearchHistoryListener) {
    private val layoutHistorySearch =  LayoutInflater.from(parent.context)
        .inflate(R.layout.history_search_panel, parent, false) as LinearLayout

    fun show(){
        layoutHistorySearch.findViewById<Button>(R.id.buttonClear).setOnClickListener {
            onHistorySearchHistoryListener.clearHistory()
            val animation = AnimationUtils.loadAnimation(applicationContext, com.example.shoplocalxml.R.anim.slide_in_right)
            layoutHistorySearch.startAnimation(animation)
            parent.removeView(layoutHistorySearch)
        }
        val animation = AnimationUtils.loadAnimation(applicationContext, com.example.shoplocalxml.R.anim.slide_in_top)
        parent.addView(layoutHistorySearch)
        layoutHistorySearch.startAnimation(animation)

    }
}