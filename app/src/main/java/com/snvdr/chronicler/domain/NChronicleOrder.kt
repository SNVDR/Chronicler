package com.snvdr.chronicler.domain

sealed class NChronicleOrder(val nOrderType: NOrderType){
    class Title(nOrderType: NOrderType):NChronicleOrder(nOrderType = nOrderType)
    class Date(nOrderType: NOrderType):NChronicleOrder(nOrderType = nOrderType)

    fun copy(nOrderType: NOrderType):NChronicleOrder{
        return when(this){
            is Title -> Title(nOrderType)
            is Date -> Date(nOrderType)
        }
    }
}