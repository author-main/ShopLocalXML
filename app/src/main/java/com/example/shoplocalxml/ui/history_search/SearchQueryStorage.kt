package com.example.shoplocalxml.ui.history_search

import com.example.shoplocalxml.AppShopLocal
import com.example.shoplocalxml.deleteFile
import com.example.shoplocalxml.fileExists
import java.io.*
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton

/*enum class SearchState {
    SEARCH_NONE,
    SEARCH_QUERY,
    SEARCH_RESULT
}*/

//@Singleton
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

    /*override fun remove(index: Int){
        changed = listSQ.removeAt(index).isNotEmpty()
    }*/

    override fun remove(value: String){
        changed = listSQ.remove(value)
    }

    override fun getQueries(fromFile: Boolean): List<String> {

      /*  val items = mutableListOf<String>()
        items.add("Samsung")
        items.add("NVidia")
        items.add("AMD")
        items.add("Intel")
        items.add("AOC")
        items.add("Corsair")
        items.add("Kingstone")
        items.add("Xiaomi")
        listSQ.clear()
        listSQ.addAll(items)
        return listSQ*/

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
        val callable = Callable<Boolean> {
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

    /*fun dispose(){
        listSQ.clear()
    }*/

    override fun removeAllQueries() {
        listSQ.clear()
        deleteFile(fileNameStorage)
    }

  /*  companion object {
        private lateinit var instance: SearchQueryStorage
        fun getInstance(): SearchQueryStorage {
            if (!this::instance.isInitialized)
                instance = SearchQueryStorage()
            return instance
        }
    }*/
}
