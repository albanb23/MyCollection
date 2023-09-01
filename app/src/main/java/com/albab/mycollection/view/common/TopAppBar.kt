package com.albab.mycollection.view.common

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopApBar(
    title: String = "",
    topIcon: ImageVector,
    topAction: () -> Unit,
    optionAction: @Composable () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                textAlign = TextAlign.Center
            )
        },
        navigationIcon = {
            IconButton(onClick = topAction) {
                Icon(
                    imageVector = topIcon, contentDescription = "Back"
                )
            }
        },
        actions = { optionAction() }
//        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
//            containerColor = MaterialTheme.colorScheme.secondaryContainer
//        )
    )
}