package com.example.shoplocalxml.dagger

import androidx.lifecycle.ViewModel
import dagger.MapKey
import dagger.Module
import dagger.Subcomponent
import kotlin.reflect.KClass


@Subcomponent(modules = [ViewModelModule::class])
interface ViewModelComponent


@Module
interface ViewModelModule

@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)