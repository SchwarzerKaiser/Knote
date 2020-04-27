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

    val _note = MediatorLiveData<Note>()

    val note: LiveData<Note>
        get(){
            _note.addSource(noteId) { id ->
                viewModelScope.launch(Dispatchers.Main) {
                    _note.value = repository.getNoteById(pk = id)
                }
            }
            return _note
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