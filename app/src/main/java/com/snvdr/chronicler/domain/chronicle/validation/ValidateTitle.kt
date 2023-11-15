package com.snvdr.chronicler.domain.chronicle.validation

class ValidateTitle {

    fun execute(title:String):ValidationResult{
        if (title.isBlank()){
            return ValidationResult(
                success = false,
                errorMessage = "Title can't be blank"
            )
        }
        return ValidationResult(success = true)
    }

}