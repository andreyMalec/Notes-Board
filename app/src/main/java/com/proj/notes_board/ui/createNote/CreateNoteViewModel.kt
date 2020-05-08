package com.proj.notes_board.ui.createNote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proj.notes_board.repo.NotesRepo
import com.proj.notes_board.ui.navigation.Screens
import kotlinx.coroutines.launch
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class CreateNoteViewModel @Inject constructor(
    private val repo: NotesRepo,
    private val router: Router
) : ViewModel() {

    private val _isCreateError = MutableLiveData(false)
    val isCreateError: LiveData<Boolean>
        get() = _isCreateError

    init {
        _isCreateError.value = false
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
}