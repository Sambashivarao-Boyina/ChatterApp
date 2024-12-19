package com.example.chatterapp.presentation.onBoarding.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chatterapp.ui.theme.ChatterAppTheme
import com.example.chatterapp.ui.theme.Purple

@Composable
fun OnBoardingButton(
    icon:ImageVector,
    enable:Boolean,
    onClick:()->Unit,
) {
    IconButton(
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors().copy(
            containerColor = Purple,
            disabledContainerColor = Color.DarkGray
        ),
        modifier = Modifier
            .size(50.dp)
            .graphicsLayer { shadowElevation = 8.dp.toPx() },
        enabled =enable
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = Color.White)
    }
}


@Composable
@Preview(showBackground = true)
fun OnBoardingButtonPreview() {
    ChatterAppTheme {
        OnBoardingButton(
            icon = Icons.AutoMirrored.Filled.ArrowBack,
            enable = false
        ) {

        }
    }
}