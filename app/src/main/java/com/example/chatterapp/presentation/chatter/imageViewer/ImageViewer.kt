package com.example.chatterapp.presentation.chatter.imageViewer

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.chatterapp.R
import com.example.chatterapp.presentation.chatter.components.RequestBox
import com.example.chatterapp.presentation.chatter.components.TopBar
import com.example.chatterapp.presentation.chatter.received_requests.ReceivedRequestBox
import com.example.chatterapp.presentation.chatter.received_requests.ReceivedRequestEvent
import com.example.chatterapp.presentation.navGraph.Route
import com.example.chatterapp.ui.theme.Black
import com.example.chatterapp.ui.theme.Blue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL

@Composable
fun ImageViewer(
    imageUrl: String,
    navController: NavHostController
) {

    val context = LocalContext.current
    val folderName = stringResource(R.string.app_name)
    Scaffold(
        topBar = {
            TopBar(
                title = "Image",
                navigationBox = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                },
                actions = {
                    Row {
                        IconButton(
                            onClick = {

                                CoroutineScope(Dispatchers.Main).launch {
                                    saveImage(
                                        context = context,
                                        imageUrl = imageUrl,
                                        folderName = folderName
                                    )

                                }
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.download),
                                contentDescription = null,
                                modifier = Modifier.size(30.dp),
                                tint = Blue
                            )
                        }
                    }
                }
            )
        },

        containerColor = Black
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .placeholder(R.drawable.default_profile)
                    .error(R.drawable.imageloadingerror)
                    .build(),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth()

            )

        }
    }
}

suspend fun saveImage(context: Context, imageUrl: String, folderName: String): Boolean {
    withContext(Dispatchers.Main) {
        Toast.makeText(context, "Image Download Started", Toast.LENGTH_LONG).show()
    }
    return withContext(Dispatchers.IO) {
        try {
            val url = URL(imageUrl)
            val connection = url.openConnection()
            connection.connect()

            val inputStream: InputStream = connection.getInputStream()
            val fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Android 10+ (Scoped Storage)
                val values = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                    put(
                        MediaStore.Images.Media.RELATIVE_PATH,
                        "${Environment.DIRECTORY_PICTURES}/$folderName"
                    )
                    put(MediaStore.Images.Media.IS_PENDING, 1)
                }

                val resolver = context.contentResolver
                val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

                uri?.let {
                    resolver.openOutputStream(it).use { outputStream ->
                        inputStream.copyTo(outputStream!!)
                    }
                    values.clear()
                    values.put(MediaStore.Images.Media.IS_PENDING, 0)
                    resolver.update(uri, values, null, null)
                }
            } else {
                // Below Android 10
                val directory = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    folderName
                )
                if (!directory.exists()) {
                    directory.mkdirs()
                }

                val file = File(directory, fileName)
                FileOutputStream(file).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }

                // Notify media scanner to update the Gallery
                context.sendBroadcast(
                    android.content.Intent(android.content.Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                        .apply {
                            data = android.net.Uri.fromFile(file)
                        }
                )
            }

            inputStream.close()

            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Image saved successfully!", Toast.LENGTH_LONG).show()
            }
            true
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Failed to save image: ${e.message}", Toast.LENGTH_LONG)
                    .show()
            }
            false
        }
    }
}