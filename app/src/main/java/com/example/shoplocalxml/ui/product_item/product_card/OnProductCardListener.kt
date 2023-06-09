package com.example.shoplocalxml.ui.product_item.product_card

interface OnProductCardListener {
    fun onChangedFavorite(value: Boolean)
    fun onClick(index: Int)
    fun onShowMenu()

}