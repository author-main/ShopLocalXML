package com.example.shoplocalxml.ui.history_search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.shoplocalxml.AppShopLocal.Companion.applicationContext
import com.example.shoplocalxml.R


class SearchHistoryPanel(private val parent: ViewGroup) {
    private val layoutHistorySearch =  LayoutInflater.from(parent.context)
        .inflate(R.layout.history_search_panel, parent, false) as LinearLayout

    fun show(){
        parent.addView(layoutHistorySearch)
    }
}