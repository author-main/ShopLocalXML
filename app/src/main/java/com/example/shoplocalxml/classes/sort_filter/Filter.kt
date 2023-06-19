package com.example.shoplocalxml.classes.sort_filter

import com.example.shoplocalxml.ANY_VALUE
import com.example.shoplocalxml.ui.product_item.ProductsAdapter

data class Filter(
    var viewmode: ProductsAdapter.Companion.ItemViewMode = ProductsAdapter.Companion.ItemViewMode.CARD,
    var brend: Int                      = ANY_VALUE,
    var favorite: Int                   = 0,
    var priceRange: Pair<Float, Float>  = 0f to 0f,
    var category: Int                   = ANY_VALUE,
    var discount: Int                   = 0
) {

    override fun equals(other: Any?): Boolean {
        val filter = other as Filter
        return  equalsFilter(filter)
    }

    private fun equalsFilter(filter: Filter): Boolean {
        return  filter.brend                == brend
                && filter.favorite          == favorite
                && filter.priceRange.first  == priceRange.first
                && filter.priceRange.second == priceRange.second
                && filter.category          == category
                && filter.discount          == discount
                && filter.viewmode          == viewmode
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}