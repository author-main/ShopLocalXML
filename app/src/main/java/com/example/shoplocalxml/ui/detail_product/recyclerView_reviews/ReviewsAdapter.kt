package com.example.shoplocalxml.ui.detail_product.recyclerView_reviews

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplocalxml.classes.Product
import com.example.shoplocalxml.classes.Review
import com.example.shoplocalxml.ui.product_item.ProductItem
import com.example.shoplocalxml.ui.product_item.ProductsAdapter
import com.example.shoplocalxml.ui.product_item.product_card.OnProductItemListener

class ReviewsAdapter (val context: Context, private var reviews: List<Review> = listOf()): RecyclerView.Adapter<ReviewsAdapter.ViewHolder>(){

    private var onClickReview: OnClickReview? = null
    fun setOnClickReview(value: OnClickReview){
        onClickReview = value
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = ReviewItem(context)
        return ReviewsAdapter.ViewHolder(view, onClickReview)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bindItem(reviews[position])
    }

    override fun getItemCount() =
        reviews.size

    @SuppressLint("NotifyDataSetChanged")
    fun setReviews(list: List<Review>) {
        reviews = list
        notifyDataSetChanged()
    }

    class ViewHolder(private val view: View, private val onClickReview: OnClickReview?) : RecyclerView.ViewHolder(view) {
        fun bindItem(value: Review){
            val item = view as ReviewItem
            val lparams = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            item.layoutParams = lparams
            item.review = value
            item.setOnClickReview {
                onClickReview?.invoke(it)
            }
        }
    }

}