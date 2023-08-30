package com.albab.mycollection.domain.model

import androidx.room.Embedded
import androidx.room.Relation

data class CollectionParent(
    @Embedded
    val parentCollection: Collection,
    @Relation(
        parentColumn = "collection_id",
        entityColumn = "collection_parent_id"
    )
    val collections: List<Collection>
)