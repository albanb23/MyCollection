package com.albab.mycollection.view.collection.list.child

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.albab.mycollection.domain.model.Collection
import com.albab.mycollection.view.collection.AddCollectionDialog
import com.albab.mycollection.view.collection.list.CollectionItem
import com.albab.mycollection.view.common.MyTopApBar

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChildCollectionsList(
    collections: List<Collection>,
    addCollection: (String, String?, String?) -> Unit,
    onCollectionClick: (String) -> Unit,
    onBackPressed: () -> Unit
) {
    var showAddCollectionDialog by rememberSaveable { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        MyTopApBar(
            title = null,
            topIcon = Icons.Default.ArrowBack,
            topAction = onBackPressed
        )
        Box(modifier = Modifier.fillMaxSize()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                items(collections, key = { "${it.collectionId}" }) { item ->
                    CollectionItem(
                        collection = item,
                        onCollectionClick = onCollectionClick,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
            FloatingActionButton(
                onClick = { showAddCollectionDialog = true }, modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add collection")
            }
        }
    }

    if (showAddCollectionDialog) {
        AddCollectionDialog(
            addCollection = addCollection,
            onDismiss = { showAddCollectionDialog = false },
        )
    }
}