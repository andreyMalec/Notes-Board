package com.proj.notes_board.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Note(
    @PrimaryKey
    val id: Long,
    val title: String,
    val description: String,
    val createdDate: Long,
    val backgroundColor: Int
)