package com.example.shoplocalxml.ui.detail_product.recyclerview_reviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.example.shoplocalxml.AppShopLocal
import com.example.shoplocalxml.classes.Review
import com.example.shoplocalxml.databinding.ReviewItemBinding


typealias OnClickReview = (Review) -> Unit
class ReviewItem: ConstraintLayout {
    val dataBinding: ReviewItemBinding = run {
        val inflater = AppShopLocal.applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        DataBindingUtil.inflate(inflater, com.example.shoplocalxml.R.layout.review_item, this, true)
    }
    var review: Review? = null
        set(value) {
            field = value
            dataBinding.review = value
        }

    private var onClickReview: OnClickReview? = null
    fun setOnClickReview(value: OnClickReview) {
        onClickReview = value
    }

    constructor(context: Context) : super(context) {
        dataBinding.textComment.setOnClickListener {
            val layout = dataBinding.textComment.layout
            if (layout != null) {
                val lines: Int = layout.lineCount
                if (lines > 0) {
                    val ellipsisCount: Int = layout.getEllipsisCount(lines - 1)
                    if (ellipsisCount > 0) {
                        onClickReview?.invoke(review!!)
                    }
                }
            }
        }
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
 }