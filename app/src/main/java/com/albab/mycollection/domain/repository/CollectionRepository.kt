package com.albab.mycollection.domain.repository

import com.albab.mycollection.domain.dao.CollectionDao
import com.albab.mycollection.domain.model.Collection
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CollectionRepository @Inject constructor(
    private val collectionDao: CollectionDao
) {

    val collectionsList: Flow<List<Collection>> = collectionDao.getAllCollections()

    suspend fun addCollection(collection: Collection) {
        try {
            collectionDao.addCollection(collection)
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }

    suspend fun deleteCollection(collection: Collection) {
        try {
            collectionDao.deleteCollection(collection)
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }

    fun getCollectionById(collectionId: String): Flow<Collection> {
        try {
            return collectionDao.getCollectionById(collectionId)
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }
}