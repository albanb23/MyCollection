package com.albab.mycollection.view.photocard.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.albab.mycollection.R
import com.albab.mycollection.config.util.ImageConverter
import com.albab.mycollection.config.util.PhotocardStatus
import com.albab.mycollection.domain.model.Photocard
import com.albab.mycollection.view.photocard.details.PhotocardDialog

@Composable
fun PhotocardList(
    photocards: List<Photocard>,
    updateStatus: (Photocard) -> Unit,
    deletePc: (Photocard) -> Unit,
    selected: Boolean,
    onPCSelected: (Boolean, Photocard) -> Unit
) {
    if (photocards.isNotEmpty()) {
        LazyVerticalGrid(columns = GridCells.Adaptive(80.dp)) {
            items(photocards) { pc ->
                PhotocardItem(
                    photocard = pc,
                    updateStatus = { updateStatus(it) },
                    selected = selected,
                    onPCSelected = onPCSelected,
                    deletePc = deletePc
                )
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = stringResource(id = R.string.no_photocards_yet),
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.Center)
            )
        }
    }
}

@Composable
fun PhotocardItem(
    photocard: Photocard,
    updateStatus: (Photocard) -> Unit,
    deletePc: (Photocard) -> Unit,
    selected: Boolean,
    onPCSelected: (Boolean, Photocard) -> Unit
) {
    val bitmap = ImageConverter.base64ToBitmap(photocard.image)
    var expanded by remember { mutableStateOf(false) }
    var pcSelected by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(8.dp)) {
        Box {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(120.dp)
                        .width(80.dp)
                        .alpha(if (photocard.status == PhotocardStatus.RECEIVED) 0.4f else 1f)
                        .clickable {
                            if (selected) {
                                pcSelected = !pcSelected
                                onPCSelected(pcSelected, photocard)
                            } else {
                                expanded = !expanded
                            }
                        }
                )

                if (selected) {
                    Icon(
                        imageVector = if (pcSelected) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp)
                    )
                } else {
                    if (photocard.status != null) {
                        val icon = when (photocard.status) {
                            PhotocardStatus.RESERVED -> Icons.Filled.Bookmark
                            PhotocardStatus.PAID -> Icons.Filled.Paid
                            PhotocardStatus.SENT -> Icons.Filled.LocalShipping
                            else -> null
                        }
                        if (icon != null) {
                            Box(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .align(Alignment.TopEnd)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(Color.White)
                                        .size(25.dp)
                                        .padding(4.dp)
                                ) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = null,
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        Text(
            text = photocard.title,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
    if (expanded) {
        PhotocardDialog(
            photocard = photocard,
            onDismiss = { expanded = false },
            onEdit = {
                expanded = false
            },
            onDelete = {
                deletePc(photocard)
                expanded = false
            },
            updateStatus = {
                updateStatus(it)
                expanded = false
            })
    }
}