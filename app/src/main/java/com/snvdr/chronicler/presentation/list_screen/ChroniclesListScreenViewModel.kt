package com.snvdr.chronicler.presentation.list_screen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snvdr.chronicler.domain.chronicle.ChronicleDto
import com.snvdr.chronicler.domain.chronicle.ChronicleOrder
import com.snvdr.chronicler.domain.chronicle.use_cases.DeleteChronicleUseCase
import com.snvdr.chronicler.domain.chronicle.use_cases.GetAllChroniclesWithOrder
import com.snvdr.chronicler.domain.chronicle.use_cases.SearchChroniclesWithOrderUseCase
import com.snvdr.chronicler.utils.DataHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChroniclesListScreenViewModel @Inject constructor(
    private val getAllChroniclesWithOrder: GetAllChroniclesWithOrder,
    private val deleteChronicleUseCase: DeleteChronicleUseCase,
    private val searchChroniclesWithOrderUseCase: SearchChroniclesWithOrderUseCase
) : ViewModel() {

    private val _screenState = mutableStateOf(ChroniclesListScreenState())
    val screenState: State<ChroniclesListScreenState> = _screenState

    private val navigationChannel = Channel<ListScreenNavigationEvents>()
    val listScreenNavigationEventsChannelFlow: Flow<ListScreenNavigationEvents> =
        navigationChannel.receiveAsFlow()

    private var getChroniclesJob: Job? = null
    private var getChroniclesWithQueryJob: Job? = null
    fun onEvent(event: ListScreenEvents){
        when(event){
            is ListScreenEvents.GetChronicles ->{
                getAllChronicles()
            }
            is ListScreenEvents.DeleteChronicle ->{
                deleteChronicle(event.chronicleDto.id)
            }
            is ListScreenEvents.Order ->{
                if (screenState.value.chronicleOrder::class == event.chronicleOrder::class &&
                    screenState.value.chronicleOrder.orderType == event.chronicleOrder.orderType){
                    return
                }
                if (screenState.value.isSearching && screenState.value.searchText.isNotEmpty()){
                    searchChroniclesByQuery(chronicleOrder = event.chronicleOrder)
                }else{
                    getAllChronicles(chronicleOrder = event.chronicleOrder)
                }
            }
            is ListScreenEvents.SwitchOrderSection -> {
                _screenState.value = screenState.value.copy(
                    isOrderSectionVisible = !screenState.value.isOrderSectionVisible
                )
            }

            is ListScreenEvents.SearchTextChange -> {
                onSearchTextChange(text = event.text)
            }

            ListScreenEvents.SearchByQuery -> {
                searchChroniclesByQuery()
            }
        }
    }
    private fun searchChroniclesByQuery(chronicleOrder: ChronicleOrder = screenState.value.chronicleOrder) {
        getChroniclesWithQueryJob?.cancel()
        getChroniclesWithQueryJob = searchChroniclesWithOrderUseCase(
            query = screenState.value.searchText,
            chronicleOrder = chronicleOrder
        ).onEach {
            _screenState.value = screenState.value.copy(
                chronicleOrder = chronicleOrder
            )
            when (it) {
                is DataHandler.Error -> {
                    _screenState.value = screenState.value.copy(
                        isLoading = false,
                        isError = it.message ?: "Unknown error",
                        chronicles = emptyList()
                    )
                }

                is DataHandler.Loading -> {
                    _screenState.value = screenState.value.copy(
                        isLoading = true,
                        isError = null,
                        chronicles = emptyList()
                    )
                }

                is DataHandler.Success -> {
                    _screenState.value = screenState.value.copy(
                        isLoading = false,
                        isError = null,
                        chronicles = it.data ?: emptyList()
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
    private fun onSearchTextChange(text: String) {
        _screenState.value = screenState.value.copy(
            searchText = text
        )
        if (screenState.value.searchText.isNotEmpty()){
            _screenState.value = screenState.value.copy(
                isSearching = true,
                chronicles = emptyList()
            )
        }else{
            _screenState.value = screenState.value.copy(
                isSearching = false
            )
            getAllChronicles(screenState.value.chronicleOrder)
        }
    }
    fun onNavigateEvent(chronicleDto: ChronicleDto?) = viewModelScope.launch {
        if (chronicleDto != null) {
            navigationChannel.send(ListScreenNavigationEvents.NavigateToUpdateScreen(chronicleDto = chronicleDto))
        } else {
            navigationChannel.send(ListScreenNavigationEvents.NavigateToAddScreen)
        }
    }
    private fun getAllChronicles(
        chronicleOrder: ChronicleOrder = screenState.value.chronicleOrder
    ) {
        Log.d("ROOM_LOG","ViewModel getAllChroniclesWithCustomQuery")
        getChroniclesJob?.cancel()
        getChroniclesJob = getAllChroniclesWithOrder(
          chronicleOrder = chronicleOrder
        ).onEach { chronicle ->
            _screenState.value = screenState.value.copy(
                chronicleOrder = chronicleOrder
            )
            when (chronicle) {
                is DataHandler.Error -> {
                    _screenState.value = screenState.value.copy(
                        isLoading = false,
                        isError = chronicle.message ?: "Unknown error",
                        chronicles = emptyList()
                    )
                }

                is DataHandler.Loading -> {
                    _screenState.value = screenState.value.copy(
                        isLoading = true,
                        isError = null,
                        chronicles = emptyList()
                    )
                }

                is DataHandler.Success -> {
                    _screenState.value = screenState.value.copy(
                        isLoading = false,
                        isError = null,
                        chronicles = chronicle.data ?: emptyList()
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
    private fun deleteChronicle(id:Long){
        deleteChronicleUseCase(id = id).onEach {
            when(it){
                is DataHandler.Error -> {
                    _screenState.value = screenState.value.copy(
                        isLoading = false,
                        isError = it.message ?: "Unknown error",
                    )
                }

                is DataHandler.Loading -> {
                    _screenState.value = screenState.value.copy(
                        isLoading = true,
                        isError = null,
                    )
                }

                is DataHandler.Success -> {
                    _screenState.value = screenState.value.copy(
                        isLoading = false,
                        isError = null,
                    )
                    getAllChroniclesWithOrder(chronicleOrder = screenState.value.chronicleOrder)
                }
            }
        }.launchIn(viewModelScope)
    }

}

