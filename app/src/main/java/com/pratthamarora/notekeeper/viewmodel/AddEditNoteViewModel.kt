package com.pratthamarora.notekeeper.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.pratthamarora.notekeeper.data.db.NotesDao
import com.pratthamarora.notekeeper.data.models.Notes

class AddEditNoteViewModel @ViewModelInject constructor(
    private val notesDao: NotesDao,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val note = savedStateHandle.get<Notes>("note")
    var noteName = savedStateHandle.get<String>("noteName") ?: note?.name ?: ""
        set(value) {
            field = value
            savedStateHandle.set("noteName", value)
        }
    var noteImportance = savedStateHandle.get<Boolean>("noteImp") ?: note?.isImportant ?: false
        set(value) {
            field = value
            savedStateHandle.set("noteImp", value)
        }

}