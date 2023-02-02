package com.example.recyclerviewtask.module

import com.example.recyclerviewtask.UserDetails
import com.example.recyclerviewtask.UserNotFoundException
import com.example.recyclerviewtask.tasks.SimpleTask
import com.example.recyclerviewtask.tasks.Task
import com.github.javafaker.Faker
import java.util.*
import java.util.concurrent.Callable
import kotlin.collections.ArrayList

typealias UsersListener = (users: List<User>) -> Unit

class UserService {

    private var users = mutableListOf<User>()

    private val listeners = mutableSetOf<UsersListener>()
    private var loaded = false

    fun loadUser(): Task<Unit> = SimpleTask<Unit>(Callable {
        Thread.sleep(2000)
        val faker = Faker.instance()
        IMAGES.shuffle()
        users = (1..100).map {
            User(
                id = it.toLong(),
                photo = IMAGES[it % IMAGES.size],
                name = faker.name().name(),
                company = faker.company().name()
            )
        }.toMutableList()
        loaded = true
        notifyChanges()
    })

    fun getUserById(id: Long): Task<UserDetails> = SimpleTask<UserDetails>(Callable {
        Thread.sleep(2000)
        val user = users.firstOrNull { it.id == id } ?: throw UserNotFoundException()
        return@Callable UserDetails(
            user = user,
            details = Faker.instance().lorem().paragraphs(3).joinToString("\n\n")
        )
    })

    fun deleteUser(user: User): Task<Unit> = SimpleTask<Unit>(Callable {
        Thread.sleep(2000)
        val indexToDelete = users.indexOfFirst { it.id == user.id }
        if (indexToDelete != -1) {
            users = ArrayList(users)
            users.removeAt(indexToDelete)
            notifyChanges()
        }
    })

    fun moveUser(user: User, moveBy: Int) {
        val oldIndex = users.indexOfFirst { it.id == user.id }
        if (oldIndex == -1) return
        val newIndex = oldIndex + moveBy
        if (newIndex < 0 || newIndex >= users.size) return
        users = ArrayList(users)
        Collections.swap(users, oldIndex, newIndex)
        notifyChanges()
    }

    fun fireUser(user: User) {
        val index = users.indexOfFirst { it.id == user.id }
        if (index == -1) return
        val updatedUser = users[index].copy(company = "")
        users = ArrayList(users)
        users[index] = updatedUser
        notifyChanges()
    }

    fun addListener(listener: UsersListener) {
        listeners.add(listener)
        listener.invoke(users)
    }

    fun removeListener(listener: UsersListener) {
        listeners.remove(listener)
    }

    private fun notifyChanges() {
        listeners.forEach { it.invoke(users) }
    }

    companion object {
        private val IMAGES = mutableListOf(
            "https://images.unsplash.com/photo-1673675289232-4ee9463f44d4?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDF8SnBnNktpZGwtSGt8fGVufDB8fHx8&auto=format&fit=crop&w=600&q=60",
            "https://images.unsplash.com/photo-1673873861141-704ae1f082c1?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDEzfEpwZzZLaWRsLUhrfHxlbnwwfHx8fA%3D%3D&auto=format&fit=crop&w=600&q=60",
            "https://images.unsplash.com/photo-1673765270555-3cbc16d01758?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDE2fEpwZzZLaWRsLUhrfHxlbnwwfHx8fA%3D%3D&auto=format&fit=crop&w=600&q=60",
            "https://images.unsplash.com/photo-1673597715309-07d932a4f2cd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDQ1fEpwZzZLaWRsLUhrfHxlbnwwfHx8fA%3D%3D&auto=format&fit=crop&w=600&q=60",
            "https://images.unsplash.com/photo-1672655651707-593c365faabb?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDg2fEpwZzZLaWRsLUhrfHxlbnwwfHx8fA%3D%3D&auto=format&fit=crop&w=600&q=60",
            "https://images.unsplash.com/photo-1672700955551-597b052c214e?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDkxfEpwZzZLaWRsLUhrfHxlbnwwfHx8fA%3D%3D&auto=format&fit=crop&w=600&q=60",
            "https://images.unsplash.com/photo-1672151466961-8d4f93537e46?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDd8SnBnNktpZGwtSGt8fGVufDB8fHx8&auto=format&fit=crop&w=600&q=60"
        )
    }
}