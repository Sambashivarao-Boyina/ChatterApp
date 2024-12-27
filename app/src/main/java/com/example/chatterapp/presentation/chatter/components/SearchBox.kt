package com.example.chatterapp.presentation.chatter.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.chatterapp.ui.theme.Blue
import com.example.chatterapp.ui.theme.DarkGray
import com.example.chatterapp.ui.theme.Gray
import com.example.chatterapp.ui.theme.LightGray

@Composable
fun SearchBox(
    value: String,
    onChange: (String) -> Unit,
    onSearch: ()-> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = "Search...",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )
        },
        trailingIcon = {
            IconButton(
                onClick = {
                    onSearch()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    modifier = Modifier.size(35.dp)

                )
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(40.dp),
        colors = OutlinedTextFieldDefaults.colors().copy(
            focusedContainerColor = DarkGray,
            unfocusedContainerColor = DarkGray,
            focusedIndicatorColor = Blue,
            unfocusedIndicatorColor = DarkGray,
            focusedTrailingIconColor = Blue,
            unfocusedTrailingIconColor = Gray,
            cursorColor = Blue,
            unfocusedPlaceholderColor = Gray,
            focusedPlaceholderColor = Gray,

        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch()
            },
        ),


    )
}