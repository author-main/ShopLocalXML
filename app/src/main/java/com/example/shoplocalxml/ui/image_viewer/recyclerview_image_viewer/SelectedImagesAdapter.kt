package com.example.shoplocalxml.ui.image_viewer.recyclerview_image_viewer

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplocalxml.log

class SelectedImagesAdapter (val context: Context, private val linkImages: List<String> = listOf(), private var selectedIndex: Int): RecyclerView.Adapter<SelectedImagesAdapter.ViewHolder>(){
    private var recyclerView: RecyclerView? = null
    private var onSelectItem: ((Int) -> Unit)? = null
    fun setOnSelectItem (value: (Int) -> Unit){
        onSelectItem = value
    }

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
                unselectItem()
                selectedIndex = it
                onSelectItem?.invoke(it)
            }
        }
    }

    fun selectItem(index: Int) {
        unselectItem()
        recyclerView?.let{
            for ( i in 0..it.childCount) {
                val item = it.getChildAt(i) as SelectedImageItem
                if (item.changedSelectedIndex(index)) {
                    selectedIndex = index
                    break
                }
            }
        }
    }



    private fun unselectItem() {
        recyclerView?.let{
            for ( i in 0..it.childCount) {
                val item = it.getChildAt(i) as SelectedImageItem
                if (item.isSelectedItem(selectedIndex)) {
                    item.isSelected = false
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