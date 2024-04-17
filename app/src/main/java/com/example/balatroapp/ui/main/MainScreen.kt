package com.example.balatroapp.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.balatroapp.Screen

@Composable
fun MainScreen(navController: NavHostController) {
    Column {
        Text(text = "Main")
        Button(onClick = { navController.navigate(Screen.List.route) }) {
            Text(text = "ToList")
        }
    }
}
