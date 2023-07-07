package com.example.shoplocalxml.ui.detail_product

import com.example.shoplocalxml.classes.Review

interface OnDetailContentListener {
    fun onShowImage(index: Int)
    fun onShowReviews()
    fun onShowBrand()
    fun onShowReview(review: Review)
    fun onShowQuestions()
    fun onAddCart()
    fun onBuyOneClick()
}