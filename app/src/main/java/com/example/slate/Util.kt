package com.example.slate

import android.view.View
import com.example.slate.data.DatabaseListItem
import com.example.slate.list.ListItem
import java.lang.IllegalArgumentException

object Util {

    @JvmStatic
    fun enableButton(button: View) {
        button.isEnabled = true
        button.isClickable = true
        button.alpha = 1.toFloat()
    }

    @JvmStatic
    fun disableButton(button: View) {
        button.isEnabled = false
        button.isClickable = false
        button.alpha = 0.5.toFloat()
    }



    @JvmStatic
    fun listItemToDatabaseListItem(listItem: ListItem): DatabaseListItem {
        return DatabaseListItem(
            0,
            listItem.name,
            listItem.quantity,
            listItem.quantityUnit
        )
    }

    fun mapToMonthString(month: Int): String {
        return when (month) {
            1 -> "January"
            2 -> "February"
            3 -> "March"
            4 -> "April"
            5 -> "May"
            6 -> "June"
            7 -> "July"
            8 -> "August"
            9 -> "September"
            10 -> "October"
            11 -> "November"
            12 -> "December"
            else -> throw IllegalArgumentException("Only months 1 -> 12 supported/exist")
        }

    }

    fun mapToDayString(day: Int): String {
        return when (day) {
            0 -> "Sunday"
            1 -> "Monday"
            2 -> "Tuesday"
            3 -> "Wednesday"
            4 -> "Thursday"
            5 -> "Friday"
            6 -> "Saturday"
            else -> throw IllegalArgumentException("Only days 0 -> 6 supported/exist")
        }
    }
}
