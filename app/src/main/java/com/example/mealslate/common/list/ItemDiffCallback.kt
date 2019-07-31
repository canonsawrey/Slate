package com.example.mealslate.common.list

import androidx.recyclerview.widget.DiffUtil
import java.util.*

class ItemDiffCallback<I : Item> : DiffUtil.ItemCallback<I>() {
    override fun areItemsTheSame(oldItem: I, newItem: I): Boolean {
        return oldItem.uniqueId() == newItem.uniqueId()
    }

    override fun areContentsTheSame(oldItem: I, newItem: I): Boolean {
        return Objects.equals(oldItem, newItem)
    }
}