package com.snvdr.chronicler.domain

import com.snvdr.chronicler.data.database.ChronicleDbEntity

data class ChronicleDto(
    val id:Long,
    val title:String,
    val content:String
)

fun ChronicleDbEntity.toChronicleDto():ChronicleDto{
    return ChronicleDto(id = id, title = title, content = content)
}

fun ChronicleDto.toChronicleDbEntity():ChronicleDbEntity{
    return ChronicleDbEntity(id = id, title = title, content = content)
}
