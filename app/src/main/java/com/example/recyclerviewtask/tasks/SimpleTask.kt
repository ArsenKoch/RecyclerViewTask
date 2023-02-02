package com.example.recyclerviewtask.tasks

import android.os.Looper
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future

private val executorService = Executors.newCachedThreadPool()
private val handler = android.os.Handler(Looper.getMainLooper())

class SimpleTask<T>(
    private val callable: Callable<T>
) : Task<T> {

    private val future: Future<*>
    private var result: Result<T> = PendingResult()

    init {
        future = executorService.submit {
            result = try {
                SuccessfulResult(callable.call())
            } catch (e: Throwable) {
                ErrorResult(e)
            }
            notifyListeners()
        }
    }

    private var valueCallback: Callback<T>? = null
    private var errorCallback: Callback<Throwable>? = null

    override fun onSuccess(callback: Callback<T>): Task<T> {
        this.valueCallback = callback
        notifyListeners()
        return this
    }

    override fun onError(callback: Callback<Throwable>): Task<T> {
        this.errorCallback = callback
        notifyListeners()
        return this
    }

    override fun cancel() {
        TODO("Not yet implemented")
    }

    override fun await(): T {
        TODO("Not yet implemented")
    }

    private fun notifyListeners() {
        handler.post {
            val result = this.result
            val callback = this.valueCallback
            val errorCallback = this.errorCallback
            if (result is SuccessfulResult && callback != null) {
                callback(result.data)
                clearCallbacks()
            } else if (result is ErrorResult && errorCallback != null) {
                errorCallback.invoke(result.error)
                clearCallbacks()
            }
        }
    }

    private fun clearCallbacks() {
        errorCallback = null
        valueCallback = null
    }
}