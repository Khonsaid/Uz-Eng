package uz.gita.latizx.uz_eng.util

import com.google.gson.Gson
import java.lang.reflect.Type

class GsonParser(private val gson: Gson) : JsonParser {
    override fun <T> fromJson(json: String, type: Type): T? = gson.fromJson(json, type)

    override fun <T> toJson(obj: T, type: Type): String? = gson.toJson(obj, type)
}