package com.example.recyclerviewtask

import androidx.lifecycle.ViewModel
import com.example.recyclerviewtask.tasks.Task

class Event<T>(
    private val value: T
) {

    private var handled: Boolean = false

    fun getValue(): T? {
        if (handled) return null
        handled = true
        return value
    }
}

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