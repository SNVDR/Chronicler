package com.snvdr.chronicler.domain.chronicle
 sealed interface OrderType{
     object Ascending: OrderType
     object Descending: OrderType
 }