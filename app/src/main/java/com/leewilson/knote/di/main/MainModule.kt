package com.leewilson.knote.di.main

import com.leewilson.knote.adapters.NoteRecyclerAdapter
import com.leewilson.knote.persistence.NoteDao
import com.leewilson.knote.repository.NotesRepository
import dagger.Module
import dagger.Provides

@Module
class MainModule {

    @Provides
    fun provideNotesRepository(dao: NoteDao): NotesRepository {
        return NotesRepository(dao)
    }
}