package com.leewilson.knote.di.main.note_detail

import androidx.lifecycle.ViewModel
import com.leewilson.knote.di.ViewModelKey
import com.leewilson.knote.ui.main.detail.NoteDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class NoteDetailViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(NoteDetailViewModel::class)
    abstract fun bindNoteDetailViewModel(noteDetailViewModel: NoteDetailViewModel): ViewModel
}