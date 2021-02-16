package com.pratthamarora.notekeeper.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.pratthamarora.notekeeper.R
import com.pratthamarora.notekeeper.databinding.FragmentAddNoteBinding
import com.pratthamarora.notekeeper.viewmodel.AddEditNoteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEditNoteFragment : Fragment(R.layout.fragment_add_note) {

    private val viewModel by viewModels<AddEditNoteViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAddNoteBinding.bind(view)
        binding.apply {
            noteName.setText(viewModel.noteName)
            noteImportantCheckBox.isChecked = viewModel.noteImportance
            noteImportantCheckBox.jumpDrawablesToCurrentState()  // animation
            dateCreated.isVisible = viewModel.note != null
            dateCreated.text = "Created: ${viewModel.note?.dateCreatedFormatted}"

        }

    }
}
