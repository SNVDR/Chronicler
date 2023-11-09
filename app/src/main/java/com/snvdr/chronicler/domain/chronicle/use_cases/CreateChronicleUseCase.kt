package com.snvdr.chronicler.domain.chronicle.use_cases

import com.snvdr.chronicler.domain.chronicle.ChronicleRepository
import com.snvdr.chronicler.domain.chronicle.SaveChronicleModel
import com.snvdr.chronicler.utils.DataHandler
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateChronicleUseCase @Inject constructor(
    private val chronicleRepository: ChronicleRepository
) {

     operator fun invoke(saveChronicleModel: SaveChronicleModel):Flow<DataHandler<Unit>>{
        return chronicleRepository.createChronicle(saveChronicleModel = saveChronicleModel)
    }

}