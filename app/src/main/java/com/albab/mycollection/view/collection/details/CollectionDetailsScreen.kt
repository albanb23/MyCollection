package com.albab.mycollection.view.collection.details

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.albab.mycollection.view.collection.CollectionViewModel
import com.albab.mycollection.view.common.Loading
import com.albab.mycollection.view.photocard.PhotocardViewModel
import com.albab.mycollection.view.photocard.list.PhotocardListUIState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CollectionDetailsScreen(
    collectionViewModel: CollectionViewModel,
    photocardViewModel: PhotocardViewModel,
    navigateToTemplate: (String) -> Unit,
    onBackPressed: () -> Unit
) {
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

    val pcUIState by produceState<PhotocardListUIState>(
        initialValue = PhotocardListUIState.Loading,
        key1 = lifecycle,
        key2 = photocardViewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            photocardViewModel.photocardListUIState?.collect { value = it }
        }
    }

    when (collectionUiState) {
        is CollectionDetailsUIState.Success -> {
            when (pcUIState) {
                is PhotocardListUIState.Success -> {
                    CollectionDetails(
                        collection = (collectionUiState as CollectionDetailsUIState.Success).collection,
                        photocards = (pcUIState as PhotocardListUIState.Success).photocards[0].photocards,
                        photocardViewModel = photocardViewModel,
                        navigateToTemplate = navigateToTemplate,
                        onBackPressed = onBackPressed
                    )
                }

                is PhotocardListUIState.Error -> {}
                PhotocardListUIState.Loading -> {}
            }
        }

        is CollectionDetailsUIState.Error -> {}
        CollectionDetailsUIState.Loading -> {
            Loading()
        }
    }
}