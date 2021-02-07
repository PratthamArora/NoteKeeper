package com.pratthamarora.notekeeper.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.pratthamarora.notekeeper.data.db.NotesDao
import com.pratthamarora.notekeeper.data.db.local.DataStoreManager
import com.pratthamarora.notekeeper.utils.SortOrder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class NotesViewModel @ViewModelInject constructor(
    private val notesDao: NotesDao,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    val searchQuery = MutableStateFlow("")
    val preferences = dataStoreManager.preferences
    private val notesFlow = combine(
        searchQuery,
        preferences
    ) { query, prefs ->
        Pair(query, prefs)
    }.flatMapLatest { (query, prefs) ->
        notesDao.getNotes(query, prefs.sortOrder, prefs.isCompleted)
    }

    fun setSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        dataStoreManager.setSortOrder(sortOrder)
    }

    fun setIsCompleted(isCompleted: Boolean) = viewModelScope.launch {
        dataStoreManager.setIsCompleted(isCompleted)
    }

    val notes = notesFlow.asLiveData()
}
