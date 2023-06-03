package com.example.shoplocalxml.ui.product_card.recycler_view_images

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplocalxml.DIR_IMAGES
import com.example.shoplocalxml.EMPTY_BITMAP
import com.example.shoplocalxml.EMPTY_STRING
import com.example.shoplocalxml.SERVER_URL
import com.example.shoplocalxml.log
import com.example.shoplocalxml.md5

class RecyclerViewImages(context: Context,
                         attrs: AttributeSet
                        )  : RecyclerView(context, attrs) {

    private val adapter = ImagesAdapter()

   // private fun getFilenameMD5(value: String) = "$SERVER_URL/$DIR_IMAGES/${md5(value)}"

    fun setImages(value: List<String>?) {
        val images = hashMapOf<String, Bitmap?>()
        value?.let {list ->
            list.forEach {
                images[md5(it)] = EMPTY_BITMAP
            }
        }
        adapter.setImages(images)
    }

    fun updateImage(url: String, value: Bitmap?) {
        adapter.updateImage(md5(url), value)
    }

}