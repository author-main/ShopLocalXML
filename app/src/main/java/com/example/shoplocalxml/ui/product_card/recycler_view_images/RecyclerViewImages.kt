package com.example.shoplocalxml.ui.product_card.recycler_view_images

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplocalxml.EMPTY_BITMAP
import com.example.shoplocalxml.md5

class RecyclerViewImages(context: Context,
                         attrs: AttributeSet
                        )  : RecyclerView(context, attrs) {

    private val images = hashMapOf<String, Bitmap?>()
    fun setImages(list: List<String>) {
        list.forEach {
            images[md5(it)] = EMPTY_BITMAP
        }
    }
    fun updateImage(url: String, value: Bitmap?) {
        images[md5(url)] = value
    }

}