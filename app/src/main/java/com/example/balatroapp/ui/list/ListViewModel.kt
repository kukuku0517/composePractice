package com.example.balatroapp.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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

}