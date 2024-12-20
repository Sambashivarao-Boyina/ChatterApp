package com.example.chatterapp.presentation.authetication.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.example.chatterapp.ui.theme.Blue
import com.example.chatterapp.ui.theme.LightGray

@Composable
fun InputBox(
    value: String,
    onChange:(String) ->Unit,
    placeholder:String,
    painter: Painter,
    isError:Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = {
            onChange(it)
        },
        placeholder = {
            Text(text = placeholder)
        },
        leadingIcon = {
            Icon(painter = painter, contentDescription = null)
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(30.dp),
        colors = OutlinedTextFieldDefaults.colors().copy(
            unfocusedTextColor = Color.White,
            focusedTextColor = Color.White,
            focusedLeadingIconColor = Blue,
            focusedIndicatorColor = Blue,
            focusedPlaceholderColor = Blue,
            errorIndicatorColor = Color.Red,
            errorLeadingIconColor = Color.Red,
            errorPlaceholderColor = Color.Red,
            errorCursorColor = Color.Red,
            cursorColor = Blue
        ),
        singleLine = true,
        isError = isError
    )
}