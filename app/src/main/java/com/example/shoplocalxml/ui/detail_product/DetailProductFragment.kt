package com.example.shoplocalxml.ui.detail_product

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Intent
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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.example.shoplocalxml.EMPTY_STRING
import com.example.shoplocalxml.FRIDAY_PERCENT
import com.example.shoplocalxml.FactoryViewModel
import com.example.shoplocalxml.MainActivity
import com.example.shoplocalxml.R
import com.example.shoplocalxml.SharedViewModel
import com.example.shoplocalxml.WORD_RATE
import com.example.shoplocalxml.classes.Product
import com.example.shoplocalxml.classes.Review
import com.example.shoplocalxml.databinding.FragmentDetailProductBinding
import com.example.shoplocalxml.getAfterWord
import com.example.shoplocalxml.getCacheDirectory
import com.example.shoplocalxml.getFormattedFloat
import com.example.shoplocalxml.getStringArrayResource
import com.example.shoplocalxml.isLastFriday
import com.example.shoplocalxml.log
import com.example.shoplocalxml.md5
import com.example.shoplocalxml.ui.detail_product.recyclerview_reviews.ReviewsAdapter
import com.example.shoplocalxml.ui.dialog.DialogReview
import com.example.shoplocalxml.ui.home.HomeViewModel
import com.example.shoplocalxml.ui.image_viewer.ImageViewerActivity
import com.example.shoplocalxml.ui.product_item.product_card.recycler_view_images.OnChangeSelectedItem
import com.google.gson.Gson
import java.lang.Exception
import java.util.Calendar


class DetailProductFragment : Fragment(), OnDetailContentListener {

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var sharedViewModel: SharedViewModel

    /*private val sharedViewModel: SharedViewModel by activityViewModels(factoryProducer = {
        FactoryViewModel(
            this/*,
            AppShopLocal.repository*/
        )
    })*/


/*    override fun onPushStackMode(value: HomeViewModel.Companion.HomeMode) {
        homeViewModel.pushStackMode(value)
    }


    override fun onPopStackMode() {
        log("pop stackmode...")
        homeViewModel.popStackMode()
    }*/

    override fun onDestroy() {
        instance = null
        super.onDestroy()
    }

    /*private var onDetailProductButtonsListener: OnDetailProductButtonsListener? = null
    fun setOnDetailProductButtonsListener(value: OnDetailProductButtonsListener) {
        onDetailProductButtonsListener = value
    }*/


    private val adapter: ReviewsAdapter by lazy {
        ReviewsAdapter(context = requireContext())
    }

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

        val mainActivity = requireActivity() as MainActivity
        sharedViewModel = mainActivity.viewModelComponent.factory.create(SharedViewModel::class.java)
        homeViewModel =
            mainActivity.viewModelComponent.factory.create(HomeViewModel::class.java)


     /*   homeViewModel =
            ViewModelProvider(requireActivity())[HomeViewModel::class.java]*/
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
        if (imageIndex > 0)
            dataBinding.cardViewProductImages.getRecyclerViewImages().scrollToPosition(imageIndex)
        else
            dataBinding.detailProductContent.indicatorImages.selectedIndex = 0
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
                    { animation.start() }, 3000)
            }
        })
        animation.start()
        sharedViewModel.getReviewsProduct(product.id, limit = 2) {
            //log(it)
            reviews = it
            //dataBinding.invalidateAll()
            val count = getReviewsCount()
            dataBinding.detailProductContent.textRating.text =
                getAfterWord(count, WORD_RATE)
            dataBinding.detailProductContent.textCountReviews.text = count.toString()
            dataBinding.detailProductContent.textCountUsersReviews.text = getAfterWord(count, WORD_RATE)
            adapter.setReviews(it)
            adapter.setOnClickReview {review ->
                val dialogReview = DialogReview()
                val gson = Gson()
                val reviewJson = gson.toJson(review)
                val bundle = Bundle()
                bundle.putString("review", reviewJson)
                dialogReview.arguments = bundle
                dialogReview.show(childFragmentManager, null)
            }
        }
        dataBinding.detailProductContent.textDescription.text = product.description
        dataBinding.detailProductContent.ratingProduct.setCount(product.star)
        dataBinding.detailProductContent.textStarUsers.text = product.star.toString()
        /*lifecycleScope.launch {
            sharedViewModel.reviews.collect {
                log(it)
            }
        }*/

        dataBinding.product = product
        dataBinding.eventhandler = this
        setDateDelivery(System.currentTimeMillis())

        val manager = GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
        dataBinding.detailProductContent.recyclerViewReviews.layoutManager = manager
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(dataBinding.detailProductContent.recyclerViewReviews)
        dataBinding.detailProductContent.recyclerViewReviews.adapter = adapter
        return dataBinding.root
    }


    private fun getReviewsCount(): Int{
        var count = 0
        if (reviews.isNotEmpty()) {
            val firstElement: String = reviews[0].username
            val pos = firstElement.indexOf(">")
            if (pos != -1) {
                try {
                    val countStr = firstElement.substring(1, pos)
                    count = countStr.toInt()
                    reviews[0].username = firstElement.substring(pos + 1)
                } catch(_: Exception) {}
            }
        }
        if (count == 0)
            dataBinding.detailProductContent.cardViewReviews.visibility = View.GONE

        return count
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
        //dataBinding.cardViewProductImages.getRecyclerViewImages().scrollToPosition(value)
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
        sharedViewModel.addProductCart(id) {idResponse ->

        }
    }

    override fun onBuyOneClick() {
        log("one click ${product.id}...")
    }

    override fun onShowImage(index: Int) {
        product.linkimages?.let{
            val list = mutableListOf<String>()
            it.forEach { link ->
                list.add("${getCacheDirectory()}${md5(link)}")
            }
            homeViewModel.pushStackMode(HomeViewModel.Companion.HomeMode.IMAGE_VIEWER)
            val gson = Gson()
            val extraList = gson.toJson(list)
            val intent = Intent(requireContext(), ImageViewerActivity::class.java)
            intent.putExtra("listimages", extraList)
            intent.putExtra("startindex", index)
            /*intent.putExtra("stackmode_listener", object: OnStackModeListener{
                override fun onPushStackMode(value: HomeViewModel.Companion.HomeMode) {
                    homeViewModel.pushStackMode(value)
                }

                override fun onPopStackMode() {
                    homeViewModel.popStackMode()
                }
            })*/

            startActivity(intent)
        }
    /*    product.linkimages?.get(index)?.let {link ->
 //           if (!reduce) md5(url) else md5("${url}_")
            val srcimage = "${getCacheDirectory()}${md5(link)}"
            val intent = Intent(requireContext(), ImageViewerActivity::class.java)
            intent.putExtra("srcimage", srcimage)
            startActivity(intent)
        }*/
        //intent.putExtra("imagehash", imagehash)
    }

    override fun onShowBrand() {
        log("log brand ${product.brand}...")
    }


    override fun onResume() {
        super.onResume()
        if (homeViewModel.modeFragment.value == HomeViewModel.Companion.HomeMode.IMAGE_VIEWER)
            homeViewModel.popStackMode()
    }

    companion object {
        private var instance: DetailProductFragment? = null
        var favorite: Byte? = null
            set(value) {
                field = value
                instance?.product?.favorite = value!!
            }
            get() =
                instance?.product?.favorite

        val id: Int?
            get() = instance?.product?.id

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

/*        @JvmStatic
        fun getRating() = getAfterWord(instance?.reviews?.count(), WORD_RATE)

        @JvmStatic
        fun getCountReviews() = instance?.reviews?.count().toString() ?: "0"*/

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