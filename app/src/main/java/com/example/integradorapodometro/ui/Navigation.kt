package com.example.integradorapodometro.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.integradorapodometro.ui.screens.LoginScreen
import com.example.integradorapodometro.ui.screens.RegisterScreen
import com.example.integradorapodometro.ui.screens.MyRoutesScreen
import com.example.integradorapodometro.ui.screens.GlobalRoutesScreen
import com.example.integradorapodometro.ui.screens.ActiveRouteScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        // ---------- LOGIN ----------
        composable("login") {
            LoginScreen(
                onLoginSuccess = { username ->
                    navController.navigate("my_routes/$username") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate("register")
                }
            )
        }

        // ---------- REGISTRO ----------
        composable("register") {
            RegisterScreen(
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }

        // ---------- MIS RECORRIDOS ----------
        composable(route = "my_routes/{username}") { backStackEntry ->
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


        // ---------- RECORRIDOS GLOBALES ----------
        composable("global_routes/{username}") { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            GlobalRoutesScreen(
                username = username,
                onStartNew = {
                    navController.navigate("active_route/$username")
                },
                onViewMine = {
                    navController.popBackStack()   // regresa a Mis Recorridos
                }
            )
        }

        // ---------- RECORRIDO ACTIVO ----------
        composable("active_route/{username}") { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            ActiveRouteScreen(
                username = username,
                onFinish = {
                    navController.popBackStack()  // vuelve a Mis Recorridos
                }
            )
        }
    }
}
