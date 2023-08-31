package com.albab.mycollection.view.collection.details

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.albab.mycollection.view.collection.CollectionViewModel
import com.albab.mycollection.view.collection.list.child.ChildCollectionsListScreen
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
    collectionViewModel.getCollectionById(collectionId)
    collectionViewModel.collectionHasChild(collectionId)
    val collectionHasChild by collectionViewModel.hasChild.collectAsState()
    if (collectionHasChild) {
        collectionViewModel.getCollectionsByParent(collectionId)
        ChildCollectionsListScreen(
            collectionViewModel = collectionViewModel,
            onCollectionClick = { childId ->
                navigateToCollection(childId)
            },
            parentId = collectionId.toLong(),
            onBackPressed = onBackPressed
        )
    } else {
        photocardViewModel.getPhotocardByCollection(collectionId)
        CollectionDetailsScreen(
            collectionViewModel,
            photocardViewModel,
            navigateToTemplate = { colId ->
                navigateToPCTemplate(colId)
            },
            onBackPressed = onBackPressed
        )
    }
}