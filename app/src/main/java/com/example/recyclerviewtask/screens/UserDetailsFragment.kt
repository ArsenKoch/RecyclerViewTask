package com.example.recyclerviewtask.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.recyclerviewtask.R
import com.example.recyclerviewtask.UserDetailsViewModel
import com.example.recyclerviewtask.databinding.FragmentUserDetailsBinding
import com.example.recyclerviewtask.factory
import com.example.recyclerviewtask.navigator

class UserDetailsFragment : Fragment() {

    private val viewModel: UserDetailsViewModel by viewModels { factory() }
    private lateinit var binding: FragmentUserDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadUser(requireArguments().getLong(ARG_USER_ID))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserDetailsBinding.inflate(inflater, container, false)

        viewModel.userDetails.observe(viewLifecycleOwner, Observer {
            binding.tvNameFragment.text = it.user.name
            if (it.user.photo.isNotBlank()) {
                Glide.with(this)
                    .load(it.user.photo)
                    .circleCrop()
                    .into(binding.ivPhotoFragment)
            } else {
                Glide.with(this)
                    .load(R.drawable.ic_person)
                    .into(binding.ivPhotoFragment)
            }
            binding.tvUserDetailsFragment.text = it.details
        })

        binding.btnDelete.setOnClickListener {
            viewModel.deleteUser()
            navigator().toast(R.string.user_delete)
            navigator().goBack()
        }

        return binding.root
    }

    companion object {

        private const val ARG_USER_ID = "ARG_USER_ID"

        fun newInstance(userId: Long): UserDetailsFragment {
            val fragment = UserDetailsFragment()
            fragment.arguments = bundleOf(ARG_USER_ID to userId)
            return fragment
        }
    }
}