package dev.yuanzix.beatify.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.yuanzix.beatify.ui.screens.StartScreen
import dev.yuanzix.beatify.ui.screens.auth.login.LoginScreen
import dev.yuanzix.beatify.ui.screens.auth.signup.SignUpScreen
import dev.yuanzix.beatify.ui.screens.auth.verify_email.VerifyEmailScreen
import dev.yuanzix.beatify.ui.screens.home.DashScreen

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController, startDestination = Destination.StartScreen
    ) {
        composable<Destination.StartScreen> {
            StartScreen(onNavigateToAuth = {
                navController.navigate(Destination.Auth.LoginScreen) {
                    popUpTo(Destination.StartScreen) { inclusive = true }

                }
            }, onNavigateToDash = {
                navController.navigate(Destination.DashNav) {
                    popUpTo(Destination.StartScreen) { inclusive = true }
                }
            })
        }

        composable<Destination.Auth.LoginScreen> {
            LoginScreen(onLoginSuccess = {
                navController.navigate(Destination.DashNav) {
                    popUpTo(Destination.Auth.LoginScreen) { inclusive = true }
                }
            }, onNavigateToSignup = {
                navController.navigate(Destination.Auth.SignupScreen)
            }, onNavigateToVerifyEmail = {
                navController.navigate(Destination.Auth.VerifyEmailScreen)
            })
        }

        composable<Destination.Auth.SignupScreen> {
            SignUpScreen(onNavigateToLogin = {
                navController.navigate(Destination.Auth.LoginScreen)
            }, onNavigateToVerifyEmail = {
                navController.navigate(Destination.Auth.VerifyEmailScreen)
            })
        }

        composable<Destination.Auth.VerifyEmailScreen> {
            VerifyEmailScreen(onVerificationSuccess = {
                navController.navigate(Destination.DashNav) {
                    popUpTo(Destination.Auth.LoginScreen) { inclusive = true }
                }
            })
        }

        composable<Destination.DashNav> {
            DashScreen(mainNavController = navController)
        }
    }
}
