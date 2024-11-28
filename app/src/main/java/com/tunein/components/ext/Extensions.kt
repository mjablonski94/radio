package com.tunein.components.ext

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.Executor
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine

fun <T> MutableLiveData<T>.readOnly(): LiveData<T> = this

fun <T> MutableLiveData<T>.postValueIfDiffers(
    newValue: T?,
    ignoreNulls: Boolean = false,
    onSet: ((newValue: T?) -> Unit)? = null,
    onNotSet: (() -> Unit)? = null
) {
    if (ignoreNulls && newValue != null && value != newValue) {
        postValue(newValue)
        onSet?.invoke(newValue)
    } else if (!ignoreNulls && value != newValue) {
        postValue(newValue)
        onSet?.invoke(newValue)
    } else onNotSet?.invoke()
}

inline fun <T> T.applyWhen(condition: Boolean, block: T.() -> T) =
    if (condition) block()
    else this


fun CoroutineContext.asExecutor(dispatcher: CoroutineDispatcher = Dispatchers.Default) =
    object : Executor {
        private val scope = CoroutineScope(this@asExecutor)

        override fun execute(command: Runnable) {
            scope.launch(dispatcher) { command.run() }
        }
    }

suspend fun <T> ListenableFuture<T>.suspendGet(dispatcher: CoroutineDispatcher = Dispatchers.Default): T {
    val executor = coroutineContext.asExecutor(dispatcher)
    return suspendCancellableCoroutine { continuation ->
        addListener(
            {
                try {
                    val result = get()
                    continuation.resumeWith(Result.success(result))
                } catch (e: Exception) {
                    continuation.resumeWithException(e)
                }
            },
            executor
        )

        continuation.invokeOnCancellation {
            this.cancel(true)
        }
    }
}