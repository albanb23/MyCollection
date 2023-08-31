package com.albab.mycollection.view.collection.details

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.albab.mycollection.view.collection.CollectionViewModel
import com.albab.mycollection.view.collection.list.child.ChildCollectionsListScreen
import com.albab.mycollection.view.common.Loading
import com.albab.mycollection.view.photocard.PhotocardViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CollectionScreen(
    collectionId: String,
    collectionViewModel: CollectionViewModel,
    photocardViewModel: PhotocardViewModel,
    navigateToCollection: (String) -> Unit,
    navigateToPCTemplate: (String) -> Unit,
    onBackPressed: () -> Unit
) {
    collectionViewModel.collectionHasChild(collectionId)
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val collectionUiState by produceState<CollectionDetailsUIState>(
        initialValue = CollectionDetailsUIState.Loading,
        key1 = lifecycle,
        key2 = collectionViewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            collectionViewModel.collectionUiState?.collect { value = it }
        }
    }
    val collectionHasChild by collectionViewModel.hasChild.collectAsState()
    when (collectionUiState) {
        is CollectionDetailsUIState.Success -> {
            val collection = (collectionUiState as CollectionDetailsUIState.Success).collection
            if (collectionHasChild) {
                collectionViewModel.getCollectionsByParent(collectionId)
                ChildCollectionsListScreen(
                    collectionViewModel = collectionViewModel,
                    onCollectionClick = { childId ->
                        navigateToCollection(childId)
                    },
                    parent = collection,
                    onBackPressed = onBackPressed
                )
            } else {
                photocardViewModel.getPhotocardByCollection(collectionId)
                CollectionDetailsScreen(
                    collection = collection,
                    collectionViewModel,
                    photocardViewModel,
                    navigateToTemplate = { colId ->
                        navigateToPCTemplate(colId)
                    },
                    onBackPressed = onBackPressed
                )
            }
        }

        is CollectionDetailsUIState.Error -> {}
        is CollectionDetailsUIState.Loading -> {
            Loading()
        }
    }
}