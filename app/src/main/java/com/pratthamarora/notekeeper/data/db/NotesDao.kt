package com.pratthamarora.notekeeper.data.db

import androidx.room.*
import com.pratthamarora.notekeeper.data.models.Notes
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {

    @Query("SELECT * FROM NOTES_TABLE")
    fun getAllNotes(): Flow<List<Notes>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Notes)

    @Update
    suspend fun updateNote(note: Notes)

    @Delete
    suspend fun deleteNote(note: Notes)

}