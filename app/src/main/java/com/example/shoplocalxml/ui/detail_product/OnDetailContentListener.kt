package com.example.shoplocalxml.ui.detail_product

import com.example.shoplocalxml.classes.Review

interface OnDetailContentListener {
    fun onShowReviews()
    fun onShowReview(review: Review)
    fun onShowQuestions()
    fun onAddCart()
    fun onBuyOneClick()
}