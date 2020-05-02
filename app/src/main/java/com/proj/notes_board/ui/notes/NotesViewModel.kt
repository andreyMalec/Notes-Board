package com.proj.notes_board.ui.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.proj.notes_board.model.Note
import com.proj.notes_board.repo.NotesRepo
import com.proj.notes_board.ui.navigation.Screens
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class NotesViewModel @Inject constructor(
    private val repo: NotesRepo,
    private val router: Router
) : ViewModel() {

    val notes: LiveData<List<Note>> = repo.getAll().asLiveData()

    fun onCreateNoteClick() {
        router.navigateTo(Screens.CreateNote)
    }
}