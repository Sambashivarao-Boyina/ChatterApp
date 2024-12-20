package com.example.chatterapp.presentation.onBoarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.chatterapp.ui.theme.Blue

@Composable
fun OnBoardingIndicator(
    size: Int,
    curPage: Int,
) {
    Row {
        repeat(size) { positon ->
            Box(
                modifier = Modifier
                    .width(35.dp)
                    .height(3.dp)
                    .background(
                        color = if (curPage == positon) Blue else Color.White
                    )
            )
            Spacer(modifier = Modifier.width(10.dp))
        }
    }
}