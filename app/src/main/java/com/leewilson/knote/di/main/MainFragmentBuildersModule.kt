package com.leewilson.knote.di.main

import com.leewilson.knote.di.main.note_detail.NoteDetailScope
import com.leewilson.knote.di.main.note_detail.NoteDetailViewModelModule
import com.leewilson.knote.di.main.notes.NotesScope
import com.leewilson.knote.di.main.notes.NotesViewModelModule
import com.leewilson.knote.ui.main.detail.NoteDetailFragment
import com.leewilson.knote.ui.main.notes.NotesFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentBuildersModule {

    @NotesScope
    @ContributesAndroidInjector(
        modules = [
            NotesViewModelModule::class
        ]
    )
    abstract fun contributeNotesFragment(): NotesFragment

    @NoteDetailScope
    @ContributesAndroidInjector(
        modules = [
            NoteDetailViewModelModule::class
        ]
    )
    abstract fun contributesNoteDetailFragment(): NoteDetailFragment
}