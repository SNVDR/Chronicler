package com.snvdr.chronicler.presentation.chronicle_details_screen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snvdr.chronicler.domain.ChronicleDto
import com.snvdr.chronicler.domain.use_cases.GetChronicleByIdUseCase
import com.snvdr.chronicler.domain.use_cases.UpdateChronicleUseCase
import com.snvdr.chronicler.utils.DataHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChronicleDetailsViewModel @Inject constructor(
    private val updateChronicleUseCase: UpdateChronicleUseCase,
    private val getChronicleByIdUseCase: GetChronicleByIdUseCase,
):ViewModel(){

    var screenState  by mutableStateOf(ChronicleDetailsScreenUIState())

    private val _testState = MutableStateFlow(ChronicleDetailsScreenUIState())
    val testState = _testState.asStateFlow()

    private var updateChronicleJob: Job? = null

    fun onEvent(events: ChronicleDetailsEvents){
        when(events){
            is ChronicleDetailsEvents.ContentChanged -> {
                screenState = screenState.copy(
                    content = events.content
                )
                _testState.value = testState.value.copy(
                    content = events.content
                )
                Log.d("TEST_DEB","Content Value in _testState:${_testState.value.content}")

            }
            ChronicleDetailsEvents.Submit -> {

            }
            is ChronicleDetailsEvents.TitleChanged -> {
                screenState = screenState.copy(
                    title = events.title
                )
                _testState.value = testState.value.copy(
                    title = events.title
                )
                Log.d("TEST_DEB","Title Value in _testState:${_testState.value.title}")

            }
        }
    }
    fun getChronicleById(id:Long){
        getChronicleByIdUseCase(id = id).onEach {
            when(it){
                is DataHandler.Error -> {
                    screenState = screenState.copy(
                        isLoading = false,
                        isError = it.message?:"Unknown error"
                    )
                    _testState.value = _testState.value.copy(
                        isLoading = false,
                        isError = it.message?:"Unknown error"
                    )
                }
                is DataHandler.Loading -> {
                    screenState = screenState.copy(
                        isLoading = true,
                        isError = null,
                    )
                    _testState.value = _testState.value.copy(
                        isLoading = true,
                        isError = null
                    )
                }
                is DataHandler.Success -> {
                    screenState = screenState.copy(
                        isLoading = false,
                        isError = null,
                        title = it.data!!.title,
                        content = it.data.content
                    )
                    _testState.value = _testState.value.copy(
                        isLoading = false,
                        isError = null,
                        title = it.data!!.title,
                        content = it.data.content
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
    fun updateChronicle(id:Long) = viewModelScope.launch{
        val dto = ChronicleDto(id = id, title = "screenState.title", content = screenState.content?:"")
        updateChronicleUseCase(chronicleDto = dto).onEach {state->
            when(state){
                is DataHandler.Error -> {
                    screenState= screenState.copy(
                        isLoading = false,
                        isError = state.message?:"Unknown error"
                    )
                }
                is DataHandler.Loading -> {
                    screenState= screenState.copy(
                        isLoading = true,
                        isError =null
                    )
                }
                is DataHandler.Success -> {
                    screenState= screenState.copy(
                        isLoading = false,
                        isError =null,
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

}