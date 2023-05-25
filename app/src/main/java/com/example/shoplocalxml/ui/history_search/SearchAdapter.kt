package com.example.shoplocalxml.ui.history_search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplocalxml.AppShopLocal.Companion.applicationContext
import com.example.shoplocalxml.EMPTY_STRING
import com.example.shoplocalxml.R

class SearchAdapter(private val items: List<String>, private val onClickItem: (query: String, delete: Boolean) -> Unit): RecyclerView.Adapter<SearchAdapter.ViewHolder>(){

    var searchQuery: String = EMPTY_STRING
        set(value) {
            setSearchQuery(value)
        }

    private var filtered: Boolean = false
    private val showItems = items.toMutableList()

    @JvmName("setSearchQuery_")
    private fun setSearchQuery(value: String) {

            filtered = value.isNotBlank() ?: false
            searchQuery = if (filtered) {
                showItems.clear()
                showItems.addAll(items.filter {
                    it.contains(value, true)
                })
                value
            } else {
                showItems.clear()
                showItems.addAll(items)
                EMPTY_STRING
            }

    }

    fun clearHistory(){
        if (showItems.isNotEmpty()) {
            val count = showItems.size - 1
            items.clear()
            showItems.clear()
            notifyItemRangeRemoved(0, count)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = showItems[position]
        holder.textItem.text = item
        val visibleDeleteButton = if (filtered) View.GONE else View.VISIBLE
        holder.deleteButton.visibility = visibleDeleteButton
        if (!filtered)
            holder.deleteButton.setOnClickListener {
                onClickItem(item, true)
                items.remove(item)
                showItems.remove(item)
                notifyItemRemoved(position)
            }

        holder.textItem.setOnClickListener {
            onClickItem(item, false)
        }

    }

    override fun getItemCount(): Int {
        return showItems.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(applicationContext).inflate(R.layout.history_search_item, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val deleteButton: ImageView = view.findViewById(R.id.buttonDeleteSearchQuery)
        val textItem: TextView = view.findViewById(R.id.textSearchQuery)
    }
}