package com.example.shoplocalxml.ui.login

import com.example.shoplocalxml.classes.User

interface OnUserListener {
    fun onRegisterUser(user: User)
    fun onRestoreUser(user: User)
}