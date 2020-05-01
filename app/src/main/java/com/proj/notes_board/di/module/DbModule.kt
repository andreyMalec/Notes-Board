package com.proj.notes_board.di.module

import android.content.Context
import com.proj.notes_board.dbService.NotesDao
import com.proj.notes_board.dbService.NotesDatabase
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module(includes = [ContextModule::class])
class DbModule {
    @Provides
    @Singleton
    fun instance(context: Context): NotesDatabase = NotesDatabase.instance(context)

    @Provides
    @Singleton
    fun notesDao(db: NotesDatabase): NotesDao = db.notesDataDao()
}