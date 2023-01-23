package com.example.recyclerviewtask

import com.example.recyclerviewtask.module.User

interface UserActionListener {

    fun onUserMove(user: User, moveBy: Int)

    fun onUserDelete(user: User)

    fun onUserDetails(user: User)
}