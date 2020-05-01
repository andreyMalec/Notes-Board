package com.proj.notes_board.ui.notes

import androidx.lifecycle.ViewModel
import com.proj.notes_board.repo.NotesRepo
import javax.inject.Inject

class NotesViewModel @Inject constructor(private val repo: NotesRepo): ViewModel() {
}