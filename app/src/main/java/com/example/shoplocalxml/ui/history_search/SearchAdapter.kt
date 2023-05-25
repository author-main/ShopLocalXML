package com.example.shoplocalxml.ui.history_search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplocalxml.AppShopLocal.Companion.applicationContext
import com.example.shoplocalxml.R

class SearchAdapter(private val items: MutableList<SearchItem>, private val onClickListener: OnSearchItemClickListener): RecyclerView.Adapter<SearchAdapter.ViewHolder>(){

    var searchQuery: String? = null
        set(value) {
            setSearchQuery(value)
        }

    private var filtered: Boolean = false

    @JvmName("setSearchQuery_")
    private fun setSearchQuery(value: String?) {
        filtered = value?.isNotBlank() ?: false
        searchQuery = if (filtered)
            value
        else
            null
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        if (!item.deleted) {
            holder.textItem.text = item.query
            val visibleDeleteButton = if (filtered) View.GONE else View.VISIBLE
            holder.deleteButton.visibility = visibleDeleteButton
            if (!filtered)
                holder.deleteButton.setOnClickListener {
                    onClickListener.onDeleteItem(item.query)
                }
        }
    }

    override fun getItemCount(): Int {
        return items.count { item -> !item.deleted }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(applicationContext).inflate(R.layout.history_search_item, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val deleteButton = view.findViewById<ImageView>(R.id.buttonDeleteSearchQuery)
        val textItem = view.findViewById<TextView>(R.id.textSearchQuery)
    }
}