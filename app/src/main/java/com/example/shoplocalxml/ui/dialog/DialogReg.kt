package com.example.shoplocalxml.ui.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Patterns
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.allViews
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.fragment.app.DialogFragment
import com.example.shoplocalxml.R
import com.example.shoplocalxml.classes.User
import com.example.shoplocalxml.custom_view.EditTextExt
import com.example.shoplocalxml.databinding.DialogRegBinding
import com.example.shoplocalxml.log
import com.example.shoplocalxml.setDialogStyle


class DialogReg: DialogFragment() {
    private lateinit var dataBinding: DialogRegBinding
    private lateinit var dialog: AlertDialog
    //private val user: User = User.getInstance()
    val user: ObservableField<User> = ObservableField<User>()
    //var lastname: String = ""

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dataBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.dialog_reg, null, false)

        val userData = User.getInstance().apply {
            lastname = "Мышанский"
        }
        user.set(userData)
        dataBinding.dialog = this

        /*dataBinding.editTextPassword.setDrawableOnClick {
            log("click drawable right...")
        }*/

        dataBinding.editTextPassword.onValidValue = {password ->
            password.length == 5
        }

        dataBinding.editTextLName.onValidValue = {lname ->
            lname.isNotBlank()
        }

        dataBinding.editTextFName.onValidValue = {fname ->
            fname.isNotBlank()
        }

        dataBinding.editTextMail.onValidValue = {email ->
            !(email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches())
        }

        dataBinding.editTextPhone.onValidValue = {phone ->
            val regExp = "^(8|\\+7)\\d{10}".toRegex()
            regExp.matches(phone)
        }

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

        var verified = true
        dataBinding.root.allViews.forEach { v ->
            if (v is EditTextExt) {
                if (!(v as EditTextExt).correctValue)
                    verified = false
            }
        }

        if (verified) {
            log("register ok...")
            dismiss()
        }
    }

}