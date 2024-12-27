package com.example.chatterapp.presentation.chatter.chat.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.min
import com.example.chatterapp.domain.model.Message
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun MessageBox(
    message:Message,
    backGroundColor: Color,
) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 300.dp)
                .background(
                    color = backGroundColor
                )
                .padding(15.dp)
        ) {
            Text(
                text = message.message,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = parseDate(message.createdAt),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}



@SuppressLint("NewApi")
fun parseDate(dateTimeString: String): String {

    val zonedDateTime = ZonedDateTime.parse(dateTimeString)

    // Format to extract only the date and time
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val formattedDateTime = zonedDateTime.toLocalDateTime().format(formatter)

    return formattedDateTime
}