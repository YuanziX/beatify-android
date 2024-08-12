package dev.yuanzix.beatify.data

data class NetworkResult<T, R>(
    val error: T? = null,
    val message: String? = null,
    val data: R? = null
)