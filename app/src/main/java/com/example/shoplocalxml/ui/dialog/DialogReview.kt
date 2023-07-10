package com.example.shoplocalxml.ui.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.shoplocalxml.R
import com.example.shoplocalxml.setDialogStyle

class DialogReview: DialogFragment() {
    private lateinit var dialog: AlertDialog
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        /*val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder
            .setView(dataBinding.root)
            .setNegativeButton(R.string.button_cancel, null)
            .setPositiveButton(R.string.btn_send, null)
        dialog = builder.create()
        setDialogStyle(dialog, title = R.string.title_restore)
        return dialog*/
        return super.onCreateDialog(savedInstanceState)
    }
}