package com.snvdr.chronicler.domain.chronicle

import com.snvdr.chronicler.data.chronicle.database.ChronicleDbEntity

data class ChronicleDto(
    val id:Long,
    val title:String,
    val content:String,
    val date: String,
)

fun ChronicleDbEntity.toChronicleDto(): ChronicleDto {
    return ChronicleDto(id = id, title = title, content = content, date = this.createdDateFormatted)
}

fun ChronicleDto.toChronicleDbEntity(): ChronicleDbEntity {
    return ChronicleDbEntity(id = id, title = title, content = content)
}
