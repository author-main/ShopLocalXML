package com.example.shoplocalxml.ui.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.View.FOCUS_DOWN
import android.view.View.FOCUS_UP
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.shoplocalxml.R
import com.example.shoplocalxml.classes.User
import com.example.shoplocalxml.databinding.DialogRegBinding
import com.example.shoplocalxml.log
import com.example.shoplocalxml.setDialogStyle

class DialogReg: DialogFragment() {
    private lateinit var dataBinding: DialogRegBinding
    private lateinit var dialog: AlertDialog
    private val user: User = User.getInstance()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dataBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.dialog_reg, null, false)

        /*dataBinding.editTextPassword.setDrawableOnClick {
            log("click drawable right...")
        }*/


        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder//.setTitle(R.string.title_reg)
            .setView(dataBinding.root)
            .setNegativeButton(R.string.button_cancel, null)
            .setPositiveButton(R.string.btn_reg, null)
        dialog = builder.create()
        setDialogStyle(dialog, title = R.string.title_reg)
        return dialog
    }

  /*  fun onClick(view: View) {

    }*/

    override fun onStart() {
        super.onStart()
        //setWidthDialog(dialog, 300)
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
            onClickDialogButton()//DialogInterface.BUTTON_POSITIVE)
        }
    }




    private fun onClickDialogButton(){//button: Int) {
        /*if (button == DialogInterface.BUTTON_POSITIVE) {
            log("button ok...")
        }*/

        val view = dataBinding.root.findFocus()
        view?.let{focusedView ->
            if (focusedView is EditText) {
                focusedView.clearFocus()
                activity?.let{parent->
                    val imm = parent.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(focusedView.getWindowToken(), 0)
                }
            }
        }

     /*   log("focused view...")
        dismiss()*/
    }

}