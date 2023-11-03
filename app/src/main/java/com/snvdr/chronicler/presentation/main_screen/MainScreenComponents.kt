@file:OptIn(ExperimentalMaterial3Api::class)

package com.snvdr.chronicler.presentation.main_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.snvdr.chronicler.domain.ChronicleDto
import com.snvdr.chronicler.presentation.destinations.ChronicleDetailsScreenWrapperDestination
import com.snvdr.chronicler.utils.OnLifecycleEvent

@Composable
@Destination(start = true)
fun ChronicleListScreen(navigator: DestinationsNavigator) {
    val viewModel = hiltViewModel<MainScreenViewModel>()
    val searchText by viewModel.searchText.collectAsState()
    val screenState by viewModel.chroniclesListScreenState

    ChronicleList(
        screenState = screenState,
        searchText = searchText,
        onAddItemClick = {
            viewModel.addData()
        }, onChronicleClick = {
            navigator.navigate(
                ChronicleDetailsScreenWrapperDestination(
                    chronicleId = it.id,
                    date = it.date
                )
            )
        }, onSearchTextChanged = {
            viewModel.onSearchTextChange(it)
        }, onSearchClick = {
            viewModel.searchChroniclesByQuery()
        }, onResume = {
            viewModel.getChronicles()
        })
}

@Composable
fun ChronicleList(
    screenState: ChroniclesListScreenState,
    searchText: String,
    onAddItemClick: () -> Unit,
    onChronicleClick: (ChronicleDto) -> Unit,
    onSearchTextChanged: (String) -> Unit,
    onSearchClick: () -> Unit,
    onResume:()->Unit
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
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

        OnLifecycleEvent { owner, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                       onResume()
                }
                else -> {  }
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