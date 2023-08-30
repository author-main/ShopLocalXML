package com.example.shoplocalxml.ui.history_search

import com.example.shoplocalxml.AppShopLocal
import com.example.shoplocalxml.deleteFile
import com.example.shoplocalxml.fileExists
import java.io.*
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import javax.inject.Inject

class SearchQueryStorage @Inject constructor(): SearchQueryStorageInterface {
    private var changed = false
    private val listSQ = mutableListOf<String>()
    override val fileNameStorage: String =
        AppShopLocal.applicationContext.applicationInfo.dataDir + "/search.lst"

    override fun put(value: String) {
        if (value.isBlank())
            return
        if (!listSQ.contains(value)) {
            changed = true
            listSQ.add(0, value)
        } else
            moveFirst(value)
    }

    override fun remove(value: String){
        changed = listSQ.remove(value)
    }

    override fun getQueries(fromFile: Boolean): List<String> {
        val callable = Callable<List<String>> {
        if (fromFile) {
            listSQ.clear()
            if (fileExists(fileNameStorage)) {
                try {
                    BufferedReader(FileReader(fileNameStorage)).use {
                        it.lineSequence().forEach { line ->
                            listSQ.add(line)
                        }
                    }
                } catch (_: IOException) {
                }
            }
        }
        listSQ
    }
    return Executors.newSingleThreadExecutor().submit(callable).get()
    }

    override fun saveQueries(): Boolean {
        val callable = Callable {
            val text = StringBuffer()
            for (i in 0 until listSQ.size)
                text.append("${listSQ[i]}\n")
            try {
                FileOutputStream(File(fileNameStorage)).use {
                    it.write(text.toString().toByteArray())
                }
                true
            } catch(_:Exception) {
                false
            }
        }
        return Executors.newSingleThreadExecutor().submit(callable).get()
    }

    private fun moveFirst(value: String) {
            changed = true
            listSQ.remove(value)
            listSQ.add(0, value)
    }

    override fun removeAllQueries() {
        listSQ.clear()
        deleteFile(fileNameStorage)
    }
}
