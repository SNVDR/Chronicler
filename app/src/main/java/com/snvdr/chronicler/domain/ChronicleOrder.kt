package com.snvdr.chronicler.domain

sealed class ChronicleOrder(val orderType: OrderType){
    class Title(orderType: OrderType):ChronicleOrder(orderType = orderType)
    class Date(orderType: OrderType):ChronicleOrder(orderType = orderType)

    fun copy(orderType: OrderType):ChronicleOrder{
        return when(this){
            is Title -> Title(orderType)
            is Date -> Date(orderType)
        }
    }
}