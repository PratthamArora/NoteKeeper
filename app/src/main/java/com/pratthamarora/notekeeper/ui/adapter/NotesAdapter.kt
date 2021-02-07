package com.pratthamarora.notekeeper.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pratthamarora.notekeeper.data.models.Notes
import com.pratthamarora.notekeeper.databinding.ItemNoteBinding


class NotesAdapter(private val listener: OnItemClickListener) :
    ListAdapter<Notes, NotesAdapter.NotesViewHolder>(NotesDiffCallback()) {

    inner class NotesViewHolder(private val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    val pos = adapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        val note = getItem(pos)
                        listener.onItemClick(note)
                    }
                }
                noteDoneCheckBox.setOnClickListener {
                    val pos = adapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        val note = getItem(pos)
                        listener.onCheckBoxClick(note, noteDoneCheckBox.isChecked)
                    }
                }
            }
        }

        fun bind(note: Notes) {
            binding.apply {
                with(note) {
                    noteDoneCheckBox.isChecked = isCompleted
                    noteText.text = name
                    noteText.paint.isStrikeThruText = isCompleted
                    notePriority.isVisible = isImportant
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(note: Notes)
        fun onCheckBoxClick(note: Notes, isChecked: Boolean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        return NotesViewHolder(
            ItemNoteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val note = getItem(position)
        holder.bind(note)
    }

    class NotesDiffCallback : DiffUtil.ItemCallback<Notes>() {
        override fun areItemsTheSame(oldItem: Notes, newItem: Notes): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Notes, newItem: Notes): Boolean {
            return oldItem == newItem
        }
    }
}