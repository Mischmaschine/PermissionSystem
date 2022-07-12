package permission.future

import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage

class FutureAction<T>(future: CompletionStage<T>) : CompletableFuture<T>() {

    constructor() : this(CompletableFuture<T>())

    constructor(objects: T) : this() {
        this.complete(objects)
    }

    init {
        future.whenComplete { result, throwable ->
            throwable?.let { completeExceptionally(it) } ?: complete(result)
        }
    }

    fun onFailure(action: (Throwable) -> Unit): FutureAction<T> {
        whenComplete { _, throwable ->
            throwable?.let { action(it) }
        }
        return this
    }

    fun onSuccess(action: (T) -> Unit): FutureAction<T> {
        whenComplete { result, throwable ->
            throwable ?: action(result)
        }
        return this
    }
}