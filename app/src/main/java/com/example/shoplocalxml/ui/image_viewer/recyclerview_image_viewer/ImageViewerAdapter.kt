package com.example.shoplocalxml.ui.image_viewer.recyclerview_image_viewer

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplocalxml.custom_view.ZoomImageView

class ImageViewerAdapter (val context: Context, private val images: List<String> = listOf(), private val startIndex: Int = 0): RecyclerView.Adapter<ImageViewerAdapter.ViewHolder>(){
    private var handleEvent = true
    private var isScaledItem = false
    private var selectedIndex = 0
    // images: список изображений в cache (полный путь + hash)
    /*var startIndexItem: Int = 0
        set(value) {
            field = value
            setStartItem(value)
        }*/
    private var onChangeSelectedItem: ((Int) -> Unit)? = null
    fun setOnChangeSelectedItem(value: (Int) -> Unit) {
        onChangeSelectedItem = value
    }
    private var recyclerView: RecyclerView? = null
    @SuppressLint("ClickableViewAccessibility")
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
        selectedIndex = startIndex
        setVisibledItem()
       /* this.recyclerView!!.addOnItemTouchListener(object: OnItemTouchListener{
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                log(isScaledItem)
                rv.
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

            }
        })*/

        /*recyclerView.setOnTouchListener(OnTouchListener { _, _ ->
            log(isScaledItem)
            recyclerView.suppressLayout(isScaledItem)
            false
        }

        )*/

        recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            /*override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }*/

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!handleEvent) return
                val offset = recyclerView.computeHorizontalScrollOffset()
                val extent = recyclerView.computeHorizontalScrollExtent()
                if (offset % extent == 0) {
                    val index = offset / extent
                    if (index != selectedIndex) {
                        selectedIndex = index
                        onChangeSelectedItem?.invoke(index)
                    }
                }
            }
        })

    }

    fun showItem(index: Int){
        selectedIndex = index
        setVisibledItem(0)
    }

    private fun setVisibledItem(prevIndex: Int = -1) {
        handleEvent = false
        if (prevIndex != -1) {
           val item = recyclerView?.getChildAt(prevIndex) as ZoomImageView
          if (item.setOriginalScale()) {
              isScaledItem = false
              recyclerView?.suppressLayout(false)
          }
        }
        recyclerView?.scrollToPosition(selectedIndex)
        handleEvent = true
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ZoomImageView(context)
        return ViewHolder(view) {
            isScaledItem = it
            recyclerView?.suppressLayout(isScaledItem)
            //log(isScaledItem)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount() =
        images.count()

    class ViewHolder(private val view: View, val onScaleImage: (Boolean) -> Unit) : RecyclerView.ViewHolder(view) {
        fun bind(value: String) {
            val item = view as ZoomImageView
            val lparams = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            item.layoutParams = lparams
            item.setImageURI(value.toUri())
            item.setOnScaleImage {
                 onScaleImage(it)
            }
        }
    }

}