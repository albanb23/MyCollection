package com.albab.mycollection.view.photocard.details

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.albab.mycollection.config.util.PhotocardStatus
import com.albab.mycollection.domain.model.Photocard
import kotlin.math.cos
import kotlin.math.sin

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
    LaunchedEffect(Unit) {
        start = true
    }
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        FABAnimated(
            degrees = 270F,
            start = start,
            onClick = {
                photocard.status = PhotocardStatus.RESERVED
                updateStatus(photocard)
            },
            icon = Icons.Filled.Bookmark
        )
        FABAnimated(
            degrees = 180F,
            start = start,
            onClick = {
                photocard.status = PhotocardStatus.PAID
                updateStatus(photocard)
            },
            icon = Icons.Filled.Paid
        )
        FABAnimated(
            degrees = 225F,
            start = start,
            onClick = {
                photocard.status = PhotocardStatus.SENT
                updateStatus(photocard)
            },
            icon = Icons.Filled.LocalShipping
        )
        FABAnimated(
            degrees = 135F,
            start = start,
            onClick = {
                photocard.status = PhotocardStatus.RECEIVED
                updateStatus(photocard)
            },
            icon = Icons.Filled.Done
        )
        FABAnimated(
            degrees = 0F,
            start = start,
            onClick = onEdit,
            icon = Icons.Filled.Edit
        )
        FABAnimated(
            degrees = 45F,
            start = start,
            onClick = onDelete,
            icon = Icons.Filled.Delete
        )

        //Button close
        FloatingActionButton(
            onClick = onClose,
            modifier = Modifier
                .padding(top = 350.dp)
                .size(60.dp)
                .align(Alignment.Center)
        ) {
            Icon(imageVector = Icons.Filled.Close, contentDescription = null)
        }
    }
}

@Composable
fun FABAnimated(
    degrees: Float,
    start: Boolean,
    onClick: () -> Unit,
    icon: ImageVector,
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
    ) {
        Icon(imageVector = icon, contentDescription = null)
    }
}