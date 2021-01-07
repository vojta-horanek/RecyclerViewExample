package eu.vojtechh.recyclerviewexample

import android.content.res.ColorStateList
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import eu.vojtechh.recyclerviewexample.databinding.NoteModelItemLayoutBinding

class NoteAdapter : ListAdapter<NoteModel, NoteAdapter.ViewHolder>(NoteModel.DiffCallback) {

    private var clickListener: (view: View, note: NoteModel) -> Unit = {_, _ -> }
    private var longClickListener: (view: View, note: NoteModel) -> Unit = {_, _ -> }

    fun setClickListener(listener: (view: View, note: NoteModel) -> Unit) {
        clickListener = listener
    }

    fun setLongClickListener(listener: (view: View, note: NoteModel) -> Unit) {
        longClickListener = listener
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
            ), clickListener, longClickListener
        )
    }

    class ViewHolder(
        private val binding: NoteModelItemLayoutBinding,
        private val onClick: (view: View, note: NoteModel) -> Unit,
        private val onLongClick: (view: View, note: NoteModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(note: NoteModel) {
            binding.note = note
            with(binding.noteCard) {
                setCardSelected(note.isSelected)
                setOnLongClickListener {
                    onLongClick(this, note)
                    true
                }
                setOnClickListener {
                    onClick(this, note)
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