package com.proj.notes_board.repo

import com.proj.notes_board.dbService.NotesDao
import com.proj.notes_board.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.*

class NotesRepo(private val dao: NotesDao) {
    fun getAll(): Flow<List<Note>> = dao.getAll()

    private suspend fun addNote(note: Note) =
        withContext(Dispatchers.IO) {
            dao.insert(note)
        }

    suspend fun createNote(title: String, description: String?, color: Int?) {
        val time = Calendar.getInstance().timeInMillis

        addNote(
            Note(
                time,
                title,
                description ?: "",
                time,
                color ?: 0
            )
        )
    }

    suspend fun deleteNote(note: Note) =
        withContext(Dispatchers.IO) {
            dao.deleteById(note.id)
        }
}