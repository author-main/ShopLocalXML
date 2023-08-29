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
import com.example.shoplocalxml.dagger.AppScope
import com.example.shoplocalxml.deleteFile
import com.example.shoplocalxml.getReduceImageHash
import com.example.shoplocalxml.log
import com.example.shoplocalxml.md5
import java.util.concurrent.Executors
import java.util.concurrent.Future
import javax.inject.Inject
@AppScope
class ImageDownloadManager @Inject constructor(
        private val cacheDrive  : ImageCacheDrive,
        private val cacheMemory : ImageCacheMemory
    ): DefaultLifecycleObserver {
    private var processClearTask = false
    private val executor =
        Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
    private val taskList: HashMap<String, Future<*>> = hashMapOf()
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

    fun download(url: String, reduce: Boolean, oncomplete: (Bitmap?)->Unit){
        val hash = getReduceImageHash(url, reduce)
        cacheMemory.get(hash)?.let{bitmap ->
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
        if (taskList.containsKey(url))
            queue.add(url to task)
        else
            taskList[url] = executor.submit(task)
    }

    fun cancelAll(){
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

    @Synchronized
    fun existCache(url: String): Boolean{
        val hash = md5(url)
        val existCacheMemory =  cacheMemory.get(hash) != null
        val existCacheDrive  =  cacheDrive.find(hash) != 0L
        return existCacheMemory || existCacheDrive
    }
}