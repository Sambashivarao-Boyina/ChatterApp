package com.example.chatterapp.presentation.authetication.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.chatterapp.R
import com.example.chatterapp.ui.theme.Blue

@Composable
fun PasswordInput(
    value:String,
    placeholder: String,
    onChange:(String) -> Unit,
    isError: Boolean = false
) {
    var showPassword by rememberSaveable {
        mutableStateOf(false)
    }
    OutlinedTextField(
        value = value,
        onValueChange = {
            onChange(it)
        },
        placeholder = {
            Text(text = placeholder)
        },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Lock, contentDescription = null)
        },
        trailingIcon ={
            IconButton(
                onClick = {
                    showPassword = !showPassword
                }
            ) {
                Icon(
                    painter = painterResource(if(showPassword) R.drawable.ic_visible else R.drawable.ic_unvisible),
                    contentDescription = null
                )
            }

        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(30.dp),
        colors = OutlinedTextFieldDefaults.colors().copy(
            unfocusedTextColor = Color.White,
            focusedTextColor = Color.White,
            focusedLeadingIconColor = Blue,
            focusedPlaceholderColor = Blue,
            focusedIndicatorColor = Blue,
            focusedTrailingIconColor = Blue,
            errorIndicatorColor = Color.Red,
            errorLeadingIconColor = Color.Red,
            errorCursorColor = Color.Red,
            errorTrailingIconColor = Color.Red,
            cursorColor = Blue
        ),
        isError = isError,
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(mask = '*'),
        singleLine = true
    )
}