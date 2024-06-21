package com.alfin.githubappuser.data.database.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alfin.githubappuser.data.database.entity.FavoriteEntity

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: FavoriteEntity)

    @Delete
    fun delete(user: FavoriteEntity)

    @Query("SELECT * FROM Favorite_User ORDER BY username ASC")
    fun getAllFavoriteData(): LiveData<List<FavoriteEntity>>

    @Query("SELECT * FROM Favorite_User WHERE username = :username")
    fun getDataByUsername(username: String): LiveData<List<FavoriteEntity>>
}