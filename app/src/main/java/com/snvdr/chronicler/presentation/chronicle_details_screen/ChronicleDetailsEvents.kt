package com.snvdr.chronicler.presentation.chronicle_details_screen

sealed class ChronicleDetailsEvents{
    data class TitleChanged(val title: String) : ChronicleDetailsEvents()
    data class ContentChanged(val content: String) : ChronicleDetailsEvents()
}
