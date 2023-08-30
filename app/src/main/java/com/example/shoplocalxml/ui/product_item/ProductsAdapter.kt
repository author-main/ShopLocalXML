package com.example.shoplocalxml.ui.product_item

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplocalxml.classes.Product
import com.example.shoplocalxml.classes.image_downloader.DiffCallback
import com.example.shoplocalxml.ui.product_item.product_card.OnProductItemListener

class ProductsAdapter(val context: Context, private var products: MutableList<Product> = mutableListOf(), private var viewMode: ItemViewMode = ItemViewMode.CARD): RecyclerView.Adapter<ProductsAdapter.ViewHolder>(){
    private var onProductItemListener: OnProductItemListener? = null

    fun setOnProductItemListener(value: OnProductItemListener){
        onProductItemListener = value
    }

    fun updateProductFavorite(id: Int, value: Boolean){
        val index = getProductPositionFromId(id)
        if (index != -1) {
            products[index].favorite = if (value) 1 else 0
            notifyItemChanged(index, value)
        }
    }

    fun setProducts(list: List<Product>){//, uploadAgain: Boolean = false) {
        swapData(list)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setViewMode(value: ItemViewMode){
        viewMode = value
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ProductItem(context)
        view.viewMode = viewMode
        return ViewHolder(view, onProductItemListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(products[position])
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.size > 0) {
            holder.updateFavoriteItem(payloads[0] as Boolean)
        } else
            onBindViewHolder(holder, position)
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

    class ViewHolder(private val view: View, private val onProductItemListener: OnProductItemListener?) : RecyclerView.ViewHolder(view) {
        fun bindItem(item: Product){
            val itemCard = view as ProductItem
            itemCard.product = item
            itemCard.setOnProductItemListener(onProductItemListener)
        }

        fun updateFavoriteItem(value: Boolean) {
            (view as ProductItem).updateFavorite(value)
        }
    }

    private fun getProductPositionFromId(id: Int) =
        products.indexOfFirst { it.id == id }

    companion object {
        enum class ItemViewMode {
            CARD,
            ROW
        }
    }
}
