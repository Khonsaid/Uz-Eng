package uz.gita.latizx.uz_eng.data.source.local.room.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.reflect.TypeToken
import uz.gita.latizx.uz_eng.data.model.MeaningModel
import uz.gita.latizx.uz_eng.util.JsonParser

@ProvidedTypeConverter
class Converters(
    private val jsonParser: JsonParser
) {
    @TypeConverter
    fun fromMeaningsJson(json: String): List<MeaningModel> =
        jsonParser.fromJson<ArrayList<MeaningModel>>(
            json,
            object : TypeToken<ArrayList<MeaningModel>>() {}.type
        )
            ?: emptyList()

    @TypeConverter
    fun toMeaningsJson(meanings: List<MeaningModel>): String =
        jsonParser.toJson(
            meanings, object : TypeToken<ArrayList<MeaningModel>>() {}.type
        ) ?: "[]"
}