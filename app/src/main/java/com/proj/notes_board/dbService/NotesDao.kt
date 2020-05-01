package com.proj.notes_board.dbService

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.proj.notes_board.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {
    @Query("SELECT * FROM Note")
    fun getAll(): Flow<List<Note>>

    @Insert(onConflict = REPLACE)
    fun insert(note: Note)

    @Update
    fun update(note: Note)

    @Query("DELETE from Note WHERE id == :id")
    fun deleteById(id: Long)

    @Query("DELETE from Note")
    fun deleteAll()
}