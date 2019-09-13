package com.greennick.result

import org.junit.Test

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
    }

    @Test(expected = IllegalStateException::class)
    fun `casted empty result works properly, not as failure`() {
        val casted: Result<Unit, Throwable> = Result.success()

        assert(!casted.isFailure)
        assert(casted.error() == null)

        casted.errorOrThrow()
    }
}
