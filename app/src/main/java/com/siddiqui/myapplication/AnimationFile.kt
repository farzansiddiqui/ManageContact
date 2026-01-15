package com.siddiqui.myapplication

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch


/*
*Write code to implement an animation similar to the one shown in the video (Jetpack Compose / XML).
Write code to implement an animation (Jetpack Compose / XML)
*
* */
@Composable
fun ShowAnimation() {
    val lifeCycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        lifeCycleOwner.lifecycleScope.launch {
            lifeCycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {

                }
            }
        }
    }

    var showAnimation by remember { mutableStateOf(false) }
    val animatedColor by animateColorAsState(
        targetValue = if (showAnimation) Color.Red else Color.Blue,
        animationSpec = tween(durationMillis = 500)
    )

    Box(
        modifier = Modifier
            .padding(16.dp)
            .background(Color.Black)
            .animateContentSize()
            .height(if (showAnimation) 200.dp else 400.dp)
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                showAnimation = !showAnimation
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
                .background(animatedColor)
        ) {
            Text(
                text = if (showAnimation) "Expanded!" else "Click me!",
                color = Color.White,
                modifier = Modifier.align(Alignment.Center),
                fontSize = 24.sp
            )
        }
    }

}


@Composable
fun ButtonCheck(){
    val count = remember { mutableIntStateOf(0) }
    SideEffect {

    }


    Button(
        onClick = {
            count.intValue++
        }
    ) {
        Text(text = "count: ${count.intValue}")
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ShowPreviewData(){
    ShowAnimation()
}
