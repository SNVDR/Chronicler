package com.snvdr.chronicler.presentation.chronicle_details_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snvdr.chronicler.domain.ChronicleDto
import com.snvdr.chronicler.domain.use_cases.GetChronicleByIdUseCase
import com.snvdr.chronicler.domain.use_cases.UpdateChronicleUseCase
import com.snvdr.chronicler.presentation.destinations.ChronicleDetailsScreenWrapperDestination
import com.snvdr.chronicler.utils.DataHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChronicleDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val updateChronicleUseCase: UpdateChronicleUseCase,
    private val getChronicleByIdUseCase: GetChronicleByIdUseCase,
) : ViewModel() {

    //  private val chronicleId:Long = checkNotNull(savedStateHandle["chronicleId"])

    private val _screenState = MutableStateFlow(ChronicleDetailsScreenState())
    val screenState = _screenState.asStateFlow()

    private val argsFromScreen = ChronicleDetailsScreenWrapperDestination.argsFrom(savedStateHandle)

    init {
        getChronicleById()
    }

    fun onEvent(event: ChronicleDetailsEvents) {
        when (event) {
            is ChronicleDetailsEvents.ContentChanged -> {
                _screenState.value = screenState.value.copy(
                    content = event.content
                )
                updateChronicle()
            }

            is ChronicleDetailsEvents.TitleChanged -> {
                _screenState.value = screenState.value.copy(
                    title = event.title
                )
                updateChronicle()
            }
        }
    }

    private fun getChronicleById() {
        getChronicleByIdUseCase(id = argsFromScreen.chronicleId).onEach {
            when (it) {
                is DataHandler.Error -> {
                    _screenState.value = _screenState.value.copy(
                        isLoading = false,
                        isError = it.message ?: "Unknown error"
                    )
                }

                is DataHandler.Loading -> {
                    _screenState.value = _screenState.value.copy(
                        isLoading = true,
                        isError = null
                    )
                }

                is DataHandler.Success -> {
                    _screenState.value = _screenState.value.copy(
                        isLoading = false,
                        isError = null,
                        title = it.data!!.title,
                        content = it.data.content
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun updateChronicle() = viewModelScope.launch(Dispatchers.IO) {
        val dto = ChronicleDto(
            id = argsFromScreen.chronicleId,
            title = _screenState.value.title,
            content = _screenState.value.content ?: "",
            date = ""
        )
        updateChronicleUseCase(chronicleDto = dto).onEach { state ->
            when (state) {
                is DataHandler.Error -> {
                    _screenState.value = _screenState.value.copy(
                        isLoading = false,
                        isError = state.message ?: "Unknown error"
                    )
                }

                is DataHandler.Loading -> {
                    _screenState.value = _screenState.value.copy(
                        isLoading = true,
                        isError = null
                    )
                }

                is DataHandler.Success -> {
                    _screenState.value = _screenState.value.copy(
                        isLoading = false,
                        isError = null,
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

}