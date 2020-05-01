package com.proj.notes_board.di.activity

import com.proj.notes_board.ui.createNote.CreateNoteFragment
import com.proj.notes_board.ui.notes.NotesFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Module
@ExperimentalCoroutinesApi
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeNotesFragment(): NotesFragment

    @ContributesAndroidInjector
    abstract fun contributeCreateNoteFragment(): CreateNoteFragment
}