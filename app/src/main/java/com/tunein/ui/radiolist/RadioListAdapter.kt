package com.tunein.ui.radiolist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tunein.databinding.ItemRadioBinding

class RadioListAdapter(
    private val onItemClicked: (RadioListItem) -> Unit
) : RecyclerView.Adapter<RadioListAdapter.ViewHolder>() {

    private val adapterItems: MutableList<RadioListItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRadioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = adapterItems.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(adapterItems[position], onItemClicked)
    }

    fun submitList(newItems: List<RadioListItem>) {
        val diffCallback = RadioStationDiffCallback(adapterItems, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        adapterItems.clear()
        adapterItems.addAll(newItems)

        diffResult.dispatchUpdatesTo(this)
    }

    class ViewHolder(
        private val binding: ItemRadioBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RadioListItem, onItemClicked: (RadioListItem) -> Unit) {
            with(binding) {
                radioName.text = item.radioStation.name
                iconRadio.visibility = if (item.isPlaying) View.VISIBLE else View.INVISIBLE
                root.setOnClickListener {
                    onItemClicked(item)
                }
            }
        }
    }

    class RadioStationDiffCallback(
        private val oldList: List<RadioListItem>,
        private val newList: List<RadioListItem>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition].radioStation.id == newList[newItemPosition].radioStation.id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition] == newList[newItemPosition]
    }
}