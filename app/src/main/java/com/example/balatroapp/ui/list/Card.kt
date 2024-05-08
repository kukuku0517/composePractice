package com.example.balatroapp.ui.list

data class Card(
    val name: String,
    val imageUrl: String,
    val linkUrl : String,
    val description: String,
    val isWished: WishedState = WishedState.UNWISHED
) {
    enum class WishedState {
        WISHED,
        LOADING,
        UNWISHED;

        fun not(): WishedState {
            return if (this == WISHED) {
                UNWISHED
            } else if (this == UNWISHED) {
                WISHED
            } else {
                LOADING
            }
        }
    }
}