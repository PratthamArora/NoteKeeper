package com.pratthamarora.notekeeper.data.db

import androidx.room.*
import com.pratthamarora.notekeeper.data.models.Notes
import com.pratthamarora.notekeeper.utils.SortOrder
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {

    fun getNotes(query: String, sortOrder: SortOrder, isCompleted: Boolean): Flow<List<Notes>> =
        when (sortOrder) {
            SortOrder.BY_DATE -> getAllNotesByDate(query, isCompleted)
            SortOrder.BY_NAME -> getAllNotesByName(query, isCompleted)
        }

    @Query("SELECT * FROM NOTES_TABLE WHERE (isCompleted != :isCompleted OR isCompleted = 0) AND name LIKE '%' || :searchQuery || '%' ORDER BY isImportant DESC,name")
    fun getAllNotesByName(searchQuery: String, isCompleted: Boolean): Flow<List<Notes>>

    @Query("SELECT * FROM NOTES_TABLE WHERE (isCompleted != :isCompleted OR isCompleted = 0) AND name LIKE '%' || :searchQuery || '%' ORDER BY isImportant DESC,dateCreated")
    fun getAllNotesByDate(searchQuery: String, isCompleted: Boolean): Flow<List<Notes>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Notes)

    @Update
    suspend fun updateNote(note: Notes)

    @Delete
    suspend fun deleteNote(note: Notes)

}