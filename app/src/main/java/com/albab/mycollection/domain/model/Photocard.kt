package com.albab.mycollection.domain.model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.albab.mycollection.config.util.PhotocardStatus
import java.time.LocalDateTime

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Collection::class,
            parentColumns = ["collection_id"],
            childColumns = ["collection_parent_id"],
            onDelete = CASCADE,
            onUpdate = CASCADE
        )
    ]
)
data class Photocard(
    var title: String,
    var description: String?,
    var image: String,
    var status: PhotocardStatus?,
    var modified: String,
    @ColumnInfo(name = "collection_parent_id")
    var collectionParentId: Long
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "photocard_id", index = true)
    var pcId: Long? = null

    @RequiresApi(Build.VERSION_CODES.O)
    var created: String = LocalDateTime.now().toString()
}
