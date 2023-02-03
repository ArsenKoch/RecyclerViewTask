package com.example.recyclerviewtask

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recyclerviewtask.module.User
import com.example.recyclerviewtask.module.UserService
import com.example.recyclerviewtask.module.UsersListener
import com.example.recyclerviewtask.tasks.*

data class UserListItem(
    val user: User,
    val isInProgress: Boolean
)

class UserListViewModel(
    private val userService: UserService
) : ViewModel() {

    private val _users = MutableLiveData<UserResult<List<UserListItem>>>()
    val users: LiveData<UserResult<List<UserListItem>>> = _users

    private val userIdsInProgress = mutableSetOf<Long>()
    private var userResult: UserResult<List<User>> = EmptyResult()
        set(value) {
            field = value
            notifyUpdates()
        }

    private val listener: UsersListener = {
        userResult = if (it.isEmpty()) {
            EmptyResult()
        } else {
            SuccessfulResult(it)
        }
    }

    init {
        addListener()
    }

    private fun addListener() {
        userService.addListener(listener)
    }

    private fun loadUser() {
        userResult = PendingResult()
        userService.loadUser()
            .onError {
                userResult = ErrorResult(it)
            }

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

    private fun isInProgress(user: User): Boolean {
        return userIdsInProgress.contains(user.id)
    }

    private fun notifyUpdates() {
        _users.value = userResult.map { users ->
            users.map { user -> UserListItem(user, isInProgress(user)) }
        }

    }
}