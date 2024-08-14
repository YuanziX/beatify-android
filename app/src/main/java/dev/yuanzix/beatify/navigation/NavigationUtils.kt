package dev.yuanzix.beatify.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.ui.graphics.vector.ImageVector
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
    data object DashNav : Destination()

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

sealed class NavigationBarItems(val route: Destination, val title: String, val icon: ImageVector) {
    data object Home : NavigationBarItems(Destination.Dash.HomeScreen, "Home Screen", Icons.Rounded.Home)
    data object Playlist : NavigationBarItems(Destination.Dash.PlaylistScreen, "Playlist Screen", Icons.Rounded.PlayArrow)
}

val navBarItems = listOf(
    NavigationBarItems.Home,
    NavigationBarItems.Playlist,
)