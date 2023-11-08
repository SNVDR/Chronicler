package com.snvdr.chronicler.presentation.list_screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.snvdr.chronicler.domain.ChronicleDto
import com.snvdr.chronicler.domain.ChronicleOrder
import com.snvdr.chronicler.presentation.destinations.AddUpdateScreenWrapperDestination
import com.snvdr.chronicler.utils.ObserveAsEvents
import kotlinx.coroutines.flow.Flow

@Composable
@Destination(start = true)
fun ChronicleListScreenWrapper(navigator: DestinationsNavigator) {
    val viewModel = hiltViewModel<ChroniclesListScreenViewModel>()
    val screenState by viewModel.screenState
    val events = viewModel.listScreenNavigationEventsChannelFlow

    ChronicleListScreen(
        screenState = screenState,
        events = events,
        searchText = screenState.searchText,
        onAddItemClick = {
            viewModel.onNavigateEvent(null)
        }, onChronicleClick = {
            viewModel.onNavigateEvent(it)
        }, onSearchTextChanged = {
            viewModel.onEvent(ListScreenEvents.SearchTextChange(text = it))
        }, onSearchClick = {
            viewModel.onEvent(ListScreenEvents.SearchByQuery)
        }, onResume = {
            Log.d("ROOM_LOG", "onResume")
            viewModel.onEvent(ListScreenEvents.GetChronicles)
        }, onNavigate = {
            if (it != null) {
                navigator.navigate(
                    AddUpdateScreenWrapperDestination(
                        chronicleId = it.id.toString()
                    )
                )
            } else {
                navigator.navigate(
                    AddUpdateScreenWrapperDestination(
                        chronicleId = null
                    )
                )
            }
        }, onOrderChange = {
            viewModel.onEvent(ListScreenEvents.Order(it))
        }, onDelete = {
            viewModel.onEvent(ListScreenEvents.DeleteChronicle(it))
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChronicleListScreen(
    screenState: ChroniclesListScreenState,
    events: Flow<ListScreenNavigationEvents>,
    searchText: String,
    onAddItemClick: () -> Unit,
    onChronicleClick: (ChronicleDto) -> Unit,
    onSearchTextChanged: (String) -> Unit,
    onSearchClick: () -> Unit,
    onResume: () -> Unit,
    onNavigate: (ChronicleDto?) -> Unit,
    onOrderChange: (ChronicleOrder) -> Unit,
    onDelete:(ChronicleDto)->Unit
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = searchText, onValueChange = { text ->
                    onSearchTextChanged(text)
                }, modifier = Modifier.fillMaxWidth(), placeholder = {
                    Text(text = "Search")
                }, trailingIcon = {
                    Icon(imageVector = Icons.Default.Search, "", modifier = Modifier.clickable {
                        onSearchClick()
                    })
                })
            OrderSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp),
                chronicleOrder = screenState.chronicleOrder,
                onOrderChange = {
                    onOrderChange(it)
                }
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                if (screenState.isSearching) {
                    item {
                        Text(text = "Searching...", color = Color.White, fontSize = 22.sp)
                    }
                }
                items(screenState.chronicles) { chronicle ->
                    ChronicleListItem(
                        chronicleDto = chronicle,
                        onItemClick = {
                            onChronicleClick(chronicle)
                        },
                        onDeleteClick = {
                            onDelete(chronicle)
                        }
                    )
                    Divider(color = Color.Black)
                }
            }
        }
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 20.dp),
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
            onClick = {
                onAddItemClick()
            })
        {
            Icon(imageVector = Icons.Default.Edit, contentDescription = "")
        }

        if (screenState.isLoading) {
            CircularProgressIndicator()
        }

        val lifecycleOwner = LocalLifecycleOwner.current

        LaunchedEffect(key1 = lifecycleOwner.lifecycle) {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                onResume()
            }
        }

        ObserveAsEvents(flow = events) { event ->
            when (event) {
                ListScreenNavigationEvents.NavigateToAddScreen -> {
                    onNavigate(null)
                }

                is ListScreenNavigationEvents.NavigateToUpdateScreen -> {
                    onNavigate(event.chronicleDto)
                }
            }
        }
    }
}
