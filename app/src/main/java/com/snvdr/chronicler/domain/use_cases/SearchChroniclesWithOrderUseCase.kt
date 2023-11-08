package com.snvdr.chronicler.domain.use_cases

import com.snvdr.chronicler.domain.ChronicleDto
import com.snvdr.chronicler.domain.ChronicleOrder
import com.snvdr.chronicler.domain.ChronicleRepository
import com.snvdr.chronicler.utils.DataHandler
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchChroniclesWithOrderUseCase @Inject constructor(
    private val chronicleRepository:ChronicleRepository
) {

    operator fun invoke(query:String, chronicleOrder: ChronicleOrder):Flow<DataHandler<List<ChronicleDto>>>{
        return chronicleRepository.searchDatabaseWithOrder(
            query = "%$query%",
            chronicleOrder = chronicleOrder
        )
    }

}