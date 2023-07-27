package com.example.shoplocalxml.ui.home

import android.os.Parcelable
import java.io.Serializable

interface OnStackModeListener {
    fun onPushStackMode(value: HomeViewModel.Companion.HomeMode)
    fun onPopStackMode()
}