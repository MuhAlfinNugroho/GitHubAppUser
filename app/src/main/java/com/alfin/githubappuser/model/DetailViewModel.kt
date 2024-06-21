package com.alfin.githubappuser.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alfin.githubappuser.data.database.entity.FavoriteEntity
import com.alfin.githubappuser.data.database.room.FavoriteDao
import com.alfin.githubappuser.data.response.ItemsItem
import com.alfin.githubappuser.data.response.ResponseUser
import com.alfin.githubappuser.data.retrofit.ApiConfig
import com.alfin.githubappuser.repository.FavoriteRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : ViewModel() {
    private val _detailUser = MutableLiveData<ResponseUser>()
    val detailUser: MutableLiveData<ResponseUser> = _detailUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _followers = MutableLiveData<List<ItemsItem>>()
    val followers: LiveData<List<ItemsItem>> = _followers

    private val _following = MutableLiveData<List<ItemsItem>>()
    val following: LiveData<List<ItemsItem>> = _following

    val errorMessage = MutableLiveData<String>()


    private val FavoriteData: FavoriteRepository = FavoriteRepository(application)

    fun insertDataUser(user: FavoriteEntity) {
        FavoriteData.insertFavoriteUser(user)
    }

    fun deleteDataUser(user: FavoriteEntity) {
        FavoriteData.deleteFavoriteUser(user)
    }

    fun getDataByUsername(username: String) = FavoriteData.getDataByUsername(username)

    init {
        getDetailUser()
    }

    fun getDetailUser(username: String = "") {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUsers(username)
        client.enqueue(object : Callback<ResponseUser> {
            override fun onResponse(
                call: Call<ResponseUser>,
                response: Response<ResponseUser>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _detailUser.value = response.body()
                } else {
                    Log.d(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseUser>, t: Throwable) {
                _isLoading.value = false
                errorMessage.postValue(t.message.toString())
                Log.d(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getFollower(username: String = "") {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _followers.value = response.body()
                } else {
                    Log.d(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                Log.d(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getFollowing(username: String = "") {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _following.value = response.body()
                } else {
                    Log.d(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                Log.d(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

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
        override fun <view : ViewModel> create(modelClass: Class<view>): view {
            if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
                return DetailViewModel(application) as view
            }
            throw IllegalArgumentException("Unknown ViewModel Class: ${modelClass.name}")
        }
    }

    companion object {
        private const val TAG = "DetailViewModel"
    }
}