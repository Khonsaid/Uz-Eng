package uz.gita.latizx.uz_eng.data.source.remote.dto

import uz.gita.latizx.dictionarydemo.data.source.remote.dto.MeaningDto

data class WordDetailDto(
    val meanings: List<MeaningDto>?,
    val phonetic: String?,
    val word: String?
)