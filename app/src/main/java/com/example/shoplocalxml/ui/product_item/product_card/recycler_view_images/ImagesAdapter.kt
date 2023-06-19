package com.example.shoplocalxml.ui.product_item.product_card.recycler_view_images

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.media.Image
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplocalxml.AppShopLocal
import com.example.shoplocalxml.DEFAULT_BITMAP
import com.example.shoplocalxml.R
import com.example.shoplocalxml.classes.image_downloader.ImageDownloadManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


data class ImageItem(var url: String, var image: Bitmap?, var default: Boolean = false){}

class ImagesAdapter(): RecyclerView.Adapter<ImagesAdapter.ViewHolder>(){
    //private val handlerUI = Handler(Looper.getMainLooper())
    private var countUploaded = 0
    private var images: MutableList<ImageItem> = mutableListOf()
    private var onClickItem: ((index: Int) -> Unit)? = null
    private var onUploaded: (() -> Unit)? = null

    fun setOnUploaded(value: () -> Unit){
        onUploaded = value
    }



  /*  @Synchronized
    fun updateImage(url: String, value: Bitmap?): Boolean{
        //val bitmap = value //?: EMPTY_BITMAP
        for (i in images.indices) {
            if (images[i].url == url) {
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
    }*/

    @SuppressLint("NotifyDataSetChanged")
    fun setImages(list: List<String>){
        images.clear()
        notifyDataSetChanged()
        countUploaded = 0
       // handlerUI.post {
        CoroutineScope(Dispatchers.Main).launch {
            for (i in list.indices) {
                val item = ImageItem(list[i], image = null)
                images.add(item)
                ImageDownloadManager.download(item.url) {
                    item.image = it ?: run {
                        item.default = true
                        DEFAULT_BITMAP
                    }
                    notifyItemChanged(i)
                    countUploaded += 1
                    if (countUploaded == images.size)
                        onUploaded?.invoke()
                }
            }

        }
      //  }
    }


  /*  fun setImages(value: List<ImageItem>){
        countUploaded = 0
        images = value
        notifyItemRangeChanged(0, images.size)
      /*  images.forEach {image ->
            ImageDownloadManager.download(image.url, reduce = true) {
                images
            }
        }*/
    }*/

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