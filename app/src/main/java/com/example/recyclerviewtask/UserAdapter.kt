package com.example.recyclerviewtask

import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.example.recyclerviewtask.databinding.ItemUserBinding
import com.example.recyclerviewtask.module.User

class UserAdapter(
    private val actionListener: UserActionListener
) : Adapter<UserViewHolder>(), View.OnClickListener {

    var users: List<UserListItem> = emptyList()
        set(value) {
            val diffCallback = UserDiffCallback(field, value)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            field = value
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserBinding.inflate(inflater, parent, false)

        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val userListItem = users[position]
        val user = userListItem.user
        val context = holder.itemView.context

        with(holder.binding) {
            holder.itemView.tag = user
            ivMore.tag = user

            if (userListItem.isInProgress) {
                ivMore.visibility = View.INVISIBLE
                pbItem.visibility = View.VISIBLE
                holder.binding.root.setOnClickListener(null)
            } else {
                ivMore.visibility = View.VISIBLE
                pbItem.visibility = View.GONE
                holder.binding.root.setOnClickListener(this@UserAdapter)
            }

            tvCompany.text =
                user.company.ifBlank { context.getString(R.string.unemployed) }
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
                showPopupMenu(p)
            }
            else -> {
                actionListener.onUserDetails(user)
            }
        }
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(view.context, view)
        val context = view.context
        val user = view.tag as User
        val position = users.indexOfFirst { it.user.id == user.id }

        popupMenu.menu.add(0, ID_MOVE_UP, Menu.NONE, context.getString(R.string.move_up)).apply {
            isEnabled = position > 0
        }
        popupMenu.menu.add(0, ID_MOVE_DOWN, Menu.NONE, context.getString(R.string.move_down))
            .apply {
                isEnabled = position < users.size - 1
            }
        popupMenu.menu.add(0, ID_REMOVE, Menu.NONE, context.getString(R.string.remove))
        if (user.company.isNotBlank()) {
            popupMenu.menu.add(0, ID_FIRE, Menu.NONE, context.getString(R.string.fire))
        }

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                ID_MOVE_UP -> {
                    actionListener.onUserMove(user, -1)
                }
                ID_MOVE_DOWN -> {
                    actionListener.onUserMove(user, 1)
                }
                ID_REMOVE -> {
                    actionListener.onUserDelete(user)
                }
                ID_FIRE -> {
                    actionListener.onUserFire(user)
                }
            }
            return@setOnMenuItemClickListener true
        }
        popupMenu.show()
    }

    companion object {
        private const val ID_MOVE_UP = 1
        private const val ID_MOVE_DOWN = 2
        private const val ID_REMOVE = 3
        private const val ID_FIRE = 4
    }
}