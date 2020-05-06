package com.proj.notes_board.repo

import com.proj.notes_board.dbService.NotesDao
import com.proj.notes_board.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.util.*

class NotesRepo(private val dao: NotesDao) {
    fun getAll(): Flow<List<Note>> = dao.getAll()

    private suspend fun add(note: Note) =
        withContext(Dispatchers.IO) {
            dao.insert(note)
        }

    suspend fun create(title: String, description: String?, color: Int?) =
        withContext(Dispatchers.IO) {
            val time = Calendar.getInstance().timeInMillis

            val newNote = Note(
                time,
                title,
                description ?: "",
                time,
                color ?: 0,
                isSelected = false,
                isDeleted = false
            )
            dao.insert(newNote)
        }

    fun getSelected() = dao.getSelected()

    suspend fun clearSelection() {
        withContext(Dispatchers.IO) {
            getSelected().first().forEach { note ->
                val selectedNote = Note(
                    note.id,
                    note.title,
                    note.description,
                    note.createdDate,
                    note.noteColor,
                    !note.isSelected,
                    note.isDeleted
                )
                dao.update(selectedNote)
            }
        }
    }

    suspend fun toggleSelect(note: Note) =
        withContext(Dispatchers.IO) {
            val selectedNote = Note(
                note.id,
                note.title,
                note.description,
                note.createdDate,
                note.noteColor,
                !note.isSelected,
                note.isDeleted
            )
            dao.update(selectedNote)
        }

    suspend fun realDelete() =
        withContext(Dispatchers.IO) {
            dao.getDeleted().forEach {
                dao.deleteById(it.id)
            }
        }

    suspend fun undoDeletion() =
        withContext(Dispatchers.IO) {
            dao.getDeleted().forEach { note ->
                val deletedNote = Note(
                    note.id,
                    note.title,
                    note.description,
                    note.createdDate,
                    note.noteColor,
                    false,
                    false
                )
                dao.update(deletedNote)
            }
        }

    suspend fun delete(id: Long) =
        withContext(Dispatchers.IO) {
            val note = dao.getById(id)
            val deletedNote = Note(
                note.id,
                note.title,
                note.description,
                note.createdDate,
                note.noteColor,
                note.isSelected,
                true
            )
            dao.update(deletedNote)
        }
}