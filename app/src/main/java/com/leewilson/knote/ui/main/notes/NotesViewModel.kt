package com.leewilson.knote.ui.main.notes

import android.util.Log
import androidx.lifecycle.*
import com.leewilson.knote.model.Note
import com.leewilson.knote.repository.NotesRepository
import com.leewilson.knote.ui.main.notes.state.NotesViewState
import com.leewilson.knote.ui.main.notes.state.NotesViewState.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class NotesViewModel @Inject constructor(
    private val repository: NotesRepository
) : ViewModel() {

    val LOG_TAG = "NotesViewModel"

    // LiveData fields

    private val _viewState: MutableLiveData<NotesViewState> = MutableLiveData()
    val viewState: LiveData<NotesViewState>
        get() = _viewState

    private val _notes: MutableLiveData<List<Note>> = object : MutableLiveData<List<Note>>() {
        override fun onActive() {
            viewModelScope.launch(Dispatchers.Main) {
                requestNotes()
            }
        }
    }
    val notes: LiveData<List<Note>>
        get() = _notes

    private val _selectedNotes: MediatorLiveData<MutableSet<Note>> = MediatorLiveData()
    val selectedNotes: LiveData<MutableSet<Note>>
        get() = _selectedNotes


    init {
        _selectedNotes.addSource(_viewState) { viewState ->
            if (viewState == DefaultViewState)
                _selectedNotes.value = mutableSetOf()
        }
        _viewState.value = DefaultViewState
        _notes.value = ArrayList<Note>()
    }


    // Interactions

    fun selectNote(note: Note) {
        val update = _selectedNotes.value
        update?.add(note)
        _selectedNotes.value = update
    }

    fun deselectNote(note: Note) {
        val update = _selectedNotes.value
        update?.remove(note)
        _selectedNotes.value = update

        // If there are no notes left selected, switch to default mode
        if(_selectedNotes.value?.size == 0)
            _viewState.value = DefaultViewState
    }

    fun setViewState(viewState: NotesViewState) {
        _viewState.value = viewState
    }

    private suspend fun requestNotes() {
        val notes = repository.getAllNotes()
        _notes.value = notes
    }

    fun deleteSelectedNotes() {
        viewModelScope.launch {
            val notes = _selectedNotes.value?.toList()
            Log.d(LOG_TAG, "deleteSelectedNotes: Deleting notes: $notes")
            notes?.let { repository.deleteNotes(it) }
            setViewState(DefaultViewState)
            requestNotes()
        }
    }
}