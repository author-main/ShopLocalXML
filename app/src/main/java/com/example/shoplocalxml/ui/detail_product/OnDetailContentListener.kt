package com.example.shoplocalxml.ui.detail_product

import com.example.shoplocalxml.classes.Review

interface OnDetailContentListener {
    fun onShowImage(index: Int)
    fun onShowReviews()
    fun onShowBrand(id: Int)
    fun onShowReview(review: Review)
    fun onShowQuestions()
    fun onAddCart(id: Int)
    fun onBuyOneClick(id: Int)
}