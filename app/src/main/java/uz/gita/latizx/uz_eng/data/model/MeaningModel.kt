package uz.gita.latizx.uz_eng.data.model

data class MeaningModel(
    val antonyms: List<String>?,
    val definitions: List<DefinitionModel>?,
    val partOfSpeech: String?,
    val synonyms: List<String>?
)
