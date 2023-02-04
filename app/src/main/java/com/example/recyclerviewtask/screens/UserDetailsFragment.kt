package com.example.recyclerviewtask.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.recyclerviewtask.R
import com.example.recyclerviewtask.UserDetailsViewModel
import com.example.recyclerviewtask.databinding.FragmentUserDetailsBinding
import com.example.recyclerviewtask.factory
import com.example.recyclerviewtask.navigator
import com.example.recyclerviewtask.tasks.SuccessfulResult

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

        viewModel.actionShowToast.observe(viewLifecycleOwner) {
            it.getValue()?.let { messageInt -> navigator().toast(messageInt) }
        }

        viewModel.actionGoBack.observe(viewLifecycleOwner) {
            it.getValue()?.let { navigator().goBack() }
        }

        viewModel.state.observe(viewLifecycleOwner) {
            binding.contentContainer.visibility = if (it.showContent) {
                val userDetails = (it.userDetailsResult as SuccessfulResult).data
                binding.tvNameFragment.text = userDetails.user.name
                if (userDetails.user.photo.isNotBlank()) {
                    Glide.with(this)
                        .load(userDetails.user.photo)
                        .circleCrop()
                        .into(binding.ivPhotoFragment)
                } else {
                    Glide.with(this)
                        .load(R.drawable.ic_person)
                        .into(binding.ivPhotoFragment)
                }

                binding.tvUserDetailsFragment.text = userDetails.details

                View.VISIBLE
            } else {
                View.GONE
            }

            binding.progressBar.visibility = if (it.showInProgress) View.VISIBLE else View.GONE
            binding.btnDelete.isEnabled = it.enableDeleteButton
        }

        binding.btnDelete.setOnClickListener {
            viewModel.deleteUser()
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