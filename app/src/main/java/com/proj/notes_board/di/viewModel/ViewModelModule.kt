package com.proj.notes_board.di.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Module
@ExperimentalCoroutinesApi
abstract class ViewModelModule {
//    @Binds
//    @IntoMap
//    @ViewModelKey(MainViewModel::class)
//    abstract fun mainViewModel(viewModel: MainViewModel): ViewModel
//
//    @Binds
//    @IntoMap
//    @ViewModelKey(NotesViewModel::class)
//    abstract fun notesViewModel(viewModel: NotesViewModel): ViewModel
//
//    @Binds
//    @IntoMap
//    @ViewModelKey(AddNoteViewModel::class)
//    abstract fun addNoteViewModel(viewModel: AddNoteViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}