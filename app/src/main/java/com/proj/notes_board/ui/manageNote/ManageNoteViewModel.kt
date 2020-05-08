package com.proj.notes_board.ui.manageNote

import androidx.lifecycle.*
import com.proj.notes_board.model.Note
import com.proj.notes_board.repo.NotesRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ManageNoteViewModel @Inject constructor(
    private val repo: NotesRepo,
    private val router: Router
) : ViewModel() {

    private val _isManageError = MutableLiveData(false)
    val isManageError: LiveData<Boolean>
        get() = _isManageError

    val noteColor = MutableLiveData<Int>()
    val noteId = MutableLiveData<Long>(null)
    val note: LiveData<Note>

    init {
        _isManageError.value = false
        note = noteId.asFlow().flatMapLatest { id ->
            repo.getById(id)
        }.asLiveData()
    }

    fun deleteNote() {
        noteId.value?.let {
            viewModelScope.launch {
                repo.delete(it)
            }
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
            router.exit()
        }
    }

    fun checkAndManageNote(title: String?, description: String?) {
        if (title.isNullOrBlank()) {
            _isManageError.value = true
            return
        } else
            manageNote(title, description, note.value)
    }

    private fun manageNote(title: String, description: String?, note: Note?) {
        viewModelScope.launch {
            val color = noteColor.value
            if (color != null && color != 0)
                repo.createOrUpdate(title, description, color, note)
            else
                repo.createOrUpdate(title, description, note?.noteColor, note)
            router.exit()
        }
    }
}