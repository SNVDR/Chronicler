package com.snvdr.chronicler.domain.use_cases

import com.snvdr.chronicler.domain.ChronicleDto
import com.snvdr.chronicler.domain.ChronicleRepository
import com.snvdr.chronicler.utils.DataHandler
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateChronicleUseCase @Inject constructor(
    private val chronicleRepository: ChronicleRepository
) {

     operator fun invoke(chronicleDto: ChronicleDto):Flow<DataHandler<Unit>>{
        return chronicleRepository.createChronicle(chronicle = chronicleDto)
    }

}