package com.snvdr.chronicler.presentation.add_update_screen

sealed class AddUpdateChronicleEvents{
    data class TitleChanged(val title: String) : AddUpdateChronicleEvents()
    data class ContentChanged(val content: String) : AddUpdateChronicleEvents()
    object AddOrUpdate:AddUpdateChronicleEvents()
}
