package com.snvdr.chronicler.presentation.main_screen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snvdr.chronicler.domain.ChronicleDto
import com.snvdr.chronicler.domain.ChronicleRepository
import com.snvdr.chronicler.domain.use_cases.CreateChronicleUseCase
import com.snvdr.chronicler.domain.use_cases.GetAllChroniclesUseCase
import com.snvdr.chronicler.utils.DataHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val createChronicleUseCase: CreateChronicleUseCase,
    private val getAllChroniclesUseCase: GetAllChroniclesUseCase,
    private val chronicleRepository: ChronicleRepository
):ViewModel() {

    private val _chroniclesListScreenState = mutableStateOf(ChroniclesListScreenState())
    val chroniclesListScreenState:State<ChroniclesListScreenState> = _chroniclesListScreenState

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    init {
        getChronicles()
        Log.d("VM_LOG","init")
    }

     fun searchChroniclesByQuery(){
        chronicleRepository.searchDatabase(query = _searchText.value).onEach {
            when(it){
                is DataHandler.Error -> {
                    _chroniclesListScreenState.value = chroniclesListScreenState.value.copy(
                        isLoading = false,
                        isError = it.message?:"Unknown error",
                        chronicles = emptyList()
                    )
                }
                is DataHandler.Loading ->{
                    _chroniclesListScreenState.value = chroniclesListScreenState.value.copy(
                        isLoading = true,
                        isError = null,
                        chronicles = emptyList()
                    )
                }
                is DataHandler.Success -> {
                    Log.d("SEARCH_LOG","Result is:${it.data}")
                    getChronicles()
                }
            }
        }.launchIn(viewModelScope)
    }

    fun onSearchTextChange(text:String){
        _searchText.value = text
    }
     fun getChronicles(){ getAllChroniclesUseCase().onEach { chronicle->
               when(chronicle){
                   is DataHandler.Error -> {
                       _chroniclesListScreenState.value = chroniclesListScreenState.value.copy(
                           isLoading = false,
                           isError = chronicle.message?:"Unknown error",
                           chronicles = emptyList()
                       )
                   }
                   is DataHandler.Loading -> {
                       _chroniclesListScreenState.value = chroniclesListScreenState.value.copy(
                           isLoading = true,
                           isError = null,
                           chronicles = emptyList()
                       )
                   }
                   is DataHandler.Success -> {
                       _chroniclesListScreenState.value = chroniclesListScreenState.value.copy(
                           isLoading = false,
                           isError = null,
                           chronicles = chronicle.data?: emptyList()
                       )
                   }
               }
        }.launchIn(viewModelScope)
    }

    fun addData() = viewModelScope.launch{
        val fakeData = ChronicleDto(Random.nextLong(),"Mock title","Mock content","1488")
        createChronicleUseCase(fakeData).onEach {
            when(it){
                is DataHandler.Error -> {
                  /*  _chroniclesListScreenState.value = chroniclesListScreenState.value.copy(
                        isLoading = false,
                        isError = it.message?:"Unknown error",
                        chronicles = emptyList()
                    )*/
                }
                is DataHandler.Loading -> {
                  /*  _chroniclesListScreenState.value = chroniclesListScreenState.value.copy(
                        isLoading = true,
                        isError = null,
                        chronicles = emptyList()
                    )*/
                }
                is DataHandler.Success ->{
                 /*   _chroniclesListScreenState.value = chroniclesListScreenState.value.copy(
                        isLoading = false,
                        isError = null,
                        chronicles = emptyList()
                    )
                    getChronicles()*/
                }
            }
        }.launchIn(viewModelScope)
    }

}