package com.example.shoplocalxml.classes.image_downloader

import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.example.shoplocalxml.CACHE_DIR
import com.example.shoplocalxml.EXT_TEMPFILE
import com.example.shoplocalxml.deleteFile
import com.example.shoplocalxml.getReduceImageHash
import com.example.shoplocalxml.log
import com.example.shoplocalxml.md5
import java.util.concurrent.Executors
import java.util.concurrent.Future

class ImageDownloadManager private constructor(): DefaultLifecycleObserver {
    private val cacheDrive  : ImageCacheDrive   = ImageCacheDriveImpl (MAX_DRIVE_CACHESIZE)
    private val cacheMemory : ImageCacheMemory  = ImageCacheMemoryImpl(MAX_MEMORY_CACHESIZE)
    private var processClearTask = false
    private val executor =
        Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
    private val taskList: HashMap<String, Future<*>> = hashMapOf() // * "звездная проекция, когда мы ничего не знаем о типе"
    private val queue = mutableListOf<Pair<String, ImageDownloader>>()
    private val handlerUI = Handler(Looper.getMainLooper())

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

    override fun onStop(owner: LifecycleOwner) {
        cancelAll()
        super.onStop(owner)
    }

    private fun download(url: String, reduce: Boolean, oncomplete: (Bitmap?)->Unit){
        val hash = getReduceImageHash(url, reduce)//if (!reduce) md5(url) else md5("${url}_")
     //   log("task = $url...")

        cacheMemory.get(hash)?.let{bitmap ->
            //log("load from cache memory...")

                handlerUI.post {
                    oncomplete(bitmap)
                }

            return
        }
        val cacheTimestamp = cacheDrive.find(hash)
        val task = ImageDownloaderImpl(url, reduce, cacheTimestamp){ bitmap: Bitmap?, timestamp: Long ->

                handlerUI.post {
                    oncomplete(bitmap)
                }

            taskList.remove(url)
            bitmap?.let{
                cacheMemory.put(hash, it)
            }
            cacheDrive.put(hash, timestamp)
            val indexTaskQueue = findTaskQueue(url)
            if (indexTaskQueue != -1) {
                val queueTask = queue[indexTaskQueue].second
                taskList[url] = executor.submit(queueTask)
                queue.removeAt(indexTaskQueue)
            }

        }

        //@Synchronized
        if (taskList.containsKey(url))
            queue.add(url to task)
        else
            taskList[url] = executor.submit(task)


    }

    private fun cancelAll(){
        if (processClearTask) return
        processClearTask = true
        queue.clear()
        val shutdownTask = taskList.isNotEmpty()
        val iterator = taskList.iterator()
        while (iterator.hasNext()){
            val task = iterator.next()
            val filenameTemp =  "$CACHE_DIR${md5(task.key)}.$EXT_TEMPFILE"
            task.value.cancel(true)
            iterator.remove()
            deleteFile(filenameTemp)
        }
        if (shutdownTask)
            executor.shutdown()
        processClearTask = false
    }

  /*  @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stopTaskDowload(){
        cancelAll()
    }*/

    private fun existCache(url: String): Boolean{
        val hash = md5(url)
        val existCacheMemory =  cacheMemory.get(hash) != null
        val existCacheDrive  =  cacheDrive.find(hash) != 0L
        return existCacheMemory || existCacheDrive
    }


    companion object {
        const val MAX_DRIVE_CACHESIZE  = 128
        const val MAX_MEMORY_CACHESIZE = 32
/*        private val instance:ImageDownloadManager by lazy {
            ImageDownloadManager()
        }*/

        private val instance =
            ImageDownloadManager()

        fun download(url: String, reduce: Boolean = false, oncomplete: (Bitmap?)->Unit) {
            instance.download(url, reduce, oncomplete)
        }

        fun cancelAll() {
            instance.cancelAll()
        }

        @Synchronized
        fun existCache(url: String) = instance.existCache(url)

        fun getInstance() = instance

    }
}