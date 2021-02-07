package com.pratthamarora.notekeeper.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.pratthamarora.notekeeper.data.db.NotesDao

class NotesViewModel @ViewModelInject constructor(private val notesDao: NotesDao) : ViewModel() {

    val notes = notesDao.getAllNotes().asLiveData()
}