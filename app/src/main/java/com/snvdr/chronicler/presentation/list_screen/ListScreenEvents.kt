package com.snvdr.chronicler.presentation.list_screen

import com.snvdr.chronicler.domain.chronicle.ChronicleDto
import com.snvdr.chronicler.domain.chronicle.ChronicleOrder

sealed class ListScreenEvents {
    object GetChronicles:ListScreenEvents()
    data class Order(val chronicleOrder: ChronicleOrder):ListScreenEvents()
    data class DeleteChronicle(val chronicleDto: ChronicleDto):ListScreenEvents()
    object SwitchOrderSection:ListScreenEvents()
    data class SearchTextChange(val text:String):ListScreenEvents()
    object SearchByQuery:ListScreenEvents()
}