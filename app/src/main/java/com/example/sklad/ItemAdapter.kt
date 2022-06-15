package com.example.sklad

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sklad.databinding.ItemViewBinding

class ItemAdapter(
    private val listener: (String) -> Unit,
) : ListAdapter<ItemAdapter.ListItem, ItemAdapter.ItemViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ItemViewHolder(private val binding: ItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ListItem) {
            with(binding) {
                nameItem.text = item.title
                quantityItem.text = item.quantity.toString()
                root.setOnClickListener { listener(item.id) }
            }
        }
    }

    data class ListItem(val id: String, val title: String, val quantity: Long)

    private object DiffCallback : DiffUtil.ItemCallback<ListItem>() {
        override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
            return oldItem.title == newItem.title && oldItem.quantity == newItem.quantity
        }

    }
}