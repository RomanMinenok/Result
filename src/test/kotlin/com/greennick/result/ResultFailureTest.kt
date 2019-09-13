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
}
