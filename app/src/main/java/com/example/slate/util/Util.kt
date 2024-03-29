package com.example.slate.util

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
            1 -> "Sunday"
            2 -> "Monday"
            3 -> "Tuesday"
            4 -> "Wednesday"
            5 -> "Thursday"
            6 -> "Friday"
            7 -> "Saturday"
            else -> throw IllegalArgumentException("Only days 1 -> 7 supported/exist")
        }
    }
}
