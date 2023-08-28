package com.example.shoplocalxml

import androidx.lifecycle.ViewModel
import com.example.shoplocalxml.repository.Repository

abstract class RepositoryViewModel(private val repository: Repository? = null): ViewModel(){
    fun getRepository() = repository
}