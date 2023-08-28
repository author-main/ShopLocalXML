package com.example.shoplocalxml.ui.image_viewer

import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.example.shoplocalxml.databinding.ActivityImageViewerBinding
import com.example.shoplocalxml.ui.image_viewer.recyclerview_image_viewer.ImageViewerAdapter
import com.example.shoplocalxml.ui.image_viewer.recyclerview_image_viewer.SelectedImagesAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class ImageViewerActivity : AppCompatActivity() {
    private lateinit var dataBinding: ActivityImageViewerBinding
    //private var onStackModeListener: OnStackModeListener? = null


    /*@SuppressLint("InlinedApi")
    private val hidePart2Runnable = Runnable {
        // Delayed removal of status and navigation bar
        if (Build.VERSION.SDK_INT >= 30) {
            fullscreenContent.windowInsetsController?.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
        } else {
            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            fullscreenContent.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LOW_PROFILE or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }
    }
    private val showPart2Runnable = Runnable {
        // Delayed display of UI elements
        supportActionBar?.show()
        fullscreenContentControls.visibility = View.VISIBLE
    }*/

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val typeToken = object : TypeToken<List<String>>() {}.type
        val gson    = Gson()
        val extraListImages    = intent.getStringExtra("listimages")
        val listImages = gson.fromJson<List<String>>(extraListImages, typeToken)
        val startIndex = intent.getIntExtra("startindex", 0)
        dataBinding = ActivityImageViewerBinding.inflate(layoutInflater)
        dataBinding.eventhandler = this
        setContentView(dataBinding.root)

        val adapter = ImageViewerAdapter(baseContext, listImages, startIndex)
        val manager = GridLayoutManager(baseContext, 1, GridLayoutManager.HORIZONTAL, false)
        dataBinding.recyclerViewImageViewer.layoutManager = manager
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(dataBinding.recyclerViewImageViewer)
        dataBinding.recyclerViewImageViewer.adapter = adapter
        val adapterSelectedImages = SelectedImagesAdapter(baseContext, listImages, startIndex)
        val managerSelectedImages = GridLayoutManager(baseContext, 1, GridLayoutManager.HORIZONTAL, false)
        dataBinding.recyclerViewSelectedImages.layoutManager = managerSelectedImages
        dataBinding.recyclerViewSelectedImages.adapter = adapterSelectedImages
        adapter.setOnChangeSelectedItem {
            adapterSelectedImages.selectItem(it)
        }
        adapterSelectedImages.setOnSelectItem {
            adapter.showItem(it)
        }




       /* val srcImage = listImages[startIndex]
        dataBinding.imageZoomView.setImageURI(srcImage.toUri())*/

        supportActionBar?.hide()//setDisplayHomeAsUpEnabled(true)
    }

    fun close() {
       // onStackModeListener?.onPopStackMode()
        finish()
    }

}