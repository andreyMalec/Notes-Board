package com.proj.notes_board.ui.navigation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.proj.notes_board.ui.MainActivity
import com.proj.notes_board.ui.manageNote.ManageNoteFragment
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

    class ManageNote(private val noteId: Long? = null) : SupportAppScreen() {
        override fun getFragment(): Fragment? {
            return ManageNoteFragment().also {
                if (noteId != null) {
                    val data = Bundle()
                    data.putLong("noteId", noteId)
                    it.arguments = data
                }
            }
        }
    }
}