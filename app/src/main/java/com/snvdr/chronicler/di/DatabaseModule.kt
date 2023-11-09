package com.snvdr.chronicler.di

import android.content.Context
import androidx.room.Room
import com.snvdr.chronicler.data.audio_record.database.AudioRecordDatabase
import com.snvdr.chronicler.data.chronicle.database.ChronicleDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun providesChronicleDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, ChronicleDatabase::class.java, "chronicles_database").build()

    @Singleton
    @Provides
    fun providesAudioRecordDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, AudioRecordDatabase::class.java, "audio_database").allowMainThreadQueries().build()

    @Singleton
    @Provides
    fun providesChroniclesDao(database: ChronicleDatabase) = database.getChronicleDao()

    @Singleton
    @Provides
    fun providesAudioRecordDao(database: AudioRecordDatabase) = database.audioRecordDAO()
}