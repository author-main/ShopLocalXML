package com.example.shoplocalxml.ui.history_search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplocalxml.AppShopLocal.Companion.applicationContext
import com.example.shoplocalxml.R

class SearchAdapter(private val items: MutableList<SearchItem>): RecyclerView.Adapter<SearchAdapter.ViewHolder>(){



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return items.count { item -> !item.deleted }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(applicationContext).inflate(R.layout.history_search_item, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

    }
}