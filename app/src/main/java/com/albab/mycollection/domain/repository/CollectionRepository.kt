package com.albab.mycollection.domain.repository

import com.albab.mycollection.domain.dao.CollectionDao
import com.albab.mycollection.domain.dao.CollectionParentDao
import com.albab.mycollection.domain.model.Collection
import com.albab.mycollection.domain.model.CollectionParent
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CollectionRepository @Inject constructor(
    private val collectionDao: CollectionDao,
    private val collectionParentDao: CollectionParentDao
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

    suspend fun updateCollection(collection: Collection) {
        try {
            collectionDao.updateCollection(collection)
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

    fun getCollectionsByParent(parentId: String): Flow<List<CollectionParent>> {
        try {
            return collectionParentDao.getCollectionsByParent(parentId)
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }

    suspend fun hasChild(collectionId: String): Boolean {
        try {
            return collectionParentDao.hasChild(collectionId)
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }

    fun getFavorites(): Flow<List<Collection>> {
        try {
            return collectionDao.getFavoritesCollections()
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }
}