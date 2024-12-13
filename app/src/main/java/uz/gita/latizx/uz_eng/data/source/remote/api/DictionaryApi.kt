package uz.gita.latizx.uz_eng.data.source.remote.api

import retrofit2.http.GET
import retrofit2.http.Path
import uz.gita.latizx.uz_eng.data.source.remote.dto.WordDetailDto

interface DictionaryApi {
    @GET("api/v2/entries/en/{word}")
    suspend fun getWordDetail(@Path("word") word:String):List<WordDetailDto>
}