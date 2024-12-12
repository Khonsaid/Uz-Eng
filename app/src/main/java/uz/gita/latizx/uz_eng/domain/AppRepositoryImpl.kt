package uz.gita.latizx.uz_eng.domain

import android.database.Cursor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import uz.gita.latizx.uz_eng.data.source.local.room.AppDatabase
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AppRepositoryImpl @Inject constructor(
    private val database: AppDatabase
) : AppRepository {

    override suspend fun getAllEng(): Cursor = withContext(Dispatchers.IO) { database.getDictionaryDao().getAllEng() }
    override suspend fun getAllUzb(): Cursor = withContext(Dispatchers.IO) { database.getDictionaryDao().getAllUzb() }

    override suspend fun getAllFav(): Cursor = withContext(Dispatchers.IO) { database.getDictionaryDao().getAllFavourite() }

    override suspend fun updateFav(id: Int, isFav: Int): Flow<Unit> = flow { emit(database.getDictionaryDao().updateFav(id, isFav)) }

    override suspend fun searchByEngWord(word: String): Cursor =
        withContext(Dispatchers.IO) { database.getDictionaryDao().searchByEng(word) }

    override suspend fun searchByUzbWord(word: String): Cursor =
        withContext(Dispatchers.IO) { database.getDictionaryDao().searchByUzb(word) }


}