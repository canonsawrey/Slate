package com.example.slate.list

import com.example.slate.main.MainActivity
import com.example.slate.R
import com.example.slate.common.list.BaseViewHolder
import com.example.slate.common.list.Item
import kotlinx.android.synthetic.main.list_item.view.*

data class ListItem(val name: String,
                    val quantity: Double?,
                    val quantityUnit: String?): Item {

    override fun layoutId(): Int = R.layout.list_item

    override fun uniqueId(): Long {
        return this.hashCode().toLong()
    }

    override fun bind(holder: BaseViewHolder) {
        holder.itemView.listItemText.text = name
        if (quantity != null) {
            var str = quantity.toString()
            if (quantityUnit != null) {
                str += " $quantityUnit"
            }
            holder.itemView.listItemQuantity.text = str
        } else {

            holder.itemView.listItemQuantity.text = "-"
        }

        holder.itemView.setOnClickListener {
            (holder.itemView.context as MainActivity).itemClicked(this.uniqueId())
        }
    }
}