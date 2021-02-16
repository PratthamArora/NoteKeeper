package com.pratthamarora.notekeeper.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.pratthamarora.notekeeper.data.db.NotesDao
import com.pratthamarora.notekeeper.data.db.local.DataStoreManager
import com.pratthamarora.notekeeper.data.models.Notes
import com.pratthamarora.notekeeper.utils.SortOrder
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class NotesViewModel @ViewModelInject constructor(
    private val notesDao: NotesDao,
    private val dataStoreManager: DataStoreManager,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val notesChannel = Channel<NotesEvent>()
    val noteEvent = notesChannel.receiveAsFlow()
    val searchQuery = savedStateHandle.getLiveData("searchQuery", "")
    val preferences = dataStoreManager.preferences
    private val notesFlow = combine(
        searchQuery.asFlow(),
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

    fun onNoteSelected(note: Notes) = viewModelScope.launch {
        notesChannel.send(NotesEvent.NavigateToEditNoteScreen(note))
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

    fun onAddNewNote() = viewModelScope.launch {
        notesChannel.send(NotesEvent.NavigateToAddNoteScreen)
    }


    sealed class NotesEvent {
        data class ShowSnackBarWithUndo(val note: Notes) : NotesEvent()
        data class NavigateToEditNoteScreen(val note: Notes) : NotesEvent()
        object NavigateToAddNoteScreen : NotesEvent()
    }

}
