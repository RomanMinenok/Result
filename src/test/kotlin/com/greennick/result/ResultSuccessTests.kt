package com.greennick.result

import org.junit.Test

class ResultSuccessTests {

    @Test
    fun `result is success`() {
        val result = Result.success("Hello!")
        assert(result.isSuccess)
    }

    @Test
    fun `success result has value`() {
        val value = "Hello!"
        val result = Result.success(value)
        assert(result.get() == value)
    }

    @Test
    fun `success result does not take default`() {
        val init = "Hello"
        val default = "World"
        val result = Result.success(init)
        assert(result.getOrDefault(default) != default)
    }

    @Test
    fun `fail result does not take default`() {
        val init = "Hello"
        val default = "World"
        val result = Result.error(init)
        assert(result.errorOrDefault(default) != default)
    }

    @Test
    fun `success result calls onSuccess block`() {
        var called = false

        val result = Result.success("Hello")
        result.onSuccess { called = true }

        assert(called)
    }

    @Test
    fun `success result calls withSuccess block`() {
        var called = false

        val result = Result.success("Hello")
        result.withSuccess { called = true }

        assert(called)
    }

    @Test
    fun `success result calls doIfSuccess block`() {
        var called = false

        val result = Result.success("Hello")
        result.doIfSuccess { called = true }

        assert(called)
    }

    @Test
    fun `success result is not failure`() {
        val result = Result.success("hello")
        assert(!result.isFailure)
    }

    @Test(expected = IllegalStateException::class)
    fun `success result throws failure error`() {
        val result = Result.success("hello")

        @Suppress("IMPLICIT_NOTHING_AS_TYPE_PARAMETER")
        result.errorOrThrow()
    }

    @Test
    fun `casted success result works properly positive`() {
        val init = "hello"
        val casted: Result<String, Throwable> = Result.success(init)

        assert(casted.isSuccess)
        assert(casted.get() == init)
    }

    @Test(expected = IllegalStateException::class)
    fun `casted success result works properly negative`() {
        val casted: Result<String, Throwable> =
            Result.success("hello")

        assert(!casted.isFailure)
        assert(casted.error() == null)

        casted.errorOrThrow()
    }

    @Test
    fun `success value become error after swap`() {
        val init = "hello"
        val swapped = Result.success(init).swap()

        assert(swapped.error() == init)
    }

    @Test
    fun `error value become success after swap`() {
        val init = "hello"
        val swapped = Result.error(init).swap()

        assert(swapped.get() == init)
    }

    @Test
    fun `tryWithResult returns value`() {
        val value = "hello"
        val result = tryWithResult { value }

        assert(result.get() == value)
    }

    @Test
    fun `mapping test`() {
        val final = 10
        val result = Result.success("hello")

        val mapped = result.map { final }

        assert(mapped.getOrThrow() == final)
    }

    @Test
    fun `swap success result elements`() {
        val init = "hello"
        val swapped: Result<Nothing, String> = Result.success(init).swap()
        assert(swapped.isFailure)
        assert(swapped.error() == init)
    }
}
