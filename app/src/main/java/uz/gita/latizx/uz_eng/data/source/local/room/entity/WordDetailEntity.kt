package uz.gita.latizx.uz_eng.data.source.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import uz.gita.latizx.uz_eng.data.model.MeaningModel

@Entity
data class WordDetailEntity(
    @PrimaryKey val id: Int? = null,
    val meanings: List<MeaningModel>?,
    val phonetic: String?,
    val word: String?
)