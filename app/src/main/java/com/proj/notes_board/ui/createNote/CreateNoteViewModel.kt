package com.proj.notes_board.ui.createNote

import androidx.lifecycle.ViewModel
import com.proj.notes_board.repo.NotesRepo
import javax.inject.Inject

class CreateNoteViewModel @Inject constructor(private val repo: NotesRepo) : ViewModel() {
}