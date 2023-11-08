package com.snvdr.chronicler.presentation.list_screen

import com.snvdr.chronicler.domain.ChronicleDto
import com.snvdr.chronicler.domain.ChronicleOrder
import com.snvdr.chronicler.domain.OrderType

data class ChroniclesListScreenState(
    val isLoading:Boolean = false,
    val chronicles:List<ChronicleDto> = emptyList(),
    val isError:String? = null,
    val isSearching:Boolean = false,
    val searchText:String = "",
    val searchError:String? = null,
    val isOrderSectionVisible:Boolean = false,
    val chronicleOrder:ChronicleOrder = ChronicleOrder.Date(orderType = OrderType.Descending)
)
