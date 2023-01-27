package com.example.recyclerviewtask.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recyclerviewtask.UserActionListener
import com.example.recyclerviewtask.UserAdapter
import com.example.recyclerviewtask.UserListViewModel
import com.example.recyclerviewtask.databinding.FragmentUserListBinding
import com.example.recyclerviewtask.factory
import com.example.recyclerviewtask.module.User

class UserListFragment : Fragment() {

    private lateinit var binding: FragmentUserListBinding
    private lateinit var adapter: UserAdapter

    private val viewModel: UserListViewModel by viewModels { factory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserListBinding.inflate(
            inflater,
            container,
            false
        )
        adapter = UserAdapter(object : UserActionListener {
            override fun onUserMove(user: User, moveBy: Int) {
                viewModel.moveUser(user, moveBy)
            }

            override fun onUserDelete(user: User) {
                viewModel.deleteUser(user)
            }

            override fun onUserDetails(user: User) {
            }

            override fun onUserFire(user: User) {
                viewModel.fireUser(user)
            }
        })

        viewModel.users.observe(viewLifecycleOwner) {
            adapter.users = it
        }

        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter

        return binding.root
    }
}