package com.example.shoplocalxml.classes.image_downloader

import android.graphics.Bitmap
import android.telecom.Call
import java.util.concurrent.Callable

interface ImageDownloader: Runnable {
    fun download()
    override fun run() {
        download()
    }
}