package com.example.shoplocalxml.ui.product_item.product_card

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.example.shoplocalxml.classes.Product
import com.example.shoplocalxml.databinding.ProductItemCardBinding
import com.example.shoplocalxml.log
import com.example.shoplocalxml.ui.product_item.product_card.recycler_view_images.ImagesAdapter
import com.example.shoplocalxml.ui.product_item.product_card.recycler_view_images.OnStateImagesListener


class ProductCard(context: Context,
                  attrs: AttributeSet)  : CardView(context, attrs) {
   /* var discount: Int = 0
        set(value) {
            field = value
            setDiscount(value)
        }*/

    var product = Product()
        set(value) {
            field = value
            setProduct(value)
        }

    private lateinit var dataBinding: ProductItemCardBinding
    private var onProductListener: OnProductListener? = null
    //private var recyclerViewImages: RecyclerViewImages? = null
    init {
        inflate(context, com.example.shoplocalxml.R.layout.product_item_card, this)
        setCardBackgroundColor(Color.TRANSPARENT)
    }

    @JvmName("setProduct_")
    private fun setProduct(value: Product?){
        value?.let{product ->
            setDiscount(product.discount)
            dataBinding.recyclerViewImages.setImages(product.linkimages)
        }
    }

    fun updateImage(url: String, bitmap: Bitmap?){
        dataBinding.recyclerViewImages.updateImage(url, bitmap)
    }

    fun setOnProductListener(value: OnProductListener) {
        onProductListener = value
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        dataBinding =
            DataBindingUtil.inflate(inflater, com.example.shoplocalxml.R.layout.product_item_card, this, true)
        val manager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        dataBinding.recyclerViewImages.layoutManager = manager
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(dataBinding.recyclerViewImages)
        dataBinding.recyclerViewImages.adapter = ImagesAdapter()
        dataBinding.recyclerViewImages.setOnStateImagesListener(object: OnStateImagesListener {
            override fun download() {
                dataBinding.imageViewProgress.startAnimation()
            }

            override fun uploaded() {
                dataBinding.imageViewProgress.stopAnimation()
            }

            override fun onClick(index: Int) {
                onProductListener?.onClick(product.id, index)
            }
        })


        dataBinding.imageFavorite.setOnCheckedListener {
            onProductListener?.onChangedFavorite(product.id, it)
        }

        dataBinding.buttonMore.setOnClickListener {
            onProductListener?.onShowMenu(product.id)
        }

        /*this.setOnClickListener {
            onProductListener?.onClick(product.id)
        }*/
    }

    @JvmName("setDiscount_")
    private fun setDiscount(value: Int) {
       val visibility = if (value == 0) View.GONE else View.VISIBLE
       dataBinding.textDiscount.visibility = visibility
       val discount =  "-${value.toString()}%"
       dataBinding.textDiscount.text = discount
    }


}