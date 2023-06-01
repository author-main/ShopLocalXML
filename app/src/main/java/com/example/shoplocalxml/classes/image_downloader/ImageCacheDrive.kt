package com.example.shoplocalxml.classes.image_downloader

interface ImageCacheDrive {
    val maxCacheSize: Int
    fun find (hash: String): Long
    fun put(hash: String, timestamp: Long)
}