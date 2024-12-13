package uz.gita.latizx.uz_eng.domain

import android.database.Cursor
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.HttpException
import uz.gita.latizx.uz_eng.data.mapper.Mapper.toDictionaryModel
import uz.gita.latizx.uz_eng.data.mapper.Mapper.toWordDetailEntity
import uz.gita.latizx.uz_eng.data.mapper.Mapper.toWordDetailModel
import uz.gita.latizx.uz_eng.data.model.DictionaryModel
import uz.gita.latizx.uz_eng.data.model.WordDetailModel
import uz.gita.latizx.uz_eng.data.source.local.room.AppDatabase
import uz.gita.latizx.uz_eng.data.source.remote.api.DictionaryApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepositoryImpl @Inject constructor(
    private val database: AppDatabase,
    private val dictionaryApi: DictionaryApi
) : AppRepository {

    override suspend fun getAllEng(): Cursor = withContext(Dispatchers.IO) { database.getDictionaryDao().getAllEng() }
    override suspend fun getAllUzb(): Cursor = withContext(Dispatchers.IO) { database.getDictionaryDao().getAllUzb() }

    override suspend fun getAllFav(): Cursor = withContext(Dispatchers.IO) { database.getDictionaryDao().getAllFavourite() }

    override suspend fun updateFav(id: Int, isFav: Int): Flow<Unit> =
        flow { emit(database.getDictionaryDao().updateFav(id, isFav)) }.flowOn(
            Dispatchers.IO
        )

    override suspend fun searchByEngWord(word: String): Cursor =
        withContext(Dispatchers.IO) { database.getDictionaryDao().searchByEng(word) }

    override suspend fun searchByUzbWord(word: String): Cursor =
        withContext(Dispatchers.IO) { database.getDictionaryDao().searchByUzb(word) }

    override fun getWord(workId: Int): Flow<Result<DictionaryModel>> = flow {
        database.getDictionaryDao().getWordById(workId)
            .flowOn(Dispatchers.IO).catch { emit(Result.failure(it)) }.collect { emit(Result.success(it.toDictionaryModel())) }
    }

    override fun getWordDetail(wordId: Int): Flow<Result<List<WordDetailModel>>> = flow {
        try {
            database.getDictionaryDao().getWordById(wordId)
                .collect { emitAll(getWord(it.english.toString())) }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    private fun getWord(word: String): Flow<Result<List<WordDetailModel>>> = flow {
        val wordDetailDao = database.getWordDetailDao()
        val wordDetail = wordDetailDao.getWordDetail(word).map { it.toWordDetailModel() }
        emit(Result.success(wordDetail))
        try {
            val remoteWordDetail = dictionaryApi.getWordDetail(word)
            wordDetailDao.deleteWordDetail(remoteWordDetail.mapNotNull { it.word })
            wordDetailDao.insertWordDetail(remoteWordDetail.map { it.toWordDetailEntity() })
        } catch (e: HttpException) {
            emit(Result.failure(Throwable("Oops, something went wrong! $e")))
        } catch (e: IOException) {
            emit(Result.failure(Throwable("Couldn't reach server, check your internet connection $e")))
        }

        val newWordInfo = wordDetailDao.getWordDetail(word).map { it.toWordDetailModel() }
        emit(Result.success(newWordInfo))
    }
}