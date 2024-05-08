package com.example.balatroapp.ui.detail

sealed class UiModel<out T> {
    data class Success<T>(
        val result: T
    ) : UiModel<T>()

    data class Fail(
        val e: Exception? = null
    ) : UiModel<Nothing>()

    data object Loading : UiModel<Nothing>()
}