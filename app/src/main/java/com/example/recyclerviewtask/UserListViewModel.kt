package com.example.recyclerviewtask

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recyclerviewtask.module.User
import com.example.recyclerviewtask.module.UserService
import com.example.recyclerviewtask.module.UsersListener

class UserListViewModel(
    private val userService: UserService
) : ViewModel() {

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    init {
        loadUsers()
    }

    private val listener: UsersListener = {
        _users.value = it
    }

    fun loadUsers() {
        userService.addListener(listener)
    }

    fun deleteUser(user: User) {
        userService.deleteUser(user)
    }

    fun moveUser(user: User, moveUser: Int) {
        userService.moveUser(user, moveUser)
    }

    fun fireUser(user: User) {
        userService.fireUser(user)
    }

    override fun onCleared() {
        super.onCleared()
        userService.removeListener(listener)
    }
}