package dev.yuanzix.beatify.data.musicRepository.utils

import dev.yuanzix.beatify.utils.Constants.BASE_URL

fun GetMusicURL(id: Int): String {
    return "$BASE_URL/music/stream?id=$id"
}