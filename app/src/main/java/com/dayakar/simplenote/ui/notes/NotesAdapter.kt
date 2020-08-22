package com.dayakar.simplenote.ui.notes

import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dayakar.simplenote.R
import com.dayakar.simplenote.databinding.NoteItemBinding
import com.dayakar.simplenote.model.Note

class NotesAdapter: ListAdapter<Note, NotesAdapter.ViewHolder>(NoteDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(NoteItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note=getItem(position)
        holder.bind(note)
    }

    class ViewHolder(val binding:NoteItemBinding):RecyclerView.ViewHolder(binding.root),
        View.OnCreateContextMenuListener {
          fun bind(note: Note){
              binding.noteItem=note
              itemView.setOnCreateContextMenuListener(this)
              itemView.setOnClickListener {
                  val noteItem= bundleOf("note" to note)
                  it.findNavController().navigate(R.id.action_notesFragment_to_addNotesFragment,noteItem)

              }
          }

        override fun onCreateContextMenu(
            contextMenu: ContextMenu?,
            view: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            contextMenu?.setHeaderTitle("Select one")
            contextMenu?.add(adapterPosition,200,0,"Edit")
            contextMenu?.add(adapterPosition,300,0,"Delete")

        }
    }

    companion object NoteDiffUtil: DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return  oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem==newItem
        }

    }


}