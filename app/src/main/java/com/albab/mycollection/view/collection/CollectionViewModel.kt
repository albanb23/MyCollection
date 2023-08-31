package com.albab.mycollection.view.collection

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albab.mycollection.domain.model.Collection
import com.albab.mycollection.domain.repository.CollectionRepository
import com.albab.mycollection.view.collection.details.CollectionDetailsUIState
import com.albab.mycollection.view.collection.list.CollectionsUIState
import com.albab.mycollection.view.collection.list.child.ChildCollectionsUIState
import com.albab.mycollection.view.common.SearchModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(
    private val collectionRepository: CollectionRepository
) : ViewModel() {

    val collectionListUiState: StateFlow<CollectionsUIState> =
        collectionRepository.collectionsList
            .map { collections -> CollectionsUIState.Success(collections) }
            .catch { CollectionsUIState.Error(it) }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                CollectionsUIState.Loading
            )

    var collectionUiState: StateFlow<CollectionDetailsUIState>? = null
    var childCollectionsUiState: StateFlow<ChildCollectionsUIState>? = null

    private var searchText: MutableStateFlow<String> = MutableStateFlow("")
    private var resultsFound: MutableStateFlow<Boolean> = MutableStateFlow(true)
    private var matchedResults: MutableStateFlow<List<Collection>> = MutableStateFlow(emptyList())

    private val _hasChild = MutableStateFlow(false)
    val hasChild: StateFlow<Boolean> get() = _hasChild

    private val _collectionsSelected = MutableStateFlow<ArrayList<Collection>>(arrayListOf())
    val collectionsSelected: StateFlow<ArrayList<Collection>> get() = _collectionsSelected

    val searchModelState = combine(
        searchText,
        matchedResults,
        resultsFound
    ) { text, results, resultsFound ->
        SearchModelState(
            text,
            results,
            resultsFound
        )
    }

    fun getCollectionById(collectionId: String) {
        collectionUiState = collectionRepository.getCollectionById(collectionId)
            .map { collection ->
                CollectionDetailsUIState.Success(collection)
            }
            .catch { CollectionDetailsUIState.Error(it) }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                CollectionDetailsUIState.Loading
            )
    }

    fun getCollectionsByParent(parentId: String) {
        childCollectionsUiState = collectionRepository.getCollectionsByParent(parentId)
            .map { child ->
                ChildCollectionsUIState.Success(child)
            }
            .catch { ChildCollectionsUIState.Error(it) }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                ChildCollectionsUIState.Loading
            )
    }

    fun collectionHasChild(collectionId: String) {
        viewModelScope.launch {
            _hasChild.value = collectionRepository.hasChild(collectionId)
        }
    }

    fun onCollectionSelected(selected: Boolean, collection: Collection) {
        if (selected) {
            _collectionsSelected.value.add(collection)
        } else {
            _collectionsSelected.value.remove(collection)
        }
    }

    fun deleteSelectedCollections() {
        for (collection in _collectionsSelected.value) {
            deleteCollection(collection)
        }
        _collectionsSelected.value.clear()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addCollection(name: String, description: String?, image: String?, parentId: Long?) {
        viewModelScope.launch {
            collectionRepository.addCollection(
                Collection(
                    name,
                    description,
                    image,
                    false,
                    LocalDateTime.now().toString(),
                    parentId
                )
            )
        }
    }

    fun deleteCollection(collection: Collection) {
        viewModelScope.launch {
            collectionRepository.deleteCollection(collection)
        }
    }

    fun updateCollection(collection: Collection) {
        viewModelScope.launch {
            collectionRepository.updateCollection(collection)
        }
    }

    fun onSearchTextChanged(text: String) {
        if (collectionListUiState.value is CollectionsUIState.Success) {
            val collections =
                (collectionListUiState.value as CollectionsUIState.Success).collections
            searchText.value = text
            if (text.isEmpty()) {
                matchedResults.value = collections
                resultsFound.value = true
                return
            }

            val results =
                collections.filter {
                    it.title.contains(text, true)
                }
            matchedResults.value = results
            resultsFound.value = results.isNotEmpty()

        }
    }
}