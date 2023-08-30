package com.example.shoplocalxml.ui.product_item.product_card.recycler_view_images

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplocalxml.AppShopLocal
import com.example.shoplocalxml.AppShopLocal.Companion.imageDownloadManager
import com.example.shoplocalxml.DEFAULT_BITMAP
import com.example.shoplocalxml.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class ImageItem(var url: String, var image: Bitmap?, var default: Boolean = false)

class ImagesAdapter: RecyclerView.Adapter<ImagesAdapter.ViewHolder>(){
    var reduceImage = false
    private var countUploaded = 0
    private var images: MutableList<ImageItem> = mutableListOf()
    private var onClickItem: ((index: Int) -> Unit)? = null
    private var onUploaded: (() -> Unit)? = null

    fun setOnUploaded(value: () -> Unit){
        onUploaded = value
    }

    @Synchronized
    @SuppressLint("NotifyDataSetChanged")
    fun setImages(list: List<String>){
        countUploaded = 0
        val listImages = mutableListOf<ImageItem>()
        list.forEach {
            listImages.add(ImageItem(it, image = null))
        }
        images = listImages
        CoroutineScope(Dispatchers.Main).launch {
            for (i in images.indices) {
                val item = images[i]
                imageDownloadManager.download(item.url, reduce = reduceImage) {
                    item.image = it ?: run {
                        item.default = true
                        DEFAULT_BITMAP
                    }
                    notifyItemChanged(i)
                    countUploaded += 1
                    if (countUploaded == images.size) {
                        onUploaded?.invoke()
                        notifyDataSetChanged()
                    }
                }
            }
        }
    }

    fun setOnClickItem(value: (index: Int) -> Unit){
        onClickItem = value
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(AppShopLocal.applicationContext).inflate(R.layout.product_images_item, parent, false)
        return ViewHolder(view, onClickItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(images[position])
    }

    override fun getItemCount() =
        images.size

    class ViewHolder(view: View, private val onClickItem: ((index: Int) -> Unit)?) : RecyclerView.ViewHolder(view) {
        private val imageItem: ImageView = view.findViewById(R.id.imageItem)
        fun bindItem(item: ImageItem){
            if (item.default)
                imageItem.scaleType = ImageView.ScaleType.CENTER_INSIDE
            imageItem.setImageBitmap(item.image)
            imageItem.setOnClickListener{
                onClickItem?.invoke(adapterPosition)
            }
        }
    }
}