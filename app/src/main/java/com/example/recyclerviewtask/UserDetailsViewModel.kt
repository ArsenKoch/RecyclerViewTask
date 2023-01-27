package com.example.recyclerviewtask

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recyclerviewtask.module.User
import com.example.recyclerviewtask.module.UserService

class UserDetailsViewModel(
    private val userService: UserService
) : ViewModel() {

    private val _userDetails = MutableLiveData<User>()
    val userDetails: LiveData<User> = _userDetails

    fun loadUser(id: Long) {

    }

    fun deleteUser() {
        val user = this.userDetails.value ?: return
        userService.deleteUser(user)
    }
}