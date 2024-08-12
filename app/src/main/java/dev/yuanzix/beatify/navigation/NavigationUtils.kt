package dev.yuanzix.beatify.navigation

import kotlinx.serialization.Serializable

sealed class SubGraph {
    @Serializable
    data object StartNav : SubGraph()

    @Serializable
    data object AuthNav : SubGraph()

    @Serializable
    data object DashNav : SubGraph()
}

sealed class Destination {
    @Serializable
    data object StartScreen : Destination()

    @Serializable
    data object AuthLoginScreen : Destination()
    @Serializable
    data object AuthSignupScreen : Destination()
    @Serializable
    data object AuthVerifyEmailScreen : Destination()

    @Serializable
    data object DashHomeScreen : Destination()
    @Serializable
    data object DashSettingsScreen : Destination()
    @Serializable
    data object DashPlaylistScreen : Destination()
}