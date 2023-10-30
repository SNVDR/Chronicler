package com.snvdr.chronicler.presentation.chronicle_details_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.NotificationAdd
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Destination
fun ChronicleDetailsScreen(chronicleId: Long, navigator: DestinationsNavigator, viewModel:ChronicleDetailsViewModel = hiltViewModel()) {
    val state = viewModel.screenState
    val scrollState = rememberScrollState()
    val testState by viewModel.testState.collectAsState()
    val isEnabled by remember {
        mutableStateOf(!viewModel.screenState.isLoading)
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray)
                .verticalScroll(state = scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.1f)
                    .background(Color.LightGray)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "navigate back",
                    tint = Color.Red,
                    modifier = Modifier
                        .size(45.dp)
                        .padding(start = 10.dp)
                        .clickable {
                            navigator.popBackStack()
                        }
                )
                Icon(
                    imageVector = Icons.Default.NotificationAdd,
                    contentDescription = "create notification",
                    tint = Color.Green,
                    modifier = Modifier
                        .size(45.dp)
                        .padding(end = 10.dp)
                )
            }
            TextField(
                value = testState.title,
                onValueChange = {
                    viewModel.onEvent(ChronicleDetailsEvents.TitleChanged(it))
                },
                textStyle = LocalTextStyle.current.copy(fontSize = 28.sp),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    containerColor = Color.Gray
                ),
                placeholder = { Text("Title", fontSize = 28.sp) },
                modifier = Modifier.fillMaxWidth(0.97f)
            )
            TextField(
                value = testState.content?:"",
                onValueChange = {
                    viewModel.onEvent(ChronicleDetailsEvents.ContentChanged(it))
                },
                textStyle = LocalTextStyle.current.copy(fontSize = 23.sp),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    containerColor = Color.Gray
                ),
                placeholder = { Text("Chronicle", fontSize = 23.sp) },
                modifier = Modifier.fillMaxWidth(0.97f)
            )

            Button(enabled = isEnabled,onClick = {
                //viewModel.updateChronicle(id = chronicleId)
                viewModel.getChronicleById(id = chronicleId)
            }) {
                Text(text = "UPDATE ME", fontSize = 24.sp)
            }
        }
        if (state.isLoading){
            CircularProgressIndicator()
        }
    }
}