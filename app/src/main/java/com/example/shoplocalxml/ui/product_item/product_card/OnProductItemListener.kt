package com.example.shoplocalxml.ui.product_item.product_card

interface OnProductItemListener {
    fun onChangedFavorite(id: Int, value: Boolean)
    fun onClick(id: Int, index: Int)
    fun onShowMenu(id: Int)
    fun onAddCart(id: Int)
}