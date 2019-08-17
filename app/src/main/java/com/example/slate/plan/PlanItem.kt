package com.example.slate.plan

import com.example.slate.main.MainActivity
import com.example.slate.R
import com.example.slate.common.list.BaseViewHolder
import com.example.slate.common.list.Item
import kotlinx.android.synthetic.main.list_item.view.*
import kotlinx.android.synthetic.main.plan_item.view.*
import java.time.LocalDateTime
import java.time.ZonedDateTime

data class PlanItem(val dayOfMonth: Int): Item {

    override fun layoutId(): Int = R.layout.plan_item

    override fun uniqueId(): Long = dayOfMonth.toLong()

    override fun bind(holder: BaseViewHolder) {
        holder.itemView.number.text = dayOfMonth.toString()

        holder.itemView.setOnClickListener {

        }
    }
}