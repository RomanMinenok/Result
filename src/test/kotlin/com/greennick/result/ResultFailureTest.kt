package com.greennick.result

import org.junit.Test

class ResultFailureTest {

    @Test
    fun `result is failure`() {
        val result = Result.error("oh no!")
        assert(result.isFailure)
    }

    @Test
    fun `failure result has error`() {
        val error = "Hello!"
        val result = Result.error(error)
        assert(result.error() == error)
    }

    @Test
    fun `failure result calls default`() {
        val default = "World"
        val result = Result.error("Hello")
        assert(result.getOrDefault(default) == default)
    }

    @Test
    fun `success result calls default`() {
        val default = "World"
        val result = Result.success("Hello")
        assert(result.errorOrDefault(default) == default)
    }

    @Test
    fun `failure result calls onError block`() {
        var called = false

        val result = Result.error("Hello")
        result.onError { called = true }

        assert(called)
    }

    @Test
    fun `failure result calls withError block`() {
        var called = false

        val result = Result.error("Hello")
        result.withError { called = true }

        assert(called)
    }

    @Test
    fun `failure result calls doIfError block`() {
        var called = false

        val result = Result.error("Hello")
        result.doIfError { called = true }

        assert(called)
    }

    @Test
    fun `failure result is not success`() {
        val result = Result.error("hello")
        assert(!result.isSuccess)
    }

    @Test(expected = IllegalStateException::class)
    fun `failure result throws success error`() {
        val result = Result.error("hello")

        @Suppress("IMPLICIT_NOTHING_AS_TYPE_PARAMETER")
        result.getOrThrow()
    }

    @Test
    fun `casted failure result works properly positive`() {
        val init = "hello"
        val casted: Result<Any, String> = Result.error(init)

        assert(casted.isFailure)
        assert(casted.error() == init)
    }

    @Test(expected = IllegalStateException::class)
    fun `casted failure result works properly negative`() {
        val casted: Result<Any, String> = Result.error("hello")

        assert(!casted.isSuccess)
        assert(casted.get() == null)

        casted.getOrThrow()
    }

    @Test
    fun `mapping error test`() {
        val final = 10
        val result = Result.error("hello")

        val mapped = result.mapError { final }

        assert(mapped.errorOrThrow() == final)
    }

    @Test
    fun `tryWithResult catches error`() {
        val exception = IllegalStateException()
        val result = tryWithResult { throw exception }

        assert(result.error() == exception)
    }

    @Test
    fun `swap failure result elements`() {
        val init = "hello"
        val swapped: Result<String, Nothing> = Result.error(init).swap()
        assert(swapped.isSuccess)
        assert(swapped.get() == init)
    }

    @Test
    fun `map to list`() {
        val result: Result<Int, String> = Result.error("hello")
        val list: List<Int> = result.toList()

        assert(list.isEmpty())
    }

    @Test
    fun `map to error list`() {
        val init = "hello"
        val result: Result<Int, String> = Result.error(init)
        val list: List<String> = result.toErrorList()

        assert(list.size == 1)
        assert(list.first() == init)
    }

    @Test
    fun `list of success items mapping to empty list`() {
        val list = listOf<Result<Int, String>>(
            Result.success(1),
            Result.success(0),
            Result.success(1),
            Result.success(5)
        )

        assert(list.toErrorList().isEmpty())
    }

    @Test
    fun `list of mixed items mapping to list`() {
        val list = listOf(
            Result.success(1),
            Result.error("Hello"),
            Result.success(1),
            Result.error("World")
        )

        val mapped = list.toErrorList()
        assert(mapped.size == 2)
        assert(mapped == listOf("Hello", "World"))
    }

    @Test
    fun `list of error items mapping to list`() {
        val list = listOf<Result<String, Int>>(
            Result.error(1),
            Result.error(0),
            Result.error(1),
            Result.error(5)
        )

        val mapped = list.toErrorList()
        assert(mapped.size == 4)
        assert(mapped == listOf(1, 0, 1, 5))
    }
}
