package com.example.balatroapp.ui.list

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavigatorProvider
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.balatroapp.Screen

val LocalListViewModelProvider = viewModelLocalOf<ListViewModel>()

fun <T> viewModelLocalOf() = compositionLocalOf<@Composable () -> T> {
    { error("") }
}

@Composable
fun ListScreen(
    navController: NavHostController,
    viewModel: ListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CompositionLocalProvider(LocalListViewModelProvider provides { viewModel }) {
        Column(
            modifier = Modifier.background(Color.DarkGray)
        ) {
            LazyColumn {
                items(state.cards) { card ->
                    CardItem(card, navController)
                }
            }
            Button(onClick = { navController.navigate(Screen.Detail.route) }) {
                Text(text = "ToDetail")
            }
        }
    }

}

enum class LikeState {
    NO_LIKE,
    LOADING,
    LIKED
}

@Composable
fun CardItem(card: Card, navController: NavHostController? = null) {
    val viewModel = LocalListViewModelProvider.current()

    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp, 4.dp, 8.dp, 4.dp)
            .background(Color.Gray, shape = RoundedCornerShape(8.dp))
            .clickable {
                navController?.navigate("${Screen.Detail.route}/${card.linkUrl}")
            },
    ) {
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
                modifier = Modifier.size(120.dp),
            )
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = card.name,
                    color = Color.White,
                )
                Spacer(modifier = Modifier.height(4.dp))
                ClickableText(
                    text = AnnotatedString(card.description),
                    maxLines = if (expanded) Int.MAX_VALUE else 1,
                    onClick = { expanded = !expanded },
                    style = TextStyle(
                        color = Color.White
                    )
                )
                IconButton(
                    onClick = {
                        viewModel.onClickWish(card)
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Icon(
                        imageVector = when (card.isWished) {
                            Card.WishedState.WISHED -> Icons.Default.FavoriteBorder
                            Card.WishedState.LOADING -> Icons.Default.FavoriteBorder
                            Card.WishedState.UNWISHED -> Icons.Default.Favorite
                        },
                        contentDescription = "Favorite",
                        tint = when (card.isWished) {
                            Card.WishedState.WISHED -> Color.Red
                            Card.WishedState.LOADING -> Color.Blue
                            Card.WishedState.UNWISHED -> Color.White
                        },
                    )
                }
            }

        }
    }
}

@Preview
@Composable
fun CardItemPreview() {
    CardItem(
        card = Card(
            name = "Joker",
            description = "This is description",
            linkUrl = "",
            imageUrl = "https://static.wikia.nocookie.net/balatrogame/images/e/ef/Joker.png/revision/latest/scale-to-width-down/70?cb=20230925003651"
        )
    )
}

