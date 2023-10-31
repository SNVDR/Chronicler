package com.snvdr.chronicler.presentation.chronicle_details_screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@Destination(navArgsDelegate = ChronicleDetailsScreenArgs::class)
@Composable
fun ChronicleDetailsScreen(navigator: DestinationsNavigator,navArgs: ChronicleDetailsScreenArgs) {
    val viewModel = hiltViewModel<ChronicleDetailsViewModel>()
    val screenState by viewModel.screenState.collectAsState()

    ChronicleDetails(
        screenState = screenState,
        onBackArrowClick = { navigator.popBackStack() }) { event ->
        viewModel.onEvent(event = event)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChronicleDetails(
    screenState: ChronicleDetailsScreenState,
    onBackArrowClick: () -> Unit,
    onEvent: (ChronicleDetailsEvents) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
            .verticalScroll(state = scrollState),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.1f)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "navigate back",
                tint = Color.LightGray,
                modifier = Modifier
                    .size(45.dp)
                    .padding(start = 10.dp)
                    .clickable {
                        onBackArrowClick()
                    }
            )
            Icon(
                imageVector = Icons.Default.NotificationAdd,
                contentDescription = "create notification",
                tint = Color.LightGray,
                modifier = Modifier
                    .size(45.dp)
                    .padding(end = 10.dp)
            )
        }
        TextField(
            value = screenState.title,
            onValueChange = {
                onEvent(ChronicleDetailsEvents.TitleChanged(it))
            },
            textStyle = LocalTextStyle.current.copy(fontSize = 28.sp),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                containerColor = Color.Transparent
            ),
            placeholder = { Text("Title", fontSize = 28.sp) },
            modifier = Modifier.fillMaxWidth(0.97f)
        )
        TextField(
            value = screenState.content ?: "",
            onValueChange = {
                onEvent(ChronicleDetailsEvents.ContentChanged(it))
            },
            textStyle = LocalTextStyle.current.copy(fontSize = 23.sp),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                containerColor = Color.Transparent
            ),
            placeholder = { Text("Chronicle", fontSize = 23.sp) },
            modifier = Modifier.fillMaxWidth(0.97f)
        )

    }
}