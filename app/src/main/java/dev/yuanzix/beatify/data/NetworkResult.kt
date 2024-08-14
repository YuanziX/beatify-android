package dev.yuanzix.beatify.data

data class NetworkResult<T, R>(
    val response: T? = null,
    val message: String? = null,
    val data: R? = null,
)