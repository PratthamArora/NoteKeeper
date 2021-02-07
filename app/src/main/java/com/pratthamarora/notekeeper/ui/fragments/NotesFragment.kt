package com.pratthamarora.notekeeper.ui.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.pratthamarora.notekeeper.R
import com.pratthamarora.notekeeper.viewmodel.NotesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotesFragment : Fragment(R.layout.fragment_all_notes) {
    private val notesViewModel by viewModels<NotesViewModel>()
}