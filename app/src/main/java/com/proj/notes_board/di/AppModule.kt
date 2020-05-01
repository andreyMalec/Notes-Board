package com.proj.notes_board.di

import com.proj.notes_board.dbService.NotesDao
import com.proj.notes_board.di.activity.ActivityModule
import com.proj.notes_board.di.viewModel.ViewModelModule
import com.proj.notes_board.repo.NotesRepo
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module(includes = [ActivityModule::class, ViewModelModule::class, Navigation::class])
class AppModule {
    @Provides
    @Singleton
    fun notesRepo(dao: NotesDao): NotesRepo = NotesRepo(dao)
}