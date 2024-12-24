package com.example.chatterapp.presentation.chatter.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.chatterapp.R
import com.example.chatterapp.domain.model.FriendRequest
import com.example.chatterapp.domain.model.User


@Composable
fun RequestBox(
    request: FriendRequest,
    user: User
) {
    val color = if(request.status == "Pending") {
        Color.Yellow
    } else if (request.status == "Rejected") {
        Color.Red
    } else {
        Color.Green
    }

    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation =  8.dp,
            pressedElevation = 16.dp,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {  },
        colors = CardDefaults.cardColors().copy(
            containerColor = Color(0XFF1F1F1F)
        )
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .padding(top = 10.dp, bottom = 0.dp, start = 10.dp, end = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if(request.receiver.userProfile == null) {
                    Image(
                        painter = painterResource(R.drawable.default_profile),
                        contentDescription = null,
                        modifier = Modifier.size(70.dp)
                            .clip(CircleShape)
                    )
                } else {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(user.userProfile)
                            .placeholder(R.drawable.default_profile)
                            .error(R.drawable.default_profile)
                            .transformations(CircleCropTransformation())
                            .build(),
                        contentDescription = null,
                        modifier = Modifier.size(70.dp)
                    )
                }

                Spacer(modifier = Modifier.width(20.dp))

                Column {
                    Text(
                        text = user.username,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge,
                        minLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = user.about,
                        style = MaterialTheme.typography.titleSmall,
                        minLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

            }

            Spacer(Modifier.height(10.dp))
            HorizontalDivider()
            Spacer(Modifier.height(10.dp))
            TextButton(
                onClick = {},
                enabled = false,
                colors = ButtonDefaults.buttonColors().copy(
                    contentColor = color,
                    containerColor = color.copy(alpha = 0.2f),
                    disabledContentColor  = color,
                    disabledContainerColor = color.copy(alpha = 0.2f)
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
                    .align(Alignment.End)
            ) {
                Text(
                    text = request.status
                )
            }
        }
    }
}
