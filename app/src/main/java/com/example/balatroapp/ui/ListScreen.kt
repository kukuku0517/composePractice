package com.example.balatroapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.balatroapp.Screen

@Composable
fun ListScreen(navController: NavHostController) {
    Column {
        Text(text = "List")
        Button(onClick = { navController.navigate(Screen.Detail.route) }) {
            Text(text = "ToDetail")
        }
    }
}
