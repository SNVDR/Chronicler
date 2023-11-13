package com.snvdr.chronicler.domain.chronicle.use_cases

import com.snvdr.chronicler.domain.chronicle.ChronicleRepository
import com.snvdr.chronicler.domain.chronicle.SaveChronicleModel
import com.snvdr.chronicler.utils.DataHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CreateChronicleUseCase @Inject constructor(
    private val chronicleRepository: ChronicleRepository
) {

    operator fun invoke(saveChronicleModel: SaveChronicleModel): Flow<DataHandler<Unit>> {
        // Validate the input data
        val validationError = validateInput(saveChronicleModel)
        return if (validationError != null) {
            // Return an error Flow if validation fails
            flow {
                emit(DataHandler.Error(message = validationError))
            }
        } else {
            // Call the repository function if validation passes
            chronicleRepository.createChronicle(saveChronicleModel = saveChronicleModel)
        }
    }

    private fun validateInput(saveChronicleModel: SaveChronicleModel): String? {
        // Perform validation logic
        return if (saveChronicleModel.title.isEmpty()) {
            "Title can't be empty"
        } else {
            null // Return null if validation passes
        }
    }

}