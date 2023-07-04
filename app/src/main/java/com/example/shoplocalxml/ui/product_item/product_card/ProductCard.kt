package com.example.shoplocalxml.ui.product_item.product_card

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.example.shoplocalxml.AppShopLocal
import com.example.shoplocalxml.R
import com.example.shoplocalxml.classes.Product
import com.example.shoplocalxml.classes.image_downloader.ImageDownloadManager
import com.example.shoplocalxml.databinding.ProductCardBinding
import com.example.shoplocalxml.log
import com.example.shoplocalxml.ui.product_item.product_card.recycler_view_images.ImagesAdapter
import com.example.shoplocalxml.ui.product_item.product_card.recycler_view_images.OnChangeSelectedItem
import com.example.shoplocalxml.ui.product_item.product_card.recycler_view_images.OnStateImagesListener
import com.example.shoplocalxml.widthProductCard

class ProductCard: CardView {
   /* var discount: Int = 0
        set(value) {
            field = value
            setDiscount(value)
        }*/

    var visibleCardButton = true
        set(value) {
            field = value
            setVisibleButton(value)
        }
    private fun setVisibleButton(value: Boolean){
        dataBinding.buttonMore.visibility = View.GONE
        dataBinding.imageFavorite.visibility = View.GONE
        dataBinding.textDiscount.visibility = View.GONE
    }
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
    //private var onProductCardListener: OnProductCardListener? = null
    //private var recyclerViewImages: RecyclerViewImages? = null

    init {
        getDataBinding()
        setCardBackgroundColor(Color.TRANSPARENT)
    }

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}




    @JvmName("setProduct_")
    private fun setProduct(value: Product?) {
        dataBinding.product = value
        value?.let { product ->
            dataBinding.recyclerViewImages.setImages(product.linkimages)
            /*dataBinding.recyclerViewImages.setOnChangeSelectedItem(object: OnChangeSelectedItem {
                override fun onChangeItemIndex(index: Int) {
                    log("$index")
                }
            })*/

            dataBinding.recyclerViewImages.setOnChangeSelectedItem {index ->
                onChangeSelectedItem?.onChangeItemIndex(index)
            }


            //log(product.linkimages)
            /*product.linkimages?.forEach {url ->
                ImageDownloadManager.download(url) { bitmap ->
                    (dataBinding.recyclerViewImages.adapter as ImagesAdapter).updateImage(url, bitmap)
                }
            }*/
        }
    }

   /* private fun setFavorite(value: Boolean){
        dataBinding.imageFavorite.isChecked = value
    }*/

    /*fun updateImage(url: String, bitmap: Bitmap?){
        dataBinding.recyclerViewImages.updateImage(url, bitmap)
    }*/

    /*fun setOnProductListener(value: OnProductCardListener) {
        onProductCardListener = value
    }*/


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
        dataBinding.recyclerViewImages.setOnStateImagesListener(object: OnStateImagesListener {
            override fun download() {
                dataBinding.imageViewProgress.startAnimation()
            }

            override fun uploaded() {
                //log("images uploaded ${product.id}...")
                dataBinding.imageViewProgress.stopAnimation()
            }

            override fun onClick(index: Int) {
                onClick?.invoke(index)
                //onProductCardListener?.onClick(index)
            }
        })



    }


    /*override fun onFinishInflate() {
        super.onFinishInflate()
/*        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
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
        dataBinding.recyclerViewImages.setOnStateImagesListener(object: OnStateImagesListener {
            override fun download() {
                dataBinding.imageViewProgress.startAnimation()
            }

            override fun uploaded() {
                dataBinding.imageViewProgress.stopAnimation()
            }

            override fun onClick(index: Int) {
                onClick?.invoke(index)
                //onProductCardListener?.onClick(index)
            }
        })*/

/*        dataBinding.imageFavorite.setOnCheckedListener {
            onProductListener?.onChangedFavorite(product.id, it)
        }

        dataBinding.imageFavorite.setOnCheckedListener( object: CheckableImageView.OnCheckedListener {
            override fun onChecked(value: Boolean) {
                onProductListener?.onChangedFavorite(product.id, value)
            }
        }
        )


         dataBinding.buttonMore.setOnClickListener {
            onProductListener?.onShowMenu(product.id)
        }*/

    }

*/

  /*  @JvmName("setDiscount_")
    private fun setDiscount(value: Int) {
       val visibility = if (value == 0) View.GONE else View.VISIBLE
       dataBinding.textDiscount.visibility = visibility
       val discount =  "-${value.toString()}%"
       dataBinding.textDiscount.text = discount
    }*/


    fun onClickMoreButton(){
        onShowMenu?.invoke()
        //onProductCardListener?.onShowMenu()
    }

    fun onCheckedFavorite(value: Boolean) {
        onChangedFavorite?.invoke(value)
        //onProductCardListener?.onChangedFavorite(value)
    }

    override fun onDetachedFromWindow() {
        dataBinding.imageViewProgress.stopAnimation()
        dataBinding.imageViewProgress.visibility = View.GONE
        super.onDetachedFromWindow()
        //log("detached")
    }

    fun updateFavorite(value: Boolean){
        dataBinding.imageFavorite.isChecked = value
    }
}

//app:onChecked="@{(checked)->eventhandler.onCheckedFavorite(checked)}"