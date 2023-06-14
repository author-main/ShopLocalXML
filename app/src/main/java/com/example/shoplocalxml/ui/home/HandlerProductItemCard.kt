package com.example.shoplocalxml.ui.home

import com.example.shoplocalxml.SharedViewModel
import com.example.shoplocalxml.log

class HandlerProductItemCard {
    companion object {
        lateinit var viewModel: SharedViewModel
        private fun viewModelInitialized() = this::viewModel.isInitialized

        /**
         * Обновление свойства favorite продукта
         * @param id идентификатор продукта
         * @param value значение свойства favorite
         */
        fun updateProductFavorite(id: Int, value: Boolean) {
            if (!viewModelInitialized())
                return
            viewModel.updateProductFavorite(id, value)
        }
        /**
         * Клик по карточке продукта
         * @param id идентификатор продукта
         * @param index индекс изображения продукта в списке
         */
        fun clickProduct(id: Int, index: Int) {
            if (!viewModelInitialized())
                return
        }
        /**
         * Открыть нижнее меню продукта
         * @param id идентификатор продукта
         */
        fun showProductMenu(id: Int) {
            if (!viewModelInitialized())
                return
        }
        /**
         * Добавить продукт в корзину
         * @param id идентификатор продукта
         */
        fun addProductCart(id: Int) {
            if (!viewModelInitialized())
                return
        }
    }
}