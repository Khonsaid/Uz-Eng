package uz.gita.latizx.uz_eng.data.mapper

import android.database.Cursor
import uz.gita.latizx.uz_eng.data.model.DictionaryModel
import uz.gita.latizx.uz_eng.data.model.WordDetailModel
import uz.gita.latizx.uz_eng.data.source.local.room.entity.DictionaryEntity
import uz.gita.latizx.uz_eng.data.source.local.room.entity.WordDetailEntity
import uz.gita.latizx.uz_eng.data.source.remote.dto.WordDetailDto

object Mapper {
    fun Cursor.toDictionaryModel(): DictionaryModel =
        DictionaryModel(
            id = this.getInt(this.getColumnIndexOrThrow("id")),
            english = this.getString(this.getColumnIndexOrThrow("english")),
            uzbek = this.getString(this.getColumnIndexOrThrow("uzbek")),
            type = this.getString(this.getColumnIndexOrThrow("type")),
            transcript = this.getString(this.getColumnIndexOrThrow("transcript")),
            countable = this.getString(this.getColumnIndexOrThrow("countable")),
            isFavourite = this.getInt(this.getColumnIndexOrThrow("is_favourite")),
            from = "",
            date = System.currentTimeMillis(),
        )

    fun DictionaryEntity.toDictionaryModel(): DictionaryModel =
        DictionaryModel(
            id = this.id,
            english = this.english,
            uzbek = this.uzbek,
            type = this.type,
            transcript = this.transcript,
            countable = this.countable,
            isFavourite = this.isFavourite,
            from = "",
            date = System.currentTimeMillis(),
        )

    fun WordDetailEntity.toWordDetailModel(): WordDetailModel = WordDetailModel(
        meanings = meanings,
        phonetic = phonetic,
        word = word
    )

    fun WordDetailDto.toWordDetailEntity(): WordDetailEntity = WordDetailEntity(
        meanings = meanings?.map { it.toMeaningModel() },
        phonetic = phonetic,
        word = word
    )
}