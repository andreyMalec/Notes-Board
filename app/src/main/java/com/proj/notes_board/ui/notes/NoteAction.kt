package com.proj.notes_board.ui.notes

import com.proj.notes_board.model.Note

interface NoteAction {
    fun onNoteClick(note: Note)

    fun onNoteLongClick(note: Note)

    fun onNoteSwipe(position: Int)
}