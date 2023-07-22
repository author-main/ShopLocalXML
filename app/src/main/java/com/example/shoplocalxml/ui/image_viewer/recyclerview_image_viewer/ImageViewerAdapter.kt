package com.example.shoplocalxml.ui.image_viewer.recyclerview_image_viewer

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplocalxml.custom_view.ZoomImageView

class ImageViewerAdapter (val context: Context, private val images: List<String> = listOf(), private val startIndex: Int = 0): RecyclerView.Adapter<ImageViewerAdapter.ViewHolder>(){
    // images: список изображений в cache (полный путь + hash)
    /*var startIndexItem: Int = 0
        set(value) {
            field = value
            setStartItem(value)
        }*/
    private var recyclerView: RecyclerView? = null
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
        setStartItem()
    }

    private fun setStartItem() {
        recyclerView?.scrollToPosition(startIndex)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ZoomImageView(context)
        return ImageViewerAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount() =
        images.count()

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(value: String) {
            val item = view as ZoomImageView
            val lparams = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            item.layoutParams = lparams
            item.setImageURI(value.toUri())
        }
    }

}