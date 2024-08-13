package dev.yuanzix.beatify.data.auth_repository.utils

@kotlinx.serialization.Serializable
data class UserDto(
    val email: String,
    val username: String,
    val first_name: String,
    val last_name: String,
    val password: String,
    val date_of_birth: String,
)

@kotlinx.serialization.Serializable
data class LoginDto(
    val email: String,
    val password: String,
)
