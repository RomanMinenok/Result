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
        assert(result.getOrThrow() == value)
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
        assert(casted.getOrThrow() == init)
    }

    @Test
    fun `casted success result works properly negative`() {
        val casted: Result<String, Throwable> = Result.success("hello")

        assert(!casted.isFailure)
        assert(casted.error() == null)

        var caught: Throwable? = null
        try {
            casted.errorOrThrow()
        } catch (e: Throwable) {
            caught = e
        }
        assert(caught is IllegalStateException)
    }
}

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
        assert(result.errorOrThrow() == error)
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
        assert(casted.errorOrThrow() == init)
    }

    @Test
    fun `casted failure result works properly negative`() {
        val casted: Result<Any, String> = Result.error("hello")

        assert(!casted.isSuccess)
        assert(casted.get() == null)

        var caught: Throwable? = null
        try {
            casted.getOrThrow()
        } catch (e: Throwable) {
            caught = e
        }
        assert(caught is IllegalStateException)
    }
}

class ResultEmptyTests {
    @Test
    fun `empty result success`() {
        val result = Result.success()
        assert(result.isSuccess)
    }

    @Test
    fun `empty result is not failed`() {
        val result = Result.success()
        assert(!result.isFailure)
    }

    @Test(expected = IllegalStateException::class)
    fun `getting error result throws`() {
        @Suppress("IMPLICIT_NOTHING_AS_TYPE_PARAMETER")
        Result.success().errorOrThrow()
    }

    @Test
    fun `casted empty result works properly, as success`() {
        val casted: Result<Unit, Throwable> = Result.success()

        assert(casted.isSuccess)
        assert(casted.get() != null)
        casted.getOrThrow()
    }

    @Test(expected = IllegalStateException::class)
    fun `casted empty result works properly, not as failure`() {
        val casted: Result<Unit, Throwable> = Result.success()

        assert(!casted.isFailure)
        assert(casted.error() == null)

        casted.errorOrThrow()
    }

    @Test
    fun `mapping test`() {
        val final = 10
        val result = Result.success("hello")

        val mapped = result.map { final }

        assert(mapped.getOrThrow() == final)
    }

    @Test
    fun `mapping error test`() {
        val final = 10
        val result = Result.error("hello")

        val mapped = result.mapError { final }

        assert(mapped.errorOrThrow() == final)
    }

    @Test
    fun `tryWithResult returns value`() {
        val value = "hello"
        val result = tryWithResult { value }

        assert(result.get() == value)
    }

    @Test
    fun `tryWithResult catches error`() {
        val exception = IllegalStateException()
        val result = tryWithResult { throw exception }

        assert(result.error() == exception)
    }
}
