package com.example.shoplocalxml.ui.image_viewer.recyclerview_image_viewer

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplocalxml.classes.Review
import com.example.shoplocalxml.ui.detail_product.recyclerview_reviews.ReviewItem
import com.example.shoplocalxml.ui.detail_product.recyclerview_reviews.ReviewsAdapter

class SelectedImagesAdapter (val context: Context, private val linkImages: List<String> = listOf(), private var startIndex: Int, private val onSelectItem: (Int) -> Unit): RecyclerView.Adapter<SelectedImagesAdapter.ViewHolder>(){

    class ViewHolder(private val view: View, val onClick: (Int) -> Unit) : RecyclerView.ViewHolder(view) {
        fun bindItem(value: String, index: Int){
            val item = view as SelectedImageItem
            item.setItemIndex(index)
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
            onSelectItem(it)
        }
    }

    override fun onBindViewHolder(holder: SelectedImagesAdapter.ViewHolder, position: Int) {
        holder.bindItem(linkImages[position], position)
    }

    override fun getItemCount() =
        linkImages.count()

}