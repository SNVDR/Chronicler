package com.snvdr.chronicler.domain.chronicle.use_cases

import com.snvdr.chronicler.domain.chronicle.ChronicleRepository
import com.snvdr.chronicler.utils.DataHandler
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteChronicleUseCase @Inject constructor(
    private val chronicleRepository: ChronicleRepository
){

    operator fun invoke(id:Long): Flow<DataHandler<Unit>>{
        return chronicleRepository.deleteSpecificChronicleById(id = id)
    }

}