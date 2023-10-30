package com.snvdr.chronicler.presentation.main_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snvdr.chronicler.domain.ChronicleDto
import com.snvdr.chronicler.domain.use_cases.CreateChronicleUseCase
import com.snvdr.chronicler.domain.use_cases.GetAllChroniclesUseCase
import com.snvdr.chronicler.utils.DataHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class MainViewModel @Inject constructor(
    private val createChronicleUseCase: CreateChronicleUseCase,
    private val getAllChroniclesUseCase: GetAllChroniclesUseCase
):ViewModel() {

    private val _mainScreenUIState = mutableStateOf(MainScreenUIState())
    val mainScreenUIState:State<MainScreenUIState> = _mainScreenUIState

    private var getChroniclesJob:Job? = null

    init {
        getChronicles()
    }

    fun getChronicles(){
        getChroniclesJob?.cancel()
        getChroniclesJob = getAllChroniclesUseCase()
            .onEach {chronicle->
               when(chronicle){
                   is DataHandler.Error -> {
                       _mainScreenUIState.value = mainScreenUIState.value.copy(
                           isLoading = false,
                           isError = chronicle.message?:"Unknown error",
                           chronicles = emptyList()
                       )
                   }
                   is DataHandler.Loading -> {
                       _mainScreenUIState.value = mainScreenUIState.value.copy(
                           isLoading = true,
                           isError = null,
                           chronicles = emptyList()
                       )
                   }
                   is DataHandler.Success -> {
                       _mainScreenUIState.value = mainScreenUIState.value.copy(
                           isLoading = false,
                           isError = null,
                           chronicles = chronicle.data?: emptyList()
                       )
                   }
               }
        }.launchIn(viewModelScope)
    }

    fun addData() = viewModelScope.launch{
        val fakeData = ChronicleDto(Random.nextLong(),"Mock title","Mock content")
        createChronicleUseCase(fakeData).onEach {
            when(it){
                is DataHandler.Error -> {
                    _mainScreenUIState.value = mainScreenUIState.value.copy(
                        isLoading = false,
                        isError = it.message?:"Unknown error",
                        chronicles = emptyList()
                    )
                }
                is DataHandler.Loading -> {
                    _mainScreenUIState.value = mainScreenUIState.value.copy(
                        isLoading = true,
                        isError = null,
                        chronicles = emptyList()
                    )
                }
                is DataHandler.Success ->{
                    _mainScreenUIState.value = mainScreenUIState.value.copy(
                        isLoading = false,
                        isError = null,
                        chronicles = emptyList()
                    )
                    getChronicles()
                }
            }
        }.launchIn(viewModelScope)
    }
}