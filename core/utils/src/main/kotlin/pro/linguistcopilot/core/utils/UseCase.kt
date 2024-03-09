package pro.linguistcopilot.core.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class UseCase<In : UseCase.Params, Out> {
    suspend operator fun invoke(
        params: In
    ) = withContext(Dispatchers.IO) {
        execute(params)
    }

    protected abstract suspend fun execute(params: In): Out

    interface Params {
        object None : Params
    }
}

abstract class UseCaseNoParam<Out> : UseCase<UseCase.Params.None, Out>() {
    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        execute(Params.None)
    }
}

abstract class UseCase2<In : UseCase2.Params, Out> {
    operator fun invoke(
        params: In,
        scope: CoroutineScope = MainScope(),
        onResult: suspend (Out) -> Unit = {}
    ) {
        scope.launch {
            val deferredJob = async(Dispatchers.IO) {
                execute(params)
            }
            onResult(deferredJob.await())
        }
    }

    protected abstract fun execute(params: In): Out

    interface Params {
        object None : Params
    }
}

abstract class UseCaseNoParam2<Out> : UseCase2<UseCase2.Params.None, Out>() {
    operator fun invoke(
        scope: CoroutineScope = MainScope(),
        onResult: suspend (Out) -> Unit = {}
    ) {
        scope.launch {
            val deferredJob = async(Dispatchers.IO) {
                execute(Params.None)
            }
            onResult(deferredJob.await())
        }
    }
}