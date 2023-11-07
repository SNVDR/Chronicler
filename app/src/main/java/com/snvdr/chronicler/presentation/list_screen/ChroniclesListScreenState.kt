package com.snvdr.chronicler.presentation.list_screen

import com.snvdr.chronicler.domain.ChronicleDto
import com.snvdr.chronicler.domain.ChronicleOrder
import com.snvdr.chronicler.domain.NChronicleOrder
import com.snvdr.chronicler.domain.NOrderType
import com.snvdr.chronicler.domain.OrderType

data class ChroniclesListScreenState(
    val isLoading:Boolean = false,
    val chronicles:List<ChronicleDto> = emptyList(),
    val isError:String? = null,
    val isSearchingChronicle:Boolean = false,
    val isOrderSectionVisible:Boolean = false,
    val chronicleOrder:NChronicleOrder = NChronicleOrder.Date(nOrderType = NOrderType.Descending)
)
