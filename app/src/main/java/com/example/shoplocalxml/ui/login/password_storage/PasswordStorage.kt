package com.example.shoplocalxml.ui.login.password_storage

import javax.crypto.Cipher

interface PasswordStorage {
    fun getDecryptCipher(): Cipher?
    fun existPassword(): Boolean
    fun putPassword(value: String)
    fun getPassword(): String?
    fun removePassword()
}