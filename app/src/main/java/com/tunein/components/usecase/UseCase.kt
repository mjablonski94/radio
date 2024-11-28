package com.tunein.components.usecase

import kotlinx.coroutines.*

abstract class UseCase<Type, in Params>(
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) where Type : Any {

    open suspend fun action(params: Params?): Type? = null

    open operator fun invoke(
        coroutineScope: CoroutineScope,
        params: Params? = null,
        executionDispatcher: CoroutineDispatcher = coroutineDispatcher,
        onResult: suspend Result<Type?>.()-> Unit = {}
    ): Job = coroutineScope.launch(executionDispatcher) {
        val result: Result<Type?> = runCatching { action(params) }
        onResult(result)
    }
}