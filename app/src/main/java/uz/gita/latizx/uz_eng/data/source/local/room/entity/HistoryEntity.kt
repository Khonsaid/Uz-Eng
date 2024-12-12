package uz.gita.latizx.uz_eng.data.source.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val english: String?,
    val type: String?,
    val transcript: String?,
    val uzbek: String?,
    val countable: String?,
    @ColumnInfo("is_favourite")
    val isFavourite: Int?,
    val fom: String?,
    val date: Long?
)