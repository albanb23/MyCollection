package com.albab.mycollection.domain.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.albab.mycollection.domain.model.CollectionParent
import kotlinx.coroutines.flow.Flow

@Dao
interface CollectionParentDao {
    @Transaction
    @Query("select * from Collection where collection_id=:parentId")
    fun getCollectionsByParent(parentId: String): Flow<List<CollectionParent>>

    @Transaction
    @Query("select exists (select * from Collection where collection_parent_id=:collectionId)")
    suspend fun hasChild(collectionId: String): Boolean
}