package com.siddiqui.myapplication.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun CustomCircularProgressBar(
    percentage:Float,
    radius: Float = 50f,
    strokeWidth:Float = 32f,
    color: Color = Color.Blue,
    backgroundColor:Color = Color.LightGray

){

    Box(

        modifier = Modifier.size((radius * 2).dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.size((radius * 2).dp)
        ){
            drawCircle(
                color = backgroundColor,
                radius = radius,
                style = Stroke(width = strokeWidth)
            )
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = 360 * percentage,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }
        Text(
            text = "${(percentage * 100).toInt()}%"
        )
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun ShowCustomPreView(){
    CustomCircularProgressBar(percentage = 0.6f)
}