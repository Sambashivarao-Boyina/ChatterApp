package com.example.chatterapp.presentation.authetication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.chatterapp.presentation.navGraph.auth.AuthNavGraph
import com.example.chatterapp.ui.theme.Blue

@Composable
fun AuthNavigatorScreen() {
    Scaffold {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Blue,
                            Color.Black,
                            Color.Black,
                            Color.Black, Color.Black, Blue
                        ),
                        start = Offset.Zero,
                        end = Offset.Infinite
                    )
                )
                .padding(it)
        ) {
            item {
                AuthNavGraph()
            }
        }
    }
}