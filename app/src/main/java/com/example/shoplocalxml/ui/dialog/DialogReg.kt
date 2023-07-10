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
import com.example.shoplocalxml.setDialogStyle
import com.example.shoplocalxml.ui.login.OnUserListener


class DialogReg: DialogFragment() {
    private lateinit var dataBinding: DialogRegBinding
    private lateinit var dialog: AlertDialog
    val user = User.getInstance()//: ObservableField<User> = ObservableField<User>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dataBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.dialog_reg, null, false)

        //user.set(User.getInstance())
        //dataBinding.dialog = this
        dataBinding.user = user

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
        builder
            .setView(dataBinding.root)
            .setNegativeButton(R.string.button_cancel, null)
            .setPositiveButton(R.string.btn_reg, null)
        dialog = builder.create()
        setDialogStyle(dialog, title = R.string.title_reg)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
            onClickDialogButton()//DialogInterface.BUTTON_POSITIVE)
        }
    }

    private fun onClickDialogButton(){//button: Int) {
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
                if (!(v as EditTextExt).validateValue())
                    verified = false
            }
        }
        if (verified) {
            dismiss()
            (parentFragment as OnUserListener).onRegisterUser(user)
        }
    }
}