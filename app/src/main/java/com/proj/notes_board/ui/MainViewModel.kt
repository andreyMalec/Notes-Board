package com.proj.notes_board.ui

import androidx.lifecycle.ViewModel
import com.proj.notes_board.ui.navigation.Screens
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class MainViewModel @Inject constructor(private val router: Router) : ViewModel() {
    fun init() {
        router.newRootScreen(Screens.Notes)
    }
}