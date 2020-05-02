package com.leewilson.knote.repository

import com.leewilson.knote.model.Note
import com.leewilson.knote.persistence.NoteDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

class NotesRepository(
    val noteDao: NoteDao
) {

    suspend fun getAllNotes(): List<Note> = noteDao.getAllNotes()

    suspend fun getNoteById(pk: Int): Note = noteDao.getNoteById(pk)

    suspend fun addNote(note: Note) = noteDao.addNote(note)

    suspend fun updateNote(note: Note) = noteDao.addNote(note)

    suspend fun deleteNotes(notes: List<Note>) = noteDao.deleteNotes(notes)
}