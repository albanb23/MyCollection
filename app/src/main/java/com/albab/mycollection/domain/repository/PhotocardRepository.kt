package com.albab.mycollection.domain.repository

import com.albab.mycollection.domain.dao.CollectionPhotocardDao
import com.albab.mycollection.domain.dao.PhotocardDao
import com.albab.mycollection.domain.model.CollectionPhotocard
import com.albab.mycollection.domain.model.Photocard
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PhotocardRepository @Inject constructor(
    private val photocardDao: PhotocardDao,
    private val collectionPhotocardDao: CollectionPhotocardDao
) {

    fun getPhotocardsByCollection(collectionId: String): Flow<List<CollectionPhotocard>> {
        try {
            return collectionPhotocardDao.getCollectionPhotocard(collectionId)
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }

    suspend fun addPhotocard(photocard: Photocard) {
        try {
            photocardDao.addPhotocard(photocard)
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }

    fun getPhotocardById(pcId: String): Flow<Photocard> {
        try {
            return photocardDao.getPhotocardById(pcId)
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }

    suspend fun updatePhotocard(photocard: Photocard) {
        try {
            photocardDao.updatePhotocard(photocard)
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }

    suspend fun deletePhotocard(photocard: Photocard) {
        try {
            photocardDao.deletePhotocard(photocard)
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }
}