package com.example.shoplocalxml.ui.product_item.product_card.recycler_view_images

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplocalxml.AppShopLocal.Companion.applicationContext
import com.example.shoplocalxml.DIR_IMAGES
import com.example.shoplocalxml.EMPTY_STRING
import com.example.shoplocalxml.SERVER_URL
import com.example.shoplocalxml.log
import com.example.shoplocalxml.md5
import com.example.shoplocalxml.ui.history_search.SearchAdapter

class RecyclerViewImages(context: Context,
                         attrs: AttributeSet
                        )  : RecyclerView(context, attrs) {

    /*init {
        layoutManager = LinearLayoutManager(applicationContext)
        adapter = imagesAdapter
    }*/

    private var onStateImagesListener: OnStateImagesListener? = null
    fun setOnStateImagesListener (value: OnStateImagesListener){
        onStateImagesListener = value
    }

    fun setImages(value: List<String>?) {
        onStateImagesListener?.download()
        val images = mutableListOf<ImageItem>()
        value?.let {list ->
            list.forEach {
                images.add(ImageItem(md5(it), image=null))//EMPTY_BITMAP))
            }
        }
        (adapter as ImagesAdapter).setImages(images)
            // scrollToPosition(0)
    }

    fun updateImage(url: String, value: Bitmap?) {
        if ((adapter as ImagesAdapter).updateImage(md5(url), value))
            onStateImagesListener?.uploaded()
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        super.setAdapter(adapter)
        (adapter as ImagesAdapter).setOnClickItem {
            onStateImagesListener?.onClick(it)
        }
    }
}