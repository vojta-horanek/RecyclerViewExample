package eu.vojtechh.recyclerviewexample

import androidx.recyclerview.widget.DiffUtil

data class NoteModel(var id: Int = -1, var name: String, var text: String, var isSelected: Boolean = false) {
    object DiffCallback : DiffUtil.ItemCallback<NoteModel>() {
        override fun areItemsTheSame(oldItem: NoteModel, newItem: NoteModel) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: NoteModel, newItem: NoteModel) = oldItem.isSelected == newItem.isSelected
    }
}