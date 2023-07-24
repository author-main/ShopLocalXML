package com.example.shoplocalxml.ui.image_viewer.recyclerview_image_viewer

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplocalxml.log

class SelectedImagesAdapter (val context: Context, private val linkImages: List<String> = listOf(), private var selectedIndex: Int, private val onSelectItem: (Int) -> Unit): RecyclerView.Adapter<SelectedImagesAdapter.ViewHolder>(){
    var recyclerView: RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    inner class ViewHolder(val view: View, val onClick: (Int) -> Unit) : RecyclerView.ViewHolder(view) {
        fun bindItem(value: String, index: Int){
            val item = view as SelectedImageItem
            item.setItemIndex(index)
            if (index == selectedIndex)
                item.isSelected = true
            item.linkImage = value
            item.setOnCLickListener {
                onClick(it)
            }
            item.setCardBackgroundColor(Color.TRANSPARENT)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SelectedImagesAdapter.ViewHolder {
        val view = SelectedImageItem(context)
        return ViewHolder(view) {
            if (it != selectedIndex) {
                unselectItem(selectedIndex)
                selectedIndex = it
                onSelectItem(it)
                //log("selected $it...")
            }
        }
    }

    private fun unselectItem(value: Int) {
        recyclerView?.let{
            for ( i in 0..it.childCount) {

                val item = it.getChildAt(i) as SelectedImageItem


                /*val holder =
                    it.getChildViewHolder(it.getChildAt(i)) as ViewHolder
                val item = holder.view as SelectedImageItem*/
                if (item.isSelectedItem(selectedIndex)) {
                    item.isSelected = false
                    //notifyItemChanged(i)
                    break
                }
            }

        }
    }

    override fun onBindViewHolder(holder: SelectedImagesAdapter.ViewHolder, position: Int) {
        holder.bindItem(linkImages[position], position)
    }

    override fun getItemCount() =
        linkImages.count()

}