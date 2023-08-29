package com.albab.mycollection.domain.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.albab.mycollection.config.util.PhotocardStatus
import com.albab.mycollection.domain.model.Photocard
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotocardDao {

    @Insert(onConflict = REPLACE)
    suspend fun addPhotocard(photocard: Photocard)

    @Query("select * from Photocard where photocard_id = :pcId")
    fun getPhotocardById(pcId: String): Flow<Photocard>

    @Update
    suspend fun updatePhotocard(photocard: Photocard)

    @Delete
    suspend fun deletePhotocard(photocard: Photocard)
}