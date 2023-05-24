package com.example.shoplocalxml.ui.history_search

data class SearchItem(var query: String, var deleted: Boolean = false) {
    override fun equals(other: Any?): Boolean {
        if (other == null || other !is SearchItem)
            return false
        return query == other.query && deleted == other.deleted
    }

    override fun hashCode(): Int {
        return 31 * query.hashCode()
    }
    fun deleteItem(){
        deleted = true
    }
}