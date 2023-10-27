package com.snvdr.chronicler.di

import com.snvdr.chronicler.data.ChronicleRepositoryImpl
import com.snvdr.chronicler.data.database.ChronicleDao
import com.snvdr.chronicler.domain.ChronicleRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(ViewModelComponent::class)
object ChronicleModule {

    @Provides
    fun providesChronicleRepository(dao: ChronicleDao):ChronicleRepository =
        ChronicleRepositoryImpl(dao = dao)

}