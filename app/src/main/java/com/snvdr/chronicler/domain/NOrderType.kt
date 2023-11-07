package com.snvdr.chronicler.domain
 sealed interface NOrderType{
     object Ascending:NOrderType
     object Descending:NOrderType
 }