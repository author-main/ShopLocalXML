package com.example.shoplocalxml.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.shoplocalxml.R
import com.example.shoplocalxml.custom_view.EditTextExt
import com.example.shoplocalxml.log

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_login, container, false)
        val editMail = root.findViewById<EditTextExt>(R.id.editTextTextEmailAddress)
        editMail.setDrawableOnClick {
            log("click drawable right...")
        }
        return root
    }

}