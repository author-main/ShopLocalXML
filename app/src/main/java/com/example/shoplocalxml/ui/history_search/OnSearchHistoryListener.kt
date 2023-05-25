package com.example.shoplocalxml.ui.history_search

interface OnSearchHistoryListener {
    fun clearHistory()
    fun clickItem(value: String)
    fun deleteItem(value: String)
}