package eu.vojtechh.recyclerviewexample

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eu.vojtechh.recyclerviewexample.databinding.SampleItemLayoutBinding

class SampleAdapter : ListAdapter<SampleModel, SampleAdapter.ViewHolder>(SampleModel.DiffCallback) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            SampleItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    class ViewHolder(
        private val binding: SampleItemLayoutBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(sample: SampleModel) {
            binding.sample = sample
            binding.executePendingBindings()
        }
    }
}