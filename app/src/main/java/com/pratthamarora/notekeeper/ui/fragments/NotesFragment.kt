package com.pratthamarora.notekeeper.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.pratthamarora.notekeeper.R
import com.pratthamarora.notekeeper.databinding.FragmentAllNotesBinding
import com.pratthamarora.notekeeper.ui.adapter.NotesAdapter
import com.pratthamarora.notekeeper.viewmodel.NotesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotesFragment : Fragment(R.layout.fragment_all_notes) {

    private val notesViewModel by viewModels<NotesViewModel>()
    private val notesAdapter by lazy {
        NotesAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAllNotesBinding.bind(view)
        binding.apply {
            allNotesRecyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = notesAdapter
                setHasFixedSize(true)
            }
        }

        notesViewModel.notes.observe(viewLifecycleOwner) {
            it?.let {
                notesAdapter.submitList(it)
            }
        }
    }
}