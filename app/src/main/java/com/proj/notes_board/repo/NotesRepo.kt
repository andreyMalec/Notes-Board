package com.proj.notes_board.repo

import com.proj.notes_board.dbService.NotesDao
import com.proj.notes_board.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class NotesRepo(private val dao: NotesDao) {
    fun getAll(): Flow<List<Note>> = dao.getAll()

    suspend fun createNote(note: Note) =
        withContext(Dispatchers.IO) {
            dao.insert(note)
        }

    suspend fun deleteNote(note: Note) =
        withContext(Dispatchers.IO) {
            dao.deleteById(note.id)
        }
}