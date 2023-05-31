package com.example.shoplocalxml.classes.image_downloader

import android.graphics.Bitmap
import java.util.concurrent.Executors
import java.util.concurrent.Future

class ImageDownloadManager {
    private val executor =
        Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
    private val taskList: HashMap<String, Future<*>> = hashMapOf() // * "звездная проекция, когда мы ничего не знаем о типе"
    private fun download(url: String, reduce: Boolean, oncomplete: (Bitmap?, Long)->Unit){
        val cacheTimestamp = 0L
        val task = ImageDownloaderImpl(url, reduce, cacheTimestamp){ bitmap: Bitmap?, timestamp: Long ->
            taskList.remove(url)
            oncomplete(bitmap, timestamp)
        }
        if (!taskList.containsKey(url))
            taskList[url] = executor.submit(task)
    }
    private fun cancelAll(){
        val iterator = taskList.iterator()
        while (iterator.hasNext()){
            val task = iterator.next()
            task.value.cancel(true)
            iterator.remove()
        }
        executor.shutdown()
    }

    companion object {
        private lateinit var instance: ImageDownloadManager
        private fun getInstance(): ImageDownloadManager =
            if (this::instance.isInitialized)
                instance
            else
                ImageDownloadManager()

        fun download(url: String, reduce: Boolean = false, oncomplete: (Bitmap?, Long)->Unit) {
            getInstance().download(url, reduce, oncomplete)
        }


    }
}