package com.service.codingtest.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.service.codingtest.model.response.FavoriteEntity

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: FavoriteEntity)

    @Delete
    fun delete(items: FavoriteEntity)

    @Update
    fun update(items: FavoriteEntity): Int

    @Query("SELECT * FROM Favorite")
    fun loadAll(): LiveData<List<FavoriteEntity>>

    @Query("SELECT EXISTS(SELECT * FROM Favorite WHERE isbn=:isbn)")
    fun exist(isbn: String): Boolean
}
