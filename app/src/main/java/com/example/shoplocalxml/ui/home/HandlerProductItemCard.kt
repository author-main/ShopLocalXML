package com.example.shoplocalxml.ui.home

import android.view.MenuItem
import com.example.shoplocalxml.SharedViewModel
import com.example.shoplocalxml.log
import com.example.shoplocalxml.ui.product_item.product_card.OnProductItemListener

class HandlerProductItemCard {
    companion object {
        var sharedViewModel: SharedViewModel? = null
        /**
         * Обновление свойства favorite продукта
         * @param id идентификатор продукта
         * @param value значение свойства favorite
         */
        fun updateProductFavorite(id: Int, value: Boolean) {
            sharedViewModel?.updateProductFavorite(id, value)
        }
        /**
         * Клик по карточке продукта
         * @param id идентификатор продукта
         * @param index индекс изображения продукта в списке
         */
        fun clickProduct(id: Int, index: Int) {
            sharedViewModel?.let {viewModel->
            }
        }
        /**
         * Открыть нижнее меню продукта
         * @param id идентификатор продукта
         */
        fun showProductMenu(id: Int, action:(itemMenu: MenuItem)->Unit) {
            sharedViewModel?.let {viewModel->
            }
        }
        /**
         * Добавить продукт в корзину
         * @param id идентификатор продукта
         */
        fun addProductCart(id: Int) {
            sharedViewModel?.let {viewModel->
            }
        }
    }
}