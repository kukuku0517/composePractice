package com.example.balatroapp.ui.list

import android.util.Log
import androidx.compose.foundation.background
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
                    CardItem(card)
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
fun CardItem(card: Card) {
    val viewModel = LocalListViewModelProvider.current()

    var expanded by remember { mutableStateOf(false) }
    /*
    CardItem이 초기화 될때 card값을 기준으로 초기화.
    이후 card값이 업데이트 되어서 recomposition 되어도 해당 값으로 업데이트 되지 않음 (초기화에만 사용)
    (아예 view가 새로 생성될 때는 card값 기준으로 적용)

    remember의 목적 자체가 recomposition이 되어도 기억되는 값이기 때문에 이게 맞는 동작이지...
     */
    var likeState by remember { mutableStateOf(if (card.isWished) LikeState.LIKED else LikeState.NO_LIKE) }

    //recomposition에도 card 기준으로 업데이트 되도록 호출
    LaunchedEffect(card.isWished) {
        likeState = if (card.isWished) LikeState.LIKED else LikeState.NO_LIKE
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp, 4.dp, 8.dp, 4.dp)
            .background(Color.Gray, shape = RoundedCornerShape(8.dp))
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
                        likeState = LikeState.LOADING
                        viewModel.onClickWish(card)
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Icon(
                        imageVector = when (likeState) {
                            LikeState.NO_LIKE -> Icons.Default.Favorite
                            else -> Icons.Default.FavoriteBorder
                        },
                        contentDescription = "Favorite",
                        tint = when (likeState) {
                            LikeState.NO_LIKE -> Color.White
                            LikeState.LOADING -> Color.Blue
                            LikeState.LIKED -> Color.Red
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
            imageUrl = "https://static.wikia.nocookie.net/balatrogame/images/e/ef/Joker.png/revision/latest/scale-to-width-down/70?cb=20230925003651"
        )
    )
}

