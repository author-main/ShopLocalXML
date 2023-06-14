package com.example.shoplocalxml.ui.home

import com.example.shoplocalxml.SharedViewModel
import com.example.shoplocalxml.log
import com.example.shoplocalxml.ui.product_item.product_card.OnProductItemListener

class HandlerProductItemCard {
    companion object: OnProductItemListener {
        var sharedViewModel: SharedViewModel? = null
        /**
         * Обновление свойства favorite продукта
         * @param id идентификатор продукта
         * @param value значение свойства favorite
         */
        override fun onChangedFavorite(id: Int, value: Boolean) {
            sharedViewModel?.updateProductFavorite(id, value)
        }
        /**
         * Клик по карточке продукта
         * @param id идентификатор продукта
         * @param index индекс изображения продукта в списке
         */
        override fun onClick(id: Int, index: Int) {
            sharedViewModel?.let {viewModel->
            }
        }
        /**
         * Открыть нижнее меню продукта
         * @param id идентификатор продукта
         */
        override fun onShowMenu(id: Int) {
            sharedViewModel?.let {viewModel->
            }
        }
        /** Добавить продукт в корзину
        * @param id идентификатор продукта
        */
        override fun onAddCart(id: Int) {
            sharedViewModel?.let {viewModel->
            }
        }
    }
}