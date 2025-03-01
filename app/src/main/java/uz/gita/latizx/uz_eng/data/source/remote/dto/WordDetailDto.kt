package uz.gita.latizx.uz_eng.data.source.remote.dto

data class WordDetailDto(
    val meanings: List<MeaningDto>?,
    val phonetic: String?,
    val word: String?
)