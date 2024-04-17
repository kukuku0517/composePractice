package com.example.balatroapp.ui.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.balatroapp.Screen

@Composable
fun ListScreen(
    navController: NavHostController,
    viewModel: ListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column {
        LazyColumn {
            items(state.cards) { card ->
                CardItem(card)
            }
        }
        Button(onClick = { navController.navigate(Screen.Detail.route) }) {
            Text(text = "ToDetail")
        }
    }

}

@Composable
fun CardItem(card: Card) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // 이미지를 왼쪽에 정렬
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(card.imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = "",
            modifier = Modifier.size(50.dp),
        )
        Column(
            modifier = Modifier
                .padding(start = 16.dp)
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = card.name,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = card.description,
            )
        }
    }
}


