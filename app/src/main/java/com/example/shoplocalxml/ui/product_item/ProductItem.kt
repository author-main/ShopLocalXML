package com.example.shoplocalxml.ui.product_item

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.FrameLayout
import com.example.shoplocalxml.classes.Product
import com.example.shoplocalxml.ui.product_item.product_card.OnProductItemListener
import com.example.shoplocalxml.widthProductCard

class ProductItem: FrameLayout {
    var product = Product()
        set(value) {
            field = value
            setProduct(value)
        }
    var viewMode: ProductsAdapter.Companion.ItemViewMode =
        ProductsAdapter.Companion.ItemViewMode.CARD
        set(value) {
            val updateView = field != value
            field = value
            if (updateView) {
                dataBinding.changeMode(value)
                getDataBinding()
            }
        }

    //private lateinit var dataBinding: ProductItemCardBinding
    private val dataBinding = SwitchableDatabinding(parent = this)
    private var onProductItemListener: OnProductItemListener? = null

    init {
        getDataBinding()
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun setOnProductItemListener(value: OnProductItemListener?) {
        onProductItemListener = value
    }

    @JvmName("setProduct_")
    private fun setProduct(value: Product) {
        dataBinding.product = value
        dataBinding.productCard.product = value
    }

    private fun onClickProductItem(index: Int) {
        onProductItemListener?.onClick(product.id, index)
    }


    private fun getDataBinding() {
        if (viewMode == ProductsAdapter.Companion.ItemViewMode.CARD) {
            val layoutParams = dataBinding.productCard.layoutParams
            layoutParams.height = widthProductCard
            layoutParams.width = widthProductCard
            dataBinding.productCard.layoutParams = layoutParams
        }
        dataBinding.textPrice.paintFlags =
            dataBinding.textPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        // Обработка события добавления продукта в корзину
        dataBinding.buttonCart.setOnClickListener {
            onProductItemListener?.onAddCart(product.id)
        }
        // Обработка события изменения favorite продукта
        dataBinding.productCard.setOnChangedFavorite {
            onProductItemListener?.onChangedFavorite(product.id, it)
        }
        // Обработка события отобразить меню продукта
        dataBinding.productCard.setOnShowMenu {
            onProductItemListener?.onShowMenu(product.id)
        }
        // Обработка события клик по продукту
        dataBinding.productCard.setOnClick {
            onClickProductItem(it)
        }
        dataBinding.root.setOnClickListener {
            onClickProductItem(0)
        }
        dataBinding.productCard.reduceImage = true
        dataBinding.eventhandler = this
    }

    fun updateFavorite(value: Boolean) {
        dataBinding.productCard.updateFavorite(value)
    }
}


