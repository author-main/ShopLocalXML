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

class ImageViewerActivity : AppCompatActivity() {
    private lateinit var dataBinding: ActivityImageViewerBinding

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
        supportActionBar?.hide()//setDisplayHomeAsUpEnabled(true)
    }

    fun close() {
        finish()
    }
}