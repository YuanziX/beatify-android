package dev.yuanzix.beatify.data.musicRepository

import dev.yuanzix.beatify.data.NetworkResult
import dev.yuanzix.beatify.data.musicRepository.utils.GetMusicListResponse
import dev.yuanzix.beatify.models.Music
import dev.yuanzix.beatify.utils.Constants.BASE_URL
import dev.yuanzix.beatify.utils.NetworkUtils
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okio.IOException

class MusicRepository {
    suspend fun fetchMusic(
        pageNo: Int,
    ): NetworkResult<GetMusicListResponse, List<Music>> = withContext(Dispatchers.IO) {
        try {
            val response: HttpResponse = NetworkUtils.client.get("$BASE_URL/music?")  {
                parameter("page", pageNo.toString())
            }

            when (response.status.value) {
                in 200..299 -> {
                    val responseBody = response.bodyAsText()
                    val musicList = Json.decodeFromString<List<Music>>(responseBody)
                    NetworkResult(GetMusicListResponse.SUCCESS, data = musicList)
                }
                400 -> NetworkResult(GetMusicListResponse.BAD_REQUEST, message = "Invalid page number")
                416 -> NetworkResult(GetMusicListResponse.END_OF_CONTENT, message = "Reached end of content")
                in 500..599 -> NetworkResult(GetMusicListResponse.INTERNAL_SERVER_ERROR, message = "Internal server error")
                else -> NetworkResult(GetMusicListResponse.INTERNAL_SERVER_ERROR, message = "Unknown error")
            }
        } catch (e: IOException) {
            NetworkResult(
                response = GetMusicListResponse.SERVER_UNREACHABLE,
                message = "Unable to reach the server. Please check your internet connection."
            )

        } catch (e: Exception) {
            NetworkResult(response = GetMusicListResponse.INTERNAL_SERVER_ERROR, message = e.message)
        }
    }
}
