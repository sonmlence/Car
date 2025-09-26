package com.example.car.ui.main

import android.content.Context
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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.car.R
import com.example.car.data.models.Note
import com.example.car.pref.Prefs
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth

class NotesFragment : Fragment() {

    private lateinit var notesAdapter: NoteAdapter
    private val notesList = mutableListOf<Note>()
    private val prefsName = "notes_prefs"
    private val keyNotes = "notes_list"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            findNavController().navigate(
                R.id.authFragment,
                null,
                androidx.navigation.NavOptions.Builder()
                    .setPopUpTo(R.id.notesFragment, true)
                    .build()
            )
        }

        loadNotes()

        val rvNotes = view.findViewById<RecyclerView>(R.id.rv_notes)
        notesAdapter = NoteAdapter(notesList) { saveNotes() }
        rvNotes.layoutManager = LinearLayoutManager(requireContext())
        rvNotes.adapter = notesAdapter

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
                    val newNote = Note(title, text)
                    notesAdapter.addNote(newNote)
                    rvNotes.scrollToPosition(0)
                }
                dialog.dismiss()
            }
            .setNegativeButton("Отмена") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun saveNotes() {
        val sharedPrefs = requireContext().getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        var savedString = ""
        for (note in notesList) {
            savedString += note.title + "||" + note.text + ";;"
        }
        editor.putString(keyNotes, savedString)
        editor.apply()
    }

    private fun loadNotes() {
        val sharedPrefs = requireContext().getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        val savedString = sharedPrefs.getString(keyNotes, null)
        if (!savedString.isNullOrEmpty()) {
            val items = savedString.split(";;")
            for (item in items) {
                if (item.isEmpty()) continue
                val parts = item.split("||")
                if (parts.size == 2) {
                    notesList.add(Note(parts[0], parts[1]))
                }
            }
        }
    }
}
