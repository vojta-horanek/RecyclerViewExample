package eu.vojtechh.recyclerviewexample

import android.content.res.ColorStateList
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import eu.vojtechh.recyclerviewexample.databinding.NoteModelItemLayoutBinding

class NoteAdapter(
    private val listener: NoteItemListener
) : ListAdapter<NoteModel, NoteAdapter.ViewHolder>(NoteModel.DiffCallback) {

    interface NoteItemListener {
        fun onNoteClick(view: View, note: NoteModel)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            NoteModelItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), listener
        )
    }

    class ViewHolder(
        private val binding: NoteModelItemLayoutBinding,
        private val listener: NoteItemListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(note: NoteModel) {
            binding.note = note
            with(binding.noteCard) {
                setCardSelected(note.isSelected)
                setOnClickListener() {
                    note.isSelected = !note.isSelected
                    setCardSelected(note.isSelected)
                    listener.onNoteClick(this, note)
                }
            }
            binding.executePendingBindings()
        }

        private fun MaterialCardView.setCardSelected(selected: Boolean) {
            if (selected) {
                /*
                cardElevation = resources.getDimension(R.dimen.elevation)
                strokeWidth = 0
                */
                val typedValue = TypedValue()
                context.theme.resolveAttribute(
                    R.attr.colorControlHighlight,
                    typedValue,
                    true
                )
                setCardForegroundColor(
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            context,
                            typedValue.resourceId
                        )
                    )
                )
            } else {
                /*
                cardElevation = 0F
                strokeWidth = resources.getDimension(R.dimen.stroke_width).toInt()
                */
                setCardForegroundColor(null)
            }
        }
    }
}