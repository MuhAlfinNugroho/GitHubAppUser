package com.alfin.githubappuser.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.alfin.githubappuser.data.response.ItemsItem
import com.alfin.githubappuser.data.response.ResponseGitHub
import com.alfin.githubappuser.data.retrofit.ApiConfig
import com.alfin.githubappuser.setting.SettingPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.IllegalArgumentException

class MainViewModel(private val pref: SettingPreferences) : ViewModel() {

    private val _listUser = MutableLiveData<List<ItemsItem>>()
    val listUser: LiveData<List<ItemsItem>> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        getUser()
    }

    companion object {
        private const val TAG = "MainViewModel"
        private const val DEFAULT_QUERY = "alfin"
    }

    init {
        searchUser(DEFAULT_QUERY)

    }

    private fun getUser() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().Users()
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _listUser.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun searchUser(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().searchUsers(query)
        client.enqueue(object: Callback<ResponseGitHub> {
            override fun onResponse(
                call: Call<ResponseGitHub>,
                response: Response<ResponseGitHub>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _listUser.value = response.body()!!.items
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseGitHub>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
    fun getTheme(): LiveData<Boolean>{
        return pref.getThemeSetting().asLiveData()
    }

    class ViewModelFactory(private val pref: SettingPreferences) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(pref) as T
            }
            throw IllegalArgumentException("Unknown ViewModel Class: " + modelClass.name)
        }
    }

}