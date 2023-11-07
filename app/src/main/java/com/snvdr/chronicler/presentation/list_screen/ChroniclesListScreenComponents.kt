@file:OptIn(ExperimentalMaterial3Api::class)

package com.snvdr.chronicler.presentation.list_screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.snvdr.chronicler.domain.NChronicleOrder
import com.snvdr.chronicler.domain.NOrderType
import com.snvdr.chronicler.presentation.destinations.AddUpdateScreenWrapperDestination
import com.snvdr.chronicler.utils.ObserveAsEvents
import kotlinx.coroutines.flow.Flow

@Composable
@Destination(start = true)
fun ChronicleListScreenWrapper(navigator: DestinationsNavigator) {
    val viewModel = hiltViewModel<MainScreenViewModel>()
    val searchText by viewModel.searchText.collectAsState()
    val screenState by viewModel.chroniclesListScreenState
    val events = viewModel.listScreenNavigationEventsChannelFlow

    ChronicleListScreen(
        screenState = screenState,
        events = events,
        searchText = searchText,
        onAddItemClick = {
            viewModel.navigateToAnotherScreen(null)
        }, onChronicleClick = {
            viewModel.navigateToAnotherScreen(it)
        }, onSearchTextChanged = {
            viewModel.onSearchTextChange(it)
        }, onSearchClick = {
            viewModel.searchChroniclesByQuery()
        }, onResume = {
            Log.d("ROOM_LOG", "onResume")
            viewModel.getAllChroniclesWithCustomQuery()
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
        })
}

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
    onOrderChange: (NChronicleOrder) -> Unit
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
                onOrderChange ={
                    onOrderChange(it)
                }
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                if (screenState.isSearchingChronicle) {
                    item {
                        Text(text = "Searching...", color = Color.White, fontSize = 22.sp)
                    }
                }
                items(screenState.chronicles) { chronicle ->
                    ChronicleListItem(chronicleDto = chronicle) {
                        onChronicleClick(chronicle)
                    }
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

        /*DisposableEffect(key1 = lifecycleOwner){
            val observer = LifecycleEventObserver{_,event->
                if (event == Lifecycle.Event.ON_RESUME){
                    onResume()
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer = observer)

            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }*/

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

@Composable
fun ChronicleListItem(chronicleDto: ChronicleDto, onChronicleClick: () -> Unit) {
    var showDate by remember {
        mutableStateOf(false)
    }
    Row(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                onChronicleClick()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = chronicleDto.title,
            fontSize = 22.sp,
            color = Color.DarkGray,
            modifier = Modifier.padding(20.dp)
        )

        if (showDate) {
            Text(
                text = chronicleDto.date,
                fontSize = 15.sp,
                color = Color.Black,
                modifier = Modifier
                    .padding(20.dp)
                    .clickable {
                        showDate = false
                    }
            )
        } else {
            IconButton(onClick = {
                showDate = true
            }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "",
                    tint = Color.Black
                )
            }
        }

    }
}

@Composable
fun OrderSection(
    modifier: Modifier = Modifier,
    chronicleOrder: NChronicleOrder = NChronicleOrder.Date(
        NOrderType.Descending
    ),
    onOrderChange: (NChronicleOrder) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            DefaultRadioButton(
                text = "Title",
                selected = chronicleOrder is NChronicleOrder.Title,
                onSelect = { onOrderChange(NChronicleOrder.Title(chronicleOrder.nOrderType)) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = "Date",
                selected = chronicleOrder is NChronicleOrder.Date,
                onSelect = { onOrderChange(NChronicleOrder.Date(chronicleOrder.nOrderType)) }
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            DefaultRadioButton(
                text = "Ascending",
                selected = chronicleOrder.nOrderType is NOrderType.Ascending,
                onSelect = {
                    onOrderChange(chronicleOrder.copy(NOrderType.Ascending))
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = "Descending",
                selected = chronicleOrder.nOrderType is NOrderType.Descending,
                onSelect = {
                    onOrderChange(chronicleOrder.copy(NOrderType.Descending))
                }
            )
        }
    }
}

@Composable
fun DefaultRadioButton(
    text: String,
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary,
                unselectedColor = MaterialTheme.colorScheme.onBackground
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, style = MaterialTheme.typography.bodyMedium)
    }
}