package com.tunein.ui.radiolist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tunein.databinding.ItemFilterBinding

class FiltersAdapter(
    private val filters: List<FilterType>,
    private val onItemClicked: (FilterType) -> Unit
) : RecyclerView.Adapter<FiltersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(filters[position], onItemClicked)
    }

    override fun getItemCount(): Int = filters.size

    class ViewHolder(
        private val binding: ItemFilterBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FilterType, onItemClicked: (FilterType) -> Unit) {
            binding.textView.text = ContextCompat.getString(binding.root.context, item.nameResId)
            binding.root.setOnClickListener { onItemClicked(item) }
        }
    }
}