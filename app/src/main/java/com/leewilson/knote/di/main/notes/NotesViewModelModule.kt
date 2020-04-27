package com.leewilson.knote.di.main.notes

import androidx.lifecycle.ViewModel
import com.leewilson.knote.di.ViewModelKey
import com.leewilson.knote.ui.main.notes.NotesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class NotesViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(NotesViewModel::class)
    abstract fun bindNotesViewModel(notesViewModel: NotesViewModel): ViewModel
}