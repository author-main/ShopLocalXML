package com.example.shoplocalxml.ui.history_search

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplocalxml.AppShopLocal.Companion.applicationContext
import com.example.shoplocalxml.EMPTY_STRING
import com.example.shoplocalxml.R
import com.example.shoplocalxml.log

class SearchAdapter(private val items: MutableList<String>, private val onClickItem: (query: String, delete: Boolean) -> Unit): RecyclerView.Adapter<SearchAdapter.ViewHolder>(){

    var searchQuery: String = EMPTY_STRING
        set(value) {
            field = setSearchQuery(value)
        }

    var filtered: Boolean = false
    private val showItems = items.toMutableList()



    //val differ = AsyncListDiffer(this, DiffCallback())


    @JvmName("setSearchQuery_")
    private fun setSearchQuery(value: String): String {
            filtered = value.isNotBlank() ?: false
            val query = if (filtered) {

                swapData(items.filter {
                    it.contains(value, true)
                })
                /*showItems.clear()
                showItems.addAll(items.filter {
                    it.contains(value, true)
                })*/
                value
            } else {
                swapData(items)
                /*showItems.clear()
                showItems.addAll(items)*/
                EMPTY_STRING
            }
            notifyItemRangeChanged(0, itemCount)
            return query
    }

    /*fun clearHistory(){
        if (showItems.isNotEmpty()) {
            val count = showItems.size - 1
            items.clear()
            //showItems.clear()
            swapData(listOf())
            notifyItemRangeRemoved(0, count)
        }
    }*/

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = showItems[position]
        holder.textItem.text = item
        val visibleDeleteButton = if (filtered) View.GONE else View.VISIBLE
        holder.deleteButton.visibility = visibleDeleteButton
        if (!filtered)
            holder.deleteButton.setOnClickListener {
                //log("item $item")
                items.remove(item)

                //showItems.remove(item)
                swapData(items)

                onClickItem(item, true)

                /*notifyItemRemoved(position)
                notifyItemRangeChanged(position, itemCount)*/
            }

        holder.layoutItem.setOnClickListener {
            onClickItem(item, false)
            //notifyItemRangeRemoved(0, itemCount)
            setSearchQuery(item)
            //notifyItemRangeChanged(0, itemCount)
        }

    }


    private fun swapData(newData: List<String>){
        val diffCallback = SearchHistoryDiffCallback(showItems, newData)
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


    class SearchHistoryDiffCallback(private val oldList: List<String>, private val newList: List<String>) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] === newList[newItemPosition]
        }

        override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
            return oldList[oldPosition] == newList[newPosition]
        }

    }



}



/*class RatingDiffCallback(private val oldList: List<Rating>, private val newList: List<Rating>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].rateIndex === newList.get(newItemPosition).rateIndex
    }

    override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        val (_, value, name) = oldList[oldPosition]
        val (_, value1, name1) = newList[newPosition]

        return name == name1 && value == value1
    }

    @Nullable
    override fun getChangePayload(oldPosition: Int, newPosition: Int): Any? {
        return super.getChangePayload(oldPosition, newPosition)
    }
}*/

