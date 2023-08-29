package com.albab.mycollection.domain.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.albab.mycollection.domain.model.CollectionPhotocard
import kotlinx.coroutines.flow.Flow

@Dao
interface CollectionPhotocardDao {
    @Transaction
    @Query("select * from collection where collection_id = :collectionId order by modified")
    fun getCollectionPhotocard(collectionId: String): Flow<List<CollectionPhotocard>>
}