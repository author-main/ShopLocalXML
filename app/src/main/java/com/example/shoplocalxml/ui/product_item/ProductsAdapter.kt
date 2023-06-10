package com.example.shoplocalxml.ui.product_item

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplocalxml.AppShopLocal.Companion.applicationContext
import com.example.shoplocalxml.R
import com.example.shoplocalxml.classes.Product
import com.example.shoplocalxml.classes.image_downloader.DiffCallback
import com.example.shoplocalxml.ui.history_search.SearchAdapter
import com.example.shoplocalxml.ui.product_item.item_card.ProductItemCard
import com.example.shoplocalxml.ui.product_item.product_card.ProductCard
import com.example.shoplocalxml.ui.product_item.product_card.recycler_view_images.ImageItem

class ProductsAdapter(private var products: MutableList<Product> = mutableListOf(), private var viewMode: ItemViewMode = ItemViewMode.CARD): RecyclerView.Adapter<ProductsAdapter.ViewHolder>(){

    private fun notifyItemsChanged(){
        notifyItemRangeChanged(0, products.size)
    }


    fun setProducts(list: List<Product>){
        swapData(list)
        notifyItemsChanged()
    }

    fun setViewMode(value: ItemViewMode){
        viewMode = value
        notifyItemsChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ProductItemCard(applicationContext)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(products[position])
    }


    private fun swapData(newData: List<Product>){
        val diffCallback = DiffCallback(products, newData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        products.clear()
        products.addAll(newData)
        diffResult.dispatchUpdatesTo(this)
    }


    override fun getItemCount() =
        products.size

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        lateinit var item: Product
        fun bindItem(item: Product){
            this.item = item
            (view as ProductItemCard).product = item
        }
    }

    companion object {
        enum class ItemViewMode(value: Int) {
            CARD(0),
            ROW(1)
        }
    }

}
