package com.siddiqui.myapplication.viewModel

import android.media.MediaPlayer
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.siddiqui.myapplication.R
import kotlinx.coroutines.delay
import java.io.File

@Composable
fun RecordingListTab(viewModel: RecordingViewModel) {
    val recordings = viewModel.recordings
    val interactionSource = remember { MutableInteractionSource() }
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var currentlyPlaying by remember { mutableStateOf<String?>(null) }
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    var currentPosition by remember { mutableIntStateOf(0) }
    var duration by remember { mutableIntStateOf(0) }
    var isPlaying by remember { mutableStateOf(false) }

    val handlePlayPause: (String) -> Unit = { filePath ->
        if (currentlyPlaying == filePath && isPlaying) {
            // Pause
            mediaPlayer?.pause()
            currentPosition = mediaPlayer?.currentPosition ?: 0
            isPlaying = false
        } else {
            // If switching file → reset position
            if (currentlyPlaying != filePath) currentPosition = 0

            // Stop previous
            mediaPlayer?.stop()
            mediaPlayer?.release()

            // Start / resume
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

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    // Slider updater
    LaunchedEffect(currentlyPlaying, isPlaying) {
        while (currentlyPlaying != null && isPlaying) {
            mediaPlayer?.let {
                sliderPosition = it.currentPosition.toFloat()
                currentPosition = it.currentPosition
            }
            delay(200)
        }
    }

    if (recordings.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No Records Found", fontSize = 18.sp)
        }
    } else {
        LazyColumn(Modifier.fillMaxSize()) {
            items(recordings) { filePath ->
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
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) { handlePlayPause(filePath) }
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



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RecordingListTabPreview() {
    RecordingListTab(viewModel())
}