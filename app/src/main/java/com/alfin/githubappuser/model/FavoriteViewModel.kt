package com.alfin.githubappuser.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alfin.githubappuser.data.database.entity.FavoriteEntity
import com.alfin.githubappuser.data.database.room.FavoriteDao
import com.alfin.githubappuser.data.database.room.FavoriteDatabase
import com.alfin.githubappuser.repository.FavoriteRepository

class FavoriteViewModel (application: Application) : ViewModel() {
    private val FavoriteData: FavoriteRepository = FavoriteRepository(application)
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getAllFavoriteData() = FavoriteData.getAllFavorite()

    class ViewModelFactory(private val application: Application) : ViewModelProvider.NewInstanceFactory() {
        companion object {
            @Volatile
            private var INSTANCE: ViewModelFactory? = null

            @JvmStatic
            fun getInstance(application: Application): ViewModelFactory {
                if (INSTANCE == null) {
                    synchronized(ViewModelFactory::class.java) {
                        INSTANCE = ViewModelFactory(application)
                    }
                }
                return INSTANCE as ViewModelFactory
            }

        }

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
                return FavoriteViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel Class: " + modelClass.name)
        }
    }
}