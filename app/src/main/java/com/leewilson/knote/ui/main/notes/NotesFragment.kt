package com.leewilson.knote.ui.main.notes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.leewilson.knote.model.Note
import com.leewilson.knote.R
import com.leewilson.knote.adapters.NoteRecyclerAdapter
import com.leewilson.knote.viewmodels.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_notes.*
import javax.inject.Inject

class NotesFragment : DaggerFragment() {

    val LOG_TAG = "NotesFragment"

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

    private var recyclerAdapter: NoteRecyclerAdapter? = null
    private lateinit var viewModel: NotesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(LOG_TAG, "onCreateView")
        return inflater.inflate(R.layout.fragment_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(LOG_TAG, "onViewCreated")


        setupNav()
        initRecyclerView(view)
        initViewModel()
        subscribeObservers()
        setFabListener()
        viewModel.refresh()
    }

    private fun setupNav() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        NavigationUI.setupWithNavController(notes_toolbar, navController, appBarConfiguration)
    }

    private fun setFabListener() {
        floatingActionButton.setOnClickListener {
            val args = Bundle()
            args.putInt("noteId", -1)
            findNavController().navigate(R.id.action_notesFragment_to_noteDetailFragment, args)
        }
    }

    private fun subscribeObservers() {
        viewModel.notes.observe(viewLifecycleOwner, Observer { listNotes ->
            if (listNotes.isEmpty()) {
                // Change layout for no notes
            } else {
                recyclerAdapter?.submitList(listNotes)
            }
        })
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this, viewModelProviderFactory)
            .get(NotesViewModel::class.java)
    }

    private fun initRecyclerView(view: View) {
        recyclerAdapter = NoteRecyclerAdapter(object : NoteRecyclerAdapter.Interaction {
            override fun onItemSelected(position: Int, item: Note) {
                val args = Bundle()
                args.putInt("noteId", item.pk)
                findNavController().navigate(R.id.action_notesFragment_to_noteDetailFragment, args)
            }
        })
        val gridLayoutManager = StaggeredGridLayoutManager(2, VERTICAL)
        notes_recyclerview.apply {
            adapter = recyclerAdapter
            layoutManager = gridLayoutManager
        }
    }

    override fun onDestroyView() {
        Log.d(LOG_TAG, "onDestroyView")
        super.onDestroyView()
        recyclerAdapter = null
    }
}