package com.snvdr.chronicler.domain.chronicle.use_cases

import com.snvdr.chronicler.domain.chronicle.ChronicleDto
import com.snvdr.chronicler.domain.chronicle.ChronicleRepository
import com.snvdr.chronicler.utils.DataHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetChronicleByIdUseCase @Inject constructor(
    private val chronicleRepository: ChronicleRepository
) {

    operator fun invoke(id:Long):Flow<DataHandler<ChronicleDto>>{
        return if (id <=0){
            flow {
                emit(DataHandler.Error(message = "Id can't be 0 or less than"))
            }
        }else{
            chronicleRepository.getSpecificChronicle(id = id)
        }
    }

}