package com.leewilson.knote.adapters

import android.os.Build
import android.text.Html
import android.text.Spanned
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.leewilson.knote.model.Note
import com.leewilson.knote.R
import com.leewilson.knote.ui.main.notes.state.NotesViewState
import com.leewilson.knote.ui.main.notes.state.NotesViewState.*
import com.leewilson.knote.utils.changeColor
import kotlinx.android.synthetic.main.item_note.view.*

class NoteRecyclerAdapter(
    val interaction: Interaction? = null,
    val viewState: LiveData<NotesViewState>,
    val selectedNotes: LiveData<MutableSet<Note>>,
    val lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<Note>() {

        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.pk == newItem.pk
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.note == newItem.note
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_note,
                parent,
                false
            ),
            interaction,
            selectedNotes,
            viewState,
            lifecycleOwner
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Note>) {
        differ.submitList(list)
    }

    class ViewHolder(
        itemView: View,
        private val interaction: Interaction?,
        val selectedNotes: LiveData<MutableSet<Note>>,
        val viewState: LiveData<NotesViewState>,
        val lifecycleOwner: LifecycleOwner
    ) : RecyclerView.ViewHolder(itemView) {

        private val LOG_TAG = "ViewHolder"

        private var isSelected: Boolean = false

        fun bind(item: Note): Unit = with(itemView) {

            itemView.setOnClickListener {
                // Toggle selected status
                isSelected = !isSelected

                // Send to ViewModel
                interaction?.onItemSelected(adapterPosition, item, isSelected)
            }

            itemView.setOnLongClickListener {
                isSelected = !isSelected

                interaction?.onLongPress(item)
                true
            }

            selectedNotes.observe(lifecycleOwner, Observer { notes ->
                Log.d(LOG_TAG, "Selected notes: $notes")

                // Find this note in list. If found, update appearance to selected
                notes?.let { notes ->
                    if(notes.contains(item)) {
                        note_cardview.changeColor(R.color.colorNoteBackgroundSelected)
                    } else {
                        note_cardview.changeColor(R.color.colorNoteBackground)
                    }
                }
            })

            viewState.observe(lifecycleOwner, Observer { viewState ->
                itemView.isLongClickable = viewState == DefaultViewState
            })

            setNote(item)
        }

        private fun setNote(note: Note) {
            val textView = itemView.findViewById<TextView>(R.id.item_textview)
            if(Build.VERSION.SDK_INT >= 24) {
                val spanned: Spanned = Html.fromHtml(note.note, Html.FROM_HTML_MODE_COMPACT)
                textView.text = spanned
            } else {
                textView.text = note.note
            }
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Note, isSelected: Boolean)
        fun onLongPress(note: Note)
    }
}