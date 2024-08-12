package dev.yuanzix.beatify.data.loginSignupRepository

import dev.yuanzix.beatify.data.NetworkResult
import dev.yuanzix.beatify.data.loginSignupRepository.utils.CreateUserError
import dev.yuanzix.beatify.data.loginSignupRepository.utils.IsVerifiedError
import dev.yuanzix.beatify.data.loginSignupRepository.utils.LoginDto
import dev.yuanzix.beatify.data.loginSignupRepository.utils.LoginError
import dev.yuanzix.beatify.data.loginSignupRepository.utils.UserDto
import dev.yuanzix.beatify.utils.Constants.BASE_URL
import dev.yuanzix.beatify.utils.NetworkUtils
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class AuthRepository {
    suspend fun createUser(
        email: String,
        username: String,
        firstName: String,
        lastName: String,
        password: String,
        dateOfBirth: String,
    ): NetworkResult<CreateUserError, Nothing> = withContext(Dispatchers.IO) {
        try {
            val userDto = UserDto(email, username, firstName, lastName, password, dateOfBirth)
            val response: HttpResponse = NetworkUtils.client.post("$BASE_URL/user") {
                contentType(ContentType.Application.Json)
                setBody(Json.encodeToString(userDto))
            }

            when (response.status.value) {
                in 200..299 -> {
                    NetworkResult()
                }
                400 -> NetworkResult(error = CreateUserError.BAD_REQUEST, message = "Bad request")
                409 -> NetworkResult(error = CreateUserError.CONFLICT, message = "The email is already registered")
                in 500..599 -> NetworkResult(error = CreateUserError.INTERNAL_SERVER_ERROR, message = "Internal server error")
                else -> NetworkResult(error = CreateUserError.INTERNAL_SERVER_ERROR, message = "Unknown error")
            }
        } catch (e: Exception) {
            NetworkResult(error = CreateUserError.INTERNAL_SERVER_ERROR, message = e.message)
        }
    }

    suspend fun isUserVerified(email: String): NetworkResult<IsVerifiedError, Boolean> =
        withContext(Dispatchers.IO) {
            try {
                if (email.isEmpty()) {
                    return@withContext NetworkResult(error = IsVerifiedError.BAD_REQUEST, message = "Email not provided")
                }

                val response: HttpResponse = NetworkUtils.client.get("$BASE_URL/user/$email/verified")

                when (response.status.value) {
                    in 200..299 -> {
                        val responseBody = response.bodyAsText()
                        val jsonResponse = Json.parseToJsonElement(responseBody).jsonObject
                        val isVerified = jsonResponse["verified"]?.jsonPrimitive?.boolean

                        if (isVerified != null) {
                            NetworkResult(data = isVerified)
                        } else {
                            NetworkResult(error = IsVerifiedError.INTERNAL_SERVER_ERROR, message = "Verification status not found in response")
                        }
                    }
                    400 -> NetworkResult(error = IsVerifiedError.BAD_REQUEST, message = "Bad request")
                    404 -> NetworkResult(error = IsVerifiedError.NOT_FOUND, message = "User not found")
                    in 500..599 -> NetworkResult(error = IsVerifiedError.INTERNAL_SERVER_ERROR, message = "Internal server error")
                    else -> NetworkResult(error = IsVerifiedError.INTERNAL_SERVER_ERROR, message = "Unknown error")
                }
            } catch (e: Exception) {
                NetworkResult(error = IsVerifiedError.INTERNAL_SERVER_ERROR, message = e.message)
            }
        }

    suspend fun loginUser(email: String, password: String): NetworkResult<LoginError, String> =
        withContext(Dispatchers.IO) {
            try {
                val loginDto = LoginDto(email, password)
                val response: HttpResponse = NetworkUtils.client.post("$BASE_URL/login") {
                    contentType(ContentType.Application.Json)
                    setBody(Json.encodeToString(loginDto))
                }

                when (response.status.value) {
                    in 200..299 -> {
                        val responseBody = response.bodyAsText()
                        val jsonResponse = Json.parseToJsonElement(responseBody).jsonObject
                        val tokenString = jsonResponse["token_string"]?.jsonPrimitive?.content

                        if (!tokenString.isNullOrEmpty()) {
                            NetworkResult(data = tokenString)
                        } else {
                            NetworkResult(error = LoginError.INTERNAL_SERVER_ERROR, message = "Token not found in response")
                        }
                    }
                    400 -> NetworkResult(error = LoginError.BAD_REQUEST, message = "Bad request")
                    401 -> NetworkResult(error = LoginError.UNAUTHORIZED, message = "Incorrect email or password")
                    in 500..599 -> NetworkResult(error = LoginError.INTERNAL_SERVER_ERROR, message = "Internal server error")
                    else -> NetworkResult(error = LoginError.INTERNAL_SERVER_ERROR, message = "Unknown error")
                }
            } catch (e: Exception) {
                NetworkResult(error = LoginError.INTERNAL_SERVER_ERROR, message = e.message)
            }
        }
}
