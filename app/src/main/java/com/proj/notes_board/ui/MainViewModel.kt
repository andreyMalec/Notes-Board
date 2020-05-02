package com.proj.notes_board.ui

import androidx.lifecycle.*
import com.proj.notes_board.model.Note
import com.proj.notes_board.repo.NotesRepo
import com.proj.notes_board.ui.navigation.Screens
import kotlinx.coroutines.launch
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val repo: NotesRepo,
    private val router: Router
) : ViewModel() {

    val notes: LiveData<List<Note>> = repo.getAll().asLiveData()

    private val _createResultError = MutableLiveData<Boolean>()
    val createResultError: LiveData<Boolean>
        get() = _createResultError

    fun init() {
        router.newRootScreen(Screens.Notes)
    }

    fun onCreateNoteClick() {
        _createResultError.value = false
        router.navigateTo(Screens.CreateNote)
    }

    fun checkAndCreateNote(title: String?, description: String?, badgeColor: Int?) {
        val notNullTitle = if (title.isNullOrBlank()) {
            _createResultError.value = true
            return
        } else
            title

        createNote(notNullTitle, description, badgeColor)
    }

    private fun createNote(title: String, description: String?, badgeColor: Int?) {
        viewModelScope.launch {
            repo.createNote(title, description, badgeColor)
            router.exit()
        }
    }
}