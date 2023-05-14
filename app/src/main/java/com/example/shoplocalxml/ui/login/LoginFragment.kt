package com.example.shoplocalxml.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.shoplocalxml.AppShopLocal.Companion.repository
import com.example.shoplocalxml.PasswordSymbol
import com.example.shoplocalxml.databinding.FragmentLoginBinding
import com.example.shoplocalxml.ui.login.access_handler.AccessHandler
import com.example.shoplocalxml.ui.login.access_handler.AccessHandlerImpl
import com.example.shoplocalxml.ui.login.finger_print.FingerPrint
import com.example.shoplocalxml.FactoryViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    //private val colorFillSymbol = resources.getColor(R.color.colorAccent, null)
    //private lateinit var accessHandler: AccessHandler
    private lateinit var dataBinding: FragmentLoginBinding
    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val passwordSymbols = arrayOfNulls<TextView>(5)
        /*val loginViewModel =
            ViewModelProvider(this)[LoginViewModel::class.java]*/

        val loginViewModel: LoginViewModel by viewModels(factoryProducer = {
            FactoryViewModel(
                this,
                repository
            )
        })


        loginViewModel.onChangePassword = { count, type ->

            if (type == PasswordSymbol.FINGER_PRINT) {
                passwordSymbols.forEach {
                    it?.alpha = 1f
                }
            } else {
                passwordSymbols.forEachIndexed { index, textView ->
                    if (index == count - 1) {
                        if (type == PasswordSymbol.NUMBER) {
                            val animation: Animation = AlphaAnimation(0f, 1f)
                            animation.duration = 300
                            textView?.alpha = 1f
                            textView?.startAnimation(animation)
                        } else if (index > count - 1)
                            textView?.alpha = 0f
                        /*else
                        textView?.alpha = 1f*/
                    }
                }
            }
        }

        loginViewModel.onValidEmail = {
            passwordSymbols.forEach{textView ->
                textView?.alpha = 0f
                //textView?.setTextColor(resources.getColor(R.color.colorAccent, null))
            }
            if (dataBinding.editTextTextEmailAddress.validateValue())
                dataBinding.editTextTextEmailAddress.text.toString()
            else
                null
        }
        // Inflate the layout for this fragment
        //val root = inflater.inflate(R.layout.fragment_login, container, false)
        dataBinding = FragmentLoginBinding.inflate(inflater, container, false)
        passwordSymbols[0] = dataBinding.textViewSym1f
        passwordSymbols[1] = dataBinding.textViewSym2f
        passwordSymbols[2] = dataBinding.textViewSym3f
        passwordSymbols[3] = dataBinding.textViewSym4f
        passwordSymbols[4] = dataBinding.textViewSym5f
        passwordSymbols.forEach {
            it?.alpha = 0f
        }

        /*val accessHandler: AccessHandler = AccessHandlerImpl()
        accessHandler.setActivityFingerPrint(requireActivity())*/
        loginViewModel.setActivityFingerPrint(requireActivity())
        val existPassword = loginViewModel.existPassword()
        val enabledKeyFingerPrint = existPassword && FingerPrint.canAuthenticate()
        dataBinding.buttonKeyFinger.isEnabled = enabledKeyFingerPrint
        if (!enabledKeyFingerPrint)
            dataBinding.buttonKeyFinger.alpha = 0.3f
        dataBinding.eventhandler = loginViewModel
        /*dataBinding.editTextTextEmailAddress.setDrawableOnClick {
            log("click drawable right...")
        }*/
        return dataBinding.root
    }

}