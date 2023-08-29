package com.albab.mycollection.config.util

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color

data class Rectangle(
    val start: Offset,
    val end: Offset,
    val size: Size = Size((end.x - start.x), (end.y - start.y)),
    val color: Color = Color.Black.copy(alpha = 0.7f)
)
