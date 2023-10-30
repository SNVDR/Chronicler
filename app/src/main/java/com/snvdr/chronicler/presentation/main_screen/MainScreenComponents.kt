package com.snvdr.chronicler.presentation.main_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.snvdr.chronicler.domain.ChronicleDto
import com.snvdr.chronicler.presentation.destinations.ChronicleDetailsScreenDestination
import com.snvdr.chronicler.utils.Screen


@Composable
@Destination(start = true)
fun ChronicleMainScreen(navigator:DestinationsNavigator,mainViewModel: MainViewModel = hiltViewModel()) {
    val state = mainViewModel.mainScreenUIState
    var isEnabledButton by remember {
        mutableStateOf(true)
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(state.value.chronicles) {chronicle->
                    TestListItem(chronicleDto = chronicle){
                        navigator.navigate(ChronicleDetailsScreenDestination(chronicle.id))
                    }
                }
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = isEnabledButton,
                onClick = {
                    mainViewModel.addData()
                })
            {
                Text(text = "Add chronicle")
            }
        }
        if (state.value.isLoading) {
            isEnabledButton = false
            CircularProgressIndicator()
        } else {
            isEnabledButton = true
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestListItem(chronicleDto: ChronicleDto, onChronicleClick:()->Unit) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth().padding(10.dp).height(80.dp),
        onClick = {
            onChronicleClick()
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = chronicleDto.title,
                fontSize = 22.sp,
                color = Color.White,
                modifier = Modifier.padding(20.dp)
            )
        }
    }
}

@Composable
fun ChronicleListItem(chronicleDto: ChronicleDto) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(15.dp)
            .fillMaxWidth()
            .background(Color.LightGray, shape = RoundedCornerShape(15))

    ) {
        Text(
            text = chronicleDto.title, fontSize = 20.sp, color = Color.White,
            modifier = Modifier.padding(20.dp)
        )
    }
}