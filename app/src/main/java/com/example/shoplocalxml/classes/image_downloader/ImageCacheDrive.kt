package com.example.shoplocalxml.classes.image_downloader

interface ImageCacheDrive {
    fun find (hash: String): Long
    fun put(hash: String, timestamp: Long)
}