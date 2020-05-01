package com.proj.notes_board.ui.navigation

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.proj.notes_board.ui.MainActivity
import com.proj.notes_board.ui.createNote.CreateNoteFragment
import com.proj.notes_board.ui.notes.NotesFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

object Screens {
    object Main : SupportAppScreen() {
        override fun getActivityIntent(context: Context): Intent? {
            return Intent(context, MainActivity::class.java)
        }
    }

    object Notes : SupportAppScreen() {
        override fun getFragment(): Fragment? {
            return NotesFragment()
        }
    }

    object CreateNote : SupportAppScreen() {
        override fun getFragment(): Fragment? {
            return CreateNoteFragment()
        }
    }
}