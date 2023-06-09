package com.example.shoplocalxml.ui.product_item

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplocalxml.R
import com.example.shoplocalxml.classes.Product
import com.example.shoplocalxml.ui.product_item.product_card.recycler_view_images.ImageItem

class ProductsAdapter(private var products: List<Product> = listOf(), private var viewMode: ItemViewMode = ItemViewMode.CARD): RecyclerView.Adapter<ProductsAdapter.ViewHolder>(){

    private fun notifyItemsChanged(){
        notifyItemRangeChanged(0, products.size)
    }

    fun setProducts(list: List<Product>){
        products = list
        notifyItemsChanged()
    }

    fun setViewMode(value: ItemViewMode){
        viewMode = value
        notifyItemsChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount() =
        products.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        lateinit var item: Product
        fun bindItem(item: Product){
            this.item = item
        }
    }
    companion object {
        enum class ItemViewMode(value: Int) {
            CARD(0),
            ROW(1)
        }
    }

}
