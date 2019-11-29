package com.arya21.pokequest.presentation.common

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

/**
 * A base viewmodel
 * Provides coroutine context
 */
abstract class BaseViewModel(): ViewModel(), CoroutineScope {
    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext = job + Dispatchers.Main

    protected suspend inline fun<T> runIO(
        crossinline block: suspend CoroutineScope.() -> T): T =
        withContext(Dispatchers.IO) {block()}

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}