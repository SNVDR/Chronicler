package com.snvdr.chronicler.presentation.main_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snvdr.chronicler.domain.ChronicleDto
import com.snvdr.chronicler.domain.ChronicleRepository
import com.snvdr.chronicler.domain.GetAllChroniclesUseCase
import com.snvdr.chronicler.utils.DataHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class ChronicleViewModel @Inject constructor(
    private val chronicleRepository: ChronicleRepository,
    private val getAllChroniclesUseCase: GetAllChroniclesUseCase
):ViewModel() {

    private val _chroniclesState = mutableStateOf(ChroniclesState())
    val chroniclesState:State<ChroniclesState> = _chroniclesState

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
                       _chroniclesState.value = chroniclesState.value.copy(
                           isLoading = false,
                           isError = chronicle.message?:"Unknown error",
                           chronicles = emptyList()
                       )
                   }
                   is DataHandler.Loading -> {
                       _chroniclesState.value = chroniclesState.value.copy(
                           isLoading = true,
                           isError = null,
                           chronicles = emptyList()
                       )
                   }
                   is DataHandler.Success -> {
                       _chroniclesState.value = chroniclesState.value.copy(
                           isLoading = false,
                           isError = null,
                           chronicles = chronicle.data?: emptyList()
                       )
                   }
               }
        }.launchIn(viewModelScope)
    }

    fun addData() = viewModelScope.launch{
        chronicleRepository.createChronicle(ChronicleDto(Random.nextLong(),"Mock title","Mock content")).onEach {

        }.launchIn(viewModelScope)
        getChronicles()
    }

}