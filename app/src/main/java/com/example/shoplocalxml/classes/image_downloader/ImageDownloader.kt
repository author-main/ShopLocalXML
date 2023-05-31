package com.example.shoplocalxml.classes.image_downloader

import android.graphics.Bitmap
import java.util.concurrent.Callable

interface ImageDownloader: Runnable {
    fun download()
}