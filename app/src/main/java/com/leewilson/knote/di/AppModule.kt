package com.leewilson.knote.di

import android.app.Application
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.leewilson.knote.persistence.AppDatabase
import com.leewilson.knote.persistence.AppDatabase.Companion.DATABASE_NAME
import com.leewilson.knote.persistence.NoteDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideGlideInstance(application: Application): RequestManager {
        return Glide.with(application)
    }

    @Singleton
    @Provides
    fun provideAppDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideNoteDao(db: AppDatabase): NoteDao {
        return db.getNoteDao()
    }
}