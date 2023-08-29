package com.albab.mycollection.view.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.albab.mycollection.R

private val Domine = FontFamily(
    Font(R.font.domine_regular),
    Font(R.font.domine_medium, FontWeight.Medium),
    Font(R.font.domine_bold, FontWeight.Bold),
    Font(R.font.domine_semibold, FontWeight.SemiBold)
)

private val Caprasimo = FontFamily(
    Font(R.font.caprasimo_regular)
)

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = Domine
    ),
    displayMedium = TextStyle(
        fontFamily = Domine
    ),
    displaySmall = TextStyle(
        fontFamily = Domine
    ),
    headlineLarge = TextStyle(
        fontFamily = Domine,
        fontSize = 24.sp,
        textAlign = TextAlign.End,
        fontWeight = FontWeight.Bold
    ),
    headlineMedium = TextStyle(
        fontFamily = Domine,
        fontSize = 18.sp,
        textAlign = TextAlign.End
    ),
    headlineSmall = TextStyle(
        fontFamily = Domine,
        fontSize = 14.sp,
        textAlign = TextAlign.End
    ),
    titleLarge = TextStyle(
        fontFamily = Caprasimo,
        fontSize = 28.sp
    ),
    titleMedium = TextStyle(
        fontFamily = Caprasimo,
        fontSize = 22.sp
    ),
    titleSmall = TextStyle(
        fontFamily = Caprasimo,
        fontSize = 18.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = Domine
    ),
    bodyMedium = TextStyle(
        fontFamily = Domine
    ),
    bodySmall = TextStyle(
        fontFamily = Domine
    ),
    labelLarge = TextStyle(
        fontFamily = Domine
    ),
    labelMedium = TextStyle(
        fontFamily = Domine
    ),
    labelSmall = TextStyle(
        fontFamily = Domine,
        fontSize = 11.sp
    )
)