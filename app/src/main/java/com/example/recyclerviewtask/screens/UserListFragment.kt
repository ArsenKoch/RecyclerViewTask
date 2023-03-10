package com.example.recyclerviewtask.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recyclerviewtask.UserAdapter
import com.example.recyclerviewtask.UserListViewModel
import com.example.recyclerviewtask.databinding.FragmentUserListBinding
import com.example.recyclerviewtask.factory
import com.example.recyclerviewtask.navigator
import com.example.recyclerviewtask.tasks.EmptyResult
import com.example.recyclerviewtask.tasks.ErrorResult
import com.example.recyclerviewtask.tasks.PendingResult
import com.example.recyclerviewtask.tasks.SuccessfulResult

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
        adapter = UserAdapter(viewModel)

        viewModel.users.observe(viewLifecycleOwner) {
            hideAll()
            when (it) {
                is SuccessfulResult -> {
                    binding.recyclerView.visibility = View.VISIBLE
                    adapter.users = it.data
                }
                is ErrorResult -> {
                    binding.tryAgainContainer.visibility = View.VISIBLE
                }
                is PendingResult -> {
                    binding.progressBarUserList.visibility = View.VISIBLE
                }
                is EmptyResult -> {
                    binding.tvNoUsers.visibility = View.VISIBLE
                }
            }
        }

        viewModel.actionShowDetails.observe(viewLifecycleOwner) {
            it.getValue()?.let { user -> navigator().showDetails(user) }
        }
        viewModel.actionShowToast.observe(viewLifecycleOwner) {
            it.getValue()?.let { messageInt -> navigator().toast(messageInt) }
        }

        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter

        return binding.root
    }

    private fun hideAll() {
        binding.tvNoUsers.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.progressBarUserList.visibility = View.GONE
        binding.tryAgainContainer.visibility = View.GONE
    }
}