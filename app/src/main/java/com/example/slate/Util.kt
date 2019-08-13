package com.example.slate

import android.view.View
import android.widget.TextView
import com.example.slate.data.DatabaseListItem
import com.example.slate.list.ListItem
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_add_list_item.*
import java.lang.IllegalArgumentException

object Util {

    const val PREFERENCES_FILE = "slate_preferences"

    @JvmStatic
    fun enableView(view: View) {
        view.isEnabled = true
        view.isClickable = true
        view.alpha = 1.toFloat()
    }

    @JvmStatic
    fun disableView(view: View) {
        view.isEnabled = false
        view.isClickable = false
        view.alpha = 0.5.toFloat()
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

    @JvmStatic
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
    @JvmStatic
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
