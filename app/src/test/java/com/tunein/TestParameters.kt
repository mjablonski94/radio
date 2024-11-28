package com.tunein

import io.mockk.mockk
import org.junit.Assert.assertEquals

data class TestParameters<G, E>(
    val given: G,
    val expected: E,
    val exception: Throwable? = null
) {

    companion object {
        infix fun <G, E> G.then(expected: E): TestParameters<G, E> = TestParameters(
            given = this,
            expected = expected
        )

        inline infix fun <G, reified E> G.thenError(error: Throwable): TestParameters<G, E> =
            TestParameters(
                given = this,
                expected = mockk(relaxed = true),
                exception = error
            )
    }
}
