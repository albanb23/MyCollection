package com.albab.mycollection.domain.model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class Collection(
    var title: String,
    var description: String?,
    var image: String?,
    var modified: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "collection_id")
    var collectionId: Long? = null

    @RequiresApi(Build.VERSION_CODES.O)
    var created: String = LocalDateTime.now().toString()
}
