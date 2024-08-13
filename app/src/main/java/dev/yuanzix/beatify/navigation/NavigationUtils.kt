package dev.yuanzix.beatify.navigation

import kotlinx.serialization.Serializable

sealed class Destination {
    @Serializable
    data object StartScreen : Destination()

    @Serializable
    object Auth {
        @Serializable
        data object LoginScreen : Destination()

        @Serializable
        data object SignupScreen : Destination()

        @Serializable
        data object VerifyEmailScreen : Destination()
    }

    @Serializable
    object Dash {
        @Serializable
        data object HomeScreen : Destination()

        @Serializable
        data object SettingsScreen : Destination()

        @Serializable
        data object PlaylistScreen : Destination()
    }
}