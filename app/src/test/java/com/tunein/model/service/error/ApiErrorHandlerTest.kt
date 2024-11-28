package com.tunein.model.service.error

import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonEncodingException
import com.tunein.TestParameters
import com.tunein.TestParameters.Companion.then
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import retrofit2.HttpException

@RunWith(Parameterized::class)
class ApiErrorHandlerTest(
    private val params: TestParameters<Throwable, Throwable>
) {

    private val apiErrorHandler = ApiErrorHandler()

    @Test
    fun `should map throwable to expected ServiceError`() = runBlocking {
        // given
        val (inputThrowable, expectedError) = params

        // when
        val actualError = runCatching {
            apiErrorHandler.withExceptionMapped { throw inputThrowable }
        }.exceptionOrNull()

        // then
        assertEquals(expectedError::class.java, actualError?.javaClass)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data() = listOf(
            HttpException(retrofit2.Response.error<String>(404, okhttp3.ResponseBody.create(null, ""))) then ServiceError.NetworkError.NoData(),
            HttpException(retrofit2.Response.error<String>(401, okhttp3.ResponseBody.create(null, ""))) then ServiceError.NetworkError.Unauthorized(),
            HttpException(retrofit2.Response.error<String>(500, okhttp3.ResponseBody.create(null, ""))) then ServiceError.NetworkError.Other(500, "Internal Server Error"),
            JsonDataException("Invalid JSON data") then ServiceError.ParsingError("Invalid JSON data"),
            JsonEncodingException("Error encoding JSON data") then ServiceError.EncodingError("Error encoding JSON data")
        )
    }
}
