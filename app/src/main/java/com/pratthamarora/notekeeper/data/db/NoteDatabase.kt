package com.pratthamarora.notekeeper.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pratthamarora.notekeeper.data.models.Notes

@Database(entities = [Notes::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NotesDao
}