package com.snvdr.chronicler.presentation.main_screen

import com.snvdr.chronicler.domain.ChronicleDto

data class ChroniclesListScreenState(
    val isLoading:Boolean = false,
    val chronicles:List<ChronicleDto> = emptyList(),
    val isError:String? = null,
    val searchingChronicle:Boolean = false
)
