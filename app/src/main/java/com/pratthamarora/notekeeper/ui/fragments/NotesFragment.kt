package com.pratthamarora.notekeeper.ui.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.pratthamarora.notekeeper.R
import com.pratthamarora.notekeeper.databinding.FragmentAllNotesBinding
import com.pratthamarora.notekeeper.ui.adapter.NotesAdapter
import com.pratthamarora.notekeeper.utils.onQueryTextChanged
import com.pratthamarora.notekeeper.viewmodel.NotesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotesFragment : Fragment(R.layout.fragment_all_notes) {

    private val notesViewModel by viewModels<NotesViewModel>()
    private val notesAdapter by lazy {
        NotesAdapter()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_notes, menu)

        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.onQueryTextChanged {
            notesViewModel.searchQuery.value = it
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort_name -> {
                true
            }
            R.id.action_sort_date_created -> {
                true
            }
            R.id.action_hide_completed -> {
                item.isChecked = !item.isChecked
                true
            }
            R.id.action_delete_completed -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

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