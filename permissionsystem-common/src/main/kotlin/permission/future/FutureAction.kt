package permission.future

import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage

class FutureAction<T>(future: CompletionStage<T>) : CompletableFuture<T>() {

    /**
     * Creates an uncompleted empty future.
     */
    constructor() : this(CompletableFuture<T>())

    /**
     * This constructor completes the future with the given value.
     * @param objects The value to complete the future with.
     */
    constructor(objects: T) : this() {
        this.complete(objects)
    }

    init {
        future.whenComplete { result, throwable ->
            throwable?.let { completeExceptionally(it) } ?: complete(result)
        }
    }

    /**
     * Is only called if the future is completed exceptionally.
     * @param action The throwable that caused the future to be completed exceptionally.
     */
    fun onFailure(action: (Throwable) -> Unit): FutureAction<T> {
        whenComplete { _, throwable ->
            throwable?.let { action(it) }
        }
        return this
    }

    /**
     * Is only called if the future is completed normally.
     * @param action The value that caused the future to be completed normally.
     */
    fun onSuccess(action: (T) -> Unit): FutureAction<T> {
        whenComplete { result, throwable ->
            throwable ?: action(result)
        }
        return this
    }

    fun onComplete(action: (T, Throwable) -> Unit): FutureAction<T> {
        whenComplete { result, throwable ->
            action(result, throwable)
        }
        return this
    }
}