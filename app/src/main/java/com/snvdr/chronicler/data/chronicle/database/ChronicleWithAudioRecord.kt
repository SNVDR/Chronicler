package com.snvdr.chronicler.data.chronicle.database

import androidx.room.Embedded
import androidx.room.Relation
import com.snvdr.chronicler.data.audio_record.database.AudioRecordDbEntity

data class ChronicleWithAudioRecord(
    @Embedded val chronicleDbEntity: ChronicleDbEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "chronicleId"
    )
    val audioRecords:List<AudioRecordDbEntity>
)