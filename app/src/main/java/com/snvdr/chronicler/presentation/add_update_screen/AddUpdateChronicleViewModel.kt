package com.snvdr.chronicler.presentation.add_update_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snvdr.chronicler.domain.chronicle.ChronicleDto
import com.snvdr.chronicler.domain.chronicle.SaveChronicleModel
import com.snvdr.chronicler.domain.chronicle.use_cases.CreateChronicleUseCase
import com.snvdr.chronicler.domain.chronicle.use_cases.GetChronicleByIdUseCase
import com.snvdr.chronicler.domain.chronicle.use_cases.UpdateChronicleUseCase
import com.snvdr.chronicler.presentation.destinations.AddUpdateScreenWrapperDestination
import com.snvdr.chronicler.utils.DataHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
class AddUpdateChronicleViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val updateChronicleUseCase: UpdateChronicleUseCase,
    private val getChronicleByIdUseCase: GetChronicleByIdUseCase,
    private val createChronicleUseCase: CreateChronicleUseCase
) : ViewModel() {

    //  private val chronicleId:Long = checkNotNull(savedStateHandle["chronicleId"])

    private val _screenState = MutableStateFlow(AddUpdateChronicleState())
    val screenState = _screenState.asStateFlow()

    private val argsFromScreen = AddUpdateScreenWrapperDestination.argsFrom(savedStateHandle)
    private val navigationChannel = Channel<AddUpdateScreenNavigationEvents>()
    val navigationEventsChannelFlow: Flow<AddUpdateScreenNavigationEvents> = navigationChannel.receiveAsFlow()

    init {
        argsFromScreen.chronicleId?.let {
            getChronicleById(id = it.toLong())
        }
    }
    fun onEvent(event: AddUpdateChronicleEvents) {
        when (event) {
            is AddUpdateChronicleEvents.ContentChanged -> {
                _screenState.value = screenState.value.copy(
                    content = event.content
                )
            }

            is AddUpdateChronicleEvents.TitleChanged -> {
                _screenState.value = screenState.value.copy(
                    title = event.title
                )
            }

            AddUpdateChronicleEvents.AddOrUpdate -> {
                addOrUpdate()
            }
        }
    }
    private fun getChronicleById(id:Long) {
        getChronicleByIdUseCase(id = id).onEach {
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
        if (argsFromScreen.chronicleId.isNullOrEmpty()){
            return@launch
        }

        val dto = ChronicleDto(
            id = argsFromScreen.chronicleId.toLong(),
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
    private fun createChronicle() {
        val chronicle = SaveChronicleModel(title = _screenState.value.title, content = _screenState.value.content?:"")
        createChronicleUseCase(chronicle).onEach {
            when(it){
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
                    )
                    navigationChannel.send(AddUpdateScreenNavigationEvents.NavigateBack)
                }
            }
        }.launchIn(viewModelScope)
    }
    private fun addOrUpdate(){
        if (argsFromScreen.chronicleId.isNullOrEmpty()){
            createChronicle()
        }else{
            updateChronicle()
        }
    }

}