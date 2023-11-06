package com.snvdr.chronicler.presentation.chronicle_details_screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Deck
import androidx.compose.material.icons.filled.NotificationAdd
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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


@Destination(navArgsDelegate = ChronicleDetailsScreenArgs::class)
@Composable
fun ChronicleDetailsScreenWrapper(
    navigator: DestinationsNavigator,
    navArgs: ChronicleDetailsScreenArgs
) {
    val viewModel = hiltViewModel<ChronicleDetailsViewModel>()
    val screenState by viewModel.screenState.collectAsState()
    ChronicleDetailsScreen(
        screenState = screenState,
        onBackArrowClick = { navigator.popBackStack() }) { event ->
        viewModel.onEvent(event = event)
    }

}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "DefaultPreviewDark"
)
@Composable
fun ChroniclesDetailsPreview() {
    val state = ChronicleDetailsScreenState(
        title = "Preview title",
        content = "Preview content"
    )
    ChroniclerTheme {
        ChronicleDetailsScreen(
            screenState = state,
            onBackArrowClick = { /*TODO*/ }
        ) {

        }
    }

}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "DefaultPreviewLight"
)
@Composable
fun ChroniclesDetailsPreviewL() {
    val state = ChronicleDetailsScreenState(
        title = "Preview title",
        content = "Preview content"
    )
    ChroniclerTheme {
        ChronicleDetailsScreen(
            screenState = state,
            onBackArrowClick = { /*TODO*/ }
        ) {

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChronicleDetailsScreen(
    screenState: ChronicleDetailsScreenState,
    onBackArrowClick: () -> Unit,
    onEvent: (ChronicleDetailsEvents) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = scrollState)
            .background(MaterialTheme.colorScheme.background),
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
                        onBackArrowClick()
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
}