package com.leewilson.knote.adapters

import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.leewilson.knote.model.Note
import com.leewilson.knote.R
import kotlinx.android.synthetic.main.fragment_note_detail.*
import kotlinx.android.synthetic.main.item_note.view.*

class NoteRecyclerAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
            interaction
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

    class ViewHolder constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Note): Unit = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }

            if(Build.VERSION.SDK_INT >= 24) {
                val spanned: Spanned = Html.fromHtml(item.note, Html.FROM_HTML_MODE_COMPACT)
                item_textview.setText(spanned)
            } else {
                item_textview.setText(item.note)
            }
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Note)
    }
}