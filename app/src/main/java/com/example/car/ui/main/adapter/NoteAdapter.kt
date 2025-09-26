package com.example.car.ui.main

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.car.R
import com.example.car.data.models.Note

class NoteAdapter(
    private val notes: MutableList<Note>,
    private val onNotesChanged: () -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvContent: TextView = itemView.findViewById(R.id.tvContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.tvTitle.text = note.title
        holder.tvContent.text = note.text

        holder.itemView.setOnLongClickListener {
            val dialogView = LayoutInflater.from(holder.itemView.context)
                .inflate(R.layout.dialog_delete_note, null)

            val dialog = AlertDialog.Builder(holder.itemView.context)
                .setView(dialogView)
                .create()

            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

            dialogView.findViewById<View>(R.id.btn_delete).setOnClickListener {
                notes.removeAt(position)
                notifyItemRemoved(position)
                onNotesChanged()
                dialog.dismiss()
            }

            dialogView.findViewById<View>(R.id.btn_cancel).setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
            true
        }
    }

    override fun getItemCount(): Int = notes.size

    fun addNote(note: Note) {
        notes.add(0, note)
        notifyItemInserted(0)
        onNotesChanged()
    }
}
