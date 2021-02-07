package com.pratthamarora.notekeeper.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.pratthamarora.notekeeper.data.db.NotesDao
import com.pratthamarora.notekeeper.data.db.local.DataStoreManager
import com.pratthamarora.notekeeper.data.models.Notes
import com.pratthamarora.notekeeper.utils.SortOrder
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class NotesViewModel @ViewModelInject constructor(
    private val notesDao: NotesDao,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val notesChannel = Channel<NotesEvent>()
    val noteEvent = notesChannel.receiveAsFlow()
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
    val notes = notesFlow.asLiveData()

    fun setSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        dataStoreManager.setSortOrder(sortOrder)
    }

    fun setIsCompleted(isCompleted: Boolean) = viewModelScope.launch {
        dataStoreManager.setIsCompleted(isCompleted)
    }

    fun onNoteSelected(note: Notes) {

    }

    fun onNoteCheckedChanged(note: Notes, checked: Boolean) = viewModelScope.launch {
        notesDao.updateNote(note.copy(isCompleted = checked))
    }

    fun onNoteSwiped(note: Notes) = viewModelScope.launch {
        notesDao.deleteNote(note)
        notesChannel.send(NotesEvent.ShowSnackBarWithUndo(note))
    }

    fun onPressedUndo(note: Notes) = viewModelScope.launch {
        notesDao.insertNote(note)
    }

    sealed class NotesEvent {
        data class ShowSnackBarWithUndo(val note: Notes) : NotesEvent()
    }

}
