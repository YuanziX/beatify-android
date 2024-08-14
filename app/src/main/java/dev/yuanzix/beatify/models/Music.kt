package dev.yuanzix.beatify.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Music(
    @SerialName("ID") val id: Int,
    @SerialName("Title") val title: String,
    @SerialName("Artist") val artist: String,
    @SerialName("Album") val album: String,
    @SerialName("Location") val location: String,
    @SerialName("Year") val year: Int
)