package com.example.balatroapp.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.balatroapp.Screen
import com.example.balatroapp.ui.list.Card
import com.example.balatroapp.ui.list.viewModelLocalOf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


val LocalDetailViewModelProvider = viewModelLocalOf<DetailViewModel>()


@Composable
fun DetailScreen(
    navController: NavHostController,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CompositionLocalProvider(value = LocalDetailViewModelProvider provides { viewModel }) {
        when (state) {
            is UiModel.Success -> {
                DetailSuccess((state as UiModel.Success).result.card)
            }

            UiModel.Loading,
            is UiModel.Fail,
            null -> {

            }
        }
    }
}

@Composable
fun DetailSuccess(card: CardDetail) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp, 4.dp, 8.dp, 4.dp)
                .background(Color.Yellow, shape = RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = card.title, style = TextStyle(
                    fontSize = 32.sp,
                    textAlign = TextAlign.Center
                )
            )
        }
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(card.imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = "",
            modifier = Modifier.size(320.dp),
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp, 4.dp, 8.dp, 4.dp)
                .background(Color.Gray, shape = RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Effect", style = TextStyle(textAlign = TextAlign.Center, fontSize = 20.sp))
                Box(
                    modifier = Modifier
                        .padding(8.dp, 4.dp, 8.dp, 4.dp)
                        .fillMaxWidth()
                        .background(Color.Black, shape = RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = card.effectDesc, style = TextStyle(
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp, 4.dp, 8.dp, 4.dp)
                .background(Color.Gray, shape = RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Rarity", style = TextStyle(textAlign = TextAlign.Center, fontSize = 20.sp))
                Box(
                    modifier = Modifier
                        .padding(8.dp, 4.dp, 8.dp, 4.dp)
                        .fillMaxWidth()
                        .background(Color.Black, shape = RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = card.rarityDesc, style = TextStyle(
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp, 4.dp, 8.dp, 4.dp)
                .background(Color.Gray, shape = RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Stats", style = TextStyle(textAlign = TextAlign.Center, fontSize = 20.sp))
                Box(
                    modifier = Modifier
                        .padding(8.dp, 4.dp, 8.dp, 4.dp)
                        .fillMaxWidth()
                        .background(Color.Black, shape = RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column {
                        Text(
                            text = card.buyPrice, style = TextStyle(
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                        )
                        Text(
                            text = card.sellPrice, style = TextStyle(
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                }
            }
        }
    }
}

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    detailRepository: DetailRepository,
) : ViewModel() {
    private val link: String = checkNotNull(savedStateHandle["link"])

    private val _state = MutableStateFlow<UiModel<DetailUiModel>?>(null)
    val state: StateFlow<UiModel<DetailUiModel>?> = _state.asStateFlow()

    data class DetailUiModel(
        val card: CardDetail
    )

    init {
        viewModelScope.launch {
            _state.value = UiModel.Loading

            val result = detailRepository.getCard(link)
            if (result != null) {
                _state.value = UiModel.Success(
                    DetailUiModel(
                        card = result
                    )
                )
            } else {
                _state.value = UiModel.Fail()
            }
        }
    }
}

@Preview
@Composable
fun DetailSuccessPreview() {
    DetailSuccess(
        card = CardDetail(
            title = "title",
            imageUrl = "imageUrl",
            effectDesc = "effectDesc",
            rarityDesc = "rarityDesc",
            buyPrice = "buyPrice",
            sellPrice = "sellPrice",
        )
    )
}
