package com.example.shoplocalxml.ui.product_item.product_card

interface OnProductListener {
    fun onChangedFavorite(value: Boolean)
    fun onClick(index: Int)
    fun onShowMenu()

}