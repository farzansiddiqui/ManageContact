package com.siddiqui.myapplication.viewModel

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.siddiqui.myapplication.R
import com.siddiqui.myapplication.service.RecordingService
import kotlinx.coroutines.delay
import java.io.File

@Composable
fun RecordingListTab(viewModel: RecordingViewModel) {
    val context = LocalContext.current
    val recordings = viewModel.recordings

    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var currentlyPlaying by remember { mutableStateOf<String?>(null) }
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    var currentPosition by remember { mutableIntStateOf(0) }
    var duration by remember { mutableIntStateOf(0) }
    var isPlaying by remember { mutableStateOf(false) }

    // 🎵 Service intent
    val serviceIntent = remember { Intent(context, RecordingService::class.java) }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    // 🎵 BroadcastReceiver → listen to notification actions
    val receiver = remember {
        object : BroadcastReceiver() {
            override fun onReceive(c: Context?, intent: Intent?) {
                when (intent?.action) {
                    "PLAYER_PLAY" -> {
                        mediaPlayer?.start()
                        isPlaying = true
                    }
                    "PLAYER_PAUSE" -> {
                        mediaPlayer?.pause()
                        isPlaying = false
                    }
                    "PLAYER_SEEK" -> {
                        val pos = intent.getLongExtra("pos", 0).toInt()
                        mediaPlayer?.seekTo(pos)
                        sliderPosition = pos.toFloat()
                    }
                }
            }
        }
    }

    DisposableEffect(Unit) {
        val filter = IntentFilter().apply {
            addAction("PLAYER_PLAY")
            addAction("PLAYER_PAUSE")
            addAction("PLAYER_SEEK")
        }
        ContextCompat.registerReceiver(
            context,
            receiver,
            filter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )

        onDispose {
                try {
                    context.unregisterReceiver(receiver)
                }catch (e: IllegalArgumentException){
                    e.printStackTrace()
                }
        }

      //  await { context.unregisterReceiver(receiver) }
    }

    // 🎵 Update notification whenever state changes
    LaunchedEffect(currentlyPlaying, isPlaying, sliderPosition) {
        if (currentlyPlaying != null) {
            val updateIntent = Intent(context, RecordingService::class.java).apply {
                action = "ACTION_UPDATE"
                putExtra("IS_PLAYING", isPlaying)
                putExtra("POSITION", sliderPosition.toLong())
                putExtra("DURATION", duration.toLong())
            }
            ContextCompat.startForegroundService(context, updateIntent)
        }
    }

    // ⏯️ Handle Play/Pause logic
    val handlePlayPause: (String) -> Unit = { filePath ->
        if (currentlyPlaying == filePath && isPlaying) {
            // Pause
            mediaPlayer?.pause()
            currentPosition = mediaPlayer?.currentPosition ?: 0
            isPlaying = false
        } else {
            // If switching track → reset position
            if (currentlyPlaying != filePath) currentPosition = 0

            mediaPlayer?.stop()
            mediaPlayer?.release()

            mediaPlayer = MediaPlayer().apply {
                setDataSource(filePath)
                prepare()
                seekTo(currentPosition)
                start()
                setOnCompletionListener {
                    currentlyPlaying = null
                    isPlaying = false
                    release()
                    mediaPlayer = null
                }
            }

            duration = mediaPlayer?.duration ?: 0
            currentlyPlaying = filePath
            isPlaying = true
        }
    }

    // 🎵 Keep updating slider when playing
    LaunchedEffect(currentlyPlaying, isPlaying) {
        while (currentlyPlaying != null && isPlaying) {
            mediaPlayer?.let {
                sliderPosition = it.currentPosition.toFloat()
                currentPosition = it.currentPosition
            }
            delay(200)
        }
    }

    // 🎨 UI
    if (recordings.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No Records Found", fontSize = 18.sp)
        }
    } else {
        LazyColumn(Modifier.fillMaxSize()) {
            items(recordings) { filePath ->
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { handlePlayPause(filePath) }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = if (currentlyPlaying == filePath && isPlaying)
                                painterResource(R.drawable.baseline_pause_24)
                            else
                                painterResource(R.drawable.baseline_mic_24),
                            contentDescription = if (currentlyPlaying == filePath && isPlaying) "Pause" else "Play",
                            modifier = Modifier
                                .size(30.dp)
                                .clickable { handlePlayPause(filePath) }
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(File(filePath).name, style = MaterialTheme.typography.body1)
                    }

                    if (currentlyPlaying == filePath) {
                        Slider(
                            value = sliderPosition,
                            onValueChange = { sliderPosition = it },
                            onValueChangeFinished = {
                                mediaPlayer?.seekTo(sliderPosition.toInt())
                                currentPosition = sliderPosition.toInt()
                            },
                            valueRange = 0f..duration.toFloat(),
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
                        )
                    }

                    Divider()
                }
            }
        }
    }
}




@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RecordingListTabPreview() {
    RecordingListTab(viewModel())
}