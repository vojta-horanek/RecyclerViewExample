package eu.vojtechh.recyclerviewexample

import androidx.core.util.ObjectsCompat
import androidx.recyclerview.widget.DiffUtil

data class SampleModel(val number: Int, val title: String, val subtitle: String, val resource: Int) {
    object DiffCallback : DiffUtil.ItemCallback<SampleModel>() {
        override fun areItemsTheSame(oldItem: SampleModel, newItem: SampleModel) = oldItem.number == newItem.number
        override fun areContentsTheSame(oldItem: SampleModel, newItem: SampleModel) =
            ObjectsCompat.equals(oldItem, newItem)
    }
}