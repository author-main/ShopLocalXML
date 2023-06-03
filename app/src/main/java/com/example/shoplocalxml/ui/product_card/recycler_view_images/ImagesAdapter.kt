package com.example.shoplocalxml.ui.product_card.recycler_view_images

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplocalxml.AppShopLocal
import com.example.shoplocalxml.R
import com.example.shoplocalxml.log
import com.example.shoplocalxml.ui.history_search.SearchAdapter


data class ImageItem(var hash: String, var image: Bitmap?){}


class ImagesAdapter(): RecyclerView.Adapter<ImagesAdapter.ViewHolder>(){

    private var images: List<ImageItem> = listOf()
    private var onClickItem: ((index: Int) -> Unit)? = null

    fun updateImage(hash: String, value: Bitmap?){
        for (i in images.indices) {
            if (images[i].hash == hash) {
                log("update item $i")
                images[i].image = value
                notifyItemChanged(i)
                break
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setImages(value: List<ImageItem>){
        images = value
        notifyDataSetChanged()
    }

    fun setOnClickItem(value: (index: Int) -> Unit){
        onClickItem = value
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(AppShopLocal.applicationContext).inflate(R.layout.product_images_item, parent, false)
        return ImagesAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imageItem.setImageBitmap(images[position].image)
    }

    override fun getItemCount() =
        images.size


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageItem: ImageView = view.findViewById(R.id.imageItem)
    }
}