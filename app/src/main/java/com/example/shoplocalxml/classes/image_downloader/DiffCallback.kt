package com.example.shoplocalxml.classes.image_downloader

import androidx.recyclerview.widget.DiffUtil
import com.example.shoplocalxml.classes.Product

class DiffCallback<T>(private val oldList: List<T>, private val newList: List<T>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] === newList[newItemPosition]
    }

    override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        return oldList[oldPosition] == newList[newPosition]
    }

}