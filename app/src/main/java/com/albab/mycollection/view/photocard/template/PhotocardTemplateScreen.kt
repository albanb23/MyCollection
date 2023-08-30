package com.albab.mycollection.view.photocard.template

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.albab.mycollection.config.util.Line
import com.albab.mycollection.view.common.Loading
import com.albab.mycollection.view.photocard.PhotocardViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PhotocardTemplateScreen(
    collectionId: String,
    photocardViewModel: PhotocardViewModel,
    onSuccess: () -> Unit
) {
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var bitmapTemplate: Bitmap? = null
    val showLoading by photocardViewModel.showLoading.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        imageUri = uri
    }

    LaunchedEffect(Unit) {
        if (bitmapTemplate == null) {
            launcher.launch("image/*")
        }
    }
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        IconButton(onClick = onSuccess, modifier = Modifier.padding(16.dp)) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                tint = Color.White
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            imageUri?.let {
                bitmapTemplate = if (Build.VERSION.SDK_INT < 28) {
                    MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                } else {
                    val source = ImageDecoder.createSource(context.contentResolver, it)
                    ImageDecoder.decodeBitmap(source)
                }
            }
            bitmapTemplate?.let { image ->
                CropImage(
                    image = image,
                    collectionId = collectionId,
                    photocardViewModel = photocardViewModel,
                    onSuccess = onSuccess
                )
            }
            if (showLoading) {
                Loading()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CropImage(
    image: Bitmap,
    collectionId: String,
    photocardViewModel: PhotocardViewModel,
    onSuccess: () -> Unit
) {
    val rectangle by photocardViewModel.cropRectangle.collectAsState()
    val rectanglesCropped by photocardViewModel.rectanglesCropped.collectAsState()
    val lines = remember { mutableStateListOf<Line>() }
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var startDrawing by remember { mutableStateOf(false) }
    var width by remember { mutableStateOf(0) }
    var height by remember { mutableStateOf(0) }

    val state = rememberTransformableState { zoomChange, offsetChange, _ ->
        scale *= zoomChange
        offset += offsetChange
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Box(
            modifier = Modifier
                .clip(RectangleShape)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    translationX = offset.x
                    translationY = offset.y
                }
                .transformable(state = state)
                .fillMaxSize()
        ) {
            Image(
                bitmap = image.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .onGloballyPositioned {
                        width = it.size.width
                        height = it.size.height
                    }
            )
            if (startDrawing) {
                Canvas(modifier = Modifier
                    .transformable(state = state)
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            val line = Line(
                                start = change.position - dragAmount,
                                end = change.position
                            )
                            lines.add(line)
                        }
                    }) {
                    if (lines.isNotEmpty()) {
                        val start = lines[0].start
                        val end = lines[lines.size - 1].end
                        photocardViewModel.getRectangle(start, end)
                        rectangle?.let {
                            drawRect(
                                color = it.color,
                                topLeft = it.start,
                                size = it.size
                            )
                        }
                    }
                }
            }
            Canvas(modifier = Modifier.fillMaxSize()) {
                rectanglesCropped.forEach { rect ->
                    drawRect(
                        color = rect.color,
                        topLeft = rect.start,
                        size = rect.size
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(horizontal = 16.dp)
                .height(60.dp)
                .align(Alignment.BottomCenter),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Images cropped: ${rectanglesCropped.size}",
                color = Color.White,
                modifier = Modifier.weight(1f)
            )
            if (!startDrawing) {
                IconButton(onClick = {
                    photocardViewModel.restartRectangle()
                    startDrawing = true
                }) {
                    Icon(
                        imageVector = Icons.Default.Draw,
                        contentDescription = "Start drawing",
                        tint = Color.White
                    )
                }
                IconButton(onClick = {
                    photocardViewModel.saveImage(image, width, height)
                    photocardViewModel.saveAndCropPhotocards(collectionId = collectionId)
                    onSuccess()
                }) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = "Save and crop",
                        tint = Color.White
                    )
                }
            } else {
                IconButton(onClick = {
                    lines.clear()
                    startDrawing = false
                }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cancel",
                        tint = Color.White
                    )
                }
                if (rectangle != null) {
                    IconButton(onClick = {
                        lines.clear()
                        photocardViewModel.saveRectangle(rectangle!!)
                        startDrawing = false
                    }) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = "Save crop",
                            tint = Color.White
                        )
                    }
                }
            }
            IconButton(onClick = {
                if (startDrawing) {
                    lines.clear()
                    photocardViewModel.restartRectangle()
                } else {
                    photocardViewModel.clearLastRectangle()
                }
            }) {
                Icon(
                    imageVector = Icons.Default.Undo,
                    contentDescription = "Undo",
                    tint = Color.White
                )
            }
        }
    }
}