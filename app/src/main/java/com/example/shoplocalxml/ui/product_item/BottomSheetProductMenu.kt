package com.example.shoplocalxml.ui.product_item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.shoplocalxml.R
import com.example.shoplocalxml.databinding.BottomsheetProductBinding
import com.example.shoplocalxml.getStringArrayResource
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BottomSheetProductMenu(
    private val onProductItemMenuListener: (itemMenu: MenuItemProduct, idProduct: Int, favorite: Boolean) -> Unit): BottomSheetDialogFragment(), View.OnClickListener {
    private lateinit var dataBinding: BottomsheetProductBinding
    private var idProduct: Int = -1
    private var favorite = false
    private val itemText = getStringArrayResource(R.array.bottomsheet_product_items)

    private fun updateFavoriteItem(){
        dataBinding.textMenuItemFavorite.text   = if (favorite) itemText[5]
        else itemText[2]
        dataBinding.imageMenuItemFavorite.setImageResource(if (favorite) R.drawable.ic_favorite
        else R.drawable.ic_favorite_bs)
    }

    override fun onClick(v: View?) {
        if (idProduct == -1) {
            dismiss()
            return
        }
        v?.let{view ->
            var hide = true
            CoroutineScope(Dispatchers.Main).launch {
                delay(150)
                MenuItemProduct.values().find {
                    it.value == view.id
                }?.let{ selectedItemMenu ->
                    if (selectedItemMenu == MenuItemProduct.ITEM_FAVORITE) {
                        favorite = !favorite
                        updateFavoriteItem()
                        hide = false
                    }
                    onProductItemMenuListener(selectedItemMenu, idProduct, favorite)
                }
                if (hide)
                    dismiss()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        arguments?.let{arg ->
            idProduct = arg.getInt("idproduct")
            favorite  = arg.getBoolean("favorite")
        }
        dataBinding = BottomsheetProductBinding.bind(inflater.inflate(R.layout.bottomsheet_product, container))
        dataBinding.textMenuItemCart.text       = itemText[0]
        dataBinding.textMenuItemBrend.text      = itemText[1]
        updateFavoriteItem()
        dataBinding.textMenuItemProduct.text    = itemText[3]
        dataBinding.textMenuItemCancel.text     = itemText[4]
        dataBinding.layoutMenuItemBrend.setOnClickListener(this)
        dataBinding.layoutMenuItemCart.setOnClickListener(this)
        dataBinding.layoutMenuItemFavorite.setOnClickListener(this)
        dataBinding.layoutMenuItemProduct.setOnClickListener(this)
        dataBinding.layoutMenuItemCancel.setOnClickListener(this)
        return dataBinding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.let{bottomSheetDialog ->
            val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    companion object {
        enum class MenuItemProduct(val value: Int) {
            ITEM_CART(R.id.layoutMenuItemCart),
            ITEM_BREND(R.id.layoutMenuItemBrend),
            ITEM_FAVORITE(R.id.layoutMenuItemFavorite),
            ITEM_PRODUCT(R.id.layoutMenuItemProduct),
            ITEM_CANCEL(R.id.layoutMenuItemCancel)
        }
    }
}