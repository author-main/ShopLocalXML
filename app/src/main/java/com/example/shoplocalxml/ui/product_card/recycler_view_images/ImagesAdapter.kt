package com.example.shoplocalxml.ui.product_card.recycler_view_images

import android.graphics.Bitmap
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplocalxml.R
import com.example.shoplocalxml.ui.history_search.SearchAdapter

class ImagesAdapter(private val images: HashMap<String, Bitmap?>, private val onClickItem: (index: Int) -> Unit): RecyclerView.Adapter<ImagesAdapter.ViewHolder>(){

    fun updateImage(hash: String, value: Bitmap?){
        images[hash] = value
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount() =
        images.size


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }
}