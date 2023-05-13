package com.example.shoplocalxml.ui.login.password_storage

interface PasswordStorage {
    fun putPassword(value: String)
    fun getPassword(): String?
    fun removePassword()
}