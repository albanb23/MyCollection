package com.albab.mycollection.view.collection.list.child

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.albab.mycollection.R
import com.albab.mycollection.domain.model.Collection
import com.albab.mycollection.view.collection.AddCollectionDialog
import com.albab.mycollection.view.collection.list.CollectionItem
import com.albab.mycollection.view.common.MyTopApBar
import com.albab.mycollection.view.ui.theme.red

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChildCollectionsList(
    collections: List<Collection>,
    parent: Collection,
    addCollection: (String, String?, String?) -> Unit,
    onCollectionClick: (String) -> Unit,
    addToFavorite: (Boolean) -> Unit,
    onCollectionSelected: (Boolean, Collection) -> Unit,
    deleteCollections: () -> Unit,
    onBackPressed: () -> Unit
) {
    var showAddCollectionDialog by rememberSaveable { mutableStateOf(false) }
    var selected by rememberSaveable { mutableStateOf(false) }
    var favorite by rememberSaveable { mutableStateOf(parent.favorite) }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        MyTopApBar(
            topIcon = Icons.Default.ArrowBack,
            topAction = onBackPressed
        ) {
            Row {
                Spacer(modifier = Modifier.weight(1f))
                if (!selected) {
                    IconButton(onClick = {
                        favorite = !favorite
                        addToFavorite(favorite)
                    }) {
                        Icon(
                            imageVector = if (favorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (favorite) red else MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Button(
                    onClick = { selected = !selected }
                ) {
                    Text(
                        text = if (selected) stringResource(id = R.string.cancel) else stringResource(
                            id = R.string.select
                        ),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
        Text(
            text = parent.title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 16.dp)
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
                        modifier = Modifier.padding(8.dp),
                        selected = selected,
                        onCollectionSelected = onCollectionSelected
                    )
                }
            }
            if (selected) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = {
                            deleteCollections()
                            selected = false
                        },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete selected"
                        )
                    }
                }
            } else {
                FloatingActionButton(
                    onClick = { showAddCollectionDialog = true }, modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add collection")
                }
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