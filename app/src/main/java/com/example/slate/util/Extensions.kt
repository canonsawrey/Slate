package com.example.slate.util

import android.icu.util.LocaleData
import com.example.slate.data.DatabaseListItem
import com.example.slate.list.ListItem
import java.util.*

val <T> T.exhaustive: T
    get() = this


fun DatabaseListItem.toListItem(): ListItem {
    return ListItem(
        this.name,
        this.quantity,
        this.quantityUnit,
        this.created.toBackportZonedDateTime(),
        null)
}

fun ListItem.toDatabaseListItem(): DatabaseListItem {
    return DatabaseListItem(
        0,
        this.name,
        this.quantity,
        this.quantityUnit,
        this.created.toEpochSecond()
    )
}

fun Calendar.addDays(days: Int): Calendar {
    val returnCalendar = Calendar.getInstance()
    returnCalendar.set(Calendar.DAY_OF_YEAR, this.get(Calendar.DAY_OF_YEAR) + days)
    return returnCalendar
}