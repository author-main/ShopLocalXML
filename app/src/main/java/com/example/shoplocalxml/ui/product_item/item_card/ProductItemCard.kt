package com.example.shoplocalxml.ui.product_item.item_card

import android.R.attr
import android.R.attr.height
import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.example.shoplocalxml.classes.Product
import com.example.shoplocalxml.databinding.ProductItemCardBinding
import com.example.shoplocalxml.getDisplaySize
import com.example.shoplocalxml.log
import com.example.shoplocalxml.toDp
import com.example.shoplocalxml.toPx
import com.example.shoplocalxml.ui.product_item.product_card.OnProductItemListener
import com.example.shoplocalxml.widthProductCard


//        val normal: FontStyle = FontStyle(FONT_WEIGHT_NORMAL, FONT_SLANT_UPRIGHT)
    //         yourTextView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);

class ProductItemCard: FrameLayout {
    var product = Product()
        set(value) {
            field = value
            setProduct(value)
        }
    private lateinit var dataBinding: ProductItemCardBinding
    private var onProductItemListener: OnProductItemListener? = null

    init {
        getDataBinding()
    }

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    fun setOnProductItemListener(value: OnProductItemListener?){
        onProductItemListener = value
    }




    @JvmName("setProduct_")
    private fun setProduct(value: Product){
        //log(product.linkimages)
        dataBinding.product = value
        dataBinding.productCard.product = value
    }

    private fun onClickProductItem(index: Int){
        onProductItemListener?.onClick(product.id, index)
        //log("click index $index...")
    }


    private fun getDataBinding(){
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        dataBinding =
            DataBindingUtil.inflate(inflater, com.example.shoplocalxml.R.layout.product_item_card, this, true)

        //val widthCard = (getDisplaySize().width - (paddingProductCard * 3)) / 2
        val layoutParams = dataBinding.productCard.layoutParams
        layoutParams.height = widthProductCard
        layoutParams.width  = widthProductCard
        dataBinding.productCard.layoutParams = layoutParams

        dataBinding.textPrice.paintFlags = dataBinding.textPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        // Обработка события добавления продукта в корзину
        dataBinding.buttonCart. setOnClickListener {
            onProductItemListener?.onAddCart(product.id)
            //log("add cart...")
        }
        // Обработка события изменения favorite продукта
        dataBinding.productCard.setOnChangedFavorite {
            onProductItemListener?.onChangedFavorite(product.id, it)
            //log("favorite $it...")
        }
        // Обработка события отобразить меню продукта
        dataBinding.productCard.setOnShowMenu {
            onProductItemListener?.onShowMenu(product.id)
            //log("show menu...")
        }
        // Обработка события клик по продукту
        dataBinding.productCard.setOnClick {
            onClickProductItem(it)
        }
        dataBinding.root.setOnClickListener {
            onClickProductItem(0)
        }

        dataBinding.eventhandler = this
    }


   /* override fun onFinishInflate() {
        super.onFinishInflate()
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        dataBinding =
            DataBindingUtil.inflate(inflater, com.example.shoplocalxml.R.layout.product_item_card, this, true)
        dataBinding.textPrice.paintFlags = dataBinding.textPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        // Обработка события добавления продукта в корзину
        dataBinding.buttonCart. setOnClickListener {
            onProductItemListener?.onAddCart(product.id)
            log("add cart...")
        }
        // Обработка события изменения favorite продукта
        dataBinding.productCard.setOnChangedFavorite {
            onProductItemListener?.onChangedFavorite(product.id, it)
            log("favorite $it...")
        }
        // Обработка события отобразить меню продукта
        dataBinding.productCard.setOnShowMenu {
            onProductItemListener?.onShowMenu(product.id)
            log("show menu...")
        }
        // Обработка события клик по продукту
        dataBinding.productCard.setOnClick {
            onClickProductItem(it)
        }
        dataBinding.root.setOnClickListener {
            onClickProductItem(0)
        }

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
    }*/

    /*fun updateImage(url: String, bitmap: Bitmap?){
        dataBinding.productCard.updateImage(url, bitmap)
    }*/


    /*companion object {
        @BindingAdapter("app:count")
        fun setReviewCountStar(view: RatingView, count: Float) {
            view.setCount(count)
        }
    }*/
}



/*
<?xml version="1.0" encoding="utf-8"?>
<layer-list xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:drawable="@drawable/button_background"/>
    <item android:drawable="?attr/selectableItemBackground"/>
</layer-list>
 */