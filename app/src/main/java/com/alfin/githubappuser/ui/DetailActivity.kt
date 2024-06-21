package com.alfin.githubappuser.ui

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.CompoundButtonCompat
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.alfin.githubappuser.R
import com.alfin.githubappuser.data.adapter.SectionPageAdapter
import com.alfin.githubappuser.data.database.entity.FavoriteEntity
import com.alfin.githubappuser.databinding.ActivityDetailBinding
import com.alfin.githubappuser.model.DetailViewModel
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel by viewModels<DetailViewModel> {
        DetailViewModel.ViewModelFactory.getInstance(application)
    }

    private var isFav: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val username = intent.getStringExtra(EXTRA_USER) ?: ""
        val avatar = intent.getStringExtra(EXTRA_AVATAR) ?: ""
        val bundle = Bundle()
        bundle.putString(EXTRA_USER, username)

        supportActionBar?.title = getString(R.string.user)

        detailViewModel.getDetailUser(username)
        showLoading(true)

        detailViewModel.detailUser.observe(this) {
            showLoading(false)
            if (it != null) {
                binding.apply {
                    Glide.with(this@DetailActivity)
                        .load(it.avatarUrl)
                        .centerCrop()
                        .into(ivPicture)
                    name.text = it.name
                    Username.text = it.login
                    follower.text = resources.getString(R.string.follower, it.followers)
                    following.text = resources.getString(R.string.follow, it.following)
                }
            }
        }

        detailViewModel.getDataByUsername(username).observe(this) {
            isFav = it.isNotEmpty()
            val favoriteUser = FavoriteEntity(username, avatar)
            if (it.isEmpty()) {
                binding.toggleFavorite.isChecked = false
            } else {
                binding.toggleFavorite.isChecked = true
            }
        }

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        supportActionBar?.elevation = 0f

        val sectionsPagerAdapter = SectionPageAdapter(this)
        sectionsPagerAdapter.username = username
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tabLayout, position ->
            tabLayout.text = resources.getString(TAB_TITLE[position])
        }.attach()

        binding.toggleFavorite.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                detailViewModel.insertDataUser(FavoriteEntity(username, avatar))
            } else {
                detailViewModel.deleteDataUser(FavoriteEntity(username, avatar))
            }
            // Set button tint color based on isChecked status
            val color = if (isChecked) R.color.red else R.color.grey
            CompoundButtonCompat.setButtonTintList(binding.toggleFavorite, ColorStateList.valueOf(ContextCompat.getColor(this, color)))
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar2.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val EXTRA_USER = "extra_user"
        const val EXTRA_AVATAR = "extra_avatar"

        @StringRes
        private val TAB_TITLE = arrayOf(
            R.string.tab_1,
            R.string.tab_2
        )
    }
}
