package com.snvdr.chronicler.domain.chronicle.use_cases

import com.snvdr.chronicler.domain.chronicle.ChronicleDto
import com.snvdr.chronicler.domain.chronicle.ChronicleRepository
import com.snvdr.chronicler.domain.chronicle.ChronicleOrder
import com.snvdr.chronicler.utils.DataHandler
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllChroniclesWithOrder @Inject constructor(
    private val chronicleRepository: ChronicleRepository
) {
    operator fun invoke(chronicleOrder: ChronicleOrder): Flow<DataHandler<List<ChronicleDto>>>{
        return chronicleRepository.getChroniclesWithOrder(chronicleOrder = chronicleOrder)
    }

}