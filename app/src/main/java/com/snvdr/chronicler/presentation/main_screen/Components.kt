package com.snvdr.chronicler.presentation.main_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.snvdr.chronicler.domain.ChronicleDto


@Composable
fun ChronicleMainScreen(chronicleViewModel: ChronicleViewModel = hiltViewModel()) {
    val state = chronicleViewModel.chroniclesState
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {
        LazyColumn(
            modifier = Modifier
                .background(Color.Green)
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(state.value.chronicles) {
                ChronicleListItem(chronicleDto = it)
            }
        }

        Button(
            onClick = {
                chronicleViewModel.addData()
            }, modifier = Modifier
                .fillMaxWidth()
                .background(Color.Red)
        ) {
            Text(text = "Add chronicle")
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
            .background(Color.Magenta, shape = RoundedCornerShape(15))

    ) {
        Text(
            text = chronicleDto.title, fontSize = 20.sp, color = Color.White,
            modifier = Modifier.padding(20.dp)
        )
    }
}