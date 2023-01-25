package com.example.recyclerviewtask

import com.example.recyclerviewtask.module.User

interface Navigator {

    fun showDetails(user: User)

    fun goBack()

    fun toast(messageInt: Int)
}