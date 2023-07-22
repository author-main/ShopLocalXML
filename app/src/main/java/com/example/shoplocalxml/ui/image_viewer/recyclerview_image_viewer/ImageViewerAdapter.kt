package com.example.shoplocalxml.ui.image_viewer.recyclerview_image_viewer

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplocalxml.custom_view.ZoomImageView
import com.example.shoplocalxml.log

class ImageViewerAdapter (val context: Context, private val images: List<String> = listOf(), private val startIndex: Int = 0, val onChangeSelectedItem: (Int) -> Unit): RecyclerView.Adapter<ImageViewerAdapter.ViewHolder>(){
    private var isNotScaleItem = false
    private var selectedIndex = 0
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
        selectedIndex = startIndex
        recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val offset = recyclerView.computeHorizontalScrollOffset()
                val extent = recyclerView.computeHorizontalScrollExtent()
                if (offset % extent == 0) {
                    val index = offset / extent
                    if (index != selectedIndex) {
                        selectedIndex = index
                        onChangeSelectedItem(index)
                    }
                }
            }
        })

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

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {//, val onClick: (ZoomImageView) -> Unit) : RecyclerView.ViewHolder(view) {
        fun bind(value: String) {
            val item = view as ZoomImageView
            val lparams = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            item.layoutParams = lparams
            item.setImageURI(value.toUri())
           /* item.setOnClickListener {
                onClick(item)
            }*/
        }
    }

}