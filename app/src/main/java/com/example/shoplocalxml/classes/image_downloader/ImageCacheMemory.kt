package com.example.shoplocalxml.classes.image_downloader

import android.graphics.Bitmap

interface ImageCacheMemory {
    fun put(hash: String, bitmap: Bitmap)
    fun get(hash: String): Bitmap?
}