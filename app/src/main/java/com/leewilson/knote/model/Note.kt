package com.leewilson.knote.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "notes")
data class Note(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "pk")
    val pk: Int,

    @ColumnInfo(name = "note")
    var note: String,

    @ColumnInfo(name = "creation_date")
    var creationDate: Long
)