package com.snvdr.chronicler.presentation.list_screen

import com.snvdr.chronicler.domain.ChronicleDto
import com.snvdr.chronicler.domain.NChronicleOrder

sealed class ListScreenEvents {
    data class Order(val chronicleOrder: NChronicleOrder):ListScreenEvents()
    data class DeleteChronicle(val chronicleDto: ChronicleDto):ListScreenEvents()
    object SwitchOrderSection:ListScreenEvents()
}