package com.example.recyclerviewtask.tasks

sealed class UserResult<T> {

    fun <R> map(mapper: (T) -> R): UserResult<R> {
        if (this is SuccessfulResult) return SuccessfulResult(mapper(data))
        return this as UserResult<R>
    }
}

class SuccessfulResult<T>(
    val data: T
) : UserResult<T>()

class ErrorResult<T>(
    val error: Throwable
) : UserResult<T>()

class PendingResult<T> : UserResult<T>()

class EmptyResult<T> : UserResult<T>()