package uz.gita.latizx.uz_eng.data.source.remote.dto

import uz.gita.latizx.uz_eng.data.model.MeaningModel

data class MeaningDto(
    val antonyms: List<String>?,
    val definitions: List<DefinitionDto>?,
    val partOfSpeech: String?,
    val synonyms: List<String>?
) {
    fun toMeaningModel(): MeaningModel = MeaningModel(
        antonyms = antonyms,
        definitions = definitions?.map { it.toDefinition() },
        partOfSpeech = partOfSpeech,
        synonyms = synonyms
    )
}