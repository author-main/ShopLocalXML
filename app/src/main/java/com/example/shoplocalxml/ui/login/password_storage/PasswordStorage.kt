package com.example.shoplocalxml.ui.login.password_storage

import javax.crypto.Cipher

interface PasswordStorage {
    fun existPassword(): Boolean
    fun putPassword(value: String)
    fun getPassword(): String?
    fun removePassword()
}