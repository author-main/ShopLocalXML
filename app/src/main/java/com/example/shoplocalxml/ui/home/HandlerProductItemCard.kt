package com.example.shoplocalxml.ui.home

import com.example.shoplocalxml.SharedViewModel
import com.example.shoplocalxml.log

class HandlerProductItemCard {
    companion object {
        lateinit var viewModel: SharedViewModel
        private fun viewModelInitialized() = this::viewModel.isInitialized
        fun updateProductFavorite(id: Int, value: Boolean) {
            if (!viewModelInitialized())
                return
            viewModel.updateProductFavorite(id, value)
        }

        fun clickProduct(id: Int, index: Int) {
            log("click...")
        }

        fun showProductMenu(id: Int) {
            log("show menu...")
        }

        fun addProductCart(id: Int) {
            log("add cart...")
        }
    }
}