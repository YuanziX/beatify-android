package dev.yuanzix.beatify.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.yuanzix.beatify.navigation.Destination
import dev.yuanzix.beatify.navigation.navBarItems
import dev.yuanzix.beatify.ui.screens.home.components.CollapsibleExoPlayer
import dev.yuanzix.beatify.ui.screens.home.home.HomeScreen
import dev.yuanzix.beatify.ui.screens.home.playlist.PlaylistScreen
import dev.yuanzix.beatify.ui.theme.Padding
import dev.yuanzix.beatify.ui.viewmodels.DashViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashScreen(mainNavController: NavController, modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    var selectedItem by rememberSaveable { mutableIntStateOf(0) }
    val dashViewModel = hiltViewModel<DashViewModel>()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                navBarItems.forEachIndexed { index, screen ->
                    NavigationBarItem(
                        alwaysShowLabel = true,
                        icon = {
                            Icon(
                                modifier = Modifier.size(20.dp),
                                imageVector = screen.icon,
                                contentDescription = screen.title,
                                tint = MaterialTheme.colorScheme.primary,
                            )
                        },
                        label = { Text(screen.title) },
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            NavHost(
                modifier = Modifier.padding(Padding.Medium).weight(0.9f),
                navController = navController,
                startDestination = Destination.Dash.HomeScreen
            ) {
                composable<Destination.Dash.HomeScreen> {
                    HomeScreen(
                        viewModel = dashViewModel
                    )
                }

                composable<Destination.Dash.PlaylistScreen> {
                    PlaylistScreen(
                        viewModel = dashViewModel
                    )
                }
            }
            CollapsibleExoPlayer(
                viewModel = dashViewModel,
            )
        }
    }
}