package com.example.slate.util

import com.example.slate.data.DatabaseListItem
import com.example.slate.list.ListItem

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