@file:Suppress("UNCHECKED_CAST")

package com.greennick.result

class Result<out T, out E> private constructor(private val result: Any?) {
    val isFailure get() = result is Failure<*>
    val isSuccess get() = !isFailure

    @Throws(IllegalStateException::class)
    fun getOrThrow(): T = when {
        isFailure -> throw IllegalStateException("There is no success value in Result")
        else -> result as T
    }

    @Throws(IllegalStateException::class)
    fun errorOrThrow(): E = when (result) {
        is Failure<*> -> result.error as E
        else -> throw IllegalStateException("There is no failure value in Result")
    }

    override fun toString() = when {
        isSuccess -> "Success with [$result]"
        else -> result.toString()
    }

    override fun equals(other: Any?) = result == other

    override fun hashCode() = result.hashCode()

    companion object {
        private val empty = Result<Unit, Nothing>(Unit)

        fun success() = empty

        fun <T> success(result: T) = Result<T, Nothing>(result)

        fun <E> error(error: E) = Result<Nothing, E>(Failure(error))
    }

    private class Failure<E>(val error: E) {
        override fun toString() = "Failure with [$error]"

        override fun equals(other: Any?) = error == other

        override fun hashCode() = error.hashCode()
    }
}
