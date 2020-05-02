package com.proj.notes_board.dbService

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.proj.notes_board.model.Note

@Database(entities = [Note::class], version = 1)
abstract class NotesDatabase : RoomDatabase() {
    companion object {
        fun instance(context: Context): NotesDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                NotesDatabase::class.java, "notesDb"
            ).build()
        }
    }

    abstract fun notesDataDao(): NotesDao
}