package com.example.recyclerviewtask.tasks

sealed class Result<T>

class SuccessfulResult<T>(
    val data: T
) : Result<T>()

class ErrorResult<T>(
    val error: Throwable
) : Result<T>()

class PendingResult<T> : Result<T>()

class EmptyResult<T> : Result<T>()