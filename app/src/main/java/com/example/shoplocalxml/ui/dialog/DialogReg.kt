package com.example.shoplocalxml.ui.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
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
        dataBinding.eventhandler = this
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.title_reg)
            .setView(dataBinding.root)
            .setNegativeButton(R.string.button_cancel, null)
            .setPositiveButton(R.string.btn_reg, null)
        dialog = builder.create()
        setDialogStyle(dialog)
        return dialog
    }
    fun onClick(view: View) {

    }

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
        log("button ok...")
        dismiss()
    }

    /* companion object {
        const val tag = "DIALOG_REGUSER"
    }*/
}