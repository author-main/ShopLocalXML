package com.example.shoplocalxml.ui.history_search

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplocalxml.AppShopLocal.Companion.applicationContext
import com.example.shoplocalxml.EMPTY_STRING
import com.example.shoplocalxml.R
import com.example.shoplocalxml.classes.image_downloader.DiffCallback

class SearchAdapter(private val items: MutableList<String>, private val onClickItem: (query: String, delete: Boolean) -> Unit): RecyclerView.Adapter<SearchAdapter.ViewHolder>(){

    var searchQuery: String = EMPTY_STRING
        set(value) {
            field = setSearchQuery(value)
        }
    private var filtered: Boolean = false
    private val showItems = items.toMutableList()

    @JvmName("setSearchQuery_")
    private fun setSearchQuery(value: String): String {
            filtered = value.isNotBlank()// ?: false
            val query = if (filtered) {
                swapData(items.filter {
                    it.contains(value, true)
                })
                value
            } else {
                swapData(items)
                EMPTY_STRING
            }
            notifyItemRangeChanged(0, itemCount)
            return query
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = showItems[position]
        holder.textItem.text = item
        val visibleDeleteButton = if (filtered) View.GONE else View.VISIBLE
        holder.deleteButton.visibility = visibleDeleteButton
        if (!filtered)
            holder.deleteButton.setOnClickListener {
                items.remove(item)
                swapData(items)
                onClickItem(item, true)
            }

        holder.layoutItem.setOnClickListener {
            onClickItem(item, false)
            setSearchQuery(item)
        }

    }

    private fun swapData(newData: List<String>){
        val diffCallback = DiffCallback(showItems, newData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        showItems.clear()
        showItems.addAll(newData)
        diffResult.dispatchUpdatesTo(this)
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
        val layoutItem: LinearLayout  = view.findViewById(R.id.layerHistorySearch)
    }
}