package com.example.shoplocalxml.ui.detail_product

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.graphics.Paint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.shoplocalxml.AppShopLocal
import com.example.shoplocalxml.EMPTY_STRING
import com.example.shoplocalxml.FRIDAY_PERCENT
import com.example.shoplocalxml.FactoryViewModel
import com.example.shoplocalxml.R
import com.example.shoplocalxml.SharedViewModel
import com.example.shoplocalxml.WORD_RATE
import com.example.shoplocalxml.classes.Product
import com.example.shoplocalxml.classes.Review
import com.example.shoplocalxml.databinding.FragmentDetailProductBinding
import com.example.shoplocalxml.getAfterWord
import com.example.shoplocalxml.getFormattedFloat
import com.example.shoplocalxml.getStringArrayResource
import com.example.shoplocalxml.isLastFriday
import com.example.shoplocalxml.log
import com.example.shoplocalxml.ui.product_item.product_card.recycler_view_images.OnChangeSelectedItem
import kotlinx.coroutines.launch
import java.util.Calendar


class DetailProductFragment : Fragment(), OnDetailContentListener {
    private val sharedViewModel: SharedViewModel by activityViewModels(factoryProducer = {
        FactoryViewModel(
            this,
            AppShopLocal.repository
        )
    })

    private lateinit var dataBinding: FragmentDetailProductBinding
    //private var onDetailContentListener: OnDetailContentListener? = null
    private var product: Product = Product()
    private var brand = EMPTY_STRING//SharedViewModel.getProductBrend(product.brand)
    private var actionSale = EMPTY_STRING//SharedViewModel.getProductPromotion(product.discount, product.sold ?: 0)
    //private var brand: String = EMPTY_STRING
    private var imageIndex: Int = 0
    //private var actionSale = EMPTY_STRING
    private var reviews = listOf<Review>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
/*        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }*/
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = FragmentDetailProductBinding.inflate(inflater, container, false)
        dataBinding.detailProductContent.indicatorImages.count = product.linkimages?.size ?: 0
        dataBinding.detailProductContent.indicatorImages.selectedIndex = imageIndex
        dataBinding.cardViewProductImages.visibleCardButton = false
        dataBinding.cardViewProductImages.product = product
        dataBinding.cardViewProductImages.setOnChangeSelectedItem(object: OnChangeSelectedItem{
            override fun onChangeItemIndex(index: Int) {
                dataBinding.detailProductContent.indicatorImages.selectedIndex = index
            }
        })
        val textFriday = "-${FRIDAY_PERCENT}%"
        dataBinding.detailProductContent.textPercentFriday.text = textFriday
        dataBinding.detailProductContent.textProductPrice.paintFlags = dataBinding.detailProductContent.textProductPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG


        val animation = ObjectAnimator.ofFloat(dataBinding.detailProductContent.textViewSale, View.ROTATION_Y, 0.0f, 360f)
        animation.startDelay = 3000
        animation.duration = 2400
        //animation.repeatCount = ObjectAnimator.INFINITE
        animation.interpolator = AccelerateDecelerateInterpolator()

        animation.addListener(object: AnimatorListenerAdapter(){
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                Handler(Looper.getMainLooper()).postDelayed(
                    Runnable { animation.start() }, 3000)
            }
        })
        animation.start()
        sharedViewModel.getReviewsProduct(product.id, limit = 3) {
            log(it)
            reviews = it
            dataBinding.invalidateAll()
        }
        /*lifecycleScope.launch {
            sharedViewModel.reviews.collect {
                log(it)
            }
        }*/

        dataBinding.product = product
        dataBinding.eventhandler = this
        setDateDelivery(System.currentTimeMillis())
        return dataBinding.root
    }


    private fun setDateDelivery(date: Long){
        val arrayMonth = getStringArrayResource(R.array.month)
        //val dateFormat = SimpleDateFormat.getDateInstance()
        //val dateDelivery = Date(date)
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = date
        calendar.add(Calendar.DAY_OF_MONTH, 3)
        val day   = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val textDate = "${getString(R.string.text_datedelivery)} $day ${arrayMonth[month]}"
        dataBinding.textViewDateDelivery.text = textDate
    }

    /*private fun setReviews(value: List<Review>) {
        reviews = value
    }*/

    private fun hideLayoutFriday(){
        dataBinding.detailProductContent.layoutFriday.visibility = View.GONE
    }

    private fun setProduct(value: Product) {
       product = value
       brand = SharedViewModel.getProductBrend(product.brand)
       actionSale = SharedViewModel.getProductPromotion(product.discount, product.sold ?: 0)
    }

   /* private fun setOnDetailContentListener(value: OnDetailContentListener){
        onDetailContentListener = value
    }*/

   /* private fun setActionSale(value: String) {
        actionSale = value
    }

    private fun setBrandName(value: String) {
        brand = value
    }*/

    private fun setImageIndex(value: Int) {
        imageIndex = value
    }

    override fun onShowReviews() {
        log("show reviews...")
    }

    override fun onShowReview(review: Review) {
        TODO("Not yet implemented")
    }

    override fun onShowQuestions() {
        log("show questions...")
    }

    override fun onAddCart() {
        log("add cart ${product.id}...")
    }

    override fun onBuyOneClick() {
        TODO("Not yet implemented")
    }

    override fun onShowImage(index: Int) {
        log("log image $index...")
    }

    override fun onShowBrand() {
        log("log brand ${product.brand}...")
    }

    companion object {
        private var instance: DetailProductFragment? = null
        @JvmStatic
        fun newInstance(product: Product, imageIndex: Int/*, brandName: String, actionSale: String, reviews: List<Review>,
                        onDetailContentListener: OnDetailContentListener*/) =
            DetailProductFragment().apply {
                setProduct(product)
                setImageIndex(imageIndex)
/*                setBrandName(brandName)
                setActionSale(actionSale)
                setReviews(reviews)*/
                //setOnDetailContentListener(onDetailContentListener)
                instance = this
            }

        @JvmStatic
        fun getBrand() = instance?.brand ?: EMPTY_STRING

        @JvmStatic
        fun getActionSale() = instance?.actionSale ?: EMPTY_STRING

        @JvmStatic
        fun getRating() = getAfterWord(instance?.reviews?.count(), WORD_RATE)

        @JvmStatic
        fun getCountReviews() = instance?.reviews?.count().toString() ?: "0"

        @JvmStatic
        fun getCountQuestions() = "4"

        @JvmStatic
        fun getFinalPrice(): String {
            val finalPrice = instance?.let {
                val product = it.product
                val percentFriday = if (isLastFriday()) FRIDAY_PERCENT else {
                    instance?.hideLayoutFriday()
                    0
                }
                getFormattedFloat(product.price - product.price * (product.discount + percentFriday) / 100f)
            } ?: EMPTY_STRING
            return finalPrice
        }
    }
}