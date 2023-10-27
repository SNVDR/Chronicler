package com.snvdr.chronicler.presentation.main_screen

import com.snvdr.chronicler.domain.ChronicleDto

data class ChroniclesState(
    val isLoading:Boolean = false,
    val chronicles:List<ChronicleDto> = emptyList(),
    val isError:String? = null
)
