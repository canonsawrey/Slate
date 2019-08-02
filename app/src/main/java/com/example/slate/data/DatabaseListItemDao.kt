package com.example.slate.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Single

@Dao
interface DatabaseListItemDao {
    @Query("SELECT * FROM DatabaseListItem")
    fun getAllItems(): Single<List<DatabaseListItem>>


    @Insert
    fun insert(item: DatabaseListItem)
}