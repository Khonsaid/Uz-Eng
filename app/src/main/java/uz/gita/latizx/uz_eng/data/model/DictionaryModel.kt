package uz.gita.latizx.uz_eng.data.model

data class DictionaryModel(
    val id: Int,
    val english: String?,
    val type: String?,
    val transcript: String?,
    val uzbek: String?,
    val countable: String?,
    var isFavourite: Int?,
    val from: String?,
    val date: Long?
)