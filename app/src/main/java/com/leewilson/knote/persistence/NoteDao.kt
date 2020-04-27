package com.leewilson.knote.persistence

import androidx.room.*
import com.leewilson.knote.model.Note

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes ORDER BY creation_date")
    suspend fun getAllNotes(): List<Note>

    @Query("SELECT * FROM notes WHERE pk = :pk LIMIT 1")
    suspend fun getNoteById(pk: Int): Note

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(note: Note)

    @Query("UPDATE notes SET note = :newValue WHERE pk = :noteId")
    suspend fun updateNote(newValue: String, noteId: Int)

    @Delete
    suspend fun deleteNote(note: Note)

    @Delete
    suspend fun deleteNotes(notes: List<Note>)
}