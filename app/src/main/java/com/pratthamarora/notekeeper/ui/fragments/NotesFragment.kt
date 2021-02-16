package com.pratthamarora.notekeeper.ui.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.pratthamarora.notekeeper.R
import com.pratthamarora.notekeeper.data.models.Notes
import com.pratthamarora.notekeeper.databinding.FragmentAllNotesBinding
import com.pratthamarora.notekeeper.ui.adapter.NotesAdapter
import com.pratthamarora.notekeeper.utils.SortOrder
import com.pratthamarora.notekeeper.utils.exhaustive
import com.pratthamarora.notekeeper.utils.onQueryTextChanged
import com.pratthamarora.notekeeper.viewmodel.NotesViewModel
import com.pratthamarora.notekeeper.viewmodel.NotesViewModel.NotesEvent.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotesFragment : Fragment(R.layout.fragment_all_notes), NotesAdapter.OnItemClickListener {

    private val notesViewModel by viewModels<NotesViewModel>()
    private val notesAdapter by lazy {
        NotesAdapter(this)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_notes, menu)

        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.onQueryTextChanged {
            notesViewModel.searchQuery.value = it
        }

        viewLifecycleOwner.lifecycleScope.launch {
            menu.findItem(R.id.action_hide_completed).isChecked =
                notesViewModel.preferences.first().isCompleted
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort_name -> {
                notesViewModel.setSortOrderSelected(SortOrder.BY_NAME)
                true
            }
            R.id.action_sort_date_created -> {
                notesViewModel.setSortOrderSelected(SortOrder.BY_DATE)
                true
            }
            R.id.action_hide_completed -> {
                item.isChecked = !item.isChecked
                notesViewModel.setIsCompleted(item.isChecked)
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
            ItemTouchHelper(object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val note = notesAdapter.currentList[viewHolder.adapterPosition]
                    notesViewModel.onNoteSwiped(note)
                }

            }).attachToRecyclerView(allNotesRecyclerView)

            addNoteButton.setOnClickListener {
                notesViewModel.onAddNewNote()
            }
        }

        notesViewModel.notes.observe(viewLifecycleOwner) {
            it?.let {
                notesAdapter.submitList(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            notesViewModel.noteEvent.collect { event ->
                when (event) {
                    is ShowSnackBarWithUndo -> {
                        Snackbar.make(requireView(), "Note Deleted", Snackbar.LENGTH_LONG)
                            .setAction("UNDO") {
                                notesViewModel.onPressedUndo(event.note)
                            }.show()
                    }
                    is NavigateToAddNoteScreen -> {
                        val action = NotesFragmentDirections.actionNotesFragmentToAddNoteFragment3()
                        findNavController().navigate(action)
                    }
                    is NavigateToEditNoteScreen -> {
                        val action =
                            NotesFragmentDirections.actionNotesFragmentToAddNoteFragment3(event.note)
                        findNavController().navigate(action)
                    }
                }.exhaustive
            }
        }
    }

    override fun onItemClick(note: Notes) {
        notesViewModel.onNoteSelected(note)
    }

    override fun onCheckBoxClick(note: Notes, isChecked: Boolean) {
        notesViewModel.onNoteCheckedChanged(note, isChecked)
    }
}