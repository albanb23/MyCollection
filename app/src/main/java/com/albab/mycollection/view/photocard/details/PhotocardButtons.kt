package com.albab.mycollection.view.photocard.details

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.albab.mycollection.R
import com.albab.mycollection.config.util.ImageConverter
import com.albab.mycollection.config.util.PhotocardStatus
import com.albab.mycollection.domain.model.Photocard
import com.albab.mycollection.view.ui.theme.pastel_blue
import com.albab.mycollection.view.ui.theme.pastel_green
import com.albab.mycollection.view.ui.theme.pastel_orange
import com.albab.mycollection.view.ui.theme.pastel_pink
import com.albab.mycollection.view.ui.theme.pastel_purple
import com.albab.mycollection.view.ui.theme.pastel_red
import com.albab.mycollection.view.ui.theme.pastel_yellow
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun RotateButtonsAnimation(
    photocard: Photocard,
    updateStatus: (Photocard) -> Unit,
    onClose: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var start by remember { mutableStateOf(false) }
    val bitmap = ImageConverter.base64ToBitmap(photocard.image)

    LaunchedEffect(Unit) {
        start = true
    }
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(CircleShape)
                .size(220.dp)
        )
        FABAnimated(
            degrees = 270F,
            start = start,
            disabled = photocard.status == PhotocardStatus.RESERVED,
            onClick = {
                photocard.status =
                    if (photocard.status != PhotocardStatus.RESERVED) PhotocardStatus.RESERVED else null
                updateStatus(photocard)
            },
            color = pastel_pink,
            icon = R.drawable.ic_bookmark
        )
        FABAnimated(
            degrees = 180F,
            start = start,
            disabled = photocard.status == PhotocardStatus.PAID,
            onClick = {
                photocard.status =
                    if (photocard.status != PhotocardStatus.PAID) PhotocardStatus.PAID else null
                updateStatus(photocard)
            },
            color = pastel_blue,
            icon = R.drawable.ic_money
        )
        FABAnimated(
            degrees = 225F,
            start = start,
            disabled = photocard.status == PhotocardStatus.SENT,
            onClick = {
                photocard.status =
                    if (photocard.status != PhotocardStatus.SENT) PhotocardStatus.SENT else null
                updateStatus(photocard)
            },
            color = pastel_purple,
            icon = R.drawable.ic_envelope
        )
        FABAnimated(
            degrees = 135F,
            start = start,
            disabled = photocard.status == PhotocardStatus.RECEIVED,
            onClick = {
                photocard.status =
                    if (photocard.status != PhotocardStatus.RECEIVED) PhotocardStatus.RECEIVED else null
                updateStatus(photocard)
            },
            color = pastel_green,
            icon = R.drawable.ic_check
        )
        FABAnimated(
            degrees = 45F,
            start = start,
            onClick = onEdit,
            color = pastel_orange,
            icon = R.drawable.ic_edit
        )
        FABAnimated(
            degrees = 0F,
            start = start,
            onClick = onDelete,
            color = pastel_red,
            icon = R.drawable.ic_delete
        )
        FloatingActionButton(
            onClick = onClose,
            backgroundColor = pastel_yellow,
            modifier = Modifier
                .padding(top = 350.dp)
                .size(60.dp)
                .align(Alignment.Center)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_close),
                contentDescription = "Close",
                modifier = Modifier.padding(14.dp)
            )
        }
    }
}

@Composable
fun FABAnimated(
    degrees: Float,
    start: Boolean,
    disabled: Boolean = false,
    onClick: () -> Unit,
    icon: Int,
    color: Color = MaterialTheme.colors.secondary,
    contentColor: Color = contentColorFor(color),
) {
    val rotation by animateFloatAsState(
        targetValue = if (start) degrees else 0F,
        tween(1400, easing = FastOutSlowInEasing)
    )
    val x = (cos(Math.toRadians(rotation.toDouble())) * 200f).toFloat()
    val y = (sin(Math.toRadians(rotation.toDouble())) * 200f).toFloat()
    FloatingActionButton(
        onClick = onClick,
        backgroundColor = color,
        contentColor = contentColor,
        modifier = Modifier
            .size(60.dp)
            .graphicsLayer {
                translationX = x
                translationY = y
            }
            .alpha(if (disabled) 0.5f else 1f)
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = "Icon",
            modifier = Modifier.padding(14.dp)
        )
    }
}