package com.snvdr.chronicler.di

import android.content.Context
import androidx.room.Room
import com.snvdr.chronicler.data.database.ChronicleDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun providesChronicleDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, ChronicleDatabase::class.java, "chronicles_database").build()

    @Singleton
    @Provides
    fun providesChroniclesDao(database: ChronicleDatabase) = database.getChronicleDao()

}