package com.snvdr.chronicler.domain.chronicle.validation

data class ValidationResult(
    val success:Boolean,
    val errorMessage:String? = null
)
