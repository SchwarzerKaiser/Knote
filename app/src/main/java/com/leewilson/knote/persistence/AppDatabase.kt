package com.leewilson.knote.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.leewilson.knote.model.Note

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getNoteDao(): NoteDao

    companion object {
        const val DATABASE_NAME = "app_db"
    }
}