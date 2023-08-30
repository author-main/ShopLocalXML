package com.example.shoplocalxml.ui.product_item.product_card

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.cardview.widget.CardView
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.example.shoplocalxml.classes.Product
import com.example.shoplocalxml.databinding.ProductCardBinding
import com.example.shoplocalxml.ui.product_item.product_card.recycler_view_images.ImagesAdapter
import com.example.shoplocalxml.ui.product_item.product_card.recycler_view_images.OnChangeSelectedItem
import com.example.shoplocalxml.ui.product_item.product_card.recycler_view_images.OnStateImagesListener

@BindingMethods(
    value = [
        BindingMethod(
            type = ProductCard::class,
            attribute = "app:onClickImageItem",
            method = "setOnClickImageItem")]
)

class ProductCard: CardView {
    private var onClickImageItem: OnClickImageItem? = null

    interface OnClickImageItem {
        fun onClickImageItem(index: Int)
    }

    fun setOnClickImageItem(value: OnClickImageItem) {
        onClickImageItem = value
    }

    var reduceImage = false
       set(value)  {
           field = value
           (dataBinding.recyclerViewImages.adapter as ImagesAdapter).reduceImage = reduceImage
       }
    var visibleCardButton = true
    private var onChangedFavorite : ((value: Boolean) -> Unit)? = null
    private var onChangeSelectedItem: OnChangeSelectedItem? = null
    private var onClick:            ((index: Int)->Unit)? = null
    private var onShowMenu:         (()->Unit)? = null

    fun setOnChangeSelectedItem(value: OnChangeSelectedItem) {
        onChangeSelectedItem = value
    }

    fun setOnChangedFavorite (action: (value: Boolean) -> Unit){
        onChangedFavorite = action
    }

    fun setOnClick (action: (value: Int) -> Unit){
        onClick = action
    }

    fun setOnShowMenu (action: () -> Unit){
        onShowMenu = action
    }

    var product = Product()
        set(value) {
            field = value
            setProduct(value)
        }

    private lateinit var dataBinding: ProductCardBinding

    init {
        getDataBinding()
        setCardBackgroundColor(Color.TRANSPARENT)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun getRecyclerViewImages() = dataBinding.recyclerViewImages

    @JvmName("setProduct_")
    private fun setProduct(value: Product?) {
        dataBinding.product = value
        value?.let { product ->
            dataBinding.recyclerViewImages.setImages(product.linkimages)
            dataBinding.recyclerViewImages.setOnChangeSelectedItem {index ->
                onChangeSelectedItem?.onChangeItemIndex(index)
            }
        }
    }

    private fun getDataBinding(){
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        dataBinding =
            DataBindingUtil.inflate(inflater, com.example.shoplocalxml.R.layout.product_card, this, true)
        dataBinding.product = product
        dataBinding.eventhandler = this

        val manager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        dataBinding.recyclerViewImages.layoutManager = manager
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(dataBinding.recyclerViewImages)
        dataBinding.recyclerViewImages.adapter = ImagesAdapter()
        (dataBinding.recyclerViewImages.adapter as ImagesAdapter).reduceImage = reduceImage
        dataBinding.recyclerViewImages.setOnStateImagesListener(object: OnStateImagesListener {
            override fun download() {
                dataBinding.imageViewProgress.startAnimation()
            }

            override fun uploaded() {
                dataBinding.imageViewProgress.stopAnimation()
            }

            override fun onClick(index: Int) {
                onClick?.invoke(index)
                onClickImageItem?.onClickImageItem(index)
            }
        })
    }

    fun onClickMoreButton(){
        onShowMenu?.invoke()
    }

    fun onCheckedFavorite(value: Boolean) {
        onChangedFavorite?.invoke(value)
    }

    override fun onDetachedFromWindow() {
        dataBinding.imageViewProgress.stopAnimation()
        dataBinding.imageViewProgress.visibility = View.GONE
        super.onDetachedFromWindow()
    }

    fun updateFavorite(value: Boolean){
        dataBinding.imageFavorite.isChecked = value
    }
}