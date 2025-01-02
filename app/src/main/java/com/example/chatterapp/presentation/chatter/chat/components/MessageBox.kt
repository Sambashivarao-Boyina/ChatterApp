package com.example.chatterapp.presentation.chatter.chat.components

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.min
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.chatterapp.R
import com.example.chatterapp.domain.model.Message
import com.example.chatterapp.presentation.navGraph.Route
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun MessageBox(
    message:Message,
    backGroundColor: Color,
    navController: NavHostController
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

            if(message.imageUrl != null) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(message.imageUrl)
                        .placeholder(R.drawable.default_profile)
                        .error(R.drawable.imageloadingerror)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier.size(300.dp)
                        .clickable {
                            navController.navigate(Route.ImageViewer.passImageUrl(message.imageUrl))
                        }
                )
            } else {
                Text(
                    text = message.message,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

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