package com.example.balatroapp

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.balatroapp.ui.theme.BalatroAppTheme
import com.example.balatroapp.ui.detail.DetailScreen
import com.example.balatroapp.ui.list.ListScreen
import com.example.balatroapp.ui.main.MainScreen
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import org.conscrypt.Conscrypt
import java.security.Security

@HiltAndroidApp
class BalatroApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Security.insertProviderAt(Conscrypt.newProvider(), 1);
    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            BalatroAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colorScheme.background) {
//                    MainScreen(navController)
                    NavHost(
                        navController,
                        startDestination = Screen.List.route,
                    ) {
                        composable(Screen.Main.route) { MainScreen(navController) }
                        composable(Screen.List.route) { ListScreen(navController) }
                        composable(Screen.Detail.route) { DetailScreen(navController) }
                    }
                }
            }
        }
    }
}

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object List : Screen("list")
    object Detail : Screen("detail")
}
