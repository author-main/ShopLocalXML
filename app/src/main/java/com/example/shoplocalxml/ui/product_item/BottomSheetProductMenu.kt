package com.example.shoplocalxml.ui.product_item

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import com.example.shoplocalxml.R
import com.example.shoplocalxml.databinding.BottomsheetProductBinding
import com.example.shoplocalxml.generated.callback.OnClickListener
import com.example.shoplocalxml.getStringArrayResource
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BottomSheetProductMenu: BottomSheetDialogFragment(), View.OnClickListener {
    //private val dataBinding: BottomsheetProductBinding = DataBindingUtil.inflate(layoutInflater, R.layout.bottomsheet_product, null, false)
    private lateinit var dataBinding: BottomsheetProductBinding

    override fun onClick(v: View?) {
        v?.let{view ->
            var hide = true
            CoroutineScope(Dispatchers.Main).launch {
                delay(150)
                when (view.id) {
                    R.id.layoutMenuItemCart -> {

                    }

                    R.id.layoutMenuItemBrend -> {

                    }

                    R.id.layoutMenuItemFavorite -> {
                        hide = false
                    }

                    R.id.layoutMenuItemProduct -> {

                    }
                    /*R.id.layoutMenuItemCancel ->{
                    dismiss()
                }*/
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
        dataBinding = BottomsheetProductBinding.bind(inflater.inflate(R.layout.bottomsheet_product, container))
        val itemText = getStringArrayResource(R.array.bottomsheet_product_items)
        dataBinding.textMenuItemCart.text       = itemText[0]
        dataBinding.textMenuItemBrend.text      = itemText[1]
        dataBinding.textMenuItemFavorite.text   = itemText[2]
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
            //bottomSheet.background = ColorDrawable(Color.TRANSPARENT)
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            //bottomSheetDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        }
    }
}