package uz.gita.latizx.uz_eng.data.mapper

import android.R.attr.type
import android.database.Cursor
import uz.gita.latizx.uz_eng.data.model.DictionaryModel

object Mapper {
    fun Cursor.toDictionaryModel(): DictionaryModel =
        DictionaryModel(
            id =  this.getInt(this.getColumnIndexOrThrow("id")),
            english =  this.getString(this.getColumnIndexOrThrow("english")),
            uzbek =  this.getString(this.getColumnIndexOrThrow("uzbek")),
            type =  this.getString(this.getColumnIndexOrThrow("type")),
            transcript =  this.getString(this.getColumnIndexOrThrow("transcript")),
            countable =  this.getString(this.getColumnIndexOrThrow("countable")),
            isFavourite =  this.getInt(this.getColumnIndexOrThrow("is_favourite")),
            from = "",
            date = System.currentTimeMillis(),
        )
}