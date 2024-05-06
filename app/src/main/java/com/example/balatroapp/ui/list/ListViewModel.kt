package com.example.balatroapp.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val listRepository: ListRepository
) : ViewModel() {
    data class ListUiModel(
        val cards: List<Card> = listOf()
    )

    private val _state = MutableStateFlow(ListUiModel())
    val state: StateFlow<ListUiModel> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.value = ListUiModel(
                cards = listRepository.getCards()
            )
        }
    }

    fun onClickWish(wishedCard: Card) {
        viewModelScope.launch {
            val isWished = wishedCard.isWished

            _state.value = _state.value.copy(
                cards = _state.value.cards.map { card ->
                    if (card.name == wishedCard.name) {
                        card.copy(
                            isWished = Card.WishedState.LOADING
                        )
                    } else {
                        card
                    }
                }
            )
            delay(1000)
            _state.value = _state.value.copy(
                cards = _state.value.cards.map { card ->
                    if (card.name == wishedCard.name) {
                        card.copy(
                            isWished = isWished.not()
                        )
                    } else {
                        card
                    }
                }
            )
        }
    }
}

fun <T> MutableStateFlow<T>.to(copy: (T) -> T) {
    this.value = copy(this.value)
}