package com.snvdr.chronicler.domain.use_cases

import com.snvdr.chronicler.domain.ChronicleDto
import com.snvdr.chronicler.domain.ChronicleRepository
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