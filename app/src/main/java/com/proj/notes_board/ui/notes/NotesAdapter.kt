package com.proj.notes_board.ui.notes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.proj.notes_board.databinding.ItemLayoutNoteBinding
import com.proj.notes_board.model.Note

class NotesAdapter constructor(private val vm: NoteAction) :
    ListAdapter<Note, NotesAdapter.NoteItemViewHolder>(diffUtilCallback) {
    companion object {
        private val diffUtilCallback = object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.id == newItem.id &&
                        oldItem.title == newItem.title &&
                        oldItem.description == newItem.description &&
                        oldItem.createdDate == newItem.createdDate &&
                        oldItem.noteColor == newItem.noteColor
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemLayoutNoteBinding = ItemLayoutNoteBinding.inflate(inflater, parent, false)
        return NoteItemViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: NoteItemViewHolder, position: Int) {
        val note = getItem(position)

        holder.binding?.note = note
    }

    inner class NoteItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: ItemLayoutNoteBinding? = androidx.databinding.DataBindingUtil.bind(view)

    }

    interface NoteAction {

    }
}