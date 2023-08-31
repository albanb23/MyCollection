package com.albab.mycollection.domain.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.albab.mycollection.domain.model.Collection
import kotlinx.coroutines.flow.Flow

@Dao
interface CollectionDao {

    @Query("select * from collection where collection_parent_id is null")
    fun getAllCollections(): Flow<List<Collection>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCollection(collection: Collection)

    @Delete
    suspend fun deleteCollection(collection: Collection)

    @Update
    suspend fun updateCollection(collection: Collection)

    @Query("select * from collection where collection_id = :collectionId")
    fun getCollectionById(collectionId: String): Flow<Collection>

    @Query("select * from Collection where favorite = 1")
    fun getFavoritesCollections(): Flow<List<Collection>>

}