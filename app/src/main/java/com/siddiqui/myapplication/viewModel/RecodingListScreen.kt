package com.siddiqui.myapplication.viewModel

import android.media.MediaPlayer
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.siddiqui.myapplication.R
import java.io.File
@Composable
fun RecordingListTab(viewModel: RecordingViewModel) {
    val context = LocalContext.current
    val recordings = viewModel.recordings
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var currentlyPlaying by remember { mutableStateOf<String?>(null) }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
        }
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(recordings) { filePath ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (currentlyPlaying == filePath) {
                            // If already playing → stop it
                            mediaPlayer?.stop()
                            mediaPlayer?.release()
                            mediaPlayer = null
                            currentlyPlaying = null
                        } else {
                            // Stop any previous playback
                            mediaPlayer?.stop()
                            mediaPlayer?.release()

                            mediaPlayer = MediaPlayer().apply {
                                setDataSource(filePath)
                                prepare()
                                start()
                                setOnCompletionListener {
                                    currentlyPlaying = null
                                    release()
                                    mediaPlayer = null
                                }
                            }
                            currentlyPlaying = filePath
                        }
                    }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = if (currentlyPlaying == filePath) painterResource(R.drawable.baseline_pause_24) else painterResource(R.drawable.baseline_mic_24),
                    contentDescription = "Play",
                    modifier = Modifier.size(30.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = File(filePath).name,
                    style = MaterialTheme.typography.body1
                )
            }
            Divider()
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RecordingListTabPreview() {
RecordingListTab(viewModel())
}