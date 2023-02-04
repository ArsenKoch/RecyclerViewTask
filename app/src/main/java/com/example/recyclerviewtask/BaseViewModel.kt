package com.example.recyclerviewtask

import androidx.lifecycle.ViewModel
import com.example.recyclerviewtask.tasks.Task

class Event<T>(
    private val value: T
)

open class BaseViewModel : ViewModel() {

    private val task = mutableListOf<Task<*>>()

    override fun onCleared() {
        super.onCleared()
        task.forEach { it.cancel() }
    }

    fun <T> Task<T>.autoCancel() {
        task.add(this)
    }
}