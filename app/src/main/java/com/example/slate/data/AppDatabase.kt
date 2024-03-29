package com.example.slate.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [DatabaseListItem::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun itemDao(): DatabaseListItemDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "slate.db")
                    .build()
            }
            return INSTANCE!!
        }
    }
}