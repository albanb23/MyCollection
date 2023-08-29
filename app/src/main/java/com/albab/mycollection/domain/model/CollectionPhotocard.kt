package com.albab.mycollection.domain.model

import androidx.room.Embedded
import androidx.room.Relation

data class CollectionPhotocard(
    @Embedded
    val collection: Collection,
    @Relation(
        parentColumn = "collection_id",
        entityColumn = "collection_parent_id",
    )
    val photocards: List<Photocard>
)
