package com.albab.mycollection.config.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.albab.mycollection.domain.dao.CollectionDao
import com.albab.mycollection.domain.dao.CollectionParentDao
import com.albab.mycollection.domain.dao.CollectionPhotocardDao
import com.albab.mycollection.domain.dao.PhotocardDao
import com.albab.mycollection.domain.model.Collection
import com.albab.mycollection.domain.model.Photocard

@Database(
    entities = [
        Collection::class,
        Photocard::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun collectionDao(): CollectionDao
    abstract fun photocardDao(): PhotocardDao
    abstract fun collectionPhotocardDao(): CollectionPhotocardDao
    abstract fun collectionParentDao(): CollectionParentDao
}