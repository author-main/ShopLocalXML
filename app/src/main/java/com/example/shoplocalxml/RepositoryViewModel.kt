package com.example.shoplocalxml

import androidx.lifecycle.ViewModel
import com.example.shoplocalxml.repository.Repository

abstract class RepositoryViewModel(protected val repository: Repository): ViewModel()