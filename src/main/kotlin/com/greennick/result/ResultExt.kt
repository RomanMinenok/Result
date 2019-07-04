@file:Suppress("UNCHECKED_CAST")

package com.greennick.result

fun <T, E> Result<T, E>.get(): T? = when {
    isSuccess -> getOrThrow()
    else -> null
}

fun <T, E> Result<T, E>.error(): E? = when {
    isFailure -> errorOrThrow()
    else -> null
}

operator fun <T, E> Result<T, E>.component1(): T? = get()

operator fun <T, E> Result<T, E>.component2(): E? = error()

inline fun <T, E> Result<T, E>.onSuccess(action: (T) -> Unit): Result<T, E> {
    if (isSuccess) {
        action(getOrThrow())
    }
    return this
}

inline fun <T, E> Result<T, E>.onError(action: (E) -> Unit): Result<T, E> {
    if (isFailure) {
        action(errorOrThrow())
    }
    return this
}

inline fun <T, E> Result<T, E>.withSuccess(action: T.() -> Unit): Result<T, E> = onSuccess(action)

inline fun <T, E> Result<T, E>.withError(action: E.() -> Unit): Result<T, E> = onError(action)

inline fun <T, E> Result<T, E>.doIfSuccess(action: () -> Unit): Result<T, E> = onSuccess { action() }

inline fun <T, E> Result<T, E>.doIfError(action: () -> Unit): Result<T, E> = onError { action() }

inline fun <R, T : R> Result<T, Any?>.getOrDefault(defaultValue: () -> R): R = when {
    isFailure -> defaultValue()
    else -> getOrThrow()
}

inline fun <R, E : R> Result<Any?, E>.errorOrDefault(defaultValue: () -> R): R = when {
    isSuccess -> defaultValue()
    else -> errorOrThrow()
}

fun <R, T : R> Result<T, Any?>.getOrDefault(defaultValue: R): R =
    getOrDefault { defaultValue }

fun <R, E : R> Result<Any?, E>.errorOrDefault(defaultValue: R): R =
    errorOrDefault { defaultValue }

inline fun <T, E, R> Result<T, E>.flatMap(transformer: (T) -> Result<R, E>): Result<R, E> = when {
    isSuccess -> transformer(getOrThrow())
    else -> this as Result<R, E>
}

inline fun <T, E, R> Result<T, E>.flatMapError(transformer: (E) -> Result<T, R>): Result<T, R> = when {
    isSuccess -> this as Result<T, R>
    else -> transformer(errorOrThrow())
}

inline fun <T, E, R> Result<T, E>.map(transformer: (T) -> R): Result<R, E> =
    flatMap { Result.success(transformer(it)) }

inline fun <T, E, R> Result<T, E>.mapError(transformer: (E) -> R): Result<T, R> =
    flatMapError { Result.error(transformer(it)) }

inline fun <T> tryWithResult(action: () -> T): Result<T, Throwable> =
    try {
        Result.success(action())
    } catch (error: Throwable) {
        Result.error(error)
    }
