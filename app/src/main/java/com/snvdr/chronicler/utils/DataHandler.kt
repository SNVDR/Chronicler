package com.snvdr.chronicler.utils

sealed class DataHandler<T>(val data:T? = null, val message: String? = null){
    class Loading<T>:DataHandler<T>()

    class Success<T>(data: T):DataHandler<T>(data = data)

    class Error<T>(data: T? = null, message: String):DataHandler<T>(data = data, message = message)
}
