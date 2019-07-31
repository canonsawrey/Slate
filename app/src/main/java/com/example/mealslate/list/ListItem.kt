package com.example.mealslate.list

import com.example.mealslate.R
import com.example.mealslate.common.list.BaseViewHolder
import com.example.mealslate.common.list.Item

data class ListItem(val name: String,
                    val quantity: Double?,
                    val quantityUnit: String?): Item {

    override fun layoutId(): Int = R.layout.list_item

    override fun uniqueId(): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun bind(holder: BaseViewHolder) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}