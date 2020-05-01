package com.proj.notes_board.di

import com.proj.notes_board.app.App
import dagger.Component
import dagger.android.AndroidInjectionModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@Singleton
@ExperimentalCoroutinesApi
@Component(modules = [AndroidInjectionModule::class, AppModule::class])
interface AppComponent {
    fun inject(app: App)
}