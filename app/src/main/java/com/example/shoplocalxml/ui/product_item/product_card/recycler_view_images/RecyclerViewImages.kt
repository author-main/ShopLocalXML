package com.example.shoplocalxml.ui.product_item.product_card.recycler_view_images

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplocalxml.classes.image_downloader.ImageDownloadManager
import com.example.shoplocalxml.log

class RecyclerViewImages(context: Context,
                         attrs: AttributeSet
                        )  : RecyclerView(context, attrs) {

    private var currentImageIndex = 0
    private var onChangeSelectedItem: ((index: Int) -> Unit)? = null
    fun setOnChangeSelectedItem(value: (index: Int) -> Unit){
        onChangeSelectedItem = value
    }
    /*private var onChangeSelectedItem: OnChangeSelectedItem? = null
    fun setOnChangeSelectedItem(value: OnChangeSelectedItem) {
        onChangeSelectedItem = value
    }*/
   init {
        addOnScrollListener(object: OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

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




    /*override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
            super.onScrollChanged(l, t, oldl, oldt)
            log("$l, $t, $oldl, $oldt")
        }*/

    fun setImages(value: List<String>?) {
        value?.let{
            var existCache = false
            for (url in it) {
                if (ImageDownloadManager.existCache(url)) {
                        //log("in cache...")
                        existCache = true
                        break
                    }
            }
            //ImageDownloadManager.existCache(url: String)
            (adapter as ImagesAdapter).setImages(it)
            if (!existCache)
                onStateImagesListener?.download()

        }

      /*  val images = mutableListOf<ImageItem>()
        value?.let {list ->
            list.forEach {
                images.add(ImageItem(it, image=null))//EMPTY_BITMAP))
            }
        }
        (adapter as ImagesAdapter).setImages(images)*/






    //CoroutineScope(Dispatchers.Main).launch {
         /*   images.forEach { item ->
                ImageDownloadManager.download(item.url) { bitmap ->
                    //(adapter as ImagesAdapter).updateImage(item.hash, bitmap)
                    updateImage(item.url, bitmap)
                }
            }*/
        //}



            // scrollToPosition(0)
    }

  /*  private fun updateImage(url: String, value: Bitmap?) {
        if ((adapter as ImagesAdapter).updateImage(url, value))
            onStateImagesListener?.uploaded()
    }*/

    override fun setAdapter(adapter: Adapter<*>?) {
        super.setAdapter(adapter)
        (adapter as ImagesAdapter).setOnClickItem {
            onStateImagesListener?.onClick(it)
        }
        (adapter as ImagesAdapter).setOnUploaded {
            onStateImagesListener?.uploaded()
        }
    }
}