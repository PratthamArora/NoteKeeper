package com.pratthamarora.notekeeper.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.pratthamarora.notekeeper.data.db.NotesDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest

class NotesViewModel @ViewModelInject constructor(private val notesDao: NotesDao) : ViewModel() {

    val searchQuery = MutableStateFlow("")
    private val notesFlow = searchQuery.flatMapLatest {
        notesDao.getAllNotes(it)
    }

    val notes = notesFlow.asLiveData()
}