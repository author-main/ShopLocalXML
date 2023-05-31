package com.example.shoplocalxml.classes.image_downloader

import android.graphics.Bitmap
import com.example.shoplocalxml.EXT_TEMPFILE
import com.example.shoplocalxml.deleteFile
import com.example.shoplocalxml.getCacheDirectory
import com.example.shoplocalxml.log
import com.example.shoplocalxml.md5
import java.util.Queue
import java.util.concurrent.Executors
import java.util.concurrent.Future

class ImageDownloadManager private constructor() {
    private val executor =
        Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
    private val taskList: HashMap<String, Future<*>> = hashMapOf() // * "звездная проекция, когда мы ничего не знаем о типе"
    private val queue = mutableListOf<Pair<String, ImageDownloader>>()
    private fun findTaskQueue(url: String): Int{
        var indexFoundTask = -1
        var i = queue.size - 1
        while (i>=0) {
            if (queue[i].first == url) {
                indexFoundTask = i
                break
            }
            i -= 1
        }
        return indexFoundTask
    }
    private fun download(url: String, reduce: Boolean, oncomplete: (Bitmap?, Long)->Unit){
        val cacheTimestamp = 0L

        log ("start download $url...")
        val task = ImageDownloaderImpl(url, reduce, cacheTimestamp){ bitmap: Bitmap?, timestamp: Long ->
            //taskList.remove(url)
            oncomplete(bitmap, timestamp)

            val indexTaskQueue = findTaskQueue(url)
            if (indexTaskQueue != -1) {
                log("add task from queue...")
                val queueTask = queue[indexTaskQueue].second
                taskList[url] = executor.submit(queueTask)
                queue.removeAt(indexTaskQueue)
            }

        }

        if (taskList.containsKey(url)) {
            log("add queue...")
            queue.add(url to task)
        }
        else {
            log("add task...")
            taskList[url] = executor.submit(task)
            var map = ""
            val iterator = taskList.iterator()
            while (iterator.hasNext()) {
                map = "$map, ${iterator.next().key}"
            }
            log("map = $map")
        }
    }
    private fun cancelAll(){
        queue.clear()
        val iterator = taskList.iterator()
        while (iterator.hasNext()){
            val task = iterator.next()
            val filenameTemp =  getCacheDirectory() + md5(task.key) + EXT_TEMPFILE
            task.value.cancel(true)
            iterator.remove()
            deleteFile(filenameTemp)
        }
        executor.shutdown()
    }

    companion object {
        private val instance:ImageDownloadManager by lazy {
            ImageDownloadManager()
        }

        /*private lateinit var instance: ImageDownloadManager
        private fun getInstance(): ImageDownloadManager =
            if (this::instance.isInitialized)
                instance
            else {
                log ("recreate...")
                ImageDownloadManager()
            }*/

        fun download(url: String, reduce: Boolean = false, oncomplete: (Bitmap?, Long)->Unit) {
            instance.download(url, reduce, oncomplete)
        }

        fun cancelAll() {
            instance.cancelAll()
        }


    }
}