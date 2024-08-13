package dev.yuanzix.beatify.data.auth_repository

import dev.yuanzix.beatify.data.NetworkResult
import dev.yuanzix.beatify.data.auth_repository.utils.CreateUserResponse
import dev.yuanzix.beatify.data.auth_repository.utils.IsVerifiedResponse
import dev.yuanzix.beatify.data.auth_repository.utils.LoginDto
import dev.yuanzix.beatify.data.auth_repository.utils.LoginResponse
import dev.yuanzix.beatify.data.auth_repository.utils.UserDto
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
import okio.IOException

class AuthRepository {
    suspend fun createUser(
        email: String,
        username: String,
        firstName: String,
        lastName: String,
        password: String,
        dateOfBirth: String,
    ): NetworkResult<CreateUserResponse, Nothing> = withContext(Dispatchers.IO) {
        try {
            val userDto = UserDto(email, username, firstName, lastName, password, dateOfBirth)
            val response: HttpResponse = NetworkUtils.client.post("$BASE_URL/user") {
                contentType(ContentType.Application.Json)
                setBody(Json.encodeToString(userDto))
            }

            when (response.status.value) {
                in 200..299 -> {
                    NetworkResult(CreateUserResponse.SUCCESS)
                }

                400 -> NetworkResult(
                    error = CreateUserResponse.BAD_REQUEST,
                    message = "Bad request"
                )

                409 -> NetworkResult(
                    error = CreateUserResponse.CONFLICT,
                    message = "The email is already registered"
                )

                in 500..599 -> NetworkResult(
                    error = CreateUserResponse.INTERNAL_SERVER_ERROR,
                    message = "Internal server error"
                )

                else -> NetworkResult(
                    error = CreateUserResponse.INTERNAL_SERVER_ERROR,
                    message = "Unknown error"
                )
            }
        } catch (e: IOException) {
            NetworkResult(
                error = CreateUserResponse.SERVER_UNREACHABLE,
                message = "Unable to reach the server. Please check your internet connection."
            )

        } catch (e: Exception) {
            NetworkResult(error = CreateUserResponse.INTERNAL_SERVER_ERROR, message = e.message)
        }
    }

    suspend fun isUserVerified(email: String): NetworkResult<IsVerifiedResponse, Boolean> =
        withContext(Dispatchers.IO) {
            try {
                if (email.isEmpty()) {
                    return@withContext NetworkResult(
                        error = IsVerifiedResponse.BAD_REQUEST,
                        message = "Email not provided"
                    )
                }

                val response: HttpResponse =
                    NetworkUtils.client.get("$BASE_URL/user/$email/verified")

                when (response.status.value) {
                    in 200..299 -> {
                        val responseBody = response.bodyAsText()
                        val jsonResponse = Json.parseToJsonElement(responseBody).jsonObject
                        val isVerified = jsonResponse["verified"]?.jsonPrimitive?.boolean

                        if (isVerified != null) {
                            NetworkResult(IsVerifiedResponse.SUCCESS, data = isVerified)
                        } else {
                            NetworkResult(
                                error = IsVerifiedResponse.INTERNAL_SERVER_ERROR,
                                message = "Verification status not found in response"
                            )
                        }
                    }

                    400 -> NetworkResult(
                        error = IsVerifiedResponse.BAD_REQUEST,
                        message = "Bad request"
                    )

                    404 -> NetworkResult(
                        error = IsVerifiedResponse.NOT_FOUND,
                        message = "User not found"
                    )

                    in 500..599 -> NetworkResult(
                        error = IsVerifiedResponse.INTERNAL_SERVER_ERROR,
                        message = "Internal server error"
                    )

                    else -> NetworkResult(
                        error = IsVerifiedResponse.INTERNAL_SERVER_ERROR,
                        message = "Unknown error"
                    )
                }
            } catch (e: IOException) {
                NetworkResult(
                    error = IsVerifiedResponse.SERVER_UNREACHABLE,
                    message = "Unable to reach the server. Please check your internet connection."
                )
            } catch (e: Exception) {
                NetworkResult(error = IsVerifiedResponse.INTERNAL_SERVER_ERROR, message = e.message)
            }
        }

    suspend fun loginUser(email: String, password: String): NetworkResult<LoginResponse, String> =
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
                            NetworkResult(LoginResponse.SUCCESS, data = tokenString)
                        } else {
                            NetworkResult(
                                error = LoginResponse.INTERNAL_SERVER_ERROR,
                                message = "Token not found in response"
                            )
                        }
                    }

                    400 -> NetworkResult(error = LoginResponse.BAD_REQUEST, message = "Bad request")
                    401 -> {
                        val responseBody = response.bodyAsText()
                        val jsonResponse = Json.parseToJsonElement(responseBody).jsonObject
                        val errorMessage = jsonResponse["error"]?.jsonPrimitive?.content

                        if (errorMessage == "email not verified") {
                            NetworkResult(
                                error = LoginResponse.NOT_VERIFIED,
                                message = "Email not verified"
                            )
                        } else {
                            NetworkResult(
                                error = LoginResponse.UNAUTHORIZED,
                                message = "Incorrect email or password"
                            )
                        }
                    }

                    in 500..599 -> NetworkResult(
                        error = LoginResponse.INTERNAL_SERVER_ERROR,
                        message = "Internal server error"
                    )

                    else -> NetworkResult(
                        error = LoginResponse.INTERNAL_SERVER_ERROR,
                        message = "Unknown error"
                    )
                }
            } catch (e: IOException) {
                NetworkResult(
                    error = LoginResponse.SERVER_UNREACHABLE,
                    message = "Unable to reach the server. Please check your internet connection."
                )
            } catch (e: Exception) {
                NetworkResult(
                    error = LoginResponse.INTERNAL_SERVER_ERROR,
                    message = e.message ?: "An unknown error occurred"
                )
            }
        }
}
