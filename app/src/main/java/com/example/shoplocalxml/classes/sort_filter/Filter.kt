package com.example.shoplocalxml.classes.sort_filter

import com.example.shoplocalxml.ANY_VALUE
import com.example.shoplocalxml.ui.product_item.ProductsAdapter

data class Filter(
    var viewmode: ProductsAdapter.Companion.ItemViewMode = ProductsAdapter.Companion.ItemViewMode.CARD,
    var enum: HashMap<Long, LongArray>   = hashMapOf(),
    var favorite: Boolean               = false,
    var fromPrice: Int                  = 0,
    var toPrice: Int                    = 0,
    //var priceRange: Pair<Int, Int>  = 0 to 0,
    var discount: Int                   = 0
) {

    override fun equals(other: Any?): Boolean {
        val filter = other as Filter
        return  equalsFilter(filter)
    }

    private fun equalsFilter(filter: Filter): Boolean {
        return  filter.equalsHashMap(enum)
                && filter.favorite          == favorite
                && filter.fromPrice         == fromPrice
                && filter.toPrice           == toPrice
                && filter.discount          == discount
                && filter.viewmode          == viewmode
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    private fun equalsHashMap(other: HashMap<Long, LongArray>): Boolean {
        if (other.isEmpty() && enum.isEmpty())
            return true

        if (other.size != enum.size)
            return false

        for (entry in other) {
            if (enum[entry.key] == null) {
                return false
            } else {
                  val arrayEnum = enum[entry.key]!!
                  if (arrayEnum.size != entry.value.size)
                      return false
                  for (entity in arrayEnum) {
                      if (entry.value.find { it == entity } == null) {
                          return false
                      }
                  }
            }
        }
        return true
    }

    fun changedViewMode(other: Filter) =
        viewmode != other.viewmode
}