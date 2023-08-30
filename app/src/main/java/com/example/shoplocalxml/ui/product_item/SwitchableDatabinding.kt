package com.example.shoplocalxml.ui.product_item

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.shoplocalxml.AppShopLocal
import com.example.shoplocalxml.classes.Product
import com.example.shoplocalxml.databinding.ProductItemCardBinding
import com.example.shoplocalxml.databinding.ProductItemRowBinding
import com.example.shoplocalxml.ui.product_item.product_card.ProductCard

class SwitchableDatabinding(private var mode: ProductsAdapter.Companion.ItemViewMode = ProductsAdapter.Companion.ItemViewMode.CARD, val parent: ViewGroup) {
    private var dataBindingCard: ProductItemCardBinding? = null
    private var dataBindingRow : ProductItemRowBinding? = null
    var product: Product = Product()
        set(value) {
            setProduct_(value)
        }
    val root: View
        get() = getRoot_()
    var eventhandler: ProductItem? = null
        set(value) {
            field = value
            dataBindingCard?.eventhandler = value
            dataBindingRow?.eventhandler  = value
        }

    init{
        getDataBindingCard()
    }

    private fun getDataBindingCard(){
        dataBindingRow?.let {
            parent.removeView(it.root)
        }
        val inflater = AppShopLocal.applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        dataBindingCard =
            DataBindingUtil.inflate(inflater, com.example.shoplocalxml.R.layout.product_item_card, parent, true)
    }

    private fun getDataBindingRow(){
        dataBindingCard?.let {
            parent.removeView(it.root)
        }
        val inflater = AppShopLocal.applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        dataBindingRow =
            DataBindingUtil.inflate(inflater, com.example.shoplocalxml.R.layout.product_item_row, parent, true)
    }


    fun changeMode(value: ProductsAdapter.Companion.ItemViewMode) {
        if (value != mode) {
            mode = value
            if (mode == ProductsAdapter.Companion.ItemViewMode.CARD)
                getDataBindingCard()
            else
                getDataBindingRow()

        }
    }

    private fun setProduct_(value: Product){
        if (mode == ProductsAdapter.Companion.ItemViewMode.CARD)
            dataBindingCard!!.product = value
        else
            dataBindingRow!!.product = value
    }
    val productCard     : ProductCard
        get() = getProductCard_()
    val buttonCart      : ImageView
        get() = getButtonCart_()
    val textPrice       : TextView
        get() = getTextPrice_()

    private fun getProductCard_() =
        if (mode == ProductsAdapter.Companion.ItemViewMode.CARD)
            dataBindingCard!!.productCard
        else
            dataBindingRow!!.productCard

    private fun getButtonCart_() =
        if (mode == ProductsAdapter.Companion.ItemViewMode.CARD)
            dataBindingCard!!.buttonCart
        else
            dataBindingRow!!.buttonCart

    private fun getTextPrice_() =
        if (mode == ProductsAdapter.Companion.ItemViewMode.CARD)
            dataBindingCard!!.textPrice
        else
            dataBindingRow!!.textPrice

    private fun getRoot_() =
        if (mode == ProductsAdapter.Companion.ItemViewMode.CARD)
            dataBindingCard!!.root
        else
            dataBindingRow!!.root
}