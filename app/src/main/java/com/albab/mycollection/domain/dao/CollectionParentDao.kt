package com.albab.mycollection.domain.dao

import androidx.room.Dao
import androidx.room.Query
import com.albab.mycollection.domain.model.Collection

@Dao
interface CollectionParentDao {

    @Query("select * from Collection where collection_parent_id=:parentId")
    fun getCollectionsByParent(parentId: Long): List<Collection>
}