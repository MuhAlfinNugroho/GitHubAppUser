package com.alfin.githubappuser.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.alfin.githubappuser.data.database.entity.FavoriteEntity
import com.alfin.githubappuser.data.database.room.FavoriteDao
import com.alfin.githubappuser.data.database.room.FavoriteDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {
    private val favoriteDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val database = FavoriteDatabase.getDatabase(application)
        favoriteDao = database.userDao()
    }

    fun getAllFavorite(): LiveData<List<FavoriteEntity>> = favoriteDao.getAllFavoriteData()

    fun insertFavoriteUser(user: FavoriteEntity) {
        executorService.execute {
            favoriteDao.insert((user))
        }
    }

    fun deleteFavoriteUser(user: FavoriteEntity) {
        executorService.execute {
            favoriteDao.delete(user)
        }
    }

    fun getDataByUsername(username: String): LiveData<List<FavoriteEntity>> = favoriteDao.getDataByUsername(username)
}