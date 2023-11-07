package com.snvdr.chronicler.domain.use_cases

import com.snvdr.chronicler.domain.ChronicleDto
import com.snvdr.chronicler.domain.ChronicleOrder
import com.snvdr.chronicler.domain.ChronicleRepository
import com.snvdr.chronicler.domain.NChronicleOrder
import com.snvdr.chronicler.domain.OrderType
import com.snvdr.chronicler.utils.DataHandler
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewGetAllChroniclesWithCustomQuery @Inject constructor(
    private val chronicleRepository: ChronicleRepository
) {
    operator fun invoke(nChronicleOrder:NChronicleOrder): Flow<DataHandler<List<ChronicleDto>>>{
        return chronicleRepository.newGetChroniclesCustomQuery(nChronicleOrder = nChronicleOrder)
    }

}