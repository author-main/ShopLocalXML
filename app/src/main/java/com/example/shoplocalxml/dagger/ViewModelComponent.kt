package com.example.shoplocalxml.dagger

import dagger.Module
import dagger.Subcomponent



@Subcomponent(modules = [ViewModelModule::class])
interface ViewModelComponent


@Module
interface ViewModelModule