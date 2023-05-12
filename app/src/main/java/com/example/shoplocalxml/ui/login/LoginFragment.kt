package com.example.shoplocalxml.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shoplocalxml.R
import com.example.shoplocalxml.custom_view.EditTextExt
import com.example.shoplocalxml.databinding.FragmentLoginBinding
import com.example.shoplocalxml.log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    private lateinit var dataBinding: FragmentLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val passwordSymbols = arrayOfNulls<TextView>(5)
        val loginViewModel =
            ViewModelProvider(this)[LoginViewModel::class.java]
        loginViewModel.onChangePassword = {value ->
            val length = value.length
            passwordSymbols.forEachIndexed { index, textView ->
                textView?.text = if (index < length)
                    resources.getString(R.string.fCharPassword)
                    else resources.getString(R.string.eCharPassword)
            }
        }
        loginViewModel.onValidEmail = {
            passwordSymbols.forEach{textView ->
                textView?.text = resources.getString(R.string.eCharPassword)
            }
            if (dataBinding.editTextTextEmailAddress.validateValue())
                dataBinding.editTextTextEmailAddress.text.toString()
            else
                null
        }
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_login, container, false)
        dataBinding = FragmentLoginBinding.inflate(inflater, container, false)
        passwordSymbols[0] = dataBinding.textViewSym1
        passwordSymbols[1] = dataBinding.textViewSym2
        passwordSymbols[2] = dataBinding.textViewSym3
        passwordSymbols[3] = dataBinding.textViewSym4
        passwordSymbols[4] = dataBinding.textViewSym5
        dataBinding.eventhandler = loginViewModel
        /*dataBinding.editTextTextEmailAddress.setDrawableOnClick {
            log("click drawable right...")
        }*/
        return dataBinding.root
    }
}