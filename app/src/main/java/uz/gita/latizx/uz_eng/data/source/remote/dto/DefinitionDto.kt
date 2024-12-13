package uz.gita.latizx.uz_eng.data.source.remote.dto

import uz.gita.latizx.uz_eng.data.model.DefinitionModel

data class DefinitionDto(
    val antonyms: List<String>?,
    val definition: String?,
    val example: String?,
    val synonyms: List<String>?
) {
    fun toDefinition(): DefinitionModel = DefinitionModel(
        antonyms = antonyms,
        definition = definition,
        example = example,
        synonyms = synonyms
    )
}