package com.pratthamarora.notekeeper.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.pratthamarora.notekeeper.data.di.ApplicationScope
import com.pratthamarora.notekeeper.data.models.Notes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Notes::class], version = 3)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NotesDao

    class Callback @Inject constructor(
        private val database: Provider<NoteDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao = database.get().noteDao()

            applicationScope.launch {
                dao.insertNote(Notes("Wash the dishes"))
                dao.insertNote(Notes("Do the laundry"))
                dao.insertNote(Notes("Buy groceries", isImportant = true))
                dao.insertNote(Notes("Prepare food", isCompleted = true))
                dao.insertNote(Notes("Call mom"))
                dao.insertNote(Notes("Visit grandma", isCompleted = true))
                dao.insertNote(Notes("Repair my bike"))
                dao.insertNote(Notes("Call Elon Musk"))
            }
        }
    }
}