package com.example.shoplocalxml.ui.history_search

interface OnSearchItemClickListener {
    fun onClickItem(query: String)
    fun onDeleteItem(query: String?)
}