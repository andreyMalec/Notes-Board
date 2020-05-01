package com.proj.notes_board.app

import android.app.Application
import com.proj.notes_board.di.AppInjector
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class App : Application(), HasAndroidInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        AppInjector.init(this)

        super.onCreate()
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector
}