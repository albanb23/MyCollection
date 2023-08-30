package com.albab.mycollection.view.photocard.details

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Publish
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.albab.mycollection.R
import com.albab.mycollection.config.util.ImageConverter
import com.albab.mycollection.domain.model.Photocard

@Composable
fun PhotocardDialog(
    photocard: Photocard,
    onDismiss: () -> Unit,
    updateStatus: (Photocard) -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        RotateButtonsAnimation(
            photocard = photocard,
            updateStatus = updateStatus,
            onClose = onDismiss,
            onEdit = onEdit,
            onDelete = onDelete
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPhotocardDialog(
    photocard: Photocard,
    onDismiss: () -> Unit,
    onUpdate: (String, String?) -> Unit
) {
    var title by remember { mutableStateOf(photocard.title) }
    var description: String? by remember { mutableStateOf(photocard.description) }
    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(shape = RoundedCornerShape(25.dp)) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(id = R.string.edit_photocard))
                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                    },
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
                    singleLine = true,
                    label = {
                        Text(text = stringResource(id = R.string.collection_title))
                    }
                )
                OutlinedTextField(
                    value = description ?: "",
                    onValueChange = { description = it },
                    modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
                    singleLine = true,
                    label = {
                        Text(text = stringResource(id = R.string.collection_desc))
                    }
                )
                Row(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(onClick = { onDismiss() }) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                    TextButton(onClick = {
                        onUpdate(title, description)
                        onDismiss()
                    }) {
                        Text(text = stringResource(id = R.string.accept))
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPhotocardDialog(
    addPhotocard: (String, String?, String) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var description: String? by remember { mutableStateOf(null) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var image by remember { mutableStateOf("") }
    var imageError by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        imageUri = uri
    }

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(shape = RoundedCornerShape(25.dp)) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(id = R.string.new_photocard))
                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                    },
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
                    singleLine = true,
                    label = {
                        Text(text = stringResource(id = R.string.collection_title))
                    }
                )
                OutlinedTextField(
                    value = description ?: "",
                    onValueChange = { description = it },
                    modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
                    singleLine = true,
                    label = {
                        Text(text = stringResource(id = R.string.collection_desc))
                    }
                )
                Row {
                    if (bitmap == null) {
                        FilledTonalButton(
                            onClick = { launcher.launch("image/*") },
                            border = if (imageError) BorderStroke(
                                1.dp,
                                MaterialTheme.colorScheme.error
                            ) else null,
                            colors = if (imageError) ButtonDefaults.filledTonalButtonColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.onErrorContainer
                            ) else ButtonDefaults.filledTonalButtonColors()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Publish, contentDescription = null)
                                Text(
                                    text = stringResource(id = R.string.load_image),
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )
                            }
                        }
                    } else {
                        Image(
                            bitmap = bitmap!!.asImageBitmap(), contentDescription = null,
                            modifier = Modifier
                                .height(50.dp)
                                .width(30.dp)
                                .clip(RoundedCornerShape(2.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                imageUri?.let {
                    bitmap = if (Build.VERSION.SDK_INT < 28) {
                        MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                    } else {
                        val source = ImageDecoder.createSource(context.contentResolver, it)
                        ImageDecoder.decodeBitmap(source)
                    }
                }
                bitmap?.let {
                    image = ImageConverter.bitmapToBase64(it)
                }

                Row(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(onClick = { onDismiss() }) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                    TextButton(onClick = {
                        if (image.isBlank()) {
                            imageError = true
                        } else {
                            addPhotocard(title, description, image)
                            onDismiss()
                        }
                    }) {
                        Text(text = stringResource(id = R.string.accept))
                    }
                }
            }
        }
    }
}