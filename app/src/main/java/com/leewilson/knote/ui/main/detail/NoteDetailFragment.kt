package com.leewilson.knote.ui.main.detail

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.leewilson.knote.R
import com.leewilson.knote.model.Note
import com.leewilson.knote.viewmodels.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_note_detail.*
import javax.inject.Inject

class NoteDetailFragment : DaggerFragment() {

    private val LOG_TAG = "NoteDetailFragment"
    private val NEW_NOTE = -1

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

    private lateinit var viewModel: NoteDetailViewModel
    private var noteId: Int = -1
    private var thisNote: Note? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_note_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteId = arguments?.getInt("noteId") ?: -1

        initViewModel()
        setupNav()
        setUpToolbarActions()

        // If argument is -1, it's a new note, otherwise existing note
        if (noteId > NEW_NOTE) {
            subscribeObservers()
            viewModel.noteId.value = noteId
        }
    }

    private fun setUpToolbarActions() {
        editor_toolbar.inflateMenu(R.menu.note_detail_actions)
        editor_toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_edit_bold -> {
                    Log.d(LOG_TAG, "Bold selected.")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        note_edittext.append(Html.fromHtml("<b>", Html.FROM_HTML_MODE_COMPACT))
                    }
                }
            }
            true
        }
    }

    private fun setupNav() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        NavigationUI.setupWithNavController(detail_toolbar, navController, appBarConfiguration)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this, viewModelProviderFactory)
            .get(NoteDetailViewModel::class.java)
    }

    private fun subscribeObservers() {
        viewModel.note.observe(viewLifecycleOwner, Observer { note ->
            thisNote = note
            displayNote(note.note)
        })
    }

    private fun displayNote(txt: String) {
        txt.replace("<br />", "\n")
        if(Build.VERSION.SDK_INT >= 24) {
            val spanned: Spanned = Html.fromHtml(txt, Html.FROM_HTML_MODE_COMPACT)
            note_edittext.setText(spanned)
        } else {
            note_edittext.setText(txt)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(LOG_TAG, "Item selected: ${item.title}")
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {

        val editorString = note_edittext.text
            .toString()
            .replace("\\n".toRegex(), "<br />")

        // New note has been edited.
        if (noteId == NEW_NOTE && !editorString.isEmpty()) {
            viewModel.saveNote(
                Note(
                    pk = 0,
                    note = editorString,
                    creationDate = System.currentTimeMillis()
                )
            )
        }

        // New note has not been edited.
        if (noteId == NEW_NOTE && editorString.isEmpty()) {
            Toast.makeText(activity, "Note discarded", Toast.LENGTH_SHORT).show()
        }

        // Existing note has been edited
        if (noteId != NEW_NOTE && editorString != thisNote?.note) {
            Log.d(LOG_TAG, "Existing note has been edited")
            thisNote?.note = editorString
            viewModel.updateNote(thisNote!!)
        }

        Log.d(LOG_TAG, "onDestroyView")
        super.onDestroyView()
    }
}