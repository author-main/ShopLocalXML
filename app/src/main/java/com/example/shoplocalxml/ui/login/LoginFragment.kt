package com.example.shoplocalxml.ui.login

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.shoplocalxml.AppShopLocal.Companion.repository
import com.example.shoplocalxml.FactoryViewModel
import com.example.shoplocalxml.OnBackPressed
import com.example.shoplocalxml.OnOpenShopListener
import com.example.shoplocalxml.R
import com.example.shoplocalxml.SharedViewModel
import com.example.shoplocalxml.TypeRequest
import com.example.shoplocalxml.classes.User
import com.example.shoplocalxml.custom_view.SnackbarExt
import com.example.shoplocalxml.databinding.FragmentLoginBinding
import com.example.shoplocalxml.getStringResource
import com.example.shoplocalxml.ui.dialog.DialogProgress
import com.example.shoplocalxml.ui.dialog.DialogReg
import com.example.shoplocalxml.ui.dialog.DialogRestore
import com.example.shoplocalxml.ui.login.LoginViewModel.Companion.KEY_FINGER
import com.example.shoplocalxml.ui.login.finger_print.FingerPrint
import com.example.shoplocalxml.vibrate


class LoginFragment : Fragment(), OnUserListener, OnBackPressed {
    private var logged_in = false
    //private lateinit var sharedViewModel: SharedViewModel
    /*private val sharedViewModel: SharedViewModel by activityViewModels(factoryProducer = {
        FactoryViewModel(
            this,
            repository
        )
    })*/

    private val sharedViewModel: SharedViewModel by activityViewModels(factoryProducer = {
        FactoryViewModel(
            requireActivity()/*,
            //this,
            repository*/
        )
    })


    private lateinit var loginViewModel: LoginViewModel
    private lateinit var dataBinding: FragmentLoginBinding
    private val passwordSymbols = arrayOfNulls<TextView>(5)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = FragmentLoginBinding.inflate(inflater, container, false)
        //loginViewModel = ViewModelProvider(this, FactoryViewModel(requireActivity(), repository))[LoginViewModel::class.java]
        loginViewModel = //ViewModelProvider(this, FactoryViewModel(this, repository))[LoginViewModel::class.java]
            ViewModelProvider(requireActivity(), FactoryViewModel(requireActivity()/*, repository*/))[LoginViewModel::class.java]

      /*  sharedViewModel = run {
            //val factory = FactoryViewModel(this, repository)
            val factory = FactoryViewModel(this, repository)
            ViewModelProvider(requireActivity(), factory)[SharedViewModel::class.java]
        }*/


        loginViewModel.onRequestProcessed = {data, typeRequest, result ->
            requestProcessed(data, typeRequest, result)
        }
        loginViewModel.onChangePassword = { count, key ->
            SnackbarExt.hideSnackbar()
            if (key == KEY_FINGER) {
                fillPasswordSym()
            } else {
                passwordSymbols.forEachIndexed { index, textView ->
                        if (key in 0..9) {
                            if (index == count - 1) {
                                val animation: Animation = AlphaAnimation(0f, 1f)
                                animation.duration = 300
                                textView?.alpha = 1f
                                textView?.startAnimation(animation)
                            }
                        } else if (index > count - 1)
                            textView?.alpha = 0f
                }
            }
        }

        loginViewModel.onValidEmail = {
            if (dataBinding.editTextEmailAddress.validateValue())
                dataBinding.editTextEmailAddress.text.toString()
            else
                null
        }
        loginViewModel.onRegisterUser = {
            DialogReg().show(childFragmentManager, null)
        }
        loginViewModel.onRestoreUser = {
            val dialogRestore = DialogRestore()
            val email = dataBinding.editTextEmailAddress.text.toString()
            if (email.isNotBlank()) {
                val bundle = Bundle()
                bundle.putString("email", email)
                dialogRestore.arguments = bundle
            }
            dialogRestore.show(childFragmentManager, null)
        }
        passwordSymbols[0] = dataBinding.textViewSym1f
        passwordSymbols[1] = dataBinding.textViewSym2f
        passwordSymbols[2] = dataBinding.textViewSym3f
        passwordSymbols[3] = dataBinding.textViewSym4f
        passwordSymbols[4] = dataBinding.textViewSym5f

