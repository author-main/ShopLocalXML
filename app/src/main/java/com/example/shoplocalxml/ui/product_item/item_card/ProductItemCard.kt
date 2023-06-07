package com.example.shoplocalxml.ui.product_item.item_card

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.example.shoplocalxml.classes.Product
import com.example.shoplocalxml.databinding.ProductCardBinding
import com.example.shoplocalxml.databinding.ProductItemCardBinding
import com.example.shoplocalxml.ui.product_item.product_card.OnProductListener
import com.example.shoplocalxml.ui.product_item.product_card.recycler_view_images.ImagesAdapter
import com.example.shoplocalxml.ui.product_item.product_card.recycler_view_images.OnStateImagesListener

//        val normal: FontStyle = FontStyle(FONT_WEIGHT_NORMAL, FONT_SLANT_UPRIGHT)
    //         yourTextView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);

class ProductItemCard(
    context: Context,
    attrs: AttributeSet
    ) : FrameLayout(context, attrs) {
    var product = Product()
        set(value) {
            field = value
            setProduct(value)
        }
    private lateinit var dataBinding: ProductItemCardBinding

    @JvmName("setProduct_")
    private fun setProduct(value: Product){
        dataBinding.product = value
        dataBinding.product = value
        dataBinding.productCard.product = value
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        dataBinding =
            DataBindingUtil.inflate(inflater, com.example.shoplocalxml.R.layout.product_item_card, this, true)
        dataBinding.textPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        dataBinding.productCard.setOnProductListener(object : OnProductListener{
            override fun onChangedFavorite(id: Int, value: Boolean) {

            }

            override fun onClick(id: Int, index: Int) {

            }

            override fun onShowMenu(id: Int) {

            }
        })
        dataBinding.eventhandler = this

        //val manager = LinearLayoutManager(context)
        /*dataBinding.recyclerViewImages.layoutManager = manager
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
        }*/

        /*this.setOnClickListener {
            onProductListener?.onClick(product.id)
        }*/
    }

}



/*
<?xml version="1.0" encoding="utf-8"?>
<layer-list xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:drawable="@drawable/button_background"/>
    <item android:drawable="?attr/selectableItemBackground"/>
</layer-list>
 */