package com.example.shoplocalxml.classes.image_downloader

import android.graphics.Bitmap
import java.util.concurrent.Callable

interface ImageDownloader<T>: Callable<T> {
    /**
     * Загрузить изображение
     * @param url ссылка на файл изображения на сервере
     * @param reduce уменьшать изображение для отображения
     * @param timestamp значение времени создания/изменения файла, используется для кеширования
     */

    fun download(): Bitmap?
}