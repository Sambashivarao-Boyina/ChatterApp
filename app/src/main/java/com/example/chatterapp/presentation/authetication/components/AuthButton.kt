package com.example.chatterapp.presentation.authetication.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.chatterapp.ui.theme.Blue

@Composable
fun AuthButton(
    text: String,
    onClick: () -> Unit,
    isLoading: Boolean
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                shadowElevation = 10f
                shape = RoundedCornerShape(30.dp)
                clip = true
            }
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Blue.copy(alpha = 1.0f),
                        Blue.copy(alpha = 0.35f)
                    )
                ),
                shape = RoundedCornerShape(30.dp)
            ),
        shape = RoundedCornerShape(30.dp),
        enabled = !isLoading,
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = Color.Transparent,
            contentColor = Color.White,
            disabledContainerColor = Blue,
            disabledContentColor = Color.Transparent,
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Text(text = text.uppercase(), fontWeight = FontWeight.ExtraBold)
        }
    }
}