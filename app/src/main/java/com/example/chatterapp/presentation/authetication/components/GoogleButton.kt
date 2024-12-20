package com.example.chatterapp.presentation.authetication.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.chatterapp.R

@Composable
fun GoogleButton(
    onClick:() -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .height(40.dp)
            .fillMaxWidth()
            .graphicsLayer {
                shadowElevation = 10f
                shape = RoundedCornerShape(30.dp)
                clip = true
            }
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.1f),
                        Color.White.copy(alpha = 0.05f)
                    )
                ),
                shape = RoundedCornerShape(30.dp)
            ),
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = Color.Transparent
        )
    ) {
        Image(painter = painterResource(R.drawable.google), contentDescription = null)
    }
}