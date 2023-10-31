package com.snvdr.chronicler.presentation.main_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.snvdr.chronicler.domain.ChronicleDto
import com.snvdr.chronicler.presentation.destinations.ChronicleDetailsScreenDestination

@Composable
@Destination(start = true)
fun ChronicleListScreen(navigator: DestinationsNavigator) {
    val viewModel = hiltViewModel<MainScreenViewModel>()
    val screenState by viewModel.chroniclesListScreenState
    ChronicleList(
        screenState = screenState,
        onAddItemClick = { viewModel.addData() }) { chronicle ->
        navigator.navigate(
            ChronicleDetailsScreenDestination(
                chronicleId = chronicle.id,
                date = chronicle.date
            )
        )
    }
}

@Composable
fun ChronicleList(
    screenState: ChroniclesListScreenState,
    onAddItemClick: () -> Unit,
    onChronicleClick: (ChronicleDto) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(screenState.chronicles) { chronicle ->
                    ChronicleListItem(chronicleDto = chronicle) {
                        onChronicleClick(chronicle)
                    }
                }
            }
            LargeFloatingActionButton(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                onClick = {
                    onAddItemClick()
                })
            {
                Text(text = "Add chronicle")
            }
        }
        if (screenState.isLoading) {
            CircularProgressIndicator()
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChronicleListItem(chronicleDto: ChronicleDto, onChronicleClick: () -> Unit) {
    var showDate by remember {
        mutableStateOf(false)
    }
    ElevatedCard(
        colors = CardDefaults.cardColors(
            MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .height(80.dp),
        onClick = {
            onChronicleClick()
        }
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
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
}