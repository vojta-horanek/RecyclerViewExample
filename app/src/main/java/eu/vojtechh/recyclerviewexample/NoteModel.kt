package eu.vojtechh.recyclerviewexample

import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "note")
data class NoteModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String,
    var text: String,
) {
    @Ignore
    var isSelected: Boolean = false

    companion object {
        fun getInstance() = NoteModel(name = "", text = "")
    }

    object DiffCallback : DiffUtil.ItemCallback<NoteModel>() {
        override fun areItemsTheSame(oldItem: NoteModel, newItem: NoteModel) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: NoteModel, newItem: NoteModel) = oldItem.isSelected == newItem.isSelected
    }
}