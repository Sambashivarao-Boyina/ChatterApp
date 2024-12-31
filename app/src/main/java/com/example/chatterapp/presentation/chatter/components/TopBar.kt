package com.example.chatterapp.presentation.chatter.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.chatterapp.ui.theme.Black
import com.example.chatterapp.ui.theme.Gray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    navigationBox :  @Composable () -> Unit = {}
) {
    TopAppBar(
        navigationIcon = {
            navigationBox()
        },
        title = {
            Text(text = title,
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold)
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Black,
            titleContentColor = Gray,
            navigationIconContentColor = Gray
        ),
        modifier = Modifier.height(100.dp)
            .padding(bottom = 10.dp)
    )

}