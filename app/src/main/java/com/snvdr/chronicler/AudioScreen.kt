package com.snvdr.chronicler

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.snvdr.chronicler.audio.db.AudioRecord

@Composable
fun AudioListScreen(audioList: List<AudioRecord>) {
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn{
            items(audioList){
                AudioItem(it, onItemClick = {fp,fn->

                })
            }
        }
    }
}

@Composable
fun AudioItem(audioRecord: AudioRecord,onItemClick:(String,String)->Unit) {
    Card(modifier = Modifier
        .padding(20.dp)
        .fillMaxWidth()
        .clickable {
            onItemClick(audioRecord.filePath,audioRecord.filename)
        }) {
        Text(text = audioRecord.filename, Modifier.padding(10.dp))
    }
}

@Composable
fun PlayAudioScreen(onPlayPause:()->Unit, onStop:()->Unit){
    Column(Modifier.fillMaxSize()) {
        Button(onClick = {
            onPlayPause()
        }) {
            Text(text = "Play/Pause")
        }

        Button(onClick = {
            onStop()
        }) {
            Text(text = "Stop")
        }
    }
}