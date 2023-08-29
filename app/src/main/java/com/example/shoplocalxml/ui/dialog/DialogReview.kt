package com.example.shoplocalxml.ui.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.shoplocalxml.AppShopLocal.Companion.applicationContext
import com.example.shoplocalxml.R
import com.example.shoplocalxml.classes.Review
import com.example.shoplocalxml.databinding.DialogReviewBinding
import com.example.shoplocalxml.setDialogStyle
import com.example.shoplocalxml.toPx
import com.google.gson.Gson

class DialogReview: DialogFragment() {
    private lateinit var dialog: AlertDialog
    private lateinit var dataBinding: DialogReviewBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dataBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.dialog_review, null, false)
        dataBinding.reviewContent.root.background = null
        dataBinding.reviewContent.textUserName.setTextColor(applicationContext.getColor(R.color.colorBrend))
        dataBinding.reviewContent.textComment.maxLines = Integer.MAX_VALUE
        val gson = Gson()
        val dataReview = requireArguments().getString("review")
        val review = gson.fromJson(dataReview, Review::class.java)
        dataBinding.review = review
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder
            .setView(dataBinding.root)
            .setPositiveButton(R.string.text_ok, null)
            .setCancelable(true)
        dialog = builder.create()
        setDialogStyle(dialog, noTitle = true)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        dialog.getButton(DialogInterface.BUTTON_POSITIVE)?.let {positive ->
            val parent = positive.parent as? LinearLayout
            parent?.gravity = Gravity.CENTER_HORIZONTAL
            val leftSpacer = parent?.getChildAt(1)
            leftSpacer?.visibility = View.GONE
            val lParams = LinearLayout.LayoutParams(
                70.toPx,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            lParams.setMargins(0,4.toPx,0,8.toPx)
            lParams.gravity = Gravity.CENTER
            positive.layoutParams = lParams
        }
    }
}