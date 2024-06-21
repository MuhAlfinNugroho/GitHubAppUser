package com.alfin.githubappuser.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alfin.githubappuser.R
import com.alfin.githubappuser.data.adapter.FavoriteAdapter
import com.alfin.githubappuser.data.database.entity.FavoriteEntity
import com.alfin.githubappuser.databinding.ActivityFavoriteBinding
import com.alfin.githubappuser.model.FavoriteViewModel

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.title = getString(R.string.favorite)

        val favoriteViewModel = obtainViewModel(this@FavoriteActivity)
        favoriteViewModel.getAllFavoriteData().observe(this) {
            setFavoriteData(it)
        }

        favoriteViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun setFavoriteData(userEntities: List<FavoriteEntity>) {
        val items = arrayListOf<FavoriteEntity>()
        userEntities.map {
            val item = FavoriteEntity(
                username = it.username,
                avatar = it.avatar,
            )
            items.add(item)
        }
        val adapter = FavoriteAdapter(items)
        binding.rvUser.layoutManager = LinearLayoutManager(this)
        binding.rvUser.setHasFixedSize(true)
        binding.rvUser.adapter = adapter

        adapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback {
            override fun onItemClicked(data: FavoriteEntity) {
                startActivity(
                    Intent(this@FavoriteActivity,DetailActivity::class.java)
                        .putExtra(DetailActivity.EXTRA_USER, data.username)
                        .putExtra(DetailActivity.EXTRA_AVATAR, data.avatar)
                )
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = FavoriteViewModel.ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
