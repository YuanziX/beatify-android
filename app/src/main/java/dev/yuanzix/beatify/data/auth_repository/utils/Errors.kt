package dev.yuanzix.beatify.data.auth_repository.utils

enum class CreateUserResponse {
    SUCCESS,
    BAD_REQUEST,
    INTERNAL_SERVER_ERROR,
    CONFLICT,
    EMAIL_NOT_SENT,
    SERVER_UNREACHABLE
}

enum class LoginResponse {
    SUCCESS,
    BAD_REQUEST,
    UNAUTHORIZED,
    NOT_VERIFIED,
    INTERNAL_SERVER_ERROR,
    SERVER_UNREACHABLE
}

enum class IsVerifiedResponse {
    SUCCESS,
    BAD_REQUEST,
    NOT_FOUND,
    INTERNAL_SERVER_ERROR,
    SERVER_UNREACHABLE
}