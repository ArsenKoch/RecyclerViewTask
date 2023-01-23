package com.example.recyclerviewtask

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.example.recyclerviewtask.databinding.ItemUserBinding
import com.example.recyclerviewtask.module.User

class UserAdapter(
    private val actionListener: UserActionListener
) : Adapter<UserViewHolder>(), View.OnClickListener {

    var users: List<User> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserBinding.inflate(inflater, parent, false)

        binding.ivMore.setOnClickListener(this)
        binding.root.setOnClickListener(this)

        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        with(holder.binding) {
            holder.itemView.tag = user
            ivMore.tag = user

            tvCompany.text = user.company
            tvName.text = user.name
            if (user.photo.isNotBlank()) {
                Glide.with(ivPhoto.context)
                    .load(user.photo)
                    .circleCrop()
                    .placeholder(R.drawable.ic_person)
                    .error(R.drawable.ic_person)
                    .into(ivPhoto)
            } else {
                ivPhoto.setImageResource(R.drawable.ic_person)
            }
        }
    }

    override fun getItemCount(): Int = users.size

    override fun onClick(p: View) {
        val user = p.tag as User
        when (p.id) {
            R.id.ivMore -> {
                //todo
            }
            else -> {
                actionListener.onUserDetails(user)
            }
        }
    }
}