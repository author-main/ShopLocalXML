package com.example.shoplocalxml.classes.image_downloader

import android.graphics.Bitmap
import android.util.LruCache
import com.example.shoplocalxml.dagger.MemoryCacheSize
import javax.inject.Inject

class ImageCacheMemoryImpl @Inject constructor(@MemoryCacheSize override val maxCacheSize: Int): ImageCacheMemory {
    private val MAX_CACHESIZE = maxCacheSize * 1024 * 1024
    private val cache = object: LruCache<String, Bitmap>(MAX_CACHESIZE){
        override fun sizeOf(hash: String, value: Bitmap): Int {
            return value.byteCount / 1024
        }
    }

    override fun put(hash: String, bitmap: Bitmap) {
        cache.put(hash, bitmap)
    }

    override fun get(hash: String): Bitmap? =
        cache.get(hash)

}