package com.example.slate.plan

import com.example.slate.main.MainActivity
import com.example.slate.R
import com.example.slate.common.list.BaseViewHolder
import com.example.slate.common.list.Item
import com.example.slate.util.Util
import kotlinx.android.synthetic.main.list_item.view.*
import kotlinx.android.synthetic.main.plan_item.view.*
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.*

data class PlanItem(val cal: Calendar): Item {

    override fun layoutId(): Int = R.layout.plan_item

    override fun uniqueId(): Long = cal.get(Calendar.DAY_OF_YEAR).toLong()

    override fun bind(holder: BaseViewHolder) {
        holder.itemView.day.text = Util.mapToDayString(cal.get(Calendar.DAY_OF_WEEK))
        holder.itemView.date.text = String.format(
            "%s %d",
            Util.mapToMonthString(cal.get(Calendar.MONTH)),
            cal.get(Calendar.DAY_OF_MONTH)
        )

        holder.itemView.setOnClickListener {

        }
    }
}