        fillPasswordSym(clear = true)
        /*passwordSymbols.forEach {
            it?.alpha = 0f
        }*/

        loginViewModel.setActivityFingerPrint(requireActivity())
        loginViewModel.getUserEmail()?.let{
            dataBinding.editTextEmailAddress.setText(it)
        }
        dataBinding.editTextEmailAddress.onValidValue = {
            !(it.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(it).matches())
        }

        loginViewModel.onPerformLogin = {
            fillPasswordSym()
            DialogProgress.show(requireContext())
        }
        val existPassword = loginViewModel.existPassword()
        val enabledKeyFingerPrint = existPassword && FingerPrint.canAuthenticate()
        dataBinding.buttonKeyFinger.isEnabled = enabledKeyFingerPrint
        if (!enabledKeyFingerPrint)
            dataBinding.buttonKeyFinger.alpha = 0.3f
        dataBinding.eventhandler = loginViewModel
        return dataBinding.root
    }

    private fun fillPasswordSym(clear: Boolean = false){
        val value = if (clear) 0f else 1f
        passwordSymbols.forEach{textView ->
            textView?.alpha = value
        }
    }

    override fun onRegisterUser(user: User) {
        DialogProgress.show(requireContext())
        loginViewModel.performRegisterUser(user)
    }

    override fun onRestoreUser(user: User) {
        DialogProgress.show(requireContext())
        loginViewModel.performRestoreUser(user)
    }

    private fun<T> requestProcessed(data: T?, type: TypeRequest, result: Boolean) {
        fillPasswordSym(clear = true)
        when (type) {
            TypeRequest.USER_LOGIN -> {
                if (result) {
                    activity?.let{
                        if (it is OnOpenShopListener) {
                            //activity?.viewModelStore?.clear()
                            DialogProgress.hide()
                            (it as OnOpenShopListener).openShop()
                            logged_in = true
                        }
                    }
                } else {
                    vibrate(400)
                    val snackbarExt = SnackbarExt(
                        dataBinding.root,
                        getStringResource(R.string.message_login_error))
                    snackbarExt.type = SnackbarExt.Companion.SnackbarType.ERROR
                    snackbarExt.show()
                }

                /*passwordSymbols.forEach{textView ->
                    textView?.alpha = 0f
                }*/
            }
            TypeRequest.USER_REGISTER -> {
                if (result) {
                    val snackbarExt = SnackbarExt(dataBinding.root, getStringResource(R.string.text_notifyreg))
                    snackbarExt.type = SnackbarExt.Companion.SnackbarType.INFO
                    snackbarExt.show()
                    val user = data as User
                    dataBinding.editTextEmailAddress.setText(user.email)
                    user.saveUserData()
                } else {
                    vibrate(400)
                    val snackbarExt = SnackbarExt(dataBinding.root, getStringResource(R.string.text_notifyreg_error))
                    snackbarExt.type = SnackbarExt.Companion.SnackbarType.ERROR
                    snackbarExt.show()
                }
            }
            TypeRequest.USER_RESTORE -> {
                if (result) {
                    val snackbarExt = SnackbarExt(dataBinding.root, getStringResource(R.string.text_notifyrestore))
                    snackbarExt.type = SnackbarExt.Companion.SnackbarType.INFO
                    snackbarExt.show()
                    val user = data as User
                    dataBinding.editTextEmailAddress.setText(user.email)
                } else {
                    vibrate(400)
                    val snackbarExt = SnackbarExt(dataBinding.root, getStringResource(R.string.text_notifyrestore_error))
                    snackbarExt.type = SnackbarExt.Companion.SnackbarType.ERROR
                    snackbarExt.show()
                }

            }
        }
        DialogProgress.hide()
    }

    override fun backPressed() {
        sharedViewModel.closeApp()
    }

    override fun onDetach() {
        super.onDetach()
        if (logged_in)
            sharedViewModel.getProducts(1)
    }
}