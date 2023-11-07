package com.snvdr.chronicler.presentation.add_update_screen

data class AddUpdateChronicleState(
    val isLoading:Boolean = false,
    val isError:String? = null,
    val title:String = "",
    val titleError:String? = null,
    val content:String? = null,
    val contentError:String? = null
)