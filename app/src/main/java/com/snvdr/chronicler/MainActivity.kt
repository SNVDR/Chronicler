package com.snvdr.chronicler

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import com.snvdr.chronicler.audio.AndroidAudioPlayer
import com.snvdr.chronicler.audio.AndroidAudioRecorder
import com.snvdr.chronicler.audio.AudioRecorder
import com.snvdr.chronicler.audio.db.AudioRecord
import com.snvdr.chronicler.audio.db.AudioRecordDao
import com.snvdr.chronicler.theme.ChroniclerTheme
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
 /*  private val recorder by lazy {
        AndroidAudioRecorder(applicationContext)
    }
    private val player by lazy {
        AndroidAudioPlayer(applicationContext)
    }
    private var audioFile: File? = null*/

    @Inject
    lateinit var audioRecorderDao: AudioRecordDao

    private lateinit var fileName: String
    private lateinit var dirPath: String
    private var recorder: MediaRecorder? = null
    private var recording = false
    private var onPause = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.RECORD_AUDIO),
            0
        )
        setContent {
            ChroniclerTheme {
                //DestinationsNavHost(navGraph = NavGraphs.root)
                val aList = audioRecorderDao.getAll()
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    LazyColumn{
                        items(aList){
                            AudioItem(it, onItemClick = {fp,fn->
                                val intent = Intent(this@MainActivity, PlayerActivity::class.java)
                                intent.putExtra("filepath", fp)
                                intent.putExtra("filename", fn)
                                startActivity(intent)
                            })
                        }
                    }
                }
                
            }
        }
    }
    @Composable
    private fun RecordAudioScreen(){
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {

            Button(onClick = {
               /* File(cacheDir, "audio.mp3").also {
                     recorder.start(it)
                     audioFile = it
                 }*/
                startRecord()
            }) {
                Text(text = "Start recording")
            }

            Button(onClick = {
                //recorder.stop()
                pauseRecording()
            }) {
                Text(text = "Pause recording")
            }

            Button(onClick = {
                //  player.playFile(audioFile?:return@Button)
                resumeRecording()
            }) {
                Text(text = "Resume recording")
            }

            Button(onClick = {
                // player.stop()
                stopRecording()
            }) {
                Text(text = "Stop recording")
            }

        }
    }
    @SuppressLint("SimpleDateFormat")
    private fun startRecord(){
        recording = true
        val pattern = "yyyy.MM.dd_hh.mm.ss"
        val simpleDateFormat = SimpleDateFormat(pattern)
        val date: String = simpleDateFormat.format(Date())

        dirPath = "${externalCacheDir?.absolutePath}/"
        fileName = "voice_record_${date}.mp3"

        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)

            setOutputFile(dirPath+fileName)
            try {
                prepare()
            } catch (e: IOException) {
                Log.e("AUDIO_TAG", "prepare() failed")
            }

            start()
        }
    }
    private fun pauseRecording(){
        onPause = true
        recorder?.apply {
            pause()
        }
    }
    private fun resumeRecording(){
        onPause = false
        recorder?.apply {
            resume()
        }
    }
    private fun stopRecording(){
        recording = false
        onPause = false
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
        audioRecorderDao.insert(AudioRecord(filename = fileName, filePath = dirPath, date = Date().time, duration = "1488"))
    }
}