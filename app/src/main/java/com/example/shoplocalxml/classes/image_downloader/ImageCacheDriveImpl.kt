package com.example.shoplocalxml.classes.image_downloader

import com.example.shoplocalxml.CACHE_DIR
import com.example.shoplocalxml.EXT_TEMPFILE
import com.example.shoplocalxml.dagger.DriveCacheSize
import com.example.shoplocalxml.deleteFile
import com.example.shoplocalxml.deleteFiles
import com.example.shoplocalxml.fileExists
import com.example.shoplocalxml.getFileSize
import com.example.shoplocalxml.log
import com.example.shoplocalxml.renameFile
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import java.io.IOException
import javax.inject.Inject

data class ImageCacheItem (var timestamp: Long, var size: Long)

class ImageCacheDriveImpl @Inject constructor(@DriveCacheSize override val maxCacheSize: Int): ImageCacheDrive {
    private val fileJournal         = File(CACHE_DIR + "imgcache.lst")
    private val fileJournalTemp     = File(CACHE_DIR + "imgcache.tmp")
    private val fileJournalBackup   = File(CACHE_DIR + "imgcache.bck")
    private var cacheSize = 0L
    private val MAX_CACHESIZE = maxCacheSize * 1024 * 1024 // 128Mb максимальный размер кэша на устройстве
    private val hashMap = LinkedHashMap<String, ImageCacheItem>(0, 0.75f, true)

    init {
        getJournalItems()
    }

    private fun getJournalItems() {
        hashMap.clear()
        deleteFiles(CACHE_DIR, EXT_TEMPFILE)
        if (!fileExists(fileJournal)) {
            if (fileExists(fileJournalBackup))
                renameFile(fileJournalBackup, fileJournal)
        }
        if (!fileExists(fileJournal)) {
            deleteCacheFiles()
            return
        }
        val fileText = StringBuffer()
        try {
            BufferedReader(FileReader(fileJournal)).use {
                it.lineSequence().forEach { line ->
                    val item = getItemFromStr(line)
                    item?.let {pair ->
                        hashMap[pair.first] = pair.second
                    }
                }
            }
            cacheSize = getCacheSize()
        } catch(_: IOException) {

        }

    }

    private fun itemToStr(hash: String, timestamp: Long) =
        "$hash $timestamp\n"


    private fun getItemFromStr(value: String):Pair<String, ImageCacheItem>?{
        return try {
            val data = value.split(' ')
            val hash = data[0]
            val filesize = getFileSize(getFilename(hash))
            if (filesize == 0L) throw Exception()
            val timestamp = data[1].toLong()
            hash to ImageCacheItem(timestamp, filesize)
        } catch (_: Exception) {
            null
        }
    }

    private fun deleteCacheFiles(){
        cacheSize = 0L
        deleteFiles(CACHE_DIR)
    }

    @Synchronized
    override fun put(hash: String, timestamp: Long) {
        //log("put journal item...")
        val filesize = getFileSize(getFilename(hash))
        if (filesize > 0) {
            hashMap[hash]?.let{
                if (it.timestamp != timestamp) {
                    cacheSize -= it.size
                    hashMap.remove(hash)
                } else
                    return
            }
            var calcCacheSize = cacheSize + filesize
            if (calcCacheSize > MAX_CACHESIZE) {
                val iterator = hashMap.iterator()
                var reduceCacheSize = calcCacheSize
                while (iterator.hasNext()) {
                    val item = iterator.next()
                    reduceCacheSize -= item.value.size
                    deleteFile(getFilename(item.key))
                    iterator.remove()
                    if (reduceCacheSize < MAX_CACHESIZE) {
                        calcCacheSize = reduceCacheSize
                        break
                    }
                }
            }
            cacheSize = calcCacheSize
            hashMap[hash] = ImageCacheItem(timestamp, filesize)
            saveJournalItems()
        } else {
            hashMap[hash]?.let{
                cacheSize -= it.size
                hashMap.remove(hash)
                deleteFile(getFilename(hash))
                saveJournalItems()
            }
        }
        //log("put journal item...")
    }

    private fun getFilename(value: String)  = "$CACHE_DIR$value"

    private fun saveJournalItems(){
        val fileText = StringBuffer()
        hashMap.forEach{item ->
            fileText.append(itemToStr(item.key, item.value.timestamp))
        }
        if (fileText.isNotEmpty()) {
            try {
                FileOutputStream(fileJournalTemp).use {
                    it.write(fileText.toString().toByteArray())
                }
                renameFile(fileJournal, fileJournalBackup)
                renameFile(fileJournalTemp, fileJournal)
            } catch(_:Exception){}
        } else
            deleteCacheFiles()
    }

    private fun getCacheSize(): Long {
        val iterator = hashMap.iterator()
        var size = 0L
        while (iterator.hasNext()) {
            val item = iterator.next()
            //item.value.size = getFileSize(getFilename(item.key))
            size += item.value.size
        }
        return size
    }

    override fun find(hash: String) =
        hashMap[hash]?.timestamp ?: 0L

}