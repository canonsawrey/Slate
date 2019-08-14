package com.example.slate.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface DatabaseListItemDao {
    @Query("SELECT * FROM DatabaseListItem")
    fun getAllItems(): Flowable<List<DatabaseListItem>>


    @Insert
    fun insert(item: DatabaseListItem): Long

    @Query("DELETE FROM DatabaseListItem WHERE item_name=:name")
    fun remove(name: String)
}