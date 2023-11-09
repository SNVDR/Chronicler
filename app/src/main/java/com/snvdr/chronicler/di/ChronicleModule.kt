package com.snvdr.chronicler.di

import com.snvdr.chronicler.data.chronicle.ChronicleRepositoryImpl
import com.snvdr.chronicler.data.chronicle.database.ChronicleDao
import com.snvdr.chronicler.domain.chronicle.ChronicleRepository
import com.snvdr.chronicler.domain.chronicle.use_cases.CreateChronicleUseCase
import com.snvdr.chronicler.domain.chronicle.use_cases.DeleteChronicleUseCase
import com.snvdr.chronicler.domain.chronicle.use_cases.GetChronicleByIdUseCase
import com.snvdr.chronicler.domain.chronicle.use_cases.GetAllChroniclesWithOrder
import com.snvdr.chronicler.domain.chronicle.use_cases.SearchChroniclesWithOrderUseCase
import com.snvdr.chronicler.domain.chronicle.use_cases.UpdateChronicleUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ChronicleModule {

    @Provides
    fun providesChronicleRepository(dao: ChronicleDao): ChronicleRepository =
        ChronicleRepositoryImpl(dao = dao)

    @Provides
    fun providesCreateChronicleUseCase(chronicleRepository: ChronicleRepository) =
        CreateChronicleUseCase(chronicleRepository = chronicleRepository)

    @Provides
    fun providesGetChronicleByIdUseCase(chronicleRepository: ChronicleRepository) =
        GetChronicleByIdUseCase(chronicleRepository = chronicleRepository)

    @Provides
    fun providesUpdateChronicleUseCase(chronicleRepository: ChronicleRepository) =
        UpdateChronicleUseCase(chronicleRepository = chronicleRepository)

    @Provides
    fun providesGetAllChroniclesWithCustomQuery(chronicleRepository: ChronicleRepository) =
        GetAllChroniclesWithOrder(chronicleRepository = chronicleRepository)
    @Provides
    fun providesDeleteChronicleUseCase(chronicleRepository: ChronicleRepository) =
        DeleteChronicleUseCase(chronicleRepository = chronicleRepository)
    @Provides
    fun provideSearchChroniclesWithOrderUseCase(chronicleRepository: ChronicleRepository) =
        SearchChroniclesWithOrderUseCase(chronicleRepository = chronicleRepository)

}