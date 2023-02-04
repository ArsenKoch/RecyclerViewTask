package com.example.recyclerviewtask

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
) : BaseViewModel(), UserActionListener {

    private val _users = MutableLiveData<UserResult<List<UserListItem>>>()
    val users: LiveData<UserResult<List<UserListItem>>> = _users

    private val _actionShowDetails = MutableLiveData<Event<User>>()
    val actionShowDetails: LiveData<Event<User>> = _actionShowDetails

    private val _actionShowToast = MutableLiveData<Event<Int>>()
    val actionShowToast: LiveData<Event<Int>> = _actionShowToast

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
        loadUser()
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
            .autoCancel()
    }

    override fun onCleared() {
        super.onCleared()
        userService.removeListener(listener)
    }

    private fun addProgressTo(user: User) {
        userIdsInProgress.add(user.id)
        notifyUpdates()
    }

    private fun removeProgressFrom(user: User) {
        userIdsInProgress.remove(user.id)
        notifyUpdates()
    }

    private fun isInProgress(user: User): Boolean {
        return userIdsInProgress.contains(user.id)
    }

    private fun notifyUpdates() {
        _users.postValue(userResult.map { users ->
            users.map { user -> UserListItem(user, isInProgress(user)) }
        })
    }

    override fun onUserMove(user: User, moveBy: Int) {
        if (isInProgress(user)) return
        addProgressTo(user)
        userService.moveUser(user, moveBy)
            .onSuccess {
                removeProgressFrom(user)
            }
            .onError {
                removeProgressFrom(user)
                _actionShowToast.value = Event("Cant move user".toInt())
            }
            .autoCancel()
    }

    override fun onUserDelete(user: User) {
        if (isInProgress(user)) return
        addProgressTo(user)
        userService.deleteUser(user)
            .onError {
                removeProgressFrom(user)
                _actionShowToast.value = Event("Cant delete user".toInt())
            }
            .onSuccess {
                removeProgressFrom(user)
            }
            .autoCancel()
    }

    override fun onUserDetails(user: User) {
        _actionShowDetails.value = Event(user)
    }

    override fun onUserFire(user: User) {
        if (isInProgress(user))
            addProgressTo(user)
        userService.fireUser(user)
            .onError {
                removeProgressFrom(user)
            }
            .onSuccess {
                removeProgressFrom(user)
            }
            .autoCancel()
    }
}