package com.example.recyclerviewtask

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recyclerviewtask.module.UserService
import com.example.recyclerviewtask.tasks.PendingResult
import com.example.recyclerviewtask.tasks.SuccessfulResult
import com.example.recyclerviewtask.tasks.UserResult

class UserDetailsViewModel(
    private val userService: UserService
) : BaseViewModel() {

    private val _state = MutableLiveData<State>()
    val state: LiveData<State> = _state

    fun loadUser(id: Long) {
        if (_state.value != null) return
        try {
            _state.value = userService.getUserById(id)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deleteUser() {
        val user = this.state.value ?: return
        userService.deleteUser(user.user)
    }

    data class State(
        val userDetailsResult: UserResult<UserDetails>,
        private val deletingInProgress: Boolean
    ) {

        val showContent: Boolean get() = userDetailsResult is SuccessfulResult
        val showInProgress: Boolean get() = userDetailsResult is PendingResult || deletingInProgress
        val enableDeleteButton: Boolean get() = !deletingInProgress
    }
}