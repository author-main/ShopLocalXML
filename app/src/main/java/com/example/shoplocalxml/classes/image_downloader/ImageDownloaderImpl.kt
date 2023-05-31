package com.example.shoplocalxml.classes.image_downloader

import android.graphics.Bitmap
import com.example.shoplocalxml.EMPTY_STRING
import com.example.shoplocalxml.EXT_TEMPFILE
import com.example.shoplocalxml.downloadImage
import com.example.shoplocalxml.fileNameFromPath
import com.example.shoplocalxml.getCacheDirectory
import com.example.shoplocalxml.loadBitmap
import com.example.shoplocalxml.md5
import com.example.shoplocalxml.renameFile
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

class ImageDownloaderImpl
    (   private val url: String = EMPTY_STRING,
        private val reduce: Boolean,
        private var timestamp: Long,
        private val onComplete: (Bitmap?, Long) -> Unit
    ): ImageDownloader {


    override fun download() {
        val result = downloadImage(url, reduce, timestamp)
        onComplete(result.first, result.second)
    }

    override fun run() {
        download()
    }

}