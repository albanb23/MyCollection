package com.albab.mycollection.view.collection.details

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.albab.mycollection.R
import com.albab.mycollection.config.util.PhotocardStatus
import com.albab.mycollection.domain.model.Collection
import com.albab.mycollection.domain.model.Photocard
import com.albab.mycollection.view.collection.AddCollectionDialog
import com.albab.mycollection.view.common.MyTopApBar
import com.albab.mycollection.view.photocard.PhotocardViewModel
import com.albab.mycollection.view.photocard.details.AddPhotocardDialog
import com.albab.mycollection.view.photocard.list.PhotocardList

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CollectionDetails(
    collection: Collection,
    photocardViewModel: PhotocardViewModel,
    photocards: List<Photocard>,
    navigateToTemplate: (String) -> Unit,
    addCollection: (String, String?, String?) -> Unit,
    onBackPressed: () -> Unit
) {
    var showAddPhotocardDialog by rememberSaveable { mutableStateOf(false) }
    var showAddCollectionDialog by rememberSaveable { mutableStateOf(false) }
    var selected by rememberSaveable { mutableStateOf(false) }
    val showAll by photocardViewModel.showAll.collectAsState()

    val photocardsFiltered = photocards.filter { pc -> pc.status != PhotocardStatus.RECEIVED }

    Column(Modifier.fillMaxSize()) {
        MyTopApBar(
            title = null,
            titleString = collection.title,
            topIcon = Icons.Default.ArrowBack,
            topAction = onBackPressed,
            optionAction = {
                Row {
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = { photocardViewModel.showAllClicked() }) {
                        Icon(
                            imageVector = if (showAll) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Show/Hide received"
                        )
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
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = collection.title,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
                PhotocardList(
                    photocards = if (showAll) photocards else photocardsFiltered,
                    updatePhotocard = {
                        photocardViewModel.updatePhotocard(it)
                    },
                    selected = selected,
                    onPCSelected = { pcSelected, pc ->
                        photocardViewModel.onPCSelected(pcSelected, pc)
                    },
                    deletePc = {
                        photocardViewModel.deletePhotocard(it)
                    }
                )
            }
            if (!selected) {
                ButtonsLayout(
                    showCollection = collection.collectionParentId == null && photocards.isEmpty(),
                    onAddCollection = { showAddCollectionDialog = true },
                    onAddItem = { showAddPhotocardDialog = true },
                    onAddTemplate = {
                        navigateToTemplate("${collection.collectionId}")
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                )
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = {
                            photocardViewModel.deleteSelectedPhotocards()
                            selected = false
                        },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete selected")
                    }
                }
            }
        }
    }

    if (showAddPhotocardDialog) {
        AddPhotocardDialog(
            addPhotocard = { title, description, image ->
                photocardViewModel.addPhotocard(
                    title,
                    description,
                    image,
                    collection.collectionId!!
                )
            }
        ) { showAddPhotocardDialog = false }
    }

    if (showAddCollectionDialog) {
        AddCollectionDialog(
            addCollection = addCollection,
            onDismiss = { showAddCollectionDialog = false }
        )
    }
}

@Composable
fun ButtonsLayout(
    showCollection: Boolean,
    onAddCollection: () -> Unit,
    onAddItem: () -> Unit,
    onAddTemplate: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (showCollection) {
            FloatingActionButton(onClick = { onAddCollection() }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add collection")
            }
        }
        FloatingActionButton(onClick = { onAddItem() }) {
            Icon(imageVector = Icons.Default.Image, contentDescription = "Add item")
        }
        FloatingActionButton(onClick = { onAddTemplate() }) {
            Icon(imageVector = Icons.Default.PhotoLibrary, contentDescription = "Add template")
        }
    }
}