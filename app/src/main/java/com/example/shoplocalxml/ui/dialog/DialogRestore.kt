package com.example.shoplocalxml.ui.dialog

import androidx.fragment.app.DialogFragment
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
import com.example.shoplocalxml.R
import com.example.shoplocalxml.classes.User
import com.example.shoplocalxml.custom_view.EditTextExt
import com.example.shoplocalxml.databinding.DialogRestoreBinding
import com.example.shoplocalxml.setDialogStyle
import com.example.shoplocalxml.ui.login.OnUserListener


class DialogRestore: DialogFragment() {
    private lateinit var dataBinding: DialogRestoreBinding
    private lateinit var dialog: AlertDialog
    val user = User()//.getInstance()
            //ObservableField<User> = ObservableField<User>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dataBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.dialog_restore, null, false)
        //user.set(User.getInstance())
        //dataBinding.dialog = this
        //dataBinding.user = user.get()
        dataBinding.user = user

        dataBinding.editTextPassword.onValidValue = {password ->
            password.length == 5
        }

        dataBinding.editTextMail.onValidValue = {email ->
            !(email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches())
        }


        val email = arguments?.getString("email")
        email?.let{
            user.email = it
        }


        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder
            .setView(dataBinding.root)
            .setNegativeButton(R.string.button_cancel, null)
            .setPositiveButton(R.string.btn_send, null)
        dialog = builder.create()
        setDialogStyle(dialog, title = R.string.title_restore)
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
                if (!v.validateValue())
                    verified = false
            }
        }
        if (verified) {
            dismiss()
            (parentFragment as OnUserListener).onRestoreUser(user)
        }
    }
}