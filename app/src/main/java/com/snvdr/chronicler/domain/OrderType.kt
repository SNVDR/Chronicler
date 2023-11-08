package com.snvdr.chronicler.domain
 sealed interface OrderType{
     object Ascending:OrderType
     object Descending:OrderType
 }