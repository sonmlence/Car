package com.example.car.ui.main

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.car.R
import com.example.car.data.models.Note
import java.text.SimpleDateFormat
import java.util.*

class NoteAdapter(
    private val notes: MutableList<Note>,
    private val onNoteDeleted: (Note) -> Unit,
    private val onNotesChanged: () -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title_note)
        val tvContent: TextView = itemView.findViewById(R.id.tv_content)
        val tvDate: TextView = itemView.findViewById(R.id.tv_date)
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
        holder.tvDate.text = note.date

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val calendar = Calendar.getInstance()
            android.app.DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    android.app.TimePickerDialog(
                        context,
                        { _, hourOfDay, minute ->
                            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                            calendar.set(Calendar.MINUTE, minute)
                            val formatted = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
                            note.date = formatted.format(calendar.time)
                            holder.tvDate.text = note.date
                            onNotesChanged()
                        },
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true
                    ).show()
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        holder.itemView.setOnLongClickListener {
            val dialogView = LayoutInflater.from(holder.itemView.context)
                .inflate(R.layout.dialog_delete_note, null)
            val dialog = AlertDialog.Builder(holder.itemView.context)
                .setView(dialogView)
                .create()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialogView.findViewById<View>(R.id.btn_delete).setOnClickListener {
                val deletedNote = notes.removeAt(position)
                notifyItemRemoved(position)
                onNoteDeleted(deletedNote)
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
