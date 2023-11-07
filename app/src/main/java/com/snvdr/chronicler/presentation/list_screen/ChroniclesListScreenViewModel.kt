package com.snvdr.chronicler.presentation.list_screen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snvdr.chronicler.domain.ChronicleDto
import com.snvdr.chronicler.domain.ChronicleOrder
import com.snvdr.chronicler.domain.ChronicleRepository
import com.snvdr.chronicler.domain.NChronicleOrder
import com.snvdr.chronicler.domain.OrderType
import com.snvdr.chronicler.domain.use_cases.CreateChronicleUseCase
import com.snvdr.chronicler.domain.use_cases.GetAllChroniclesUseCase
import com.snvdr.chronicler.domain.use_cases.GetAllChroniclesWithCustomQuery
import com.snvdr.chronicler.utils.DataHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val createChronicleUseCase: CreateChronicleUseCase,
    private val getAllChroniclesUseCase: GetAllChroniclesUseCase,
    private val getAllChroniclesWithCustomQueryUseCase: GetAllChroniclesWithCustomQuery,
    private val chronicleRepository: ChronicleRepository
) : ViewModel() {

    private val _chroniclesListScreenState = mutableStateOf(ChroniclesListScreenState())
    val chroniclesListScreenState: State<ChroniclesListScreenState> = _chroniclesListScreenState

    private val navigationChannel = Channel<ListScreenNavigationEvents>()
    val listScreenNavigationEventsChannelFlow: Flow<ListScreenNavigationEvents> =
        navigationChannel.receiveAsFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private var getChroniclesJob: Job? = null


    fun onEvent(event: ListScreenEvents){
        when(event){
            is ListScreenEvents.DeleteChronicle ->{

            }
            is ListScreenEvents.Order ->{
                if (chroniclesListScreenState.value.chronicleOrder::class == event.chronicleOrder::class &&
                    chroniclesListScreenState.value.chronicleOrder.nOrderType == event.chronicleOrder.nOrderType){
                    return
                }
                newGetAllChroniclesWithCustomQuery(nChronicleOrder = event.chronicleOrder)
            }
            is ListScreenEvents.SwitchOrderSection -> {
                _chroniclesListScreenState.value = chroniclesListScreenState.value.copy(
                    isOrderSectionVisible = !chroniclesListScreenState.value.isOrderSectionVisible
                )
            }
        }
    }
    fun searchChroniclesByQuery() {
        chronicleRepository.searchDatabase(query = _searchText.value).onEach {
            when (it) {
                is DataHandler.Error -> {
                    _chroniclesListScreenState.value = chroniclesListScreenState.value.copy(
                        isLoading = false,
                        isError = it.message ?: "Unknown error",
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
                    Log.d("SEARCH_LOG", "Result is:${it.data}")
                    //  getChronicles()
                    _chroniclesListScreenState.value = chroniclesListScreenState.value.copy(
                        isLoading = false,
                        isError = null,
                        chronicles = it.data ?: emptyList()
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
    fun onSearchTextChange(text: String) {
        _searchText.value = text
        if (_searchText.value.isNotEmpty()) {
            _chroniclesListScreenState.value = _chroniclesListScreenState.value.copy(
                isSearchingChronicle = true,
                chronicles = emptyList()
            )
        } else {
            _chroniclesListScreenState.value = _chroniclesListScreenState.value.copy(
                isSearchingChronicle = false
            )
        }
    }
    fun navigateToAnotherScreen(chronicleDto: ChronicleDto?) = viewModelScope.launch {
        if (chronicleDto != null) {
            navigationChannel.send(ListScreenNavigationEvents.NavigateToUpdateScreen(chronicleDto = chronicleDto))
        } else {
            navigationChannel.send(ListScreenNavigationEvents.NavigateToAddScreen)
        }
    }
    fun getAllChroniclesWithCustomQuery(
        chronicleOrder: ChronicleOrder = ChronicleOrder.date,
        orderType: OrderType = OrderType.DESC
    ) {
        Log.d("ROOM_LOG","ViewModel getAllChroniclesWithCustomQuery")

        getAllChroniclesWithCustomQueryUseCase(
           chronicleOrder = chronicleOrder,
            orderType = orderType
        ).onEach { chronicle ->
            when (chronicle) {
                is DataHandler.Error -> {
                    _chroniclesListScreenState.value = chroniclesListScreenState.value.copy(
                        isLoading = false,
                        isError = chronicle.message ?: "Unknown error",
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
                        chronicles = chronicle.data ?: emptyList()
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun newGetAllChroniclesWithCustomQuery(
       nChronicleOrder: NChronicleOrder
    ) {
        Log.d("ROOM_LOG","ViewModel getAllChroniclesWithCustomQuery")
        getChroniclesJob?.cancel()
        getChroniclesJob = chronicleRepository.newGetChroniclesCustomQuery(
          nChronicleOrder = nChronicleOrder
        ).onEach { chronicle ->
            _chroniclesListScreenState.value = chroniclesListScreenState.value.copy(
                chronicleOrder = nChronicleOrder
            )
            when (chronicle) {
                is DataHandler.Error -> {
                    _chroniclesListScreenState.value = chroniclesListScreenState.value.copy(
                        isLoading = false,
                        isError = chronicle.message ?: "Unknown error",
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
                        chronicles = chronicle.data ?: emptyList()
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

}

