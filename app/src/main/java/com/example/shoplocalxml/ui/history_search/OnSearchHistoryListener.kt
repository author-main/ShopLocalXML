package com.example.shoplocalxml.ui.history_search

interface OnSearchHistoryListener {
    fun clearSearchHistory()
    fun clickSearchHistoryItem(value: String)
    fun deleteSearchHistoryItem(value: String)
}