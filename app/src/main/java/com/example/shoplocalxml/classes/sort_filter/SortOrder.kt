package com.example.shoplocalxml.classes.sort_filter

enum class Sort(val value: Int) {PRICE(0), POPULAR(1), RATING(2)}
enum class Order(val value: Int) {ASCENDING(0), DESCENDING(1)}
data class SortOrder(var sort: Sort = Sort.PRICE, var order: Order = Order.ASCENDING) {
    fun invertOrder() {
        order = if (order == Order.ASCENDING)
            Order.DESCENDING
        else
            Order.ASCENDING
    }

    override fun equals(other: Any?): Boolean {
        val data = other as SortOrder
        return sort == data.sort && order == data.order
    }

    override fun hashCode(): Int {
        return 31 *  sort.hashCode() + order.hashCode()
    }
}