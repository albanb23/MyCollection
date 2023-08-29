package com.albab.mycollection.config.hilt

import android.content.Context
import androidx.room.Room
import com.albab.mycollection.config.db.AppDatabase
import com.albab.mycollection.domain.dao.CollectionDao
import com.albab.mycollection.domain.dao.CollectionPhotocardDao
import com.albab.mycollection.domain.dao.PhotocardDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    fun provideCollectionDao(appDatabase: AppDatabase): CollectionDao {
        return appDatabase.collectionDao()
    }

    @Provides
    fun providePhotocardDao(appDatabase: AppDatabase): PhotocardDao {
        return appDatabase.photocardDao()
    }

    @Provides
    fun provideCollectionPhotocardDao(appDatabase: AppDatabase): CollectionPhotocardDao {
        return appDatabase.collectionPhotocardDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "MyCollectionDB"
        ).fallbackToDestructiveMigration().build()
    }
}