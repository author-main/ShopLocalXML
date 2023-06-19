package com.example.shoplocalxml.ui.product_item

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplocalxml.AppShopLocal.Companion.applicationContext
import com.example.shoplocalxml.DATA_PORTION
import com.example.shoplocalxml.DIR_IMAGES
import com.example.shoplocalxml.R
import com.example.shoplocalxml.SERVER_URL
import com.example.shoplocalxml.classes.Product
import com.example.shoplocalxml.classes.image_downloader.DiffCallback
import com.example.shoplocalxml.log
import com.example.shoplocalxml.ui.history_search.SearchAdapter
import com.example.shoplocalxml.ui.product_item.item_card.ProductItemCard
import com.example.shoplocalxml.ui.product_item.product_card.OnProductItemListener
import com.example.shoplocalxml.ui.product_item.product_card.ProductCard
import com.example.shoplocalxml.ui.product_item.product_card.recycler_view_images.ImageItem

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

    private fun notifyItemsChanged(){
        notifyItemRangeChanged(0, products.size)
    }

    fun setProducts(list: List<Product>){//, uploadAgain: Boolean = false) {
        swapData(list)
        /*if (uploadAgain) {
            swapData(list)
            notifyItemsChanged()
        } else {
            val portions = (list.size - 1) / DATA_PORTION
            val startPosition = portions * DATA_PORTION
            val count = list.size - startPosition
            swapData(list)
            notifyItemRangeChanged(startPosition, count)
        }*/
    }

   /* fun setViewMode(value: ItemViewMode){
        viewMode = value
        notifyItemsChanged()
    }*/


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ProductItemCard(context)
        return ViewHolder(view, onProductItemListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(products[position])
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        //super.onBindViewHolder(holder, position, payloads)
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
        //lateinit var item: Product
        fun bindItem(item: Product){
          //  this.item = item
            val itemCard = view as ProductItemCard
            itemCard.product = item
            itemCard.setOnProductItemListener(onProductItemListener)
            /*with((view as ProductItemCard)) {
                this.product = item
            }*/

        }

        fun updateFavoriteItem(value: Boolean) {
            //log("favorite ${item.favorite}")
            (view as ProductItemCard).updateFavorite(value)
        }

    }

    private fun getProductPositionFromId(id: Int) =
        products.indexOfFirst { it.id == id }

    companion object {
        enum class ItemViewMode(value: Int) {
            CARD(0),
            ROW(1)
        }
    }

}
