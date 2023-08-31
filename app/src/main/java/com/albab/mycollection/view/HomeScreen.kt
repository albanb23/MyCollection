package com.albab.mycollection.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.albab.mycollection.view.collection.AddCollectionDialog
import com.albab.mycollection.view.collection.CollectionViewModel
import com.albab.mycollection.view.collection.list.CollectionListScreen
import com.albab.mycollection.view.common.MyTopApBar
import com.albab.mycollection.view.navigation.Home

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    collectionViewModel: CollectionViewModel,
    onCollectionClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddDialog by rememberSaveable { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = modifier
                .fillMaxSize()
        ) {
//            Text(
//                text = stringResource(id = R.string.my_collections),
//                style = MaterialTheme.typography.titleLarge,
//                modifier = Modifier.padding(16.dp)
//            )
            MyTopApBar(
                title = Home.title,
                topIcon = Icons.Default.Menu,
                topAction = {},
                optionAction = {}
            )
            CollectionListScreen(
                collectionViewModel = collectionViewModel,
                onCollectionClick = onCollectionClick
            )
        }
        AddCollectionFAB(
            onClickFAB = { showAddDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )
    }
    if (showAddDialog) {
        AddCollectionDialog(
            addCollection = { title, description, image ->
                collectionViewModel.addCollection(title, description, image, null)
            },
            onDismiss = { showAddDialog = false }
        )
    }

}

@Composable
fun AddCollectionFAB(onClickFAB: () -> Unit, modifier: Modifier = Modifier) {
    LargeFloatingActionButton(
        onClick = { onClickFAB() },
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null
        )
    }
}