package com.albab.mycollection.view.collection.list

import android.graphics.Bitmap
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomStart
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.albab.mycollection.R
import com.albab.mycollection.config.util.ImageConverter
import com.albab.mycollection.domain.model.Collection
import com.albab.mycollection.view.collection.CollectionViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CollectionList(
    viewModel: CollectionViewModel,
    collections: List<Collection>,
    onCollectionClick: (String) -> Unit
) {
    if (collections.isNotEmpty()) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(collections, key = { "${it.collectionId}" }) { item ->
                val dismissState = rememberDismissState()
                if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                    viewModel.deleteCollection(item)
                }
                SwipeToDismiss(
                    state = dismissState,
                    directions = setOf(DismissDirection.EndToStart),
                    dismissThresholds = { direction ->
                        FractionalThreshold(if (direction == DismissDirection.EndToStart) 0.1f else 0.05f)
                    },
                    background = {
                        val color by animateColorAsState(
                            when (dismissState.targetValue) {
                                DismissValue.Default -> Transparent
                                else -> MaterialTheme.colorScheme.error
                            }
                        )
                        val scale by animateFloatAsState(
                            if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Delete collection",
                                tint = White,
                                modifier = Modifier
                                    .padding(vertical = 8.dp, horizontal = 16.dp)
                                    .align(CenterEnd)
                                    .scale(scale)
                            )
                        }
                    }
                ) {
                    CollectionItem(
                        collection = item,
                        onCollectionClick = onCollectionClick,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = stringResource(id = R.string.no_collections_yet),
                modifier = Modifier
                    .padding(16.dp)
                    .align(Center)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionItem(
    collection: Collection,
    onCollectionClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    onCollectionSelected: ((Boolean, Collection) -> Unit)? = null
) {
    var collectionSelected by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxWidth()) {
        Card(
            onClick = {
                if (selected && onCollectionSelected != null) {
                    collectionSelected = !collectionSelected
                    onCollectionSelected(collectionSelected, collection)
                } else {
                    onCollectionClick("${collection.collectionId}")
                }
            },
            shape = RoundedCornerShape(25.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .height(200.dp)
            ) {
                val bitmap: Bitmap? = collection.image?.let { ImageConverter.base64ToBitmap(it) }
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.padding(bottom = 50.dp)
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(White)
                        .align(BottomStart)
                        .padding(16.dp)
                ) {
                    Text(text = collection.title, style = MaterialTheme.typography.titleMedium)
                    Text(text = collection.description ?: "")
                }
            }
        }
        if (selected) {
            Icon(
                imageVector = if (collectionSelected) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = "Selected icon",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            )
        }
    }

}