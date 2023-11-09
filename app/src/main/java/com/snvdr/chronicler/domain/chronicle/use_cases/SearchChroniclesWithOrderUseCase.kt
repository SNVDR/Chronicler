package com.snvdr.chronicler.domain.chronicle.use_cases

import com.snvdr.chronicler.domain.chronicle.ChronicleDto
import com.snvdr.chronicler.domain.chronicle.ChronicleOrder
import com.snvdr.chronicler.domain.chronicle.ChronicleRepository
import com.snvdr.chronicler.utils.DataHandler
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchChroniclesWithOrderUseCase @Inject constructor(
    private val chronicleRepository: ChronicleRepository
) {

    operator fun invoke(query:String, chronicleOrder: ChronicleOrder):Flow<DataHandler<List<ChronicleDto>>>{
        return chronicleRepository.searchDatabaseWithOrder(
            query = "%$query%",
            chronicleOrder = chronicleOrder
        )
    }

}