package com.alfin.githubappuser.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alfin.githubappuser.data.adapter.UsersAdapter
import com.alfin.githubappuser.databinding.FragmentFollowBinding
import com.alfin.githubappuser.model.DetailViewModel

class FollowFragment : Fragment() {
    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var adapter: UsersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var position = 0
        var username = arguments?.getString(USERNAME)

        setAdapter()

        detailViewModel = ViewModelProvider(
            requireActivity(), ViewModelProvider.NewInstanceFactory()
        )[DetailViewModel::class.java]

        arguments?.let {
            position = it.getInt(POSITION)
            username = it.getString(USERNAME)
        }

        if (position == 1) {
            showLoading(true)
            username?.let {
                detailViewModel.getFollower(it)
            }
        } else {
            showLoading(true)
            username?.let {
                detailViewModel.getFollowing(it)
            }
        }

        detailViewModel.followers.observe(viewLifecycleOwner) {
            if (position == 1) {
                adapter.ListUser(it)
            }
            showLoading(false)
        }

        detailViewModel.following.observe(viewLifecycleOwner) {
            if (position == 2) {
                adapter.ListUser(it)
            }
            showLoading(false)
        }

        detailViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    private fun setAdapter() {
        adapter = UsersAdapter()
        binding.rvUser.adapter = adapter
        binding.rvUser.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val POSITION = "position"
        const val USERNAME = "username"
    }
}