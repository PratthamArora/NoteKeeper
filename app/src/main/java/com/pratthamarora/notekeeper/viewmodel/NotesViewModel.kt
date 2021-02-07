package com.pratthamarora.notekeeper.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.pratthamarora.notekeeper.data.db.NotesDao
import com.pratthamarora.notekeeper.utils.SortOrder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest

class NotesViewModel @ViewModelInject constructor(private val notesDao: NotesDao) : ViewModel() {

    val searchQuery = MutableStateFlow("")
    val sortOrder = MutableStateFlow(SortOrder.BY_DATE)
    val isCompleted = MutableStateFlow(false)
    private val notesFlow = combine(
        searchQuery,
        sortOrder,
        isCompleted
    ) { query, sortOrder, isCompleted ->
        Triple(query, sortOrder, isCompleted)
    }.flatMapLatest { (query, sortOrder, isCompleted) ->
        notesDao.getNotes(query, sortOrder, isCompleted)
    }

    val notes = notesFlow.asLiveData()
}
