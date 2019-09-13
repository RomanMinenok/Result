@file:Suppress("UNCHECKED_CAST")

package com.greennick.result

/**
 * @return success value or null if absent.
 */
fun <T, E> Result<T, E>.get(): T? = when {
    isSuccess -> getOrThrow()
    else -> null
}

/**
 * @return error value or null if absent.
 */
fun <T, E> Result<T, E>.error(): E? = when {
    isFailure -> errorOrThrow()
    else -> null
}

/**
 * See [get].
 * Used for destructuring.
 */
operator fun <T, E> Result<T, E>.component1(): T? = get()

/**
 * See [error].
 * Used for destructuring.
 */
operator fun <T, E> Result<T, E>.component2(): E? = error()

/**
 * Replace success with error and otherwise.
 *
 * @return new Result with swapped types and values.
 */
fun <T, E> Result<T, E>.swap(): Result<E, T> = when {
    isSuccess -> Result.error(getOrThrow())
    else -> Result.success(errorOrThrow())
}

/**
 * @param action - called with success value if presents.
 * @return Result itself.
 */
inline fun <T, E> Result<T, E>.onSuccess(action: (T) -> Unit): Result<T, E> {
    if (isSuccess) {
        action(getOrThrow())
    }
    return this
}

/**
 * @param action - called with error value if presents.
 * @return Result itself.
 */
inline fun <T, E> Result<T, E>.onError(action: (E) -> Unit): Result<T, E> {
    if (isFailure) {
        action(errorOrThrow())
    }
    return this
}

/**
 * @param action - called with success value as receiver if presents.
 * @return Result itself.
 */
inline fun <T, E> Result<T, E>.withSuccess(action: T.() -> Unit): Result<T, E> = onSuccess(action)

/**
 * @param action - called with error value as receiver if presents.
 * @return Result itself.
 */
inline fun <T, E> Result<T, E>.withError(action: E.() -> Unit): Result<T, E> = onError(action)

/**
 * @param action - called in case of success result.
 * @return Result itself.
 */
inline fun <T, E> Result<T, E>.doIfSuccess(action: () -> Unit): Result<T, E> = onSuccess { action() }

/**
 * @param action - called in case of failed result.
 * @return Result itself.
 */
inline fun <T, E> Result<T, E>.doIfError(action: () -> Unit): Result<T, E> = onError { action() }

/**
 * @return Result's success value if presents.
 * Call [defaultValue] block and return its result otherwise.
 */
inline fun <R, T : R> Result<T, Any?>.getOrDefault(defaultValue: () -> R): R = when {
    isFailure -> defaultValue()
    else -> getOrThrow()
}

/**
 * @return Result's error value if presents.
 * Call [defaultValue] block and return its result otherwise.
 */
inline fun <R, E : R> Result<Any?, E>.errorOrDefault(defaultValue: () -> R): R = when {
    isSuccess -> defaultValue()
    else -> errorOrThrow()
}

/**
 * @return Result's success value if presents.
 * Returns [defaultValue] otherwise.
 */
fun <R, T : R> Result<T, Any?>.getOrDefault(defaultValue: R): R =
    getOrDefault { defaultValue }

/**
 * @return Result's error value if presents.
 * Returns [defaultValue] otherwise.
 */
fun <R, E : R> Result<Any?, E>.errorOrDefault(defaultValue: R): R =
    errorOrDefault { defaultValue }

/**
 * Maps inner success value with [transformer] block if presents to new Result.
 * Returns casted origin Result otherwise.
 */
inline fun <T, E, R> Result<T, E>.flatMap(transformer: (T) -> Result<R, E>): Result<R, E> = when {
    isSuccess -> transformer(getOrThrow())
    else -> this as Result<R, E>
}

/**
 * Maps inner error value with [transformer] block if presents to new Result.
 * Returns casted origin Result otherwise.
 */
inline fun <T, E, R> Result<T, E>.flatMapError(transformer: (E) -> Result<T, R>): Result<T, R> = when {
    isSuccess -> this as Result<T, R>
    else -> transformer(errorOrThrow())
}

/**
 * Maps inner success value with [transformer] block if presents and return new Result with it.
 * Returns casted origin Result otherwise.
 */
inline fun <T, E, R> Result<T, E>.map(transformer: (T) -> R): Result<R, E> =
    flatMap { Result.success(transformer(it)) }

/**
 * Maps inner error value with [transformer] block if presents and return new Result with it.
 * Returns casted origin Result otherwise.
 */
inline fun <T, E, R> Result<T, E>.mapError(transformer: (E) -> R): Result<T, R> =
    flatMapError { Result.error(transformer(it)) }

/**
 * Call [action] wrapped in try-catch block.
 *
 * @return Result with [action]`s value or with caught exception.
 */
inline fun <T> tryWithResult(action: () -> T): Result<T, Throwable> =
    try {
        Result.success(action())
    } catch (error: Throwable) {
        Result.error(error)
    }
