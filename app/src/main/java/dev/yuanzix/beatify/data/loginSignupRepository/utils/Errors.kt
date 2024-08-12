package dev.yuanzix.beatify.data.loginSignupRepository.utils

enum class CreateUserError {
    BAD_REQUEST,
    INTERNAL_SERVER_ERROR,
    CONFLICT,
    EMAIL_NOT_SENT
}

enum class LoginError {
    BAD_REQUEST,
    UNAUTHORIZED,
    INTERNAL_SERVER_ERROR
}

enum class IsVerifiedError {
    BAD_REQUEST,
    NOT_FOUND,
    INTERNAL_SERVER_ERROR
}