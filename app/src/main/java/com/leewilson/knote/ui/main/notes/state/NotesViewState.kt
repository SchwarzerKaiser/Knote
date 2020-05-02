package com.leewilson.knote.ui.main.notes.state

sealed class NotesViewState {

    object DefaultViewState : NotesViewState() {
        override fun toString(): String {
            return "DefaultViewState"
        }
    }

    object MultiSelectionState : NotesViewState() {
        override fun toString(): String {
            return "MultiSelectionState"
        }
    }
}