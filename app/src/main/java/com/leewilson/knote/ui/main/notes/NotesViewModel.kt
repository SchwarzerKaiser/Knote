package com.leewilson.knote.ui.main.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leewilson.knote.model.Note
import com.leewilson.knote.repository.NotesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class NotesViewModel @Inject constructor(
    private val repository: NotesRepository
) : ViewModel() {

    private val _notes: MutableLiveData<List<Note>> = object : MutableLiveData<List<Note>>() {
        override fun onActive() {
            viewModelScope.launch(Dispatchers.Main) {
                requestNotes()
            }
        }
    }

    val notes: LiveData<List<Note>>
        get() = _notes

    private suspend fun requestNotes() {
        val notes = repository.getAllNotes()
        _notes.value = notes
    }

    fun refresh() {
        viewModelScope.launch {
            requestNotes()
        }
    }
}