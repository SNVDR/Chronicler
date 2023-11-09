package com.snvdr.chronicler.presentation.list_screen

import com.snvdr.chronicler.domain.chronicle.ChronicleDto

sealed class ListScreenNavigationEvents{
    data class NavigateToUpdateScreen(val chronicleDto: ChronicleDto) : ListScreenNavigationEvents()
    object NavigateToAddScreen:ListScreenNavigationEvents()
}
