package com.leewilson.knote.ui.main.detail

import androidx.lifecycle.*
import com.leewilson.knote.model.Note
import com.leewilson.knote.repository.NotesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class NoteDetailViewModel @Inject constructor(
    private val repository: NotesRepository
) : ViewModel() {

    val noteId = MutableLiveData<Int>()

    val note: MediatorLiveData<Note> = MediatorLiveData()

    init {
        note.addSource(noteId) { id ->
            viewModelScope.launch {
                note.value = repository.getNoteById(id)
            }
        }
    }

    fun saveNote(note: Note) {
        GlobalScope.launch {
            repository.addNote(note)
        }
    }

    fun updateNote(note: Note) {
        GlobalScope.launch {
            repository.updateNote(note)
        }
    }
}