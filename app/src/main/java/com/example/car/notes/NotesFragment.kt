package com.example.car.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.car.R
import com.example.car.data.AppDatabase
import com.example.car.data.models.Note
import com.example.car.pref.Prefs
import com.example.car.ui.main.NoteAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class NotesFragment : Fragment() {

    private lateinit var notesAdapter: NoteAdapter
    private val notesList = mutableListOf<Note>()
    private lateinit var db: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = AppDatabase.getDatabase(requireContext())

        val prefs = Prefs(requireContext())
        val imgProfile = view.findViewById<ImageView>(R.id.img_profile)
        val tvUserName = view.findViewById<TextView>(R.id.tv_user_name)
        val btnLogout = view.findViewById<Button>(R.id.btn_logout)

        tvUserName.text = prefs.getUserName() ?: "Без имени"
        Glide.with(this)
            .load(prefs.getUserPhoto())
            .placeholder(R.mipmap.ic_launcher)
            .circleCrop()
            .into(imgProfile)

        btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            prefs.setUserLoggedIn(false)
            requireActivity().finishAffinity()
        }

        val rvNotes = view.findViewById<RecyclerView>(R.id.rv_notes)
        notesAdapter = NoteAdapter(
            notesList,
            onNoteDeleted = { note -> deleteNote(note) },
            onNotesChanged = { updateNotes() }
        )
        rvNotes.layoutManager = LinearLayoutManager(requireContext())
        rvNotes.adapter = notesAdapter

        loadNotes()

        val fabAdd = view.findViewById<FloatingActionButton>(R.id.btn_add)
        fabAdd.setOnClickListener {
            showAddNoteDialog(rvNotes)
        }

        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(
                left = systemBars.left,
                top = systemBars.top,
                right = systemBars.right,
                bottom = systemBars.bottom
            )
            insets
        }
        ViewCompat.requestApplyInsets(view)
    }

    private fun showAddNoteDialog(rvNotes: RecyclerView) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_note, null)
        val etNoteTitle = dialogView.findViewById<EditText>(R.id.etNoteTitle)
        val etNoteText = dialogView.findViewById<EditText>(R.id.etNoteText)

        AlertDialog.Builder(requireContext())
            .setTitle("Новая заметка")
            .setView(dialogView)
            .setPositiveButton("Добавить") { dialog, _ ->
                val title = etNoteTitle.text.toString().trim()
                val text = etNoteText.text.toString().trim()
                if (title.isNotEmpty() || text.isNotEmpty()) {
                    val calendar = Calendar.getInstance()
                    android.app.DatePickerDialog(
                        requireContext(),
                        { _, year, month, day ->
                            calendar.set(year, month, day)
                            android.app.TimePickerDialog(
                                requireContext(),
                                { _, hour, minute ->
                                    calendar.set(Calendar.HOUR_OF_DAY, hour)
                                    calendar.set(Calendar.MINUTE, minute)
                                    val formattedDate =
                                        SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
                                            .format(calendar.time)
                                    val newNote =
                                        Note(title = title, text = text, date = formattedDate)
                                    addNote(newNote, rvNotes)
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
                dialog.dismiss()
            }
            .setNegativeButton("Отмена") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun addNote(note: Note, rvNotes: RecyclerView) {
        lifecycleScope.launch(Dispatchers.IO) {
            db.noteDao().insert(note)
            val updatedList = db.noteDao().getAllNotes()
            withContext(Dispatchers.Main) {
                notesList.clear()
                notesList.addAll(updatedList)
                notesAdapter.notifyDataSetChanged()
                rvNotes.scrollToPosition(0)
            }
        }
    }

    private fun loadNotes() {
        lifecycleScope.launch(Dispatchers.IO) {
            val loadedNotes = db.noteDao().getAllNotes()
            withContext(Dispatchers.Main) {
                notesList.clear()
                notesList.addAll(loadedNotes)
                notesAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun deleteNote(note: Note) {
        lifecycleScope.launch(Dispatchers.IO) {
            db.noteDao().delete(note)
            updateNotes()
        }
    }

    private fun updateNotes() {
        lifecycleScope.launch(Dispatchers.IO) {
            val updatedList = db.noteDao().getAllNotes()
            withContext(Dispatchers.Main) {
                notesList.clear()
                notesList.addAll(updatedList)
                notesAdapter.notifyDataSetChanged()
            }
        }
    }
}
