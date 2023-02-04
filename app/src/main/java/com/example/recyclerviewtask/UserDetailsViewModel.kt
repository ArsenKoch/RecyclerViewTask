package com.example.recyclerviewtask

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recyclerviewtask.module.UserService
import com.example.recyclerviewtask.tasks.EmptyResult
import com.example.recyclerviewtask.tasks.PendingResult
import com.example.recyclerviewtask.tasks.SuccessfulResult
import com.example.recyclerviewtask.tasks.UserResult

class UserDetailsViewModel(
    private val userService: UserService
) : BaseViewModel() {

    private val _state = MutableLiveData<State>()
    val state: LiveData<State> = _state

    private val currentState: State get() = state.value!!

    init {
        _state.value = State(
            userDetailsResult = EmptyResult(),
            deletingInProgress = false
        )
    }

    fun loadUser(id: Long) {
        if (currentState.userDetailsResult is SuccessfulResult) return
        _state.value = currentState.copy(userDetailsResult = PendingResult())

        userService.getUserById(id)
            .onError {
                //todo
            }
            .onSuccess {
                _state.value = currentState.copy(userDetailsResult = SuccessfulResult(it))
            }
            .autoCancel()
    }

    fun deleteUser() {
        val userDetailsResult = currentState.userDetailsResult
        if (userDetailsResult !is SuccessfulResult) return
        _state.value = currentState.copy(deletingInProgress = true)
        userService.deleteUser(userDetailsResult.data.user)
            .onSuccess {
                //todo
            }
            .onError {
                _state.value = currentState.copy(deletingInProgress = false)
            }
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