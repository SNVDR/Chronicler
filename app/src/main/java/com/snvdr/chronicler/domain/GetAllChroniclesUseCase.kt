package com.snvdr.chronicler.domain

import com.snvdr.chronicler.utils.DataHandler
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllChroniclesUseCase @Inject constructor(
    private val chronicleRepository: ChronicleRepository
) {

    operator fun invoke():Flow<DataHandler<List<ChronicleDto>>>{
        return chronicleRepository.getAllChronicles()
    }

}