package com.example.integradorapodometro.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.integradorapodometro.ui.screens.LoginScreen
import com.example.integradorapodometro.ui.screens.RegisterScreen
import com.example.integradorapodometro.ui.screens.MyRoutesScreen
import com.example.integradorapodometro.ui.screens.GlobalRoutesScreen
import com.example.integradorapodometro.ui.screens.ActiveRouteScreen

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val MY_ROUTES = "my_routes/{username}"
    const val GLOBAL_ROUTES = "global_routes/{username}"
    const val ACTIVE_ROUTE = "active_route/{username}"
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN,
        modifier = modifier
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = { username ->
                    navController.navigate("my_routes/$username")
                },
                onRegisterClick = {
                    navController.navigate(Routes.REGISTER)
                }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                onBackToLogin = { navController.popBackStack() }
            )
        }

        composable("my_routes/{username}") { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            MyRoutesScreen(
                username = username,
                onStartNew = {
                    navController.navigate("active_route/$username")
                },
                onViewGlobal = {
                    navController.navigate("global_routes/$username")
                }
            )
        }

        composable("global_routes/{username}") { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            GlobalRoutesScreen(
                username = username,
                onStartNew = {
                    navController.navigate("active_route/$username")
                },
                onViewMine = {
                    navController.navigate("my_routes/$username") {
                        popUpTo("my_routes/$username") { inclusive = true }
                    }
                }
            )
        }

        composable("active_route/{username}") { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            ActiveRouteScreen(
                username = username,
                onFinish = {
                    navController.popBackStack() // regresa a mis recorridos
                }
            )
        }
    }
}
