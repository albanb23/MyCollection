package com.albab.mycollection.view.collection.list.child

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
import com.albab.mycollection.view.common.Loading
import com.albab.mycollection.view.common.SearchModelState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChildCollectionsListScreen(
    collectionViewModel: CollectionViewModel,
    parentId: Long?,
    onCollectionClick: (String) -> Unit,
    onBackPressed: () -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState<ChildCollectionsUIState>(
        initialValue = ChildCollectionsUIState.Loading,
        key1 = lifecycle,
        key2 = collectionViewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            collectionViewModel.childCollectionsUiState?.collect { value = it }
        }
    }

    when (uiState) {
        is ChildCollectionsUIState.Success -> {
            val childCollections =
                (uiState as ChildCollectionsUIState.Success).collections[0].collections
            val searchModelState by collectionViewModel.searchModelState.collectAsState(
                initial = SearchModelState(
                    "",
                    childCollections
                )
            )
            val list = searchModelState.list.ifEmpty { childCollections }
//            SearchScreen(
//                searchText = searchModelState.searchText,
//                matchesFound = searchModelState.resultsFound,
//                onTextChanged = { collectionViewModel.onSearchTextChanged(it) },
//                onTextCleared = { collectionViewModel.onSearchTextChanged("") }) {
            ChildCollectionsList(
                collections = childCollections,
                addCollection = { title, description, image ->
                    collectionViewModel.addCollection(title, description, image, parentId)
                },
                onCollectionClick = onCollectionClick,
                onCollectionSelected = { selected, col ->
                    collectionViewModel.onCollectionSelected(selected, col)
                },
                deleteCollections = { collectionViewModel.deleteSelectedCollections() },
                onBackPressed = onBackPressed
            )
//            }
        }

        is ChildCollectionsUIState.Error -> {}
        is ChildCollectionsUIState.Loading -> {
            Loading()
        }
    }
}