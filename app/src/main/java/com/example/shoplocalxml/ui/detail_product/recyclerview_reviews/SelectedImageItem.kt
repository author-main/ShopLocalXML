package com.example.shoplocalxml.ui.detail_product.recyclerview_reviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import com.example.shoplocalxml.DEFAULT_BITMAP
import com.example.shoplocalxml.R
import com.example.shoplocalxml.databinding.ItemSelectedImageBinding


class SelectedImageItem: CardView {
    private var index = -1
    var onClick: ((Int) -> Unit)? = null
    private fun setOnCLickListener(index: Int, onClick: (Int) -> Unit) {
        this.onClick = onClick
        this.index   = index
    }
    private val dataBinding: ItemSelectedImageBinding = run {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        DataBindingUtil.inflate(inflater, com.example.shoplocalxml.R.layout.item_selected_image, this, true)
    }

    var linkImage: String? = null
        set(value) {
            field = value
            setLinkImageItem(value)
        }


    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        isSelected = false
        dataBinding.imageViewSelected.setOnClickListener {
            isSelected = !isSelected
            onClick?.invoke(index)
        }
    }

    private fun setLinkImageItem(value: String?) {
        value?.let {
            dataBinding.imageViewSelected.setImageURI(it.toUri())
        } ?: run {
            dataBinding.imageViewSelected.setImageDrawable(null)
        }
    }
}