package com.example.recyclerviewtask

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recyclerviewtask.module.UserService
import com.example.recyclerviewtask.tasks.PendingResult
import com.example.recyclerviewtask.tasks.SuccessfulResult
import com.example.recyclerviewtask.tasks.UserResult

class UserDetailsViewModel(
    private val userService: UserService
) : BaseViewModel() {

    private val _userDetails = MutableLiveData<UserDetails>()
    val userDetails: LiveData<UserDetails> = _userDetails

    fun loadUser(id: Long) {
        if (_userDetails.value != null) return
        try {
            _userDetails.value = userService.getUserById(id)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deleteUser() {
        val user = this.userDetails.value ?: return
        userService.deleteUser(user.user)
    }

    data class State(
        val userDetailsResult: UserResult<UserDetails>,
        private val deletingInProgress: Boolean
    )
}