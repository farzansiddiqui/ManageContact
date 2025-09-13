package com.siddiqui.myapplication.view


import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.siddiqui.myapplication.R
import com.siddiqui.myapplication.viewModel.RecordingViewModel
import kotlin.io.encoding.ExperimentalEncodingApi


@Composable
fun RecordAudio(viewModel: RecordingViewModel) {
    var isRecording by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }



    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (granted) {
                isRecording = true
                viewModel.startRecoding()
            } else {
                Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    )
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        contentAlignment = Alignment.Center) {

        val infiniteTransition = rememberInfiniteTransition(label = "pulse_animation")

        val scale by infiniteTransition.animateFloat(
            initialValue = 0.8f,
            targetValue = 1.2f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulse_scale"
        )

        Box(
            modifier = Modifier
                .size(72.dp)
                .scale(scale)
                .background(if (isRecording) Color.Red else MaterialTheme.colors.primary, CircleShape).
                clickable(
                    interactionSource = interactionSource,
                    indication = null,
                ) {

                    if (isRecording) {
                        viewModel.stopRecording()
                        isRecording = false
                    } else {
                        if (ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.RECORD_AUDIO
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            viewModel.startRecoding()
                            isRecording = true
                        } else {
                            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                        }
                    }
                }
        ){
            if (isRecording) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_pause_24),
                    contentDescription = "Stop Recording", Modifier.align(Alignment.Center),
                    tint = Color.White
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_mic_24),
                    contentDescription = "Start Recording", Modifier.align(Alignment.Center),
                    tint = Color.White
                )
            }
        }
    }

}





@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ShowRecordAudio() {
    RecordAudio(viewModel = viewModel())

}