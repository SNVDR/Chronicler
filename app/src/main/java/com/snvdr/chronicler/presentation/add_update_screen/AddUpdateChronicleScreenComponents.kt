package com.snvdr.chronicler.presentation.add_update_screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.snvdr.chronicler.theme.ChroniclerTheme
import com.snvdr.chronicler.utils.ObserveAsEvents
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


@Destination(navArgsDelegate = AddUpdateChronicleScreenArgs::class)
@Composable
fun AddUpdateScreenWrapper(
    navigator: DestinationsNavigator,
    navArgs: AddUpdateChronicleScreenArgs
) {
    val viewModel = hiltViewModel<AddUpdateChronicleViewModel>()
    val screenState by viewModel.screenState.collectAsState()
    val events = viewModel.navigationEventsChannelFlow
    AddUpdateScreen(
        screenState = screenState,
        events = events,
        onScreenEvent = { event ->
            viewModel.onEvent(event = event)
        }, onNavBack = {
            navigator.popBackStack()
        })
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "DefaultPreviewDark"
)
@Composable
fun AddUpdateScreenPreview() {
    val state = AddUpdateChronicleState(
        title = "Preview title",
        content = "Preview content"
    )
    val events :Flow<AddUpdateScreenNavigationEvents> = flow {

    }
    ChroniclerTheme {
        AddUpdateScreen(
            screenState = state,
            events = events,
            onScreenEvent = {

            },
            onNavBack = {

            }
        )
    }

}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "DefaultPreviewLight"
)
@Composable
fun ChroniclesDetailsPreviewL() {
    val state = AddUpdateChronicleState(
        title = "Preview title",
        content = "Preview content"
    )
    val events :Flow<AddUpdateScreenNavigationEvents> = flow {

    }
    ChroniclerTheme {
        AddUpdateScreen(
            screenState = state,
            events = events,
            onScreenEvent = {

            },
            onNavBack = {

            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddUpdateScreen(
    screenState: AddUpdateChronicleState,
    events: Flow<AddUpdateScreenNavigationEvents>,
    onScreenEvent: (AddUpdateChronicleEvents) -> Unit,
    onNavBack:()->Unit
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = scrollState)

        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "navigate back",
                    modifier = Modifier
                        .size(60.dp)
                        .padding(start = 10.dp)
                        .clickable {
                            onNavBack()
                        },
                    tint = MaterialTheme.colorScheme.primaryContainer
                )
                Icon(
                    imageVector = Icons.Default.NotificationAdd,
                    contentDescription = "create notification",
                    modifier = Modifier
                        .size(60.dp)
                        .padding(end = 10.dp),
                    tint = MaterialTheme.colorScheme.primaryContainer
                )
            }
            TextField(
                value = screenState.title,
                onValueChange = {
                    onScreenEvent(AddUpdateChronicleEvents.TitleChanged(it))
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
                    onScreenEvent(AddUpdateChronicleEvents.ContentChanged(it))
                },
                textStyle = LocalTextStyle.current.copy(fontSize = 23.sp),
                placeholder = { Text("Chronicle", fontSize = 23.sp) },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    containerColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth(0.97f)
            )
        }
        Button(
            onClick = {
                onScreenEvent(AddUpdateChronicleEvents.AddOrUpdate)
            },
            modifier = Modifier
                .padding(bottom = 15.dp)
                .align(Alignment.BottomCenter)
                .fillMaxWidth(0.8f)
        ) {

            Text(text = "Save or update note")
        }

        ObserveAsEvents(flow = events){event->
            when(event){
                AddUpdateScreenNavigationEvents.NavigateBack ->{
                    onNavBack()
                }
            }
        }
    }
}