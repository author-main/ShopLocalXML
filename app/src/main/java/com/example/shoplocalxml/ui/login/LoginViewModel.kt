package com.example.shoplocalxml.ui.login

import android.view.View
import android.widget.Button
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoplocalxml.log
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    var onChangePassword: ((value: String) -> Unit)? = null
    var onValidEmail: (() -> String?)? = null
    private val KEY_FINGER      = 10
    private val KEY_BACKSPACE   = 11
    private var userPassword    = ""
    private val _text = MutableLiveData<String>().apply {
        value = "This is slideshow Fragment"
    }
    val text: LiveData<String> = _text

    fun onClick(index: Int){
        if (index in 0..11) {
            var changed = false
            when (index) {
                KEY_FINGER -> {}
                KEY_BACKSPACE -> {
                    if (userPassword.isNotBlank()) {
                        changed = true
                        userPassword = userPassword.substring(0, userPassword.length - 1)
                    }
                }
                else -> {
                    if (userPassword.length < 5) {
                        changed = true
                        userPassword = "$userPassword$index"
                    }
                }
            }
            if (changed) {
                onChangePassword?.invoke(userPassword)
                if (userPassword.length == 5) {
                    viewModelScope.launch {
                        delay(500)
                        onLogin()
                    }

                }
            }
        }
    }

    private fun onLogin(){
        val validEmail = onValidEmail?.invoke()
         if (!validEmail.isNullOrBlank()) {
             log (validEmail)
         }
        userPassword = ""
    }
}