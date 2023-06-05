package com.example.shoplocalxml.ui.product_card.recycler_view_images

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplocalxml.AppShopLocal
import com.example.shoplocalxml.DEFAULT_BITMAP
import com.example.shoplocalxml.EMPTY_STRING
import com.example.shoplocalxml.R
import com.example.shoplocalxml.log
import com.example.shoplocalxml.ui.history_search.SearchAdapter



data class ImageItem(var hash: String, var image: Bitmap?, var default: Boolean = false){}

class ImagesAdapter(): RecyclerView.Adapter<ImagesAdapter.ViewHolder>(){

    private var countUploaded = 0
    private var images: List<ImageItem> = listOf()
    private var onClickItem: ((index: Int) -> Unit)? = null

    @Synchronized
    fun updateImage(hash: String, value: Bitmap?): Boolean{
        //val bitmap = value //?: EMPTY_BITMAP
        for (i in images.indices) {
            if (images[i].hash == hash) {
                var default = false
                val bitmap = value ?: run{
                    default = true
                    DEFAULT_BITMAP
                }
                images[i].default = default
                images[i].image = bitmap//DEFAULT_BITMAP
                notifyItemChanged(i)
                break
            }
        }
        countUploaded += 1
        return countUploaded == itemCount
    }

    //@SuppressLint("NotifyDataSetChanged")
    fun setImages(value: List<ImageItem>){
        countUploaded = 0
        images = value
//        notifyDataSetChanged()
        notifyItemRangeChanged(0, images.size)
    }

    fun setOnClickItem(value: (index: Int) -> Unit){
        onClickItem = value
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(AppShopLocal.applicationContext).inflate(R.layout.product_images_item, parent, false)
        return ImagesAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (images[position].default)
            holder.imageItem.scaleType = ImageView.ScaleType.CENTER_INSIDE
        /*else
            holder.imageItem.scaleType = ImageView.ScaleType.FIT_CENTER*/
        holder.imageItem.setImageBitmap(images[position].image)

        holder.imageItem.setOnClickListener{
            onClickItem?.invoke(position)
        }

    }

    override fun getItemCount() =
        images.size


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageItem: ImageView = view.findViewById(R.id.imageItem)
    }
}