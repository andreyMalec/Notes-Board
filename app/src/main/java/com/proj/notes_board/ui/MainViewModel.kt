package com.proj.notes_board.ui

import androidx.lifecycle.*
import com.proj.notes_board.model.Note
import com.proj.notes_board.repo.NotesRepo
import com.proj.notes_board.ui.navigation.Screens
import com.proj.notes_board.ui.notes.NotesAdapter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val repo: NotesRepo,
    private val router: Router
) : ViewModel(), NotesAdapter.NoteAction {

    val notes: LiveData<List<Note>> = repo.getAll().asLiveData()

    private val selected = mutableListOf<Long>()

    private val _isCreateError = MutableLiveData(false)
    val isCreateError: LiveData<Boolean>
        get() = _isCreateError

    private val _selectedCount = MutableLiveData(0)
    val selectedCount: LiveData<Int>
        get() = _selectedCount

    fun init() {
        router.newRootScreen(Screens.Notes)

        viewModelScope.launch {
            selected.clear()
            selected.addAll(repo.getSelected().first().map { it.id })
            _selectedCount.value = selected.size
        }
    }

    fun onCreateNoteClick() {
        _isCreateError.value = false
        router.navigateTo(Screens.CreateNote)
    }

    fun checkAndCreateNote(title: String?, description: String?, badgeColor: Int?) {
        if (title.isNullOrBlank()) {
            _isCreateError.value = true
            return
        } else
            createNote(title, description, badgeColor)
    }

    private fun createNote(title: String, description: String?, badgeColor: Int?) {
        viewModelScope.launch {
            repo.create(title, description, badgeColor)
            router.exit()
        }
    }

    fun clearSelection() {
        viewModelScope.launch {
            repo.clearSelection()
            selected.clear()
            _selectedCount.value = selected.size
        }
    }

    fun deleteSelected() {
        viewModelScope.launch {
            selected.forEach {
                repo.delete(it)
            }
            selected.clear()
            _selectedCount.value = selected.size
        }
    }

    fun undoDeletion() {
        viewModelScope.launch {
            repo.undoDeletion()
        }
    }

    fun realDelete() {
        viewModelScope.launch {
            repo.realDelete()
        }
    }

    override fun onNoteClick(note: Note) {
        if (selected.isEmpty()) return

        toggleSelect(note)
    }

    override fun onNoteLongClick(note: Note) {
        toggleSelect(note)
    }

    private fun toggleSelect(note: Note) {
        viewModelScope.launch {
            if (note.isSelected)
                selected.remove(note.id)
            else
                selected.add(note.id)

            repo.toggleSelect(note)

            _selectedCount.value = selected.size
        }
    }
}