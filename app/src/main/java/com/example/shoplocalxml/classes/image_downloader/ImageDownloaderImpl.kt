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

class ImageDownloaderImpl
    (   private val url: String = EMPTY_STRING,
        private val reduce: Boolean,
        private var timestamp: Long,
        private val action: (bitmap: Bitmap?, timestamp: Long) -> Unit
    ): ImageDownloader<Bitmap?>{

    override fun download(): Bitmap? {
        val result = downloadImage(url, reduce, timestamp)
        action(result.first, result.second)
        return result.first
     /*   val bufferSize = 32768
        val fileName = fileNameFromPath(url)
        val fileHash = md5(fileName)
        val filenameCache = getCacheDirectory() + fileHash
        val filenameTemp  = "$filenameCache.$EXT_TEMPFILE"
        var bitmap: Bitmap? = null
        var success = false
        val conn = URL(url).openConnection() as HttpURLConnection
        try {
            conn.connect()
            if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                conn.requestMethod = "HEAD"
                val fileTime = conn.lastModified
                if (timestamp != fileTime) {
                    timestamp = fileTime
                    conn.requestMethod = "GET"
                    val inputStream = conn.inputStream
                    val outputStream = FileOutputStream(filenameTemp)
                    val buffer = ByteArray(bufferSize)
                    var count: Int
                    while (inputStream.read(buffer).also { count = it } > 0) {
                        outputStream.write(buffer, 0, count)
                    }
                    inputStream.close()
                    outputStream.flush()
                    outputStream.close()
                    renameFile(filenameTemp, filenameCache)
                    bitmap = loadBitmap(filenameCache, reduce)
                    success = true
                }
            }
        } catch (_: Exception) {
        } finally {
            conn.disconnect()
        }
        if (!success)
            bitmap = loadBitmap(filenameCache, reduce)
        action(bitmap, timestamp)
        return bitmap*/
    }

    override fun call(): Bitmap? =
        download()
}