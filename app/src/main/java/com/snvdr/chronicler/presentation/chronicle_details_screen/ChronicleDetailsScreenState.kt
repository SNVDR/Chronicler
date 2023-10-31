package com.snvdr.chronicler.presentation.chronicle_details_screen

data class ChronicleDetailsScreenState(
    val isLoading:Boolean = false,
    val isError:String? = null,
    val title:String = "",
    val titleError:String? = null,
    val content:String? = null,
    val contentError:String? = null
)