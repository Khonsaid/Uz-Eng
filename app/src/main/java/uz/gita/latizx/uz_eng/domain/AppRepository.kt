package uz.gita.latizx.uz_eng.domain

import android.database.Cursor
import kotlinx.coroutines.flow.Flow

interface AppRepository {
    suspend fun getAllEng(): Cursor
    suspend fun getAllUzb(): Cursor
    suspend fun getAllFav(): Cursor
    suspend fun updateFav(id: Int, isFav: Int): Flow<Unit>
    suspend fun searchByEngWord(word: String): Cursor
    suspend fun searchByUzbWord(word: String): Cursor
}