package dev.yuanzix.beatify.utils

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO

object NetworkUtils {
    val client = HttpClient(CIO)
}