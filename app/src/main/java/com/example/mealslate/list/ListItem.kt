package com.example.mealslate.list

import android.view.View
import androidx.core.util.Pair
import com.bumptech.glide.Glide
import com.example.mealslate.R
import com.example.mealslate.common.list.BaseViewHolder
import com.example.mealslate.common.list.Item
import kotlinx.android.synthetic.main.list_item.view.*

data class ListItem(val name: String,
                    val quantity: Double?,
                    val quantityUnit: String?): Item {

    override fun layoutId(): Int = R.layout.list_item

    override fun uniqueId(): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

            holder.itemView.listItemQuantity.text = ""
        }
//        val sharedTransitionIds = Pair.create(
//            holder.itemView.root as View,
//            "serviceDetailsContent"
//        )
        //holder.itemView.setOnClickListener { onItemClick?.invoke(this, sharedTransitionIds) }
    }
}