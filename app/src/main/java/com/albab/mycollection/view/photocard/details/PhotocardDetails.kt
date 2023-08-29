package com.albab.mycollection.view.photocard.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.albab.mycollection.config.util.ImageConverter
import com.albab.mycollection.config.util.PhotocardStatus
import com.albab.mycollection.domain.model.Photocard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotocardDetails(
    photocard: Photocard,
    updateItem: (Photocard) -> Unit
) {
    val bitmap = ImageConverter.base64ToBitmap(photocard.image)
    var title by remember { mutableStateOf(photocard.title) }
    var description: String? by remember { mutableStateOf(photocard.description) }
    var status: PhotocardStatus? by remember { mutableStateOf(photocard.status) }
    var titleError by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.7f)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .padding(16.dp)
                .align(Alignment.BottomEnd)
        ) {
            TextField(
                value = title,
                onValueChange = {
                    title = it
                },
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                textStyle = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = description ?: "",
                onValueChange = { description = it },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                textStyle = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.fillMaxWidth()
            )
            StatusSelector(
                status = status,
                onStatusChanged = { s -> status = s },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
fun StatusSelector(
    status: PhotocardStatus?,
    onStatusChanged: (PhotocardStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        FloatingActionButton(
            onClick = { onStatusChanged(PhotocardStatus.RESERVED) }
        ) {
            Icon(
                imageVector = Icons.Filled.Bookmark,
                contentDescription = null
            )
        }
        FloatingActionButton(
            onClick = { onStatusChanged(PhotocardStatus.PAID) }
        ) {
            Icon(
                imageVector = Icons.Filled.Paid,
                contentDescription = null
            )
        }
        FloatingActionButton(
            onClick = { onStatusChanged(PhotocardStatus.SENT) }
        ) {
            Icon(
                imageVector = Icons.Filled.LocalShipping,
                contentDescription = null
            )
        }
        FloatingActionButton(
            onClick = { onStatusChanged(PhotocardStatus.RECEIVED) }
        ) {
            Icon(
                imageVector = Icons.Filled.Done,
                contentDescription = null
            )
        }
    }
}