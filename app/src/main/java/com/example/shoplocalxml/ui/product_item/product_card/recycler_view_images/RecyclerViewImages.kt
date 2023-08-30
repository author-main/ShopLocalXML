package com.example.shoplocalxml.ui.product_item.product_card.recycler_view_images

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplocalxml.AppShopLocal.Companion.imageDownloadManager

class RecyclerViewImages(context: Context,
                         attrs: AttributeSet
                        )  : RecyclerView(context, attrs) {
    private var currentImageIndex = 0
    private var onChangeSelectedItem: ((index: Int) -> Unit)? = null
    fun setOnChangeSelectedItem(value: (index: Int) -> Unit){
        onChangeSelectedItem = value
    }

   init {
        addOnScrollListener(object: OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val offset = computeHorizontalScrollOffset()
                val extent = computeHorizontalScrollExtent()
                if (offset % extent == 0) {
                    val index = offset / extent
                    if (index != currentImageIndex) {
                        currentImageIndex = index
                        onChangeSelectedItem?.invoke(index)
                    }
                }
            }
        })
    }

    private var onStateImagesListener: OnStateImagesListener? = null
    fun setOnStateImagesListener (value: OnStateImagesListener){
        onStateImagesListener = value
    }

    fun setImages(value: List<String>?) {
        value?.let {
            var existCache = false
            for (url in it) {
                if (imageDownloadManager.existCache(url)) {
                    existCache = true
                    break
                }
            }
            (adapter as ImagesAdapter).setImages(it)
            if (!existCache)
                onStateImagesListener?.download()
        }
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        super.setAdapter(adapter)
        (adapter as ImagesAdapter).setOnClickItem {
            onStateImagesListener?.onClick(it)
        }
        adapter.setOnUploaded {
            onStateImagesListener?.uploaded()
        }
    }
}