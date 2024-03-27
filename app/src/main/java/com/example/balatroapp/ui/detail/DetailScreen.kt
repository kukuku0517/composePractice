package com.example.balatroapp.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.balatroapp.Screen

@Composable
fun DetailScreen(navController: NavHostController) {
    Column {
        Text(text = "Detail")
        Button(onClick = { navController.navigate(Screen.Main.route) }) {
            Text(text = "ToMain")
        }
    }
}

