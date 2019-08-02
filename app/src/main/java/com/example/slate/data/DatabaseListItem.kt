package com.example.slate.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DatabaseListItem(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "item_name") val name: String,
    @ColumnInfo(name = "quantity") val quantity: Double?,
    @ColumnInfo(name = "quantity_unit") val quantityUnit: String?
)