package com.example.recyclerviewtask.tasks

import java.util.concurrent.Callable

class SimpleTask<T>(
    val callable: Callable<T>
) {
}